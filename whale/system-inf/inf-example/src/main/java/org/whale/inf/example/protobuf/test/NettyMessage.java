package org.whale.inf.example.protobuf.test;

import com.baidu.bjf.remoting.protobuf.FieldType;
import com.baidu.bjf.remoting.protobuf.annotation.Protobuf;


public class NettyMessage {

	@Protobuf(fieldType = FieldType.OBJECT, order=1, required = true)
	private Header header;
	
	@Protobuf(fieldType = FieldType.OBJECT, order=2, required = true)
	private Object body;

	public Header getHeader() {
		return header;
	}

	public void setHeader(Header header) {
		this.header = header;
	}

	public Object getBody() {
		return body;
	}

	public void setBody(Object body) {
		this.body = body;
	}
	
	
}
