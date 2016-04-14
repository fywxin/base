package org.whale.system.cache.impl;

import java.io.Serializable;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * 缓存最新最热的数据
 * 
 * @author wjs
 * @date 2015年5月22日 下午1:09:16
 */
public class HostJvmCacheService<M extends Serializable> extends AbstractCacheService<M> {

	@Override
	public void clear(String cacheName) {
		
		
	}

	@Override
	public Object getNativeCache() {
		
		return null;
	}

	@Override
	public Set<String> getKeys(String cacheName) {
		
		return null;
	}

	@Override
	public void doPut(String cacheName, String key, M value, Integer seconds) {
		
		
	}

	@Override
	public void mdoPut(String cacheName, Map<String, M> keyValues,
			Integer seconds) {
		
		
	}

	@Override
	public M doGet(String cacheName, String key) {
		
		return null;
	}

	@Override
	public List<M> mdoGet(String cacheName, List<String> keys) {
		
		return null;
	}

	@Override
	public void doDel(String cacheName, String key) {
		
		
	}

	@Override
	public void mdoDel(String cacheName, List<String> keys) {
		
		
	}

}
