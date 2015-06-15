package org.whale.inf.client;

import org.aopalliance.intercept.MethodInterceptor;
import org.aopalliance.intercept.MethodInvocation;
import org.springframework.aop.framework.ProxyFactory;
import org.springframework.beans.factory.FactoryBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.remoting.support.UrlBasedRemoteAccessor;
import org.whale.inf.client.chain.impl.RootHttpInvoke;

public class HttpInvokeFactory extends UrlBasedRemoteAccessor implements MethodInterceptor, FactoryBean{
	
	private Object proxyObject = null;
	
	private Integer conTimeOut;
	
	private Integer readTimeOut;
	
	private String version;
	
	@Autowired
	private RootHttpInvoke rootHttpInvoke;

	@Override
	public Object getObject() throws Exception {
		return proxyObject;
	}

	@Override
	public Class<?> getObjectType() {
		return getServiceInterface();
	}

	@Override
	public boolean isSingleton() {
		return true;
	}

	@Override
	public Object invoke(MethodInvocation invocation) throws Throwable {
		RpcContext rpcContext = new RpcContext();
		rpcContext.setInvocation(invocation);
		rpcContext.setVersion(version);
		rpcContext.setHttpInvokeRouter(this);
        
		rootHttpInvoke.invoke(rpcContext);
		return null;
	}
	
	@SuppressWarnings("all")
	@Override
	public void afterPropertiesSet() {
		super.afterPropertiesSet();
		proxyObject = ProxyFactory.getProxy(getServiceInterface(), this);
	}

	public Object getProxyObject() {
		return proxyObject;
	}

	public void setProxyObject(Object proxyObject) {
		this.proxyObject = proxyObject;
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

	public String getVersion() {
		return version;
	}

	public void setVersion(String version) {
		this.version = version;
	}

	
}
