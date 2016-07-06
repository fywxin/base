package org.whale.system.common.util;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.Proxy;
import java.net.URL;
import java.net.URLEncoder;
import java.nio.charset.Charset;
import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;

import javax.net.ssl.HostnameVerifier;
import javax.net.ssl.HttpsURLConnection;
import javax.net.ssl.SSLContext;

import org.apache.commons.io.Charsets;
import org.apache.commons.io.IOUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import org.whale.system.common.constant.SysConstant;
import org.whale.system.common.exception.HttpClientException;
import org.whale.system.common.exception.HttpClientIOException;
import org.whale.system.spring.SpringContextHolder;

/**
 * 基于JDK的HTTP 请求
 *
 * 出现乱码时，注意http头是否设置
 *   "content-type", "application/json;charset=utf-8"
 * 
 * @author wjs
 * @Date 2015-05-19 17:21:02
 */
@Component
public class SimpleHttpClient {
	
	private static final Logger logger = LoggerFactory.getLogger("SimpleHttpClient");
	
	// 连接超时
	private int connectionTimeout = 60 * 1000;
	// 读超时, read exception 调整此值
	private int readTimeout = 60 * 1000 * 2;
	//打印慢日志，超过slowTime将被打印到日志中
	private Integer slowTime;
	
	private Map<String, String> commonHeaders	= new HashMap<String, String>();
	
	private SSLContext sslContext 				= null;
	
	private HostnameVerifier hostNameVerifier 	= null;
	//连接通过指定的代理建立
	private Proxy connectionProxy 	= Proxy.NO_PROXY;
	
	/**
	 * Get请求
	 * 
	 * @param url  地址
	 * @return
	 */
	public static String get(String url){
		return get(url, null, "UTF-8");
	}
	
	/**
	 * Get请求
	 * 
	 * @param url  地址
	 * @param headers	http头
	 * @return
	 */
	public static String get(String url, Map<String, String> headers){
		return get(url, headers, "UTF-8");
	}
	
	/**
	 * Get请求
	 * 
	 * @param url 地址
	 * @param headers http头
	 * @param charset 返回内容编码
	 * @return
	 */
	public static String get(String url, Map<String, String> headers, String charset){
		return get(url, headers, charset, null);
	}
	
	/**
	 * Get请求
	 * 
	 * @param url
	 * @param headers
	 * @param readTimeout 读超时时间
	 * @return
	 */
	public static String get(String url, Map<String, String> headers, Integer readTimeout) {
		return get(url, headers, "UTF-8", null, readTimeout);
	}
	
	/**
	 * Get请求
	 * @param url
	 * @param headers
	 * @param charset 返回内容编码
	 * @param readTimeout 读超时时间
	 * @return
	 */
	public static String get(String url, Map<String, String> headers, String charset, Integer readTimeout) {
		return get(url, headers, charset, null, readTimeout);
	}
	
	/**
	 * Get请求
	 * @param url
	 * @param headers
	 * @param charset 返回内容编码
	 * @param conTimeout 连接超时时间
	 * @param readTimeout 读超时时间
	 * @return
	 */
	public static String get(String url, Map<String, String> headers, String charset, Integer conTimeout, Integer readTimeout) {
		return getThis().doExecute(url, "GET", headers, null, conTimeout, readTimeout, null, charset);
	}
	
	//------------------------------POST请求-----------------------------------
	
	
	/**
	 * POST请求
	 *
	 *
	 * @param url
	 * @param params 请求内容 KEY-VALUE模式
	 * @return
	 */
	public static String post(String url, Map<String, Object> params){
		return post(url, null, params, null, null, null, null);
	}
	
	/**
	 * POST请求
	 * @param url
	 * @param params 请求内容 KEY-VALUE模式
	 * @param headers 请求头
	 * @return
	 */
	public static String post(String url, Map<String, String> headers, Map<String, Object> params){
		return post(url, headers, params, null, null, null, null);
	}
	
