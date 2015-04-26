package org.whale.ext.mixDao.orm;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * ID 不能是自增类型，否则批量保存获取不到ID，保存Redis 无key
 *
 * @author 王金绍
 * 2015年4月26日 下午8:27:29
 */
@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.TYPE})
@Documented
public @interface MixOrm {

	/**
	 * 缓存前缀名字
	 * @return
	 * 2015年4月26日 上午8:15:24
	 */
	String cacheName();
	
	/**
	 *  缓存时间
	 * @return
	 * 2015年4月26日 上午8:16:13
	 */
	int cacheTime();
	
}
