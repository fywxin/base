package org.whale.system.filter;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingDeque;
import java.util.concurrent.atomic.AtomicLong;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.InitializingBean;
import org.whale.system.base.Iquery;
import org.whale.system.base.Page;
import org.whale.system.base.UserContext;
import org.whale.system.common.util.LangUtil;
import org.whale.system.common.util.PropertiesUtil;
import org.whale.system.common.util.Strings;
import org.whale.system.common.util.ThreadContext;
import org.whale.system.domain.Log;
import org.whale.system.jdbc.IOrmDao;
import org.whale.system.jdbc.filter.BaseDaoFilterWarpper;
import org.whale.system.jdbc.filter.impl.OrmExeContext;
import org.whale.system.jdbc.orm.entry.OrmTable;
import org.whale.system.rpc.LogRpc;

import com.alibaba.fastjson.JSON;

/**
 * 日志过滤收集器
 *
 * @author 王金绍
 * 2015年4月26日 下午3:43:15
 */
public class LogFilter<T extends Serializable,PK extends Serializable> extends BaseDaoFilterWarpper<T,PK> implements InitializingBean {
	
	private static final Logger logger = LoggerFactory.getLogger(LogFilter.class);
	
	// 队列
	private static final BlockingQueue<Log> LOG_QUEUE = new LinkedBlockingDeque<Log>();
   	// 等待时长
   	private long defaultWaitTime = 20000;
   	// 线程休眠时长
   	private long defaultSleepTime = 10000;
   	// 网络异常发生后，等待多少时长进行重试,单位毫秒
   	private long defaultNetErrorTryTime = 1000;
   	// 记录当前网络异常发生时间
   	private long currentNetErrorTime = 0;
   	// 队列长度
   	private int defaultQueueSize = 150;
   	//队列锁通知对象
   	private final Object notify = new Object();
   	//不记录日志数据
   	private boolean stopFlag = false;
   	//保存增删改操作日志
   	private boolean saveDllLog = true;
   	//保存查询日志
   	private boolean saveFindLog = true;
   	//是否打印sql调用结果
   	private boolean printRsStr = true;
   	//只保存异常记录，只针对存储到mysql关系型数据库时，可以开启此开关
   	private boolean onlyException = false;
   	//接口保存日志
	private LogRpc logRpc;
	//本项目的appId，应用日志统一存储时，用于区分属于不同的应用
	private String appId = null;
	//项目进程id，用于防止id重复
	private int pId = LangUtil.getPid();
	//自增序列
	private AtomicLong seq = new AtomicLong(0);
	
	
	private void saveLog(IOrmDao<T, PK> baseDao, Object rs){
		OrmTable ormTable = baseDao._getOrmTable();
		if(ormTable.getEntityName().equals("Log"))
			return ;
		
		OrmExeContext ormExeContext = (OrmExeContext)ThreadContext.getContext().get(ThreadContext.KEY_OPT_CONTEXT);
		if(ormExeContext == null){
			return ;
		}
		Log log = this.newLog();
		log.setCnName(ormTable.getTableCnName());
		log.setTableName(ormTable.getTableDbName());
		log.setOpt(ormExeContext.getMethodName());
		log.setArg(ormExeContext.getArg());
		log.setSqlStr(ormExeContext.getSql());
		log.setRs(rs);
	}
	
	
	public Log newLog(){
		int order = 1;
		
		Log log = new Log();
		Long time = System.currentTimeMillis();
		long seqNum = seq.getAndIncrement();
		if(seqNum == Long.MAX_VALUE){
			seq.set(0L);
		}
		//时间戳+机器ID+PID+计数器
		String id= time + pId+ this.getAppId()  + seqNum;
		log.setId(id);
		log.setAppId(this.getAppId());
		
		Long createTime = ThreadContext.getContext().getLong(ThreadContext.KEY_LOG_CREATE_TIME);
		if(createTime == null){
			createTime = System.currentTimeMillis();
			ThreadContext.getContext().put(ThreadContext.KEY_LOG_CREATE_TIME, createTime);
		}
		Long costTime = time-createTime;
		Long methodCostTime = costTime;
		
		//是否由网页入口
		boolean fromWeb = ThreadContext.getContext().getBoolean(ThreadContext.KEY_FROM_WEB) == null ? false : ThreadContext.getContext().getBoolean(ThreadContext.KEY_FROM_WEB).booleanValue();
		if(fromWeb){
			log.setUri(ThreadContext.getContext().getStr(ThreadContext.KEY_LOG_URI));
		}else{
			log.setUri(Thread.currentThread().getName());
		}
		
		Log prxLog = (Log)ThreadContext.getContext().get(ThreadContext.KEY_LOG_PREX);
		
		if(prxLog != null){
			order = prxLog.getCallOrder()+1;
			methodCostTime = time-createTime-prxLog.getCostTime();
			this.addLogRecvQueue(prxLog);
			if(order >= Integer.MAX_VALUE){
				order=1;
			}
		}
		log.setCreateTime(createTime);
		log.setCallOrder(order);
		log.setCostTime(costTime.intValue());
		log.setMethodCostTime(methodCostTime.intValue());
		log.setRsType(Log.RS_NORMAL);
		
		UserContext uc = (UserContext)ThreadContext.getContext().get(ThreadContext.KEY_USER_CONTEXT);
		if(uc != null){
			log.setUserName(uc.getUserName());
			log.setIp(uc.getIp());
		}
		
		ThreadContext.getContext().put(ThreadContext.KEY_LOG_PREX, log);
		return log;
	}
	