	/**
	 * POST请求
	 * @param url
	 * @param params
	 * @param headers
	 * @param charset 请求和响应编码
	 * @return
	 */
	public static String post(String url, Map<String, String> headers, Map<String, Object> params, String charset) {
		return post(url, headers, params, null, null, charset, charset);
	}
	
	/**
	 * POST请求
	 * @param url
	 * @param headers
	 * @param params
	 * @param timeout 超时时间
	 * @return
	 */
	public static String post(String url, Map<String, String> headers, Map<String, Object> params, Integer timeout) {
		
		return post(url, headers, params, timeout, timeout, null, null);
	}

	/**
	 * POST请求
	 * @param url
	 * @param headers
	 * @param params
	 * @param conTimeout
	 * @param readTimeout
	 * @param reqCharset
	 * @param resCharset
	 * @return
	 */
	public static String post(String url, Map<String, String> headers, Map<String, Object> params, Integer conTimeout, Integer readTimeout, String reqCharset, String resCharset) {
		if(Strings.isBlank(reqCharset)){
			reqCharset = SysConstant.UTF_8; 
		}
		if(Strings.isBlank(resCharset)){
			resCharset = SysConstant.UTF_8; 
		}
		
		String bodyStr = null;
		
		if(headers == null){
			headers = new HashMap<String, String>();
		}
		if(!headers.containsKey("content-type") && !headers.containsKey("Content-Type")){
			headers.put("content-type", "application/x-www-form-urlencoded");
		}
		
		if(params != null && params.size() > 0){
			StringBuilder strb = new StringBuilder();
			for(Entry<String, Object> entry : params.entrySet()){
				if(entry.getValue() != null){
					if(strb.length()>0){
						strb.append("&");
					}
					try{
						strb.append(URLEncoder.encode(entry.getKey(), reqCharset)).append("=").append(URLEncoder.encode(entry.getValue().toString(), reqCharset));
					}catch(UnsupportedEncodingException e){
						throw new HttpClientIOException(e);
					}
				}
			}
			bodyStr = strb.toString();
		}
		
		return getThis().doExecute(url, "POST", headers, bodyStr, conTimeout, readTimeout, reqCharset, resCharset);
	}
	
	/**
	 * POST请求
	 * @param url 地址
	 * @param postStr 请求内容
	 * @return
	 */
	public static String post(String url, String postStr){
		return post(url, null, postStr, null, null, null, null);
	}
	
	/**
	 * POST请求
	 * @param url 地址
	 * @param headers 消息头
	 * @param postStr 请求内容
	 * @return
	 */
	public static String post(String url, Map<String, String> headers, String postStr){
		return post(url, headers, postStr, null, null, null, null);
	}
	
	public static String post(String url, Map<String, String> headers, String postStr, String charset) {
		return post(url, headers, postStr, null, null, charset, charset);
	}
	
	public static String post(String url, Map<String, String> headers, String postStr, Integer timeout) {
		return post(url, headers, postStr, timeout, timeout, null, null);
	}
	
	public static String post(String url, Map<String, String> headers, String postStr, Integer timeout, String charset) {
		return post(url, headers, postStr, timeout, timeout, charset, charset);
	}
	
	
	public static String post(String url, Map<String, String> headers, String postStr, Integer conTimeout, Integer readTimeout, String reqCharset, String resCharset) {
		if(Strings.isBlank(reqCharset)){
			reqCharset = SysConstant.UTF_8; 
		}
		if(Strings.isBlank(resCharset)){
			resCharset = SysConstant.UTF_8; 
		}
		return getThis().doExecute(url, "POST", headers, postStr, conTimeout, readTimeout, reqCharset, resCharset);
	}
	
