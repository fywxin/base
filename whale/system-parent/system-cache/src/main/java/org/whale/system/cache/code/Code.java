package org.whale.system.cache.code;

import java.io.IOException;
import java.io.Serializable;

/**
 * 编码解码器
 *
 * @author 王金绍
 * 2015年4月25日 下午10:29:50
 */
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
