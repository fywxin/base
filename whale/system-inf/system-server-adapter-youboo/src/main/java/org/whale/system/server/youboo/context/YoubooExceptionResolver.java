package org.whale.system.server.youboo.context;

import java.io.UnsupportedEncodingException;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.handler.SimpleMappingExceptionResolver;
import org.whale.system.common.encrypt.AESUtil;
import org.whale.system.common.util.Strings;
import org.whale.system.common.util.ThreadContext;
import org.whale.system.common.util.WebUtil;
import org.whale.system.inf.ErrorCode;
import org.whale.system.inf.Result;
import org.whale.system.server.ServerException;

import com.alibaba.fastjson.JSON;

public class YoubooExceptionResolver extends SimpleMappingExceptionResolver {
	private static final Logger logger = LoggerFactory.getLogger(YoubooExceptionResolver.class);

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
				Result rs = null;
				if (ex instanceof ServerException) {
					ServerException ServerException = (ServerException)ex;
					rs = Result.fail(ServerException.getCode(), ServerException.getMessage());
 				} else {
 					rs = Result.fail(ErrorCode.UNKNOW_ERROR);
				}
				String json = JSON.toJSONString(rs);
				YoubooContext context = (YoubooContext)ThreadContext.getContext().get(YoubooContext.THREAD_KEY);
				response.addHeader("reqno", context.getReqParam().getReqno());
				if(Strings.isNotBlank(context.getResSecure())){
					response.addHeader("encrypt", "1");
					try {
						WebUtil.print(response, AESUtil.encrypt(json.getBytes("utf-8"), context.getResSecure().getBytes("utf-8")));
					} catch (UnsupportedEncodingException e) {
						logger.error("编码错误", e);
					}
				}else{
					response.addHeader("encrypt", "0");
					WebUtil.print(response, json);
				}
				return null;
			}
		} else {
			return null;
		}
	}
}
