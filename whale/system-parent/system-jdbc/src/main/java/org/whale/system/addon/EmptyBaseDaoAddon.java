package org.whale.system.addon;

import java.util.List;

import org.whale.system.base.BaseDao;


@SuppressWarnings("all")
public abstract class EmptyBaseDaoAddon implements IBaseDaoAddon {

	@Override
	public void beforeSave(Object obj, BaseDao baseDao) {
	}

	@Override
	public void afterSave(Object obj, BaseDao baseDao) {
	}

	@Override
	public void beforeUpdate(Object obj, BaseDao baseDao) {
	}

	@Override
	public void afterUpdate(Object obj, BaseDao baseDao) {
	}

	@Override
	public void beforeDelete(Object id, BaseDao baseDao) {
	}

	@Override
	public void afterDelete(Object id, BaseDao baseDao) {
	}

	@Override
	public void beforeSaveBatch(List<Object> objs, BaseDao baseDao) {
	}

	@Override
	public void afterSaveBatch(List<Object> objs, BaseDao baseDao) {
	}

	@Override
	public void beforeUpdateBatch(List<Object> objs, BaseDao baseDao) {
	}

	@Override
	public void afterUpdateBatch(List<Object> objs, BaseDao baseDao) {
	}
	
	@Override
	public void beforeDeleteBatch(List<Object> objs, BaseDao baseDao) {
	}

	@Override
	public void afterDeleteBatch(List<Object> objs, BaseDao baseDao) {
	}

	@Override
	public boolean access(Object obj, BaseDao baseDao) {
		return true;
	}

}