	/**
	 * http 请求执行体
	 * http://ferreousbox.iteye.com/blog/157728
	 * 
	 * @param url  Url 地址
	 * @param method  请求方法
	 * @param header	请求头
	 * @param body  请求内容
	 * @param contimeout	超时时间
	 * @param reqCharset	请求编码
	 * @param resCharset	返回编码
	 * @return
	 */
	private String doExecute(String url, String method, Map<String, String> header, String body, Integer contimeout, Integer readtimeout, String reqCharset, String resCharset){
		if(logger.isDebugEnabled()){
			logger.debug("url:{}, method:{}, contimeout: {}, readtimeout: {}, reqCharset:{}, resCharset: {} \nheader: {}, \nbody:{}", 
					url, method, contimeout, readtimeout, reqCharset, resCharset, header, body);
		}
		
		long start = System.currentTimeMillis();
		try {
			HttpURLConnection con = prepareConnection(new URL(url), method, header, contimeout, readtimeout);
			byte[] datas = null;
			boolean hasBody = "POST".equals(method) && Strings.isNotBlank(body);
			if(hasBody){
				datas = body.getBytes(Charsets.toCharset(reqCharset));
				con.setFixedLengthStreamingMode(datas.length); //校验 和增加头 Content-Length ： XXX
			}

			con.connect();
			
			if(hasBody){
				OutputStream ops = con.getOutputStream();
				try{
//					OutputStreamWriter writer = new OutputStreamWriter(ops, reqCharset);
//					writer.write(body);
//					writer.flush();

//					IOUtils.write(body, ops, reqCharset);
					ops.write(datas);
					ops.flush();

				}finally{
					ops.close();
				}
			}
			//Status-Line = HTTP-Version SP Status-Code SP Reason-Phrase  返回头中得到
			int resCode = con.getResponseCode();
			
			InputStream ips = con.getInputStream();

			String resStr = null;
			try{
				resStr = IOUtils.toString(ips, resCharset);
			}finally{
				ips.close();
			}
			long costTime = System.currentTimeMillis()-start;
			
			if(resCode > 300){
				logger.warn("HTTP返回状态码错误。 url: {}, state: {}, costTime:{}, resp: {}", url, resCode, costTime, resStr);
				throw new HttpClientException("返回状态码["+resCode+"] 错误: "+resStr);
			}
			
			if(slowTime != null && costTime > slowTime){
				logger.warn("url:{}, 耗时[{}], 返回 :\n {}",url, costTime, resStr);
			}else{
				logger.debug("url:{}, 耗时[{}], 返回:\n {}" ,url, costTime, resStr);
			}
			return resStr;
		} catch (Exception e) {
			logger.error("HTTP调用异常", e);
			throw new HttpClientIOException(e);
		}
	}

	
	public HttpURLConnection prepareConnection(URL url, String method, Map<String, String> headers, Integer contimeout, Integer readtimeout)throws IOException {
		HttpURLConnection con = (HttpURLConnection)url.openConnection(connectionProxy);
		con.setConnectTimeout((contimeout != null && contimeout > 0) ? contimeout : connectionTimeout);
		con.setReadTimeout((readtimeout != null && readtimeout > 0) ? readtimeout : readTimeout);
		con.setAllowUserInteraction(false); //是否允许用户修改连接上下文信息
		con.setDefaultUseCaches(false);
		con.setDoInput(true);//启用
		con.setDoOutput(true); //启用Output
		con.setUseCaches(false);
		con.setInstanceFollowRedirects(true); //设置此 HttpURLConnection 实例是否应该自动执行 HTTP 重定向（响应代码为 3xx 的请求）。
		con.setRequestMethod(method);
		
		// do stuff for ssl
		if (HttpsURLConnection.class.isInstance(con)) {
			HttpsURLConnection https = HttpsURLConnection.class.cast(con);
			if (hostNameVerifier != null) {
				https.setHostnameVerifier(hostNameVerifier);
			}
			if (sslContext != null) {
				https.setSSLSocketFactory(sslContext.getSocketFactory());
			}
		}
		
		for (Entry<String, String> entry : commonHeaders.entrySet()) {
			con.setRequestProperty(entry.getKey(), entry.getValue());
		}
		if(headers != null){
			for (Entry<String, String> entry : headers.entrySet()) {
				con.setRequestProperty(entry.getKey(), entry.getValue());
			}
		}
			
		return con;
	}
	
	
//	
//	private byte[] doExecute(String url, String method, Map<String, String> header, byte[] body, Integer contimeout, Integer readtimeout){
//		logger.debug("url:{}, method:{}, contimeout: {}, readtimeout: {}, reqCharset:{}, resCharset: {} \nheader: {}, \nbody:{}", url, method, contimeout, readtimeout, reqCharset, resCharset, header, body);
//		long start = System.currentTimeMillis();
//		try {
//			HttpURLConnection con = prepareConnection(new URL(url), method, header, contimeout, readtimeout);
//			con.connect();
//			
//			if("POST".equals(method) && body != null && body.length > 0){
//				OutputStream ops = con.getOutputStream();
//				try{
//					ops.write(body);
//					ops.flush();
//				}finally{
//					ops.close();
//				}
//			}
//			int resCode = con.getResponseCode();
//			
//			InputStream ips = con.getInputStream();
//			int b;
//			ByteArrayOutputStream bos = new ByteArrayOutputStream();
//			try{
//				while((b = ips.read()) != -1){
//					bos.write(b);
//				}
//				bos.flush();
//				byte[] rs = bos.toByteArray();
//				
//			}finally{
//				bos.close();
//				ips.close();
//			}
	
