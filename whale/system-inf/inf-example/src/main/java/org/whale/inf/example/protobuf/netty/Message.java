package org.whale.inf.example.protobuf.netty;

import com.baidu.bjf.remoting.protobuf.FieldType;
import com.baidu.bjf.remoting.protobuf.annotation.Protobuf;

public class Message {
	
	@Protobuf(fieldType = FieldType.INT64, order=3, required = true)
	private Long id;
	
	@Protobuf(fieldType = FieldType.OBJECT, order=2, required = true)
	private Header header;
	
	private String name;

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public Header getHeader() {
		return header;
	}

	public void setHeader(Header header) {
		this.header = header;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}
	
	
}
