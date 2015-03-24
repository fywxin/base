package org.whale.system.base;

import java.io.Serializable;
import java.util.List;

public abstract class BaseService<T, PK extends Serializable> {
	
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
	
	public void delete(List<PK> ids){
		if(ids == null || ids.size() < 1)
			return ;
		for(PK id : ids){
			this.delete(id);
		}
	}
	
	public T get(PK id){
		if(id == null)
			return null;
		return this.getDao().get(id);
	}
	
	public List<T> queryAll(){
		return this.getDao().queryAll();
	}
	
	public void queryPage(Page page){
		this.getDao().queryPage(page);
	}
	
	public abstract BaseDao<T, PK> getDao();
}
