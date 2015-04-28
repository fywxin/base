package org.whale.system.jdbc.filter.query;

import java.io.Serializable;
import java.util.List;
import java.util.Map;

import org.whale.system.base.Page;
import org.whale.system.jdbc.IOrmDao;

/**
 * 查询过滤器 模板
 *
 * @author 王金绍
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
	public void beforeGetObject(IOrmDao<T, PK> baseDao, String sql) {
		
	}

	@Override
	public void afterGetObject(IOrmDao<T, PK> baseDao, T rs, String sql) {
		
	}

	@Override
	public void beforeGetObject(IOrmDao<T, PK> baseDao, String sql, Object... args) {
		
	}

	@Override
	public void afterGetObject(IOrmDao<T, PK> baseDao, T rs, String sql, Object... args) {
		
	}

	@Override
	public void beforeQueryAll(IOrmDao<T, PK> baseDao) {
		
	}

	@Override
	public void afterQueryAll(IOrmDao<T, PK> baseDao, List<T> rs) {
		
	}

	@Override
	public void beforeQuery(IOrmDao<T, PK> baseDao, T t) {
		
	}

	@Override
	public void afterQuery(IOrmDao<T, PK> baseDao, List<T> rs, T t) {
		
	}

	@Override
	public void beforeQuery(IOrmDao<T, PK> baseDao, String sql) {
		
	}

	@Override
	public void afterQuery(IOrmDao<T, PK> baseDao, List<T> rs, String sql) {
		
	}

	@Override
	public void beforeQuery(IOrmDao<T, PK> baseDao, String sql, Object... args) {
		
	}

	@Override
	public void afterQuery(IOrmDao<T, PK> baseDao, List<T> rs, String sql, Object... args) {
		
	}

	@Override
	public void beforeQueryPage(IOrmDao<T, PK> baseDao, Page page) {
		
	}

	@Override
	public void afterQueryPage(IOrmDao<T, PK> baseDao, Page page) {
		
	}

	@Override
	public void beforeQueryForNumber(IOrmDao<T, PK> baseDao, String sql, Object... args) {
		
	}

	@Override
	public void afterQueryForNumber(IOrmDao<T, PK> baseDao, Number num, String sql, Object... args) {
		
	}

	@Override
	public void beforeQueryForList(IOrmDao<T, PK> baseDao, String sql, Object... args) {
		
	}

	@Override
	public void afterQueryForList(IOrmDao<T, PK> baseDao, List<Map<String, Object>> rs, String sql, Object... args) {
		
	}

	@Override
	public void beforeQueryForMap(IOrmDao<T, PK> baseDao, String sql, Object... args) {
	}

	@Override
	public void afterQueryForMap(IOrmDao<T, PK> baseDao, Map<String, Object> rs, String sql, Object... args) {
		
	}

	@Override
	public void beforeQueryOther(IOrmDao<T, PK> baseDao, String sql, Object... args) {
		
	}

	@Override
	public void afterQueryOther(IOrmDao<T, PK> baseDao, List<?> rs, String sql, Object... args) {
	}
	
	
}
