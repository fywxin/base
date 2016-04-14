package org.whale.inf.server;

import javax.servlet.http.HttpServletRequest;

import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.aspectj.lang.annotation.Pointcut;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.whale.inf.common.InfException;
import org.whale.inf.common.ResultCode;
import org.whale.inf.common.SignService;
import org.whale.system.common.util.ThreadContext;

@Aspect
public class NoMethodArgumentResolver {
	
	private static final Logger logger = LoggerFactory.getLogger("server");
	
	@Autowired(required=false)
	private SignService signService;
	
	@Autowired
	private ServerIntfFilterRunner filterRunner;
	
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
		
		filterRunner.exeBeforeReq(serverContext);
		
		serverContext.setRequest(request);
		serverContext.setAppId(request.getParameter("appid") == null ? request.getParameter("appId") : request.getParameter("appid"));
		serverContext.setReqno(request.getParameter("reqno"));
		serverContext.setUri(this.getUri(request));
		serverContext.setSession(request.getParameter("session"));
		serverContext.setTimestamp(request.getParameter("timestamp"));
		serverContext.setVersion(request.getParameter("version"));
		serverContext.setSign(request.getParameter("sign"));
		serverContext.setFormat(request.getParameter("format"));
		serverContext.setGzip(request.getParameter("gzip") == null ? false : "1".equals(request.getParameter("gzip")));
		
		logger.info("请求报文：空\n请求参数：{}", serverContext.toParamStr());
		
		//签名校验
		if(signService != null){
			String sign = this.signService.signReq(serverContext);
			if(!sign.equals(serverContext.getSign())){
				throw new InfException(ResultCode.SIGN_ERROR);
			}
		}
		
		filterRunner.exeAfterReq(serverContext);
	}
	
	/**
	 * 获取请求服务端uri
	 * @param request
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
