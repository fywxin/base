package org.whale.system.service;

import java.io.Serializable;
import java.util.List;
import java.util.Map;

import org.whale.system.base.Iquery;
import org.whale.system.base.Page;
import org.whale.system.jdbc.IOrmDao;

/**
 * service 基础模板类
 * 注意事务
 *
 * @author 王金绍
 * 2015年4月26日 下午3:49:57
 */
public abstract class BaseService<T extends Serializable, PK extends Serializable> {
	
	public void save(T t){
		if(t == null)
			throw new NullPointerException();
		getDao().save(t);
	}
	
	public void saveBatch(List<T> list){
		if(list == null)
			throw new NullPointerException();
		if(list.size() == 0)
			return ;
		if(list.size() == 1){
			this.save(list.get(0));
		}else{
			getDao().saveBatch(list);
		}
	}
	
	public void update(T t){
		if(t == null)
			throw new NullPointerException();
		getDao().update(t);
	}
	
	public void updateNotNull(T t){
		getDao().updateNotNull(t);
	}
	
	public void updateBatch(List<T> list){
		if(list == null)
			throw new NullPointerException();
		if(list.size() == 0)
			return ;
		if(list.size() == 1){
			this.update(list.get(0));
		}else{
			getDao().updateBatch(list);
		}
	}
	
	public void delete(PK id){
		if(id == null)
			return ;
		getDao().delete(id);
	}
	
	public void delete(Iquery query){
		this.getDao().delete(query);
	}
	
	public void deleteBatch(List<PK> ids){
		if(ids == null || ids.size() < 1)
			return ;
		this.getDao().deleteBatch(ids);
	}
	
	public T get(PK id){
		if(id == null)
			return null;
		return this.getDao().get(id);
	}
	
	public T get(Iquery query){
		return this.getDao().get(query);
	}
	
	public List<T> query(Iquery query){
		return this.getDao().query(query);
	}
	
	public List<T> queryAll(){
		return this.getDao().queryAll();
	}
	
	public void queryPage(Page page){
		this.getDao().queryPage(page);
	}
	
	public Integer count(Iquery query){
		return this.getDao().count(query);
	}
	
	public List<Map<String, Object>> queryForList(Iquery query){
		return this.queryForList(query);
	}
	
	public Map<String, Object> queryForMap(Iquery query){
		return this.queryForMap(query);
	}
	
	public abstract IOrmDao<T, PK> getDao();
}
