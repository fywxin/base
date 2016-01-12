package org.whale.system.jdbc.filter.query;

import java.io.Serializable;
import java.util.List;
import java.util.Map;

import org.whale.system.base.Page;
import org.whale.system.base.Iquery;
import org.whale.system.jdbc.IOrmDao;

/**
 * 查询过滤器 模板
 *
 * @author wjs
 * 2015年4月26日 下午3:47:57
 */
@SuppressWarnings("all")
public abstract class BaseDaoQueryFilterWarpper<T extends Serializable,PK extends Serializable> implements BaseDaoQueryFilter<T, PK> {

	@Override
	public void beforeGet(IOrmDao<T, PK> baseDao, PK id) {
		
	}

	@Override
	public void afterGet(IOrmDao<T, PK> baseDao, T rs, PK id) {
		
	}
	
	@Override
	public void beforeGet(IOrmDao<T, PK> baseDao, Iquery query) {
		
	}

	@Override
	public void afterGet(IOrmDao<T, PK> baseDao, T rs, Iquery query) {
		
	}


	@Override
	public void beforeQueryAll(IOrmDao<T, PK> baseDao) {
		
	}

	@Override
	public void afterQueryAll(IOrmDao<T, PK> baseDao, List<T> rs) {
		
	}

	@Override
	public void beforeQuery(IOrmDao<T, PK> baseDao, Iquery query) {
		
	}

	@Override
	public void afterQuery(IOrmDao<T, PK> baseDao, List<T> rs, Iquery query) {
		
	}

	@Override
	public void beforeQueryPage(IOrmDao<T, PK> baseDao, Page page) {
		
	}

	@Override
	public void afterQueryPage(IOrmDao<T, PK> baseDao, Page page) {
		
	}


	@Override
	public void beforeCount(IOrmDao<T, PK> baseDao, Iquery query) {
		
	}

	@Override
	public void afterCount(IOrmDao<T, PK> baseDao, Number num, Iquery query) {
		
	}

	@Override
	public void beforeQueryForList(IOrmDao<T, PK> baseDao, Iquery query) {
		
	}

	@Override
	public void afterQueryForList(IOrmDao<T, PK> baseDao, List<Map<String, Object>> rs, Iquery query) {
		
	}

	@Override
	public void beforeQueryForMap(IOrmDao<T, PK> baseDao, Iquery query) {
	}

	@Override
	public void afterQueryForMap(IOrmDao<T, PK> baseDao, Map<String, Object> rs, Iquery query) {
		
	}
	
}
