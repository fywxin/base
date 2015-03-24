package org.whale.system.jdbc.util;

import java.util.Set;

/**
 * 使用的数据库类型
 *
 * @author 王金绍
 * 2014年9月7日-下午4:29:46
 */
public class DbKind {
	
	public static String dbType = "MYSQL";
	
	public static Set<String> tables = null;
	
	/**0:关闭创建表功能，1：强制创建 2：增量创建 */
	public static Integer createTableModel = 2;

	/**
	 * mysql
	 * @return
	 */
	public static boolean isMysql(){
		return "MYSQL".equals(dbType);
	}
	
	/**
	 * oracle
	 * @return
	 */
	public static boolean isOracle(){
		return "ORACLE".equals(dbType);
	}
	
	/**
	 * 是否存在表
	 * @param tableName
	 * @return
	 * @Date 2015年3月12日 下午5:43:36
	 */
	public static boolean isTableExist(String tableName){
		if(tables == null)
			return false;
		return tables.contains(tableName.toUpperCase());
	}
	
}
