package org.whale.inf.common;

/**
 * 加密解密类
 * 
 * @author wjs
 * @date 2015年12月1日 下午3:57:59
 */
public interface EncryptService {

	/**
	 * 加密
	 * @param datas
	 * @param context
	 * @return
	 */
	public byte[] encrypt(byte[] datas, InfContext context);
	
	/**
	 * 解密
	 * @param datas
	 * @param context
	 * @return
	 */
	public byte[] decrypt(byte[] datas, InfContext context);
}
