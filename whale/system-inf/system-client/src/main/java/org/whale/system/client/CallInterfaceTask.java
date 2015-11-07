package org.whale.system.client;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.util.Map.Entry;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.whale.system.common.exception.HttpClientException;
import org.whale.system.common.util.Strings;

/**
 * Http 接口服务调用任务
 * 
 * @author 王金绍
 * 2015年11月8日 上午12:55:07
 */
public class CallInterfaceTask implements Runnable {
	
	private static final Logger logger = LoggerFactory.getLogger(CallInterfaceTask.class);
	
	private ClientContext context;
	
	public CallInterfaceTask(ClientContext clientContext){
		this.context = clientContext;
	}

	@Override
	public void run() {
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
			
			con.connect();
			OutputStream ops = con.getOutputStream();
			try{
				OutputStreamWriter writer = new OutputStreamWriter(ops, "UTF-8");
				if(context.getArg() != null){
					writer.write(context.getReqStr());
					if(logger.isDebugEnabled()){
						logger.debug("HTTP-body:{}", context.getReqStr());
					}
				}else{
					if(logger.isDebugEnabled()){
						logger.debug("HTTP-body 为空");
					}
				}
				writer.flush();
			}finally{
				ops.close();
			}
			
			
			resCode = con.getResponseCode();
			
			InputStream ips = con.getInputStream();
			StringBuffer response = new StringBuffer();
			try{
				BufferedReader in = new BufferedReader(new InputStreamReader(ips, "UTF-8"));
				String inputLine;
				
				while ((inputLine = in.readLine()) != null) {
					response.append(inputLine).append("\n");
				}
			}finally{
				ips.close();
			}
			String resStr = response.toString();
			context.setResStr(resStr);
			if(logger.isDebugEnabled()){
				logger.debug("HTTP-response:{}", resStr);
			}
		}catch(Exception e){
			throw new HttpClientException("HTTP接口执行异常", e);
		}
		
		if(resCode > 300){
			throw new HttpClientException("HTTP接口请求返回状态码错误");
		}
	}

}
