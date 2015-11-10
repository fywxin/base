package org.whale.system.client;

import java.lang.reflect.Method;
import java.net.Proxy;
import java.util.HashMap;
import java.util.Map;

/**
 * 客户端请求上下文
 * 
 * @author 王金绍
 * 2015年11月8日 上午12:08:04
 */
public class ClientContext {
	
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
	
	//请求参数，只能是一个
	private Object arg;
	
	//响应对象
	private Object rs;
	
	//请求字符串
	private String reqStr;
	
	//响应字符串
	private String resStr;
	
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
	
	//异步回调处理类
	private AsycHandler asycHandler;
	
	private Map<String, Object> attachment = new HashMap<String, Object>();

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

	public Object getArg() {
		return arg;
	}

	public void setArg(Object arg) {
		this.arg = arg;
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

	public String getReqStr() {
		return reqStr;
	}

	public void setReqStr(String reqStr) {
		this.reqStr = reqStr;
	}

	public String getResStr() {
		return resStr;
	}

	public void setResStr(String resStr) {
		this.resStr = resStr;
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
	
	

	public AsycHandler getAsycHandler() {
		return asycHandler;
	}

	public void setAsycHandler(AsycHandler asycHandler) {
		this.asycHandler = asycHandler;
	}

	@Override
	public String toString() {
		return "ClientContext [serviceUrl=" + serviceUrl + ", url=" + url
				+ ", params=" + params + ", headers=" + headers + ", method="
				+ method + ", proxyObject=" + proxyObject + ", arg=" + arg
				+ ", rs=" + rs + ", reqStr=" + reqStr + ", resStr=" + resStr
				+ ", connectTimeout=" + connectTimeout + ", readTimeout="
				+ readTimeout + ", httpProxy=" + httpProxy + ", reqno=" + reqno
				+ ", attachment=" + attachment + "]";
	}
	
	
}