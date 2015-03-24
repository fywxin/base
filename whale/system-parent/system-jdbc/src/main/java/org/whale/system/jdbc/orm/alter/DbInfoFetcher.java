package org.whale.system.jdbc.orm.alter;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashSet;
import java.util.Set;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;
import org.whale.system.common.exception.SysException;
import org.whale.system.common.util.PropertiesUtil;
import org.whale.system.common.util.SpringContextHolder;
import org.whale.system.common.util.Strings;
import org.whale.system.jdbc.orm.OrmContext;
import org.whale.system.jdbc.util.DbKind;

@Component
public class DbInfoFetcher {
	private static Logger logger = LoggerFactory.getLogger(DbInfoFetcher.class);
	
	/** 自动创建表格键值常量 */
	private static final String ORM_TABLE_DDL = "orm.table.ddl";

	@Autowired
	JdbcTemplate jdbcTemplate;
	@Autowired
	private OrmContext ormContext;
	
	private AlterTableService alterTableService;
	
	public void getDbInfo(){
		try {
			logger.info("ORM:开始获取数据库基本信息...");
			DbKind.dbType = this.getDbName();
			DbKind.tables = this.getTableNames();
			DbKind.createTableModel = this.getCreateTableModel();
			if(DbKind.isMysql()){
				alterTableService = SpringContextHolder.getBean(MySqlAlterTableService.class);
			}else{
				alterTableService = SpringContextHolder.getBean(OracleAlterTableService.class);
			}
			logger.info("ORM: 获取数据库基本信息完成！");
		} catch (SQLException e) {
			throw new SysException("获取数据库元信息出错！",e);
		}
	}
	
	public void alertTable(Class<?> clazz){
		if(DbKind.createTableModel == null){
			this.getDbInfo();
		}
		if(DbKind.createTableModel == 0){
			
		}else if(DbKind.createTableModel == 1){
			alterTableService.dropTable(clazz);
			alterTableService.createTable(clazz);
		}else{
			if(!DbKind.isTableExist(ormContext.getOrmTable(clazz).getTableDbName())){
				alterTableService.createTable(clazz);
			}
		}
	}
	
	private Set<String> getTableNames() throws SQLException{
		ResultSet rs = this.jdbcTemplate.getDataSource().getConnection().getMetaData().getTables(null, null, null, null);
		Set<String> tableNames = new HashSet<String>(64);
		while(rs.next()){
			if(Strings.isNotBlank(rs.getString(3))){
				tableNames.add(rs.getString(3).toUpperCase());
			}
		}
		logger.info("ORM:数据库已存在表："+tableNames);
		return tableNames;
	}
	
	private String getDbName() throws SQLException{
		String driver = this.jdbcTemplate.getDataSource().getConnection().getMetaData().getDriverName();
		if(driver.toUpperCase().contains("MYSQL")){
			logger.info("ORM:数据库类型为：MYSQL");
			return "MYSQL";
		}
		if(driver.toUpperCase().contains("ORACLE")){
			logger.info("ORM:数据库类型为：ORACLE");
			return "ORACLE";
		}
		return null;
	}
	
	private Integer getCreateTableModel(){
		String ddl = PropertiesUtil.getValue(ORM_TABLE_DDL, "append").toLowerCase().trim();
		if("off".equals(ddl)){
			logger.info("ORM：创表模式：关闭");
			return 0;
		}else if("create".equals(ddl)){
			logger.info("ORM：创表模式：清空创建");
			return 1;
		}else{
			logger.info("ORM：创表模式 增量创建");
			return 2;
		}
	}
}
