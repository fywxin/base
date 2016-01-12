package org.whale.system.annotation.jdbc;

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
	String value();
	/** 实体中文名 */
	String cnName() default "";
	/** java字段转数据库字段规则 */
	ColumnFormat colFormat() default ColumnFormat.SAME;
	
	/** 数据库表对应的序列名 ORACLE */
	String sequence() default "";
	/** 数据库拥有人 ORACLE */
	String schema() default "";
	
	/**
	 * 
	 * 字段到数据库转换的规则
	 * 相同规则，驼峰规则
	 * SAME : 数据库与java属性名称一致  userName userName
	 * CAMEL2UNDERLINE_UPPER : 驼峰规则转下划线大写  userName USER_NAME
	 * CAMEL2UNDERLINE_LOWER : 驼峰规则转下划线小写 userName user_name
	 * 
	 * @author 王金绍
	 * 2015年10月13日 下午10:31:18
	 */
	public static enum ColumnFormat {
		SAME, CAMEL2UNDERLINE_UPPER, CAMEL2UNDERLINE_LOWER
	}
}
