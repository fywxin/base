package org.whale.system.cache.impl;

import org.whale.system.cache.impl.jvm.CacheEntry;

import java.io.Serializable;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicLong;

/**
 * 永远不过期
 *
 * Created by 王金绍 on 2016/5/4.
 */
public class SimpleJvmCacheService<M extends Serializable> extends AbstractCacheService<M> {

    private final ConcurrentHashMap<String, M> cache = new ConcurrentHashMap<String, M>();

    //当前缓存记录数，由于 ConcurrentHashMap 的size方法会锁定整个表，所以退而取其次, 高并发情况下可能存在误差，有必要矫正
    public final AtomicLong num = new AtomicLong(0);

    @Override
    public void doPut(String cacheName, String key, M value, Integer seconds) {
        if (this.cache.put(this.getKey(cacheName, key), value) == null){
            num.incrementAndGet();
        }
    }

    @Override
    public void mdoPut(String cacheName, Map<String, M> keyValues, Integer seconds) {
        for(Map.Entry<String, M> entry : keyValues.entrySet()){
            if(this.cache.put(this.getKey(cacheName, entry.getKey()), entry.getValue()) == null){
                num.incrementAndGet();
            }
        }
    }

    @Override
    public M doGet(String cacheName, String key) {
        return this.cache.get(this.getKey(cacheName, key));
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
        if (this.cache.remove(this.getKey(cacheName, key)) != null){
            num.decrementAndGet();
        }
    }

    @Override
    public void mdoDel(String cacheName, List<String> keys) {
        for(String key : keys){
            if(this.cache.remove(this.getKey(cacheName, key)) != null){
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
        Object val = null;
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
}
