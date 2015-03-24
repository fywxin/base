package org.whale.system.jdbc.orm.sql.impl;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import org.whale.system.common.util.LangUtil;
import org.whale.system.jdbc.orm.entry.OrmColumn;
import org.whale.system.jdbc.orm.entry.OrmSql;
import org.whale.system.jdbc.orm.entry.OrmTable;

@Component
public class SqlDelBulider {
	
	private static Logger logger = LoggerFactory.getLogger(SqlDelBulider.class);

	public OrmSql bulidDelete(OrmTable ormTable){
		OrmSql ormSql = new OrmSql();
		
		List<Field> fields = new ArrayList<Field>(1);
		List<Integer> argTypes = new ArrayList<Integer>(1);
		List<OrmColumn> sCols = new ArrayList<OrmColumn>(1);
		StringBuilder strb = new StringBuilder();
		
		strb.append("DELETE FROM ").append(ormTable.getTableDbName()).append(" WHERE ");

		strb.append(ormTable.getIdCol().getSqlName()).append("=?");
		fields.add(ormTable.getIdCol().getField());
		argTypes.add(ormTable.getIdCol().getType());
		sCols.add(ormTable.getIdCol());

		ormSql.setArgTypes(LangUtil.toArray(argTypes));
		ormSql.setFields(fields);
		ormSql.setOpType(OrmSql.OPT_DELETE);
		ormSql.setSql(strb.toString());
		ormSql.setTable(ormTable);
		ormSql.setCols(sCols);
		
		logger.info("ORM: clazz="+ormTable.getClazz()+" 创建delete sql语句：\n"+strb.toString());
		
		return ormSql;
	}
	
}
