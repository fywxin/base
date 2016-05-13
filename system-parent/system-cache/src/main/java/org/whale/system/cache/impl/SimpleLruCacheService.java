package org.whale.system.cache.impl;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.whale.system.common.constant.SysConstant;
import org.whale.system.common.util.PropertiesUtil;

import java.io.Serializable;
import java.util.*;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantReadWriteLock;

/**
 * Created by 王金绍 on 2016/5/13.
 */
public class SimpleLruCacheService<M extends Serializable> extends AbstractCacheService<M> {

    private static final Logger logger = LoggerFactory.getLogger(SimpleLruCacheService.class);

    private final Map<String, M> cache;

    private final ReentrantReadWriteLock rwl;

    private final Lock r;

    private final Lock w;


    public SimpleLruCacheService() {
        rwl = new ReentrantReadWriteLock();
        r = rwl.readLock();
        w = rwl.writeLock();

        this.cache = new LinkedHashMap<String, M>() {
            private static final long serialVersionUID = -3834209229668463829L;

            @Override
            protected boolean removeEldestEntry(Entry<String, M> eldest) {
                return size() > PropertiesUtil.getValueInt("cache.lru.size", SysConstant.MAX_LRU_CACHE_SIZE);
            }
        };
    }

    @Override
    public void doPut(String cacheName, String key, M value, Integer seconds) {
        String k =  this.getKey(cacheName, key);
        w.lock();
        try{
            this.cache.put(k, value);
        }finally {
            w.unlock();
        }
    }

    @Override
    public void mdoPut(String cacheName, Map<String, M> keyValues, Integer seconds) {
        w.lock();
        try{
            for(Map.Entry<String, M> entry : keyValues.entrySet()){
                this.cache.put(this.getKey(cacheName, entry.getKey()), entry.getValue());
            }
        }finally {
            w.unlock();
        }
    }


    @Override
    public M doGet(String cacheName, String key) {
        String k =  this.getKey(cacheName, key);
        r.lock();
        try {
            return this.cache.get(k);
        }finally {
            r.unlock();
        }
    }

    @Override
    public List<M> mdoGet(String cacheName, List<String> keys) {
        List<M> rs = new ArrayList<M>(keys.size());
        r.lock();
        try {
            for(String key : keys){
                rs.add(this.cache.get(this.getKey(cacheName, key)));
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
}
