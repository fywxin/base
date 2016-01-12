package org.whale.system.spring;

import java.lang.annotation.Annotation;
import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.InitializingBean;
import org.springframework.core.Conventions;
import org.springframework.core.MethodParameter;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.support.WebDataBinderFactory;
import org.springframework.web.context.request.NativeWebRequest;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.method.support.HandlerMethodReturnValueHandler;
import org.springframework.web.method.support.ModelAndViewContainer;
import org.whale.system.annotation.web.ReqBody;
import org.whale.system.annotation.web.RespBody;

/**
 * 自定义 @ReqBody @RespBody 解析处理器
 * 
 * http://www.codeweblog.com/springmvc%E6%BA%90%E7%A0%81%E6%80%BB%E7%BB%93-%E5%85%AD-mvc-annotation-driven%E4%B8%AD%E7%9A%84handlermethodreturnvaluehandler/
 * 
 * @author 王金绍
 * 2015年10月31日 上午12:15:28
 */
public class ReqRespBodyProcessorProxy implements HandlerMethodArgumentResolver, HandlerMethodReturnValueHandler, InitializingBean {
	
	//真正的参数解析与报文返回处理对象
	private ReqRespBodyProcessor reqRespBodyProcessor;
	
	//外部处理对象
	private ReqRespHandler reqRespHandler;

	public ReqRespBodyProcessorProxy(){
	}
	
	@Override
	public boolean supportsParameter(MethodParameter parameter) {
		return parameter.hasParameterAnnotation(ReqBody.class);
	}
	
	@Override
	public boolean supportsReturnType(MethodParameter returnType) {
		Annotation[] anns = returnType.getMethodAnnotations();
		if(anns != null){
			for(Annotation ann : anns){
				if (RespBody.class.isInstance(ann)) {
					return true;
				}
			}
		}
		
		return false;
	}

	@Override
	public Object resolveArgument(MethodParameter parameter,
			ModelAndViewContainer mavContainer, NativeWebRequest webRequest,
			WebDataBinderFactory binderFactory) throws Exception {

		boolean hasHandler = this.reqRespHandler != null;
		if(hasHandler){//获取req param 参数数据，绑定上下文
			this.reqRespHandler.beforeResolveArgument(parameter, mavContainer, webRequest, binderFactory);
		}
		
		//请求参数解密与校验
		Object argument = reqRespBodyProcessor.readWithMessageConverters(webRequest, parameter, parameter.getGenericParameterType());

		String name = Conventions.getVariableNameForParameter(parameter);
		WebDataBinder binder = binderFactory.createBinder(webRequest, argument, name);

		if (argument != null) {
			//validate(binder, parameter);
			if(hasHandler){//自定义校验标签使用验证
				this.reqRespHandler.validateArgument(parameter, mavContainer, webRequest, binderFactory, binder, argument);
			}
		}

		mavContainer.addAttribute(BindingResult.MODEL_KEY_PREFIX + name, binder.getBindingResult());
		
		
		if(hasHandler){//扫尾工作
			this.reqRespHandler.afterResolveArgument(parameter, mavContainer, webRequest, binderFactory, argument);
		}
		return argument;
	}
	
	@Override
	public void handleReturnValue(Object returnValue,
			MethodParameter returnType, ModelAndViewContainer mavContainer,
			NativeWebRequest webRequest) throws Exception {
		mavContainer.setRequestHandled(true);
		
		if(this.reqRespHandler != null){//返回处理
			this.reqRespHandler.beforeHandleReturnValue(returnValue, returnType, mavContainer, webRequest);
		}
		if (returnValue != null) {
			//返回加密
			reqRespBodyProcessor.writeWithMessageConverters(returnValue, returnType, webRequest);
			if(this.reqRespHandler != null){//返回处理
				this.reqRespHandler.afterHandleReturnValue(returnValue, returnType, mavContainer, webRequest);
			}
		}
	}

	public ReqRespHandler getReqRespHandler() {
		return reqRespHandler;
	}

	public void setReqRespHandler(ReqRespHandler reqRespHandler) {
		this.reqRespHandler = reqRespHandler;
	}

	@Override
	public void afterPropertiesSet() throws Exception {
		FastJsonReqRespHttpMessageConverter msgConverter = new FastJsonReqRespHttpMessageConverter();
		msgConverter.setReqRespHandler(reqRespHandler);
		List<HttpMessageConverter<?>> httpMessageConverters = new ArrayList<HttpMessageConverter<?>>(1);
		httpMessageConverters.add(msgConverter);
		reqRespBodyProcessor = new ReqRespBodyProcessor(httpMessageConverters);
	}

	
}
