package org.whale.system.cache.code;

import java.io.IOException;
import java.io.Serializable;

import com.baidu.bjf.remoting.protobuf.Codec;
import com.baidu.bjf.remoting.protobuf.ProtobufProxy;

@SuppressWarnings("all")
public class CodeProtoBuf<M extends Serializable> extends AbstractCode<M> {
	

	@Override
	public byte[] doEncode(String cacheName, M m) throws IOException {
		Class<M> clazz = (Class<M>)this.map.get(cacheName);
		Codec<M> codec = ProtobufProxy.create(clazz);
		return codec.encode(m);
	}

	@Override
	public M doDecode(Class<?> clazz, byte[] bytes) throws IOException {
		Codec<M> codec = (Codec<M>)ProtobufProxy.create(clazz);
		return codec.decode(bytes);
	}

}
