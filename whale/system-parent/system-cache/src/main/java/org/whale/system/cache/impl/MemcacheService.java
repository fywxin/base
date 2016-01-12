package org.whale.system.cache.impl;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;

import net.rubyeye.xmemcached.GetsResponse;
import net.rubyeye.xmemcached.MemcachedClient;

import org.springframework.beans.factory.annotation.Autowired;
import org.whale.system.common.exception.RemoteCacheException;
import org.whale.system.common.exception.SysException;

/**
 * memcached 缓存实现， 学习memcache NIO通信
 *
 * @author wjs
 * 2015年4月25日 下午10:28:54
 */
public class MemcacheService<M extends Serializable> extends AbstractCacheService<M> {
	
	@Autowired
	private MemcachedClient memcachedClient;
	
	@Override
	public void doPut(String cacheName, String key, M value, Integer seconds) {
		try {
			if(seconds == null || seconds < 1){
				this.memcachedClient.setWithNoReply(this.getKey(cacheName, key), -1, value);
			}else{
				this.memcachedClient.setWithNoReply(this.getKey(cacheName, key), seconds, value);
			}
		} catch (Exception e) {
			throw new RemoteCacheException("memcahced缓存出现异常！", e);
		}
	}

	@Override
	public void mdoPut(String cacheName, Map<String, M> keyValues, Integer seconds) {
		for(Map.Entry<String, M> entry : keyValues.entrySet()){
			this.doPut(cacheName, entry.getKey(), entry.getValue(), seconds);
		}
	}

	@Override
	public M doGet(String cacheName, String key) {
		try {
			return this.memcachedClient.get(this.getKey(cacheName, key));
		} catch (Exception e) {
			throw new RemoteCacheException("memcahced缓存出现异常！", e);
		}
	}

	@Override
	public List<M> mdoGet(String cacheName, List<String> keys) {
		try {
			Map<String, GetsResponse<M>> rs = this.memcachedClient.gets(keys);
			if(rs != null){
				List<M> list = new ArrayList<M>(rs.size());
				for(String key : keys){
					list.add(rs.get(key).getValue());
				}
				return list;
			}
			return null;
		} catch (Exception e) {
			throw new RemoteCacheException("memcahced缓存出现异常！", e);
		}
	}

	@Override
	public void doDel(String cacheName, String key) {
		try{
			this.memcachedClient.deleteWithNoReply(this.getKey(cacheName, key));
		} catch (Exception e) {
			throw new RemoteCacheException("memcahced缓存出现异常！", e);
		}
	}

	@Override
	public void mdoDel(String cacheName, List<String> keys) {
		for(String key : keys){
			this.doDel(cacheName, key);
		}
	}
	

	@Override
	public void clear(String cacheName) {
		throw new SysException("Memcacahed 不支持缓存批量清空功能！");
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
