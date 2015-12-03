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
import org.whale.inf.common.InfException;
import org.whale.inf.common.Result;
import org.whale.inf.common.ResultCode;
import org.whale.inf.common.SignService;
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
				data = this.encryptService.encrypt(data, context);
			}
			//执行调用前方法、请求头签名
			if(this.signService != null){
				String sign = this.signService.signReq(context);
				if(Strings.isNotBlank(sign)){
					context.getParams().put("sign", sign);
				}
			}
		}
		
		OutputStream ops = null;
		InputStream ips = null;
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
			con.setAllowUserInteraction(false);
			con.setDefaultUseCaches(false);
			con.setDoInput(true);
			con.setDoOutput(true);
			con.setUseCaches(false);
			con.setInstanceFollowRedirects(false);
			con.setRequestMethod("POST");
			con.setRequestProperty("content-type", "application/json");
			con.setRequestProperty("accept", "application/json");
			con.setRequestProperty("Connection", "keep-alive");
			
			if(context.getHeaders().size() > 0){
				for (Entry<String, String> entry : context.getHeaders().entrySet()) {
					con.setRequestProperty(entry.getKey(), entry.getValue());
				}
			}
			
			this.clientInvokeHandler.onReqest(context);
			if(logger.isDebugEnabled()){
				logger.debug("HTTP-request:{}\nHTTP-header:{}", 
								(context.getReqStr() == null ? JSON.toJSONString(context.getArgs()) : context.getReqStr()),
								context.getHeaders());
			}
			con.connect();
			ops = con.getOutputStream();
			if(context.getArgs() != null && context.getArgs().size() > 0){
				ops.write(data);
				ops.flush();
			}
			resCode = con.getResponseCode();
			ips = con.getInputStream();
			data = IOUtils.toByteArray(ips);
			
			if(resCode > 300){
				logger.error("HTTP接口返回状态码["+resCode+"]错误\n"+new String(data, "UTF-8"));
				throw new InfException(ResultCode.NET_ERROR);
			}
			
			if(this.encryptService != null){
				String encrypt = con.getHeaderField("encrypt");
				if("true".equals(encrypt)){
					data = this.encryptService.decrypt(data, context);
				}
			}
			
			Object rs = this.clientCodec.decode(data, context);
			if(logger.isDebugEnabled()){
				logger.debug("HTTP-response:{}", context.getRespStr() == null ? JSON.toJSONString(rs) : context.getRespStr());
			}
			
			
			
			if(this.signService != null){
				String sign = con.getHeaderField("sign");
				if(Strings.isNotBlank(sign)){
					String rsSign = this.signService.signResp(context);
					if(!sign.equals(rsSign)){
						throw new InfException(ResultCode.SIGN_ERROR);
					}
				}
			}
			
			if(rs == null || !(rs instanceof Result)){
				logger.error("返回对象为空，或类型非 Result对象:{}\n"+context.getRespStr());
				throw new InfException(ResultCode.RESP_DATA_ERROR);
			}
			if(context.getMethod().getReturnType().equals(Result.class)){
				context.setRs(rs);
			}else{
				Result<?> result = (Result<?>)rs;
				if(result.isSuccess()){
					context.setRs(rs);
				}
			}
			
		}catch(IOException e){
			throw new InfException(ResultCode.NET_ERROR, e);
		}finally{
			IOUtils.closeQuietly(ops);
			IOUtils.closeQuietly(ips);
		}
		this.clientInvokeHandler.afterCall(context);
	}

}
