package org.whale.system.jdbc.orm.table;

import org.springframework.core.PriorityOrdered;
import org.whale.system.jdbc.orm.entry.OrmTable;

/**
 * 表扩展信息预读取, 将读取到的信息保存到  extInfo 字段
 *
 * @author wjs
 * 2015年4月26日 上午8:34:15
 */
public interface TableExtInfoReader extends PriorityOrdered{
	
	/**
	 * 读取信息
	 * @param ormTable
	 * 2015年4月26日 上午9:01:14
	 */
	void readExtInfo(OrmTable ormTable);

	/**
	 * 是否匹配
	 * @param ormTable
	 * @return
	 * 2015年4月26日 上午9:01:21
	 */
	boolean match(OrmTable ormTable);
	
}
