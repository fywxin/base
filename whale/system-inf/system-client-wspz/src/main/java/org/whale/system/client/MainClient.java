package org.whale.system.client;

import java.io.IOException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.support.ClassPathXmlApplicationContext;
import org.whale.inf.api.User;
import org.whale.inf.api.UserInf;

import com.alibaba.fastjson.JSON;

public class MainClient {
	public static final Logger log = LoggerFactory.getLogger(MainClient.class);

	public static void main(String[] args) throws IOException {
        ClassPathXmlApplicationContext context = new ClassPathXmlApplicationContext("applicationContext.xml");
        context.start();
        
        UserInf userService = context.getBean(UserInf.class);
        User user  = userService.get(1L);
        System.out.println(JSON.toJSONString(user));
        
        //userService.sayHello(2.562f);
        
	}
}
