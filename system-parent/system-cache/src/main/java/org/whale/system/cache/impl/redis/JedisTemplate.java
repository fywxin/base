package org.whale.system.cache.impl.redis;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import redis.clients.jedis.Jedis;
import redis.clients.jedis.Pipeline;
import redis.clients.jedis.Response;
import redis.clients.jedis.exceptions.JedisConnectionException;
import redis.clients.jedis.exceptions.JedisException;
import redis.clients.util.Pool;

/**
 * JedisTemplate 提供了一个template方法，负责对Jedis连接的获取与归还。
 * JedisAction<T> 和 JedisActionNoResult两种回调接口，适用于有无返回值两种情况。
 * 同时提供一些最常用函数的封装, 如get/set/zadd等。
 */
public class JedisTemplate {
	private static Logger logger = LoggerFactory.getLogger(JedisTemplate.class);

	private Pool<Jedis> jedisPool;

	public JedisTemplate(Pool<Jedis> jedisPool) {
		this.jedisPool = jedisPool;
	}

	/**
	 * 执行有返回结果的action。
	 */
	public <T> T execute(JedisAction<T> jedisAction) throws JedisException {
		Jedis jedis = null;
		boolean broken = false;
		try {
			jedis = jedisPool.getResource();
			return jedisAction.action(jedis);
		} catch (JedisConnectionException e) {
			logger.error("Redis connection lost.", e);
			broken = true;
			throw e;
		} finally {
			closeResource(jedis, broken);
		}
	}
	
	public Object execute2(JedisAction2 jedisAction) throws JedisException {
		Jedis jedis = null;
		boolean broken = false;
		try {
			jedis = jedisPool.getResource();
			return jedisAction.action(jedis);
		} catch (JedisConnectionException e) {
			logger.error("Redis connection lost.", e);
			broken = true;
			throw e;
		} finally {
			closeResource(jedis, broken);
		}
	}

	/**
	 * 执行无返回结果的action。
	 */
	public void execute(JedisActionNoResult jedisAction) throws JedisException {
		Jedis jedis = null;
		boolean broken = false;
		try {
			jedis = jedisPool.getResource();
			jedisAction.action(jedis);
		} catch (JedisConnectionException e) {
			logger.error("Redis connection lost.", e);
			broken = true;
			throw e;
		} finally {
			closeResource(jedis, broken);
		}
	}

	/**
	 * 根据连接是否已中断的标志，分别调用returnBrokenResource或returnResource。
	 */
	protected void closeResource(Jedis jedis, boolean connectionBroken) {
		if (jedis != null) {
			try {
				if (connectionBroken) {
					jedisPool.returnBrokenResource(jedis);
				} else {
					jedisPool.returnResource(jedis);
				}
			} catch (Exception e) {
				logger.error("Error happen when return jedis to pool, try to close it directly.", e);
				JedisUtils.closeJedis(jedis);
			}
		}
	}

	/**
	 * 获取内部的pool做进一步的动作。
	 */
	public Pool<Jedis> getJedisPool() {
		return jedisPool;
	}

	/**
	 * 有返回结果的回调接口定义。
	 */
	public interface JedisAction<T> {
		T action(Jedis jedis);
	}
	
	public interface JedisAction2 {
		Object action(Jedis jedis);
	}

	/**
	 * 无返回结果的回调接口定义。
	 */
	public interface JedisActionNoResult {
		void action(Jedis jedis);
	}

	// ////////////// 常用方法的封装 ///////////////////////// //

	// ////////////// 公共 ///////////////////////////
	/**
	 * 删除key, 如果key存在返回true, 否则返回false。
	 */
	public Boolean del(final String... keys) {
		return execute(new JedisAction<Boolean>() {

			@Override
			public Boolean action(Jedis jedis) {
				return jedis.del(keys) == 1 ? true : false;
			}
		});
	}
	
	public void del(final byte[] key){
		execute(new JedisActionNoResult() {

			@Override
			public void action(Jedis jedis) {
				jedis.del(key);
			}
		});
	}
	
	public void del(final List<byte[]> keys){
		execute(new JedisActionNoResult() {

			@Override
			public void action(Jedis jedis) {
				byte[][] _keys = new byte[keys.size()][];
				for(int i=0; i<keys.size(); i++){
					_keys[i] = keys.get(i);
				}
				jedis.del(_keys);
			}
		});
	}

	public void flushDB() {
		execute(new JedisActionNoResult() {

			@Override
			public void action(Jedis jedis) {
				jedis.flushDB();
			}
		});
	}

	// ////////////// 关于String ///////////////////////////
	/**
	 * 如果key不存在, 返回null.
	 */
	public String get(final String key) {
		return execute(new JedisAction<String>() {

			@Override
			public String action(Jedis jedis) {
				return jedis.get(key);
			}
		});
	}
	
	public byte[] get(final byte[] key){
		Object rs= execute2(new JedisAction2() {
			@Override
			public Object action(Jedis jedis) {
				return jedis.get(key);
			}
		});
		
		return (byte[])rs;
	}
	
