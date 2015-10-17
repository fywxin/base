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

@Component
public class SqlUpdateBulider {
	
	private static Logger logger = LoggerFactory.getLogger(SqlUpdateBulider.class);

	public OrmSql bulidUpdate(OrmTable ormTable){
		OrmSql ormSql = new OrmSql();
		
		List<OrmColumn> cols = ormTable.getOrmCols();
		
		List<Integer> argTypes = new ArrayList<Integer>();
		List<Field> fields = new ArrayList<Field>(cols.size());
		List<OrmColumn> sCols = new ArrayList<OrmColumn>(cols.size());
		
		StringBuilder sql = new StringBuilder("UPDATE ");
		sql.append(ormTable.getTableDbName()).append(" t SET ");
		
		for(OrmColumn col : cols){
			if(col.getIsId() || !col.getUpdateAble()) 
				continue;
			//乐观锁 t.version = t.version+1
			if(col.getIsOptimisticLock()){
				sql.append(" t.").append(col.getSqlName()).append("=(t.").append(col.getSqlName()).append("+1),");
				continue;
			}
			sql.append(" t.").append(col.getSqlName()).append("=?,");
			argTypes.add(col.getType());
			fields.add(col.getField());
			sCols.add(col);
		}
		sql.deleteCharAt(sql.length()-1);
		sql.append(" WHERE ");
		OrmColumn idCol = ormTable.getIdCol();
		sql.append("t.").append(idCol.getSqlName()).append("=?");
		argTypes.add(idCol.getType());
		fields.add(idCol.getField());
		sCols.add(idCol);
		
		//乐观锁  AND t.version = ?
		OrmColumn optimisticLockCol = ormTable.getOptimisticLockCol();
		if(optimisticLockCol != null){
			sql.append(" AND t.").append(optimisticLockCol.getSqlName()).append("=?");
			argTypes.add(optimisticLockCol.getType());
			fields.add(optimisticLockCol.getField());
			sCols.add(optimisticLockCol);
		}
		
		ormSql.setSql(sql.toString());
		ormSql.setFields(fields);
		ormSql.setArgTypes(ListUtil.toArray(argTypes));
		ormSql.setTable(ormTable);
		ormSql.setOpType(OrmSql.OPT_UPDATE);
		ormSql.setCols(sCols);
		
		logger.info("ORM: clazz={} 创建update sql语句：\n{}", ormTable.getClazz(), sql.toString());
		
		return ormSql;
	}
}
