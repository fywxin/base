package org.whale.system.jdbc.orm;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Component;
import org.whale.system.common.exception.OrmException;
import org.whale.system.common.reflect.EntryContext;
import org.whale.system.jdbc.orm.entry.OrmClass;
import org.whale.system.jdbc.orm.entry.OrmColumn;
import org.whale.system.jdbc.orm.entry.OrmSql;
import org.whale.system.jdbc.orm.entry.OrmTable;
import org.whale.system.jdbc.orm.sql.SqlBulider;
import org.whale.system.jdbc.orm.table.OrmTableBulider;
import org.whale.system.jdbc.orm.value.ValueBulider;
import org.whale.system.jdbc.util.AnnotationUtil;
import org.whale.system.jdbc.util.RowMapperBulider;

/**
 * ORM 容器上下文
 *
 * @author 王金绍
 * 2014年9月7日-下午4:59:41
 */
@Component
public class OrmContext extends EntryContext {
	
	private static Logger logger = LoggerFactory.getLogger(OrmContext.class);
	
	@Autowired
	private RowMapperBulider rowMapperBulider;
	@Autowired
	private OrmTableBulider ormTableBulider;
	@Autowired
	private SqlBulider sqlBulider;
	@Autowired
	protected ValueBulider valueBulider;
	
	private final Map<Class<?>, OrmClass> ormClassCache = new HashMap<Class<?>, OrmClass>();
	//锁
	private Object lock = new Object();
	
	/**
	 * 
	 *功能说明: 添加一个元注释
	 *创建人: 王金绍
	 *创建时间:2013-3-18 下午6:25:53
	 *@param clazz void
	 *
	 */
	public OrmClass parse(Class<?> clazz){
		logger.info("ORM: 类[{}] 开始解析...", clazz.getName());
		OrmClass ormClass = new OrmClass();
					
		OrmTable ormTable = this.ormTableBulider.parse(clazz);
		
		List<OrmSql> ormSqls = new ArrayList<OrmSql>(OrmSql.OPT_MAX);
		OrmSql save = sqlBulider.bulid(ormTable, OrmSql.OPT_SAVE);
		OrmSql saveBatch = sqlBulider.bulid(ormTable, OrmSql.OPT_SAVE_BATCH);
		OrmSql update = sqlBulider.bulid(ormTable, OrmSql.OPT_UPDATE);
		OrmSql delete = sqlBulider.bulid(ormTable, OrmSql.OPT_DELETE);
		OrmSql get = sqlBulider.bulid(ormTable, OrmSql.OPT_GET);
		OrmSql getAll = sqlBulider.bulid(ormTable, OrmSql.OPT_GET_ALL);
		
		ormSqls.add(save);
		ormSqls.add(saveBatch);
		ormSqls.add(update);
		ormSqls.add(delete);
		ormSqls.add(get);
		ormSqls.add(getAll);
		
		//添加RowMapper缓存对象
		RowMapper<?> rowMapper = this.rowMapperBulider.getRowMapper(clazz, ormTable.getOrmCols());
		logger.info("ORM: 类[{}] 解析RowMapper完成!", clazz.getName());
		
		ormClass.setOrmTable(ormTable);
		ormClass.setOrmSqls(ormSqls);
		ormClass.setRowMapper(rowMapper);
		
		logger.info("ORM: 解析完成 {} : {}", clazz.getName(),ormClass);
		
		return ormClass;
	}
	
	/**
	 * 获取OrmClass 对象
	 * 
	 * @param clazz
	 * @return
	 */
	public OrmClass getOrmClass(Class<?> clazz){
		OrmClass ormClass =  this.ormClassCache.get(clazz);
		if(ormClass == null){
			synchronized (lock) {
				if(this.ormClassCache.get(clazz) == null){
					logger.info("ORM: 缓存中查找不到 类[{}] 对应的OrmClass, 开始解析...", clazz.getName());
					ormClass = this.parse(clazz);
					if(ormClass != null){
						this.ormClassCache.put(clazz, ormClass);
					}else{
						logger.warn("ORM: 解析类[{}] 返回 OrmClass = null", clazz.getName());
					}
				}
			}
		}
		return ormClass;
	}

	/**
	 *功能说明: 是否包含某类对象
	 *
	 *@param clazz
	 *@return boolean
	 */
	public boolean contain(Class<?> clazz){
		return this.ormClassCache.containsKey(clazz);
	}
	
	
	
