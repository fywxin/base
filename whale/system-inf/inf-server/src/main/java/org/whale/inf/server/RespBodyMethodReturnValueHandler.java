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

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.serializer.SerializerFeature;

public class RespBodyMethodReturnValueHandler implements HandlerMethodReturnValueHandler {
	
	@Autowired(required=false)
	private EncryptService encryptService;
	
	@Autowired(required=false)
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
		ServerContext context = ServerContext.get();
		ServletOutputStream out = null;
		try{
			HttpServletResponse response = webRequest.getNativeResponse(HttpServletResponse.class);
			out = response.getOutputStream();
			
			//String text = JSON.toJSONString(obj, this.getFeatures());
	        //保证客户端可以反序列化成功
	        String text = JSON.toJSONString(returnValue, SerializerFeature.WriteClassName);
	        context.setRespStr(text);
	        
	        response.addHeader("reqno", context.getReqno());
	        
	        byte[] datas = text.getBytes("UTF-8");
	        if(signService != null){
	        	String sign = this.signService.signResp(context);
	        	response.addHeader("sign", sign);
	        }
	        if(this.encryptService != null && context.getRespEncryptFlag()){
	        	datas = this.encryptService.encrypt(datas, context);
	        	response.addHeader("encrypt", "true");
	        }
	        
	        out.write(datas);
	        out.flush();
		}finally{
			ServerContext.remove();
			if(out != null){
				out.close();
			}
		}
	}

}
