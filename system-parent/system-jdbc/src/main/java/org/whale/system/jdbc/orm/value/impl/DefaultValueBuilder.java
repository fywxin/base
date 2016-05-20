package org.whale.system.jdbc.orm.value.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.whale.system.jdbc.orm.OrmContext;
import org.whale.system.jdbc.orm.entry.OrmSql;
import org.whale.system.jdbc.orm.entry.OrmTable;
import org.whale.system.jdbc.orm.entry.OrmValue;
import org.whale.system.jdbc.orm.value.ValueBuilder;

@Component
public class DefaultValueBuilder implements ValueBuilder {
	
	@Autowired
	private OrmContext ormContext;
	@Autowired
	private ValueSaveBuilder valueSaveBuilder;
	@Autowired
	private ValueUpdateBuilder valueUpdateBuilder;
	@Autowired
	private ValueDelBuilder valueDelBuilder;
	@Autowired
	private ValueGetBuilder valueGetBuilder;
	
	@Override
	public OrmValue getSave(Object obj) {
		if(obj == null) return null;
		OrmSql ormSql = ormContext.getOrmSql(obj.getClass(), OrmSql.OPT_SAVE);
		return this.valueSaveBuilder.getSave(obj, ormSql);
	}

	@Override
	public OrmValue getSave(List<?> objs) {
		if(objs == null || objs.size() < 1) return null;
		OrmSql ormSql = ormContext.getOrmSql(objs.get(0).getClass(), OrmSql.OPT_SAVE_BATCH);
		return this.valueSaveBuilder.getSave(objs, ormSql);
	}

	@Override
	public OrmValue getUpdate(Object obj) {
		if(obj == null) return null;
		OrmSql ormSql = ormContext.getOrmSql(obj.getClass(), OrmSql.OPT_UPDATE);
		return this.valueUpdateBuilder.getUpdate(obj, ormSql);
	}
	
	@Override
	public OrmValue getUpdate(List<?> objs) {
		if(objs == null || objs.size() < 1) return null;
		OrmSql ormSql = ormContext.getOrmSql(objs.get(0).getClass(), OrmSql.OPT_UPDATE);
		return this.valueUpdateBuilder.getUpdate(objs, ormSql);
	}

	@Override
	public OrmValue getDelete(Class<?> clazz, Object pk) {
		if(pk == null) return null;
		OrmSql ormSql = ormContext.getOrmSql(clazz, OrmSql.OPT_DELETE);
		return this.valueDelBuilder.getDelete(pk, ormSql);
	}

	@Override
	public OrmValue getClear(Class<?> clazz, List<?> pks) {
		if(pks == null || pks.size() < 1) return null;
		OrmSql ormSql = ormContext.getOrmSql(clazz, OrmSql.OPT_DELETE);
		return this.valueDelBuilder.getClear(pks, ormSql);
	}

	@Override
	public OrmValue getDeleteX(Object obj) {
		if(obj == null) return null;
		OrmSql ormSql = ormContext.getOrmSql(obj.getClass(), OrmSql.OPT_DELETE);
		return this.valueDelBuilder.getDeleteX(obj, ormSql);
	}
	
	@Override
	public OrmValue getDeleteBy(Object obj) {
		if(obj == null) return null;
		OrmTable ormTable = ormContext.getOrmTable(obj.getClass());
		return this.valueDelBuilder.getDeleteBy(obj, ormTable);
	}

	@Override
	public OrmValue getClearX(List<?> objs) {
		if(objs == null || objs.size() < 1) return null;
		OrmSql ormSql = ormContext.getOrmSql(objs.get(0).getClass(), OrmSql.OPT_DELETE);
		return this.valueDelBuilder.getClearX(objs, ormSql);
	}

	@Override
	public OrmValue getGet(Class<?> clazz, Object pk) {
		if(pk == null) return null;
		OrmSql ormSql = ormContext.getOrmSql(clazz, OrmSql.OPT_GET);
		return this.valueGetBuilder.getGet(pk, ormSql);
	}

	@Override
	public OrmValue getGetX(Object obj) {
		if(obj == null) return null;
		OrmSql ormSql = ormContext.getOrmSql(obj.getClass(), OrmSql.OPT_GET);
		return this.valueGetBuilder.getGetX(obj, ormSql);
	}
	
	@Override
	public OrmValue getAll(Class<?> clazz){
		OrmSql ormSql = ormContext.getOrmSql(clazz, OrmSql.OPT_GET_ALL);
		return new OrmValue(ormSql);
	}
	
	@Override
	public OrmValue getUpdateNotNull(Object obj) {
		if(obj == null) return null;
		OrmTable ormTable = ormContext.getOrmTable(obj.getClass());
		return this.valueUpdateBuilder.getUpdateNotNull(obj, ormTable);
	}

	@Override
	public OrmValue getQuery(Object obj) {
		if(obj == null) return null;
		OrmTable ormTable = ormContext.getOrmTable(obj.getClass());
		return this.valueGetBuilder.getQuery(obj, ormTable, false);
	}
	
	@Override
	public OrmValue getQueryLike(Object obj) {
		if(obj == null) return null;
		OrmTable ormTable = ormContext.getOrmTable(obj.getClass());
		return this.valueGetBuilder.getQuery(obj, ormTable, true);
	}

}
