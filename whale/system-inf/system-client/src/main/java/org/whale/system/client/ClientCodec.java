package org.whale.system.client;

/**
 * 编码解码器
 * 
 * @author 王金绍
 * @date 2015年11月20日 下午5:45:15
 */
public interface ClientCodec {

	public byte[] encode(Object[] args, ClientContext clientContext);
	
	public Object decode(byte[] data, ClientContext clientContext);
}
