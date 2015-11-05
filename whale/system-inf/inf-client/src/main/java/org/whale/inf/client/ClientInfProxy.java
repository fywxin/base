package org.whale.inf.client;

import java.io.Serializable;
import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.util.Map;

public class ClientInfProxy<T> implements InvocationHandler, Serializable {
	
	private final Class<T> mapperInterface;
	 private final Map<Method, UriMethod> methodCache;

	public ClientInfProxy(Class<T> mapperInterface, Map<Method, UriMethod> methodCache){
		this.mapperInterface = mapperInterface;
		this.methodCache = methodCache;
	}
	
	@Override
	public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
		if (Object.class.equals(method.getDeclaringClass())) {
		      return method.invoke(this, args);
		    }
		    final UriMethod uriMethod = cachedMapperMethod(method);
		    return uriMethod.execute(args);
	}
	
	private UriMethod cachedMapperMethod(Method method) {
		UriMethod mapperMethod = methodCache.get(method);
	    if (mapperMethod == null) {
	      mapperMethod = new UriMethod(mapperInterface, method);
	      methodCache.put(method, mapperMethod);
	    }
	    return mapperMethod;
	  }

}
