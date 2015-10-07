package org.whale.system.base;

/**
 * 单表动态sql语句
 * 
 * @author 王金绍
 *
 */
public interface Iquery {
	
	/**
	 * 获取单条记录语句
	 * @return
	 */
	public String getSql(SqlType sqlType);
	
	/**
	 * 获取语句对应的参数
	 * @return
	 */
	public Object[] getArgs();
	
	
	/**
	 * 获取SQL语句
	 * 
	 * QUERY 查询语句
	 * GET 获取单条记录语句，没有order
	 * COUNT 获取总数语句
	 * DEL 删除记录语句
	 * 
	 * @author 王金绍
	 */
	public static enum SqlType{
		QUERY,GET, COUNT, DEL
	}
}
