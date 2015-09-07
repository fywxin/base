package org.whale.system.annotation.jdbc;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * 数据库字段定义
 *
 * @author 王金绍
 * 2014年9月6日-下午2:03:10
 */
@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.FIELD})
@Documented
public @interface Column {

	//int type() default Types.VARCHAR;
	/** 规则接口，默认使用驼峰原则实现方式 */
	String name() default "";
	/** 中文名，提示与代码生成器配合 */
	String cnName() ;
	/**为一个字段声明默认值,发现一个字段没有被设值时，会用你声明的这个 默认值填出字段，再执行操作。以支持书写类似 '@Default("${name}@gmail.com")' 这样的语法， 其中类似 ${XXXXX} */
	String defaultValue() default "";
//	/** 字段长度 */
//	int width() default 0;
//	/** 小数点精确度 */
//	int precision() default 0;
	/** 是否唯一 */
	boolean unique() default false;  //OrmException  前台保存抓取到OrmException 自动提示已经存在
//	/** 是否可为空 */
//	boolean nullable() default true; //OrmException
	/** 是否不能更新*/
	boolean updateable() default true;
	
}
