package org.whale.system.jdbc.orm.rowMapper;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.support.lob.DefaultLobHandler;
import org.springframework.stereotype.Component;
import org.whale.system.jdbc.orm.entry.OrmColumn;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * jdbcTemplate RowMapper 生成器
 *
 * @author wjs
 * 2014年9月7日-下午4:26:47
 */
@Component
public class RowMapperBuilder {

	private final Map<Class<?>, RowMapper<?>> map = new HashMap<Class<?>, RowMapper<?>>();

	@Autowired
	private DefaultLobHandler lobHandler;
	
	/**
	 * 生产RowMapper
	 * 
	 * @param clazz 对象累
	 * @param list  数据库字段
	 * @return
	 */
	public <M> RowMapper<?> get(final Class<M> clazz, final List<OrmColumn> list){
		RowMapper<?> rowMapper =  this.map.get(clazz);
		if (rowMapper == null){
			//rowMapper = new OrmBeanPropertyRowMapper<M>(clazz, list);
			rowMapper = new OrmRowMapper<M>(clazz, list, lobHandler);
			this.map.put(clazz, rowMapper);
		}
		return rowMapper;
	}
	
	
	public <M> RowMapper<?> get(final Class<M> clazz){
		RowMapper<?> rowMapper =  this.map.get(clazz);
		if (rowMapper == null){
			rowMapper = new OrmBeanPropertyRowMapper<M>(clazz);
			map.put(clazz, rowMapper);
		}
		return rowMapper;
	}
}
