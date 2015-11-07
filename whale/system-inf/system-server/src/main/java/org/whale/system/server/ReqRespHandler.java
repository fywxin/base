package org.whale.system.server;

import org.springframework.core.MethodParameter;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.support.WebDataBinderFactory;
import org.springframework.web.context.request.NativeWebRequest;
import org.springframework.web.method.support.ModelAndViewContainer;

/**
 * 自定义标签 @ReqBody @ResBody 执行器
 * 
 * @author 王金绍
 * 2015年10月31日 上午12:23:54
 */
public interface ReqRespHandler {

	/**
	 * @ReqBody 参数值读取前执行
	 * @param parameter
	 * @param mavContainer
	 * @param webRequest
	 * @param binderFactory
	 */
	public void beforeResolveArgument(MethodParameter parameter,
			ModelAndViewContainer mavContainer, NativeWebRequest webRequest,
			WebDataBinderFactory binderFactory);
	
	/**
	 * 参数合法性校验处理
	 * @param parameter
	 * @param mavContainer
	 * @param webRequest
	 * @param binderFactory
	 * @param binder
	 * @param argument
	 */
	public void validateArgument(MethodParameter parameter,ModelAndViewContainer mavContainer, NativeWebRequest webRequest,
			WebDataBinderFactory binderFactory, WebDataBinder binder, Object argument);
	
	/**
	 * @ReqBody 参数值读取完成后执行
	 * @param parameter
	 * @param mavContainer
	 * @param webRequest
	 * @param binderFactory
	 * @param argument
	 */
	public void afterResolveArgument(MethodParameter parameter,
			ModelAndViewContainer mavContainer, NativeWebRequest webRequest,
			WebDataBinderFactory binderFactory, Object argument);
	
	/**
	 * @ResBody 执行时执行
	 * @param returnValue
	 * @param returnType
	 * @param mavContainer
	 * @param webRequest
	 */
	public void beforeHandleReturnValue(Object returnValue,
			MethodParameter returnType, ModelAndViewContainer mavContainer,
			NativeWebRequest webRequest);
	
	/**
	 * @ResBody 执行时执行
	 * @param returnValue
	 * @param returnType
	 * @param mavContainer
	 * @param webRequest
	 */
	public void afterHandleReturnValue(Object returnValue,
			MethodParameter returnType, ModelAndViewContainer mavContainer,
			NativeWebRequest webRequest);
	
	/**
	 * 读取二进制流前执行
	 * 
	 * @param datas
	 * @return
	 */
	public byte[] onRead(byte[] datas);
	
	/**
	 * 写入二进制流前执行
	 * @param datas
	 * @return
	 */
	public byte[] onWrite(byte[] datas);
	
}
