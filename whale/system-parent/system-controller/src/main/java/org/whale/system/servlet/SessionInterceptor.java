package org.whale.system.servlet;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;
import org.whale.system.base.UserContext;
import org.whale.system.common.constant.SysConstant;
import org.whale.system.common.exception.RefreshAuthException;
import org.whale.system.common.util.Strings;
import org.whale.system.common.util.ThreadContext;

public class SessionInterceptor extends HandlerInterceptorAdapter {
	
	private static final Logger logger = LoggerFactory.getLogger(SessionInterceptor.class);

	@Override
	public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
		ThreadContext.getContext().put(ThreadContext.KEY_REQUEST, request);
		ThreadContext.getContext().put(ThreadContext.KEY_RESPONSE, response);
		if(SysConstant.isRefreshAuth){
			int time = 5;
			while(SysConstant.isRefreshAuth){
				if(time > 0){
					Thread.sleep(1000);
				}else{
					logger.warn("权限缓存刷新超过5秒还未完成！");
					if (request.getHeader("x-requested-with") != null&& request.getHeader("x-requested-with").equalsIgnoreCase("XMLHttpRequest")) {
						throw new RefreshAuthException("系统正刷新权限缓存，请稍等后再进入");
					} else {
						response.setHeader("Location", "/waiting");
						response.sendRedirect("/waiting");
						return false;
					}
				}
				time-- ;
			}
		}
		
		String uri = request.getRequestURI().substring(request.getContextPath().length());
		
		if(!("".equals(uri) || "/login".equals(uri))){
			HttpSession session = request.getSession();
			UserContext uc = (UserContext) session.getAttribute(SysConstant.USER_CONTEXT_KEY);
			if(uc == null && session.isNew()){
				String jsessionid = (String) request.getParameter("jsessionid");
				if (Strings.isNotBlank(jsessionid)) {
					session = MySessionContext.getSession(jsessionid);
					uc = (UserContext) session.getAttribute(SysConstant.USER_CONTEXT_KEY);
					ThreadContext.getContext().put(ThreadContext.KEY_USER_CONTEXT, uc);
				}
			}else{
				ThreadContext.getContext().put(ThreadContext.KEY_USER_CONTEXT, uc);
			}
		}
		return super.preHandle(request, response, handler);
	}

}
