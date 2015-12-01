package org.whale.inf.common;

import java.util.List;
import java.util.Map;

/**
 * 接口请求上线文
 * 
 * @author 王金绍
 * @date 2015年12月1日 下午3:52:49
 */
public interface InfContext {

	/**
	 * 获取请求字符串
	 * @return
	 */
	String getReqStr();
	
	/**
	 * 设置请求字符串
	 * @param reqStr
	 */
	void setReqStr(String reqStr);
	
	/***
	 * 获取响应字符串
	 * @return
	 */
	String getRespStr();
	
	/**
	 * 设置响应字符串
	 * @param respStr
	 */
	void setRespStr(String respStr);
	
	/**
	 * 获取请求参数
	 * @return
	 */
	List<Object> getArgs();
	
	/**
	 * 设置请求参数
	 * @param args
	 */
	void setArgs(List<Object> args);
	
	/**
	 * 获取处理结果
	 * @return
	 */
	Object getRs();
	
	/**
	 * 设置处理结果
	 */
	void setRs(Object rs);
	
	/**
	 * 获取关联信息
	 * @return
	 */
	Map<String, Object> getAttachment();
	
	/**
	 * 返回关联信息
	 * @param attachment
	 */
	void setAttachment(Map<String, Object> attachment);
}
