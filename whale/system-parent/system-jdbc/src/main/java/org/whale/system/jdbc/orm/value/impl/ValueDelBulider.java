package org.whale.system.jdbc.orm.value.impl;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;

import org.springframework.stereotype.Component;
import org.whale.system.common.exception.OrmException;
import org.whale.system.common.util.ListUtil;
import org.whale.system.jdbc.orm.entry.OrmColumn;
import org.whale.system.jdbc.orm.entry.OrmSql;
import org.whale.system.jdbc.orm.entry.OrmTable;
import org.whale.system.jdbc.orm.entry.OrmValue;
import org.whale.system.jdbc.util.AnnotationUtil;

@Component
public class ValueDelBulider {

	/**
	 * 
	 *功能说明: 删除单条为主键的记录
	 *创建人: 王金绍
	 *创建时间:2013-3-15 下午12:45:50
	 *@param pk
	 *@param ormSql
	 *@return OrmValue
	 *
	 */
	public OrmValue getDelete(Object pk, OrmSql ormSql) {
		OrmValue ormValue = new OrmValue(ormSql);
		Object[] args = new Object[]{pk};
		ormValue.setArgs(args);
		return ormValue;
	}

	/**
	 * 
	 *功能说明: 批量删除多条为主键的记录
	 *创建人: 王金绍
	 *创建时间:2013-3-15 下午12:55:33
	 *@param pks
	 *@param ormSql
	 *@return OrmValue
	 *
	 */
	public OrmValue getClear(List<?> pks, OrmSql ormSql) {
		OrmValue ormValue = new OrmValue(ormSql);
		List<Object[]> batchArgs = new ArrayList<Object[]>(pks.size());
		for(Object obj : pks){
			batchArgs.add(new Object[]{obj});
		}
		ormValue.setBatchArgs(batchArgs);
		return ormValue;
	}
	
	/**
	 * 
	 *功能说明: 按对象删除记录，只要主键或复合主键不为空
	 *创建人: 王金绍
	 *创建时间:2013-3-15 下午1:16:50
	 *@param obj
	 *@param ormSql
	 *@return OrmValue
	 *
	 */
	public OrmValue getDeleteX(Object obj, OrmSql ormSql) {
		OrmTable ormTable = ormSql.getTable();
		Object id = AnnotationUtil.getFieldValue(obj, ormTable.getIdCol().getField());
		if(id == null)
			throw new OrmException("对象 ["+obj.getClass().getName()+"] 中主键字段 ["+ormTable.getIdCol().getAttrName()+"] 值不能为空");
		return this.getDelete(id, ormSql);
		
	}
	
	/**
	 * 
	 *功能说明:
	 *创建人: 王金绍
	 *创建时间:2013-3-15 下午1:17:27
	 *@param objs
	 *@param ormSql
	 *@return OrmValue
	 *
	 */
	public OrmValue getClearX(List<?> objs, OrmSql ormSql) {
		OrmTable ormTable = ormSql.getTable();
		List<Object> args = new ArrayList<Object>();
		for(Object obj : objs){
			Object id = AnnotationUtil.getFieldValue(obj, ormTable.getIdCol().getField());
			if(id == null)
				throw new OrmException("对象 ["+obj.getClass().getName()+"] 中主键字段 ["+ormTable.getIdCol().getAttrName()+"] 值不能为空");
			args.add(id);
		}
		return this.getClear(args, ormSql);
	}
	
	/**
	 * 
	 *功能说明: 按对象删除记录，只要主键或复合主键不为空
	 *创建人: 王金绍
	 *创建时间:2013-3-15 下午1:16:50
	 *@param obj
	 *@param ormSql
	 *@return OrmValue
	 *
	 */
	public OrmValue getDeleteBy(Object obj, OrmTable ormTable) {
		OrmValue ormValue = new OrmValue();
		List<OrmColumn> cols = ormTable.getOrmCols();
		
		List<Integer> argTypes = new ArrayList<Integer>();
		List<Field> fields = new ArrayList<Field>();
		List<OrmColumn> sCols = new ArrayList<OrmColumn>();
		List<Object> objs = new ArrayList<Object>();
		
		StringBuilder sql = new StringBuilder("DELETE FROM ");
		sql.append(ormTable.getTableDbName()).append(" WHERE 1=1 ");
		
		//待更新字段
		Object val = null;
		for(OrmColumn col : cols){
			if(col.getIsId() ||(val = AnnotationUtil.getFieldValue(obj, col.getField())) == null || val.equals(col.getValue())) 
				continue;
			sql.append(" AND ").append(col.getSqlName()).append("=?");
			argTypes.add(col.getType());
			fields.add(col.getField());
			sCols.add(col);
			objs.add(val);
		}
		if(objs.size() < 1){
			throw new OrmException("无按条件删除字段");
		}
		
		ormValue.setSql(sql.toString());
		ormValue.setFields(fields);
		ormValue.setArgTypes(ListUtil.toArray(argTypes));
		ormValue.setTable(ormTable);
		ormValue.setOpType(OrmSql.OPT_DELETE_BY);
		ormValue.setCols(sCols);
		ormValue.setArgs(objs.toArray());
		
		return ormValue;
	}
}
