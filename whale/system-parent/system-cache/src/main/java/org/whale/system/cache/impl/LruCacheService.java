package org.whale.system.cache.impl;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.Map.Entry;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.whale.system.cache.ICacheService;
import org.whale.system.cache.impl.jvm.CacheEntry;
import org.whale.system.common.constant.SysConstant;
import org.whale.system.common.util.PropertiesUtil;


public class LruCacheService implements ICacheService {

	private static final Logger logger = LoggerFactory .getLogger(LruCacheService.class);

	private final Map<String, CacheEntry> cache;
	
	private static final Long CACHE_SCAN_INTERVAL = PropertiesUtil.getValueInt("cache.jvm.exp.interval", 10) * 1000L;

	public LruCacheService() {
		this.cache = new LinkedHashMap<String, CacheEntry>() {
			private static final long serialVersionUID = -3834209229668463829L;

			@Override
			protected boolean removeEldestEntry(Entry<String, CacheEntry> eldest) {
				return size() > PropertiesUtil.getValueInt("cache.lru.size", SysConstant.MAX_LRU_CACHE_SIZE);
			}

		};
		
		Thread thread = new LruCacheExpScanThread();
		thread.setName("LRU Cache缓存过期扫描线程");
		thread.setDaemon(true);
		thread.start();
		logger.info("LRU CACHE: 启动过期缓存扫描线程...");
	}
	
	@Override
	public void put(String cacheName, String key, Object value) {
		this.put(cacheName, key, value, null);
	}

	@Override
	public void put(String cacheName, String key, Object value, Integer expTime) {
		logger.info("key:" + this.getKey(cacheName, key) + "  value : " + value);
		synchronized (cache) {
			this.cache.put(this.getKey(cacheName, key), new CacheEntry(value, expTime));
		}
	}

	@Override
	public Object get(String cacheName, String key) {
		logger.info("取值 key:" + this.getKey(cacheName, key));
		synchronized (cache) {
			CacheEntry cacheEntry = (CacheEntry)this.cache.get(this.getKey(cacheName, key));
			if(cacheEntry == null)
				return null;
			cacheEntry.updateLastActiveTime();
			return cacheEntry.getValue();
		}
	}

	@Override
	public void evict(String cacheName, String key) {
		synchronized (cache) {
			this.cache.remove(this.getKey(cacheName, key));
		}
	}

	@Override
	public void clear(String cacheName) {
		synchronized (cache) {
			Set<String> keys = this.cache.keySet();
			for (String key : keys) {
				if (key.startsWith(cacheName + "_")) {
					this.cache.remove(key);
				}
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
	
	class LruCacheExpScanThread extends Thread {
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
								logger.debug("LRU CACHE: 清除过期缓存 key="+key+" value = "+cache.get(key));
							}
							cache.remove(key);
						}
						removeKeys.clear();
					}
					
					Thread.sleep(CACHE_SCAN_INTERVAL);
				} catch (Exception e) {
					logger.error("LRU CACHE: 清除过期缓存出现异常：", e);
				}
			}
			
		}
	}

	@Override
	public Set<String> getKeys(String cacheName) {
		Set<String> ks = new HashSet<String>();
		synchronized (cache) {
			Set<String> keys = this.cache.keySet();
			for (String key : keys) {
				if (key.startsWith(cacheName + "_")) {
					ks.add(key);
				}
			}
		}
		return ks;
	}
}
