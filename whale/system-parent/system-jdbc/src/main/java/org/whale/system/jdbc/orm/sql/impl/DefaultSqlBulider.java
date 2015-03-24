package org.whale.system.jdbc.orm.sql.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.whale.system.common.exception.OrmException;
import org.whale.system.jdbc.orm.entry.OrmSql;
import org.whale.system.jdbc.orm.entry.OrmTable;
import org.whale.system.jdbc.orm.sql.SqlBulider;


@Component
public class DefaultSqlBulider implements SqlBulider {
	
	@Autowired
	private SqlSaveBulider sqlSaveBulider;
	@Autowired
	private SqlUpdateBulider sqlUpdateBulider;
	@Autowired
	private SqlDelBulider sqlDelBulider;
	@Autowired
	private SqlGetBulider sqlGetBulider;

	@Override
	public OrmSql bulid(OrmTable ormTable, int opType) {
		if(OrmSql.OPT_SAVE == opType){
			return this.sqlSaveBulider.bulidSave(ormTable);
		}
		if(OrmSql.OPT_SAVE_BATCH == opType){
			return this.sqlSaveBulider.bulidSaveBatch(ormTable);
		}
		if(OrmSql.OPT_UPDATE == opType){
			return this.sqlUpdateBulider.bulidUpdate(ormTable);
		}
		if(OrmSql.OPT_DELETE == opType){
			return this.sqlDelBulider.bulidDelete(ormTable);
		}
		if(OrmSql.OPT_GET == opType){
			return this.sqlGetBulider.bulidGet(ormTable);
		}
		if(OrmSql.OPT_GET_ALL == opType){
			return this.sqlGetBulider.bulidGetAll(ormTable);
		}
		
		throw new OrmException("操作类型错误");
	}

}
