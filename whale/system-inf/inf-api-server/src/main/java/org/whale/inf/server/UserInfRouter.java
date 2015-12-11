package org.whale.inf.server;

import java.util.List;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.whale.inf.api.Dept;
import org.whale.inf.api.User;
import org.whale.inf.common.Result;
import org.whale.system.annotation.web.ReqParam;
import org.whale.system.annotation.web.RespBody;
import org.whale.system.common.exception.BusinessException;

import com.alibaba.fastjson.JSON;

@Controller
@RequestMapping("/user")
public class UserInfRouter {

	@RespBody
	@RequestMapping("/get")
	public Result<User> get(@ReqParam long id) {
		
		if(id == 1){
			throw new BusinessException("sadfasdf");
		}
		
		System.out.println("id =  "+id);
		User user = new User();
		user.setId(id);
		user.setAge(32);
		
		return Result.success(user);
	}

	@RespBody
	@RequestMapping("/sayHello")
	public Result<?> sayHello(@ReqParam String userName, @ReqParam Dept dept) {
		
		System.out.println("userName =  "+userName);
		System.out.println("dept =  "+JSON.toJSONString(dept));
		
		return Result.success();
	}

	@RespBody
	@RequestMapping("/testList")
	public Result<User> testList(@ReqParam List<Dept> depts){
		System.out.println("depts =  "+JSON.toJSONString(depts));
		
		User user = new User();
		user.setId(23L);
		user.setAge(153);
		Result rs= Result.success(user);
		rs.putAttachment("att", 1);
		
		return rs;
	}
	
	@RespBody
	@RequestMapping("/emptyBodyTest")
	public Result<Integer> emptyBodyTest(){
		return Result.success(11111);
	}
}
