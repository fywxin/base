package org.whale.inf.client;

import java.util.HashMap;
import java.util.Map;

import org.aopalliance.intercept.MethodInvocation;
import org.whale.inf.client.chain.HttpInvoke;


public class RpcContext {

	//
	private MethodInvocation invocation;
	//代理接口对象
	private Object proxyObject;
	
	private Object rs;
	
	private String hostUrl;
	
	private String serviceUri;
	
	private Integer conTimeOut;
	
	private Integer readTimeOut;
	
	private Integer retry;
	
	private String version;
	
	private Map<String, Object> attachment = new HashMap<String, Object>();
	
	private HttpInvoke nextInvoke;
	
	private HttpInvokeFactory httpInvokeRouter;

	public MethodInvocation getInvocation() {
		return invocation;
	}

	public void setInvocation(MethodInvocation invocation) {
		this.invocation = invocation;
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

	public String getHostUrl() {
		return hostUrl;
	}

	public void setHostUrl(String hostUrl) {
		this.hostUrl = hostUrl;
	}

	public String getServiceUri() {
		return serviceUri;
	}

	public void setServiceUri(String serviceUri) {
		this.serviceUri = serviceUri;
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

	public HttpInvokeFactory getHttpInvokeRouter() {
		return httpInvokeRouter;
	}

	public void setHttpInvokeRouter(HttpInvokeFactory httpInvokeRouter) {
		this.httpInvokeRouter = httpInvokeRouter;
	}
	
	
}
