package org.whale.system.spring;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.handler.SimpleMappingExceptionResolver;
import org.whale.system.common.exception.BaseException;
import org.whale.system.common.util.WebUtil;

public class SimpleWhaleExceptionResolver  extends SimpleMappingExceptionResolver {

	private static final Logger logger = LoggerFactory.getLogger(SimpleWhaleExceptionResolver.class);
	
	@Override
	protected ModelAndView doResolveException(HttpServletRequest request,
			HttpServletResponse response, Object handler, Exception ex) {
		
		logger.error(ex.getMessage(), ex);
			 
        String viewName = determineViewName(ex, request);  
        if (viewName != null) {// JSP格式返回  
            if (!(request.getHeader("accept").indexOf("application/json") > -1 || (request  
                    .getHeader("X-Requested-With")!= null && request  
                    .getHeader("X-Requested-With").indexOf("XMLHttpRequest") > -1))) {  
                // 如果不是异步请求    
                Integer statusCode = determineStatusCode(request, viewName);  
                if (statusCode != null) {  
                    applyStatusCodeIfPossible(request, response, statusCode);  
                }  
                return getModelAndView(viewName, ex, request);  
            } else {
            	if(ex instanceof BaseException){
            		WebUtil.fail(response, ex.getMessage());
            	}else{
            		WebUtil.fail(response, "业务处理出现未知异常，请联系管理员");
            	}
                return null; 
            }  
        } else {  
            return null;  
        }  
	}
}
