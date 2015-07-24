package org.whale.inf.client;

import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Map;

import org.whale.inf.client.chain.HttpInvoke;

public class RpcContext {
	
	//代理接口对象
	private Object proxyObject;
	
	private Method method;
	
	private Class<?>[] paramTypes;
	
	private String version;

    private Object[] args;
    
    private String argsStr;
    
    private String serviceUrl;
    
    private Integer conTimeOut;
	
	private Integer readTimeOut;
	
	private Integer retry;
	
	private Map<String, Object> attachment = new HashMap<String, Object>();
	
	private Object rs;
	
	private HttpInvoke nextInvoke;
	
	private HttpInvokeFactory httpInvokeFactory;
	
	private boolean post;
	
	private Map<String, Object> header = new HashMap<String, Object>();

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

	
	public String getServiceUrl() {
		return serviceUrl;
	}

	public void setServiceUrl(String serviceUrl) {
		this.serviceUrl = serviceUrl;
	}

	public Integer getConTimeOut() {
		return conTimeOut;
	}

	public void setConTimeOut(Integer conTimeOut) {
		this.conTimeOut = conTimeOut;
	}

	public Integer getReadTimeOut() {
		return readTimeOut;
	}

	public void setReadTimeOut(Integer readTimeOut) {
		this.readTimeOut = readTimeOut;
	}

	public Integer getRetry() {
		return retry;
	}

	public void setRetry(Integer retry) {
		this.retry = retry;
	}

	public String getVersion() {
		return version;
	}

	public void setVersion(String version) {
		this.version = version;
	}

	public Map<String, Object> getAttachment() {
		return attachment;
	}

	public void setAttachment(Map<String, Object> attachment) {
		this.attachment = attachment;
	}

	public HttpInvoke getNextInvoke() {
		return nextInvoke;
	}

	public void setNextInvoke(HttpInvoke nextInvoke) {
		this.nextInvoke = nextInvoke;
	}

	public HttpInvokeFactory getHttpInvokeFactory() {
		return httpInvokeFactory;
	}

	public void setHttpInvokeFactory(HttpInvokeFactory httpInvokeFactory) {
		this.httpInvokeFactory = httpInvokeFactory;
	}

	public Class<?>[] getParamTypes() {
		return paramTypes;
	}

	public void setParamTypes(Class<?>[] paramTypes) {
		this.paramTypes = paramTypes;
	}

	public Object[] getArgs() {
		return args;
	}

	public void setArgs(Object[] args) {
		this.args = args;
	}

	public Method getMethod() {
		return method;
	}

	public void setMethod(Method method) {
		this.method = method;
	}

	public String getArgsStr() {
		return argsStr;
	}

	public void setArgsStr(String argsStr) {
		this.argsStr = argsStr;
	}

	public boolean getPost() {
		return post;
	}

	public void setPost(boolean post) {
		this.post = post;
	}
	
}
