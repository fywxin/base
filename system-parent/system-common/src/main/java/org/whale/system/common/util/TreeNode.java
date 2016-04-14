package org.whale.system.common.util;

import java.util.Map;

public interface TreeNode {

	Object id();
	
	Object pid();
	
	String name();
	
	Map<String, Object> asMap();
}
