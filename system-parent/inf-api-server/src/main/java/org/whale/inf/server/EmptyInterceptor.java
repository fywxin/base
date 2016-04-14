package org.whale.inf.server;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;
import org.whale.system.common.util.ThreadContext;

public class EmptyInterceptor extends HandlerInterceptorAdapter {
	@Override
	public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
		String uri = request.getRequestURI().substring(request.getContextPath().length());
		ThreadContext.getContext().put(ThreadContext.KEY_LOG_CREATE_TIME, System.currentTimeMillis());
		ThreadContext.getContext().put(ThreadContext.KEY_LOG_URI, uri);
		ThreadContext.getContext().put(ThreadContext.KEY_FROM_WEB, true);
		ThreadContext.getContext().put(ThreadContext.KEY_REQUEST, request);
		
		return super.preHandle(request, response, handler);
	}
}
