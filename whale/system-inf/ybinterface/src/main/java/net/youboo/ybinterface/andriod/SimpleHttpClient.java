package net.youboo.ybinterface.andriod;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.Proxy;
import java.net.URL;
import java.util.HashSet;
import java.util.Set;

import org.apache.commons.io.IOUtils;
import org.whale.system.common.encrypt.AES;
import org.whale.system.common.encrypt.Digest;

import com.alibaba.fastjson.JSON;

public class SimpleHttpClient {
	
	public static final Set<String> useLoginKeyUriSet = new HashSet<String>();
	
	//public static String host = "http://4203ab35.nat123.net:19079/ybinterface";
	public static String host = "http://127.0.0.1:8080/ybinterface";
	
	static{
		useLoginKeyUriSet.add("/login");
		useLoginKeyUriSet.add("/restpwd");
	}

	public static String post(String uri, ReqParam reqParam, EncryKey encryKey, Object datas) throws MalformedURLException, IOException{
		return post(uri, reqParam, encryKey, datas, 30000, 30000);
	}
	
	/**
	 * 
	 * @param urlStr 接口地址
	 * @param reqParam 系统参数
	 * @param encryKey 加密签名key
	 * @param datas 传输对象
	 * @param contimeout 连接超时时间
	 * @param readtimeout 读超时时间
	 * @return
	 * @throws IOException 
	 * @throws MalformedURLException 
	 */
	public static String post(String uri, ReqParam reqParam, EncryKey encryKey, Object datas, Integer contimeout, Integer readtimeout) throws MalformedURLException, IOException{
		String encryStr = getEncryStr(uri, reqParam, encryKey);
		String signStr = reqParam.buildSignStr()+encryKey.getSignKey();
		
		String postStr = null;
		if(datas != null){
			postStr = JSON.toJSONString(datas);
			signStr += postStr;
		}
		System.out.println("postStr:"+postStr);
		System.out.println("signStr:"+signStr);
		
		String sign = Digest.signMD5(signStr);//签名
		String urlParam = reqParam.formatUrlStr(sign);//系统参数拼接成url参数
		System.out.println("sign:"+sign);
		String url = host + uri +"?" + urlParam;
		System.out.println(url);
		
		HttpURLConnection con = (HttpURLConnection)new URL(url).openConnection(Proxy.NO_PROXY);
		con.setConnectTimeout(contimeout);
		con.setReadTimeout(readtimeout);
		con.setAllowUserInteraction(false); //是否允许用户修改连接上下文信息
		con.setDefaultUseCaches(false);
		con.setDoInput(true);//启用
		con.setDoOutput(true); //启用Output
		con.setUseCaches(false);
		con.setInstanceFollowRedirects(true); //设置此 HttpURLConnection 实例是否应该自动执行 HTTP 重定向（响应代码为 3xx 的请求）。
		con.setRequestMethod("POST");
		con.setRequestProperty("content-type", "application/json");
		con.setRequestProperty("accept", "application/json");
		con.setRequestProperty("Connection", "keep-alive");
		
		con.connect();
		OutputStream ops = con.getOutputStream();
		try{
			if(datas != null){
				byte[] bytes = null;
				if(encryStr != null){
					bytes = AES.encrypt(postStr.getBytes("UTF-8"), encryStr.getBytes("UTF-8"));
				}else{
					bytes = postStr.getBytes("UTF-8");
				}
				ops.write(bytes);
			}
			ops.flush();
		}finally{
			ops.close();
		}
		
		
		int resCode = con.getResponseCode();
		InputStream ips = con.getInputStream();
		byte[] bytes = null;
		try{
			bytes = IOUtils.toByteArray(ips);
			if(encryStr != null && "1".equals(con.getRequestProperty("encrypt"))){
				bytes = AES.decrypt(bytes, encryStr.getBytes("UTF-8"));
			}
		}finally{
			ips.close();
		}
		
		if(resCode > 300){
			throw new RuntimeException("HTTP接口请求返回状态码错误");
		}
		
		return new String(bytes);
	}
	
	/**
	 * 获取加密解密密匙
	 * @param uri
	 * @param reqParam
	 * @param encryKey
	 * @return
	 */
	private static String getEncryStr(String uri, ReqParam reqParam, EncryKey encryKey) {
		if(useLoginKeyUriSet.contains(uri)){
			return encryKey.getLoginKey();
		}else{
			if("1".equals(reqParam.getEncrypt())){
				return encryKey.getUserNameKey();
			}
		}
		return null;
	}

	public static void main(String[] args) {
		
	}
	
	
}
