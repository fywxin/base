package org.whale.system.cache.code;

import java.io.IOException;
import java.io.Serializable;

public interface Code<M extends Serializable> {

	/**
	 * 编码
	 * @param m
	 * @return
	 */
	byte[] encode(String cacheName, M m) throws IOException;
	
	/**
	 * 解码
	 * @param bytes
	 * @return
	 */
	M decode(String cacheName, byte[] bytes) throws IOException;
}
