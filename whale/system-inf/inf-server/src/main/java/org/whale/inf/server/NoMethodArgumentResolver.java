package org.whale.inf.server;

import javax.servlet.http.HttpServletRequest;

import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.aspectj.lang.annotation.Pointcut;
import org.springframework.beans.factory.annotation.Autowired;
import org.whale.inf.common.InfException;
import org.whale.inf.common.ResultCode;
import org.whale.inf.common.SignService;
import org.whale.system.common.util.ThreadContext;

@Aspect
public class NoMethodArgumentResolver {
	
	@Autowired(required=false)
	private SignService signService;
	
	@Pointcut("@annotation(org.whale.system.annotation.web.RespBody)")
	public void noArgAspect() {
		
	}

	@Before("noArgAspect()")
	public void doBefore(JoinPoint joinPoint) {
		if(joinPoint.getArgs() != null && joinPoint.getArgs().length > 0){
			return ;
		}
		
		HttpServletRequest request = (HttpServletRequest)ThreadContext.getContext().get(ThreadContext.KEY_REQUEST);
		if(request == null){
			throw new RuntimeException("未将Request对象设置到ThreadContext线程上下文中!");
		}
		ServerContext serverContext = new ServerContext();
		ServerContext.set(serverContext);
		serverContext.setAppId(request.getParameter("appId"));
		serverContext.setReqno(request.getParameter("reqno"));
		serverContext.setUri(this.getUri(request));
		
		//签名校验
		if(signService != null){
			String sign = this.signService.signReq(serverContext);
			if(!sign.equals(request.getParameter("sign"))){
				throw new InfException(ResultCode.SIGN_ERROR);
			}
		}
		
		
	}
	
	/**
	 * 获取请求服务端uri
	 * @param webRequest
	 * @return
	 */
	private String getUri(HttpServletRequest request){
		String ctx = request.getContextPath();
		if(ctx != null && ctx.length() > 1){
			return request.getRequestURI().substring(ctx.length());
		}
		return request.getRequestURI();
	}
}
