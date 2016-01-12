package org.whale.inf.common;

/**
 * 签名服务
 * 
 * @author wjs
 * @date 2015年12月1日 下午3:58:33
 */
public interface SignService {

	/**
	 * 请求字符串签名
	 * @param datas
	 * @param context
	 * @return
	 */
	public String signReq(InfContext context);
	
	/**
	 * 请求字符串签名
	 * @param datas
	 * @param context
	 * @return
	 */
	public String signResp(InfContext context);
}
