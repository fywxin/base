package org.whale.inf.client;

import java.lang.reflect.Method;
import java.lang.reflect.Proxy;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class ClientInfProxyFactory<T> {
	private final Class<T> mapperInterface;
	  private Map<Method, UriMethod> methodCache = new ConcurrentHashMap<Method, UriMethod>();

	  public ClientInfProxyFactory(Class<T> mapperInterface) {
	    this.mapperInterface = mapperInterface;
	  }

	  public Class<T> getMapperInterface() {
	    return mapperInterface;
	  }

	  public Map<Method, UriMethod> getMethodCache() {
	    return methodCache;
	  }

	  @SuppressWarnings("unchecked")
	  protected T newInstance(ClientInfProxy<T> mapperProxy) {
	    return (T) Proxy.newProxyInstance(mapperInterface.getClassLoader(), new Class[] { mapperInterface }, mapperProxy);
	  }

	  public T newInstance() {
	    final ClientInfProxy<T> mapperProxy = new ClientInfProxy<T>(mapperInterface, methodCache);
	    return newInstance(mapperProxy);
	  }
}
