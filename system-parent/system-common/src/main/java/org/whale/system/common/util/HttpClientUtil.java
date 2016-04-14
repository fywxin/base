package org.whale.system.common.util;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.apache.http.NameValuePair;
import org.apache.http.StatusLine;
import org.apache.http.client.HttpClient;
import org.apache.http.client.HttpResponseException;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.methods.HttpUriRequest;
import org.apache.http.entity.StringEntity;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.params.CoreConnectionPNames;
import org.apache.http.params.HttpParams;
import org.apache.http.util.EntityUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.whale.system.common.exception.HttpClientException;
import org.whale.system.common.exception.HttpClientIOException;
import org.whale.system.spring.SpringContextHolder;


/**
 * 
 *功能说明: httpClient 4.0 工具类
 *
 *创建人:wjs
 */
public class HttpClientUtil {
	
	private static final Logger logger = LoggerFactory.getLogger("PoolHttpClient");
	// 客户端 
	private static HttpClient httpClient;
	
	
	public static String get(String url, Map<String, String> headers){
		return get(url, headers, "UTF-8");
	}
	
	public static String get(String url, Map<String, String> headers, String resCharset){
		return get(url, headers, "UTF-8", null);
	}
	
	public static String get(String url, Map<String, String> headers, String resCharset, Integer timeout){
		return get(url, headers, "UTF-8", null, timeout);
	}
	
	/**
	 * 
	 * @param url 
	 * @param headers 请求头信息
	 * @param resCharset  返回字符集编码
	 * @param contimeout 超时时间
	 * @param readtimeout 超时时间
	 * @return
	 */
	public static String get(String url, Map<String, String> headers, String resCharset, Integer contimeout, Integer readtimeout) {
		if(logger.isDebugEnabled()){
			logger.debug("url: {} headers：{} resCharset: {}", url, headers, resCharset);
		}
		
		HttpGet httpGet = new HttpGet(url);

		if (headers != null && !headers.isEmpty()) {
			for (Map.Entry<String, String> entry : headers.entrySet()) {
				httpGet.setHeader(entry.getKey(), entry.getValue());
			}
		}
		return doExecute(httpGet, url, resCharset, contimeout, readtimeout);
	}
	
	
	public static String post(String url, Map<String, String> headers, String postStr){
		return post(url, headers, postStr, "UTF-8", "UTF-8");
	}
	
	
	public static String post(String url, Map<String, String> headers, String postStr, String reqCharset, String resCharset) {
		
		return post(url, headers, postStr, reqCharset, resCharset, null);
	}
	
	public static String post(String url, Map<String, String> headers, String postStr, String reqCharset, String resCharset, Integer timeout) {
		
		return post(url, headers, postStr, reqCharset, resCharset, null, timeout);
	}
	
	/**
	 * 
	 * @param url
	 * @param headers 头部信息
	 * @param postStr 请求内容
	 * @param reqCharset 请求参数编码
	 * @param resCharset 返回结果解码
	 * @param contimeout 超时时间
	 * @param readtimeout 超时时间
	 * @return
	 */
	public static String post(String url, Map<String, String> headers, String postStr, String reqCharset, String resCharset, Integer contimeout, Integer readtimeout) {
		if(logger.isDebugEnabled()){
			logger.debug("url: {} headers：{} charset: {} postStr: {}", url, headers, postStr);
		}
		
		HttpPost httpPost = new HttpPost(url);

		if (headers != null && !headers.isEmpty()) {
			for (Map.Entry<String, String> entry : headers.entrySet()) {
				httpPost.addHeader(entry.getKey(), entry.getValue());
			}
		}
		if(Strings.isNotBlank(postStr)){
			try {
				httpPost.setEntity(new StringEntity(postStr.trim(), reqCharset));
			} catch (UnsupportedEncodingException e) {
				throw new HttpClientException(e);
			}
		}
		return doExecute(httpPost, url, resCharset, contimeout, readtimeout);
	}
	
	public static String post(String url, Map<String, String> params, Map<String, String> headers){
		return post(url, params, headers, "UTF-8");
	}
	

	public static String post(String url, Map<String, String> params, Map<String, String> headers, String resCharset) {
		
		return post(url, params, headers, resCharset, null);
	}
	
	public static String post(String url, Map<String, String> params, Map<String, String> headers, String resCharset, Integer timeout) {
		
		return post(url, params, headers, resCharset, null, timeout);
	}
	
