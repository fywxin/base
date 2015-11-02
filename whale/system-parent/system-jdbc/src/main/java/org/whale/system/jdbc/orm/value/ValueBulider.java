package org.whale.system.jdbc.orm.value;

import java.util.List;

import org.whale.system.jdbc.orm.entry.OrmValue;


public interface ValueBulider {

	OrmValue getSave(Object obj);
	
	OrmValue getSave(List<?> objs);
	
	OrmValue getUpdate(Object obj);
	
	OrmValue getUpdate(List<?> objs);
	
	OrmValue getDelete(Class<?> clazz, Object pk);
	
	OrmValue getDeleteX(Object obj);
	
	OrmValue getDeleteBy(Object obj);
	
	OrmValue getClear(Class<?> clazz, List<?> pks);
	
	OrmValue getClearX(List<?> objs);
	
	OrmValue getGet(Class<?> clazz, Object pk);
	
	OrmValue getGetX(Object obj);
	
	OrmValue getAll(Class<?> clazz);
	
	//------------------------------------动态判断----------------------------
	
	OrmValue getUpdateNotNull(Object obj);
	
	OrmValue getQuery(Object obj);
	
	OrmValue getQueryLike(Object obj);
}
