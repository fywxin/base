package org.whale.system.jdbc.common;

import java.util.HashMap;
import java.util.Map;

public class OrmConstant {

	public static final Map<Integer, String> JDBC_TYPE_MAP = new HashMap<Integer, String>();
	
	static{
	//	JDBC_TYPE_MAP.put(key, "Long")
	}
	
	public static void main(String[] args) {
		System.out.println(Long.MAX_VALUE);
		System.out.println(Integer.MAX_VALUE);
	}
}