	//需要解码出http header  和 body的 byte[]
//			String resStr = response.toString();
//			long costTime = System.currentTimeMillis()-start;
//			
//			if(resCode > 300){
//				logger.warn("HTTP返回状态码错误。 url: {}, state: {}, costTime:{}, resp: {}", url, resCode, costTime, resStr);
//				throw new HttpClientException("返回状态码["+resCode+"] 错误: "+resStr);
//			}
//			
//			if(slowTime != null && costTime > slowTime){
//				logger.warn("url:{}, 耗时[{}], 返回 :\n {}",url, costTime, resStr);
//			}else{
//				logger.debug("url:{}, 耗时[{}], 返回:\n {}" ,url, costTime, resStr);
//			}
//			return resStr;
//		} catch (Exception e) {
//			logger.error("HTTP调用异常", e);
//			throw new HttpClientIOException(e);
//		}
//	}

	public int getConnectionTimeout() {
		return connectionTimeout;
	}

	public void setConnectionTimeout(int connectionTimeout) {
		this.connectionTimeout = connectionTimeout;
	}

	public int getReadTimeout() {
		return readTimeout;
	}

	public void setReadTimeout(int readTimeout) {
		this.readTimeout = readTimeout;
	}

	public SSLContext getSslContext() {
		return sslContext;
	}

	public void setSslContext(SSLContext sslContext) {
		this.sslContext = sslContext;
	}

	public HostnameVerifier getHostNameVerifier() {
		return hostNameVerifier;
	}

	public void setHostNameVerifier(HostnameVerifier hostNameVerifier) {
		this.hostNameVerifier = hostNameVerifier;
	}
	
	public Integer getSlowTime() {
		return slowTime;
	}

	public void setSlowTime(Integer slowTime) {
		this.slowTime = slowTime;
	}

	public Map<String, String> getCommonHeaders() {
		return commonHeaders;
	}

	public void setCommonHeaders(Map<String, String> commonHeaders) {
		this.commonHeaders = commonHeaders;
	}

	public Proxy getConnectionProxy() {
		return connectionProxy;
	}

	public void setConnectionProxy(Proxy connectionProxy) {
		this.connectionProxy = connectionProxy;
	}

	public static SimpleHttpClient getThis(){
		return SpringContextHolder.getBean(SimpleHttpClient.class);
	}
	
	
}
