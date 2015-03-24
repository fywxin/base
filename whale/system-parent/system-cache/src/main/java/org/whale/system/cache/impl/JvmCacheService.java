package org.whale.system.cache.impl;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.whale.system.cache.ICacheService;
import org.whale.system.cache.impl.jvm.CacheEntry;
import org.whale.system.common.util.PropertiesUtil;

public class JvmCacheService implements ICacheService {
	
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
	public void put(String cacheName, String key, Object value) {
		this.put(cacheName, key, value, null);
	}

	@Override
	public void put(String cacheName, String key, Object value, Integer expTime) {
		logger.info("key:"+this.getKey(cacheName, key) + "  value : "+value);
		this.cache.put(this.getKey(cacheName, key), new CacheEntry(value, expTime));
	}

	@Override
	public Object get(String cacheName, String key) {
		logger.info("取值 key:"+this.getKey(cacheName, key));
		CacheEntry cacheEntry = this.cache.get(this.getKey(cacheName, key));
		if(cacheEntry == null)
			return null;
		cacheEntry.updateLastActiveTime();
		return cacheEntry.getValue();
	}

	@Override
	public void evict(String cacheName, String key) {
		this.cache.remove(this.getKey(cacheName, key));
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

	private String getKey(String cacheName, Object key){
		StringBuilder strb = new StringBuilder();
		return strb.append(cacheName).append("_").append(key.toString()).toString();
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
	
}
