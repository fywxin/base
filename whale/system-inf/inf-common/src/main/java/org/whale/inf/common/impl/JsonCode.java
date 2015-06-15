package org.whale.inf.common.impl;

import java.io.IOException;
import java.io.Serializable;

import org.whale.inf.common.ProtocolCode;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.serializer.SerializerFeature;

public class JsonCode<M extends Serializable> implements ProtocolCode<M> {

	@Override
	public String encode(M m) throws IOException {
		return JSON.toJSONString(m, SerializerFeature.WriteClassName);
	}

	@Override
	public M decode(String body) throws IOException {
		return (M)JSON.parse(body);
	}

}
