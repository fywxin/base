package org.whale.inf.common;

/**
 * 加密解密类
 * 
 * @author 王金绍
 * @date 2015年12月1日 下午3:57:59
 */
public interface EncryptService {

	public byte[] onWrite(byte[] datas, InfContext context);
	
	public byte[] onRead(byte[] datas, InfContext context);
}
