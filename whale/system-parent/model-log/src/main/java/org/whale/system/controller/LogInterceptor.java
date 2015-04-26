package org.whale.system.controller;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;
import org.whale.system.common.util.ThreadContext;
import org.whale.system.domain.Log;
import org.whale.system.filter.LogFilter;

public class LogInterceptor extends HandlerInterceptorAdapter {

	@SuppressWarnings("all")
	@Autowired
	private LogFilter LogFilter;

	@Override
	public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
		String uri = request.getRequestURI().substring(request.getContextPath().length());
		ThreadContext.getContext().put(ThreadContext.KEY_LOG_CREATE_TIME, System.currentTimeMillis());
		ThreadContext.getContext().put(ThreadContext.KEY_LOG_URI, uri);
		ThreadContext.getContext().put(ThreadContext.KEY_REQUEST, request);
		ThreadContext.getContext().put(ThreadContext.KEY_RESPONSE, response);
		ThreadContext.getContext().put(ThreadContext.KEY_FROM_WEB, true);
		
		return super.preHandle(request, response, handler);
	}
	
	@Override
	public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex)
			throws Exception {
		try {
			Log log = (Log)ThreadContext.getContext().get(ThreadContext.KEY_LOG_PREX);
			Long costTime = System.currentTimeMillis() - log.getCreateTime();
			log.setCostTime(costTime.intValue());
			
			this.LogFilter.addLogRecvQueue(log);
		} catch (Exception e) {
			
		} finally{
			ThreadContext.removeContext();
		}
	}
}
