package org.whale.system.annotation.jdbc;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * 数字主键
 *
 * @author wjs
 * 2014年9月6日-下午2:03:16
 */
@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.FIELD})
@Documented
public @interface Id {

	/**是否自增主键，false时，值有开发者提供 */
	boolean auto() default true;
}
