package org.whale.system.annotation.web;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * 重复提交
 * 
 * @author wjs
 * @date 2015年10月17日 下午10:25:21
 */
@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
public @interface MultiCommit {
	/**
	 * 是否要重定向
	 * @return
	 */
	boolean needRestrict() default false;
}
