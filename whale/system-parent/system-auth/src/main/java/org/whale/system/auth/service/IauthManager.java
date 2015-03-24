package org.whale.system.auth.service;

import org.aspectj.lang.JoinPoint;

public interface IauthManager {

	public void authCheck(JoinPoint joinPoint);
}
