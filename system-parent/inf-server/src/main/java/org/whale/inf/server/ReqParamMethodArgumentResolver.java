package org.whale.inf.server;

import java.io.IOException;
import java.io.InputStream;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.io.IOUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.MethodParameter;
import org.springframework.http.HttpInputMessage;
import org.springframework.http.server.ServletServerHttpRequest;
import org.springframework.web.bind.support.WebDataBinderFactory;
import org.springframework.web.context.request.NativeWebRequest;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.method.support.ModelAndViewContainer;
import org.whale.inf.common.EncryptService;
import org.whale.inf.common.InfException;
import org.whale.inf.common.ResultCode;
import org.whale.inf.common.SignService;
import org.whale.system.annotation.web.ReqParam;
import org.whale.system.common.util.ReflectionUtil;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;

/**
 * 实现思路
 * 	客户端 ： 多个参数 JSON 数组序列化，放置到body中
 * 	服务端 : 	1. 从request 的body 中获取请求串
 * 			2. 反序列化请求串为JSONArray 对象
 * 			3. @ReqParam 解析器记录当前参数解析位置，并放置到线程上下文中，并自增位置
 * 
 * 	服务端空参数解决办法
 * 			对空参数，配合切面来实现，切面用于校验签名的合法性，判断方法有@ResBody
 * 
 * 	插件：	1. 流读取前
 * 
 * @author wjs
 * @date 2015年11月12日 下午5:32:07
 */
public class ReqParamMethodArgumentResolver implements HandlerMethodArgumentResolver {
	
	private static final Logger logger = LoggerFactory.getLogger("server");
	
	@Autowired(required=false)
	private EncryptService encryptService;
	
	@Autowired(required=false)
	private SignService signService;
	
	@Autowired
	private ServerIntfFilterRunner filterRunner;

	@Override
	public boolean supportsParameter(MethodParameter parameter) {
		return parameter.hasParameterAnnotation(ReqParam.class);
	}

	/**
	 * 处理完成后 !!!! 线程上下文要清除
	 * 直接清除  ThreadContext 对象
	 */
	@Override
	public Object resolveArgument(MethodParameter parameter,
			ModelAndViewContainer mavContainer, NativeWebRequest webRequest,
			WebDataBinderFactory binderFactory) throws Exception {
		Object param = null;
		ServerContext context = ServerContext.get();
		if(context == null){
			context = new ServerContext();
			filterRunner.exeBeforeReq(context);
			
			context.setRequest((HttpServletRequest)webRequest.getNativeRequest());
			context.setUri(this.getUri(webRequest));
			context.setAppId(webRequest.getParameter("appid") == null ? webRequest.getParameter("appId") : webRequest.getParameter("appid"));
			context.setReqno(webRequest.getParameter("reqno"));
			context.setSession(webRequest.getParameter("session"));
			context.setTimestamp(webRequest.getParameter("timestamp"));
			context.setVersion(webRequest.getParameter("version"));
			context.setSign(webRequest.getParameter("sign"));
			context.setFormat(webRequest.getParameter("format"));
			context.setGzip(webRequest.getParameter("gzip") == null ? false : "1".equals(webRequest.getParameter("gzip")));
			
			context.setParamIndex(0);
			byte[] datas = this.readBodyByte(webRequest);
			if(datas != null && datas.length > 0){
				//解密
				if(encryptService != null){
					datas = encryptService.decrypt(datas, context);
				}
				context.setReqStr(new String(datas, "UTF-8"));
				
				//入参数是数组
				if(context.getReqStr().startsWith("[") || context.getReqStr().trim().startsWith("[")) {
					JSONArray bodyJsonArr = JSON.parseArray(context.getReqStr());
					context.setBodyJsonArr(bodyJsonArr);
				}
				
				param = this.decode(parameter, context);
				context.addArg(context.getParamIndex(), param);
				context.incParamIndex();
			}else{
				context.setReqStr(null);
			}
			
			logger.info("请求报文：{}\n请求参数：{}", context.getReqStr(), context.toParamStr());
			
			//签名校验
			if(signService != null){
				String sign = this.signService.signReq(context);
				if(!sign.equals(context.getSign())){
					throw new InfException(ResultCode.SIGN_ERROR);
				}
			}
			ServerContext.set(context);
		}else{
			if(context.getBodyJsonArr() == null){
				logger.error("接口超过一个参数，入参必须是数组!");
				throw new InfException(ResultCode.REQ_DATA_ERROR);
			}
			param = this.decode(parameter, context);
			context.addArg(context.getParamIndex(), param);
			context.incParamIndex();
		}
		filterRunner.exeAfterReq(context);
		return param;
	}
	
