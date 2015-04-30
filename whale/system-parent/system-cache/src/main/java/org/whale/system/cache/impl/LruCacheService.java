package org.whale.system.cache.impl;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.Map.Entry;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.whale.system.cache.impl.jvm.CacheEntry;
import org.whale.system.common.constant.SysConstant;
import org.whale.system.common.util.PropertiesUtil;


public class LruCacheService<M extends Serializable> extends AbstractCacheService<M> {

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
	public void doPut(String cacheName, String key, M value, Integer seconds) {
		synchronized (cache) {
			this.cache.put(this.getKey(cacheName, key), new CacheEntry(value, seconds));
		}
	}

	@Override
	public void mdoPut(String cacheName, Map<String, M> keyValues, Integer seconds) {
		synchronized (cache) {
			for(Map.Entry<String, M> entry : keyValues.entrySet()){
				this.cache.put(this.getKey(cacheName, entry.getKey()), new CacheEntry(entry.getValue(), seconds));
			}
		}
	}


	@Override
	@SuppressWarnings("all")
	public M doGet(String cacheName, String key) {
		synchronized (cache) {
			CacheEntry cacheEntry = (CacheEntry)this.cache.get(this.getKey(cacheName, key));
			if(cacheEntry == null)
				return null;
			cacheEntry.updateLastActiveTime();
			return (M)cacheEntry.getValue();
		}
	}

	@Override
	@SuppressWarnings("all")
	public List<M> mdoGet(String cacheName, List<String> keys) {
		synchronized (cache) {
			List<M> rs = new ArrayList<M>(keys.size());
			CacheEntry cacheEntry = null;
			for(String key : keys){
				cacheEntry = (CacheEntry)this.cache.get(this.getKey(cacheName, key));
				if(cacheEntry == null){
					rs.add(null);
				}else{
					cacheEntry.updateLastActiveTime();
					rs.add((M)cacheEntry.getValue());
				}
			}
			return rs;
		}
	}

	@Override
	public void doDel(String cacheName, String key) {
		synchronized (cache) {
			this.cache.remove(this.getKey(cacheName, key));
		}
	}

	@Override
	public void mdoDel(String cacheName, List<String> keys) {
		synchronized (cache) {
			for(String key : keys){
				this.cache.remove(this.getKey(cacheName, key));
			}
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
								logger.debug("LRU CACHE: 清除过期缓存 key={} value = {}",key, cache.get(key));
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
	
}
