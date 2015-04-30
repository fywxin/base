package org.whale.system.jdbc.orm.alter;

import java.sql.Types;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import org.whale.system.common.util.Strings;
import org.whale.system.jdbc.orm.entry.OrmColumn;
import org.whale.system.jdbc.orm.entry.OrmTable;

@Component
public class OracleAlterTableService extends AbstractAlterTableService {
	
	private static Logger logger = LoggerFactory.getLogger(OracleAlterTableService.class);

	@Override
	public void createTable(OrmTable table) {
		this.table(table);
		this.comment(table);
		this.index(table);
	}
	

	/**
	 * 
	 *功能说明: 创建表格
	 *创建人: 王金绍
	 *创建时间:2013-4-15 下午4:56:41
	 *@param table void
	 *
	 */
	private void table(OrmTable table){
		StringBuilder strb = new StringBuilder();
		
		strb.append("create table ").append(table.getTableDbName()).append(" (");
		List<OrmColumn> cols = table.getOrmCols();
		for(OrmColumn col : cols){
			strb.append(" ").append(col.getSqlName()).append(" ");
			if(col.getType() == Types.NUMERIC ||
					col.getType() == Types.DECIMAL || 
					col.getType() == Types.BIT || 
					col.getType() == Types.TINYINT || 
					col.getType() == Types.SMALLINT || 
					col.getType() == Types.INTEGER || 
					col.getType() == Types.BIGINT || 
					col.getType() == Types.REAL || 
					col.getType() == Types.FLOAT || 
					col.getType() == Types.DOUBLE){
				strb.append("NUMBER");
			}else if(col.getType() == Types.VARCHAR ||
					col.getType() == Types.CHAR ||
					col.getType() == Types.LONGVARCHAR){
				strb.append("VARCHAR2(4000)");
			}else if(col.getType() == Types.DATE ||
					col.getType() == Types.TIME ||
					col.getType() == Types.TIMESTAMP){
				strb.append("DATE");
			}else if(col.getType() == Types.CLOB){
				strb.append("CLOB");
			}else if(col.getType() == Types.BLOB){
				strb.append("BLOB");
			}else if(col.getType() == Types.BOOLEAN){
				strb.append("NUMBER(1)");
			}
			
//			if(!col.getNullAble()){
//				strb.append(" not null");
//			}
			strb.append(",");
			
		}
		strb.deleteCharAt(strb.length()-1).append(")");
		
		logger.info("ORM: 创建表格[{}] sql: \n{}", table.getTableDbName(), strb.toString());
		jdbcTemplate.update(strb.toString());
	}
	
	/**
	 * 
	 *功能说明: 注释
	 *创建人: 王金绍
	 *创建时间:2013-4-15 下午4:56:58
	 *@param table void
	 *
	 */
	private void comment(OrmTable table){
		List<OrmColumn> cols = table.getOrmCols();
		StringBuilder strb = new StringBuilder();
		for(OrmColumn col : cols){
			//注释
			if(Strings.isNotBlank(col.getCnName())){
				strb.append("comment on column ")
					.append(table.getTableDbName())
					.append(".")
					.append(col.getSqlName())
					.append(" is '")
					.append(col.getCnName())
					.append("'");
				this.jdbcTemplate.update(strb.toString());
				strb = new StringBuilder();
			}
		}
	}
	
	/**
	 * 
	 *功能说明: 主键
	 *创建人: 王金绍
	 *创建时间:2013-4-15 下午4:57:09
	 *@param table void
	 *
	 */
	private void index(OrmTable table) {
		StringBuilder strb = new StringBuilder();
		strb.append("alter table ").append(table.getTableDbName())
			.append(" add constraint PK_")
			.append(table.getTableDbName())
			//.append("_").append(table.getIdCol().getSqlName())
			.append(" primary key (");
		if(table.getIdCol() != null){
			strb.append(table.getIdCol().getSqlName());
		}
		
		strb.append(") using index");
		this.jdbcTemplate.update(strb.toString());
	}


}
