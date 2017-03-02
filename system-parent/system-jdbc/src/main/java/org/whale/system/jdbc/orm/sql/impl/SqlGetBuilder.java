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
public class SqlGetBuilder {
	
	private static Logger logger = LoggerFactory.getLogger(SqlGetBuilder.class);

	public OrmSql buildGet(OrmTable ormTable){
		OrmSql ormSql = new OrmSql();
		
		List<Field> fields = new ArrayList<Field>(1);
		List<Integer> argTypes = new ArrayList<Integer>(1);
		List<OrmColumn> sCols = new ArrayList<OrmColumn>(1);
		StringBuilder strb = new StringBuilder();
		strb.append(ormTable.getSqlHeadPrefix());
		
		OrmColumn idCol = ormTable.getIdCol();
		strb.append(" WHERE ").append(idCol.getSqlName()).append("=?");
		fields.add(idCol.getField());
		argTypes.add(idCol.getType());
		sCols.add(idCol);
		
		ormSql.setArgTypes(ListUtil.toArray(argTypes));
		ormSql.setCols(sCols);
		ormSql.setFields(fields);
		ormSql.setOpType(OrmSql.OPT_GET);
		ormSql.setSql(strb.toString());
		ormSql.setTable(ormTable);
		
		logger.info("ORM: clazz={} 创建get sql语句：\n{}", ormTable.getClazz(), strb.toString());
		
		return ormSql;
	}
	
	public OrmSql buildGetAll(OrmTable ormTable){
		OrmSql ormSql = new OrmSql();
		
		List<Field> fields = new ArrayList<Field>(1);
		List<Integer> argTypes = new ArrayList<Integer>(1);
		List<OrmColumn> sCols = new ArrayList<OrmColumn>(1);
		
		StringBuilder strb = new StringBuilder();
		strb.append(ormTable.getSqlHeadPrefix()).append(ormTable.getSqlOrderSuffix());

		ormSql.setArgTypes(ListUtil.toArray(argTypes));
		ormSql.setCols(sCols);
		ormSql.setFields(fields);
		ormSql.setOpType(OrmSql.OPT_GET_ALL);
		ormSql.setSql(strb.toString());
		ormSql.setTable(ormTable);
		
		logger.info("ORM: clazz={} 创建getAll sql语句：\n{}", ormTable.getClazz(), strb.toString());
		
		return ormSql;
	}
}
