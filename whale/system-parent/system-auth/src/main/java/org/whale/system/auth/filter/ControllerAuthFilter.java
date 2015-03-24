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
 *
 * @author 王金绍
 * @date 2014年12月31日 下午3:07:48
 */
@Component
@Aspect
public class ControllerAuthFilter {
	
	@Autowired
	private IauthManager authManager;

	@Pointcut("@annotation(org.whale.system.auth.annotation.Auth)")
	public void authAspect() {
	}

	@Before("authAspect()")
	public void doBefore(JoinPoint joinPoint) {
		this.authManager.authCheck(joinPoint);
	}
}
