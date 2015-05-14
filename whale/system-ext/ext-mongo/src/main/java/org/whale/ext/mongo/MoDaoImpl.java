package org.whale.ext.mongo;

import java.io.Serializable;
import java.util.List;

import org.bson.conversions.Bson;
import org.whale.ext.mongo.entry.mogo.MogoTable;

public class MoDaoImpl<T extends Serializable> implements IMoDao<T> {
	
	private MogoTable mogoTable;

	@Override
	public void save(T obj) {
		
	}

	@Override
	public void save(List<T> objs) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void update(T obj) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void updateByFind(Bson filter, T obj) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void updateByFind(T filter, T obj) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void update(List<T> objs) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void delete(String id) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void delete(List<String> ids) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void delete(T t) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void delete(Bson filter) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public T get(String id) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public T get(T t) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public List<T> query(T t) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public List<T> query(Bson filter) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public List<T> queryAll() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public long count() {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public long count(T t) {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public long count(Bson filter) {
		// TODO Auto-generated method stub
		return 0;
	}

}
