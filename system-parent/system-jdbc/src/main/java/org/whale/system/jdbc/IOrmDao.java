package org.whale.system.jdbc;

import org.whale.system.base.Iquery;
import org.whale.system.base.Page;
import org.whale.system.base.Q;
import org.whale.system.jdbc.orm.OrmContext;
import org.whale.system.jdbc.orm.entry.OrmTable;

import java.io.Serializable;
import java.util.List;
import java.util.Map;

/**
 * Dao 模板方法定义
 * 
 * @author wjs
 * 2014年9月17日-上午10:37:39
 */
public interface IOrmDao<T extends Serializable,PK extends Serializable> {
	
	Q q();

	/**
	 * 保存实体
	 * @param t
	 */
	void save(T t);
	
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
	 * 更新非空字段, id字段必须有值
	 * 
	 * @param t
	 */
	void updateNotNull(T t);
	
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
	void deleteBatch(List<PK> ids);
	
	/**
	 * 按照自定义条件删除
	 * @param query
	 */
	void delete(Iquery query);
	
	/**
	 * 获取单个对象
	 * @param id
	 * @return
	 */
	T get(PK id);
	
	/**
	 * 按自定义条件查询对象
	 * @param query
	 * @return
	 */
	T get(Iquery query);
	
	
	/**
	 * 返回所有记录， 按id排序
	 * @return
	 */
	List<T> queryAll();
	
	/**
	 * 按自定义条件查询
	 * @param query
	 * @return
	 */
	List<T> query(Iquery query);
	
	/**
	 * 分页查询
	 * @param page
	 */
	void queryPage(Page page);
	
	/**
	 * 总记录数
	 * @param query
	 * @return
	 */
	Integer count(Iquery query);
	
	
	List<Map<String, Object>> queryForList(Iquery query);
	
	Map<String, Object> queryForMap(Iquery query);

	
	//--------------------------------------本Orm提供的内部容器访问方法---------------------------------------
	OrmTable _getOrmTable();
	
	OrmContext _getOrmContext();
}
