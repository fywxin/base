package org.whale.inf.server;

import java.io.IOException;
import java.io.InputStream;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.io.IOUtils;
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
import org.whale.system.common.util.ThreadContext;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;

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
 * @author 王金绍
 * @date 2015年11月12日 下午5:32:07
 */
public class ReqParamMethodArgumentResolver implements HandlerMethodArgumentResolver{
	
	@Autowired(required=false)
	private EncryptService encryptService;
	@Autowired(required=false)
	private SignService signService;

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
		ServerContext context = (ServerContext)ThreadContext.getContext().get(ServerContext.THREAD_KEY);
		if(context == null){
			context = new ServerContext();
			context.setParamIndex(0);
			byte[] datas = this.readBodyByte(webRequest);
			if(datas != null && datas.length > 0){
				//解密
				if(encryptService != null){
					datas = encryptService.decrypt(datas, context);
				}
				context.setBody(new String(datas, "UTF-8"));
				JSONArray bodyJsonArr = JSON.parseArray(context.getBody());
				context.setBodyJsonArr(bodyJsonArr);
				
				param = this.decode(parameter, context);
				context.addArg(context.getParamIndex(), param);
				context.incParamIndex();
			}else{
				context.setBody(null);
			}
			//签名校验
			if(signService != null){
				String sign = this.signService.signReq(context);
				if(!sign.equals(webRequest.getParameter("sign"))){
					throw new InfException(ResultCode.SIGN_ERROR);
				}
			}
			ThreadContext.getContext().put(ServerContext.THREAD_KEY, context);
		}else{
			param = this.decode(parameter, context);
			context.addArg(context.getParamIndex(), param);
			context.incParamIndex();
		}
		
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
		if(ReflectionUtil.isBaseDataType(parameter.getParameterType())){
			return jsonArray.getObject(context.getParamIndex(), parameter.getParameterType());
		}else{
			return jsonArray.getObject(context.getParamIndex(), parameter.getParameterType());
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
}
