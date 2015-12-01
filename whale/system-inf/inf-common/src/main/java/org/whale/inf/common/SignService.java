package org.whale.inf.common;

/**
 * 签名服务
 * 
 * @author 王金绍
 * @date 2015年12月1日 下午3:58:33
 */
public interface SignService {

	/**
	 * 签名
	 * @param datas
	 * @param context
	 * @return
	 */
	public String sign(InfContext context);
}
