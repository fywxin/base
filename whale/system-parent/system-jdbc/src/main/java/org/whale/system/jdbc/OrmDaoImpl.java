package org.whale.system.jdbc;

import java.io.Serializable;
import java.lang.reflect.Field;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Types;
import java.util.ArrayList;
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
import org.whale.system.base.Cmd;
import org.whale.system.base.Iquery;
import org.whale.system.base.Page;
import org.whale.system.base.Iquery.SqlType;
import org.whale.system.common.exception.StaleObjectStateException;
import org.whale.system.common.exception.SysException;
import org.whale.system.common.util.ReflectionUtil;
import org.whale.system.common.util.Strings;
import org.whale.system.jdbc.orm.OrmContext;
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
		final OrmValue ormValue = this.valueBulider.getSave(t);
		Object idVal = ReflectionUtil.getFieldValue(t, this._getOrmTable().getIdCol().getAttrName());
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
			Field idFile = this._getOrmTable().getIdCol().getField();
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
		
		int col = this.jdbcTemplate.update(ormValues.getSql(), ormValues.getArgs());
		//乐观锁， 锁已过期，更新记录数为0，抛出异常
		if(this._getOrmTable().getOptimisticLockCol() != null && col == 0){
			throw new StaleObjectStateException("乐观锁，当前版本 ["+AnnotationUtil.getFieldValue(t, this._getOrmTable().getOptimisticLockCol().getField())+"] 已过期，更新失败！");
		}
	}
	
	@Override
	public void updateNotNull(T t) {
		OrmValue ormValue = this.valueBulider.getUpdateNotNull(t);
		if(ormValue == null)
			return ;
		int col = this.jdbcTemplate.update(ormValue.getSql(), ormValue.getArgs());
		//乐观锁， 锁已过期，更新记录数为0，抛出异常
		if(this._getOrmTable().getOptimisticLockCol() != null && col == 0){
			throw new StaleObjectStateException("乐观锁，当前版本 ["+AnnotationUtil.getFieldValue(t, this._getOrmTable().getOptimisticLockCol().getField())+"] 已过期，更新失败！");
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
		
		//乐观锁， 锁已过期，更新记录数为!=1，抛出异常
		int[] rs = this.batch(ormValues);
		for(int i : rs){
			if(i != 1){
				throw new StaleObjectStateException("乐观锁，批量更新失败！");
			}
		}
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
	public void deleteBatch(List<PK> ids) {
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
	 * 按自定义条件删除
	 */
	public void delete(Iquery query){
		this.jdbcTemplate.update(query.getSql(SqlType.DEL), query.getArgs());
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
	 * 按自定义条件获取
	 */
	public T get(Iquery query){
		List<T> list = this.jdbcTemplate.query(query.getSql(SqlType.GET), query.getArgs(), this.rowMapper);
		
		if(list == null || list.size() < 1)
			return null;
		return list.get(0);
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
	 * 按自定义条件查询
	 */
	public List<T> query(Iquery query){
		
		return this.jdbcTemplate.query(query.getSql(SqlType.QUERY), query.getArgs(), this.rowMapper);
	}
	
	/**
	 * 分页查询
	 * @param page
	 */
	@SuppressWarnings("all")
	public void queryPage(Page page){
		String sql = page.sql();
		String countSql = page.countSql();
		List<Object> params = page.args();
		
		if(Strings.isBlank(sql)){
			throw new SysException("Page 分页语句为空");
		}
		
		if(page.getTotal() == null || page.getTotal() < 1){
			if(Strings.isBlank(countSql)){
				int index = -1;
				String temp = sql.toLowerCase();
				//出现多个from值时，采用保守方式获取总记录数，但会降低效率
				//TODO 是否要移除order by
				if(temp.substring((index = temp.lastIndexOf("from"))+5).lastIndexOf("from") == -1){
					countSql = "select count(1) "+sql.substring(index);
				}else{
					countSql = "select count(1) from ("+sql+")";
				}
			}
			page.setTotal(this.jdbcTemplate.queryForLong(countSql,params.toArray()));
		}
		
		if(page.getTotal() > 0){
			if(DbKind.isMysql()){
				sql += " LIMIT ?, ?";
				if(page.getOffset() == null){
					params.add((page.getPageNo()-1) * page.getPageSize());
				}else{
					params.add(page.getOffset());
				}
				params.add(page.getPageSize());
			}else{
				sql = "select * from (select row_.*, rownum rownum_ from ( "+sql+" ) row_ where rownum <=?) where rownum_>=?";
				if(page.getOffset() == null){
					params.add((page.getPageNo()+1) * page.getPageSize());
					params.add(page.getPageNo() * page.getPageSize());
				}else{
					params.add(page.getOffset()+page.getPageSize());
					params.add(page.getOffset());
				}
				
			}
			page.setData(this.jdbcTemplate.queryForList(sql, params.toArray()));
		}else{
			page.setData(new ArrayList(0));
		}
		
		if(logger.isDebugEnabled()){
			logger.debug(page.toString());
		}
		
		//防止sql语句返回前台，导致安全问题
		page.setCountSql(null);
		page.setSql(null);
		page.args().clear();
		page.setCmd(null);
	}

	@Override
	@SuppressWarnings("all")
	public Integer count(Iquery query) {
		return this.jdbcTemplate.queryForInt(query.getSql(SqlType.COUNT), query.getArgs());
	}
	
//--------------------------------------内部方法-----------------------------------------------
	
	/**
	 * 获取当前实体对象封装体
	 * @return
	 */
	public OrmTable _getOrmTable() {
		if(ormTable == null){
			synchronized (this) {
				if(ormTable == null){
					ormTable = this.ormContext.getOrmTable(clazz);
				}
			}
		}
		return ormTable;
	}
	
	@Override
	public OrmContext _getOrmContext() {
		return this.ormContext;
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
		this._getOrmTable();
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
	public List<Map<String, Object>> queryForList(Iquery query) {
		if(Strings.isBlank(query.getSql(SqlType.QUERY)))
			return null;
		return this.jdbcTemplate.queryForList(query.getSql(SqlType.QUERY), query.getArgs());
	}


	@Override
	public Map<String, Object> queryForMap(Iquery query) {
		if(Strings.isBlank(query.getSql(SqlType.QUERY)))
			return null;
		return this.jdbcTemplate.queryForMap(query.getSql(SqlType.QUERY), query.getArgs());
	}
	
	/**
	 * 取得当前对象的select t.* from db t
	 * 
	 * @return
	 * 2015年6月14日 上午7:57:01
	 
	public String sqlHead(){
		return _getOrmTable().getSqlHeadPrefix();
	}
	*/
	/**
	 * 取得当前对象的 order by t.x
	 * 
	 * @return
	 * 2015年6月14日 上午7:57:01
	 
	public String sqlOrder(){
		return this._getOrmTable().getSqlOrderSuffix();
	}
*/
	@Override
	public Cmd cmd() {
		return Cmd.newCmd(clazz);
	}

	

	
}
