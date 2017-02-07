package org.whale.system.jdbc.filter;

import java.io.Serializable;
import java.util.List;
import java.util.Map;

import org.whale.system.base.Page;
import org.whale.system.base.Iquery;
import org.whale.system.jdbc.IOrmDao;

/**
 * 过滤器执行模板类
 *
 * @author wjs
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
	public void beforeUpdateNotNull(T obj, IOrmDao<T, PK> baseDao) {
		
	}

	@Override
	public void afterUpdateNotNull(T obj, IOrmDao<T, PK> baseDao) {
		
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
	public void beforeDeleteBatch(List<PK> ids, IOrmDao<T, PK> baseDao) {
		
	}

	@Override
	public void afterDeleteBatch(List<PK> ids, IOrmDao<T, PK> baseDao) {
		
	}

	@Override
	public void beforeDelete(Iquery query, IOrmDao<T, PK> baseDao) {
		
	}

	@Override
	public void afterDelete(Iquery query, IOrmDao<T, PK> baseDao) {
		
	}
	
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
	public void beforeContain(IOrmDao<T, PK> baseDao, PK id){}

	@Override
	public void afterContain(IOrmDao<T, PK> baseDao, boolean contain, PK id){}

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
