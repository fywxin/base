package org.whale.system.jdbc;

import java.io.Serializable;
import java.lang.reflect.Field;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Types;
import java.util.Date;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.BatchPreparedStatementSetter;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.PreparedStatementCreator;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.whale.system.base.BaseDao;
import org.whale.system.base.Page;
import org.whale.system.common.exception.OrmException;
import org.whale.system.common.util.ReflectionUtil;
import org.whale.system.common.util.Strings;
import org.whale.system.jdbc.orm.OrmContext;
import org.whale.system.jdbc.orm.entry.OrmColumn;
import org.whale.system.jdbc.orm.entry.OrmTable;
import org.whale.system.jdbc.orm.entry.OrmValue;
import org.whale.system.jdbc.orm.value.ValueBulider;
import org.whale.system.jdbc.util.AnnotationUtil;
import org.whale.system.jdbc.util.DbKind;

public class OrmDaoImpl<T extends Serializable,PK extends Serializable> implements IOrmDao<T, PK> {

	private static Logger logger = LoggerFactory.getLogger(BaseDao.class);
	
	//T类
	private Class<T> clazz;
	//rowMapper 转换器
	private RowMapper<T> rowMapper;
	@Autowired
	protected JdbcTemplate jdbcTemplate;
	@Autowired
	protected ValueBulider valueBulider;
	
	//该类Dao 插件上下文
	@Autowired
	protected OrmContext ormContext;
	//T 对应的表信息
	private OrmTable ormTable;
	
	public OrmDaoImpl(){
		clazz = ReflectionUtil.getSuperClassGenricType(getClass());
	}
	
	/**
	 * 保存实体对象
	 * 1. mysql 主键回填
	 * 2. oracle 采用序列生成，所以无需主键回填
	 * @param t
	 */
	public void save(T t){
		OrmValue ormValue = this.valueBulider.getSave(t);
		this.doSave(ormValue, t);
	}
	
	/**
	 * 保存多个对象
	 * @param list
	 */
	public void save(List<T> list){
		if(list == null || list.size() < 1) 
			return ;
		
		for(T t : list){
			OrmValue ormValue = this.valueBulider.getSave(t);
			this.doSave(ormValue, t);
		}
    }

	/**
	 * 对象保存
	 * @param ormValue
	 * @param t
	 * @date 2015年5月3日 上午11:26:10
	 */
	private void doSave(final OrmValue ormValue, T t){
		Object idVal = ReflectionUtil.getFieldValue(t, this.getOrmTable().getIdCol().getAttrName());
		if(idVal == null && DbKind.isMysql()){
			KeyHolder keyHolder = new GeneratedKeyHolder();
			this.jdbcTemplate.update(new PreparedStatementCreator(){

				@Override
				public PreparedStatement createPreparedStatement(Connection con) throws SQLException {
					PreparedStatement ps = con.prepareStatement(ormValue.getSql(), Statement.RETURN_GENERATED_KEYS);
					if(ormValue.getArgs() != null){
						int i=1;
						for(Object obj : ormValue.getArgs()){
							ps.setObject(i++, obj);
						}
					}
					return ps;
				}
				
			}, keyHolder);
			Field idFile = this.getOrmTable().getIdCol().getField();
			idFile.setAccessible(true);
			try {
				ReflectionUtil.writeField(idFile, t, keyHolder.getKey().longValue());
			} catch (Exception e) {
				logger.error("设置主键值异常", e);
			}
		}else{
			this.jdbcTemplate.update(ormValue.getSql(), ormValue.getArgs());
		}
	}
	
	/**
	 * 批量保存对象，效率更高，不会返回主键
	 * @param list
	 */
	public void saveBatch(List<T> list){
		if(list == null || list.size() < 1)
			return ;
		if(list.size() == 1){
			this.save(list.get(0));
			return ;
		}
		OrmValue ormValues = this.valueBulider.getSave(list);
		if(ormValues == null) 
			return ;
		
		this.batch(ormValues);
    }
	
	/**
	 * 更新对象
	 * @param t
	 */
	public void update(T t){
		OrmValue ormValues = this.valueBulider.getUpdate(t);
		if(ormValues == null) 
			return ;
		
		this.jdbcTemplate.update(ormValues.getSql(), ormValues.getArgs());
	}
	
