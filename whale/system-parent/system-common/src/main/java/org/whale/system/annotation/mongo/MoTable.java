package org.whale.system.annotation.mongo;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.TYPE})
@Documented
public @interface MoTable {

	/**
	 * 集合名
	 * @return
	 * @date 2015年5月13日 下午3:38:59
	 */
	String value();
}
