package net.youboo.ybinterface.andriod.demo;

import java.io.IOException;
import java.net.MalformedURLException;

import net.youboo.ybinterface.andriod.EncryKey;
import net.youboo.ybinterface.andriod.ReqParam;
import net.youboo.ybinterface.andriod.SimpleHttpClient;

public class EmptyBodyDemo {

	public static void main(String[] args) throws MalformedURLException, IOException {
		String session = "6F485E163C6FE896A70C9F5CCC930E62";
		ReqParam reqParam = new ReqParam("10001", "12345678901234567890", "1.0", session);
		EncryKey encryKey = new EncryKey();
		encryKey.setLoginKey("123456");
		encryKey.setSignKey("111111");
		
		
		String rs = SimpleHttpClient.post("/empty", reqParam, encryKey, null);
		System.out.println(rs);
	}
}
