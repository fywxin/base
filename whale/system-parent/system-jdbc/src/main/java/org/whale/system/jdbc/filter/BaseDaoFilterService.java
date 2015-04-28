package org.whale.system.jdbc.filter;

import java.io.Serializable;
import java.util.Collections;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.OrderComparator;
import org.springframework.stereotype.Component;
import org.whale.system.base.Page;
import org.whale.system.common.exception.OrmException;
import org.whale.system.jdbc.IOrmDao;
import org.whale.system.jdbc.filter.dll.BaseDaoDllFilter;
import org.whale.system.jdbc.filter.query.BaseDaoQueryFilter;

/**
 * Filter执行器
 * 
 * 注入 dllFilter 和 queryFilter 的正反序集合引入
 * 
 * 正反序 在执行调用链时，形成嵌套调用链过滤器
 * 
 *
 * @author 王金绍
 * 2015年4月26日 下午1:58:56
 */
@Component
public class BaseDaoFilterService<T extends Serializable,PK extends Serializable> implements InitializingBean {

	private static final Logger logger = LoggerFactory.getLogger(BaseDaoFilterService.class);
	
	@Autowired(required = false)
	private List<BaseDaoDllFilter<T, PK>> dllFilters;
	
	@Autowired(required = false)
	private List<BaseDaoQueryFilter<T, PK>> queryFilters;
	
	@Override
	public void afterPropertiesSet() throws Exception {
		if(dllFilters == null || dllFilters.size() < 1){
			logger.warn("ORM: 容器中没有DLL插件存在");
			throw new OrmException("容器中没有DLL插件存在");
		}else{
			Collections.sort(dllFilters, new OrderComparator());
		}
		if(queryFilters == null || queryFilters.size() < 1){
			logger.warn("ORM: 容器中没有Query插件存在");
			throw new OrmException("容器中没有Query插件存在");
		}else{
			Collections.sort(queryFilters, new OrderComparator());
		}
	}
	
	public void exeBeforeSave(T obj, IOrmDao<T, PK> baseDao) {
		for(BaseDaoDllFilter<T, PK> filter : dllFilters){
			filter.beforeSave(obj, baseDao);
		}
	}
	
