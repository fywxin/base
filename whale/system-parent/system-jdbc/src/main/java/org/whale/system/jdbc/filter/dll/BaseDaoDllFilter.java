package org.whale.system.jdbc.filter.dll;

import java.io.Serializable;
import java.util.List;

import org.springframework.core.PriorityOrdered;
import org.whale.system.base.Iquery;
import org.whale.system.jdbc.IOrmDao;
import org.whale.system.jdbc.filter.OrmFilter;

/**
 * BaseDao 增删改过滤器
 * 
 * @author 王金绍
 * 2014年9月17日-上午11:03:03
 */

public interface BaseDaoDllFilter<T extends Serializable,PK extends Serializable> extends PriorityOrdered, OrmFilter{

	void beforeSave(T obj, IOrmDao<T, PK> baseDao);
	
	void afterSave(T obj, IOrmDao<T, PK> baseDao);
	
	void beforeSaveBatch(List<T> objs, IOrmDao<T, PK> baseDao);
	
	void afterSaveBatch(List<T> objs, IOrmDao<T, PK> baseDao);
	
	
	
	void beforeUpdate(T obj, IOrmDao<T, PK> baseDao);
	
	void afterUpdate(T obj, IOrmDao<T, PK> baseDao);
	
	void beforeUpdateBatch(List<T> objs, IOrmDao<T, PK> baseDao);
	
	void afterUpdateBatch(List<T> objs, IOrmDao<T, PK> baseDao);
	
	
	void beforeDelete(PK id, IOrmDao<T, PK> baseDao);
	
	void afterDelete(PK id, IOrmDao<T, PK> baseDao);
	
	void beforeDeleteBatch(List<PK> ids, IOrmDao<T, PK> baseDao);
	
	void afterDeleteBatch(List<PK> ids, IOrmDao<T, PK> baseDao);
	
	void beforeDelete(Iquery query, IOrmDao<T, PK> baseDao);
	
	void afterDelete(Iquery query, IOrmDao<T, PK> baseDao);

}