	public List<String> mget(final List<String> keys){
		return execute(new JedisAction<List<String>>() {
			@Override
			public List<String> action(Jedis jedis) {
				String[] _keys = new String[keys.size()];
				Iterator<String> it = keys.iterator();
				int i=0;
				while(it.hasNext()){
					_keys[i] = it.next();
					i++;
				}
				return jedis.mget(_keys);
			}
		});
	}
	
	@SuppressWarnings("all")
	public List<byte[]> mgetByte(final List<byte[]> keys){
		Object rs= execute2(new JedisAction2() {
			@Override
			public Object action(Jedis jedis) {
				byte[][] _keys = new byte[keys.size()][];
				for(int i=0; i<keys.size(); i++){
					_keys[i] = keys.get(i);
				}
				
				return jedis.mget(_keys);
			}
		});
		
		return (List<byte[]>)rs;
	}

	/**
	 * 如果key不存在, 返回null.
	 */
	public Long getAsLong(final String key) {
		String result = get(key);
		return result != null ? Long.valueOf(result) : null;
	}

	/**
	 * 如果key不存在, 返回null.
	 */
	public Integer getAsInt(final String key) {
		String result = get(key);
		return result != null ? Integer.valueOf(result) : null;
	}
	
	/**
	 * 如果key不存在, 返回null.
	 */
	public Integer getAsInt(final String key, final String field) {
		String result = hget(key, field);
		return result != null ? Integer.valueOf(result) : null;
	}
	
	//--------------------------set----------------------------

	public void set(final String key, final String value) {
		execute(new JedisActionNoResult() {

			@Override
			public void action(Jedis jedis) {
				jedis.set(key, value);
			}
		});
	}
	
	public void set(final byte[] key, final byte[] value) {
		execute(new JedisActionNoResult() {

			@Override
			public void action(Jedis jedis) {
				jedis.set(key, value);
			}
		});
	}
	
	public void mset(final Map<String, String> vals){
		 execute(new JedisActionNoResult() {
			@Override
			public void action(Jedis jedis) {
				Pipeline p = jedis.pipelined();
				for(Map.Entry<String, String> entry : vals.entrySet()){
					p.set(entry.getKey(), entry.getValue());
				}
				p.sync();
			}
		});
	}
	
	public void msetByte(final List<byte[]> key, final List<byte[]> value) {
		execute(new JedisActionNoResult() {
			@Override
			public void action(Jedis jedis) {
				Pipeline p = jedis.pipelined();
				for(int i=0; i<key.size(); i++){
					p.set(key.get(i), value.get(i));
				}
				p.sync();
			}
		});
	}

	public void setex(final String key, final String value, final int seconds) {
		execute(new JedisActionNoResult() {

			@Override
			public void action(Jedis jedis) {
				jedis.setex(key, seconds, value);
			}
		});
	}
	
	public void setex(final byte[] key, final byte[] value, final int seconds) {
		execute(new JedisActionNoResult() {

			@Override
			public void action(Jedis jedis) {
				jedis.setex(key, seconds, value);
			}
		});
	}
	
	public void msetex(final Map<String, String> vals, final int seconds) {
		execute(new JedisActionNoResult() {
			@Override
			public void action(Jedis jedis) {
				Pipeline p = jedis.pipelined();
				for(Map.Entry<String, String> entry : vals.entrySet()){
					p.setex(entry.getKey(), seconds, entry.getValue());
				}
				p.sync();
			}
		});
	}
	
	public void msetexByte(final List<byte[]> key, final List<byte[]> value, final int seconds) {
		execute(new JedisActionNoResult() {
			@Override
			public void action(Jedis jedis) {
				Pipeline p = jedis.pipelined();
				for(int i=0; i<key.size(); i++){
					p.setex(key.get(i), seconds, value.get(i));
				}
				p.sync();
			}
		});
	}

	/**
	 * 如果key还不存在则进行设置，返回true，否则返回false.
	 */
	public Boolean setnx(final String key, final String value) {
		return execute(new JedisAction<Boolean>() {

			@Override
			public Boolean action(Jedis jedis) {
				return jedis.setnx(key, value) == 1 ? true : false;
			}
		});
	}

	/**
	 * 综合setNX与setEx的效果。
	 */
	public Boolean setnxex(final String key, final String value, final int seconds) {
		return execute(new JedisAction<Boolean>() {

			@Override
			public Boolean action(Jedis jedis) {
				String result = jedis.set(key, value, "NX", "EX", seconds);
				return JedisUtils.isStatusOk(result);
			}
		});
	}

	public Long incr(final String key) {
		return execute(new JedisAction<Long>() {
			@Override
			public Long action(Jedis jedis) {
				return jedis.incr(key);
			}
		});
	}
	
	public Long incrBy(final String key, final long value) {
		return execute(new JedisAction<Long>() {
			@Override
			public Long action(Jedis jedis) {
				return jedis.incrBy(key, value);
			}
		});
	}
	