	/**
	 * post 请求 
	 * @param url 地址
	 * @param params 参数对
	 * @param headers 头
	 * @param resCharset 返回结果解码
	 * @param contimeout 超时时间
	 * @param readtimeout 超时时间
	 * @return
	 */
	public static String post(String url, Map<String, String> params, Map<String, String> headers, String resCharset, Integer contimeout, Integer readtimeout) {
		if(logger.isDebugEnabled()){
			logger.debug("url: {} params：{} headers：{} resCharset: {}", url, params, headers, resCharset);
		}
		
		HttpPost httpPost = new HttpPost(url);
		
		if(params != null && params.size() > 0) {
			List<NameValuePair> parameters = new ArrayList<NameValuePair>(params.size());
			for (Map.Entry<String, String> entry : params.entrySet()) {
				parameters.add(new BasicNameValuePair(entry.getKey(), entry.getValue()));
			}
			UrlEncodedFormEntity formEntiry;
			try {
				formEntiry = new UrlEncodedFormEntity(parameters);
				httpPost.setEntity(formEntiry);
			} catch (UnsupportedEncodingException e) {
				throw new HttpClientException(e);
			}
		}
		
		if (headers != null && !headers.isEmpty()) {
			for (Map.Entry<String, String> entry : headers.entrySet()) {
				httpPost.addHeader(entry.getKey(), entry.getValue());
			}
		}
		
		return doExecute(httpPost, url, resCharset, contimeout, readtimeout);
	}
	
	/**
	 * http 请求执行体
	 * 
	 * @param request
	 * @param url
	 * @param charset
	 * @param conTimeout 超时时间
	 * @param readTimeout 超时时间
	 * @return
	 */
	private static String doExecute(HttpUriRequest request, String url, String charset, Integer conTimeout, Integer readTimeout){
		try {
			if(conTimeout != null || readTimeout != null){
				HttpParams httpParams = getHttpClient().getParams();
				if(conTimeout != null && conTimeout > 0){
					httpParams.setIntParameter(CoreConnectionPNames.CONNECTION_TIMEOUT,  conTimeout);
				}
				if(readTimeout != null && readTimeout > 0){
					httpParams.setIntParameter(CoreConnectionPNames.SO_TIMEOUT,  readTimeout);
				}
				request.setParams(httpParams);
			}
			
			
			HttpResponse response = getHttpClient().execute(request);
			if(response == null)
				throw new HttpResponseException(404,"url: ["+url+"] response == null ");
			
			StatusLine statusLine = response.getStatusLine();
			if (statusLine.getStatusCode() >= HttpStatus.SC_MULTIPLE_CHOICES) {
				if(response != null){
					EntityUtils.consume(response.getEntity());
				}
				request.abort();
				logger.warn("状态码错误！ url: {} state: {} resp: {}", url, statusLine.getStatusCode(), statusLine.getReasonPhrase());
				throw new HttpResponseException(statusLine.getStatusCode(), statusLine.getReasonPhrase());
			}else if(statusLine.getStatusCode() == HttpStatus.SC_OK){
				HttpEntity httpEntity = response.getEntity();
				String resStr = EntityUtils.toString(httpEntity, charset);
				EntityUtils.consume(httpEntity);  
				
				if(logger.isDebugEnabled()){
					logger.debug("url: {} 返回内容 :\n {}" ,url, resStr);
				}
				return resStr;
			}
		//http://www.blogjava.net/wangxinsh55/archive/2012/07/16/383210.html
		//发生异常，一定要catch，然后调用 httpGet.abort(); 来释放连接，否则会 CLOSE_WAIT 数量大多，导致连接被占用完
		//org.apache.http.conn.ConnectionPoolTimeoutException: Timeout waiting for connection
		}  catch (IOException e) {
			request.abort();
			throw new HttpClientIOException(e);
		} catch (Exception e) {
			request.abort();
			throw new HttpClientException(e);
		} 
		
		if(logger.isDebugEnabled()){
			logger.debug("url: {} 返回内容：空", url);
		}
		return null;
	}
	
	public static HttpClient getHttpClient() {
		if(httpClient == null){
			httpClient = SpringContextHolder.getBean(HttpClientPoolConUtil.class).getHttpclient();
		}
		return httpClient;
	}
	
}
