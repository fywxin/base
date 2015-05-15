package org.whale.system.jdbc;

import java.io.Serializable;

import org.whale.system.base.IbaseDao;
import org.whale.system.jdbc.orm.OrmContext;
import org.whale.system.jdbc.orm.entry.OrmTable;

/**
 * Dao 模板方法定义
 * 
 * @author 王金绍
 * 2014年9月17日-上午10:37:39
 */
public interface IOrmDao<T extends Serializable,PK extends Serializable> extends IbaseDao<T, PK>{
	
	OrmTable getOrmTable();
	
	OrmContext getOrmContext();
	
}
