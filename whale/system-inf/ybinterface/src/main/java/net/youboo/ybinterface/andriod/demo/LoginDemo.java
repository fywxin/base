package net.youboo.ybinterface.andriod.demo;

import java.io.IOException;
import java.net.MalformedURLException;

import org.whale.system.inf.Result;

import com.alibaba.fastjson.JSON;

import net.youboo.ybinterface.andriod.EncryKey;
import net.youboo.ybinterface.andriod.ReqParam;
import net.youboo.ybinterface.andriod.SimpleHttpClient;
import net.youboo.ybinterface.request.LoginReq;

public class LoginDemo {

	
	public static void main(String[] args) throws MalformedURLException, IOException {
		ReqParam reqParam = new ReqParam("10001", "12345678901234567890", "1.0");
		EncryKey encryKey = new EncryKey();
		encryKey.setLoginKey("0123456789123456");
		encryKey.setSignKey("111111");
		
		LoginReq loginParam = new LoginReq();
		loginParam.setPassword("111111");
		loginParam.setLoginName("15960108244");
		loginParam.setLoginType(0);
		
		String rs = SimpleHttpClient.post("/login", reqParam, encryKey, loginParam);
		System.out.println(rs);
		Result j = JSON.parseObject(rs, Result.class);
		System.out.println(j);
		
		//String a="1000120151111111111123456789012345678901.0111111{\"loginName\":\"15960108244\",\"loginType\":1,\"password\":\"111111\"} ";
		//a="p";
		//byte[] bb = new byte[]{58, 49, 44};
		//byte[] b = a.getBytes("UTF-8");
		//System.out.println(new String(bb));
	}
}
