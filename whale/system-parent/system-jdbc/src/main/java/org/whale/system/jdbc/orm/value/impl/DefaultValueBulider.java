package org.whale.system.jdbc.orm.value.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.whale.system.jdbc.orm.OrmContext;
import org.whale.system.jdbc.orm.entry.OrmSql;
import org.whale.system.jdbc.orm.entry.OrmTable;
import org.whale.system.jdbc.orm.entry.OrmValue;
import org.whale.system.jdbc.orm.value.ValueBulider;

@Component
public class DefaultValueBulider implements ValueBulider {
	
	@Autowired
	private OrmContext ormContext;
	@Autowired
	private ValueSaveBulider valueSaveBulider;
	@Autowired
	private ValueUpdateBulider valueUpdateBulider;
	@Autowired
	private ValueDelBulider valueDelBulider;
	@Autowired
	private ValueGetBulider valueGetBulider;
	
	@Override
	public OrmValue getSave(Object obj) {
		if(obj == null) return null;
		OrmSql ormSql = ormContext.getOrmSql(obj.getClass(), OrmSql.OPT_SAVE);
		return this.valueSaveBulider.getSave(obj, ormSql);
	}

	@Override
	public OrmValue getSave(List<?> objs) {
		if(objs == null || objs.size() < 1) return null;
		OrmSql ormSql = ormContext.getOrmSql(objs.get(0).getClass(), OrmSql.OPT_SAVE_BATCH);
		return this.valueSaveBulider.getSave(objs, ormSql);
	}

	@Override
	public OrmValue getUpdate(Object obj) {
		if(obj == null) return null;
		OrmSql ormSql = ormContext.getOrmSql(obj.getClass(), OrmSql.OPT_UPDATE);
		return this.valueUpdateBulider.getUpdate(obj, ormSql);
	}
	
	@Override
	public OrmValue getUpdate(List<?> objs) {
		if(objs == null || objs.size() < 1) return null;
		OrmSql ormSql = ormContext.getOrmSql(objs.get(0).getClass(), OrmSql.OPT_UPDATE);
		return this.valueUpdateBulider.getUpdate(objs, ormSql);
	}

	@Override
	public OrmValue getDelete(Class<?> clazz, Object pk) {
		if(pk == null) return null;
		OrmSql ormSql = ormContext.getOrmSql(clazz, OrmSql.OPT_DELETE);
		return this.valueDelBulider.getDelete(pk, ormSql);
	}

	@Override
	public OrmValue getClear(Class<?> clazz, List<?> pks) {
		if(pks == null || pks.size() < 1) return null;
		OrmSql ormSql = ormContext.getOrmSql(clazz, OrmSql.OPT_DELETE);
		return this.valueDelBulider.getClear(pks, ormSql);
	}

	@Override
	public OrmValue getDeleteX(Object obj) {
		if(obj == null) return null;
		OrmSql ormSql = ormContext.getOrmSql(obj.getClass(), OrmSql.OPT_DELETE);
		return this.valueDelBulider.getDeleteX(obj, ormSql);
	}
	
	@Override
	public OrmValue getDeleteBy(Object obj) {
		if(obj == null) return null;
		OrmTable ormTable = ormContext.getOrmTable(obj.getClass());
		return this.valueDelBulider.getDeleteBy(obj, ormTable);
	}

	@Override
	public OrmValue getClearX(List<?> objs) {
		if(objs == null || objs.size() < 1) return null;
		OrmSql ormSql = ormContext.getOrmSql(objs.get(0).getClass(), OrmSql.OPT_DELETE);
		return this.valueDelBulider.getClearX(objs, ormSql);
	}

	@Override
	public OrmValue getGet(Class<?> clazz, Object pk) {
		if(pk == null) return null;
		OrmSql ormSql = ormContext.getOrmSql(clazz, OrmSql.OPT_GET);
		return this.valueGetBulider.getGet(pk, ormSql);
	}

	@Override
	public OrmValue getGetX(Object obj) {
		if(obj == null) return null;
		OrmSql ormSql = ormContext.getOrmSql(obj.getClass(), OrmSql.OPT_GET);
		return this.valueGetBulider.getGetX(obj, ormSql);
	}
	
	@Override
	public OrmValue getAll(Class<?> clazz){
		OrmSql ormSql = ormContext.getOrmSql(clazz, OrmSql.OPT_GET_ALL);
		return new OrmValue(ormSql);
	}
	
	@Override
	public OrmValue getUpdateOnly(Object obj) {
		if(obj == null) return null;
		OrmTable ormTable = ormContext.getOrmTable(obj.getClass());
		return this.valueUpdateBulider.getUpdateOnly(obj, ormTable);
	}

	@Override
	public OrmValue getQuery(Object obj) {
		if(obj == null) return null;
		OrmTable ormTable = ormContext.getOrmTable(obj.getClass());
		return this.valueGetBulider.getQuery(obj, ormTable, false);
	}
	
	@Override
	public OrmValue getQueryLike(Object obj) {
		if(obj == null) return null;
		OrmTable ormTable = ormContext.getOrmTable(obj.getClass());
		return this.valueGetBulider.getQuery(obj, ormTable, true);
	}

}
