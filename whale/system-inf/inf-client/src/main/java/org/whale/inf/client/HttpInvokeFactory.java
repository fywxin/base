package org.whale.inf.client;

import java.util.Arrays;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.aop.framework.ProxyFactory;
import org.springframework.beans.factory.FactoryBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.aopalliance.intercept.MethodInterceptor;
import org.aopalliance.intercept.MethodInvocation;
import org.springframework.remoting.support.UrlBasedRemoteAccessor;
import org.whale.inf.common.EncryptService;
import org.whale.inf.common.SignService;

/**
 * 客户端Http代理接口处理器
 * 
 * @author 王金绍
 * 2015年11月8日 上午12:20:23
 */
public class HttpInvokeFactory extends UrlBasedRemoteAccessor implements MethodInterceptor, FactoryBean<Object> {
	
	private static final Logger logger = LoggerFactory.getLogger(HttpInvokeFactory.class);

	private Object proxyObject = null;
	
	private Integer connectTimeout = 10000;
	
	private Integer readTimeout = 30000;
	
	@Autowired(required=false)
	private ClientInvokeHandler clientInvokeHandler;
	
	@Autowired
	private ClientCodec clientCodec;
	
	@Autowired(required=false)
	private EncryptService encryptService;
	
	@Autowired(required=false)
	private SignService signService;
	
	@Autowired
	private ClientConf clientConf;
	
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
		ClientContext context = new ClientContext();
		
		context.setArgs(Arrays.asList(invocation.getArguments()));
		context.setMethod(invocation.getMethod());
		
		context.setServiceUrl(this.getServiceUrl());
		context.setProxyObject(this.getObject());
		context.setConnectTimeout(this.connectTimeout);
		context.setReadTimeout(this.readTimeout);
		
		if(logger.isDebugEnabled()){
			logger.debug("客户端beforeCall前:{}", context);
		}
		
		CallTask task = new CallTask(context, clientCodec, encryptService, signService, clientInvokeHandler, clientConf);
		if(context.getIsAsyc()){
			new Thread(task).start();//TODO 并发很高时，可以采用线程池， 线程池可以适当大一些
		}else{
			task.run();
		}
		
		if(logger.isDebugEnabled()){
			logger.debug("接口调用完成，返回:{}", context.getRs());
		}
		return context.getRs();
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

	public Integer getReadTimeout() {
		return readTimeout;
	}

	public void setReadTimeout(Integer readTimeout) {
		this.readTimeout = readTimeout;
	}

	public Integer getConnectTimeout() {
		return connectTimeout;
	}

	public void setConnectTimeout(Integer connectTimeout) {
		this.connectTimeout = connectTimeout;
	}
}
