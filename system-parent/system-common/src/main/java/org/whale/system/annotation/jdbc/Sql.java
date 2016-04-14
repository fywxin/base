package org.whale.system.annotation.jdbc;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * 插入数据库前字段sql定义
 *
 * @author wjs
 * 2014年9月6日-下午2:03:23
 */
@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.FIELD})
@Documented
public @interface Sql {

	String value();
	/** 插入数据库前执行 */
	boolean isPre() default true;
	/** 当前字段值为空时才执行 */
	boolean onlyNull() default true;
}
