package org.whale.inf.client;

import java.lang.reflect.Method;
import java.net.Proxy;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.whale.inf.common.InfContext;
import org.whale.system.common.util.ThreadContext;

/**
 * 客户端请求上下文
 * 
 * @author 王金绍
 * 2015年11月8日 上午12:08:04
 */
public class ClientContext implements InfContext{
	
	public static ClientContext get(){
		return (ClientContext)ThreadContext.getContext().get(ThreadContext.KEY_CLIENT_CONTEXT);
	}
	
	public static void set(ClientContext clientContext){
		ThreadContext.getContext().put(ThreadContext.KEY_CLIENT_CONTEXT, clientContext);
	}
	
	//注册类@RequestMapping 地址
	private String serviceUrl;
	
	//最后http调用地址  clientConf + serviceUrl + methodName
	private String url;
	
	//请求参数
	private Map<String, Object> params = new HashMap<String, Object>();
	
	//请求头
	private Map<String, String> headers = new HashMap<String, String>();

	//执行方法
	private Method method;
	
	//代理对象
	private Object proxyObject;
	
	//请求参数对象
	private List<Object> args;
	
	//响应对象
	private Object rs;
	
	//请求字符串 可能为空
	private String reqStr;
	
	//响应字符串 可能为空
	private String respStr;
	
	//连接超时
	private Integer connectTimeout;
	
	//读超时
	private Integer readTimeout;
	
	//http代理
	private Proxy httpProxy = Proxy.NO_PROXY;
	
	/**请求流水号 */
	private String reqno;
	
	//错误重试
	private Integer retry;
		
	//是否异步
	private boolean isAsyc;
	
	//当前请求session
	private String session;
	
	
	
	private Map<String, Object> attachment = new HashMap<String, Object>();
	
	public ClientContext putParam(String key, Object value) {
		this.getParams().put(key, value);
		return this;
	}
	
	public ClientContext putAttachment(String key, Object value) {
		this.getAttachment().put(key, value);
		return this;
	}
	
	public ClientContext putHeader(String key, String value) {
		this.getHeaders().put(key, value);
		return this;
	}

	public String getUrl() {
		return url;
	}

	public void setUrl(String url) {
		this.url = url;
	}

	public Method getMethod() {
		return method;
	}

	public void setMethod(Method method) {
		this.method = method;
	}

	public String getServiceUrl() {
		return serviceUrl;
	}

	public void setServiceUrl(String serviceUrl) {
		this.serviceUrl = serviceUrl;
	}

	public Map<String, Object> getParams() {
		return params;
	}

	public void setParams(Map<String, Object> params) {
		this.params = params;
	}

	public Map<String, String> getHeaders() {
		return headers;
	}

	public void setHeaders(Map<String, String> headers) {
		this.headers = headers;
	}

	public Object getProxyObject() {
		return proxyObject;
	}

	public void setProxyObject(Object proxyObject) {
		this.proxyObject = proxyObject;
	}

	public Object getRs() {
		return rs;
	}

	public void setRs(Object rs) {
		this.rs = rs;
	}

	public Proxy getHttpProxy() {
		return httpProxy;
	}

	public void setHttpProxy(Proxy httpProxy) {
		this.httpProxy = httpProxy;
	}

	public Integer getConnectTimeout() {
		return connectTimeout;
	}

	public void setConnectTimeout(Integer connectTimeout) {
		this.connectTimeout = connectTimeout;
	}

	public Integer getReadTimeout() {
		return readTimeout;
	}

	public void setReadTimeout(Integer readTimeout) {
		this.readTimeout = readTimeout;
	}

	public String getReqno() {
		return reqno;
	}

	public void setReqno(String reqno) {
		this.reqno = reqno;
	}

	public Map<String, Object> getAttachment() {
		return attachment;
	}

	public void setAttachment(Map<String, Object> attachment) {
		this.attachment = attachment;
	}

	public Integer getRetry() {
		return retry;
	}

	public void setRetry(Integer retry) {
		this.retry = retry;
	}

	public boolean getIsAsyc() {
		return isAsyc;
	}

	public void setIsAsyc(boolean isAsyc) {
		this.isAsyc = isAsyc;
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

	@Override
	public List<Object> getArgs() {
		return this.args;
	}

	@Override
	public void setArgs(List<Object> args) {
		this.args = args;
	}

	public String getSession() {
		return session;
	}

	public void setSession(String session) {
		this.session = session;
	}
	
	
}
