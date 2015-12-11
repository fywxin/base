package org.whale.inf.server;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.whale.inf.common.InfContext;
import org.whale.system.common.util.ThreadContext;

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
	
	public static ServerContext get(){
		return (ServerContext)ThreadContext.getContext().get(ServerContext.THREAD_KEY);
	}
	
	public static void set(ServerContext serverContext){
		ThreadContext.getContext().put(ServerContext.THREAD_KEY, serverContext);
	}
	
	public static void remove(){
		ThreadContext.getContext().remove(ServerContext.THREAD_KEY);
	}
	
	//当前解析参数位置
	private transient Integer paramIndex;
	//请求URI
	private String uri;
	//客户端APPID
	private String appId;
	//请求流水号
	private String reqno;
	
	private String reqStr;
	
	private String respStr;
	
	private List<Object> args;
	
	private Object rs;
	
	private Map<String, Object> attachment;
	
	private JSONArray bodyJsonArr;
	
	//响应内容是否需要加密
	private boolean respEncryptFlag;
	
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

	public boolean getRespEncryptFlag() {
		return respEncryptFlag;
	}

	public void setRespEncryptFlag(boolean respEncryptFlag) {
		this.respEncryptFlag = respEncryptFlag;
	}

	public String getUri() {
		return uri;
	}

	public void setUri(String uri) {
		this.uri = uri;
	}

	public String getAppId() {
		return appId;
	}

	public void setAppId(String appId) {
		this.appId = appId;
	}

	public String getReqno() {
		return reqno;
	}

	public void setReqno(String reqno) {
		this.reqno = reqno;
	}

	@Override
	public String toString() {
		return "ServerContext [uri=" + uri + ", appId=" + appId + ", reqno="
				+ reqno + ", reqStr=" + reqStr + ", respStr=" + respStr
				+ ", args=" + args + ", rs=" + rs + ", attachment="
				+ attachment + ", respEncryptFlag=" + respEncryptFlag + "]";
	}
	
	
	
}