	/**
	 * 
	 *功能说明: 获取类对象的rowMapper
	 *创建人: 王金绍
	 *创建时间:2013-4-9 上午9:56:03
	 *@param clazz
	 *@return RowMapper<?>
	 *
	 */
	@SuppressWarnings("all")
	public <T> RowMapper<T> getRowMapper(Class<T> clazz){
		return (RowMapper<T>)this.getOrmClass(clazz).getRowMapper();
	}
	
	
	
	
	/**
	 * 
	 *功能说明: 获取某个类的所有OrmSql
	 *创建人: 王金绍
	 *创建时间:2013-3-14 下午4:03:42
	 *@param clazz
	 *@return List<OrmSql>
	 *
	 */
	private List<OrmSql> getOrmSqls(Class<?> clazz){
		return this.getOrmClass(clazz).getOrmSqls();
	}
	
	
	/**
	 * 
	 *功能说明: 获取不同操作类型对应的OrmSql
	 *创建人: 王金绍
	 *创建时间:2013-3-14 下午4:03:22
	 *@param clazz
	 *@param opType
	 *@return OrmSql
	 *
	 */
	public OrmSql getOrmSql(Class<?> clazz, int opType) {
		if(opType < 0 || opType > OrmSql.OPT_MAX){
			throw new OrmException("ORM异常：非法操作类型");
		}
		if(clazz == null){
			throw new OrmException("ORM异常：clazz == null");
		}
		List<OrmSql> sqls = this.getOrmSqls(clazz);
		if(sqls == null || sqls.size() < 1) return null;
		return sqls.get(opType);
	}
	
	
	/**
	 * 
	 *功能说明: 获取类的@Table 定义信息
	 *创建人: 王金绍
	 *创建时间:2013-3-9 下午1:40:19
	 *@param clazz
	 *@return OrmTable
	 *
	 */
	public OrmTable getOrmTable(Class<?> clazz){
		return this.getOrmClass(clazz).getOrmTable();
	}
	
	/**
	 * 
	 *功能说明: 获取类的所有@Column 定义信息
	 *创建人: 王金绍
	 *创建时间:2013-3-9 下午1:44:48
	 *@param clazz
	 *@return List<OrmColumn>
	 *
	 */
	public List<OrmColumn> getOrmColumns(Class<?> clazz){
		OrmTable ormTable = getOrmTable(clazz);
		if(ormTable == null)
			return null;
		return ormTable.getOrmCols();
	}
	
	/**
	 * 
	 *功能说明: 获取类中字段{colName}的@Column 定义信息
	 *创建人: 王金绍
	 *创建时间:2013-3-9 下午1:45:21
	 *@param clazz
	 *@param colName
	 *@return OrmColumn
	 *
	 */
	public OrmColumn getOrmColumn(Class<?> clazz, String colName){
		List<OrmColumn> list = getOrmColumns(clazz);
		if(list != null && list.size() >0){
			for(OrmColumn col : list){
				if(col.getAttrName().equals(colName))
					return col;
			}
		}
		return null;
	}
	
	/**
	 * 
	 *功能说明: 获取类中ID主键字段的@Column 定义信息
	 *创建人: 王金绍
	 *创建时间:2013-3-9 下午1:46:00
	 *@param clazz
	 *@return OrmColumn
	 *
	 */
	public OrmColumn getIdOrmColumn(Class<?> clazz){
		OrmTable ormTable = getOrmTable(clazz);
		if(ormTable == null)
			return null;
		return ormTable.getIdCol();
	}
	
	/**
	 * 
	 *功能说明: 获取类的所有@Column 对应的 Field 对象列表
	 *创建人: 王金绍
	 *创建时间:2013-3-9 下午1:46:20
	 *@param clazz
	 *@return List<Field>
	 *
	 */
	public List<Field> getColumnFields(Class<?> clazz){
		List<OrmColumn> cols = getOrmColumns(clazz);
		if(cols == null || cols.size() < 1)
			return null;
		List<Field> fields = new ArrayList<Field>(cols.size());
		for(OrmColumn col : cols){
			fields.add(col.getField());
		}
		return fields;
	}
	
	/**
	 * 
	 *功能说明: 获取类中字段{colName}的 Field 对象
	 *创建人: 王金绍
	 *创建时间:2013-3-9 下午1:47:14
	 *@param clazz
	 *@param colName
	 *@return Field
	 *
	 */
	public Field getColumnField(Class<?> clazz, String colName){
		List<OrmColumn> cols = getOrmColumns(clazz);
		if(cols != null && cols.size() > 0){
			for(OrmColumn col : cols){
				if(col.getAttrName().equals(colName))
					return col.getField();
			}
		}
		return null;
	}
	
	/**
	 * 
	 *功能说明:获取类中ID主键字段的 Field 对象
	 *创建人: 王金绍
	 *创建时间:2013-3-9 下午1:47:47
	 *@param clazz
	 *@return Field
	 *
	 */
	public Field getIdColumnField(Class<?> clazz){
		OrmColumn col = getIdOrmColumn(clazz);
		if(col == null)
			return null;
		return col.getField();
	}
	
	/**
	 * 
	 *功能说明: 获取类中{colName}字段的值
	 *创建人: 王金绍
	 *创建时间:2013-3-9 下午1:48:09
	 *@param obj
	 *@param colName
	 *@return Object
	 *
	 */
	public Object getColumnValue(Object obj, String colName){
		if(obj == null) return null;
		Field field = getColumnField(obj.getClass(), colName);
		if(field == null)
			return null;
		return AnnotationUtil.getFieldValue(obj, field);
	}
	
	/**
	 * 
	 *功能说明: 获取类中ID主键字段的值
	 *创建人: 王金绍
	 *创建时间:2013-3-9 下午1:48:09
	 *@param obj
	 *@param colName
	 *@return Object
	 *
	 */
	public Object getIdValue(Object obj){
		if(obj == null) return null;
		Field idField = getIdColumnField(obj.getClass());
		if(idField == null)
			return null;
		return AnnotationUtil.getFieldValue(obj, idField);
	}
	
	public Map<Class<?>, OrmClass> getOrmClassCache(){
		return this.ormClassCache;
	}
	
}
