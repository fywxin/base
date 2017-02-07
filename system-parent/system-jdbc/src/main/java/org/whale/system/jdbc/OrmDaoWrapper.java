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
import org.whale.system.common.util.Mapper;
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
	public int save(T t) {
		filter.exeBeforeSave(t, this);
		int i = super.save(t);
		filter.exeAfterSave(t, this);
		return i;
	}

	@Override
	public int[] saveBatch(List<T> list) {
		filter.exeBeforeSaveBatch(list, this);
		int[] intArr = super.saveBatch(list);
		filter.exeAfterSaveBatch(list, this);
		return intArr;
	}

	@Override
	public int update(T t) {
		filter.exeBeforeUpdate(t, this);
		int i = super.update(t);
		filter.exeAfterUpdate(t, this);
		return i;
	}
	
	@Override
	public int updateNotNull(T t) {
		filter.exeBeforeUpdateNotNull(t, this);
		int i = super.updateNotNull(t);
		filter.exeAfterUpdateNotNull(t, this);
		return i;
	}

	@Override
	public int[] updateBatch(List<T> list) {
		filter.exeBeforeUpdateBatch(list, this);
		int[] intArr = super.updateBatch(list);
		filter.exeAfterUpdateBatch(list, this);
		return intArr;
	}

	@Override
	public int delete(PK id) {
		filter.exeBeforeDelete(id, this);
		int i = super.delete(id);
		filter.exeAfterDelete(id, this);
		return i;
	}

	@Override
	public int[] deleteBatch(List<PK> ids) {
		filter.exeBeforeDeleteBatch(ids, this);
		int[] intArr = super.deleteBatch(ids);
		filter.exeAfterDeleteBatch(ids, this);
		return intArr;
	}
	
	public int delete(String sql, Object... objs){
		return this.delete(Find.newQuery(sql, objs));
	}

	@Override
	public int delete(Iquery query) {
		filter.exeBeforeDelete(query, this);
		int i = super.delete(query);
		filter.exeAfterDelete(query, this);
		return i;
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
		Class dlazz = page.getDataClass();
		super.queryPage(page);
		if(dlazz != null
				&& !dlazz.equals(Map.class)
				&& !dlazz.equals(super.getClazz())
				&& page.getData() != null
				&& page.getData().size() >0
				){
			List<Map<String, Object>> data = (List<Map<String, Object>>)page.getData();
			List list = new ArrayList(data.size());
			for(Map<String, Object> map : data){
				list.add(Mapper.map2Clazz(map, dlazz));

			}
			page.setData(list);
		}
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

	public boolean exist(PK id){
		filter.exeBeforeExist(this, id);
		boolean rs = super.exist(id);
		filter.exeAfterExist(this, rs, id);
		return rs;
	}
	
	public List<Map<String, Object>> queryForList(String sql, Object... objs) {
		return this.queryForList(Find.newQuery(sql, objs));
	}
	
	public <M> List<M> query(Class<M> clazz, Iquery query){
		return this.query(clazz, query.getSql(SqlType.QUERY), query.getArgs());
	}
	
	public <M> List<M> query(Class<M> clazz, String sql, Object... objs){
		return Mapper.map2List(this.queryForList(Find.newQuery(sql, objs).setClazz(clazz)), clazz);
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
