package org.whale.system.jdbc;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.whale.system.base.Iquery;
import org.whale.system.base.Iquery.SqlType;
import org.whale.system.base.Page;
import org.whale.system.base.Find;
import org.whale.system.common.util.ReflectionUtil;
import org.whale.system.jdbc.filter.BaseDaoFilterService;

/**
 * BaseDao 绑定执行 Filter
 *
 * @author wjs
 * 2015年4月26日 下午3:44:43
 */
public class OrmDaoWrapper<T extends Serializable,PK extends Serializable> extends OrmDaoImpl<T, PK> {

	@Autowired
	private BaseDaoFilterService<T, PK> filter;

	@Override
	public void save(T t) {
		filter.exeBeforeSave(t, this);
		super.save(t);
		filter.exeAfterSave(t, this);
	}

	@Override
	public void saveBatch(List<T> list) {
		filter.exeBeforeSaveBatch(list, this);
		super.saveBatch(list);
		filter.exeAfterSaveBatch(list, this);
	}

	@Override
	public void update(T t) {
		filter.exeBeforeUpdate(t, this);
		super.update(t);
		filter.exeAfterUpdate(t, this);
	}
	
	@Override
	public void updateNotNull(T t) {
		filter.exeBeforeUpdateNotNull(t, this);
		super.updateNotNull(t);
		filter.exeAfterUpdateNotNull(t, this);
	}

	@Override
	public void updateBatch(List<T> list) {
		filter.exeBeforeUpdateBatch(list, this);
		super.updateBatch(list);
		filter.exeAfterUpdateBatch(list, this);
	}

	@Override
	public void delete(PK id) {
		filter.exeBeforeDelete(id, this);
		super.delete(id);
		filter.exeAfterDelete(id, this);
	}

	@Override
	public void deleteBatch(List<PK> ids) {
		filter.exeBeforeDeleteBatch(ids, this);
		super.deleteBatch(ids);
		filter.exeAfterDeleteBatch(ids, this);
	}
	
	public void delete(String sql, Object... objs){
		this.delete(Find.newQuery(sql, objs));
	}

	@Override
	public void delete(Iquery query) {
		filter.exeBeforeDelete(query, this);
		super.delete(query);
		filter.exeAfterDelete(query, this);
	}

	@Override
	public T get(PK id) {
		filter.exeBeforeGet(this, id);
		T t= super.get(id);
		filter.exeAfterGet(this, t, id);
		return t;
	}
	
	public T get(String sql, Object... objs){
		return this.get(Find.newQuery(sql, objs));
	}
	
	
	
	public T get(Iquery query){
		filter.exeBeforeGet(this, query);
		T t= super.get(query);
		filter.exeAfterGet(this, t, query);
		return t;
	}
	
	@Override
	public List<T> queryAll() {
		filter.exeBeforeQueryAll(this);
		List<T> rs = super.queryAll();
		filter.exeAfterQueryAll(this, rs);
		return rs;
	}
	
	public List<T> query(String sql, Object... objs) {
		return this.query(Find.newQuery(sql, objs));
	}

	@Override
	public List<T> query(Iquery query) {
		filter.exeBeforeQuery(this, query);
		List<T> rs = super.query(query);
		filter.exeAfterQuery(this, rs, query);
		return rs;
	}

	/**
	 * 结果类型转换
	 */
	@Override
	public void queryPage(Page page) {
		filter.exeBeforeQueryPage(this, page);
		super.queryPage(page);
//		if(page.getDataClass() != null && page.getData() != null && page.getData().size() >0 && !page.getClass().equals(Map.class)){
//			List<Map<String, Object>> data = (List<Map<String, Object>>)page.getData();
//			List list = new ArrayList(data.size());
//			for(Map<String, Object> map : data){
//				list.add(ReflectionUtil.map2Clazz(map, page.getDataClass()));
//			}
//			page.setData(list);
//		}
		filter.exeAfterQueryPage(this, page);
	}
	
	public Integer count(String sql, Object... objs) {
		return this.count(Find.newQuery(sql, objs));
	}

	@Override
	public Integer count(Iquery query) {
		filter.exeBeforeCount(this, query);
		Integer rs = super.count(query);
		filter.exeAfterCount(this, rs, query);
		return rs;
	}
	
	public List<Map<String, Object>> queryForList(String sql, Object... objs) {
		return this.queryForList(Find.newQuery(sql, objs));
	}
	
	public <M> List<M> query(Class<M> clazz, Iquery query){
		return this.query(clazz, query.getSql(SqlType.QUERY), query.getArgs());
	}
	
	public <M> List<M> query(Class<M> clazz, String sql, Object... objs){
		List<Map<String, Object>> rs = this.queryForList(Find.newQuery(sql, objs).setClazz(clazz));
		if(rs == null || rs.size() < 1){
			return null;
		}
		List<M> list = new ArrayList<M>(rs.size());

		for(Map<String, Object> map : rs){
			list.add(ReflectionUtil.map2Clazz(map, clazz));
		}
		return list;
	}
	
	public <M> M get(Class<M> clazz, Iquery query){
		return this.get(clazz, query.getSql(SqlType.QUERY), query.getArgs());
	}
	
	
	public <M> M get(Class<M> clazz, String sql, Object... objs){
		List<M> list = this.query(clazz, sql, objs);
		if(list == null || list.size() < 1){
			return null;
		}
		return list.get(0);
	}

	@Override
	public List<Map<String, Object>> queryForList(Iquery query) {
		filter.exeBeforeQueryForList(this, query);
		List<Map<String, Object>> rs = super.queryForList(query);
		filter.exeAfterQueryForList(this, rs, query);
		return rs;
	}
	
	public Map<String, Object> queryForMap(String sql, Object... objs) {
		return this.queryForMap(Find.newQuery(sql, objs));
	}

	@Override
	public Map<String, Object> queryForMap(Iquery query) {
		filter.exeBeforeQueryForMap(this, query);
		Map<String, Object> rs = super.queryForMap(query);
		filter.exeAfterQueryForMap(this, rs, query);
		return rs;
	}

}
