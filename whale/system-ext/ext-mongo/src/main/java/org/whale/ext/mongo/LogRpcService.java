package org.whale.ext.mongo;

import java.io.IOException;
import java.util.List;

import javax.annotation.Resource;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.whale.system.base.Page;
import org.whale.system.common.exception.SysException;
import org.whale.system.common.util.Strings;
import org.whale.system.domain.Log;
import org.whale.system.rpc.LogRpc;
import org.zstacks.zbus.client.Consumer;
import org.zstacks.zbus.client.MqConfig;
import org.zstacks.znet.Message;
import org.zstacks.znet.callback.MessageCallback;
import org.zstacks.znet.nio.Session;

import com.alibaba.fastjson.JSON;

@Component
public class LogRpcService implements LogRpc,InitializingBean {
	
	private static Logger logger = LoggerFactory.getLogger(LogRpcService.class);
	
	@Autowired
	private LogDao logDao;
	
	@Resource(name="zbusMqConfig")
	private MqConfig mqConfig;
	

	@Override
	public Page queryLogPage(Page page) {
		this.logDao.queryPage(page);
		Page page2 = new Page();
		page2.setTotal(page.getTotal());
		page2.setDatas(page.getDatas());
		return page2;
	}

	@Override
	public Log get(String id) {
		if(Strings.isBlank(id))
			return null;
		return this.logDao.get(id);
	}

	@Override
	public void save(List<Log> logs) {
		if(logs == null || logs.size() < 1)
			return ;
		this.logDao.save(logs);
	}
	
	@SuppressWarnings("all")
	private void startConsumer() throws IOException{
		new Consumer(mqConfig).onMessage(new MessageCallback() {
           
			@Override
			public void onMessage(Message msg, Session sess)
					throws IOException {
				String body = msg.getBodyString();
				if(Strings.isNotBlank(body)){
					logger.info("接收到Mq消息{}", body);
					try{
						List<Log> logs = JSON.parseArray(body, Log.class);
						if(logs == null || logs.size() < 1){
							logger.warn("接收到Mq消息{}转为日志为空", body);
						}else{
							logDao.save(logs);
						}
					}catch(Throwable e){
						logger.error("处理消息出现异常", e);
					}
				}else{
					logger.warn("接收到Mq消息为空{}", msg);
				}
			}
        });
	}

	@Override
	public void afterPropertiesSet() throws Exception {
		try {
			startConsumer();
		} catch (IOException e) {
			logger.error("启动日志消费者异常", e);
			throw new SysException("启动日志消费者异常", e);
		}
	}

}
