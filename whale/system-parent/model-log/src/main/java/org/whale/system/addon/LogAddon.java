package org.whale.system.addon;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingDeque;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.whale.system.base.BaseDao;
import org.whale.system.base.UserContext;
import org.whale.system.common.util.Strings;
import org.whale.system.common.util.ThreadContext;
import org.whale.system.dao.LogDao;
import org.whale.system.domain.Log;
import org.whale.system.jdbc.orm.entry.OrmTable;

import com.alibaba.fastjson.JSON;

/**
 * 日志处理插件
 * 
 * @author 王金绍
 * 2014年9月17日-下午2:48:26
 */
@SuppressWarnings("all")
@Component
public class LogAddon extends EmptyBaseDaoAddon implements InitializingBean{
	
	private static final Logger logger = LoggerFactory.getLogger(LogAddon.class);
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
    private int defaultQueueSize = 50;

    private final Object notify = new Object();

    private boolean stopFlag = false;
    
	@Autowired
	private LogDao logDao;

	@Override
	public void afterSave(Object obj, BaseDao baseDao) {
		this.saveLog("save", obj, baseDao);
	}

	@Override
	public void afterUpdate(Object obj, BaseDao baseDao) {
		this.saveLog("update", obj, baseDao);
	}

	@Override
	public void afterDelete(Object id, BaseDao baseDao) {
		this.saveLog("delete", id, baseDao);
	}

	@Override
	public void afterSaveBatch(List<Object> objs, BaseDao baseDao) {
		this.saveLog("saveBatch", objs, baseDao);
	}

	@Override
	public void afterUpdateBatch(List<Object> objs, BaseDao baseDao) {
		this.saveLog("updateBatch", objs, baseDao);
	}

	@Override
	public void afterDeleteBatch(List<Object> objs, BaseDao baseDao) {
		this.saveLog("deleteBatch", objs, baseDao);
	}
	
	private void saveLog(String opt, Object arg, BaseDao baseDao){
		OrmTable ormTable = baseDao.getOrmTable();
		if(ormTable.getEntityName().equals("Log"))
			return ;
		
		Log log = this.newLog();
		log.setCnName(ormTable.getTableCnName());
		log.setTableName(ormTable.getTableDbName());
		log.setOpt(opt);
		log.setArg(arg);
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
	public int getOrder() {
		return 1000;
	}
}
