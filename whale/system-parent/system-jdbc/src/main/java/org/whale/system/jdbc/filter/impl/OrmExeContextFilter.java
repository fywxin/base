package org.whale.system.jdbc.filter.impl;

import java.io.Serializable;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Component;
import org.whale.system.base.Page;
import org.whale.system.common.util.ThreadContext;
import org.whale.system.jdbc.IOrmDao;
import org.whale.system.jdbc.filter.BaseDaoFilterWarpper;

/**
 * ORM容器上下文获取截取器
 * 第一个执行before，最后一个执行 after
 *
 * @author 王金绍
 * 2015年4月26日 下午12:22:19
 */
@Component
public class OrmExeContextFilter<T extends Serializable,PK extends Serializable> extends BaseDaoFilterWarpper<T, PK> {
	
	private void clear(){
		ThreadContext.getContext().remove(ThreadContext.KEY_OPT_CONTEXT);
	}
	
	@Override
	public void beforeSave(T obj, IOrmDao<T, PK> baseDao) {
		ThreadContext.getContext().put(ThreadContext.KEY_OPT_CONTEXT, new OrmExeContext("save", obj, baseDao));
	}

	@Override
	public void afterSave(T obj, IOrmDao<T, PK> baseDao) {
		this.clear();
	}

	@Override
	public void beforeSave(List<T> objs, IOrmDao<T, PK> baseDao) {
		ThreadContext.getContext().put(ThreadContext.KEY_OPT_CONTEXT, new OrmExeContext("saves", objs, baseDao));
	}

	@Override
	public void afterSave(List<T> objs, IOrmDao<T, PK> baseDao) {
		this.clear();
	}

	@Override
	public void beforeSaveBatch(List<T> objs, IOrmDao<T, PK> baseDao) {
		ThreadContext.getContext().put(ThreadContext.KEY_OPT_CONTEXT, new OrmExeContext("saveBatch", objs, baseDao));
	}

	@Override
	public void afterSaveBatch(List<T> objs, IOrmDao<T, PK> baseDao) {
		this.clear();
	}

	@Override
	public void beforeUpdate(T obj, IOrmDao<T, PK> baseDao) {
		ThreadContext.getContext().put(ThreadContext.KEY_OPT_CONTEXT, new OrmExeContext("update", obj, baseDao));
	}

	@Override
	public void afterUpdate(T obj, IOrmDao<T, PK> baseDao) {
		this.clear();
	}

	@Override
	public void beforeUpdate(List<T> objs, IOrmDao<T, PK> baseDao) {
		ThreadContext.getContext().put(ThreadContext.KEY_OPT_CONTEXT, new OrmExeContext("updates", objs, baseDao));
	}

	@Override
	public void afterUpdate(List<T> objs, IOrmDao<T, PK> baseDao) {
		this.clear();
	}

	@Override
	public void beforeUpdateBatch(List<T> objs, IOrmDao<T, PK> baseDao) {
		ThreadContext.getContext().put(ThreadContext.KEY_OPT_CONTEXT, new OrmExeContext("updateBatch", objs, baseDao));
	}

	@Override
	public void afterUpdateBatch(List<T> objs, IOrmDao<T, PK> baseDao) {
		this.clear();
	}

	@Override
	public void beforeDelete(PK id, IOrmDao<T, PK> baseDao) {
		ThreadContext.getContext().put(ThreadContext.KEY_OPT_CONTEXT, new OrmExeContext("delete", id, baseDao));
	}

	@Override
	public void afterDelete(PK id, IOrmDao<T, PK> baseDao) {
		this.clear();
	}

	@Override
	public void beforeDelete(List<PK> ids, IOrmDao<T, PK> baseDao) {
		ThreadContext.getContext().put(ThreadContext.KEY_OPT_CONTEXT, new OrmExeContext("deletes", ids, baseDao));
	}

	@Override
	public void afterDelete(List<PK> ids, IOrmDao<T, PK> baseDao) {
		this.clear();
	}

	@Override
	public void beforeDeleteBy(T obj, IOrmDao<T, PK> baseDao) {
		ThreadContext.getContext().put(ThreadContext.KEY_OPT_CONTEXT, new OrmExeContext("deleteBy", obj, baseDao));
	}

	@Override
	public void afterDeleteBy(T obj, IOrmDao<T, PK> baseDao) {
		this.clear();
	}

	@Override
	public void beforeGet(IOrmDao<T, PK> baseDao, PK id) {
		ThreadContext.getContext().put(ThreadContext.KEY_OPT_CONTEXT, new OrmExeContext("get", id, baseDao));
	}

	@Override
	public void afterGet(IOrmDao<T, PK> baseDao, T rs, PK id) {
		this.clear();
	}
	
	@Override
	public void beforeGetObject(IOrmDao<T, PK> baseDao, T t) {
		ThreadContext.getContext().put(ThreadContext.KEY_OPT_CONTEXT, new OrmExeContext("getObject", t, baseDao));
	}

