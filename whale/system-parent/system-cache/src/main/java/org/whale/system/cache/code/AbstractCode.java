package org.whale.system.cache.code;

import java.io.IOException;
import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

public abstract class AbstractCode <M extends Serializable> implements Code<M>{

	protected Map<String, Class<?>> map = new HashMap<String, Class<?>>();
	
	@Override
	public byte[] encode(String cacheName, M m) throws IOException{
		if(!map.containsKey(cacheName)){
			if(m instanceof String){
				map.put(cacheName, String.class);
			} else if(m instanceof Integer){
				map.put(cacheName, Integer.class);
			} else if(m instanceof Long){
				map.put(cacheName, Long.class);
			} else if(m instanceof Boolean){
				map.put(cacheName, Boolean.class);
			} else if(m instanceof Float){
				map.put(cacheName, Float.class);
			} else if(m instanceof Double){
				map.put(cacheName, Double.class);
			} else if(m instanceof Short){
				map.put(cacheName, Short.class);
			} else if(m instanceof Byte){
				map.put(cacheName, Byte.class);
			}else{
				map.put(cacheName, m.getClass());
			}
		}
		
		return this.doEncode(cacheName, m);
	}
	
	public abstract byte[] doEncode(String cacheName, M m) throws IOException;

	@Override
	public M decode(String cacheName, byte[] bytes) throws IOException {
		
		return doDecode(this.map.get(cacheName), bytes);
	}
	
	public abstract M doDecode(Class<?> type, byte[] bytes) throws IOException;
	
	
	public boolean isBaseType(M m){
		return 	m instanceof String 
				|| m instanceof Integer 
				|| m instanceof Long 
				|| m instanceof Boolean
				|| m instanceof Float
				|| m instanceof Double
				|| m instanceof Short
				|| m instanceof Byte;
	}

	public Map<String, Class<?>> getMap() {
		return map;
	}
}
