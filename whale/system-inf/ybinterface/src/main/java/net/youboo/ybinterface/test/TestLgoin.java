package net.youboo.ybinterface.test;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.Proxy;
import java.net.URL;

import org.apache.commons.io.IOUtils;
import org.whale.system.common.encrypt.AESUtil;
import org.whale.system.common.encrypt.EncryptUtil;

import com.alibaba.fastjson.JSON;

import net.youboo.ybinterface.param.LoginInfoParam;

public class TestLgoin {

	public static void main(String[] args) throws IOException {
		LoginInfoParam loginParam = new LoginInfoParam();
		loginParam.setPassword("1");
		loginParam.setUserName("admin");
		String datas = JSON.toJSONString(loginParam);
		
		StringBuilder strb = new StringBuilder();
		strb.append("10001")
			.append("201511251111")
			.append("12345678901234567890")
			.append("1.0")//参数
			.append("111111")//签名密钥
			.append(datas);//数据体
		System.out.println(strb.toString());
		String sign= new String(EncryptUtil.md5(strb.toString().getBytes("utf-8")));
		
		System.out.println(sign);
		
		URL url = new URL("http://127.0.0.1:7040/ybinterface/login?app_key=10001&timestamp=201511251111&reqno=12345678901234567890&version=1.0&sign="+sign);
		HttpURLConnection con = (HttpURLConnection)url.openConnection(Proxy.NO_PROXY);
		con.setConnectTimeout(30000);
		con.setReadTimeout(30000);
		con.setAllowUserInteraction(false); //是否允许用户修改连接上下文信息
		con.setDefaultUseCaches(false);
		con.setDoInput(true);//启用
		con.setDoOutput(true); //启用Output
		con.setUseCaches(false);
		con.setInstanceFollowRedirects(true); //设置此 HttpURLConnection 实例是否应该自动执行 HTTP 重定向（响应代码为 3xx 的请求）。
		con.setRequestMethod("POST");
		con.setRequestProperty("content-type", "application/json");
		con.setRequestProperty("accept", "application/json");
		
		con.connect();
		OutputStream ops = con.getOutputStream();
		try{
			byte[] by = AESUtil.encrypt(datas.getBytes("UTF-8"), "123456".getBytes());
			System.out.println(new String(AESUtil.decrypt(by, "123456".getBytes())));
			
			ops.write(by);
		}finally{
			ops.close();
		}
		
		int resCode = con.getResponseCode();
		
		InputStream ips = con.getInputStream();
		StringBuffer response = new StringBuffer();
		try{
			byte[] bytes = IOUtils.toByteArray(ips);
			if("1".equals(con.getRequestProperty("encrypt"))){
				bytes = AESUtil.decrypt(bytes, "admin".getBytes());
			}
			
			System.out.println(new String(bytes));
		}finally{
			ips.close();
		}
	}
	
	
}
