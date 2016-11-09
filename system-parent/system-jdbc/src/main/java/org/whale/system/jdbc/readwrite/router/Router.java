package org.whale.system.jdbc.readwrite.router;

import java.util.List;

import javax.sql.DataSource;

/**
 * 路由选择
 * 
 * @author 王金绍
 *
 */
public interface Router {

	/**
	 * 路由选择, 返回数据源数组下标
	 * 
	 * 返回-1，表示没有找到匹配的，使用 writeDataSource
	 *
	 * @param readDsList
	 * @param readDsKeyList
	 * @return
	 */
	String route(List<DataSource> readDsList, List<String> readDsKeyList);
	
}
