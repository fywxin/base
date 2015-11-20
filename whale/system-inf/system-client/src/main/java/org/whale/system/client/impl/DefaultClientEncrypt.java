package org.whale.system.client.impl;

import org.whale.system.client.ClientContext;
import org.whale.system.client.ClientEncrypt;

/**
 * 不加解密
 * 
 * @author 王金绍
 * @date 2015年11月20日 下午6:01:35
 */
public class DefaultClientEncrypt implements ClientEncrypt {

	@Override
	public byte[] onWrite(byte[] datas, ClientContext clientContext) {
		return datas;
	}

	@Override
	public byte[] onRead(byte[] datas, ClientContext clientContext) {
		return datas;
	}

}
