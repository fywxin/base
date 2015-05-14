package org.whale.ext.mongo;

import java.io.Serializable;
import java.util.List;

import org.bson.conversions.Bson;

public interface IMoDao<T extends Serializable> {

	public void save(T obj);
	
	public void save(List<T> objs);
	
	public void update(T obj);
	
	public void updateByFind(Bson filter, T obj);
	
	public void updateByFind(T filter, T obj);
	
	public void update(List<T> objs);
	
	public void delete(String id);
	
	public void delete(List<String> ids);
	
	public void delete(T t);
	
	public void delete(Bson filter);
	
	public T get(String id);
	
	public T get(T t);
	
	public List<T> query(T t);
	
	public List<T> query(Bson filter);
	
	public List<T> queryAll();
	
	public long count();
	
	public long count(T t);
	
	public long count(Bson filter);
}
