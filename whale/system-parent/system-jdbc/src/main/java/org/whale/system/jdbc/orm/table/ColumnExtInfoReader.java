package org.whale.system.jdbc.orm.table;

import org.springframework.core.PriorityOrdered;
import org.whale.system.jdbc.orm.entry.OrmColumn;

/**
 * 字段扩展信息读取，将读取到的扩张信息保存到 extInfo 字段
 *
 * @author 王金绍
 * 2015年4月26日 上午9:01:39
 */
public interface ColumnExtInfoReader extends PriorityOrdered {

	/**
	 * 读取字段扩张信息
	 * 
	 * @param ormColumn
	 * 2015年4月26日 上午9:05:31
	 */
	void readExtInfo(OrmColumn ormColumn);

	/**
	 * 是否匹配字段
	 * @param ormColumn
	 * @return
	 * 2015年4月26日 上午9:05:40
	 */
	boolean match(OrmColumn ormColumn);
}
