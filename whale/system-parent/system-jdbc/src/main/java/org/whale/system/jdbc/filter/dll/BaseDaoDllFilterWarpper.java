package org.whale.system.jdbc.filter.dll;

import java.io.Serializable;
import java.util.List;

import org.whale.system.base.Page;
import org.whale.system.base.Iquery;
import org.whale.system.jdbc.IOrmDao;

/**
 * 增删改执行模板
 *
 * @author 王金绍
 * 2015年4月26日 下午3:49:13
 */
@SuppressWarnings("all")
public abstract class BaseDaoDllFilterWarpper<T extends Serializable,PK extends Serializable> implements BaseDaoDllFilter<T, PK> {

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

}
