package org.whale.system.spring;

import java.io.IOException;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.handler.SimpleMappingExceptionResolver;
import org.whale.system.common.exception.BaseException;
import org.whale.system.common.exception.BusinessException;
import org.whale.system.common.exception.NotLoginException;
import org.whale.system.common.exception.OrmException;
import org.whale.system.common.exception.SysException;
import org.whale.system.common.util.ThreadContext;
import org.whale.system.common.util.WebUtil;
import org.whale.system.domain.Log;
import org.whale.system.filter.LogFilter;

public class WhaleLogExceptionResolver extends SimpleMappingExceptionResolver {

	private static final Logger logger = LoggerFactory.getLogger(WhaleLogExceptionResolver.class);
	
	@SuppressWarnings("all")
	@Autowired
	private LogFilter logFilter;

	@Override
	protected ModelAndView doResolveException(HttpServletRequest request,
			HttpServletResponse response, Object handler, Exception ex) {
		
		logger.error(ex.getMessage(), ex);
		
		Log log = (Log)ThreadContext.getContext().get(ThreadContext.KEY_LOG_PREX);
		
		if(log == null){
			log = this.logFilter.newLog();
		}
		
		if(ex != null){
			log.setRsStr(getExceptionAllinfo(ex));
			if(ex instanceof BusinessException || ex.getCause() instanceof BusinessException){
				log.setRsType(Log.RS_BusinessException);
			}else if(ex instanceof OrmException || ex.getCause() instanceof OrmException){
				log.setRsType(Log.RS_OrmException);
			}else if(ex instanceof SysException || ex.getCause() instanceof SysException){
				log.setRsType(Log.RS_SysException);
			}else if(ex instanceof RuntimeException || ex.getCause() instanceof RuntimeException){
				log.setRsType(Log.RS_RunTimeException);
			}else{
				log.setRsType(Log.RS_OTHER);
			}
		}
		
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
            		response.addHeader("login", "1");
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
	
	public static String getExceptionAllinfo(Exception ex){
		StringBuilder strb = new StringBuilder();
        StackTraceElement[] trace = ex.getStackTrace();
        
        for (StackTraceElement s : trace) {
        	strb.append("\tat ").append(s).append("\r\n");
        }
        return strb.toString();
 }
}
