package org.whale.system.jdbc.orm.table;

import org.whale.system.jdbc.orm.entry.OrmTable;

/**
 * 从实体中解析元注释，构建OrmTable对象
 *
 * @author wjs
 * 2014年9月6日-下午1:58:58
 */
public interface OrmTableBulider {

	OrmTable parse(Class<?> clazz);
}
