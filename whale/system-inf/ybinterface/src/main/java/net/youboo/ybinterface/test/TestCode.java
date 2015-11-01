package net.youboo.ybinterface.test;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.Proxy;
import java.net.URL;

import net.youboo.ybinterface.param.OneBodyParam;

import org.apache.commons.io.IOUtils;
import org.whale.system.common.encrypt.AESUtil;
import org.whale.system.common.encrypt.EncryptUtil;

import com.alibaba.fastjson.JSON;

public class TestCode {

	public static void main(String[] args) throws IOException {
		OneBodyParam oneBodyParam = new OneBodyParam();
		oneBodyParam.setValue("18521551255");
		
		String body = JSON.toJSONString(oneBodyParam);
		
		StringBuilder strb = new StringBuilder();
		strb.append("10001")
			.append("6D58B13564FC4A83BFDF7D8552A6634F")
			.append("201511251111")
			.append("12345678901234567890")
			.append("1.0")//参数
			.append("111111")//签名密钥
			.append(body);
		System.out.println(strb.toString());
		String sign= new String(EncryptUtil.md5(strb.toString().getBytes("utf-8")));
		
		System.out.println(sign);
		
		URL url = new URL("http://127.0.0.1:7040/ybinterface/empty?app_key=10001&timestamp=201511251111&session=6D58B13564FC4A83BFDF7D8552A6634F&reqno=12345678901234567890&version=1.0&sign="+sign);
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
			ops.write(body.getBytes());
			ops.flush();
		}finally{
			ops.close();
		}
		
		int resCode = con.getResponseCode();
		
		InputStream ips = con.getInputStream();
		StringBuffer response = new StringBuffer();
		try{
			byte[] bytes = IOUtils.toByteArray(ips);
			if("1".equals(con.getRequestProperty("encrypt"))){
				bytes = AESUtil.decrypt(bytes, "123456".getBytes());
			}
			
			System.out.println(new String(bytes));
		}finally{
			ips.close();
		}
	}

}
