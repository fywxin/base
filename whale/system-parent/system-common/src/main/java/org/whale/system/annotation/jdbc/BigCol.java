package org.whale.system.annotation.jdbc;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * 大数据字段：
 * 
 * 主表对象将不保存此值，值保存再特定的打字段值表中
 * 
 * 目的： 提高效率
 * 
 * key： tableName + colName + id
 * 
 * @author 王金绍
 *
 */
@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.FIELD})
@Documented
public @interface BigCol {

	/**是否在缓存中保存一份数据，如redis */
	boolean cache() default true;
	
	/**是否保存到数据库中，cache和saveDb 必须至少一个为true */
	boolean saveDb() default true;
	
	/**不保存到统一表时，用户自定义保存的表名称  */
	String tableName() default "";
	/**不保存到统一表时，用户自定义保存的字段名称 */
	String colName() default "";
}
