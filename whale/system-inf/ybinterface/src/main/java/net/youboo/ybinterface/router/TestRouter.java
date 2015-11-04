package net.youboo.ybinterface.router;

import java.util.Random;

import net.youboo.ybinterface.context.Result;
import net.youboo.ybinterface.param.EmptyBodyParam;
import net.youboo.ybinterface.param.LoginInfoParam;
import net.youboo.ybinterface.param.OneBodyParam;
import net.youboo.ybinterface.vo.LoginInfoVo;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.whale.system.annotation.web.ReqBody;
import org.whale.system.annotation.web.RespBody;

@Controller
public class TestRouter {

	@RespBody(secure=true)
	@RequestMapping("/login")
	public Result<LoginInfoVo> login(@ReqBody(secure=true) LoginInfoParam loginInfo){
		if(loginInfo.getLogin_name().equals("18650365658") && loginInfo.getPassword().equals("123456")){
			LoginInfoVo loginInfoVo = new LoginInfoVo();
			loginInfoVo.setName("18650365658");
			loginInfoVo.setUserId("123456");
			return Result.success(loginInfoVo);
		}else{
			throw new RuntimeException("用户名密码错误");
		}
	}
	
	@RespBody
	@RequestMapping("/empty")
	public Result<String> login(@ReqBody EmptyBodyParam emptyBodyParam){
		
		return Result.success("空对象！！");
	}
	
	@RespBody
	@RequestMapping("/code")
	private Result<Integer> code(@ReqBody OneBodyParam phone){
		System.out.println("phone : "+phone.getValue());
		return Result.success(new Random().nextInt(5000));
	}
	
}