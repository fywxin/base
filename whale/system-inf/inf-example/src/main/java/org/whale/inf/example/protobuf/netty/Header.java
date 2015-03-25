package org.whale.inf.example.protobuf.netty;

import com.baidu.bjf.remoting.protobuf.FieldType;
import com.baidu.bjf.remoting.protobuf.annotation.Protobuf;

public class Header {

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
