package org.whale.inf.server;

import java.lang.annotation.Annotation;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.MethodParameter;
import org.springframework.web.context.request.NativeWebRequest;
import org.springframework.web.method.support.HandlerMethodReturnValueHandler;
import org.springframework.web.method.support.ModelAndViewContainer;
import org.whale.inf.common.EncryptService;
import org.whale.inf.common.SignService;
import org.whale.system.annotation.web.RespBody;
import org.whale.system.common.util.ThreadContext;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.serializer.SerializerFeature;

public class RespBodyMethodReturnValueHandler implements HandlerMethodReturnValueHandler {
	
	@Autowired
	private EncryptService encryptService;
	
	@Autowired
	private SignService signService;

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
	public void handleReturnValue(Object returnValue, MethodParameter returnType, ModelAndViewContainer mavContainer, NativeWebRequest webRequest) throws Exception {
		//设置true，防止变为页面跳转处理
		mavContainer.setRequestHandled(true);
		ServerContext context = (ServerContext)ThreadContext.getContext().get(ServerContext.THREAD_KEY);
		ServletOutputStream out = null;
		try{
			HttpServletResponse response = webRequest.getNativeResponse(HttpServletResponse.class);
			out = response.getOutputStream();
			
			//String text = JSON.toJSONString(obj, this.getFeatures());
	        //保证客户端可以反序列化成功
	        String text = JSON.toJSONString(returnValue, SerializerFeature.WriteClassName);
	        
	        byte[] bytes = text.getBytes("UTF-8");
	        out.write(bytes);
	        out.flush();
		}finally{
			ThreadContext.getContext().remove(ServerContext.THREAD_KEY);
			if(out != null){
				out.close();
			}
		}
	}

}
