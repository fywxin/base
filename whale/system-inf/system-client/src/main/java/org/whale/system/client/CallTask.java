package org.whale.system.client;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.util.Map.Entry;

import org.apache.commons.io.IOUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.whale.system.common.exception.HttpClientException;
import org.whale.system.common.util.Strings;

import com.alibaba.fastjson.JSON;

/**
 * Http 接口服务调用任务
 * 
 * @author 王金绍
 * 2015年11月8日 上午12:55:07
 */
public class CallTask implements Runnable {
	
	private static final Logger logger = LoggerFactory.getLogger(CallTask.class);
	
	private ClientContext context;
	
	public CallTask(ClientContext clientContext){
		this.context = clientContext;
	}

	@Override
	public void run() {
		byte[] data = null;
		//TODO 改为调用链模式
		if(context.getArgs() != null && context.getArgs().length > 0){
			//编码
			data = this.context.getClientCodec().encode(context.getArgs(), context);
			//执行调用前方法、请求头签名
			this.context.getClientInvokeHandler().beforeCall(context);
			//加密报文
			if(this.context.getClientEncrypt() != null){
				data = this.context.getClientEncrypt().onWrite(data, context);
			}
		}
		
		int resCode = 0;
		try{
			String url = context.getUrl();
			if(Strings.isBlank(url)){
				url = context.getServiceUrl();
			}
			if(context.getParams().size() > 0){
				StringBuilder paramStr = new StringBuilder();
				for (Entry<String, Object> entry : context.getParams().entrySet()) {
					paramStr.append("&").append(entry.getKey()).append("=").append(URLEncoder.encode(entry.getValue().toString(), "UTF-8"));
				}
				
				if(url.indexOf("?") == -1){
					url += "?"+paramStr.substring(1);
				}else{
					url += paramStr.toString();
				}
			}
			
			if(logger.isDebugEnabled()){
				logger.debug("HTTP-url:{}", url);
			}
			
			HttpURLConnection con = (HttpURLConnection)new URL(url).openConnection(context.getHttpProxy());
			con.setConnectTimeout(context.getConnectTimeout());
			con.setReadTimeout(context.getReadTimeout());
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
			
			if(context.getHeaders().size() > 0){
				for (Entry<String, String> entry : context.getHeaders().entrySet()) {
					con.setRequestProperty(entry.getKey(), entry.getValue());
				}
			}
			if(logger.isDebugEnabled()){
				logger.debug("HTTP-header:{}", context.getHeaders());
			}
			
			this.context.getClientInvokeHandler().onReqest(context);
			con.connect();
			OutputStream ops = con.getOutputStream();
			try{
				if(logger.isDebugEnabled()){
					logger.debug("HTTP-request:{}", context.getReqStr() == null ? JSON.toJSONString(context.getArgs()) : context.getReqStr());
				}
				
				if(context.getArgs() != null && context.getArgs().length > 0){
					ops.write(data);
					ops.flush();
				}
			}finally{
				ops.close();
			}
			
			resCode = con.getResponseCode();
			
			InputStream ips = con.getInputStream();
			try{
				data = IOUtils.toByteArray(ips);
				data = this.context.getClientEncrypt().onRead(data, context);
				Object rs = this.context.getClientCodec().decode(data, context);
				context.setRs(rs);
			}finally{
				ips.close();
			}
			if(logger.isDebugEnabled()){
				logger.debug("HTTP-response:{}", context.getRespStr() == null ? JSON.toJSONString(context.getRs()) : context.getRespStr());
			}
			
			if(resCode > 300){
				throw new HttpClientException("HTTP接口返回状态码["+resCode+"]错误, 返回信息:\n"+context.getRespStr());
			}
		}catch(IOException e){
			throw new HttpClientException("HTTP接口执行异常", e);
		}
		this.context.getClientInvokeHandler().afterCall(context);
	}

}
