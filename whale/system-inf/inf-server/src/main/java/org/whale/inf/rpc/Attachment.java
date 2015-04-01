package org.whale.inf.rpc;

import java.util.Map;

/**
 * 附加信息
 * 
 * @author Administrator
 *
 */
public interface Attachment {

	Map<String, String> getAttachments();
	
	String getAttachment(String key);
	
	String getAttachment(String key, String defaultValue);
}
