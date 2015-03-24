package org.whale.system.addon.impl;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.whale.system.addon.EmptyBaseDaoAddon;
import org.whale.system.base.BaseDao;
import org.whale.system.common.exception.OrmException;
import org.whale.system.jdbc.orm.OrmContext;
import org.whale.system.jdbc.orm.entry.OrmColumn;
import org.whale.system.jdbc.util.AnnotationUtil;

/**
 * 解析@SQL 默认不启用
 * @author 王金绍
 * 2014年9月17日-上午11:28:52
 */
@SuppressWarnings("all")
public class ExeSqlOnSaveAddon extends EmptyBaseDaoAddon{
	
	@Autowired
	private OrmContext ormContext;
	
	@Override
	public void beforeSave(Object obj, BaseDao baseDao) {
		List<OrmColumn> cols = baseDao.getOrmTable().getSqlExecCols();
		if(cols == null)
			return ;
		for(OrmColumn col : cols){
			if(col.getOrmColumnSql() != null && col.getOrmColumnSql().getIsPre() && !"".equals(col.getOrmColumnSql().getSql().trim())){
				this.execute(obj, baseDao, col);
			}
		}
	}
	
	/**
	 * 
	 *功能说明: 执行sql，并将值设置给该字段
	 *创建人: 王金绍
	 *创建时间:2013-3-18 下午6:10:56
	 *@param obj
	 *@param col void
	 *
	 */
	private void execute(Object obj, BaseDao baseDao, OrmColumn col){
		//字段值不为空，且sql执行替换策略为col为空时替换
		if(col.getOrmColumnSql().getReplaceWhenNull() && AnnotationUtil.getFieldValue(obj, col.getField()) != null)
			return ;
		//获取元注释解析后的sql语句
		String sql = col.getOrmColumnSql().getNewSql();
		List<Map<String, Object>> rs = null;
		List<String> params = col.getOrmColumnSql().getParams();
		if(params != null && params.size() > 0){
			List<OrmColumn> cols = new ArrayList<OrmColumn>(params.size());
			for(String param : params){
				OrmColumn colDefined = this.ormContext.getOrmColumn(obj.getClass(), param);
				cols.add(colDefined);
			}
			List<Object> args = AnnotationUtil.getColumnValues(obj, cols);
			rs = baseDao.getJdbcTemplate().queryForList(sql, args.toArray());
		}else{
			rs = baseDao.getJdbcTemplate().queryForList(sql);
		}
		if(rs == null || rs.size() == 0) return ;
		if(rs.size() > 1 || rs.get(0).keySet().size() > 1)
			throw new OrmException("SQL 返回结果不能过一条记录");
		
		Collection<Object> values = rs.get(0).values();
		if(values == null)
			return ;
		if(values.size() != 1)
			throw new OrmException("SQL 返回结果单条记录中不能有多个字段集");
		//设值
		AnnotationUtil.setColumnValue(obj, col, values.toArray()[0]);
	}

	@Override
	public int getOrder() {
		return 90;
	}

}
