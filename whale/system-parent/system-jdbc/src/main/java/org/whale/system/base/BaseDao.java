package org.whale.system.base;

import java.io.Serializable;

import org.whale.system.jdbc.OrmDaoWrapper;

/**
 * Dao 层条件不满足，应该直接抛出异常，因为编码中，在service层就需检测确保dao层的数据符合条件
 *
 * @author wjs
 * 2014年9月6日-下午1:48:47
 */
public abstract class BaseDao<T extends Serializable,PK extends Serializable> extends OrmDaoWrapper<T, PK>{
	
}