package org.whale.inf.client;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.support.ClassPathXmlApplicationContext;
import org.whale.inf.api.Dept;
import org.whale.inf.api.User;
import org.whale.inf.api.UserInf;
import org.whale.inf.common.Result;

import com.alibaba.fastjson.JSON;

public class MainClient {
	public static final Logger log = LoggerFactory.getLogger(MainClient.class);

	public static void main(String[] args) throws IOException {
        ClassPathXmlApplicationContext context = new ClassPathXmlApplicationContext("applicationContext.xml");
        context.start();
        
        UserInf userService = context.getBean(UserInf.class);
        User user  = userService.get(1L);
        System.out.println(JSON.toJSONString(user));
        
        Dept dept = new Dept();
        dept.setDeptName("测试部门");
        dept.setId(1L);
        
        userService.sayHello("wjs", dept);
        
        Dept dept2 = new Dept();
        dept2.setDeptName("测试部门2");
        dept2.setId(13L);
        
        List<Dept> depts = new ArrayList<Dept>();
        depts.add(dept2);
        depts.add(dept);
        
        Result<User> rs = userService.testList(depts);
        System.out.println(JSON.toJSONString(rs));
        System.out.println(JSON.toJSONString(rs.getData()));
        System.out.println(JSON.toJSONString(rs.getAttachment()));
        
        System.out.println(userService.emptyBodyTest());
	}
}
