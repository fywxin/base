package org.whale.system.jdbc.orm.rowMapper;

import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.support.lob.DefaultLobHandler;
import org.whale.system.jdbc.orm.entry.OrmColumn;
import org.whale.system.spring.SpringContextHolder;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * jdbcTemplate RowMapper 生成器
 *
 * @author wjs
 * 2014年9月7日-下午4:26:47
 */
public class RowMapperBuilder {

	private static final Map<Class<?>, RowMapper<?>> map = new HashMap<Class<?>, RowMapper<?>>();
	
	/**
	 * 生产RowMapper
	 * 
	 * @param clazz 对象累
	 * @param list  数据库字段
	 * @return
	 */
	public static <M> RowMapper<?> get(final Class<M> clazz, final List<OrmColumn> list){
		RowMapper<?> rowMapper =  map.get(clazz);
		if (rowMapper == null){
			//rowMapper = new OrmBeanPropertyRowMapper<M>(clazz, list);
			rowMapper = new OrmRowMapper<M>(clazz, list, SpringContextHolder.getBean(DefaultLobHandler.class));
			map.put(clazz, rowMapper);
		}
		return rowMapper;
	}
	
	
	public static <M> RowMapper<?> get(final Class<M> clazz){
		RowMapper<?> rowMapper =  map.get(clazz);
		if (rowMapper == null){
			rowMapper = new OrmBeanPropertyRowMapper<M>(clazz);
			map.put(clazz, rowMapper);
		}
		return rowMapper;
	}
}
