package org.whale.system.cache;

import java.util.Set;

public interface ICacheService {
	
	/**
	 * 缓存中保存记录，不会过期
	 * @param cacheName
	 * @param key
	 * @param value
	 */
	void put(String cacheName, String key, Object value);
	
	/**
	 * 
	 *功能说明: 往本缓中存入一条记录，expTime time后过期
	 *创建人: 王金绍
	 *创建时间:2013-4-28 下午3:06:19
	 *@param key
	 *@param value void
	 *
	 */
	void put(String cacheName, String key, Object value, Integer expTime);
	
	/**
	 * 根据key获取缓存记录数据
	 * @param key
	 * @return
	 */
	Object get(String cacheName, String key);
	
	/**
	 * 删除键为KEY的缓存记录
	 * @param key
	 */
	void evict(String cacheName, String key);
	
	/**
	 * 清除该缓存实例的所有缓存记录
	 */
	void clear(String cacheName);
	
	/**
	 * 获取缓存对象
	 * @return
	 */
	Object getNativeCache();
	
	/**
	 * 获取缓存的所有key集合
	 * @date 2015年1月16日 上午10:07:12
	 */
	Set<String> getKeys(String cacheName);
	
}
