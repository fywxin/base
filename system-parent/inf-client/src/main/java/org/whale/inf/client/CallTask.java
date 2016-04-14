package org.whale.inf.client;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.util.Date;
import java.util.Map.Entry;
import java.util.concurrent.atomic.AtomicLong;

import org.apache.commons.io.IOUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.whale.inf.common.EncryptService;
import org.whale.inf.common.InfException;
import org.whale.inf.common.Result;
import org.whale.inf.common.ResultCode;
import org.whale.inf.common.SignService;
import org.whale.system.common.util.Strings;
import org.whale.system.common.util.TimeUtil;

import com.alibaba.fastjson.JSON;

/**
 * Http 接口服务调用任务
 * 
 * @author wjs
 * 2015年11月8日 上午12:55:07
 */
public class CallTask implements Runnable {
	
	private static final Logger logger = LoggerFactory.getLogger(CallTask.class);
	
	private static final AtomicLong seq = new AtomicLong(10000);
	
	private ClientContext context;
	
	private ClientCodec clientCodec;
	
	private EncryptService encryptService;
	
	private SignService signService;
	
	private ClientIntfFilterRunner filterRunner;
	
	private ClientConf clientConf;
	
	
	public CallTask(ClientContext clientContext, ClientCodec clientCodec, EncryptService encryptService, SignService signService, ClientIntfFilterRunner filterRunner, ClientConf clientConf){
		this.context = clientContext;
		this.clientCodec = clientCodec;
		this.encryptService = encryptService;
		this.signService = signService;
		this.filterRunner = filterRunner;
		this.clientConf = clientConf;
	}

	@Override
	public void run() {
		filterRunner.exeBefore(context);
		
		String nextSeq = clientConf.getAppId()+":"+seq.incrementAndGet();
		context.setReqno(nextSeq);
		context.setUrl(clientConf.getServerHost()+context.getServiceUrl()+"/"+context.getMethod().getName());
		context.putParam("appId", clientConf.getAppId())
			.putParam("reqno", nextSeq)
			.putParam("timestamp", TimeUtil.formatTime(new Date(), TimeUtil.COMMON_FORMAT));
		
		byte[] data = null;
		//TODO 改为调用链模式
		if(context.getArgs() != null && context.getArgs().size() > 0){
			//请求对象编码
			data = this.clientCodec.encode(context.getArgs(), context);
			//加密报文
			if(this.encryptService != null){
				data = this.encryptService.encrypt(data, context);
			}
		}
		//执行调用前方法、请求头签名
		if(this.signService != null){
			context.getParams().put("sign", this.signService.signReq(context));
		}
		
		OutputStream ops = null;
		InputStream ips = null;
		int resCode = 0;
		try{
			//拼接请求url和参数
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
			
			//连接打开
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
			con.setRequestProperty("Connection", "keep-alive");// 维持长连接  
			con.setRequestProperty("Charset", "UTF-8");
			
			//设置请求头部
			if(context.getHeaders().size() > 0){
				for (Entry<String, String> entry : context.getHeaders().entrySet()) {
					con.setRequestProperty(entry.getKey(), entry.getValue());
				}
			}
			filterRunner.exeOnRequest(context);
			if(logger.isDebugEnabled()){
				logger.debug("HTTP-request:{}\nHTTP-header:{}", 
								(context.getReqStr() == null ? JSON.toJSONString(context.getArgs()) : context.getReqStr()),
								context.getHeaders());
			}
			//发起接口请求调用
			con.connect();
			ops = con.getOutputStream();
			if(context.getArgs() != null && context.getArgs().size() > 0){
				ops.write(data);
				ops.flush();
			}
			//读取http 返回数据
			resCode = con.getResponseCode();
			ips = con.getInputStream();
			data = IOUtils.toByteArray(ips);
			
			//HTTP状态码错误异常抛出
			if(resCode != HttpURLConnection.HTTP_OK){
				logger.error("HTTP接口返回状态码["+resCode+"]错误\n"+new String(data, "UTF-8"));
				throw new InfException(ResultCode.NET_ERROR);
			}
			
			//返回结果解密
			if(this.encryptService != null){
				String encrypt = con.getHeaderField("encrypt");
				if("true".equals(encrypt)){
					data = this.encryptService.decrypt(data, context);
				}
			}
			
			//反序列化返回结果
			Object rs = this.clientCodec.decode(data, context);
			if(logger.isDebugEnabled()){
				logger.debug("HTTP-response:{}", context.getRespStr() == null ? JSON.toJSONString(rs) : context.getRespStr());
			}
			
			//返回结果非rs对象校验
			if(rs == null){
				logger.error("返回对象为空");
				throw new InfException(ResultCode.RESP_DATA_ERROR);
			}
			if(!(rs instanceof Result)){
				logger.error("返回对象类型非 Result对象:{}\n"+context.getRespStr());
				throw new InfException(ResultCode.RESP_DATA_ERROR);
			}
			
			//附返回值给代理接口方法
			Result<?> result = (Result<?>)rs;
			Result.set(result);
			
			//校验返回结果签名
			if(this.signService != null){
				String sign = con.getHeaderField("sign");
				if(Strings.isNotBlank(sign)){
					String rsSign = this.signService.signResp(context);
					if(!sign.equals(rsSign)){
						throw new InfException(ResultCode.SIGN_ERROR);
					}
				}
			}
			
			if(context.getMethod().getReturnType().equals(Result.class)){
				context.setRs(result);
			}else{
				if(result.isSuccess()){
					context.setRs(result.getData());
				}else{
					throw new InfException(result.getCode(), result.getMsg());
				}
			}
		}catch(IOException e){
			filterRunner.exeException(context, e);
			throw new InfException(ResultCode.NET_ERROR, e);
		}catch(InfException e){
			filterRunner.exeException(context, e);
			throw e;
		}finally{
			IOUtils.closeQuietly(ops);
			IOUtils.closeQuietly(ips);
		}
		
		filterRunner.exeAfter(context);
	}

}
