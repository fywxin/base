package org.whale.system.base;

import java.util.List;

import org.springframework.jdbc.core.JdbcTemplate;

/**
 * Dao 模板方法定义
 * 
 * @author 王金绍
 * 2014年9月17日-上午10:37:39
 */
public interface IBaseDao<T,PK> {

	/**
	 * 保存实体
	 * @param t
	 */
	void save(T t);
	
	/**
	 * 保存多个实体
	 * @param list
	 */
	void save(List<T> list);
	
	/**
	 * 批量保存实体
	 * @param list
	 */
	void saveBatch(List<T> list);
	
	/**
	 * 更新实体
	 * @param t
	 */
	void update(T t);
	
	/**
	 * 更新多个实体
	 * @param list
	 */
	void update(List<T> list);
	
	/**
	 * 批量更新多个实体
	 * @param list
	 */
	void updateBatch(List<T> list);
	
	/**
	 * 删除实体
	 * @param id
	 */
	void delete(PK id);
	
	/**
	 * 删除多个实体
	 * @param ids
	 */
	void delete(List<PK> ids);
	
	/**
	 * 按照实体条件删除记录
	 * @param t
	 */
	void deleteBy(T t);
	
	/**
	 * 获取单个对象
	 * @param id
	 * @return
	 */
	T get(PK id);
	
	/**
	 * 按sql返回单个对象
	 * @param sql
	 * @return
	 */
	T getObject(String sql);
	
	/**
	 * 按sql返回单个对象
	 * @param sql
	 * @param args
	 * @return
	 */
	T getObject(String sql, Object...args);
	
	/**
	 * 返回所有记录， 按id排序
	 * @return
	 */
	List<T> queryAll();
	
	/**
	 * 按对象条件动态拼接查询
	 * @param t
	 * @return
	 */
	List<T> query(T t);
	
	/**
	 * 按sql返回多个对象
	 * @param sql
	 * @return
	 */
	List<T> query(String sql);
	
	/**
	 * 按sql返回多个对象
	 * @param sql
	 * @param args
	 * @return
	 */
	List<T> query(String sql, Object...args);
	
	/**
	 * 分页查询
	 * @param page
	 */
	void queryPage(Page page);
	
	/**
	 * 
	 * @return
	 */
	JdbcTemplate getJdbcTemplate();
}
