package org.whale.system.cache.impl;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.whale.system.cache.impl.jvm.CacheEntry;
import org.whale.system.common.util.PropertiesUtil;

public class JvmCacheService<M extends Serializable> extends AbstractCacheService<M> {
	
	private static final Logger logger = LoggerFactory.getLogger(JvmCacheService.class);
	
	private ConcurrentHashMap<String, CacheEntry> cache = new ConcurrentHashMap<String, CacheEntry>();
	
	private static final Long CACHE_SCAN_INTERVAL = PropertiesUtil.getValueInt("cache.jvm.exp.interval", 10) * 1000L;
	
	public JvmCacheService() {
		Thread thread = new JvmCacheExpScanThread();
		thread.setName("JVM Cache缓存过期扫描线程");
		thread.setDaemon(true);
		thread.start();
		logger.info("JVM CACHE: 启动过期缓存扫描线程...");
	}
	
	@Override
	public void doPut(String cacheName, String key, M value, Integer seconds) {
		this.cache.put(this.getKey(cacheName, key), new CacheEntry(value, seconds));
	}

	@Override
	public void mdoPut(String cacheName, Map<String, M> keyValues, Integer seconds) {
		Map<String, CacheEntry> map = new HashMap<String, CacheEntry>(keyValues.size() * 2);
		for(Map.Entry<String, M> entry : keyValues.entrySet()){
			map.put(this.getKey(cacheName, entry.getKey()), new CacheEntry(entry.getValue(), seconds));
		}
		this.cache.putAll(map);
	}

	@Override
	@SuppressWarnings("all")
	public M doGet(String cacheName, String key) {
		CacheEntry cacheEntry = this.cache.get(this.getKey(cacheName, key));
		if(cacheEntry == null)
			return null;
		cacheEntry.updateLastActiveTime();
		return (M)cacheEntry.getValue();
	}

	@Override
	public List<M> mdoGet(String cacheName, List<String> keys) {
		List<M> list = new ArrayList<M>(keys.size());
		for(String key : keys){
			list.add(doGet(cacheName, key));
		}
		return list;
	}

	@Override
	public void doDel(String cacheName, String key) {
		this.cache.remove(this.getKey(cacheName, key));
	}

	@Override
	public void mdoDel(String cacheName, List<String> keys) {
		for(String key : keys){
			this.doDel(cacheName, key);
		}
	}
	
	@Override
	public Set<String> getKeys(String cacheName) {
		Set<String> ks = this.cache.keySet();
		Set<String> keys = this.cache.keySet();
		for(String key : keys){
			if(key.startsWith(cacheName+"_")){
				ks.add(key);
			}
		}
		return ks;
	}
	
	@Override
	public void clear(String cacheName) {
		Set<String> keys = this.cache.keySet();
		for(String key : keys){
			if(key.startsWith(cacheName+"_")){
				this.cache.remove(key);
			}
		}
	}

	@Override
	public Object getNativeCache() {
		return cache;
	}
	
	class JvmCacheExpScanThread extends Thread {
		@Override
		public void run() {
			
			List<String> removeKeys = new ArrayList<String>();
			while(true){
				try {
					for(Map.Entry<String, CacheEntry> entry : cache.entrySet()){
						if(entry.getValue().isOutOfDate()){
							removeKeys.add(entry.getKey());
						}
					}
					if(removeKeys.size() > 0){
						for(String key : removeKeys){
							if(logger.isDebugEnabled()){
								logger.debug("JVM CACHE: 清除过期缓存 key="+key+" value = "+cache.get(key));
							}
							cache.remove(key);
						}
						removeKeys.clear();
					}
					
					Thread.sleep(CACHE_SCAN_INTERVAL);
				} catch (Exception e) {
					logger.error("JVM CACHE: 清除过期缓存出现异常：", e);
				}
			}
			
		}
	}

	

	
	
}