	/**
	 * 更新多个对象
	 * @param list
	 */
	public void update(List<T> list){
		if(list == null || list.size() < 1)
			return ;
		
		OrmValue ormValues = null;
		for(T t : list){
			ormValues = this.valueBulider.getUpdate(t);
			if(ormValues == null) 
				return ;
			this.jdbcTemplate.update(ormValues.getSql(), ormValues.getArgs());
		}
	}
	
	/**
	 * 批量更新对象，效率更高
	 * @param list
	 */
	public void updateBatch(List<T> list){
		if(list == null || list.size() < 1) return ;
		if(list.size() == 1){
			this.update(list.get(0));
			return ;
		}
		
		OrmValue ormValues = this.valueBulider.getUpdate(list);
		if(ormValues == null) 
			return ;
		
		this.batch(ormValues);
	}
	
	/**
	 * 只对有值的部分进行修改
	 * @param t
	 */
	public void updateOnly(T t){
		OrmValue ormValues = this.valueBulider.getUpdateOnly(t);
		if(ormValues == null) 
			return ;
		
		this.jdbcTemplate.update(ormValues.getSql(), ormValues.getArgs());
	}
	
	/**
	 * 删除对象
	 * @param id
	 */
	public void delete(PK id){
		OrmValue ormValues = this.valueBulider.getDelete(getClazz(), id);
		if(ormValues == null) 
			return ;
		
		this.jdbcTemplate.update(ormValues.getSql(), ormValues.getArgs());
	}
	
	/**
	 * 删除多个对象
	 * @param ids
	 */
	public void delete(List<PK> ids) {
		if(ids == null || ids.size() < 1) return ;
		if(ids.size() == 1){
			this.delete(ids.get(0));
			return ;
		}
		OrmValue ormValues = this.valueBulider.getClear(getClazz(), ids);
		if(ormValues == null) 
			return ;
		
		this.batch(ormValues);
	}
	
	/**
	 * 按照实体条件删除记录
	 */
	public void deleteBy(T t) {
		OrmValue ormValues = this.valueBulider.getDeleteBy(t);
		if(ormValues == null) 
			return ;
		
		this.jdbcTemplate.update(ormValues.getSql(), ormValues.getArgs());
	}
	
	/**
	 * 获取对象
	 * @param id
	 * @return
	 */
	public T get(PK id){
		OrmValue ormValues = this.valueBulider.getGet(getClazz(), id);
		if(ormValues == null) return null;
		List<T> list = (List<T>)this.jdbcTemplate.query(ormValues.getSql(), ormValues.getArgs(), this.rowMapper);
		if(list == null || list.size() < 1) return null;
		return list.get(0);
	}
	
	/**
	 * 按对象条件获取一条记录
	 */
	@Override
	public T getObject(T t) {
		OrmValue ormValues = this.valueBulider.getQuery(t);
		if(ormValues == null) return null;
		List<T> rs = (List<T>)this.jdbcTemplate.query(ormValues.getSql(), ormValues.getArgs(), this.rowMapper);
		if(rs == null || rs.size() < 1)
			return null;
		return rs.get(0);
	}
	
	/**
	 * 返回单条记录
	 * @param sql
	 * @return
	 */
	public T getObject(String sql){
		return this.jdbcTemplate.queryForObject(sql, rowMapper);
	}
	
	/**
	 * 返回单条记录
	 * @param sql
	 * @param args
	 * @return
	 */
	public T getObject(String sql, Object...args){
		List<T> list = this.jdbcTemplate.query(sql, args, this.rowMapper);
		if(list == null || list.size() < 1)
			return null;
		return list.get(0);
	}
	
	/**
	 * 按条件等值查询对象
	 * @param t
	 * @return
	 */
	public List<T> query(T t){
		OrmValue ormValues = this.valueBulider.getQuery(t);
		if(ormValues == null) return null;
		return (List<T>)this.jdbcTemplate.query(ormValues.getSql(), ormValues.getArgs(), this.rowMapper);
	}
	
	/**
	 * 模糊查询匹配
	 * @param t
	 * @return
	 */
	public List<T> queryLike(T t){
		OrmValue ormValues = this.valueBulider.getQueryLike(t);
		if(ormValues == null) return null;
		return (List<T>)this.jdbcTemplate.query(ormValues.getSql(), ormValues.getArgs(), this.rowMapper);
	}
	
