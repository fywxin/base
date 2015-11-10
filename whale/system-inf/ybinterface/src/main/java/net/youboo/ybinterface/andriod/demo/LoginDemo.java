package net.youboo.ybinterface.andriod.demo;

import java.io.IOException;
import java.net.MalformedURLException;

import net.youboo.ybinterface.andriod.EncryKey;
import net.youboo.ybinterface.andriod.ReqParam;
import net.youboo.ybinterface.andriod.SimpleHttpClient;
import net.youboo.ybinterface.param.LoginInfoParam;
import net.youboo.ybinterface.request.LoginReq;

public class LoginDemo {

	
	public static void main(String[] args) throws MalformedURLException, IOException {
		ReqParam reqParam = new ReqParam("10001", "12345678901234567890", "1.0");
		EncryKey encryKey = new EncryKey();
		encryKey.setLoginKey("123456");
		encryKey.setSignKey("111111");
		
		LoginReq loginParam = new LoginReq();
		loginParam.setPassword("111111");
		loginParam.setLoginName("15960108233");
		loginParam.setLoginType(1);
		
		String rs = SimpleHttpClient.post("/login", reqParam, encryKey, loginParam);
		System.out.println(rs);
		
		
	}
}
