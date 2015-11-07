package org.whale.system.server;

import org.springframework.core.MethodParameter;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.support.WebDataBinderFactory;
import org.springframework.web.context.request.NativeWebRequest;
import org.springframework.web.method.support.ModelAndViewContainer;

/**
 * 空扩展
 * 
 * @author 王金绍
 * 2015年10月31日 上午1:16:03
 */
public class EmptyReqRespHandler implements ReqRespHandler {

	@Override
	public void beforeResolveArgument(MethodParameter parameter,
			ModelAndViewContainer mavContainer, NativeWebRequest webRequest,
			WebDataBinderFactory binderFactory) {
	}

	@Override
	public void validateArgument(MethodParameter parameter,
			ModelAndViewContainer mavContainer, NativeWebRequest webRequest,
			WebDataBinderFactory binderFactory, WebDataBinder binder,
			Object argument) {
	}

	@Override
	public void afterResolveArgument(MethodParameter parameter,
			ModelAndViewContainer mavContainer, NativeWebRequest webRequest,
			WebDataBinderFactory binderFactory, Object argument) {
	}

	@Override
	public void beforeHandleReturnValue(Object returnValue,
			MethodParameter returnType, ModelAndViewContainer mavContainer,
			NativeWebRequest webRequest) {
	}
	
	@Override
	public void afterHandleReturnValue(Object returnValue,
			MethodParameter returnType, ModelAndViewContainer mavContainer,
			NativeWebRequest webRequest) {
	}

	@Override
	public byte[] onRead(byte[] datas) {
		return datas;
	}

	@Override
	public byte[] onWrite(byte[] datas) {
		return datas;
	}

}
