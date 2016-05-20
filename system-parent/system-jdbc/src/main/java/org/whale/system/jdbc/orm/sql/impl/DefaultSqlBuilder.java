package org.whale.system.jdbc.orm.sql.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.whale.system.common.exception.OrmException;
import org.whale.system.jdbc.orm.entry.OrmSql;
import org.whale.system.jdbc.orm.entry.OrmTable;
import org.whale.system.jdbc.orm.sql.SqlBuilder;


@Component
public class DefaultSqlBuilder implements SqlBuilder {
	
	@Autowired
	private SqlSaveBuilder sqlSaveBuilder;
	@Autowired
	private SqlUpdateBuilder sqlUpdateBuilder;
	@Autowired
	private SqlDelBuilder sqlDelBuilder;
	@Autowired
	private SqlGetBuilder sqlGetBuilder;

	@Override
	public OrmSql build(OrmTable ormTable, int opType) {
		if(OrmSql.OPT_SAVE == opType){
			return this.sqlSaveBuilder.buildSave(ormTable);
		}
		if(OrmSql.OPT_SAVE_BATCH == opType){
			return this.sqlSaveBuilder.buildSaveBatch(ormTable);
		}
		if(OrmSql.OPT_UPDATE == opType){
			return this.sqlUpdateBuilder.buildUpdate(ormTable);
		}
		if(OrmSql.OPT_DELETE == opType){
			return this.sqlDelBuilder.buildDelete(ormTable);
		}
		if(OrmSql.OPT_GET == opType){
			return this.sqlGetBuilder.buildGet(ormTable);
		}
		if(OrmSql.OPT_GET_ALL == opType){
			return this.sqlGetBuilder.buildGetAll(ormTable);
		}
		
		throw new OrmException("操作类型错误");
	}

}
