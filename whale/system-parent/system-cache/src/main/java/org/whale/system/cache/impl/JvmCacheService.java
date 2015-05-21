package org.whale.system.cache.impl;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicLong;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.whale.system.cache.impl.jvm.CacheEntry;
import org.whale.system.common.util.PropertiesUtil;

public class JvmCacheService<M extends Serializable> extends AbstractCacheService<M> {
	
	private static final Logger logger = LoggerFactory.getLogger(JvmCacheService.class);
	
	private ConcurrentHashMap<String, CacheEntry> cache = new ConcurrentHashMap<String, CacheEntry>();
	
	private static final Long CACHE_SCAN_INTERVAL = PropertiesUtil.getValueInt("cache.jvm.exp.interval", 10) * 1000L;
	
	//cache 内保存的最大记录数
	private static final Long maxCacheNum = PropertiesUtil.getValueLong("cache.jvm.maxCacheNum", 393216L);
	
	//当前缓存记录数，由于 ConcurrentHashMap 的size方法会锁定整个表，所以退而取其次
	private AtomicLong num = new AtomicLong(0);
	
	//丢失的更新记录累积
	private AtomicInteger lostRec = new AtomicInteger(0);
	
	public JvmCacheService() {
		Thread thread = new JvmCacheExpScanThread();
		thread.setName("JVM Cache缓存过期扫描线程");
		thread.setDaemon(true);
		thread.start();
		logger.info("JVM CACHE: 启动过期缓存扫描线程...");
	}
	
	@Override
	public void doPut(String cacheName, String key, M value, Integer seconds) {
		if(num.getAndIncrement() > maxCacheNum){
			logger.warn("JVM CACHE: 当前缓存数量超过：{}， 丢弃 {} 条新记录" , lostRec.incrementAndGet());
			num.decrementAndGet();
		}else{
			this.cache.put(this.getKey(cacheName, key), new CacheEntry(value, seconds));
		}
	}

	@Override
	public void mdoPut(String cacheName, Map<String, M> keyValues, Integer seconds) {
		if(num.addAndGet(keyValues.size()) > maxCacheNum){
			logger.warn("JVM CACHE: 当前缓存数量超过：{}， 丢弃 {} 条新记录" , lostRec.addAndGet(keyValues.size()));
			num.addAndGet(-keyValues.size());
		}else{
			Map<String, CacheEntry> map = new HashMap<String, CacheEntry>(keyValues.size() * 2);
			for(Map.Entry<String, M> entry : keyValues.entrySet()){
				map.put(this.getKey(cacheName, entry.getKey()), new CacheEntry(entry.getValue(), seconds));
			}
			this.cache.putAll(map);
		}
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
		CacheEntry entry = this.cache.remove(this.getKey(cacheName, key));
		if(entry != null){
			num.decrementAndGet();
		}	
	}

	@Override
	public void mdoDel(String cacheName, List<String> keys) {
		CacheEntry entry = null;
		for(String key : keys){
			entry = this.cache.remove(this.getKey(cacheName, key));
			if(entry != null){
				num.decrementAndGet();
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
	
	@Override
	public void clear(String cacheName) {
		Set<String> keys = this.cache.keySet();
		for(String key : keys){
			if(key.startsWith(cacheName+"_")){
				this.cache.remove(key);
				num.decrementAndGet();
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
								logger.debug("JVM CACHE: 清除过期缓存 key={} value = {}",key, cache.get(key));
							}
							cache.remove(key);
						}
						
						long n = num.addAndGet(new Long(removeKeys.size()));
						logger.info("当前缓存记录数：{}", n);
						if(num.getAndIncrement() < maxCacheNum){
							lostRec.set(0);
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
