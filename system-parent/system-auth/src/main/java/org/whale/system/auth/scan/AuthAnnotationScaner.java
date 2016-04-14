package org.whale.system.auth.scan;

import java.lang.reflect.Method;
import java.util.HashSet;
import java.util.Set;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.annotation.AnnotationUtils;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Controller;
import org.whale.system.annotation.auth.Auth;
import org.whale.system.auth.domain.AuthBean;
import org.whale.system.common.exception.AuthAnnotationException;
import org.whale.system.common.util.AnnotationScanUtil;
import org.whale.system.common.util.ListUtil;
import org.whale.system.common.util.PropertiesUtil;
import org.whale.system.common.util.Strings;

/**
 * 权限扫描器
 * 
 * @author wjs
 * @date 2014年12月23日 下午10:09:18
 */
@Component
public class AuthAnnotationScaner {
	
	private static Logger logger = LoggerFactory.getLogger(AuthAnnotationScaner.class);

	/**
	 * 扫描系统所有的Router 类
	 * 
	 * @date 2014年12月23日 下午10:09:34
	 */
	public static void authScan(){
		AuthBean.clear();
		String authScanPacke = PropertiesUtil.getValue("auth.scan.package");
		Set<String> packs = ListUtil.toHashSet(ListUtil.split(authScanPacke));
		if (packs == null){
			packs = new HashSet<String>(4);
		}
		packs.add("org.whale.system");

		Set<Class<?>> controllers = new HashSet<Class<?>>(64);
		Set<Class<?>> tmp = null;
		for (String pkg : packs){
			tmp = AnnotationScanUtil.getAnnotationsClass(pkg, Controller.class);
			if (tmp != null && tmp.size() > 0){
				controllers.addAll(tmp);
			}
		}

		if(controllers == null || controllers.size() < 1){
			logger.warn("@Auth: 系统没有发现@Auth的注释类");
		}else{
			logger.info("@Auth: 扫描类集合 "+controllers);
			for(Class<?> controller : controllers) {
				scanController(controller);
			}
		}
	}
	
	/**
	 * 扫描单个controller 类的权限数据
	 * 
	 * @param controller
	 * @date 2014年12月23日 下午10:12:39
	 */
	private static void scanController(Class<?> controller){
		logger.info("@Auth: 扫描类 : "+controller.getName()+" 开始...");
		String className = controller.getName();
		Method[] methods = controller.getDeclaredMethods();
		
		Set<String> ms = null;
		String code = null;
		String[] codes = null;
		for(Method method : methods){
			Auth auth = AnnotationUtils.findAnnotation(method, Auth.class);
			if(auth != null){
				code = auth.code();
				if(Strings.isBlank(code)){
					throw new AuthAnnotationException("权限编码不能为空！"+className+"#"+method.getName());
				}
				codes = code.split(",");
				if(codes.length == 1){
					AuthBean authBean = new AuthBean();
					authBean.setAuthCode(code.trim());
					authBean.setAuthName(auth.name());
					ms = new HashSet<String>(4);
					ms.add(className+"#"+method.getName());
					authBean.setMethods(ms);
					authBean.setController(controller);
					
					AuthBean.put(authBean);
					logger.info("@Auth: 扫描方法注释 : "+authBean);
				}else{
					String[] names = auth.name().split(",");
					for(int i=0; i< codes.length; i++){
						if(Strings.isNotBlank(codes[i])){
							AuthBean authBean = new AuthBean();
							authBean.setAuthCode(codes[i].trim());
							if(codes.length == names.length){
								authBean.setAuthName(names[i]);
							}else{
								authBean.setAuthName(auth.name());
							}
							ms = new HashSet<String>(4);
							ms.add(className+"#"+method.getName());
							authBean.setMethods(ms);
							authBean.setController(controller);
							AuthBean.put(authBean);
							logger.info("@Auth: 扫描方法注释 : "+authBean);
						}
					}
				}
			}
		}
		logger.info("@Auth: 扫描类 : "+controller.getName()+" 完成！");
	}
}
