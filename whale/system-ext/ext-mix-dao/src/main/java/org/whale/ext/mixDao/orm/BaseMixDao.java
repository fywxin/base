package org.whale.ext.mixDao.orm;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.whale.system.base.BaseDao;
import org.whale.system.base.Page;
import org.whale.system.cache.ICacheService;
import org.whale.system.common.util.ReflectionUtil;

@Component
public class BaseMixDao<T extends Serializable,PK extends Serializable> extends BaseDao<T, PK> {
	
	@Autowired
	private ICacheService<T> cacheService;

	private String getId(T t){
		return ReflectionUtil.getFieldValue(t, this.getOrmTable().getIdCol().getAttrName()).toString();
	}
	
	private MixOrmEntry getMixOrmEntry(){
		Map<String, Object> extInfo = this.getOrmTable().getExtInfo();
		if(extInfo != null)
			return (MixOrmEntry)extInfo.get(MixOrmEntry.MIX_ORM_KEY);
		return null;
	}
	
	private void put(T t){
		MixOrmEntry mixOrm = this.getMixOrmEntry();
		if(mixOrm != null){
			this.cacheService.put(mixOrm.getCacheName(), this.getId(t), t);
		}
	}
	
	private void mput(List<T> list) {
		MixOrmEntry mixOrm = this.getMixOrmEntry();
		if(mixOrm != null){
			Map<String, T> map = new HashMap<String, T>();
			for(T obj : list){
				map.put(this.getId(obj), obj);
			}
			this.cacheService.mput(mixOrm.getCacheName(), map);
		}
	}
	
	private List<T> cacheQuery(List<T> list){
		MixOrmEntry mixOrm = this.getMixOrmEntry();
		if(list != null && list.size() > 0 && mixOrm != null){
			List<String> keys = new ArrayList<String>(list.size());
			for(T obj : list){
				keys.add(this.getId(obj));
			}
			return this.cacheService.mget(mixOrm.getCacheName(), keys);
		}
		return list;
	}
	
	@Override
	public void save(T t) {
		super.save(t);
		this.put(t);
	}

	@Override
	public void save(List<T> list) {
		super.save(list);
		this.mput(list);
	}

	/**
	 * ID 必须为非自增类型
	 */
	@Override
	public void saveBatch(List<T> list) {
		super.saveBatch(list);
		this.mput(list);
	}

	@Override
	public void update(T t) {
		super.update(t);
		this.put(t);
	}

	@Override
	public void update(List<T> list) {
		super.update(list);
		this.mput(list);
	}

	@Override
	public void updateBatch(List<T> list) {
		super.updateBatch(list);
		this.mput(list);
	}

	@Override
	public void updateOnly(T t) {
		// TODO Auto-generated method stub
		super.updateOnly(t);
	}

	@Override
	public void delete(PK id) {
		super.delete(id);
		MixOrmEntry mixOrm = this.getMixOrmEntry();
		if(mixOrm != null){
			this.cacheService.del(mixOrm.getCacheName(), id.toString());
		}
	}

	@Override
	public void delete(List<PK> ids) {
		super.delete(ids);
		MixOrmEntry mixOrm = this.getMixOrmEntry();
		if(mixOrm != null){
			List<String> idS = new ArrayList<String>(ids.size());
			for(PK id : ids){
				idS.add(id.toString());
			}
			this.cacheService.mdel(mixOrm.getCacheName(), idS);
		}
	}

	@Override
	public void deleteBy(T t) {
		super.deleteBy(t);
	}

	@Override
	public T get(PK id) {
		MixOrmEntry mixOrm = this.getMixOrmEntry();
		if(mixOrm != null){
			return this.cacheService.get(mixOrm.getCacheName(), id.toString());
		}
		return super.get(id);
	}

	@Override
	public T getObject(String sql) {
		T t = super.getObject(sql);
		MixOrmEntry mixOrm = this.getMixOrmEntry();
		if(t != null && mixOrm != null){
			return this.cacheService.get(mixOrm.getCacheName(), this.getId(t));
		}
		return t;
	}

	@Override
	public T getObject(String sql, Object... args) {
		T t = super.getObject(sql, args);
		MixOrmEntry mixOrm = this.getMixOrmEntry();
		if(t != null && mixOrm != null){
			return this.cacheService.get(mixOrm.getCacheName(), this.getId(t));
		}
		return t;
	}
	
	

	@Override
	public List<T> query(T t) {
		return this.cacheQuery(super.query(t));
	}

	@Override
	public List<T> queryLike(T t) {
		return this.cacheQuery(super.queryLike(t));
	}

	@Override
	public List<T> query(String sql) {
		return this.cacheQuery(super.query(sql));
	}

	@Override
	public List<T> query(String sql, Object... args) {
		return this.cacheQuery(super.query(sql, args));
	}

	@Override
	public List<T> queryAll() {
		return this.cacheQuery(super.queryAll());
	}

	@Override
	public void queryPage(Page page) {
		super.queryPage(page);
	}

	
}
