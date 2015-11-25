package org.whale.system.jdbc.filter.dll.impl;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.springframework.stereotype.Component;
import org.whale.system.common.exception.BusinessException;
import org.whale.system.jdbc.IOrmDao;
import org.whale.system.jdbc.filter.dll.BaseDaoDllFilterWarpper;
import org.whale.system.jdbc.orm.entry.OrmColumn;
import org.whale.system.jdbc.orm.entry.OrmTable;
import org.whale.system.jdbc.util.AnnotationUtil;

/**
 * 唯一值校验
 * 
 * @author 王金绍
 * 2014年9月17日-上午11:19:05
 */
@SuppressWarnings("all")
@Component
public class UniqueCheckFilter<T extends Serializable,PK extends Serializable> extends BaseDaoDllFilterWarpper<T, PK> {

	
	@Override
	public void beforeSave(T obj, IOrmDao<T, PK> baseDao) {
		List<OrmColumn> cols = baseDao._getOrmTable().getUniqueCheckCols();
		if(cols == null)
			return ;
		
		for(OrmColumn col : cols){
			Object val = AnnotationUtil.getFieldValue(obj, col.getField());
			String sql = "select count(1) from "+baseDao._getOrmTable().getTableDbName()
					+" where "+col.getSqlName()+"=?";
			if(baseDao.getJdbcTemplate().queryForInt(sql, val) > 0){
				throw new BusinessException(col.getCnName()+"["+val+"] 已存在值");
			}
		}
	}

	@Override
	public void beforeSaveBatch(List<T> objs, IOrmDao<T, PK> baseDao) {
		List<OrmColumn> cols = baseDao._getOrmTable().getUniqueCheckCols();
		if(cols == null)
			return ;
		Map<OrmColumn, Set<Object>> valMap = new HashMap<OrmColumn, Set<Object>>();
		Object val = null;
		Set<Object> vals = null;
		for(T obj : objs){
			for(OrmColumn col : cols){
				val = AnnotationUtil.getFieldValue(obj, col.getField());
				vals = valMap.get(col);
				if(vals == null){
					vals = new HashSet<Object>();
					valMap.put(col, vals);
				}
				if(vals.contains(val)){
					throw new BusinessException(col.getCnName()+"["+val+"] 已存在值");
				}
				vals.add(val);
			}
		}
		
		for(Map.Entry<OrmColumn, Set<Object>> entry : valMap.entrySet()){
			for(Object colVal : entry.getValue()){
				String sql = "select count(1) from "+baseDao._getOrmTable().getTableDbName()
						+" where "+entry.getKey().getSqlName()+"=?";
				if(baseDao.getJdbcTemplate().queryForInt(sql, colVal) > 0){
					throw new BusinessException(entry.getKey().getCnName()+"["+val+"] 已存在值");
				}
			}
		}
	}

	@Override
	public void beforeUpdate(T obj, IOrmDao<T, PK> baseDao) {
		OrmTable ormTable = baseDao._getOrmTable();
		List<OrmColumn> cols = ormTable.getUniqueCheckCols();
		if(cols == null)
			return ;
		
		for(OrmColumn col : cols){
			Object val = AnnotationUtil.getFieldValue(obj, col.getField());
			if(val != null){
				String queryObjectSql = "select "+col.getSqlName()+" AS oldValue from "+ormTable.getTableDbName()+" where "+ormTable.getIdCol().getSqlName()+"=?";
				List<Map<String, Object>> list = baseDao.getJdbcTemplate().queryForList(queryObjectSql, AnnotationUtil.getFieldValue(obj, ormTable.getIdCol().getField()));
				if(list == null || list.size() < 1)
					throw new BusinessException("找不到 Id["+val+"] 的记录");
				if(list.get(0).get("oldValue").equals(val)){
					continue;
				}
				
				String sql = "select count(1) from "+ormTable.getTableDbName()
						+" where "+col.getSqlName()+"=?";
				if(baseDao.getJdbcTemplate().queryForInt(sql, val) > 0){
					throw new BusinessException(col.getCnName()+"["+val+"] 已存在值");
				}
			}
			
		}
	}
	
	public void beforeUpdateNotNull(T obj, org.whale.system.jdbc.IOrmDao<T,PK> baseDao) {
		OrmTable ormTable = baseDao._getOrmTable();
		//根据ID更新时，需要做唯一性校验，否则根据唯一更新，不需要校验
		if(AnnotationUtil.getFieldValue(obj, ormTable.getIdCol().getField()) != null){
			this.beforeUpdate(obj, baseDao);
		}
	}

	@Override
	public void beforeUpdateBatch(List<T> objs, IOrmDao<T, PK> baseDao) {
		OrmTable ormTable = baseDao._getOrmTable();
		List<OrmColumn> cols = ormTable.getUniqueCheckCols();
		if(cols == null)
			return ;
		Map<OrmColumn, List<Object>> valMap = new HashMap<OrmColumn, List<Object>>();
		Object val = null;
		List<Object> vals = null;
		for(T obj : objs){
			for(OrmColumn col : cols){
				val = AnnotationUtil.getFieldValue(obj, col.getField());
				vals = valMap.get(col);
				if(vals == null){
					vals = new ArrayList<Object>(objs.size());
					valMap.put(col, vals);
				}
				if(vals.contains(val)){
					throw new BusinessException(col.getCnName()+"["+val+"] 已存在值");
				}
				vals.add(val);
			}
		}
		
		Object colVal = null;
		for(Map.Entry<OrmColumn, List<Object>> entry : valMap.entrySet()){
			
			for(int i=0; i<entry.getValue().size(); i++){
				colVal = entry.getValue().get(i);
				String queryObjectSql = "select "+entry.getKey().getSqlName()+" AS oldValue from "+ormTable.getTableDbName()+" where "+ormTable.getIdCol().getSqlName()+"=?";
				List<Map<String, Object>> list = baseDao.getJdbcTemplate().queryForList(queryObjectSql, AnnotationUtil.getFieldValue(objs.get(i), ormTable.getIdCol().getField()));
				if(list == null || list.size() < 1)
					throw new BusinessException("找不到 "+entry.getKey().getCnName()+"["+val+"] 的记录");
				if(list.get(0).get("oldValue").equals(colVal)){
					continue;
				}
				
				String sql = "select count(1) from "+ormTable.getTableDbName()
						+" where "+entry.getKey().getSqlName()+"=?";
				if(baseDao.getJdbcTemplate().queryForInt(sql, colVal) > 0){
					throw new BusinessException(entry.getKey().getCnName()+"["+colVal+"] 已存在值");
				}
			}
		}
	}

	@Override
	public int getOrder() {
		return 110;
	}

}