	public void exeAfterSave(T obj, IOrmDao<T, PK> baseDao) {
		for(int i=dllFilters.size()-1; i>=0; i--){
			dllFilters.get(i).afterSave(obj, baseDao);
		}
	}


	
	public void exeBeforeSave(List<T> objs, IOrmDao<T, PK> baseDao) {
		for(BaseDaoDllFilter<T, PK> filter : dllFilters){
			filter.beforeSave(objs, baseDao);
		}
	}

	
	public void exeAfterSave(List<T> objs, IOrmDao<T, PK> baseDao) {
		for(int i=dllFilters.size()-1; i>=0; i--){
			dllFilters.get(i).afterSave(objs, baseDao);
		}
	}

	
	public void exeBeforeSaveBatch(List<T> objs, IOrmDao<T, PK> baseDao) {
		for(BaseDaoDllFilter<T, PK> filter : dllFilters){
			filter.beforeSaveBatch(objs, baseDao);
		}
	}

	
	public void exeAfterSaveBatch(List<T> objs, IOrmDao<T, PK> baseDao) {
		for(int i=dllFilters.size()-1; i>=0; i--){
			dllFilters.get(i).afterSaveBatch(objs, baseDao);
		}
		
	}

	
	public void exeBeforeUpdate(T obj, IOrmDao<T, PK> baseDao) {
		for(BaseDaoDllFilter<T, PK> filter : dllFilters){
			filter.beforeUpdate(obj, baseDao);
		}
	}

	
	public void exeAfterUpdate(T obj, IOrmDao<T, PK> baseDao) {
		for(int i=dllFilters.size()-1; i>=0; i--){
			dllFilters.get(i).afterUpdate(obj, baseDao);
		}
	}

	
	public void exeBeforeUpdate(List<T> objs, IOrmDao<T, PK> baseDao) {
		for(BaseDaoDllFilter<T, PK> filter : dllFilters){
			filter.beforeUpdate(objs, baseDao);
		}
	}

	
	public void exeAfterUpdate(List<T> objs, IOrmDao<T, PK> baseDao) {
		for(int i=dllFilters.size()-1; i>=0; i--){
			dllFilters.get(i).afterUpdate(objs, baseDao);
		}
	}

	
	public void exeBeforeUpdateBatch(List<T> objs, IOrmDao<T, PK> baseDao) {
		for(BaseDaoDllFilter<T, PK> filter : dllFilters){
			filter.beforeUpdateBatch(objs, baseDao);
		}
	}

	
	public void exeAfterUpdateBatch(List<T> objs, IOrmDao<T, PK> baseDao) {
		for(int i=dllFilters.size()-1; i>=0; i--){
			dllFilters.get(i).afterUpdateBatch(objs, baseDao);
		}
	}

	
	public void exeBeforeDelete(PK id, IOrmDao<T, PK> baseDao) {
		for(BaseDaoDllFilter<T, PK> filter : dllFilters){
			filter.beforeDelete(id, baseDao);
		}
	}

	
	public void exeAfterDelete(PK id, IOrmDao<T, PK> baseDao) {
		for(int i=dllFilters.size()-1; i>=0; i--){
			dllFilters.get(i).afterDelete(id, baseDao);
		}
	}

	
	public void exeBeforeDelete(List<PK> ids, IOrmDao<T, PK> baseDao) {
		for(BaseDaoDllFilter<T, PK> filter : dllFilters){
			filter.beforeDelete(ids, baseDao);
		}
	}

	
	public void exeAfterDelete(List<PK> ids, IOrmDao<T, PK> baseDao) {
		for(int i=dllFilters.size()-1; i>=0; i--){
			dllFilters.get(i).afterDelete(ids, baseDao);
		}
	}

	
	public void exeBeforeDeleteBy(T obj, IOrmDao<T, PK> baseDao) {
		for(BaseDaoDllFilter<T, PK> filter : dllFilters){
			filter.beforeDeleteBy(obj, baseDao);
		}
	}

	
	public void exeAfterDeleteBy(T obj, IOrmDao<T, PK> baseDao) {
		for(int i=dllFilters.size()-1; i>=0; i--){
			dllFilters.get(i).afterDeleteBy(obj, baseDao);
		}
	}

	
	public void exeBeforeGet(IOrmDao<T, PK> baseDao, PK id) {
		for(BaseDaoQueryFilter<T, PK> filter : queryFilters){
			filter.beforeGet(baseDao, id);
		}
	}

	
	public void exeAfterGet(IOrmDao<T, PK> baseDao, T rs, PK id) {
		for(int i=queryFilters.size()-1; i>=0; i--){
			queryFilters.get(i).afterGet(baseDao, rs, id);
		}
	}

	
	public void exeBeforeGetObject(IOrmDao<T, PK> baseDao, String sql) {
		for(BaseDaoQueryFilter<T, PK> filter : queryFilters){
			filter.beforeGetObject(baseDao, sql);
		}
	}

	
	public void exeAfterGetObject(IOrmDao<T, PK> baseDao, T rs, String sql) {
		for(int i=queryFilters.size()-1; i>=0; i--){
			queryFilters.get(i).afterGetObject(baseDao, rs, sql);
		}
	}

	
	public void exeBeforeGetObject(IOrmDao<T, PK> baseDao, String sql, Object... args) {
		for(BaseDaoQueryFilter<T, PK> filter : queryFilters){
			filter.beforeGetObject(baseDao, sql, args);
		}
	}

	
	public void exeAfterGetObject(IOrmDao<T, PK> baseDao, T rs, String sql, Object... args) {
		for(int i=queryFilters.size()-1; i>=0; i--){
			queryFilters.get(i).afterGetObject(baseDao, rs, sql, args);
		}
	}

	
	public void exeBeforeQueryAll(IOrmDao<T, PK> baseDao) {
		for(BaseDaoQueryFilter<T, PK> filter : queryFilters){
			filter.beforeQueryAll(baseDao);
		}
	}

	
	public void exeAfterQueryAll(IOrmDao<T, PK> baseDao, List<T> rs) {
		for(int i=queryFilters.size()-1; i>=0; i--){
			queryFilters.get(i).afterQueryAll(baseDao, rs);
		}
	}

	
	public void exeBeforeQuery(IOrmDao<T, PK> baseDao, T t) {
		for(BaseDaoQueryFilter<T, PK> filter : queryFilters){
			filter.beforeQuery(baseDao, t);
		}
	}

	
	public void exeAfterQuery(IOrmDao<T, PK> baseDao, List<T> rs, T t) {
		for(int i=queryFilters.size()-1; i>=0; i--){
			queryFilters.get(i).afterQuery(baseDao, rs, t);
		}
	}

	
	public void exeBeforeQuery(IOrmDao<T, PK> baseDao, String sql) {
		for(BaseDaoQueryFilter<T, PK> filter : queryFilters){
			filter.beforeQuery(baseDao, sql);
		}
	}

	
	public void exeAfterQuery(IOrmDao<T, PK> baseDao, List<T> rs, String sql) {
		for(int i=queryFilters.size()-1; i>=0; i--){
			queryFilters.get(i).afterQuery(baseDao, rs, sql);
		}
	}

	
	public void exeBeforeQuery(IOrmDao<T, PK> baseDao, String sql, Object... args) {
		for(BaseDaoQueryFilter<T, PK> filter : queryFilters){
			filter.beforeQuery(baseDao, sql, args);
		}
	}

	
	public void exeAfterQuery(IOrmDao<T, PK> baseDao, List<T> rs, String sql, Object... args) {
		for(int i=queryFilters.size()-1; i>=0; i--){
			queryFilters.get(i).afterQuery(baseDao, rs, sql, args);
		}
	}

	
	public void exeBeforeQueryPage(IOrmDao<T, PK> baseDao, Page page) {
		for(BaseDaoQueryFilter<T, PK> filter : queryFilters){
			filter.beforeQueryPage(baseDao, page);
		}
	}

	
	public void exeAfterQueryPage(IOrmDao<T, PK> baseDao, Page page) {
		for(int i=queryFilters.size()-1; i>=0; i--){
			queryFilters.get(i).afterQueryPage(baseDao, page);
		}
	}
	
}
