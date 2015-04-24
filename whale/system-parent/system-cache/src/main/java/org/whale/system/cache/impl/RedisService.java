package org.whale.system.cache.impl;

import java.io.IOException;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.whale.system.cache.code.Code;
import org.whale.system.cache.impl.redis.JedisTemplate;
import org.whale.system.common.exception.CacheException;
import org.whale.system.common.exception.RemoteCacheException;
import org.whale.system.common.exception.SysException;

public class RedisService<M extends Serializable> extends AbstractCacheService<M> {
	
	@Autowired
	private JedisTemplate JedisTemplate;
	@Autowired
	private Code<M> code;
	
	@Override
	public void doPut(String cacheName, String key, M value, Integer seconds) {
		byte[] keyByte = null;
		byte[] valByte = null;
		try {
			keyByte = this.getByteKey(cacheName, key);
			valByte = code.encode(cacheName, value);
		} catch (IOException e) {
			throw new CacheException("Redis缓存编码异常！", e);
		}
		
		try {
			if(seconds == null || seconds < 1){
				this.JedisTemplate.set(keyByte, valByte);
			}else{
				this.JedisTemplate.setex(keyByte, valByte, seconds);
			}
		} catch (Exception e) {
			throw new RemoteCacheException("Redis缓存出现异常！", e);
		}
	}
	@Override
	public void mdoPut(String cacheName, Map<String, M> keyValues, Integer seconds) {
		List<byte[]> keyBytes = new ArrayList<byte[]>(keyValues.size());
		List<byte[]> valBytes = new ArrayList<byte[]>(keyValues.size());
		try {
			for(Map.Entry<String, M> entry : keyValues.entrySet()){
				keyBytes.add(this.getByteKey(cacheName, entry.getKey()));
				valBytes.add(this.code.encode(cacheName, entry.getValue()));
			}
		} catch (IOException e) {
			throw new CacheException("Redis缓存编码异常！", e);
		}
		try {
			if(seconds == null || seconds < 1){
				this.JedisTemplate.msetByte(keyBytes, valBytes);
			}else{
				this.JedisTemplate.msetexByte(keyBytes, valBytes, seconds);
			}
		} catch (Exception e) {
			throw new RemoteCacheException("Redis缓存出现异常！", e);
		}
	}
	@Override
	public M doGet(String cacheName, String key) {
		byte[] bytes = null;
		try {
			bytes = this.JedisTemplate.get(this.getByteKey(cacheName, key));
		} catch (Exception e) {
			throw new RemoteCacheException("Redis缓存出现异常！", e);
		}
		if(bytes != null){
			try {
				M m = this.code.decode(cacheName, bytes);
				return m;
			} catch (Exception e) {
				throw new CacheException("Redis缓存解码异常！", e);
			}
		}
		return null;
	}
	@Override
	public List<M> mdoGet(String cacheName, List<String> keys) {
		List<byte[]> keyBytes = new ArrayList<byte[]>(keys.size());
		try {
			for(String key : keys){
				keyBytes.add(this.getByteKey(cacheName, key));
			}
		} catch (Exception e) {
			throw new CacheException("Redis缓存编码异常！", e);
		}
		
		
		List<byte[]> rsBytes = null;
		if(keyBytes.size() > 0){
			try {
				rsBytes = this.JedisTemplate.mgetByte(keyBytes);
			} catch (Exception e) {
				throw new RemoteCacheException("Redis缓存出现异常！", e);
			}
			if(rsBytes != null && rsBytes.size() > 0){
				List<M> rs = new ArrayList<M>(rsBytes.size());
				try {
					for(byte[] rsByte : rsBytes){
						rs.add(this.code.decode(cacheName, rsByte));
					}
					return rs;
				} catch (Exception e) {
					throw new CacheException("Redis缓存解码异常！", e);
				}
			}
		}
		
		return null;
	}
	@Override
	public void doDel(String cacheName, String key) {
		try {
			this.JedisTemplate.del(this.getByteKey(cacheName, key));
		} catch (Exception e) {
			throw new RemoteCacheException("Redis缓存出现异常！", e);
		}
	}
	@Override
	public void mdoDel(String cacheName, List<String> keys) {
		List<byte[]> keyBytes = new ArrayList<byte[]>(keys.size());
		try {
			for(String key : keys){
				keyBytes.add(this.getByteKey(cacheName, key));
			}
		} catch (Exception e) {
			throw new CacheException("Redis缓存编码异常！", e);
		}
		if(keyBytes.size() > 0){
			try {
				this.JedisTemplate.del(keyBytes);
			} catch (Exception e) {
				throw new RemoteCacheException("Redis缓存出现异常！", e);
			}
		}
	}
	
	@Override
	public void clear(String cacheName) {
		throw new SysException("Redis 不支持缓存批量清空功能！");
	}
	@Override
	public Object getNativeCache() {
		return this.JedisTemplate;
	}
	@Override
	public Set<String> getKeys(String cacheName) {
		return JedisTemplate.keys(cacheName+"*");
	}
}
