package org.whale.system.cache.impl;

import java.util.Set;

import net.rubyeye.xmemcached.MemcachedClient;

import org.springframework.beans.factory.annotation.Autowired;
import org.whale.system.cache.ICacheService;
import org.whale.system.common.exception.RemoteCacheException;
import org.whale.system.common.exception.SysException;

public class MemcacheService implements ICacheService {
	
	@Autowired
	private MemcachedClient memcachedClient;
	
	@Override
	public void put(String cacheName, String key, Object value) {
		this.put(cacheName, key, value, -1);
	}
	
	@Override
	public void put(String cacheName, String key, Object value, Integer expTime) {
		try {
			if(key == null)
				return ;
			this.memcachedClient.setWithNoReply(this.getKey(cacheName, key), expTime, value);
		} catch (Exception e) {
			throw new RemoteCacheException("memcahced缓存出现异常！", e);
		}
	}

	@Override
	public Object get(String cacheName, String key) {
		if(key == null)
			return null;
		Object value = null;
		try {
			value = this.memcachedClient.get(this.getKey(cacheName, key));
		} catch (Exception e) {
			throw new RemoteCacheException("memcahced缓存出现异常！", e);
		}
		return value;
	}

	@Override
	public void evict(String cacheName, String key) {
		try {
			if(key == null)
				return ;
			this.memcachedClient.deleteWithNoReply(this.getKey(cacheName, key));
		} catch (Exception e) {
			throw new RemoteCacheException("memcahced缓存出现异常！", e);
		}
	}

	@Override
	public void clear(String cacheName) {
		throw new SysException("Memcacahed 不支持缓存批量清空功能！");
	}

	private String getKey(String cacheName, Object key){
		StringBuilder strb = new StringBuilder();
		return strb.append(cacheName).append("_").append(key.toString()).toString();
	}

	@Override
	public Object getNativeCache() {
		return memcachedClient;
	}

	@Override
	public Set<String> getKeys(String cacheName) {
		throw new SysException("Memcacahed 不支持缓存批量获取key集合功能！");
	}

	
}