    public void setLogRpc(LogRpc logRpc) {
		this.logRpc = logRpc;
	}


	class ConsumeLogThread extends Thread {

        @Override
        public void run() {
            int waitTime = 0;
            while (!stopFlag) {
            	//System.out.println(LOG_QUEUE.size()+" | "+defaultQueueSize+" | "+ waitTime+" | "+defaultWaitTime);
                if (LOG_QUEUE.size() > defaultQueueSize || waitTime > defaultWaitTime) {
                    consumeRecvQueue(LOG_QUEUE, defaultQueueSize);
                    
                    if(waitTime > 0){
                        waitTime = 0;
                    }
                } else {
                    long startTime = System.currentTimeMillis();
                    waitForRecvQueue(defaultSleepTime);
                    waitTime += (System.currentTimeMillis() - startTime);
                }
            }
        }
    }

    private void waitForRecvQueue(long miliSeconds) {
        synchronized (notify) {
            try {
                notify.wait(miliSeconds);
            } catch (InterruptedException e) {
                logger.error(e.getMessage(), e);
            }
        }
    }

    private void consumeRecvQueue(BlockingQueue<Log> queue, int size) {
        List<Log> logs = new ArrayList<Log>();
        try {
            queue.drainTo(logs, size);
                    
            if (!logs.isEmpty()) {
                long interval = 0;
                
                if(currentNetErrorTime > 0){
                    interval = System.currentTimeMillis() - currentNetErrorTime;
                }
                
                if(interval == 0 || interval >= defaultNetErrorTryTime){
                	
                	List<Log> expLogs = new LinkedList<Log>();
                	for(Log log : logs){
                		if(this.onlyException){ //只保存异常日志，扔掉非异常日志对象
                			if(log.getRsType() != 1){
                				if(Strings.isBlank(log.getArgStr())){
    	                			log.setArgStr(JSON.toJSONString(log.getArg()));
    	                		}
    	                		if(printRsStr && Strings.isBlank(log.getRsStr())){
    	                			log.setRsStr(JSON.toJSONString(log.getRs()));
    	                		}
    	                		log.setArg(null);
    	                		log.setRs(null);
    	                		expLogs.add(log);
                			}
                		}else{//保存所有日志
                			if(Strings.isBlank(log.getArgStr())){
	                			log.setArgStr(JSON.toJSONString(log.getArg()));
	                		}
	                		if(printRsStr && Strings.isBlank(log.getRsStr())){
	                			log.setRsStr(JSON.toJSONString(log.getRs()));
	                		}
	                		log.setArg(null);
	                		log.setRs(null);
                		}
                	}
                	if(this.onlyException){
                		this.logRpc.save(expLogs);
                	}else{
                		this.logRpc.save(logs);
                	}
                    if(logger.isDebugEnabled()){
                        logger.debug("保存日志成功， 具体日志信息: {}", logs);
                    }
                    
                    if(interval > 0){
                        currentNetErrorTime = 0;
                    }
                }else{
                    printLoseLogs(logs, null);
                }
            }
        } catch (Throwable e) {
            printLoseLogs(logs, e);
        } finally {
            if (!logs.isEmpty()) {
                logs.clear();
            }
        }
    }
    
    private void printLoseLogs(List<Log> logs, Throwable e){
        if(e == null){
            logger.error("丢失推送日志 {}条", logs.size());
        }
        else{
            logger.error("丢失推送日志 {}条{}", logs.size(), e);
        }
    }

    public void addLogRecvQueue(Log log) {
        LOG_QUEUE.add(log);
        synchronized (notify) {
            notify.notifyAll();
        }
    }

    public void addLogRecvQueues(List<Log> logs) {
        LOG_QUEUE.addAll(logs);
        synchronized (notify) {
            notify.notifyAll();
        }
    }

    public long getDefaultWaitTime() {
        return defaultWaitTime;
    }

    public void setDefaultWaitTime(long defaultWaitTime) {
        this.defaultWaitTime = defaultWaitTime;
    }

    public int getDefaultQueueSize() {
        return defaultQueueSize;
    }

    public void setDefaultQueueSize(int defaultQueueSize) {
        this.defaultQueueSize = defaultQueueSize;
    }

    public long getDefaultSleepTime() {
        return defaultSleepTime;
    }

