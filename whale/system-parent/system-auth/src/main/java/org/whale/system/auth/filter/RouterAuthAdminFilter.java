package org.whale.system.auth.filter;

import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.aspectj.lang.annotation.Pointcut;
import org.springframework.stereotype.Component;
import org.whale.system.base.UserContext;
import org.whale.system.common.exception.ForbidVisitException;
import org.whale.system.common.exception.NotLoginException;

/**
 * 管理员权限标签过滤
 *
 * @author wjs
 * @date 2015年1月5日 下午5:22:04
 */
@Component
@Aspect
public class RouterAuthAdminFilter {

	@Pointcut("@annotation(org.whale.system.annotation.auth.AuthAdmin)")
	public void authAdminAspect() {
	}
	
	@Before("authAdminAspect()")
	public void doBefore(JoinPoint joinPoint) {
		UserContext uc = UserContext.get();
		if(uc == null){
			throw new NotLoginException("用户未登录！");
		}
		if(!uc.isSuperAdmin()){
			throw new ForbidVisitException("无权限访问");
		}
		
	}
}
