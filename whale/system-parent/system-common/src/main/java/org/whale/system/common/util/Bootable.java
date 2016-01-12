package org.whale.system.common.util;

import java.util.Map;

import org.springframework.core.PriorityOrdered;

/**
 * 初始化启动
 *
 * @author wjs
 * 2014年9月6日-下午1:29:45
 */
public interface Bootable extends PriorityOrdered{
	
	Object init(Map<String, Object> context);
	
	boolean access();
	
}