	/**
	 * TODO 测试list 与 数组  嵌套对象， Date
	 * 
	 * @param parameter
	 * @param context
	 * @return
	 */
	private Object decode(MethodParameter parameter, ServerContext context) {
		JSONArray jsonArray = context.getBodyJsonArr();
		if(jsonArray != null){
			if(ReflectionUtil.isBaseDataType(parameter.getParameterType())){
				return jsonArray.getObject(context.getParamIndex(), parameter.getParameterType());
			}else{
//				if(parameter.getMethod().getParameterTypes().length == 1){
//					return JSON.parseArray(context.getReqStr(), parameter.getParameterType());
//				}else{
//					return jsonArray.getObject(context.getParamIndex(), parameter.getParameterType());
//				}
				return jsonArray.getObject(context.getParamIndex(), parameter.getParameterType());
			}
		//对方传入的是对象，而不是数组，那么只能只有一个参数
		}else{
			if(ReflectionUtil.isBaseDataType(parameter.getParameterType())){
				JSONObject jsonObject = JSON.parseObject(context.getReqStr());
				if(jsonObject.keySet() != null && jsonObject.keySet().size() == 1){
					return jsonObject.getObject(jsonObject.keySet().iterator().next(), parameter.getParameterType());
				}else{
					throw new InfException(ResultCode.REQ_DATA_ERROR);
				}
			}else{
				return JSON.parseObject(context.getReqStr(), parameter.getParameterType());
			}
		}
		
	}
	
	/**
	 * 从request 中读取body 信息
	 * 
	 * @param webRequest
	 * @return
	 * @throws IOException 
	 */
	private byte[] readBodyByte(NativeWebRequest webRequest){
		final HttpServletRequest servletRequest = webRequest.getNativeRequest(HttpServletRequest.class);
		HttpInputMessage inputMessage = new ServletServerHttpRequest(servletRequest);
		try{
			InputStream inputStream = inputMessage.getBody();
			if (inputStream == null) {
				return null;
			} else if (inputStream.markSupported()) {
				inputStream.mark(1);
				if (inputStream.read() == -1) {
					return null;
				}
				inputStream.reset();
			} else {
//				final PushbackInputStream pushbackInputStream = new PushbackInputStream(inputStream);
//				int b = pushbackInputStream.read();
//				if (b == -1) {
//					return null;
//				}
//				else {
//					pushbackInputStream.unread(b);
//				}
//				inputMessage = new ServletServerHttpRequest(servletRequest) {
//					@Override
//					public InputStream getBody() throws IOException {
//						// Form POST should not get here
//						return pushbackInputStream;
//					}
//				};
			}
			return IOUtils.toByteArray(inputStream);
		}catch(IOException e){
			throw new InfException(ResultCode.REQ_DATA_ERROR, e);
		}
	}
	
	/**
	 * 获取请求服务端uri
	 * @param webRequest
	 * @return
	 */
	private String getUri(NativeWebRequest webRequest){
		HttpServletRequest request = (HttpServletRequest)webRequest.getNativeRequest();
		String ctx = request.getContextPath();
		if(ctx != null && ctx.length() > 1){
			return request.getRequestURI().substring(ctx.length());
		}
		return request.getRequestURI();
	}

}
