package org.whale.system.base;

import java.util.List;

/**
 * 单表动态sql语句
 * 
 * @author 王金绍
 *
 */
public interface Iquery {

	/**
	 * 获取条件删除语句
	 * @return
	 */
	public String getDelSql();
	
	/**
	 * 获取单条记录语句
	 * @return
	 */
	public String getGetSql();
	
	/**
	 * 获取 查询记录语句
	 * @return
	 */
	public String getQuerySql();
	
	/**
	 * 获取总记录数语句
	 * @return
	 */
	public String getCountSql();
	
	/**
	 * 获取语句对应的参数
	 * @return
	 */
	public List<Object> getArgs();
}
