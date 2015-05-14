package org.whale.ext.main;

import java.io.IOException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.support.ClassPathXmlApplicationContext;

/**
 * @author 王金绍
 * @date 2013-9-29 下午4:56:48 
 */
public class MainMongoServer {
	
	public static final Logger log = LoggerFactory.getLogger(MainMongoServer.class);

	public static void main(String[] args) throws IOException {
        ClassPathXmlApplicationContext context = new ClassPathXmlApplicationContext("META-INF/spring/applicationContext.xml");
        context.start();
        
//        final LogDao logDao = context.getBean(LogDao.class);
//        
//        //1）创建Broker代表
//        SingleBrokerConfig brokerConfig = new SingleBrokerConfig();
//        brokerConfig.setBrokerAddress("127.0.0.1:15555");
//        Broker broker = new SingleBroker(brokerConfig);
//
//        MqConfig config = new MqConfig(); 
//        config.setBroker(broker);
//        config.setMq("MyMQ");
//
//        //2) 创建消费者
//        @SuppressWarnings("resource")
//        Consumer c = new Consumer(config);
//
//        c.onMessage(new MessageCallback() {
//           
//			@Override
//			public void onMessage(Message msg, Session sess)
//					throws IOException {
//				
//				String body = msg.getBodyString();
//				if(Strings.isNotBlank(body)){
//					List<Log> logs = JSON.parseArray(body, Log.class);
//					logDao.save(logs);
//				}
//				
//			}
//        });

        log.info("系统启动成功");
        try {
			Thread.currentThread().join();
		} catch (InterruptedException e) {
			e.printStackTrace();
			log.error("系统异常退出...", e);
			context.close();
		}
        
	}
}

