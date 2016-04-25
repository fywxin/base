package org.whale.system.log.service;


import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.whale.system.log.dao.LogInfoDao;
import org.whale.system.log.domain.LogInfo;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingDeque;

/**
 * 服务调用日志保存
 * @author 王金绍
 *
 */
@Component
public class LogStoreService implements InitializingBean{

	private static final Logger logger = LoggerFactory.getLogger(LogStoreService.class);
	
	 // 队列
    private static final BlockingQueue<LogInfo> LOG_QUEUE = new LinkedBlockingDeque<LogInfo>();
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
	private LogInfoDao logInfoDao;
	
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

    private void consumeRecvQueue(BlockingQueue<LogInfo> queue, int size) {
        List<LogInfo> logs = new ArrayList<LogInfo>();
        try {
            queue.drainTo(logs, size);
                    
            if (!logs.isEmpty()) {
                long interval = 0;
                
                if(currentNetErrorTime > 0){
                    interval = System.currentTimeMillis() - currentNetErrorTime;
                }
                
                if(interval == 0 || interval >= defaultNetErrorTryTime){
                    this.logInfoDao.saveBatch(logs);
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
    
    private void printLoseLogs(List<LogInfo> logs, Throwable e){
        if(e == null){
            logger.error("丢失推送日志 " + logs.size() + "条");
        }
        else{
            logger.error("丢失推送日志 " + logs.size() + "条", e);
        }
    }

    public void addLogRecvQueue(LogInfo log) {
        LOG_QUEUE.add(log);
        synchronized (notify) {
            notify.notifyAll();
        }
    }

    public void addLogRecvQueues(List<LogInfo> logs) {
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
			logger.error("ORM:设置日志后台扫描线程被停止！userId = ");
		}
	}

}
