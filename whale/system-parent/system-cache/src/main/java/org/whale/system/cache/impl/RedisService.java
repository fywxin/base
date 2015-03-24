package org.whale.system.cache.impl;

import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.whale.system.cache.ICacheService;
import org.whale.system.cache.impl.redis.JedisTemplate;
import org.whale.system.common.exception.RemoteCacheException;
import org.whale.system.common.exception.SysException;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.serializer.SerializerFeature;

public class RedisService implements ICacheService {
	
	@Autowired
	private JedisTemplate JedisTemplate;
	
	@Override
	public void put(String cacheName, String key, Object value) {
		this.put(cacheName, key, value, null);
	}

	@Override
	public void put(String cacheName, String key, Object value, Integer expTime) {
		try {
			if(expTime == null || expTime < 1){
				this.JedisTemplate.set(this.getKey(cacheName, key), JSON.toJSONString(value, SerializerFeature.WriteClassName));
			}else{
				this.JedisTemplate.setex(this.getKey(cacheName, key), JSON.toJSONString(value, SerializerFeature.WriteClassName), expTime);
			}
		} catch (Exception e) {
			throw new RemoteCacheException("Redis缓存出现异常！", e);
		}
	}

	@Override
	public Object get(String cacheName, String key) {
		try {
			return JSON.parse(this.JedisTemplate.get(this.getKey(cacheName, key)));
		} catch (Exception e) {
			throw new RemoteCacheException("Redis缓存出现异常！", e);
		}
	}

	@Override
	public void evict(String cacheName, String key) {
		try {
			this.JedisTemplate.del(this.getKey(cacheName, key));
		} catch (Exception e) {
			throw new RemoteCacheException("Redis缓存出现异常！", e);
		}
	}

	@Override
	public void clear(String cacheName) {
		throw new SysException("Redis 不支持缓存批量清空功能！");
	}
	
	private String getKey(String cacheName, Object key){
		StringBuilder strb = new StringBuilder();
		return strb.append(cacheName).append("_").append(key.toString()).toString();
	}

	@Override
	public Object getNativeCache() {
		return JedisTemplate;
	}

	@Override
	public Set<String> getKeys(String cacheName) {
		return JedisTemplate.keys(cacheName+"*");
	}

	

	
//	public static void main(String[] args) {
//		UserAuth userAuth = new UserAuth();
//		userAuth.setUserId(1L);
//		
//		System.out.println(JSON.toJSONString(userAuth, SerializerFeature.WriteClassName));
//		String str = "{\"@type\":\"org.whale.system.auth.domain.UserAuth\",\"userId\":1}";
//		UserAuth userAuth2 = (UserAuth)JSON.parse(str);
//		System.out.println(userAuth2.getUserId());
//		
//		
//		Dict dict = new Dict();
//		dict.setDictId(2L);
//		dict.setDictCode("dictCode");
//		
//		DictItem item = new DictItem();
//		item.setDictId(2L);
//		item.setDictItemId(1L);
//		item.setItemName("itemName1");
//		
//		DictItem item2 = new DictItem();
//		item2.setDictId(2L);
//		item2.setDictItemId(2L);
//		item2.setItemName("itemName2");
//		
//		List<DictItem> items = new ArrayList<DictItem>();
//		items.add(item2);
//		items.add(item);
//		
//		dict.setItems(items);
//		
//		System.out.println(JSON.toJSONString(dict, SerializerFeature.WriteClassName));
//		
//		String rs = "{\"@type\":\"org.whale.system.domain.Dict\",\"dictCode\":\"dictCode\",\"dictId\":2,\"dictType\":0,\"items\":[{\"dictId\":2,\"dictItemId\":2,\"itemName\":\"itemName2\"},{\"dictId\":2,\"dictItemId\":1,\"itemName\":\"itemName1\"}]}";
//		Dict di = (Dict)JSON.parse(rs);
//		System.out.println(di.getDictCode());
//		items = di.getItems();
//		System.out.println(items.size());
//		System.out.println(items.get(0).getItemName());
//	}
}