    public void setDefaultSleepTime(long defaultSleepTime) {
        this.defaultSleepTime = defaultSleepTime;
    }

    public long getDefaultNetErrorTryTime() {
        return defaultNetErrorTryTime;
    }

    public void setDefaultNetErrorTryTime(long defaultNetErrorTryTime) {
        this.defaultNetErrorTryTime = defaultNetErrorTryTime;
    }

	@Override
	public void afterPropertiesSet() throws Exception {
		Thread thread = new ConsumeLogThread();
		thread.setName("日志扫描处理线程");
		thread.setDaemon(true);
		thread.start();
		logger.info("ORM:启动日志处理线程...");
	}

	public void setStopFlag(boolean stopFlag) {
		logger.warn("ORM:设置日志后台扫描线程状态为 {}", stopFlag);
		this.stopFlag = stopFlag;
		if(this.stopFlag){
			UserContext uc = (UserContext)ThreadContext.getContext().get(ThreadContext.KEY_USER_CONTEXT);
			logger.error("ORM:设置日志后台扫描线程被停止！userId = {}", uc.getUserId());
		}
	}

	

	@Override
	public void afterSave(T obj, IOrmDao<T, PK> baseDao) {
		if(this.saveDllLog){
			this.saveLog(baseDao, null);
		}
	}

	@Override
	public void afterSaveBatch(List<T> objs, IOrmDao<T, PK> baseDao) {
		if(this.saveDllLog){
			this.saveLog(baseDao, null);
		}
	}

	@Override
	public void afterUpdate(T obj, IOrmDao<T, PK> baseDao) {
		if(this.saveDllLog){
			this.saveLog(baseDao, null);
		}
	}

	@Override
	public void afterUpdateBatch(List<T> objs, IOrmDao<T, PK> baseDao) {
		if(this.saveDllLog){
			this.saveLog(baseDao, null);
		}
	}

	@Override
	public void afterDelete(PK id, IOrmDao<T, PK> baseDao) {
		if(this.saveDllLog){
			this.saveLog(baseDao, null);
		}
	}

	@Override
	public void afterDeleteBatch(List<PK> ids, IOrmDao<T, PK> baseDao) {
		if(this.saveDllLog){
			this.saveLog(baseDao, null);
		}
	}

	@Override
	public void afterDelete(Iquery query, IOrmDao<T, PK> baseDao) {
		if(this.saveDllLog){
			this.saveLog(baseDao, null);
		}
	}

	@Override
	public void afterGet(IOrmDao<T, PK> baseDao, T rs, PK id) {
		if(this.saveFindLog){
			this.saveLog(baseDao, rs);
		}
	}

	@Override
	public void afterGet(IOrmDao<T, PK> baseDao, T rs, Iquery query) {
		if(this.saveFindLog){
			this.saveLog(baseDao, rs);
		}
	}

	@Override
	public void afterQueryAll(IOrmDao<T, PK> baseDao, List<T> rs) {
		if(this.saveFindLog){
			this.saveLog(baseDao, rs);
		}
	}

	@Override
	public void afterQuery(IOrmDao<T, PK> baseDao, List<T> rs, Iquery query) {
		if(this.saveFindLog){
			this.saveLog(baseDao, rs);
		}
	}

	@Override
	public void afterQueryPage(IOrmDao<T, PK> baseDao, Page page) {
		if(this.saveFindLog){
			this.saveLog(baseDao, page.getDatas());
		}
	}
	
	@Override
	public void afterCount(IOrmDao<T, PK> baseDao, Number num, Iquery query) {
		if(this.saveFindLog){
			this.saveLog(baseDao, num.longValue());
		}
	}

	@Override
	public void afterQueryForList(IOrmDao<T, PK> baseDao, List<Map<String, Object>> rs, Iquery query) {
		if(this.saveFindLog){
			this.saveLog(baseDao, rs);
		}
	}

	@Override
	public void afterQueryForMap(IOrmDao<T, PK> baseDao, Map<String, Object> rs, Iquery query) {
		if(this.saveFindLog){
			this.saveLog(baseDao, rs);
		}
	}

	@Override
	public int getOrder() {
		return 1000;
	}

	public boolean isSaveDllLog() {
		return saveDllLog;
	}

	public void setSaveDllLog(boolean saveDllLog) {
		this.saveDllLog = saveDllLog;
	}

	public boolean isSaveFindLog() {
		return saveFindLog;
	}

	public void setSaveFindLog(boolean saveFindLog) {
		this.saveFindLog = saveFindLog;
	}


	public String getAppId() {
		if(appId == null){
			appId = PropertiesUtil.getValue("appId", "unknown");
		}
		return appId;
	}


	public void setAppId(String appId) {
		this.appId = appId;
	}


	public boolean isPrintRsStr() {
		return printRsStr;
	}


	public void setPrintRsStr(boolean printRsStr) {
		this.printRsStr = printRsStr;
	}
}
