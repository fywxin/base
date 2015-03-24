package org.whale.system.jdbc.annotation;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * 表
 *
 * @author 王金绍
 * 2014年9月6日-下午2:03:28
 */
@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.TYPE})
@Documented
public @interface Table {

	/** 数据库表名 */
	String value() default "";
	/** 数据库表对应的序列名 */
	String sequence() default "";
	/** 数据库拥有人 */
	String schema() default "";
	/** 实体中文名 */
	String cnName();
}
