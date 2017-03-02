package org.whale.system.jdbc.orm.value;

import java.util.List;

import org.whale.system.jdbc.orm.entry.OrmValue;


public interface ValueBuilder {

	OrmValue getSave(Object obj);
	
	OrmValue getSave(List<?> objs);
	
	OrmValue getUpdate(Object obj);
	
	OrmValue getUpdate(List<?> objs);
	
	OrmValue getDelete(Class<?> clazz, Object pk);

	@Deprecated
	OrmValue getDeleteX(Object obj);

	@Deprecated
	OrmValue getDeleteBy(Object obj);
	
	OrmValue getClear(Class<?> clazz, List<?> pks);

	@Deprecated
	OrmValue getClearX(List<?> objs);
	
	OrmValue getGet(Class<?> clazz, Object pk);

	@Deprecated
	OrmValue getGetX(Object obj);
	
	OrmValue getAll(Class<?> clazz);
	
	//------------------------------------动态判断----------------------------
	
	OrmValue getUpdateNotNull(Object obj);

	@Deprecated
	OrmValue getQuery(Object obj);

	@Deprecated
	OrmValue getQueryLike(Object obj);
}
