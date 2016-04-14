package org.whale.system.auth.init;

import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import org.whale.system.auth.scan.AuthAnnotationScaner;
import org.whale.system.common.constant.OrderNumConstant;
import org.whale.system.common.util.Bootable;

/**
 * 权限扫描器初始化系统注册的权限
 *
 * @author wjs
 * @date 2015年1月5日 下午5:28:53
 */
@Component
public class AuthBeansCreator implements Bootable{
	
	private static Logger logger = LoggerFactory.getLogger(AuthBeansCreator.class);

	@Override
	public Object init(Map<String, Object> context) {
		logger.info("@Auth: 权限模型扫描开始...");
		AuthAnnotationScaner.authScan();
		logger.info("@Auth: 权限模型扫描完成！");
		return null;
	}

	@Override
	public boolean access() {
		return true;
	}

	@Override
	public int getOrder() {
		return OrderNumConstant.AUTH_BEAN_INIT_ORDER;
	}

}
