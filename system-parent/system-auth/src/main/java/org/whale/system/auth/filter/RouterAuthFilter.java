package org.whale.system.auth.filter;

import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.aspectj.lang.annotation.Pointcut;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.whale.system.auth.service.IauthManager;

/**
 * http://itindex.net/detail/50710-springaop-controller-service
 * http://tiangai.iteye.com/blog/2103708
 *
 * @author wjs
 * @date 2014年12月31日 下午3:07:48
 */
@Component
@Aspect
public class RouterAuthFilter {
	
	@Autowired
	private IauthManager authManager;

	@Pointcut("@annotation(org.whale.system.annotation.auth.Auth)")
	public void authAspect() {
	}

	@Before("authAspect()")
	public void doBefore(JoinPoint joinPoint) {
		this.authManager.authCheck(joinPoint);
	}
}
