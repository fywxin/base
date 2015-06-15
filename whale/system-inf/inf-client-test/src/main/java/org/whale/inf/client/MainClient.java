package org.whale.inf.client;

import java.io.IOException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.support.ClassPathXmlApplicationContext;
import org.whale.inf.api.UserService;

public class MainClient {
	public static final Logger log = LoggerFactory.getLogger(MainClient.class);

	public static void main(String[] args) throws IOException {
        ClassPathXmlApplicationContext context = new ClassPathXmlApplicationContext("applicationContext.xml");
        context.start();
        
        UserService userService = context.getBean(UserService.class);
        userService.get(0L);
        log.info("接口测试客户端启动成功");
        try {
			Thread.currentThread().join();
		} catch (InterruptedException e) {
			e.printStackTrace();
			log.error("接口测试客户端异常退出", e);
			context.close();
		}
        
	}
}
