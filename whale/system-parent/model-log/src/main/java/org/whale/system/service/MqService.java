package org.whale.system.service;

import java.io.IOException;
import java.util.List;

import javax.annotation.Resource;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.whale.system.base.Page;
import org.whale.system.domain.Log;
import org.whale.system.rpc.LogRpc;
import org.zstacks.zbus.client.Producer;
import org.zstacks.znet.Message;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.serializer.SerializerFeature;


public class MqService implements LogRpc{
	
	private static Logger logger = LoggerFactory.getLogger(MqService.class);

	@Resource(name="zbusMqProducer")
	private Producer producer;
	@Resource(name="zbusLogRpcConsumer")
	private LogRpc logRpc;
	
	public MqService(){
//		 //创建Broker代理【重量级对象，需要释放】
//	    SingleBrokerConfig config = new SingleBrokerConfig();
//	    config.setBrokerAddress("127.0.0.1:15555");
//	    Broker broker = null;
//		try {
//			broker = new SingleBroker(config);
//			//创建生产者 【轻量级对象，不需要释放，随便使用】
//		    this.producer = new Producer(broker, "MyMQ");
//		    producer.createMQ(); 
//		} catch (IOException e) {
//			throw new SysException("未启动消息队列管理器", e);
//		}
	}

	@Override
	public void save(List<Log> logs) {
		try {
			logger.info("开始发送日志{}", logs);
			this.producer.createMQ();
			Message msg = new Message(); 
			msg.setBody(JSON.toJSONString(logs, SerializerFeature.WriteClassName));
			Message res = producer.sendSync(msg, 1000);
			if(res != null){
				logger.info("日志发送完成，返回{}", res);
			}
		} catch (IOException e) {
			logger.error("发送日志异常", e);
		} 
	}


	@Override
	public Log get(String objId) {
		return this.logRpc.get(objId);
	}

	@Override
	public Page queryLogPage(Page page) {
		this.logRpc.queryLogPage(page);
		return page;
	}

}
