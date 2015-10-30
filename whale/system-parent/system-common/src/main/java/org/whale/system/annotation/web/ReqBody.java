package org.whale.system.annotation.web;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * 自定义body解析标签，类比@RequstBody
 * 
 * @author 王金绍
 * 2015年10月31日 上午1:16:19
 */
@Target(ElementType.PARAMETER)
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface ReqBody {

	/**
	 * 是否必填
	 * @return
	 */
	boolean required() default true;
}
