package org.whale.system.jdbc.filter;

import java.io.Serializable;
import java.util.List;
import java.util.Map;

import org.whale.system.base.Page;
import org.whale.system.jdbc.IOrmDao;

/**
 * 过滤器执行模板类
 *
 * @author 王金绍
 * 2015年4月26日 下午3:47:16
 */
public abstract class BaseDaoFilterWarpper<T extends Serializable,PK extends Serializable> implements BaseDaoFilter<T, PK> {

	@Override
	public void beforeSave(T obj, IOrmDao<T, PK> baseDao) {
		
	}

	@Override
	public void afterSave(T obj, IOrmDao<T, PK> baseDao) {
		
	}

	@Override
	public void beforeSave(List<T> objs, IOrmDao<T, PK> baseDao) {
		
	}

	@Override
	public void afterSave(List<T> objs, IOrmDao<T, PK> baseDao) {
		
	}

	@Override
	public void beforeSaveBatch(List<T> objs, IOrmDao<T, PK> baseDao) {
		
	}

	@Override
	public void afterSaveBatch(List<T> objs, IOrmDao<T, PK> baseDao) {
		
	}

	@Override
	public void beforeUpdate(T obj, IOrmDao<T, PK> baseDao) {
		
	}

	@Override
	public void afterUpdate(T obj, IOrmDao<T, PK> baseDao) {
		
	}

	@Override
	public void beforeUpdate(List<T> objs, IOrmDao<T, PK> baseDao) {
		
	}

	@Override
	public void afterUpdate(List<T> objs, IOrmDao<T, PK> baseDao) {
		
	}

	@Override
	public void beforeUpdateBatch(List<T> objs, IOrmDao<T, PK> baseDao) {
		
	}

	@Override
	public void afterUpdateBatch(List<T> objs, IOrmDao<T, PK> baseDao) {
		
	}

	@Override
	public void beforeDelete(PK id, IOrmDao<T, PK> baseDao) {
		
	}

	@Override
	public void afterDelete(PK id, IOrmDao<T, PK> baseDao) {
		
	}

	@Override
	public void beforeDelete(List<PK> ids, IOrmDao<T, PK> baseDao) {
		
	}

	@Override
	public void afterDelete(List<PK> ids, IOrmDao<T, PK> baseDao) {
		
	}

	@Override
	public void beforeDeleteBy(T obj, IOrmDao<T, PK> baseDao) {
		
	}

	@Override
	public void afterDeleteBy(T obj, IOrmDao<T, PK> baseDao) {
		
	}
	
	@Override
	public void beforeGet(IOrmDao<T, PK> baseDao, PK id) {
		
	}

	@Override
	public void afterGet(IOrmDao<T, PK> baseDao, T rs, PK id) {
		
	}
	
	@Override
	public void beforeGetObject(IOrmDao<T, PK> baseDao, T t) {
		
	}

	@Override
	public void afterGetObject(IOrmDao<T, PK> baseDao, T rs, T t) {
		
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
