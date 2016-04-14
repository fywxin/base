package org.whale.inf.server;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.whale.inf.common.InfContext;
import org.whale.system.common.util.ThreadContext;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;

/**
 * 接口请求上下文
 * 
 * @author wjs
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
	
	//登录后，参数必须携带该值
	private String session;
	
	//时间戳
	private String timestamp;
	
	//接口版本
	private String version;
	
	//参数签名结果
	private String sign;
	
	//响应格式
	private String format;
	
	//是否启用压缩
	private Boolean gzip;
	
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
	
	private HttpServletResponse response;
	
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

	public String getSession() {
		return session;
	}

	public void setSession(String session) {
		this.session = session;
	}

	public String getTimestamp() {
		return timestamp;
	}

	public void setTimestamp(String timestamp) {
		this.timestamp = timestamp;
	}

	public String getVersion() {
		return version;
	}

	public void setVersion(String version) {
		this.version = version;
	}

	public String getSign() {
		return sign;
	}

	public void setSign(String sign) {
		this.sign = sign;
	}

	public String getFormat() {
		return format;
	}

	public void setFormat(String format) {
		this.format = format;
	}

	public Boolean getGzip() {
		return gzip;
	}

	public void setGzip(Boolean gzip) {
		this.gzip = gzip;
	}
	
	public HttpServletResponse getResponse() {
		return response;
	}

	public void setResponse(HttpServletResponse response) {
		this.response = response;
	}

	@Override
	public String toString() {
		return "ServerContext [uri=" + uri + ", appId=" + appId + ", session="+session+ ", sign="+ sign+", reqno="
				+ reqno + ", \nreqStr=" + reqStr + ", \nrespStr=" + respStr
				+ ", \nargs=" + args + ", rs=" + rs + ", \nattachment=" + attachment+ "]";
	}
	
	public String toParamStr(){
		return "appId=" + appId +", uri=" + uri+", version="+ version +", reqno="+ reqno+ ", sign="+ sign;
	}
	
}
