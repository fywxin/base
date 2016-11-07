package org.whale.system.jdbc.orm.alter;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.whale.system.common.exception.OrmException;
import org.whale.system.jdbc.orm.OrmContext;
import org.whale.system.jdbc.orm.entry.OrmTable;

import javax.annotation.Resource;

public abstract class AbstractAlterTableService implements AlterTableService {
	
	private static Logger logger = LoggerFactory.getLogger(AbstractAlterTableService.class);

	@Autowired
	private OrmContext ormContext;
	@Resource(name="jdbcTemplate")
	JdbcTemplate jdbcTemplate;

	public void createTable(Class<?> clazz){
		this.createTable(this.getOrmTable(clazz));
	}
	
	public void dropTable(Class<?> clazz){
		OrmTable table = this.getOrmTable(clazz);
		try {
			this.jdbcTemplate.execute("DROP TABLE "+table.getTableDbName());
		} catch (Exception e) {
			logger.error("ORM：删除表格["+table.getTableDbName()+"]出现异常", e);
		}
		logger.info("ORM：删除表格["+table.getTableDbName()+"]完成！");
	}
	
	public abstract void createTable(OrmTable table);
	
	
	private OrmTable getOrmTable(Class<?> clazz){
		OrmTable table = ormContext.getOrmTable(clazz);
		if(table == null) 
			throw new OrmException("Orm容器中查找不到类["+clazz.getName()+"]对应的OrmTable对象");
		return table;
	}
}
