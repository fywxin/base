package org.whale.system.cache.impl;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.whale.system.cache.impl.jvm.CacheEntry;
import org.whale.system.common.constant.SysConstant;
import org.whale.system.common.util.PropertiesUtil;

import java.io.Serializable;
import java.util.*;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantReadWriteLock;


/**
 * http://www.yufengof.com/2015/11/18/lru-and-linkedhashmap-source-code/?hmsr=toutiao.io&utm_medium=toutiao.io&utm_source=toutiao.io
 * 
 * @author wjs
 * @date 2015年11月30日 上午9:47:28
 */
public class LruCacheService<M extends Serializable> extends AbstractCacheService<M> {

	private static final Logger logger = LoggerFactory .getLogger(LruCacheService.class);

	private final Map<String, CacheEntry> cache;
	
	private static final Long CACHE_SCAN_INTERVAL = PropertiesUtil.getValueInt("cache.jvm.exp.interval", 10) * 1000L;

	private final ReentrantReadWriteLock rwl;

	private final Lock r;

	private final Lock w;


	public LruCacheService() {
		rwl = new ReentrantReadWriteLock();
		r = rwl.readLock();
		w = rwl.writeLock();

		this.cache = new LinkedHashMap<String, CacheEntry>() {
			private static final long serialVersionUID = -3834209229668463829L;

			//一定要是 Map.Entry<String, CacheEntry>, Entry<String, CacheEntry>则编译不通过
			@Override
			protected boolean removeEldestEntry(Map.Entry<String, CacheEntry> eldest) {
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
		String k =  this.getKey(cacheName, key);
		CacheEntry cacheEntry = new CacheEntry(value, seconds);
		w.lock();
		try{
			this.cache.put(k, cacheEntry);
		}finally {
			w.unlock();
		}
	}

	@Override
	public void mdoPut(String cacheName, Map<String, M> keyValues, Integer seconds) {
		w.lock();
		try{
			for(Map.Entry<String, M> entry : keyValues.entrySet()){
				this.cache.put(this.getKey(cacheName, entry.getKey()), new CacheEntry(entry.getValue(), seconds));
			}
		}finally {
			w.unlock();
		}
	}


	@Override
	@SuppressWarnings("all")
	public M doGet(String cacheName, String key) {
		String k =  this.getKey(cacheName, key);
		r.lock();
		try {
			CacheEntry cacheEntry = (CacheEntry)this.cache.get(k);
			if(cacheEntry == null)
				return null;
			cacheEntry.updateLastActiveTime();
			return (M)cacheEntry.getValue();
		}finally {
			r.unlock();
		}
	}

	@Override
	@SuppressWarnings("all")
	public List<M> mdoGet(String cacheName, List<String> keys) {
		List<M> rs = new ArrayList<M>(keys.size());
		r.lock();
		try {
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
		}finally {
			r.unlock();
		}
	}

	@Override
	public void doDel(String cacheName, String key) {
		String k =  this.getKey(cacheName, key);
		w.lock();
		try {
			this.cache.remove(k);
		}finally {
			w.unlock();
		}
	}

	@Override
	public void mdoDel(String cacheName, List<String> keys) {
		w.lock();
		try {
			for(String key : keys){
				this.cache.remove(this.getKey(cacheName, key));
			}
		}finally {
			w.unlock();
		}
	}
	
	@Override
	public void clear(String cacheName) {
		w.lock();
		try {
			Set<String> keys = this.cache.keySet();
			for (String key : keys) {
				if (key.startsWith(cacheName + "_")) {
					this.cache.remove(key);
				}
			}
		}finally {
			w.unlock();
		}
	}

	@Override
	public Set<String> getKeys(String cacheName) {
		Set<String> ks = new HashSet<String>();
		r.lock();
		try {
			Set<String> keys = this.cache.keySet();
			for (String key : keys) {
				if (key.startsWith(cacheName + "_")) {
					ks.add(key);
				}
			}
			return ks;
		}finally {
			r.unlock();
		}
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
