package org.whale.system.addon.impl;

import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Component;
import org.whale.system.addon.EmptyBaseDaoAddon;
import org.whale.system.base.BaseDao;
import org.whale.system.common.exception.BusinessException;
import org.whale.system.jdbc.orm.entry.OrmColumn;
import org.whale.system.jdbc.orm.entry.OrmTable;
import org.whale.system.jdbc.util.AnnotationUtil;

/**
 * 唯一值校验
 * @author 王金绍
 * 2014年9月17日-上午11:19:05
 */
@SuppressWarnings("all")
@Component
public class UniqueCheckAddon extends EmptyBaseDaoAddon {
	
	@Override
	public void beforeSave(Object obj, BaseDao baseDao) {
		List<OrmColumn> cols = baseDao.getOrmTable().getUniqueCheckCols();
		if(cols == null)
			return ;
		
		for(OrmColumn col : cols){
			Object val = AnnotationUtil.getFieldValue(obj, col.getField());
			String sql = "select count(1) from "+baseDao.getOrmTable().getTableDbName()
					+" where "+col.getSqlName()+"=?";
			if(baseDao.getJdbcTemplate().queryForInt(sql, val) > 0){
				throw new BusinessException(col.getCnName()+"["+val+"] 已存在值");
			}
		}
	}

	@Override
	public void beforeUpdate(Object obj, BaseDao baseDao) {
		OrmTable ormTable = baseDao.getOrmTable();
		List<OrmColumn> cols = ormTable.getUniqueCheckCols();
		if(cols == null)
			return ;
		
		for(OrmColumn col : cols){
			Object val = AnnotationUtil.getFieldValue(obj, col.getField());
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

	@Override
	public int getOrder() {
		return 110;
	}

}
