package org.whale.system.addon.impl;

import java.util.List;

import org.springframework.stereotype.Component;
import org.whale.system.addon.EmptyBaseDaoAddon;
import org.whale.system.addon.OptContext;
import org.whale.system.base.BaseDao;
import org.whale.system.common.util.ThreadContext;

/**
 * ORM容器上下文获取截取器,须在最先位置
 * @author 王金绍
 * 2014年9月17日-上午11:32:18
 */
@Component
@SuppressWarnings("all")
public class OrmContextAddon extends EmptyBaseDaoAddon {

	@Override
	public void beforeSave(Object obj, BaseDao baseDao) {
		ThreadContext.getContext().put(ThreadContext.KEY_OPT_CONTEXT, new OptContext("save", obj, baseDao));
	}

	@Override
	public void afterSave(Object obj, BaseDao baseDao) {
		this.clear();
	}

	@Override
	public void beforeUpdate(Object obj, BaseDao baseDao) {
		ThreadContext.getContext().put(ThreadContext.KEY_OPT_CONTEXT, new OptContext("update", obj, baseDao));
	}

	@Override
	public void afterUpdate(Object obj, BaseDao baseDao) {
		this.clear();
	}

	@Override
	public void beforeDelete(Object id, BaseDao baseDao) {
		ThreadContext.getContext().put(ThreadContext.KEY_OPT_CONTEXT, new OptContext("delete", id, baseDao));
	}

	@Override
	public void afterDelete(Object id, BaseDao baseDao) {
		this.clear();
	}

	@Override
	public void beforeSaveBatch(List<Object> objs, BaseDao baseDao) {
		ThreadContext.getContext().put(ThreadContext.KEY_OPT_CONTEXT, new OptContext("saveBatch", objs, baseDao));
	}

	@Override
	public void afterSaveBatch(List<Object> objs, BaseDao baseDao) {
		this.clear();
	}

	@Override
	public void beforeUpdateBatch(List<Object> objs, BaseDao baseDao) {
		ThreadContext.getContext().put(ThreadContext.KEY_OPT_CONTEXT, new OptContext("updateBatch", objs, baseDao));
	}

	@Override
	public void afterUpdateBatch(List<Object> objs, BaseDao baseDao) {
		this.clear();
	}
	
	private void clear(){
		ThreadContext.getContext().remove(ThreadContext.KEY_OPT_CONTEXT);
	}

	@Override
	public int getOrder() {
		return Integer.MIN_VALUE;
	}
}
