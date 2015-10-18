package org.whale.system.spring;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.handler.SimpleMappingExceptionResolver;
import org.whale.system.common.exception.BaseException;
import org.whale.system.common.exception.NotLoginException;
import org.whale.system.common.util.WebUtil;

/**
 * 统一异常处理
 * http://gaojiewyh.iteye.com/blog/1297746
 *
 * @author 王金绍
 * 2014年9月18日-下午4:27:21
 */
public class WhaleExceptionResolver extends SimpleMappingExceptionResolver {
	
	private static final Logger logger = LoggerFactory.getLogger(WhaleExceptionResolver.class);

	@Override
	protected ModelAndView doResolveException(HttpServletRequest request,
			HttpServletResponse response, Object handler, Exception ex) {
		
		logger.error(ex.getMessage(), ex);
		
		// Expose ModelAndView for chosen error view.  
        String viewName = determineViewName(ex, request);  
        if (viewName != null) {// JSP格式返回  
            if (!(request.getHeader("accept").indexOf("application/json") > -1 || (request  
                    .getHeader("X-Requested-With")!= null && request  
                    .getHeader("X-Requested-With").indexOf("XMLHttpRequest") > -1))) {  
                // 如果不是异步请求  
                // Apply HTTP status code for error views, if specified.  
                // Only apply it if we're processing a top-level request.  
                Integer statusCode = determineStatusCode(request, viewName);  
                if (statusCode != null) {  
                    applyStatusCodeIfPossible(request, response, statusCode);  
                }  
                return getModelAndView(viewName, ex, request);  
            } else {
            	if(ex instanceof NotLoginException){
            		response.setStatus(401);//401 用户未登录，提示用户重新登录
            	}else if(ex instanceof BaseException){
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
