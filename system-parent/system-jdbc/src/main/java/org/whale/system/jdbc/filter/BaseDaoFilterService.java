package org.whale.system.jdbc.filter;

import java.io.Serializable;
import java.util.Collections;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.OrderComparator;
import org.springframework.stereotype.Component;
import org.whale.system.base.Iquery;
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
 * @author wjs
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
	
	public void exeBeforeUpdateNotNull(T obj, IOrmDao<T, PK> baseDao) {
		for(BaseDaoDllFilter<T, PK> filter : dllFilters){
			filter.beforeUpdateNotNull(obj, baseDao);
		}
	}

	public void exeAfterUpdateNotNull(T obj, IOrmDao<T, PK> baseDao) {
		for(int i=dllFilters.size()-1; i>=0; i--){
			dllFilters.get(i).afterUpdateNotNull(obj, baseDao);
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

	public void exeBeforeDeleteBatch(List<PK> ids, IOrmDao<T, PK> baseDao) {
		for(BaseDaoDllFilter<T, PK> filter : dllFilters){
			filter.beforeDeleteBatch(ids, baseDao);
		}
	}
	
	public void exeAfterDeleteBatch(List<PK> ids, IOrmDao<T, PK> baseDao) {
		for(int i=dllFilters.size()-1; i>=0; i--){
			dllFilters.get(i).afterDeleteBatch(ids, baseDao);
		}
	}
	
	public void exeBeforeDelete(Iquery query, IOrmDao<T, PK> baseDao) {
		for(BaseDaoDllFilter<T, PK> filter : dllFilters){
			filter.beforeDelete(query, baseDao);
		}
	}

	public void exeAfterDelete(Iquery query, IOrmDao<T, PK> baseDao) {
		for(int i=dllFilters.size()-1; i>=0; i--){
			dllFilters.get(i).afterDelete(query, baseDao);
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
	
	public void exeBeforeGet(IOrmDao<T, PK> baseDao, Iquery query) {
		for(BaseDaoQueryFilter<T, PK> filter : queryFilters){
			filter.beforeGet(baseDao, query);
		}
	}

	public void exeAfterGet(IOrmDao<T, PK> baseDao, T rs, Iquery query) {
		for(int i=queryFilters.size()-1; i>=0; i--){
			queryFilters.get(i).afterGet(baseDao, rs, query);
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

	public void exeBeforeQuery(IOrmDao<T, PK> baseDao, Iquery query) {
		for(BaseDaoQueryFilter<T, PK> filter : queryFilters){
			filter.beforeQuery(baseDao, query);
		}
	}

	public void exeAfterQuery(IOrmDao<T, PK> baseDao, List<T> rs, Iquery query) {
		for(int i=queryFilters.size()-1; i>=0; i--){
			queryFilters.get(i).afterQuery(baseDao, rs, query);
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
	
	public void exeBeforeCount(IOrmDao<T, PK> baseDao, Iquery query) {
		for(BaseDaoQueryFilter<T, PK> filter : queryFilters){
			filter.beforeCount(baseDao, query);
		}
	}
	
	public void exeAfterCount(IOrmDao<T, PK> baseDao, Number num, Iquery query) {
		for(int i=queryFilters.size()-1; i>=0; i--){
			queryFilters.get(i).afterCount(baseDao, num, query);
		}
	}

	public void exeBeforeContain(IOrmDao<T, PK> baseDao, PK id) {
		for(BaseDaoQueryFilter<T, PK> filter : queryFilters){
			filter.beforeContain(baseDao, id);
		}
	}

	public void exeAfterContain(IOrmDao<T, PK> baseDao, boolean contain, PK id) {
		for(int i=queryFilters.size()-1; i>=0; i--){
			queryFilters.get(i).afterContain(baseDao, contain, id);
		}
	}
	
	//---------------------------------------JdbcTemplate native ---------------------------
	
	
	
	public void exeBeforeQueryForList(IOrmDao<T, PK> baseDao, Iquery query) {
		for(BaseDaoQueryFilter<T, PK> filter : queryFilters){
			filter.beforeQueryForList(baseDao, query);
		}
	}
	
	public void exeAfterQueryForList(IOrmDao<T, PK> baseDao, List<Map<String, Object>> rs, Iquery query) {
		for(int i=queryFilters.size()-1; i>=0; i--){
			queryFilters.get(i).afterQueryForList(baseDao, rs, query);
		}
	}
	
	public void exeBeforeQueryForMap(IOrmDao<T, PK> baseDao, Iquery query) {
		for(BaseDaoQueryFilter<T, PK> filter : queryFilters){
			filter.beforeQueryForMap(baseDao, query);
		}
	}
	
	public void exeAfterQueryForMap(IOrmDao<T, PK> baseDao, Map<String, Object> rs, Iquery query) {
		for(int i=queryFilters.size()-1; i>=0; i--){
			queryFilters.get(i).afterQueryForMap(baseDao, rs, query);
		}
	}
	
}
