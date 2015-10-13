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
public class MySqlAlterTableService extends AbstractAlterTableService {
	
	private static Logger logger = LoggerFactory.getLogger(MySqlAlterTableService.class);

	/**
	 * 
	 *功能说明: 创建表格
	 *创建人: 王金绍
	 *创建时间:2013-4-15 下午4:56:41
	 *@param table void
	 *
	 */
	public void createTable(OrmTable table){
		StringBuilder strb = new StringBuilder();
		
		strb.append("CREATE TABLE `").append(table.getTableDbName()).append("` (\n");
		List<OrmColumn> cols = table.getOrmCols();
		for(OrmColumn col : cols){
			strb.append("\t`").append(col.getSqlName()).append("` ");
			if(col.getIsId()){
				if(col.getIdAuto()){
					strb.append("int(11) unsigned NOT NULL auto_increment,\n");
					continue ;
				}else{
					
				}
			}
			
			if(col.getType() == Types.NUMERIC){
				strb.append("numeric");
//				if(col.getWidth() > 0){
//					strb.append("(").append(col.getWidth()).append(",").append(col.getPrecision()).append(")");
//				}
			}else if(col.getType() == Types.BIT){
				strb.append("bit");
//				if(col.getWidth() > 0){
//					strb.append("(").append(col.getWidth()).append(")");
//				}
			}else if(col.getType() == Types.REAL){
				strb.append("real");
//				if(col.getWidth() > 0){
//					strb.append("(").append(col.getWidth()).append(")");
//				}
			}else if(col.getType() == Types.TINYINT){
				strb.append("tinyint");
//				if(col.getWidth() > 0){
//					strb.append("(").append(col.getWidth()).append(")");
//				}
			}else if(col.getType() == Types.SMALLINT){
				strb.append("smallint");
//				if(col.getWidth() > 0){
//					strb.append("(").append(col.getWidth()).append(")");
//				}
			}else if(col.getType() == Types.INTEGER){
				strb.append("int");
//				if(col.getWidth() > 0){
//					strb.append("(").append(col.getWidth()).append(")");
//				}
			}else if(col.getType() == Types.BIGINT){
				strb.append("bigint");
//				if(col.getWidth() > 0){
//					strb.append("bigint(").append(col.getWidth()).append(")");
//				}else{
//					strb.append("int(").append(col.getWidth()).append(") unsigned");
//				}
			}else if(col.getType() == Types.FLOAT){
				strb.append("float");
//				if(col.getWidth() > 0){
//					strb.append("(").append(col.getWidth()).append(",").append(col.getPrecision()).append(")");
//				}
			}else if(col.getType() == Types.DOUBLE){
				strb.append("double");
//				if(col.getWidth() > 0){
//					strb.append("(").append(col.getWidth()).append(",").append(col.getPrecision()).append(")");
//				}
			}else if(col.getType() == Types.DECIMAL){
				strb.append("decimal");
//				if(col.getWidth() > 0){
//					strb.append("(").append(col.getWidth()).append(",").append(col.getPrecision()).append(")");
//				}
			}else if(col.getType() == Types.VARCHAR){
				strb.append("varchar(100)");
//				if(col.getWidth() <= 0){
//					strb.append("(1024)");
//				}else{
//					strb.append("(").append(col.getWidth()).append(")");
//				}
			}else if(col.getType() == Types.CHAR){
				strb.append("char");
//				if(col.getWidth() <= 0){
//					strb.append("(100)");
//				}else{
//					strb.append("(").append(col.getWidth()).append(")");
//				}
			}else if(col.getType() == Types.LONGVARCHAR){
				strb.append("longtext");
			}else if(col.getType() == Types.DATE){
				strb.append("date");
			}else if(col.getType() == Types.TIME){
				strb.append("datetime");
			}else if(col.getType() == Types.TIMESTAMP){
				strb.append("timestamp");
			}else if(col.getType() == Types.CLOB){
				strb.append("binary");
			}else if(col.getType() == Types.BLOB){
				strb.append("blob");
			}else if(col.getType() == Types.BOOLEAN){
				strb.append("char(1)");
			}
			
//			if(!col.getNullAble()){
//				strb.append(" NOT NULL");
//			}
			
			if(Strings.isNotBlank(col.getCnName())){
				strb.append(" COMMENT '").append(col.getCnName()).append("'");
			}
			strb.append(",\n");
		}
		if(table.getIdCol() == null){
			strb.deleteCharAt(strb.length()-1);
		}else{
			strb.append("\tPRIMARY KEY  (`"+table.getIdCol().getSqlName()+"`)");
		}
		
		strb.append("\n) ENGINE=InnoDB AUTO_INCREMENT=0 DEFAULT CHARSET=utf8;");
		
		logger.info("ORM: 创建表格[{}] sql: \n{}", table.getTableDbName(), strb.toString());
		
		jdbcTemplate.update(strb.toString());
		
		logger.info("ORM: 创建表格[{}]完成!", table.getTableDbName());
	}
	
}
