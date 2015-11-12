package org.whale.test;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.whale.inf.api.User;
import org.whale.inf.api.UserInf;
import org.whale.system.annotation.web.ReqBody;
import org.whale.system.annotation.web.ReqParam;
import org.whale.system.annotation.web.RespBody;
import org.whale.system.inf.Result;

@Controller
@RequestMapping("/wspz")
public class WspzRouter {

	@RespBody
	@RequestMapping("/get")
	public Result<User> get(@ReqBody long id) {
		User user = new User();
		user.setAge(15);
		user.setUserName("dsawe");
		return Result.success(user);
	}

	@RespBody
	@RequestMapping("/sayHello")
	public Result sayHello(@ReqBody double val) {
		System.out.println("val==="+val);
		return Result.success();
	}
	
	/**
	 * 
	 * @param id
	 * @param name
	 * @return
	 */
	@ResponseBody
	@RequestMapping("/reqParamTest")
	public Result<?> reqParamTest(@ReqParam Long id, @ReqParam String name) {
		
		return Result.success(id +":"+name);
	}
}
