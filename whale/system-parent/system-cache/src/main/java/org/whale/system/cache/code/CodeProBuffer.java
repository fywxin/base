package org.whale.system.cache.code;

import java.io.IOException;
import java.io.Serializable;

public class CodeProBuffer<M extends Serializable> extends AbstractCode<M> {

	@Override
	public byte[] doEncode(String cacheName, M m) throws IOException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public M doDecode(Class<?> type, byte[] bytes) throws IOException {
		// TODO Auto-generated method stub
		return null;
	}

}
