package org.whale.system.annotation.jdbc;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * 乐观锁字段
 * 
 * 使用场景： 高并发环境update操作
 * 
 * 使用要求：
 * 		1.字段类型必须是 Integer 或 Long， 
 * 		2.每张表最多只有一个乐观锁字段
 * 		3.初始值必须null
 * 		4.保存时，如果为null，系统将设置为1
 * 
 * 原理：
 * 	UPDATE T_USER u
 *  	SET u.address = #address#, u.version = u.version + 1
 *	WHERE u.id = #id#
 * 		AND u.version = #version#
 * 
 * 脏数据异常：  StaleObjectStateException
 * 
 * @author 王金绍
 * @date 2015年7月17日 下午4:40:30
 */
@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.FIELD})
@Documented
public @interface OptimisticLock {

}
