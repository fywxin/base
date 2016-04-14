package org.whale.inf.server;

import java.lang.annotation.Annotation;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.MethodParameter;
import org.springframework.web.context.request.NativeWebRequest;
import org.springframework.web.method.support.HandlerMethodReturnValueHandler;
import org.springframework.web.method.support.ModelAndViewContainer;
import org.whale.inf.common.EncryptService;
import org.whale.inf.common.SignService;
import org.whale.system.annotation.web.RespBody;
import org.whale.system.common.util.PropertiesUtil;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.serializer.SerializerFeature;

public class RespBodyMethodReturnValueHandler implements HandlerMethodReturnValueHandler {
	
	private static final Logger logger = LoggerFactory.getLogger("server");
	
	@Autowired(required=false)
	private EncryptService encryptService;
	
	@Autowired(required=false)
	private SignService signService;
	
	@Autowired
	private ServerIntfFilterRunner filterRunner;

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
			context.setResponse(response);
			
			filterRunner.exeBeforeResp(context);
			out = response.getOutputStream();
			
			String text =  null;
			if(PropertiesUtil.getValueBoolean("inf."+context.getAppId()+".serializer", true)){//是否开启
				text = JSON.toJSONString(returnValue, SerializerFeature.WriteClassName);
			}else{
				text = JSON.toJSONString(returnValue);
			}
			
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
	        
	        logger.info("响应报文:{}", text);
	        
	        filterRunner.exeAfterResp(context);
	        
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
