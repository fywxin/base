package org.whale.system.jdbc.orm.table;

/**
 * 实体名与数据库表明，序列名转换规则器
 *
 * @author wjs
 * 2014年9月6日-下午1:59:22
 */
public interface ParseTableName {

	String getDbTableName(String tableName);
	
	String getDbSequence(String tableName);
}