	/**
	 * 按sql返回查询对象
	 * @param sql
	 * @return
	 */
	public List<T> query(String sql){
		return this.jdbcTemplate.query(sql, this.rowMapper);
	}
	
	/**
	 * 返回多个记录
	 * @param sql
	 * @param args
	 * @return
	 */
	public List<T> query(String sql, Object...args){
		return this.jdbcTemplate.query(sql, args, this.rowMapper);
	}
	
	/**
	 * 获取所有记录
	 * @return
	 */
	public List<T> queryAll(){
		OrmValue ormValues = this.valueBulider.getAll(this.getClazz());
		if(ormValues == null) return null;
		return (List<T>)this.jdbcTemplate.query(ormValues.getSql(), this.rowMapper);
	}
	
	/**
	 * 分页查询
	 * @param page
	 */
	@SuppressWarnings("all")
	public void queryPage(Page page){
		String sql = page.getSql();
		String countSql = page.getCountSql();
		List<Object> params = page.getArgs();
		
		if(Strings.isBlank(sql)){
			this.createPageSql(page);
			sql = page.getSql();
		}
		
		if(page.getTotal() == null || page.getTotal() < 1){
			if(Strings.isBlank(countSql)){
				countSql = "select count(1) from ("+sql+") t_t";
			}
			logger.debug("ORM: 开始总数查询: {}\n参数：{}", countSql, params);
			page.setTotal(this.jdbcTemplate.queryForLong(countSql,params.toArray()));
		}
		
		if(page.isAutoPage()){
			if(DbKind.isMysql()){
				sql = "SELECT t_t.* FROM ( "+sql+" ) t_t LIMIT ?, ?";
				params.add((page.getPageNo()-1) * page.getPageSize());
				params.add(page.getPageSize());
			}else{
				sql = "select * from (select row_.*, rownum rownum_ from ( "+sql+" ) row_ where rownum <=?) where rownum_>=?";
				params.add((page.getPageNo()+1) * page.getPageSize());
				params.add(page.getPageNo() * page.getPageSize());
			}
		}
		
		logger.debug("ORM: 开始分页查询: {} \n参数：{}", sql, params);
		page.setDatas(this.jdbcTemplate.queryForList(sql, params.toArray()));
		
		//防止sql语句返回前台，导致安全问题
		page.setCountSql(null);
		page.setSql(null);
		page.getArgs().clear();
		page.getParam().clear();
		
		logger.debug("ORM: 结束分页查询，返回: {}", page.getDatas());
	}
	
	/**
	 * 创建分页sql语句，默认单表简单查询
	 * 
	 * 子类可以覆盖此方法
	 * @param page
	 */
	public void createPageSql(Page page){
		StringBuilder strb = new StringBuilder();
		if(page.getParam().size() > 0){
			for(Map.Entry<String, Object> entry : page.getParam().entrySet()){
				if(entry.getValue() != null){
					if((entry.getValue() instanceof String)){
						if(Strings.isNotBlank(entry.getValue().toString())){
							strb.append(" AND t.").append(entry.getKey()).append(" like ? ");
							page.addArg("%"+entry.getValue().toString().trim()+"%");;
						}
					}else{
						strb.append(" AND t.").append(entry.getKey()).append(" = ? ");
						page.addArg(entry.getValue());;
					}
				}
			}
		}
		
		OrmTable ormTable = getOrmTable();
		page.setCountSql("SELECT count(1) FROM " + ormTable.getTableDbName() +" t WHERE 1=1 "+strb.toString());
		
		if(page.getOrderColumn().size() < 1){
			strb.append(getOrmTable().getSqlOrderSuffix());
		}else{
			for(int i=0; i<page.getOrderColumn().size(); i++){
				strb.append(" ORDER BY t.").append(page.getOrderColumn().get(i)).append(page.getOrderAsc().get(i).booleanValue() ? " asc" : " desc");
				if(i != page.getOrderColumn().size()-1){
					strb.append(",");
				}
			}
		}
		
		page.setSql(this.getSqlHead().append(strb.toString()).toString());
	}
	
