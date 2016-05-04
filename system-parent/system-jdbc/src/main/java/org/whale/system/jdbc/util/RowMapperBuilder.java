package org.whale.system.jdbc.util;

import java.io.InputStream;
import java.lang.reflect.Type;
import java.math.BigDecimal;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Time;
import java.sql.Timestamp;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.support.lob.DefaultLobHandler;
import org.springframework.stereotype.Component;
import org.whale.system.jdbc.orm.entry.OrmColumn;

/**
 * jdbcTemplate RowMapper 生成器
 *
 * @author wjs
 * 2014年9月7日-下午4:26:47
 */
@Component
public class RowMapperBuilder {

	private static Logger logger = LoggerFactory.getLogger(RowMapperBuilder.class);

	@Autowired
	private DefaultLobHandler lobHandler;
	
	private Map<Class<?>, RowMapper<?>> map = new HashMap<Class<?>, RowMapper<?>>();
	
	/**
	 * 生产RowMapper
	 * 
	 * @param clazz 对象累
	 * @param list  数据库字段
	 * @return
	 */
	@SuppressWarnings("all")
	public RowMapper<?> buildRowMapper(final Class<?> clazz, final List<OrmColumn> list){
		RowMapper<?> rowMapper = new RowMapper() {
			@Override
			public Object mapRow(ResultSet rs, int rowNum) throws SQLException {
				Object obj = null;
				try {
					obj = clazz.newInstance();
					//对应关系见 http://hi.baidu.com/linyongboole/item/d3749cbff69121422bebe302
					for(OrmColumn col : list){
						if(java.sql.Types.VARCHAR == col.getType()){
							AnnotationUtil.setFieldValue(obj, col.getField(), rs.getObject(col.getSqlName()));
							continue ;
						}
						if(java.sql.Types.BIGINT == col.getType()){
							BigDecimal bg = rs.getBigDecimal(col.getSqlName());
							if(bg != null)
								AnnotationUtil.setFieldValue(obj, col.getField(), bg.longValue());
							continue ;
						}
						if(java.sql.Types.INTEGER == col.getType()){
							BigDecimal bg = rs.getBigDecimal(col.getSqlName());
							if(bg != null)
								AnnotationUtil.setFieldValue(obj, col.getField(), bg.intValue());
							continue ;
						}
						//不能用java.sql.Date 否则 时分秒 的值为 0
						if(java.sql.Types.TIMESTAMP == col.getType() || java.sql.Types.DATE == col.getType()){
							Timestamp time = rs.getTimestamp(col.getSqlName());
							if(time != null){
								AnnotationUtil.setFieldValue(obj, col.getField(),new Date(time.getTime()));
								continue ;
							}
						}
						if(java.sql.Types.BOOLEAN == col.getType()){
							AnnotationUtil.setFieldValue(obj, col.getField(), rs.getBoolean(col.getSqlName()));
							continue ;
						}
						if(java.sql.Types.DOUBLE == col.getType()){
							BigDecimal bg = rs.getBigDecimal(col.getSqlName());
							if(bg != null)
								AnnotationUtil.setFieldValue(obj, col.getField(), bg.doubleValue());
							continue ;
						}
						if(java.sql.Types.FLOAT == col.getType()){
							BigDecimal bg = rs.getBigDecimal(col.getSqlName());
							if(bg != null)
								AnnotationUtil.setFieldValue(obj, col.getField(), bg.floatValue());
							continue ;
						}
						
						if(java.sql.Types.CLOB == col.getType()){
							Type type = col.getField().getType();
							String t = type.toString();
							if("class java.lang.String".equals(t)){
								String value = lobHandler.getClobAsString(rs, col.getSqlName());
								AnnotationUtil.setFieldValue(obj, col.getField(), value);
								continue ;
							}
							if("class java.io.InputStream".equals(t)){
								InputStream value = lobHandler.getBlobAsBinaryStream(rs, col.getSqlName());
								AnnotationUtil.setFieldValue(obj, col.getField(), value);
								continue ;
							}
							if("class [Ljava.lang.Byte".equals(t) || "class [B".equals(t)){
								byte[] value = lobHandler.getBlobAsBytes(rs, col.getSqlName());
								AnnotationUtil.setFieldValue(obj, col.getField(), value);
								continue ;
							}
						}
						if(java.sql.Types.SMALLINT == col.getType()){
							BigDecimal bg = rs.getBigDecimal(col.getSqlName());
							if(bg != null)
								AnnotationUtil.setFieldValue(obj, col.getField(), bg.shortValue());
							continue ;
						}
						if(java.sql.Types.TINYINT == col.getType()){
							AnnotationUtil.setFieldValue(obj, col.getField(), rs.getByte(col.getSqlName()));
							continue ;
						}
						if(java.sql.Types.DISTINCT == col.getType()){
							AnnotationUtil.setFieldValue(obj, col.getField(), rs.getBigDecimal(col.getSqlName()));
							continue ;
						}
						if(java.sql.Types.TIME == col.getType() ){
							Time time = rs.getTime(col.getSqlName());
							if(time != null){
								AnnotationUtil.setFieldValue(obj, col.getField(),new Date(time.getTime()));
								continue ;
							}
						}
						AnnotationUtil.setFieldValue(obj, col.getField(), rs.getObject(col.getSqlName()));
					}
				} catch (InstantiationException e) {
					logger.error("设置值异常", e);
				} catch (IllegalAccessException e) {
					logger.error("设置值异常", e);
				}
				return obj;
			}
		};
		
		map.put(clazz, rowMapper);
		return rowMapper;
	}
	
	
	public RowMapper<?> getRowMapper(final Class<?> clazz){
		return this.getRowMapper(clazz);
	}
}
