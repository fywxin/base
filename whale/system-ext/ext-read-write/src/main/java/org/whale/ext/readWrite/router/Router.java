package org.whale.ext.readWrite.router;

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
	 * @param writeDataSource
	 * @param readDataSources
	 * @param readDataSourceNames
	 * @return
	 */
	int route(DataSource writeDataSource, List<DataSource> readDataSources, List<String> readDataSourceNames);
	
}
