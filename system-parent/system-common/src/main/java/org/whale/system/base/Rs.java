package org.whale.system.base;

import com.alibaba.fastjson.JSON;

public class Rs {
	
	private static final String DEFAULT_SUCCESS_MSG = "成功";
	private static final String DEFAULT_FAIL_MSG = "失败";
	
	/**
	 * 将处理成功结果(默认提示信息）返回给客服端ajax脚本
	 */
	public static Rs success() {
		return success(DEFAULT_SUCCESS_MSG, null);
	}
	
	/**
	 * 将处理成功结果(用户设置提示信息）返回给客服端ajax脚本
	 * @param msg
	 */
	public static Rs success(String msg) {
		return success(msg, null);
	}
	
	/**
	 * 将处理成功结果(用户返回的数据）返回给客服端ajax脚本
	 * @param obj 用户返回的数据
	 */
	public static Rs success(Object obj) {
		return success(DEFAULT_SUCCESS_MSG, obj);
	}
	
	/**
	 * 将处理成功结果(用户设置提示信息,用户返回的数据）返回给客服端ajax脚本
	 * @param msg  用户设置提示信息
	 * @param obj  用户返回的数据
	 */
	public static Rs success(String msg, Object obj) {
		return new Rs(true, msg, obj, null);
	}
	
	/**
	 * 将处理失败结果（用户设置失败提示信息） 返回给客服端ajax脚本
	 * @param msg  用户设置失败提示信息
	 */
	public static Rs fail(String msg) {
		return fail(msg, null);
	}
	
	/**
	 * 将处理失败结果（用户返回失败相关数据） 返回给客服端ajax脚本
	 * @param obj  用户返回失败相关数据
	 */
	public static Rs fail(Object obj) {
		return fail(DEFAULT_FAIL_MSG, obj);
	}
	
	/**
	 * 将处理失败结果（用户设置失败提示信息,用户返回失败相关数据） 返回给客服端ajax脚本
	 * @param msg  用户设置失败提示信息
	 * @param obj  用户返回失败相关数据
	 */
	public static Rs fail(String msg, Object obj) {
		return fail(msg, null, obj);
	}
	
	/**
	 * 将处理失败结果（用户设置失败提示信息,用户返回失败相关数据） 返回给客服端ajax脚本
	 * @param msg
	 * @param code 编码
	 */
	public static Rs fail(String msg, String code) {
		return fail(msg, code, null);
	}
	
	/**
	 * 将处理失败结果（用户设置失败提示信息,用户返回失败相关数据） 返回给客服端ajax脚本
	 * @param msg 信息
	 * @param code 编码
	 * @param obj 数据
	 */
	public static Rs fail(String msg, String code, Object obj) {
		return new Rs(false, msg, obj, code);
	}
	
	

	private boolean rs = true;
	
	private String code = "1";
	
	private String msg;
	
	private Object datas;
	
	public Rs(){}
	
	public Rs(boolean rs, String msg, Object datas, String code){
		this.rs = rs;
		this.code = code;
		this.msg = msg;
		this.datas = datas;
	}

	public boolean getRs() {
		return rs;
	}

	public void setRs(boolean rs) {
		this.rs = rs;
	}

	public String getCode() {
		return code;
	}

	public void setCode(String code) {
		this.code = code;
	}

	public String getMsg() {
		return msg;
	}

	public void setMsg(String msg) {
		this.msg = msg;
	}

	public Object getDatas() {
		return datas;
	}

	public void setDatas(Object datas) {
		this.datas = datas;
	}
	
	@Override
	public String toString() {
		StringBuilder strb = new StringBuilder("{\"rs\":");
		strb.append(rs ? "true" : "false").append(",\"code\":\"").append(code)
			.append("\",\"msg\":\"").append(null == msg ? "" : msg)
			.append("\",\"datas\":").append(null == datas ? "{}" : JSON.toJSONString(datas))
			.append("}");
		return strb.toString();
	}
}
