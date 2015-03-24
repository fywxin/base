package org.whale.system.addon;

import java.util.List;

import org.springframework.core.PriorityOrdered;
import org.whale.system.base.BaseDao;

/**
 * 
 * 
 * @author 王金绍
 * 2014年9月17日-上午11:03:03
 */
@SuppressWarnings("all")
public interface IBaseDaoAddon extends PriorityOrdered, OrmAddon{

	void beforeSave(Object obj, BaseDao baseDao);
	
	void afterSave(Object obj, BaseDao baseDao);
	
	void beforeUpdate(Object obj, BaseDao baseDao);
	
	void afterUpdate(Object obj, BaseDao baseDao);
	
	void beforeDelete(Object id, BaseDao baseDao);
	
	void afterDelete(Object id, BaseDao baseDao);
	
	void beforeSaveBatch(List<Object> objs, BaseDao baseDao);
	
	void afterSaveBatch(List<Object> objs, BaseDao baseDao);
	
	void beforeUpdateBatch(List<Object> objs, BaseDao baseDao);
	
	void afterUpdateBatch(List<Object> objs, BaseDao baseDao);
	
	void beforeDeleteBatch(List<Object> objs, BaseDao baseDao);
	
	void afterDeleteBatch(List<Object> objs, BaseDao baseDao);
	
	boolean access(Object obj, BaseDao baseDao);
}
