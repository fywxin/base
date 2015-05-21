package org.whale.system.cache.impl;

import java.io.Serializable;
import java.util.ArrayList;
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
	
	//当前缓存记录数，由于 ConcurrentHashMap 的size方法会锁定整个表，所以退而取其次, 高并发情况下可能存在误差，有必要矫正
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
		if(num.get() > maxCacheNum){
			logger.warn("JVM CACHE: 当前缓存数量超过：{}， 丢弃 {} 条新记录" , lostRec.incrementAndGet());
		}else{
			CacheEntry val = this.cache.put(this.getKey(cacheName, key), new CacheEntry(value, seconds));
			if(val == null){
				num.incrementAndGet();
			}
		}
	}

	@Override
	public void mdoPut(String cacheName, Map<String, M> keyValues, Integer seconds) {
		if(num.get() > maxCacheNum){
			logger.warn("JVM CACHE: 当前缓存数量超过：{}， 丢弃 {} 条新记录" , lostRec.addAndGet(keyValues.size()));
		}else{
			CacheEntry val = null;
			for(Map.Entry<String, M> entry : keyValues.entrySet()){
				val = this.cache.put(this.getKey(cacheName, entry.getKey()), new CacheEntry(entry.getValue(), seconds));
				if(val == null){
					num.incrementAndGet();
				}
			}
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
		CacheEntry val = null;
		for(String key : keys){
			if(key.startsWith(cacheName+"_")){
				val = this.cache.remove(key);
				if(val != null){
					num.decrementAndGet();
				}
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
			
			while(true){
				try {
					CacheEntry val = null;
					for(Map.Entry<String, CacheEntry> entry : cache.entrySet()){
						if(entry.getValue().isOutOfDate()){
							val = cache.remove(entry.getKey());
							if(val != null){
								num.decrementAndGet();
							}
							logger.debug("JVM CACHE: 清除过期缓存 key={} value = {}",entry.getKey(), val);
						}
					}
					
					if(num.get() < maxCacheNum){
						lostRec.set(0);
					}
					
					Thread.sleep(CACHE_SCAN_INTERVAL);
				} catch (Exception e) {
					logger.error("JVM CACHE: 清除过期缓存出现异常：", e);
				}
			}
			
		}
	}

	

	
	
}
