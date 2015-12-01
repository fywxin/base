package org.whale.inf.server;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.whale.inf.common.InfContext;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;

/**
 * 接口请求上下文
 * 
 * @author 王金绍
 * @date 2015年12月1日 下午4:20:16
 */
public class ServerContext implements InfContext {
	
	public transient static final String THREAD_KEY = "server:p:i";
	
	//当前解析参数位置
	private transient Integer paramIndex;
	
	private String reqStr;
	
	private String respStr;
	
	private List<Object> args;
	
	private Object rs;
	
	private Map<String, Object> attachment;
	
	private String body;
	
	private JSONArray bodyJsonArr;
	
	private HttpServletRequest request;
	
	public JSONObject readJSONObject(){
		return bodyJsonArr.getJSONObject(this.paramIndex);
	}
	
	public JSONArray readJSONArray(){
		return bodyJsonArr.getJSONArray(this.paramIndex);
	}
	
	public void addArg(int index, Object arg){
		if(args == null){
			args = new ArrayList<Object>();
		}
		args.add(index, arg);
	}
	
	
	public void incParamIndex(){
		this.paramIndex++;
	}

	
	public Integer getParamIndex() {
		return paramIndex;
	}

	public void setParamIndex(Integer paramIndex) {
		this.paramIndex = paramIndex;
	}


	public List<Object> getArgs() {
		return args;
	}


	public void setArgs(List<Object> args) {
		this.args = args;
	}


	public Object getRs() {
		return rs;
	}

	public void setRs(Object rs) {
		this.rs = rs;
	}

	public Map<String, Object> getAttachment() {
		return attachment;
	}

	public void setAttachment(Map<String, Object> attachment) {
		this.attachment = attachment;
	}

	public String getBody() {
		return body;
	}

	public void setBody(String body) {
		this.body = body;
	}

	public JSONArray getBodyJsonArr() {
		return bodyJsonArr;
	}

	public void setBodyJsonArr(JSONArray bodyJsonArr) {
		this.bodyJsonArr = bodyJsonArr;
	}

	public String getReqStr() {
		return reqStr;
	}

	public void setReqStr(String reqStr) {
		this.reqStr = reqStr;
	}

	public String getRespStr() {
		return respStr;
	}

	public void setRespStr(String respStr) {
		this.respStr = respStr;
	}

	public HttpServletRequest getRequest() {
		return request;
	}

	public void setRequest(HttpServletRequest request) {
		this.request = request;
	}
	
	
}
