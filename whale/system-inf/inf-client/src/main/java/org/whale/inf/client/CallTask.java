package org.whale.inf.client;

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
import org.whale.inf.common.EncryptService;
import org.whale.inf.common.SignService;
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
	
	private ClientCodec clientCodec;
	
	private EncryptService encryptService;
	
	private SignService signService;
	
	private ClientInvokeHandler clientInvokeHandler;
	
	
	public CallTask(ClientContext clientContext, ClientCodec clientCodec, EncryptService encryptService, SignService signService, ClientInvokeHandler clientInvokeHandler){
		this.context = clientContext;
		this.clientCodec = clientCodec;
		this.encryptService = encryptService;
		this.signService = signService;
		this.clientInvokeHandler = clientInvokeHandler;
	}

	@Override
	public void run() {
		this.clientInvokeHandler.beforeCall(context);
		byte[] data = null;
		//TODO 改为调用链模式
		if(context.getArgs() != null && context.getArgs().size() > 0){
			//编码
			data = this.clientCodec.encode(context.getArgs(), context);
			//加密报文
			if(this.encryptService != null){
				data = this.encryptService.onWrite(data, context);
			}
			//执行调用前方法、请求头签名
			if(this.signService != null){
				String sign = this.signService.sign(context);
				if(Strings.isNotBlank(sign)){
					context.getParams().put("sign", sign);
				}
			}
		}
		System.out.println("data == "+data);
		
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
			
			this.clientInvokeHandler.onReqest(context);
			con.connect();
			OutputStream ops = null;
			InputStream ips = null;
			try{
				if(logger.isDebugEnabled()){
					logger.debug("HTTP-request:{}", context.getReqStr() == null ? JSON.toJSONString(context.getArgs()) : context.getReqStr());
				}
				ops = con.getOutputStream();
				
				if(context.getArgs() != null && context.getArgs().size() > 0){
					ops.write(data);
					ops.flush();
				}
				resCode = con.getResponseCode();
				
				ips = con.getInputStream();
				data = IOUtils.toByteArray(ips);
				if(this.encryptService != null){
					data = this.encryptService.onRead(data, context);
				}
				
				Object rs = this.clientCodec.decode(data, context);
				context.setRs(rs);
				if(logger.isDebugEnabled()){
					logger.debug("HTTP-response:{}", context.getRespStr() == null ? JSON.toJSONString(context.getRs()) : context.getRespStr());
				}
			}catch(Exception e){
				e.printStackTrace();
			}finally{
				if(ops != null){
					ops.close();
				}
				if(ips != null){
					ips.close();
				}
			}
			
			if(resCode > 300){
				throw new HttpClientException("HTTP接口返回状态码["+resCode+"]错误, 返回信息:\n"+context.getRespStr());
			}
		}catch(IOException e){
			throw new HttpClientException("HTTP接口执行异常", e);
		}
		this.clientInvokeHandler.afterCall(context);
	}

}
