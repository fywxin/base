package org.whale.system.annotation.jdbc;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * 表
 *
 * @author wjs
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
	 * 保证插入只有一条记录，重复记录将被忽略  insert ignore into
	 * 【确定存在唯一主键，使用方式2】
	 * 兼容ORACLE 带验证！
	 *
	 * 参考：http://www.suiyiwen.com/question/4293
	 *
	 *  uniqueIndex 为false 时，采用方式 1
	 *  方式1： insert into ...select ... not exists
	 *  方式2：insert ignore into
	 *
	 *  @Unique与@Column(unique=true)区别：
	 *
	 *  @Unique： 发现重复记录，丢弃本次插入，不抛出异常
	 *  @Column(unique=true): 先查询，然后再插入，并不能保证唯一的原子性，同时会抛出异常
	 *
	 * Created by 王金绍 on 2016/5/26.
	 */
	boolean unique() default false;
	
	/**
	 * 
	 * 字段到数据库转换的规则
	 * 相同规则，驼峰规则
	 * SAME : 数据库与java属性名称一致  userName userName
	 * CAMEL2UNDERLINE_UPPER : 驼峰规则转下划线大写  userName USER_NAME
	 * CAMEL2UNDERLINE_LOWER : 驼峰规则转下划线小写 userName user_name
	 * 
	 * @author wjs
	 * 2015年10月13日 下午10:31:18
	 */
	public static enum ColumnFormat {
		SAME, CAMEL2UNDERLINE_UPPER, CAMEL2UNDERLINE_LOWER
	}
}
