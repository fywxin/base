package org.whale.inf.server;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.whale.inf.api.Dept;
import org.whale.inf.api.User;
import org.whale.system.annotation.web.ReqParam;
import org.whale.system.inf.Result;

import com.alibaba.fastjson.JSON;

@Controller
@RequestMapping("/user")
public class UserInfRouter {

	@ResponseBody
	@RequestMapping("/get")
	public Result<User> get(@ReqParam long id) {
		
		System.out.println("id =  "+id);
		User user = new User();
		user.setId(id);
		user.setAge(32);
		
		return Result.success(user);
	}

	@ResponseBody
	@RequestMapping("/sayHello")
	public Result<?> sayHello(@ReqParam String userName, @ReqParam Dept dept) {
		
		System.out.println("userName =  "+userName);
		System.out.println("dept =  "+JSON.toJSONString(dept));
		
		return Result.success();
	}

}
