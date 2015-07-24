package org.whale.system.jdbc.orm.value.impl;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;
import org.whale.system.common.exception.OrmException;
import org.whale.system.jdbc.orm.entry.OrmColumn;
import org.whale.system.jdbc.orm.entry.OrmSql;
import org.whale.system.jdbc.orm.entry.OrmTable;
import org.whale.system.jdbc.orm.entry.OrmValue;
import org.whale.system.jdbc.util.AnnotationUtil;
import org.whale.system.jdbc.util.DbKind;

@Component
public class ValueSaveBulider {

	@Autowired
	private JdbcTemplate jdbcTemplate;
	
	public OrmValue getSave(Object obj, OrmSql ormSql) {
		OrmValue ormValues = new OrmValue(ormSql);
		OrmTable ormTable = ormSql.getTable();
		
		//如果有Id字段，获取Id字段自增序列值，并设置到实体中
		OrmColumn idCol = ormTable.getIdCol();
		if(idCol != null){
			Object value = null;
			//ORACLE
			if(idCol.getIdAuto()){
				if(DbKind.isOracle()){
					value = this.getNextId(ormTable.getSequence());
					AnnotationUtil.setFieldValue(obj, idCol.getField(), value);
				}
			//非自增ID， 由用户入库前，指定ID值
			}else{
				value = AnnotationUtil.getFieldValue(obj, idCol.getField());
				if(value == null)
					throw new OrmException("主键值不能为空");
			}
		}
		
		//乐观锁字段，入库时为null，则默认设置为1
		OrmColumn optimisticLockCol = ormTable.getOptimisticLockCol();
		if(optimisticLockCol != null){
			if(AnnotationUtil.getFieldValue(obj, optimisticLockCol.getField()) == null){
				if(optimisticLockCol.getField().getType().toString().toLowerCase().indexOf("long") != -1){
					AnnotationUtil.setFieldValue(obj, optimisticLockCol.getField(), 1L);
				}else{
					AnnotationUtil.setFieldValue(obj, optimisticLockCol.getField(), 1);
				}
			}
		}
		
		List<Object> args = this.getValues(obj, ormSql.getCols());
		ormValues.setArgs(args.toArray());
		return ormValues;
	}

	/**
	 * 
	 *功能说明: 批量保存，无法获取到各个实体的id值
	 *创建人: 王金绍
	 *创建时间:2013-3-15 上午10:33:06
	 *@param objs
	 *@param ormSql
	 *@return OrmValue
	 *
	 */
	public OrmValue getSave(List<?> objs, OrmSql ormSql) {
		OrmValue ormValues = new OrmValue(ormSql);
		List<Object[]> batchArgs = new ArrayList<Object[]>(objs.size());
		
		List<OrmColumn> cols = ormSql.getCols();
		List<Object> args = new ArrayList<Object>(cols.size());
		
		for(Object obj : objs){
			args = this.getValues(obj, cols);
			batchArgs.add(args.toArray());
		}
		
		ormValues.setBatchArgs(batchArgs);
		return ormValues;
	}
	
	/**
	 * 
	 *功能说明: 根据序列获取下一个值
	 *创建人: 王金绍
	 *创建时间:2013-3-15 上午10:34:54
	 *@param sequence
	 *@return Object
	 *
	 */
	@SuppressWarnings("all")
	private Object getNextId(String sequence){
		String sql = "select "+sequence+".NEXTVAL from dual";
		return this.jdbcTemplate.queryForLong(sql);
	}
	
	/**
	 * 
	 *功能说明: 获取字段定义的值
	 *创建人: 王金绍
	 *创建时间:2013-3-15 上午10:54:07
	 *@param obj
	 *@param list
	 *@return List<Object>
	 *
	 */
	private List<Object> getValues(Object obj, List<OrmColumn> list){
		List<Object> args = new ArrayList<Object>(list.size());
		Object val = null;
		for(OrmColumn col : list){
//			if(DbKind.isMysql()){
//				if(col.getIsId() && col.getIdAuto())
//					continue;
//			}
			val = AnnotationUtil.getFieldValue(obj, col.getField());
//			if(!col.getNullAble() && val == null) 
//				throw new OrmException("对象 ["+obj.getClass().getName()+"] 中字段 ["+col.getAttrName()+"] 值不能为空");
			
			//乐观锁字段，入库时为null，则默认设置为1
			if(col.getIsOptimisticLock() && val == null){
				val = 1;
				AnnotationUtil.setFieldValue(obj, col.getField(), 1);
			}
			
			args.add(val);
		}
		return args;
	}
}
