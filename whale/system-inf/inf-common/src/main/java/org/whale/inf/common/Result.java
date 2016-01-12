package org.whale.inf.common;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

import org.whale.system.common.util.ThreadContext;

/**
 * 服务端返回结果对象
 * 
 * @author 王金绍
 * @date 2015年12月1日 下午3:43:12
 */
public class Result<T> implements Serializable {

	private static final long serialVersionUID = -56340583049801L;
	
	private static final String KEY_INF_RESULT = "inf_Result";

	//返回编码
	private String code = ResultCode.SUCCESS.getCode();
	
	//提示信息
	private String msg = ResultCode.SUCCESS.getMsg();
	
	//返回数据
	private T data;
	
	//附加信息
	private Map<String, Object> attachment;
	
	public static Result<?> get(){
		return (Result<?>)ThreadContext.getContext().get(KEY_INF_RESULT);
	}
	
	public static void set(Result<?> rs){
		ThreadContext.getContext().put(KEY_INF_RESULT, rs);
	}
	
	@SuppressWarnings("all")
	public static Result<?> success(){
		return new Result();
	}
	
	public static <M> Result<M> success(M data){
		Result<M> rs = new Result<M>();
		rs.setData(data);
		return rs;
	}
	
	public static <M> Result<M> fail(ResultCode resultCode){
		return fail(resultCode, null);
	}
	
	public static <M> Result<M> fail(ResultCode resultCode, M data){
		return fail(resultCode.getCode(), resultCode.getMsg(), data);
	}
	
	public static <M> Result<M> fail(String code, String message){
		return fail(code, message, null);
	}
	
	public static <M> Result<M> fail(String code, String message, M data){
		Result<M> rs = new Result<M>();
		rs.setCode(code);
		rs.setMsg(message);
		rs.setData(data);
		return rs;
	}
	
	
 	
	public boolean isSuccess(){
		return ResultCode.SUCCESS.getCode().equals(code);
	}
	
	public Result<?> putAttachment(String key, Object val){
		if(attachment == null){
			attachment = new HashMap<String, Object>();
		}
		attachment.put(key, val);
		return this;
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

	public T getData() {
		return data;
	}

	public void setData(T data) {
		this.data = data;
	}
	
	public Map<String, Object> getAttachment() {
		return attachment;
	}

	public void setAttachment(Map<String, Object> attachment) {
		this.attachment = attachment;
	}

	@Override
	public String toString() {
		return "Result [" + code + ": " + msg + "\ndata=" + data + "\nattachment=" + attachment + "]";
	}
}
