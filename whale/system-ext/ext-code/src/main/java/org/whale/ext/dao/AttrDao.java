package org.whale.ext.dao;

import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Repository;
import org.whale.ext.domain.Attr;
import org.whale.system.base.BaseDao;
import org.whale.system.base.Query;
import org.whale.system.jdbc.util.DbKind;

@Repository
public class AttrDao extends BaseDao<Attr, Long> {
	
	public List<Attr> queryByDomainId(Long domainId){
		
		return this.query(this.cmd().eq("domainId", domainId));
	}
	
	
	
	String queryColsByTable_MYSQL = "SELECT t.COLUMN_NAME AS name, "
								+ "		(CASE WHEN t.IS_NULLABLE = 'YES' THEN '1' ELSE '0' END) AS isNull,"
								+ "		(t.ORDINAL_POSITION * 10) AS sort,"
								+ "		t.COLUMN_COMMENT AS comments,"
								+ "		t.CHARACTER_MAXIMUM_LENGTH as maxLength, "
								+ "		t.COLUMN_TYPE AS jdbcType "
								+ "	FROM information_schema.`COLUMNS` t "
								+ "	WHERE t.TABLE_SCHEMA = (select database()) "
								+ "		AND upper(t.TABLE_NAME) = upper(?) "
								+ "	ORDER BY t.ORDINAL_POSITION";
	
	String queryColsByTable_ORACLE = "SELECT t.COLUMN_NAME AS name,"
								+ "		(CASE WHEN t.NULLABLE = 'Y' THEN '1' ELSE '0' END) AS isNull,"
								+ "		(t.COLUMN_ID * 10) AS sort,"
								+ "		c.COMMENTS AS comments,"
								+ "		t.CHAR_LENGTH AS maxLength,"
								+ "		decode(t.DATA_TYPE,'DATE',t.DATA_TYPE || '(' || t.DATA_LENGTH || ')',"
								+ "		'VARCHAR2', t.DATA_TYPE || '(' || t.DATA_LENGTH || ')',"
								+ "		'VARCHAR', t.DATA_TYPE || '(' || t.DATA_LENGTH || ')',"
								+ "		'NVARCHAR2', t.DATA_TYPE || '(' || t.DATA_LENGTH/2 || ')',"
								+ "		'CHAR', t.DATA_TYPE || '(' || t.DATA_LENGTH || ')',"
								+ "		'NUMBER',t.DATA_TYPE || (nvl2(t.DATA_PRECISION,nvl2(decode(t.DATA_SCALE,0,null,t.DATA_SCALE),"
								+ "		'(' || t.DATA_PRECISION || ',' || t.DATA_SCALE || ')',"
								+ "		'(' || t.DATA_PRECISION || ')'),'(18)')),t.DATA_TYPE) AS jdbcType "
								+ "	FROM user_tab_columns t, user_col_comments c "
								+ "	WHERE t.TABLE_NAME = c.table_name"
								+ "		AND t.COLUMN_NAME = c.column_name"
								+ "		AND t.TABLE_NAME = upper(?) "
								+ "	ORDER BY t.COLUMN_ID";
	
	/**
	 * 根据表名获取其所有的字段列表
	 * @param table
	 * @return
	 */
	public List<Map<String, Object>> queryColsByTable(String table){
		if(DbKind.isMysql()){
			return this.queryForList(Query.newQuery(queryColsByTable_MYSQL, table));
		}else{
			return this.queryForList(Query.newQuery(queryColsByTable_ORACLE, table));
		}
	}
}
