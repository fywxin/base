package org.whale.system.jdbc.filter;

import java.io.Serializable;

import org.whale.system.jdbc.filter.dll.BaseDaoDllFilter;
import org.whale.system.jdbc.filter.query.BaseDaoQueryFilter;

/**
 * BaseDao 全局过滤器
 * 
 * 包含  BaseDaoDllFilter + BaseDaoQueryFilter
 *
 * @author wjs
 * 2015年4月26日 下午12:01:31
 */
public interface BaseDaoFilter<T extends Serializable,PK extends Serializable> extends BaseDaoDllFilter<T, PK>, BaseDaoQueryFilter<T, PK> {

}
