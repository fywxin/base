package org.whale.inf.client;

import java.util.List;

/**
 * 编码解码器
 * 
 * @author wjs
 * @date 2015年11月20日 下午5:45:15
 */
public interface ClientCodec {

	public byte[] encode(List<Object> args, ClientContext clientContext);
	
	public Object decode(byte[] data, ClientContext clientContext);
}
