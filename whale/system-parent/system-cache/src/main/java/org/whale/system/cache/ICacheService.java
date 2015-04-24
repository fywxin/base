package org.whale.system.cache;

import java.io.Serializable;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * 一个cacheName 只能对应一种java类型或类
 * 
 * @author Administrator
 *
 */
public interface ICacheService<M extends Serializable> {
	
	/**
	 * 缓存中保存记录，不会过期
	 * @param cacheName
	 * @param key
	 * @param value
	 */
	void put(String cacheName, String key, M value);
	
	/**
	 * 批量存储
	 * 
	 * @param cacheName
	 * @param keyValues
	 */
	void mput(String cacheName, Map<String, M> keyValues);
	
	/**
	 * 
	 *功能说明: 往本缓中存入一条记录，seconds 秒后过期
	 *创建人: 王金绍
	 *创建时间:2013-4-28 下午3:06:19
	 *@param key
	 *@param value void
	 *
	 */
	void put(String cacheName, String key, M value, Integer seconds);
	
	/**
	 * 批量存储不过期缓存
	 * 
	 * @param cacheName
	 * @param keyValues
	 * @param expTime
	 */
	void mput(String cacheName, Map<String, M> keyValues, Integer seconds);
	
	/**
	 * 根据key获取缓存记录数据
	 * @param key
	 * @return
	 */
	M get(String cacheName, String key);
	
	/**
	 * 批量获取
	 * 
	 * @param cacheName
	 * @param keys
	 * @return
	 */
	List<M> mget(String cacheName, List<String> keys);
	
	/**
	 * 删除键为KEY的缓存记录
	 * @param key
	 */
	void del(String cacheName, String key);
	
	/**
	 * 批量删除
	 * 
	 * @param cacheName
	 * @param keys
	 */
	void mdel(String cacheName, List<String> keys);
	
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
