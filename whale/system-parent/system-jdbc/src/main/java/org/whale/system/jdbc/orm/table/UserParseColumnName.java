package org.whale.system.jdbc.orm.table;

import org.springframework.core.PriorityOrdered;

/**
 * 用户自定义的个别表字段名与数据库名转换规则
 *
 * @author wjs
 * 2014年9月6日-下午1:59:34
 */
public interface UserParseColumnName extends ParseColumnName, PriorityOrdered  {

	/** 该表是否运用用户自定义的类型转换实现类 */
	boolean match(String entryName);
}
