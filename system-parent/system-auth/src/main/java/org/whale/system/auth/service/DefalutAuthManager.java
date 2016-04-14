package org.whale.system.auth.service;

import java.util.Set;

import org.aspectj.lang.JoinPoint;
import org.springframework.beans.factory.annotation.Autowired;
import org.whale.system.auth.cache.UserAuthCacheService;
import org.whale.system.auth.domain.AuthBean;
import org.whale.system.auth.domain.UserAuth;
import org.whale.system.base.UserContext;
import org.whale.system.common.exception.ForbidVisitException;
import org.whale.system.common.exception.NotLoginException;
import org.whale.system.common.util.ThreadContext;

public class DefalutAuthManager implements IauthManager {
	
	@Autowired
	private UserAuthCacheService userAuthCacheService;

	@Override
	public void authCheck(JoinPoint joinPoint) {
		UserContext uc = (UserContext)ThreadContext.getContext().get(ThreadContext.KEY_USER_CONTEXT);
		if(uc == null){
			throw new NotLoginException("用户未登录！");
		}
		if(!uc.isSuperAdmin()){
			String targetName = joinPoint.getTarget().getClass().getName(); 
			String methodName = joinPoint.getSignature().getName();
			
			String method = targetName+"#"+methodName;
			
			UserAuth auth = userAuthCacheService.getUserAuth(uc.getUserId());
			
			Set<String> authCodes = auth.getAuthCodes();
			if(authCodes == null || authCodes.size() < 1){
				throw new ForbidVisitException("无权限访问");
			}
			boolean hasRight = false;
			for(String authCode : authCodes){
				AuthBean authBean = AuthBean.get(authCode);
				if(authBean != null){
					if(authBean.getMethods().contains(method)){
						hasRight = true;
						break;
					}
				}
			}
			
			if(!hasRight){
				throw new ForbidVisitException("无权限访问");
			}
		}
	}

}
