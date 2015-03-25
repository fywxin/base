package org.whale.inf.example.protobuf.test;

import java.util.HashMap;
import java.util.Map;

import com.baidu.bjf.remoting.protobuf.FieldType;
import com.baidu.bjf.remoting.protobuf.annotation.Protobuf;

public class Header {
	
	/**业务请求消息 */
	public static final int MSG_REQ = 0;
	/**业务响应消息 */
	public static final int MSG_RESP = 1;
	/**业务ONE WAY消息(即是请求又是响应消息) */
	public static final int MSG_ONE_WAY = 2;
	/**心跳请求消息 */
	public static final int HEART_BEAT_REQ = 3;
	/**心跳响应消息 */
	public static final int HEART_BEAT_RESP = 4;
	/**握手登录消息 */
	public static final int LOGIN_REQ = 5;
	/**握手返回消息 */
	public static final int LOGIN_RESP = 6;

	@Protobuf(fieldType = FieldType.INT32, order=1, required = true)
	private int crcCode = 0xabef0101;
	@Protobuf(fieldType = FieldType.INT32, order=2, required = true)
	private int length; //消息长度
	@Protobuf(fieldType = FieldType.INT64, order=3, required = true)
	private long sessionId;//会话ID
	@Protobuf(fieldType = FieldType.INT32, order=4, required = true)
	private int type;//消息类型
	@Protobuf(fieldType = FieldType.INT32, order=5, required = true)
	private int priority;//消息优先级
	@Protobuf(fieldType = FieldType.STRING, order=6, required = true)
	private String token;//校验码
//	@Protobuf(fieldType = FieldType.OBJECT, order=7, required = true)
//	private Map<String, String> attachment = new HashMap<String, String>();
	
	
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
	public int getType() {
		return type;
	}
	public void setType(int type) {
		this.type = type;
	}
	public int getPriority() {
		return priority;
	}
	public void setPriority(int priority) {
		this.priority = priority;
	}
	public String getToken() {
		return token;
	}
	public void setToken(String token) {
		this.token = token;
	}
	

	
}
