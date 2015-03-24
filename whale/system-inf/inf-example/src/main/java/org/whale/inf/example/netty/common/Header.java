package org.whale.inf.example.netty.common;

import java.util.HashMap;
import java.util.Map;

public class Header {
	
	/**业务请求消息 */
	public static final byte MSG_REQ = 0;
	/**业务响应消息 */
	public static final byte MSG_RESP = 1;
	/**业务ONE WAY消息(即是请求又是响应消息) */
	public static final byte MSG_ONE_WAY = 2;
	/**心跳请求消息 */
	public static final byte HEART_BEAT_REQ = 3;
	/**心跳响应消息 */
	public static final byte HEART_BEAT_RESP = 4;
	/**握手登录消息 */
	public static final byte LOGIN_REQ = 5;
	/**握手返回消息 */
	public static final byte LOGIN_RESP = 6;

	private int crcCode = 0xabef0101;
	
	private int length; //消息长度
	
	private long sessionId;//会话ID
	
	private byte type;//消息类型
	
	private byte priority;//消息优先级
	
	//private String token;//校验码
	
	private Map<String, String> attachment = new HashMap<String, String>();

	public int getCrcCode() {
		return crcCode;
	}

	public void setCrcCode(int crcCode) {
		this.crcCode = crcCode;
	}

	public int getLength() {
		return length;
	}

	public void setLength(int length) {
		this.length = length;
	}

	public long getSessionId() {
		return sessionId;
	}

	public void setSessionId(long sessionId) {
		this.sessionId = sessionId;
	}

	public byte getType() {
		return type;
	}

	public void setType(byte type) {
		this.type = type;
	}

	public byte getPriority() {
		return priority;
	}

	public void setPriority(byte priority) {
		this.priority = priority;
	}

//	public String getToken() {
//		return token;
//	}
//
//	public void setToken(String token) {
//		this.token = token;
//	}

	public Map<String, String> getAttachment() {
		return attachment;
	}

	public void setAttachment(Map<String, String> attachment) {
		this.attachment = attachment;
	}
	
	
}
