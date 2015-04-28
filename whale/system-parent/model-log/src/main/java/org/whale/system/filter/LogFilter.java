package org.whale.system.filter;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingDeque;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.whale.system.base.Page;
import org.whale.system.base.UserContext;
import org.whale.system.common.util.Strings;
import org.whale.system.common.util.ThreadContext;
import org.whale.system.dao.LogDao;
import org.whale.system.domain.Log;
import org.whale.system.jdbc.IOrmDao;
import org.whale.system.jdbc.filter.BaseDaoFilterWarpper;
import org.whale.system.jdbc.filter.impl.OrmExeContext;
import org.whale.system.jdbc.orm.entry.OrmTable;

import com.alibaba.fastjson.JSON;

/**
 * 日志过滤收集器
 *
 * @author 王金绍
 * 2015年4月26日 下午3:43:15
 */
@Component
public class LogFilter<T extends Serializable,PK extends Serializable> extends BaseDaoFilterWarpper<T, PK> implements InitializingBean{
	
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

   	private final Object notify = new Object();

   	private boolean stopFlag = false;
   	
   	private boolean saveDllLog = true;
   	
   	private boolean saveFindLog = true;
   
	@Autowired
	private LogDao logDao;	
	
	
	private void saveLog(IOrmDao<T, PK> baseDao){
		OrmTable ormTable = baseDao.getOrmTable();
		if(ormTable.getEntityName().equals("Log"))
			return ;
		
		OrmExeContext ormExeContext = (OrmExeContext)ThreadContext.getContext().get(ThreadContext.KEY_OPT_CONTEXT);
		Log log = this.newLog();
		log.setCnName(ormTable.getTableCnName());
		log.setTableName(ormTable.getTableDbName());
		log.setOpt(ormExeContext.getMethodName());
		log.setArg(ormExeContext.getArg());
		log.setSqlStr(ormExeContext.getSql());
		
	}
	
	
	public Log newLog(){
		Log log = new Log();
		int order = 1;
		Long time = System.currentTimeMillis();
		
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
	

    class ConsumeLogThread extends Thread {

        @Override
        public void run() {
            int waitTime = 0;
            while (!stopFlag) {
            	System.out.println(LOG_QUEUE.size()+" | "+defaultQueueSize+" | "+ waitTime+" | "+defaultWaitTime);
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
                	for(Log log : logs){
                		if(Strings.isBlank(log.getDatas())){
                			log.setDatas(JSON.toJSONString(log.getArg()));
                		}
                	}
                    this.logDao.saveBatch(logs);
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
            logger.error("丢失推送日志 " + logs.size() + "条");
        }
        else{
            logger.error("丢失推送日志 " + logs.size() + "条", e);
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
		logger.warn("ORM:设置日志后台扫描线程状态为 "+stopFlag);
		this.stopFlag = stopFlag;
		if(this.stopFlag){
			UserContext uc = (UserContext)ThreadContext.getContext().get(ThreadContext.KEY_USER_CONTEXT);
			logger.error("ORM:设置日志后台扫描线程被停止！userId = "+ uc.getUserId());
		}
	}

	

	@Override
	public void afterSave(T obj, IOrmDao<T, PK> baseDao) {
		if(this.saveDllLog){
			this.saveLog(baseDao);
		}
	}

	@Override
	public void afterSave(List<T> objs, IOrmDao<T, PK> baseDao) {
		if(this.saveDllLog){
			this.saveLog(baseDao);
		}
	}

	@Override
	public void afterSaveBatch(List<T> objs, IOrmDao<T, PK> baseDao) {
		if(this.saveDllLog){
			this.saveLog(baseDao);
		}
	}

	@Override
	public void afterUpdate(T obj, IOrmDao<T, PK> baseDao) {
		if(this.saveDllLog){
			this.saveLog(baseDao);
		}
	}

	@Override
	public void afterUpdate(List<T> objs, IOrmDao<T, PK> baseDao) {
		if(this.saveDllLog){
			this.saveLog(baseDao);
		}
	}

	@Override
	public void afterUpdateBatch(List<T> objs, IOrmDao<T, PK> baseDao) {
		if(this.saveDllLog){
			this.saveLog(baseDao);
		}
	}

	@Override
	public void afterDelete(PK id, IOrmDao<T, PK> baseDao) {
		if(this.saveDllLog){
			this.saveLog(baseDao);
		}
	}

	@Override
	public void afterDelete(List<PK> ids, IOrmDao<T, PK> baseDao) {
		if(this.saveDllLog){
			this.saveLog(baseDao);
		}
	}

	@Override
	public void afterDeleteBy(T obj, IOrmDao<T, PK> baseDao) {
		if(this.saveDllLog){
			this.saveLog(baseDao);
		}
	}

	@Override
	public void afterGet(IOrmDao<T, PK> baseDao, T rs, PK id) {
		if(this.saveFindLog){
			this.saveLog(baseDao);
		}
	}

	@Override
	public void afterGetObject(IOrmDao<T, PK> baseDao, T rs, String sql) {
		if(this.saveFindLog){
			this.saveLog(baseDao);
		}
	}

	@Override
	public void afterGetObject(IOrmDao<T, PK> baseDao, T rs, String sql,
			Object... args) {
		if(this.saveFindLog){
			this.saveLog(baseDao);
		}
	}

	@Override
	public void afterQueryAll(IOrmDao<T, PK> baseDao, List<T> rs) {
		if(this.saveFindLog){
			this.saveLog(baseDao);
		}
	}

	@Override
	public void afterQuery(IOrmDao<T, PK> baseDao, List<T> rs, T t) {
		if(this.saveFindLog){
			this.saveLog(baseDao);
		}
	}

	@Override
	public void afterQuery(IOrmDao<T, PK> baseDao, List<T> rs, String sql) {
		if(this.saveFindLog){
			this.saveLog(baseDao);
		}
	}

	@Override
	public void afterQuery(IOrmDao<T, PK> baseDao, List<T> rs, String sql,
			Object... args) {
		if(this.saveFindLog){
			this.saveLog(baseDao);
		}
	}

	@Override
	public void afterQueryPage(IOrmDao<T, PK> baseDao, Page page) {
		if(this.saveFindLog){
			this.saveLog(baseDao);
		}
	}
	
	@Override
	public void afterQueryForNumber(IOrmDao<T, PK> baseDao, Number num, String sql, Object... args) {
		if(this.saveFindLog){
			this.saveLog(baseDao);
		}
	}

	@Override
	public void afterQueryForList(IOrmDao<T, PK> baseDao, List<Map<String, Object>> rs, String sql, Object... args) {
		if(this.saveFindLog){
			this.saveLog(baseDao);
		}
	}

	@Override
	public void afterQueryForMap(IOrmDao<T, PK> baseDao, Map<String, Object> rs, String sql, Object... args) {
		if(this.saveFindLog){
			this.saveLog(baseDao);
		}
	}

	@Override
	public void afterQueryOther(IOrmDao<T, PK> baseDao, List<?> rs, String sql, Object... args) {
		if(this.saveFindLog){
			this.saveLog(baseDao);
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
}
