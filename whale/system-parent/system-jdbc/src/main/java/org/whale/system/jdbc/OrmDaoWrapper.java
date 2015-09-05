package org.whale.system.jdbc;

import java.io.Serializable;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.whale.system.base.Iquery;
import org.whale.system.base.Page;
import org.whale.system.jdbc.filter.BaseDaoFilterService;

/**
 * BaseDao 绑定执行 Filter
 *
 * @author 王金绍
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
	public void save(List<T> list) {
		filter.exeBeforeSave(list, this);
		super.save(list);
		filter.exeAfterSave(list, this);
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
	public void update(List<T> list) {
		filter.exeBeforeUpdate(list, this);
		super.update(list);
		filter.exeAfterUpdate(list, this);
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
	public void delete(List<PK> ids) {
		filter.exeBeforeDelete(ids, this);
		super.delete(ids);
		filter.exeAfterDelete(ids, this);
	}

	@Override
	public void deleteBy(Iquery query) {
		filter.exeBeforeDeleteBy(query, this);
		super.deleteBy(query);
		filter.exeAfterDeleteBy(query, this);
	}

	@Override
	public T get(PK id) {
		filter.exeBeforeGet(this, id);
		T t= super.get(id);
		filter.exeAfterGet(this, t, id);
		return t;
	}

	@Override
	public T getObject(String sql, Object... args) {
		filter.exeBeforeGetObject(this, sql, args);
		T t= super.getObject(sql, args);
		filter.exeAfterGetObject(this, t, sql, args);
		return t;
	}
	
	public T getBy(Iquery query){
		filter.exeBeforeGetBy(this, query);
		T t= super.getBy(query);
		filter.exeAfterGetBy(this, t, query);
		return t;
	}
	
	@Override
	public List<T> queryAll() {
		filter.exeBeforeQueryAll(this);
		List<T> rs = super.queryAll();
		filter.exeAfterQueryAll(this, rs);
		return rs;
	}
	
	@Override
	public List<T> query(String sql, Object... args) {
		filter.exeBeforeQuery(this, sql, args);
		List<T> rs = super.query(sql, args);
		filter.exeAfterQuery(this, rs, sql, args);
		return rs;
	}

	@Override
	public List<T> queryBy(Iquery query) {
		filter.exeBeforeQueryBy(this, query);
		List<T> rs = super.queryBy(query);
		filter.exeAfterQueryBy(this, rs, query);
		return rs;
	}

	
	@Override
	public void queryPage(Page page) {
		filter.exeBeforeQueryPage(this, page);
		super.queryPage(page);
		filter.exeAfterQueryPage(this, page);
	}

	@Override
	public Integer queryForInt(String sql, Object... args) {
		filter.exeBeforeQueryForNumber(this, sql, args);
		Integer rs = super.queryForInt(sql, args);
		filter.exeAfterQueryForNumber(this, rs, sql, args);
		return rs;
	}

	@Override
	public Long queryForLong(String sql, Object... args) {
		filter.exeBeforeQueryForNumber(this, sql, args);
		Long rs = super.queryForLong(sql);
		filter.exeAfterQueryForNumber(this, rs, sql, args);
		return rs;
	}

	@Override
	public List<Map<String, Object>> queryForList(String sql, Object... args) {
		filter.exeBeforeQueryForList(this, sql, args);
		List<Map<String, Object>> rs = super.queryForList(sql, args);
		filter.exeAfterQueryForList(this, rs, sql, args);
		return rs;
	}

	@Override
	public Map<String, Object> queryForMap(String sql, Object... args) {
		filter.exeBeforeQueryForMap(this, sql, args);
		Map<String, Object> rs = super.queryForMap(sql);
		filter.exeAfterQueryForMap(this, rs, sql, args);
		return rs;
	}

}
