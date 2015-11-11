package org.whale.system.server.adapter;

import java.io.UnsupportedEncodingException;
import java.lang.annotation.Annotation;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.MethodParameter;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.support.WebDataBinderFactory;
import org.springframework.web.context.request.NativeWebRequest;
import org.springframework.web.method.support.ModelAndViewContainer;
import org.whale.system.annotation.jdbc.Validate;
import org.whale.system.common.encrypt.EncryptUtil;
import org.whale.system.common.exception.FieldValidErrorException;
import org.whale.system.common.exception.SysException;
import org.whale.system.common.util.PropertiesUtil;
import org.whale.system.common.util.ReflectionUtil;
import org.whale.system.common.util.ThreadContext;
import org.whale.system.common.util.ValidUtil;
import org.whale.system.inf.ErrorCode;
import org.whale.system.server.EmptyReqRespHandler;
import org.whale.system.server.ServerException;

/**
 * 服务端响应处理器
 * 
 * @author 王金绍
 * 2015年11月8日 上午12:14:16
 */
@Component
public class WspzReqRespHandler extends EmptyReqRespHandler{
	
	private static final Logger logger = LoggerFactory.getLogger(WspzReqRespHandler.class);

	public void beforeResolveArgument(MethodParameter parameter, ModelAndViewContainer mavContainer, NativeWebRequest webRequest, WebDataBinderFactory binderFactory) {
		WspzContext context = new WspzContext();
		ThreadContext.getContext().put(WspzContext.THREAD_KEY, context);
		HttpServletRequest request = (HttpServletRequest)webRequest.getNativeRequest();
		context.setUri(this.getUri(webRequest));
		context.setAppId(request.getParameter("appId"));
		context.setSign(request.getParameter("sign"));
		context.setSignKey(PropertiesUtil.getValue("appId."+context.getAppId()));
		context.setReqno(request.getParameter("reqno"));
		
		if(logger.isDebugEnabled()){
			logger.debug("beforeResolveArgument {}", context);
		}
	}

	public void afterResolveArgument(MethodParameter parameter, ModelAndViewContainer mavContainer, NativeWebRequest webRequest, WebDataBinderFactory binderFactory, Object argument) {
		WspzContext context = (WspzContext)ThreadContext.getContext().get(WspzContext.THREAD_KEY);
		context.setArgument(argument);
		
		StringBuilder strb = new StringBuilder();
		strb.append(context.getUri())
			.append(context.getSignKey());
		if(argument != null){
			strb.append(context.getReqStr());
		}
		
		try {
			String calSign = EncryptUtil.md5(strb.toString().getBytes("utf-8"));
			if(logger.isDebugEnabled()){
				logger.debug("afterResolveArgument签名校验：{}， 待签名串：{} \nserverSign:{}, clientSign:{}", calSign.equals(context.getSign()), strb, calSign, context.getSign());
			}
			if(!calSign.equals(context.getSign())){
				throw new ServerException(ErrorCode.SIGN_ERROR);
			}
		} catch (UnsupportedEncodingException e) {
			logger.error("afterResolveArgument签名校验异常，编码错误!", e);
			throw new ServerException(ErrorCode.SIGN_ERROR);
		}
	}

	@Override
	public void validateArgument(MethodParameter parameter,
			ModelAndViewContainer mavContainer, NativeWebRequest webRequest,
			WebDataBinderFactory binderFactory, WebDataBinder binder,
			Object argument) {
		
		Annotation[] annotations = parameter.getParameterAnnotations();
		if(annotations != null && annotations.length > 0){
			for (Annotation ann : annotations) {
				if(ann.annotationType() == Validate.class){
					Validate vals = (Validate)ann;
					if(ReflectionUtil.isBaseDataType(argument.getClass())){
						String msg = ValidUtil.valid(argument, vals);
						if(msg != null){
							throw new FieldValidErrorException(msg);
						}
					}else{
						Map<String, String> error = ValidUtil.valid(argument);
						if(error != null && error.size() > 0){
							throw new FieldValidErrorException(error);
						}
					}
				}
			}
		}
	}

	@Override
	public void afterHandleReturnValue(Object returnValue, MethodParameter returnType, ModelAndViewContainer mavContainer, NativeWebRequest webRequest) {
		WspzContext context = (WspzContext)ThreadContext.getContext().get(WspzContext.THREAD_KEY);
		context.setRs(returnValue);
		if(logger.isDebugEnabled()){
			logger.debug("afterHandleReturnValue {}", context);
		}
		HttpServletResponse response = (HttpServletResponse)webRequest.getNativeResponse();
		response.addHeader("Connection", "keep-alive");
	}

	public byte[] onRead(byte[] datas) {
		WspzContext context = (WspzContext)ThreadContext.getContext().get(WspzContext.THREAD_KEY);
		if(context == null){
			throw new SysException("接口系统参数对象为空，请copy线程上下文数据");
		}
		context.setReqStr(new String(datas));
		if(datas.length == 0){//body 为空时，防止反序列化异常
			datas = "{}".getBytes();
		}
		return datas;
	}

	public byte[] onWrite(byte[] datas) {
		WspzContext context = (WspzContext)ThreadContext.getContext().get(WspzContext.THREAD_KEY);
		if(context == null){
			throw new SysException("接口系统参数对象为空，请copy线程上下文数据");
		}
		context.setResStr(new String(datas));
		return datas;
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
