package org.whale.system.jdbc.orm.sql.impl;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import org.whale.system.common.util.ListUtil;
import org.whale.system.jdbc.orm.entry.OrmColumn;
import org.whale.system.jdbc.orm.entry.OrmSql;
import org.whale.system.jdbc.orm.entry.OrmTable;
import org.whale.system.jdbc.util.DbKind;


/**
 * 保存
 *
 * @author wjs
 * 2014年9月6日-下午1:57:56
 */
@Component
public class SqlSaveBulider {
	
	private static Logger logger = LoggerFactory.getLogger(SqlSaveBulider.class);
	
	/**
	 * 
	 *功能说明: 
	 *	oracle : 主键的值是在插入前获取，故而id是 ？
	 *  mysql  : 主键自增时，值是再插入后在获取的，故不要id。
	 *  	http://tech.it168.com/oldarticle/2007-05-15/200705150841875.shtml
	 *创建人: wjs
	 *创建时间:2013-3-19 下午2:20:55
	 *@param table
	 *
	 */
	public OrmSql bulidSave(OrmTable table){
		OrmSql ormSql = new OrmSql();
		List<OrmColumn> cols = table.getOrmCols();
		
		List<Integer> argTypes = new ArrayList<Integer>(cols.size());
		List<Field> fields = new ArrayList<Field>(cols.size());
		
		StringBuilder sql = new StringBuilder("INSERT INTO ");
		sql.append(table.getTableDbName()).append("(");
		
		StringBuilder temp = new StringBuilder();
		
		for(int i=0; i<cols.size(); i++){
			OrmColumn col = cols.get(i);
			
//			if(col.getIsId() && col.getIdAuto()){
//				if(DbKind.isMysql()){
//					continue;
//				}
//			}
			
			sql.append(col.getSqlName());
			if(i != cols.size()-1)
				sql.append(",");
			
			argTypes.add(col.getType());
			fields.add(col.getField());
			temp.append(",?");
		}
		sql.append(") VALUES(").append(temp.substring(1)).append(")");
		ormSql.setSql(sql.toString());
		ormSql.setFields(fields);
		ormSql.setArgTypes(ListUtil.toArray(argTypes));
		ormSql.setTable(table);
		ormSql.setOpType(OrmSql.OPT_SAVE);
		ormSql.setCols(cols);
		
		logger.info("ORM: clazz={} 创建save sql语句：\n{}", table.getClazz(), sql.toString());
		
		return ormSql;
	}
	
	/**
	 * 
	 *功能说明: 批量插入，id无法返回，故而id处为 .NEXTVAL
	 *创建人: wjs
	 *创建时间:2013-3-19 下午2:22:10
	 *@param table
	 *
	 */
	public OrmSql bulidSaveBatch(OrmTable table){
		OrmSql ormSql = new OrmSql();
		List<OrmColumn> cols = table.getOrmCols();
		
		List<Integer> argTypes = new ArrayList<Integer>();
		List<Field> fields = new ArrayList<Field>(cols.size());
		List<OrmColumn> sCols = new ArrayList<OrmColumn>(cols.size());
		
		StringBuilder sql = new StringBuilder("INSERT INTO ");
		sql.append(table.getTableDbName()).append("(");
		
		StringBuilder temp = new StringBuilder();
		
		for(int i=0; i<cols.size(); i++){
			OrmColumn col = cols.get(i);
			
//			if(col.getIsId() && col.getIdAuto()){
//				if(DbKind.isMysql()){
//					continue;
//				}
//			}
			
			sql.append(col.getSqlName());
			if(i != cols.size()-1)
				sql.append(",");
			
			//TODO  mysql 是否需要此值？
			//将ID放入sql语句中获取 --此处待扩展优化
			if(col.getIsId() && col.getIdAuto() && DbKind.isOracle()){
				temp.append(",").append(table.getSequence()).append(".NEXTVAL");
			}else{
				argTypes.add(col.getType());
				fields.add(col.getField());
				sCols.add(col);
				temp.append(",?");
			}
		}
		sql.append(") VALUES(").append(temp.substring(1)).append(")");
		ormSql.setSql(sql.toString());
		ormSql.setFields(fields);
		ormSql.setArgTypes(ListUtil.toArray(argTypes));
		ormSql.setTable(table);
		ormSql.setOpType(OrmSql.OPT_SAVE_BATCH);
		ormSql.setCols(sCols);
		
		logger.info("ORM: clazz={} 创建batchSave sql语句：\n{}", table.getClazz(), sql.toString());
		
		return ormSql;
	}
}
