package org.whale.system.client.mybatis;

import java.util.HashMap;
import java.util.Map;

public class ClientInfRegistry {
	private final Map<Class<?>, ClientInfProxyFactory<?>> knownMappers = new HashMap<Class<?>, ClientInfProxyFactory<?>>();

	public <T> T getMapper(Class<T> type) {
		final ClientInfProxyFactory<T> mapperProxyFactory = (ClientInfProxyFactory<T>) knownMappers.get(type);
	    if (mapperProxyFactory == null)
	      throw new RuntimeException("Type " + type + " is not known to the MapperRegistry.");
	    try {
	      return mapperProxyFactory.newInstance();
	    } catch (Exception e) {
	      throw new RuntimeException("Error getting mapper instance. Cause: " + e, e);
	    }
	}
	
	public <T> boolean hasMapper(Class<T> type) {
		return knownMappers.containsKey(type);
	}
	
	public <T> void addMapper(Class<T> type) {
		if (type.isInterface()) {
		      if (hasMapper(type)) {
		        throw new RuntimeException("Type " + type + " is already known to the MapperRegistry.");
		      }
		      boolean loadCompleted = false;
		      try {
		        knownMappers.put(type, new ClientInfProxyFactory<T>(type));
		        // It's important that the type is added before the parser is run
		        // otherwise the binding may automatically be attempted by the
		        // mapper parser. If the type is already known, it won't try.
//		        MapperAnnotationBuilder parser = new MapperAnnotationBuilder(config, type);
//		        parser.parse();
		        loadCompleted = true;
		      } finally {
		        if (!loadCompleted) {
		          knownMappers.remove(type);
		        }
		      }
	}
	}
}
