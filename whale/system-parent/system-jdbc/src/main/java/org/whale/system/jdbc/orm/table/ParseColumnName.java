package org.whale.system.jdbc.orm.table;

/**
 * 字段名与数据库名转换规则器
 *
 * @author 王金绍
 * 2014年9月6日-下午1:59:13
 */
public interface ParseColumnName {

	/**
	 * 
	 *功能说明: 根据字段名称获取 数据库字段名
	 *创建人: 王金绍
	 *创建时间:2013-3-14 下午5:35:14
	 *@param columnAttr
	 *@return String
	 *
	 */
	String getDbColName(String columnAttr);
}
