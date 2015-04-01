package org.whale.inf.rpc;

import java.net.InetSocketAddress;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.Future;

/**
 * RPC 请求上下文
 * 
 * @author Administrator
 *
 */
public class RpcContext {

	private static final ThreadLocal<RpcContext> LOCAL = new ThreadLocal<RpcContext>() {
		@Override
		protected RpcContext initialValue() {
			return new RpcContext();
		}
	};

	public static RpcContext getContext() {
	    return LOCAL.get();
	}
	
	public static void removeContext() {
	    LOCAL.remove();
	}
	
	private Map<String, Object> conf;
	
	private Future<?> future;
	
	private String methodName;

    private Class<?>[] parameterTypes;

    private Object[] arguments;

	private InetSocketAddress localAddress;

	private InetSocketAddress remoteAddress;

    private final Map<String, String> attachments = new HashMap<String, String>();

    private final Map<String, Object> values = new HashMap<String, Object>();

    @SuppressWarnings("unchecked")
    public <T> Future<T> getFuture() {
        return (Future<T>) future;
    }

	public void setFuture(Future<?> future) {
		this.future = future;
	}

	public String getMethodName() {
		return methodName;
	}

	public void setMethodName(String methodName) {
		this.methodName = methodName;
	}

	public Class<?>[] getParameterTypes() {
		return parameterTypes;
	}

	public void setParameterTypes(Class<?>[] parameterTypes) {
		this.parameterTypes = parameterTypes;
	}

	public Object[] getArguments() {
		return arguments;
	}

	public void setArguments(Object[] arguments) {
		this.arguments = arguments;
	}

	public InetSocketAddress getLocalAddress() {
		return localAddress;
	}

	public void setLocalAddress(InetSocketAddress localAddress) {
		this.localAddress = localAddress;
	}

	public InetSocketAddress getRemoteAddress() {
		return remoteAddress;
	}

	public void setRemoteAddress(InetSocketAddress remoteAddress) {
		this.remoteAddress = remoteAddress;
	}

	public Map<String, String> getAttachments() {
		return attachments;
	}

	public Map<String, Object> getValues() {
		return values;
	}

	public Map<String, Object> getConf() {
		return conf;
	}

	public void setConf(Map<String, Object> conf) {
		this.conf = conf;
	}
    
}
