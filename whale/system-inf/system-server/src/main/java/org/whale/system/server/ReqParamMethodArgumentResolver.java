package org.whale.system.server;

import java.io.IOException;
import java.io.InputStream;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.io.IOUtils;
import org.springframework.core.MethodParameter;
import org.springframework.http.HttpInputMessage;
import org.springframework.http.server.ServletServerHttpRequest;
import org.springframework.web.bind.support.WebDataBinderFactory;
import org.springframework.web.context.request.NativeWebRequest;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.method.support.ModelAndViewContainer;
import org.whale.system.annotation.web.ReqParam;
import org.whale.system.common.util.ThreadContext;

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

	@Override
	public boolean supportsParameter(MethodParameter parameter) {
		return parameter.hasParameterAnnotation(ReqParam.class);
	}

	/**
	 * !!!! 线程上下文要清除
	 * 直接清除  ThreadContext 对象
	 */
	@Override
	public Object resolveArgument(MethodParameter parameter,
			ModelAndViewContainer mavContainer, NativeWebRequest webRequest,
			WebDataBinderFactory binderFactory) throws Exception {
		
		ServerContext context = (ServerContext)ThreadContext.getContext().get(ServerContext.THREAD_KEY);
		try{
		if(context == null){
			context = new ServerContext();
			context.setIndex(0);
			context.setData(this.readBodyByte(webRequest));
			//context.setBodyStr(this.readBody(webRequest));
			if(context.getBody() == null){
				return null;
			}
			ThreadContext.getContext().put(ServerContext.THREAD_KEY, context);
		}else{
			context.increaseIndex();
		}
		}catch(Exception e){
			e.printStackTrace();
			throw e;
		}
		return context.getObject(parameter.getParameterType());
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
			throw new ServerException(e);
		}
	}
}
