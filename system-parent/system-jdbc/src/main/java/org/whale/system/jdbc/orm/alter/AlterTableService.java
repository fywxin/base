package org.whale.system.jdbc.orm.alter;

/**
 * 创建表格
 *
 * @author wjs
 * 2014年9月6日-下午1:51:02
 */
public interface AlterTableService {
	
	public void createTable(Class<?> clazz);
	
	public void dropTable(Class<?> clazz);
}
