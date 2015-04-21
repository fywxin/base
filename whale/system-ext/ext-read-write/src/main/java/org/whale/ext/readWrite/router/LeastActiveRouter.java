package org.whale.ext.readWrite.router;

import java.util.List;

import javax.sql.DataSource;

import com.alibaba.druid.pool.DruidDataSource;

/**
 * 最少活跃数据源选择
 * 
 * 使慢的提供者收到更少请求，因为越慢的提供者的调用前后计数差会越大。
 * 
 * @author 王金绍
 */
public class LeastActiveRouter extends AbstractRouter {

	@Override
	@SuppressWarnings("all")
	public int doSelect(DataSource writeDataSource, List<DataSource> readDataSources, List<String> readDataSourceNames) {
		DruidDataSource selDataSource = (DruidDataSource)readDataSources.get(0);
		DruidDataSource compareDataSource = null;
		
		int index = 0;
		for(int i=1; i<readDataSources.size(); i++ ){
			compareDataSource = (DruidDataSource)readDataSources.get(i);
			if((selDataSource.getMaxActive()-selDataSource.getActiveCount()) < (compareDataSource.getMaxActive() - compareDataSource.getActiveCount())){
				selDataSource = compareDataSource;
				index = i;
			}
		}
		
		return index;
	}
	
}