	public Long decrBy(final String key, final long value) {
		return execute(new JedisAction<Long>() {
			@Override
			public Long action(Jedis jedis) {
				return jedis.decrBy(key, value);
			}
		});
	}

	public Long decr(final String key) {
		return execute(new JedisAction<Long>() {
			@Override
			public Long action(Jedis jedis) {
				return jedis.decr(key);
			}
		});
	}

	// ////////////// 关于List ///////////////////////////
	public void lpush(final String key, final String... values) {
		execute(new JedisActionNoResult() {
			@Override
			public void action(Jedis jedis) {
				jedis.lpush(key, values);
			}
		});
	}

	public String rpop(final String key) {
		return execute(new JedisAction<String>() {

			@Override
			public String action(Jedis jedis) {
				return jedis.rpop(key);
			}
		});
	}

	/**
	 * 返回List长度, key不存在时返回0，key类型不是list时抛出异常.
	 */
	public Long llen(final String key) {
		return execute(new JedisAction<Long>() {

			@Override
			public Long action(Jedis jedis) {
				return jedis.llen(key);
			}
		});
	}

	/**
	 * 删除List中的第一个等于value的元素，value不存在或key不存在时返回false.
	 */
	public Boolean lremOne(final String key, final String value) {
		return execute(new JedisAction<Boolean>() {
			@Override
			public Boolean action(Jedis jedis) {
				Long count = jedis.lrem(key, 1, value);
				return (count == 1);
			}
		});
	}

	/**
	 * 删除List中的所有等于value的元素，value不存在或key不存在时返回false.
	 */
	public Boolean lremAll(final String key, final String value) {
		return execute(new JedisAction<Boolean>() {
			@Override
			public Boolean action(Jedis jedis) {
				Long count = jedis.lrem(key, 0, value);
				return (count > 0);
			}
		});
	}

	// ////////////// 关于Sorted Set ///////////////////////////
	/**
	 * 加入Sorted set, 如果member在Set里已存在, 只更新score并返回false, 否则返回true.
	 */
	public Boolean zadd(final String key, final String member, final double score) {
		return execute(new JedisAction<Boolean>() {

			@Override
			public Boolean action(Jedis jedis) {
				return jedis.zadd(key, score, member) == 1 ? true : false;
			}
		});
	}

	/**
	 * 删除sorted set中的元素，成功删除返回true，key或member不存在返回false。
	 */
	public Boolean zrem(final String key, final String member) {
		return execute(new JedisAction<Boolean>() {

			@Override
			public Boolean action(Jedis jedis) {
				return jedis.zrem(key, member) == 1 ? true : false;
			}
		});
	}

	/**
	 * 当key不存在时返回null.
	 */
	public Double zscore(final String key, final String member) {
		return execute(new JedisAction<Double>() {

			@Override
			public Double action(Jedis jedis) {
				return jedis.zscore(key, member);
			}
		});
	}

	/**
	 * 返回sorted set长度, key不存在时返回0.
	 */
	public Long zcard(final String key) {
		return execute(new JedisAction<Long>() {

			@Override
			public Long action(Jedis jedis) {
				return jedis.zcard(key);
			}
		});
	}
	
	/**
	 * 按正则表达式返回key集合
	 * 
	 * http://blog.csdn.net/fachang/article/details/7984123
	 * @param pattern
	 * @return
	 */
	public Set<String> keys(final String pattern){
		return execute(new JedisAction<Set<String>>() {

			@Override
			public Set<String> action(Jedis jedis) {
				return jedis.keys(pattern);
			}
		});
	}
	
	
	public Long hset(final String key, final String field, final String value){
		return execute(new JedisAction<Long>() {

			@Override
			public Long action(Jedis jedis) {
				return jedis.hset(key, field, value);
			}
		});
	}
	
	public String hget(final String key, final String field){
		return execute(new JedisAction<String>() {

			@Override
			public String action(Jedis jedis) {
				return jedis.hget(key, field);
			}
		});
	}
	
	
	
	
	public List<Long> mincr(final String[] keys){
		 return execute(new JedisAction<List<Long>>() {
			@Override
			public List<Long> action(Jedis jedis) {
				Pipeline p = jedis.pipelined();
				List<Response<Long>> ress = new ArrayList<Response<Long>>();
				List<Long> lst = new ArrayList<Long>();
				for(String key : keys){
					ress.add(p.incr(key));
				}
				p.sync();
				for(Response<Long> res : ress){
					lst.add(res.get());
				}
				
				return lst;
			}
		});
	}
	
	
	public List<Long> mdecr(final String[] keys){
		 return execute(new JedisAction<List<Long>>() {
			@Override
			public List<Long> action(Jedis jedis) {
				Pipeline p = jedis.pipelined();
				List<Response<Long>> ress = new ArrayList<Response<Long>>();
				List<Long> lst = new ArrayList<Long>();
				for(String key : keys){
					ress.add(p.decr(key));
				}
				p.sync();
				for(Response<Long> res : ress){
					lst.add(res.get());
				}
				
				return lst;
			}
		});
	}

}