	@Override
	public void afterGetObject(IOrmDao<T, PK> baseDao, T rs, T t) {
		this.clear();
	}


	@Override
	public void beforeGetObject(IOrmDao<T, PK> baseDao, String sql) {
		ThreadContext.getContext().put(ThreadContext.KEY_OPT_CONTEXT, new OrmExeContext("getObject", sql, baseDao));
	}

	@Override
	public void afterGetObject(IOrmDao<T, PK> baseDao, T rs, String sql) {
		this.clear();
	}

	@Override
	public void beforeGetObject(IOrmDao<T, PK> baseDao, String sql, Object... args) {
		ThreadContext.getContext().put(ThreadContext.KEY_OPT_CONTEXT, new OrmExeContext("getObject", sql, this.parse(args), baseDao));
	}

	@Override
	public void afterGetObject(IOrmDao<T, PK> baseDao, T rs, String sql, Object... args) {
		this.clear();
	}

	@Override
	public void beforeQueryAll(IOrmDao<T, PK> baseDao) {
		ThreadContext.getContext().put(ThreadContext.KEY_OPT_CONTEXT, new OrmExeContext("queryAll", null, baseDao));
	}

	@Override
	public void afterQueryAll(IOrmDao<T, PK> baseDao, List<T> rs) {
		this.clear();
	}

	@Override
	public void beforeQuery(IOrmDao<T, PK> baseDao, T t) {
		ThreadContext.getContext().put(ThreadContext.KEY_OPT_CONTEXT, new OrmExeContext("query", null, t, baseDao));
	}

	@Override
	public void afterQuery(IOrmDao<T, PK> baseDao, List<T> rs, T t) {
		this.clear();
	}

	@Override
	public void beforeQuery(IOrmDao<T, PK> baseDao, String sql) {
		ThreadContext.getContext().put(ThreadContext.KEY_OPT_CONTEXT, new OrmExeContext("query", sql, null, baseDao));
	}

	@Override
	public void afterQuery(IOrmDao<T, PK> baseDao, List<T> rs, String sql) {
		this.clear();
	}

	@Override
	public void beforeQuery(IOrmDao<T, PK> baseDao, String sql, Object... args) {
		ThreadContext.getContext().put(ThreadContext.KEY_OPT_CONTEXT, new OrmExeContext("query", sql, this.parse(args), baseDao));
	}

	@Override
	public void afterQuery(IOrmDao<T, PK> baseDao, List<T> rs, String sql, Object... args) {
		this.clear();
	}

	@Override
	public void beforeQueryPage(IOrmDao<T, PK> baseDao, Page page) {
		ThreadContext.getContext().put(ThreadContext.KEY_OPT_CONTEXT, new OrmExeContext("queryPage", page.getSql(), page, baseDao));
	}

	@Override
	public void afterQueryPage(IOrmDao<T, PK> baseDao, Page page) {
		this.clear();
	}

	@Override
	public int getOrder() {
		return Integer.MIN_VALUE;
	}
	
	private List<Object> parse(Object... objs){
		if(objs == null)
			return null;
		List<Object> list = new LinkedList<Object>();
		for(Object obj : objs){
			list.add(obj);
		}
		return list;
	}

	//-----------------------------------------------JdbcTemplate 自带---------------------------------------
	
	@Override
	public void beforeQueryForNumber(IOrmDao<T, PK> baseDao, String sql, Object... args) {
		ThreadContext.getContext().put(ThreadContext.KEY_OPT_CONTEXT, new OrmExeContext("queryForNumber", sql, this.parse(args), baseDao));
	}

	@Override
	public void afterQueryForNumber(IOrmDao<T, PK> baseDao, Number num, String sql, Object... args) {
		this.clear();
	}

	@Override
	public void beforeQueryForList(IOrmDao<T, PK> baseDao, String sql, Object... args) {
		ThreadContext.getContext().put(ThreadContext.KEY_OPT_CONTEXT, new OrmExeContext("queryForList", sql, this.parse(args), baseDao));
	}

	@Override
	public void afterQueryForList(IOrmDao<T, PK> baseDao, List<Map<String, Object>> rs, String sql, Object... args) {
		this.clear();
	}

	@Override
	public void beforeQueryForMap(IOrmDao<T, PK> baseDao, String sql, Object... args) {
		ThreadContext.getContext().put(ThreadContext.KEY_OPT_CONTEXT, new OrmExeContext("queryForMap", sql, this.parse(args), baseDao));
	}

	@Override
	public void afterQueryForMap(IOrmDao<T, PK> baseDao, Map<String, Object> rs, String sql, Object... args) {
		this.clear();
	}
	
	@Override
	public void beforeQueryOther(IOrmDao<T, PK> baseDao, String sql, Object... args) {
		ThreadContext.getContext().put(ThreadContext.KEY_OPT_CONTEXT, new OrmExeContext("queryOther", sql, this.parse(args), baseDao));
	}

	@Override
	public void afterQueryOther(IOrmDao<T, PK> baseDao, List<?> rs, String sql, Object... args) {
		this.clear();
	}

	
}
