package org.whale.system.cache.impl;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.whale.system.cache.ICacheService;

/**
 * 组合缓存，二级缓存
 * levelFirstCache jvm级别
 * levelSecondCache redis 或 encache 或 memcached
 * 
 * 一级缓存只保存最热的数据
 * 二级缓存保存数据量更大
 * 
 * @author wjs
 * @date 2015年5月22日 下午12:53:37
 */
public class CombineCacheService<M extends Serializable> extends AbstractCacheService<M> {
	
	//一级缓存
	private ICacheService<M> levelFirstCache;
	//二级缓存
	private ICacheService<M> levelSecondCache;

	@Override
	public void clear(String cacheName) {
		levelFirstCache.clear(cacheName);
		levelSecondCache.clear(cacheName);
	}

	@Override
	public Object getNativeCache() {
		return levelSecondCache;
	}
	
	public Object getNativeCacheFirst() {
		return levelFirstCache;
	}

	public Object getNativeCacheSecond() {
		return levelSecondCache;
	}
	
	@Override
	public Set<String> getKeys(String cacheName) {
		
		return levelSecondCache.getKeys(cacheName);
	}

	@Override
	public void doPut(String cacheName, String key, M value, Integer seconds) {
		levelFirstCache.put(cacheName, key, value, seconds);
		levelSecondCache.put(cacheName, key, value, seconds);
	}

	@Override
	public void mdoPut(String cacheName, Map<String, M> keyValues, Integer seconds) {
		levelFirstCache.mput(cacheName, keyValues, seconds);
		levelSecondCache.mput(cacheName, keyValues, seconds);
	}

	@Override
	public M doGet(String cacheName, String key) {
		M m = levelFirstCache.get(cacheName, key);
		if(m == null){
			m =levelSecondCache.get(cacheName, key);
			if(m != null){
				levelFirstCache.put(cacheName, key, m);
			}
		}
		return m;
	}

	@Override
	public List<M> mdoGet(String cacheName, List<String> keys) {
		List<M> rs = new ArrayList<M>(keys.size());
		M m = null;
		for(String key : keys){
			m = levelFirstCache.get(cacheName, key);
			if(m == null){
				m =levelSecondCache.get(cacheName, key);
				if(m != null){
					levelFirstCache.put(cacheName, key, m);
				}
			}
			rs.add(m);
		}
		
		return rs;
	}

	@Override
	public void doDel(String cacheName, String key) {
		levelFirstCache.del(cacheName, key);
		levelSecondCache.del(cacheName, key);
	}

	@Override
	public void mdoDel(String cacheName, List<String> keys) {
		levelFirstCache.mdel(cacheName, keys);
		levelSecondCache.mdel(cacheName, keys);
	}

	public void setLevelFirstCache(ICacheService<M> levelFirstCache) {
		this.levelFirstCache = levelFirstCache;
	}

	public void setLevelSecondCache(ICacheService<M> levelSecondCache) {
		this.levelSecondCache = levelSecondCache;
	}
	
}
