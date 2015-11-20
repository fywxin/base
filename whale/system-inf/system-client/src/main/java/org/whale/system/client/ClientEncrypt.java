package org.whale.system.client;

/**
 * 加密解密器
 * 
 * @author 王金绍
 * @date 2015年11月20日 下午5:59:40
 */
public interface ClientEncrypt {

	public byte[] onWrite(byte[] datas, ClientContext clientContext);
	
	public byte[] onRead(byte[] datas, ClientContext clientContext);
}
