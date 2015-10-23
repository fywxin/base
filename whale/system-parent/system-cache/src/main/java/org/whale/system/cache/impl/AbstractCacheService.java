package org.whale.system.cache.impl;

import java.io.Serializable;
import java.io.UnsupportedEncodingException;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.whale.system.cache.ICacheService;
import org.whale.system.common.util.Strings;

import com.alibaba.fastjson.JSON;

/**
 * 缓存模板抽象类
 *
 * @author 王金绍
 * 2015年4月25日 下午10:25:22
 */
public abstract class AbstractCacheService<M extends Serializable> implements ICacheService<M> {
	
	private static Logger logger = LoggerFactory.getLogger(AbstractCacheService.class);

	@Override
	public void put(String cacheName, String key, M value) {
		if(logger.isDebugEnabled()){
			logger.debug("put: {}_{} | {}",cacheName, key, JSON.toJSONString(value));
		}
		
		this.put(cacheName, key, value, null);
	}

	@Override
	public void mput(String cacheName, Map<String, M> keyValues) {
		if(logger.isDebugEnabled()){
			logger.debug("mput: {}_ | {}",cacheName, JSON.toJSONString(keyValues));
		}
		
		this.mput(cacheName, keyValues, null);
	}

	@Override
	public void put(String cacheName, String key, M value, Integer seconds) {
		if(logger.isDebugEnabled()){
			logger.debug("put: {}_{} |{} - {}",cacheName, key, seconds, JSON.toJSONString(value));
		}
		
		if(Strings.isBlank(key))
			return ;
		this.doPut(cacheName, key, value, seconds);
	}
	
	public abstract void doPut(String cacheName, String key, M value, Integer seconds);

	@Override
	public void mput(String cacheName, Map<String, M> keyValues, Integer seconds) {
		if(logger.isDebugEnabled()){
			logger.debug("mput: {}_ |{} - {}",cacheName, seconds, JSON.toJSONString(keyValues));
		}
		
		if(keyValues == null || keyValues.size() < 1)
			return ;
		if(keyValues.size() == 1){
			this.put(cacheName, keyValues.keySet().iterator().next(), keyValues.values().iterator().next(), seconds);
		}else{
			this.mdoPut(cacheName, keyValues, seconds);
		}
	}
	
	public abstract void mdoPut(String cacheName, Map<String, M> keyValues, Integer seconds);

	@Override
	public M get(String cacheName, String key) {
		if(logger.isDebugEnabled()){
			logger.debug("get: {}_{}",cacheName, key);
		}
		
		if(Strings.isBlank(key))
			return null;
		return this.doGet(cacheName, key);
	}
	
	public abstract M doGet(String cacheName, String key);

	@Override
	@SuppressWarnings("all")
	public List<M> mget(String cacheName, List<String> keys) {
		if(logger.isDebugEnabled()){
			logger.debug("mget: {}_{}",cacheName, keys);
		}
		
		if(keys == null || keys.size() < 1)
			return null;
		if(keys.size() == 1)
			return Arrays.asList(this.get(cacheName, keys.get(0)));
		return this.mdoGet(cacheName, keys);
	}
	
	public abstract List<M> mdoGet(String cacheName, List<String> keys);

	@Override
	public void del(String cacheName, String key) {
		if(logger.isDebugEnabled()){
			logger.debug("del: {}_{}",cacheName, key);
		}
		
		if(key == null)
			return ;
	}
	
	public abstract void doDel(String cacheName, String key);

	@Override
	public void mdel(String cacheName, List<String> keys) {
		if(logger.isDebugEnabled()){
			logger.debug("mdel: {}_{}",cacheName, keys);
		}
		
		if(keys == null || keys.size() < 1)
			return ;
		if(keys.size() == 1){
			this.del(cacheName, keys.get(0));
		}else{
			this.mdoDel(cacheName, keys);
		}
	}
	
	public abstract void mdoDel(String cacheName, List<String> keys);
	
	
	/**
	 * 获取组合后的key值
	 * 
	 * @param cacheName
	 * @param key
	 * @return
	 * @throws UnsupportedEncodingException
	 * 2015年4月25日 下午10:25:58
	 */
	protected byte[] getByteKey(String cacheName, Object key) throws UnsupportedEncodingException{
		StringBuilder strb = new StringBuilder();
		String k = strb.append(cacheName).append(":").append(key.toString()).toString();
		return k.getBytes("UTF-8");
	}

	protected String getKey(String cacheName, Object key){
		StringBuilder strb = new StringBuilder();
		return strb.append(cacheName).append(":").append(key.toString()).toString();
	}
	
}
