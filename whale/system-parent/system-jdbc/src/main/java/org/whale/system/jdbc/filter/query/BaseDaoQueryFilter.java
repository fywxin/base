package org.whale.system.jdbc.filter.query;

import java.io.Serializable;
import java.util.List;
import java.util.Map;

import org.springframework.core.PriorityOrdered;
import org.whale.system.base.Iquery;
import org.whale.system.base.Page;
import org.whale.system.jdbc.IOrmDao;
import org.whale.system.jdbc.filter.OrmFilter;

/**
 * BaseDao 查询过滤器
 * 
 * @author 王金绍
 * 2014年9月17日-上午11:03:03
 */

public interface BaseDaoQueryFilter<T extends Serializable,PK extends Serializable> extends PriorityOrdered, OrmFilter{
	
	void beforeGet(IOrmDao<T, PK> baseDao, PK id);
	
	void afterGet(IOrmDao<T, PK> baseDao, T rs, PK id);
	
	void beforeGetBy(IOrmDao<T, PK> baseDao, Iquery query);
	
	void afterGetBy(IOrmDao<T, PK> baseDao, T rs, Iquery query);
	
	void beforeGetObject(IOrmDao<T, PK> baseDao, String sql, Object...args);
	
	void afterGetObject(IOrmDao<T, PK> baseDao, T rs , String sql, Object...args);
	
	
	
	void beforeQueryAll(IOrmDao<T, PK> baseDao);
	
	void afterQueryAll(IOrmDao<T, PK> baseDao, List<T> rs);
	
	void beforeQuery(IOrmDao<T, PK> baseDao, String sql, Object...args);
	
	void afterQuery(IOrmDao<T, PK> baseDao, List<T> rs, String sql, Object...args);
	
	void beforeQueryBy(IOrmDao<T, PK> baseDao, Iquery query);
	
	void afterQueryBy(IOrmDao<T, PK> baseDao, List<T> rs, Iquery query);
	
	void beforeQueryPage(IOrmDao<T, PK> baseDao, Page page);
	
	void afterQueryPage(IOrmDao<T, PK> baseDao, Page page);
	
	
	
	void beforeQueryForNumber(IOrmDao<T, PK> baseDao, String sql, Object... args);
	
	void afterQueryForNumber(IOrmDao<T, PK> baseDao, Number num, String sql, Object... args);
	
	void beforeQueryForList(IOrmDao<T, PK> baseDao, String sql, Object... args);
	
	void afterQueryForList(IOrmDao<T, PK> baseDao, List<Map<String, Object>> rs, String sql, Object... args);
	
	void beforeQueryForMap(IOrmDao<T, PK> baseDao, String sql, Object... args);
	
	void afterQueryForMap(IOrmDao<T, PK> baseDao, Map<String, Object> rs, String sql, Object... args);

}
