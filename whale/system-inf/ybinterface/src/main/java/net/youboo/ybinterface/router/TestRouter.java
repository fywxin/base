package net.youboo.ybinterface.router;

import net.youboo.ybinterface.constant.ErrorCode;
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
		if(loginInfo.getUserName().equals("admin") && loginInfo.getPassword().equals("1")){
			LoginInfoVo loginInfoVo = new LoginInfoVo();
			loginInfoVo.setName("admin");
			loginInfoVo.setUserId("1");
			return Result.success(loginInfoVo);
		}else{
			return Result.fail(ErrorCode.DATA_ERROR);
		}
	}
	
	@RespBody
	@RequestMapping("/empty")
	public Result<LoginInfoVo> login(@ReqBody EmptyBodyParam emptyBodyParam){
		LoginInfoVo loginInfoVo = new LoginInfoVo();
		loginInfoVo.setName("admin");
		loginInfoVo.setUserId("1");
		return Result.success(loginInfoVo);
	}
	
	@RespBody
	@RequestMapping("/code")
	private Result<String> code(@ReqBody OneBodyParam phone){
		System.out.println("phone : "+phone.getValue());
		return Result.success("2152");
	}
	
}