	/**
	 * 返回当前实体查找sql的前半部分
	 * @return
	 */
	public StringBuilder getSqlHead(){
		return new StringBuilder(getOrmTable().getSqlHeadPrefix());
	}
	
	/**
	 * 获取当前实体的数据库表名
	 * @return
	 */
	public String getTableName(){
		return getOrmTable().getTableDbName();
	}
	
	/**
	 * 获取当前实体对象封装体
	 * @return
	 */
	public OrmTable getOrmTable() {
		if(ormTable == null){
			synchronized (this) {
				if(ormTable == null){
					ormTable = this.ormContext.getOrmTable(clazz);
				}
			}
		}
		return ormTable;
	}

//--------------------------------------------------------------------------------------------------
	
	/**
	 * 
	 *功能说明: 批量处理
	 *创建人: 王金绍
	 *创建时间:2013-4-11 下午3:57:41
	 *@param ormValues
	 *@return int[]
	 *
	 */
	int[] batch(OrmValue ormValues){
		final String sql = ormValues.getSql();
		final List<Object[]> batchArgs = ormValues.getBatchArgs();
		final int[] argsType = ormValues.getArgTypes();
		final int argsNum = batchArgs.get(0).length;
		
		return this.jdbcTemplate.batchUpdate(sql, new BatchPreparedStatementSetter() {  
            @Override  
            public void setValues(PreparedStatement ps, int i) throws SQLException {
            	for(int j=1; j<=argsNum; j++){
            		if(argsType[j-1] == Types.DATE){
            			if(batchArgs.get(i)[j-1] != null){
            				Date date = (Date)batchArgs.get(i)[j-1];
            				ps.setTimestamp(j, new java.sql.Timestamp(date.getTime()));
            			}else{
            				ps.setTimestamp(j, null);
            			}
            			
            		}else{
            			ps.setObject(j, batchArgs.get(i)[j-1]);
            		}
            	}
            }  
            @Override  
            public int getBatchSize() {  
                return batchArgs.size();  
            }  
        });  
	}
	
	public Class<T> getClazz() {
		return clazz;
	}
	
	
	
	public void setOrmContext(OrmContext ormContext) {
		this.ormContext = ormContext;
		this.getOrmTable();
	}

	public RowMapper<T> getRowMapper() {
		return rowMapper;
	}

	public void setRowMapper(RowMapper<T> rowMapper) {
		this.rowMapper = rowMapper;
	}

	@Override
	public JdbcTemplate getJdbcTemplate() {
		return jdbcTemplate;
	}

	@Override
	public OrmContext getOrmContext() {
		return this.ormContext;
	}

	@Override
	@SuppressWarnings("all")
	public Integer queryForInt(String sql, Object... args) {
		if(Strings.isBlank(sql))
			return null;
		return this.jdbcTemplate.queryForInt(sql, args);
	}

	@Override
	@SuppressWarnings("all")
	public Long queryForLong(String sql, Object... args) {
		if(Strings.isBlank(sql))
			return null;
		return this.jdbcTemplate.queryForLong(sql, args);
	}

	@Override
	public List<Map<String, Object>> queryForList(String sql, Object... args) {
		if(Strings.isBlank(sql))
			return null;
		return this.jdbcTemplate.queryForList(sql, args);
	}


	@Override
	public Map<String, Object> queryForMap(String sql, Object... args) {
		if(Strings.isBlank(sql))
			return null;
		return this.jdbcTemplate.queryForMap(sql, args);
	}

	@Override
	public <E> List<E> queryOther(Class<E> clazz, String sql, Object... args) {
		if(Strings.isBlank(sql))
			return null;
		return this.jdbcTemplate.query(sql, args, this.getOrmContext().getRowMapper(clazz));
	}

	@Override
	public T newT() {
		T t = null;
		try {
			t = this.clazz.newInstance();
		} catch (Exception e) {
			throw new OrmException("实体类"+ormTable.getClass()+"默认构造方法不能访问！");
		}
		List<OrmColumn> valCols = this.getOrmTable().getValueCols();
		if(valCols != null && valCols.size() > 0){
			for(OrmColumn col : valCols){
				AnnotationUtil.setFieldValue(t, col.getField(), null);
			}
		}
		return t;
	}

	

}
