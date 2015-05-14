package org.whale.system.annotation.auth;


import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * 权限控制体系
 * 
 * 方法覆盖类上的配置
 *
 * @author 王金绍
 * 2014年9月6日-下午2:03:16
 */
@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.METHOD })
@Documented
public @interface Auth {
	
	/**
	 * 权限编码 多个使用逗号分割
	 * @return
	 * @date 2014年12月22日 下午9:35:57
	 */
	String code();
	
	/**
	 * 权限名称
	 * @return
	 */
	String name();
	
}
