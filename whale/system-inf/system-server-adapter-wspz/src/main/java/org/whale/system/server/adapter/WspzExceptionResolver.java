package org.whale.system.server.adapter;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.handler.SimpleMappingExceptionResolver;
import org.whale.system.common.exception.FieldValidErrorException;
import org.whale.system.common.util.WebUtil;
import org.whale.system.inf.ErrorCode;
import org.whale.system.inf.Result;
import org.whale.system.server.ServerException;

/**
 * 接口异常处理
 * 
 * @author 王金绍
 * 2015年11月8日 上午12:19:15
 */
public class WspzExceptionResolver extends SimpleMappingExceptionResolver {

	@Override
	protected ModelAndView doResolveException(HttpServletRequest request,
			HttpServletResponse response, Object handler, Exception ex) {
		
		String viewName = determineViewName(ex, request);
		if (viewName != null) {// JSP格式返回
			if (!(request.getHeader("accept").indexOf("application/json") > -1 
					|| (request.getHeader("X-Requested-With") != null && request.getHeader("X-Requested-With").indexOf("XMLHttpRequest") > -1))) {
				
				Integer statusCode = determineStatusCode(request, viewName);// 如果不是异步请求
				if (statusCode != null) {
					applyStatusCodeIfPossible(request, response, statusCode);
				}
				return getModelAndView(viewName, ex, request);
			} else {
				Result<?> rs = null;
				if (ex instanceof ServerException) {
					ServerException ServerException = (ServerException)ex;
					rs = Result.fail(ServerException.getCode(), ServerException.getMessage());
 				} else if(ex instanceof FieldValidErrorException){
 					FieldValidErrorException fieldEx = (FieldValidErrorException)ex;
 					rs = Result.fail(ErrorCode.FIELD_VALID_ERROR, fieldEx.getError());
 				} else {
 					rs = Result.fail(ErrorCode.UNKNOW_ERROR);
				}
				WebUtil.print(response, rs);
				return null;
			}
		} else {
			return null;
		}
	}
}
