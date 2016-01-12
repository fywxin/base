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
public class ValueGetBulider {

	public OrmValue getGet(Object pk, OrmSql ormSql) {
		OrmValue ormValue = new OrmValue(ormSql);
		Object[] args = new Object[]{pk};
		ormValue.setArgs(args);
		return ormValue;
	}
	
	/**
	 * 
	 *功能说明: 根据复合主键查找该对象
	 *创建人: 王金绍
	 *创建时间:2013-3-18 上午10:19:52
	 *@param obj
	 *@param ormSql
	 *@return OrmValue
	 *
	 */
	public OrmValue getGetX(Object obj, OrmSql ormSql) {
		OrmTable ormTable = ormSql.getTable();
		Object id = AnnotationUtil.getFieldValue(obj, ormTable.getIdCol().getField());
		if(id == null)
			throw new OrmException("对象 ["+obj.getClass().getName()+"] 中主键字段 ["+ormTable.getIdCol().getAttrName()+"] 值不能为空");
		return this.getGet(id, ormSql);
	}
	
	
	
	/**
	 * 
	 *功能说明: obj中值非空字段才作为查询条件，字符串类型采用like
	 *   动态生存，sql也是变化的，故而绕过ormSql 动态生成sql语句
	 *创建人: 王金绍
	 *创建时间:2013-3-18 上午10:09:18
	 *@param obj
	 *@param ormTable
	 *@param likeQuery 是否模糊查询
	 *@return OrmValue
	 *
	 */
	public OrmValue getQuery(Object obj, OrmTable ormTable, boolean likeQuery) {
		OrmValue ormValue = new OrmValue();
		List<OrmColumn> cols = ormTable.getOrmCols();
		
		List<Integer> argTypes = new ArrayList<Integer>();
		List<Field> fields = new ArrayList<Field>();
		List<OrmColumn> sCols = new ArrayList<OrmColumn>();
		List<Object> objs = new ArrayList<Object>();
		
		StringBuilder conditionStrb = new StringBuilder();
		
		//查询条件
		Object val = null;
		for(OrmColumn col : cols){
			if((val = AnnotationUtil.getFieldValue(obj, col.getField())) != null){
				//字符串类型
				if(likeQuery && java.sql.Types.VARCHAR == col.getType()){
					conditionStrb.append(" and t.").append(col.getSqlName()).append(" like ? ");
					objs.add("%"+val.toString().trim()+"%");
				}else{
					conditionStrb.append(" and t.").append(col.getSqlName()).append("=? ");
					objs.add(val);
				}
				
				argTypes.add(col.getType());
				fields.add(col.getField());
				sCols.add(col);
			}
		}
		
		StringBuilder strb = new StringBuilder(ormTable.getSqlHeadPrefix());
		strb.append(" where 1=1 ")
			.append(conditionStrb.substring(1))
			.append(ormTable.getSqlOrderSuffix());
		
		ormValue.setSql(strb.toString());
		ormValue.setFields(fields);
		ormValue.setArgTypes(ListUtil.toArray(argTypes));
		ormValue.setTable(ormTable);
		ormValue.setOpType(OrmSql.OPT_QUERY);
		ormValue.setCols(sCols);
		ormValue.setArgs(objs.toArray());
		
		return ormValue;
	}
}
