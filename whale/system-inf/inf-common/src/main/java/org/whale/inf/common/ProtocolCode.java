package org.whale.inf.common;

import java.io.IOException;
import java.io.Serializable;

/**
 * HTTP请求 编码解码协议
 * 
 * @author 王金绍
 * @date 2015年6月15日 下午6:13:43
 */
public interface ProtocolCode<M extends Serializable> {

	/**
	 * 编码  JSON 或者 XML 
	 * @param m
	 * @return
	 * @throws IOException
	 */
	String encode(M m) throws IOException;
	
	/**
	 * 解码 
	 * @param body
	 * @return
	 * @throws IOException
	 */
	M decode(String body) throws IOException;
}
