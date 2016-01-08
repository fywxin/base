package org.whale.system.common.util;

public interface ValidKeyParser {

	String parseKey(Object obj, String key);
	
	boolean support(Object obj);
}
