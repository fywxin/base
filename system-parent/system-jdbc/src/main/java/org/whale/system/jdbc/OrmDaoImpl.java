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
import org.springframework.jdbc.core.*;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.whale.system.base.BaseDao;
import org.whale.system.base.Q;
import org.whale.system.base.Iquery;
import org.whale.system.base.Page;
import org.whale.system.base.Iquery.SqlType;
import org.whale.system.common.exception.OrmException;
import org.whale.system.common.exception.StaleObjectStateException;
import org.whale.system.common.exception.SysException;
import org.whale.system.common.util.ReflectionUtil;
import org.whale.system.common.util.Strings;
import org.whale.system.jdbc.orm.OrmContext;
import org.whale.system.jdbc.orm.entry.OrmColumn;
import org.whale.system.jdbc.orm.entry.OrmTable;
import org.whale.system.jdbc.orm.entry.OrmValue;
import org.whale.system.jdbc.orm.rowMapper.RowMapperBuilder;
import org.whale.system.jdbc.orm.value.ValueBuilder;
import org.whale.system.jdbc.util.AnnotationUtil;
import org.whale.system.jdbc.util.DbKind;

import javax.annotation.Resource;

public class OrmDaoImpl<T extends Serializable,PK extends Serializable> implements IOrmDao<T, PK> {

	private static Logger logger = LoggerFactory.getLogger(BaseDao.class);
	
	//T类
	private Class<T> clazz;

	private RowMapperResultSetExtractor<T> oneRowMapperResultSetExtractor;

	private RowMapperResultSetExtractor<T> anyRowMapperResultSetExtractor;

	@Resource(name="jdbcTemplate")
	protected JdbcTemplate jdbcTemplate;
	@Autowired
	protected ValueBuilder valueBuilder;
	
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
	@Override
	public int save(T t){
		final OrmValue ormValue = this.valueBuilder.getSave(t);
		OrmColumn idCol = this._getOrmTable().getIdCol();
		Object idVal = ReflectionUtil.getFieldValue(t, idCol.getAttrName());

		boolean idIsEmpty = (idVal == null || ((idVal instanceof Number) && ((Number) idVal).intValue() == 0));
		if (!idCol.getIdAuto() && idIsEmpty){
			throw new OrmException("非自增主键，ID不能为空或0");
		}
		//防止id 被非空过滤器设置为0时，不去主动获取id
		if(idCol.getIdAuto() && idIsEmpty){
			if (DbKind.isMysql()){//mysql 获取主键
				KeyHolder keyHolder = new GeneratedKeyHolder();
				int i = this.jdbcTemplate.update(new PreparedStatementCreator() {

					@Override
					public PreparedStatement createPreparedStatement(Connection con) throws SQLException {
						PreparedStatement ps = con.prepareStatement(ormValue.getSql(), Statement.RETURN_GENERATED_KEYS);
						if (ormValue.getArgs() != null) {
							int i = 1;
							for (Object obj : ormValue.getArgs()) {
								ps.setObject(i++, obj);
							}
						}
						return ps;
					}

				}, keyHolder);

				if (ormTable.getUnique() && i == 0){
					logger.info("存在重复记录，该插入被忽略 {}", t);
					return 0;
				}

				Field idFile = idCol.getField();
				idFile.setAccessible(true);
				try {
					if (idCol.getType() == Types.BIGINT){
						ReflectionUtil.writeField(idFile, t, keyHolder.getKey().longValue());
					}else if (idCol.getType() == Types.INTEGER){
						ReflectionUtil.writeField(idFile, t, keyHolder.getKey().intValue());
					}else{
						logger.warn("自增主键类型存在问题，非Integer或Long");
						ReflectionUtil.writeField(idFile, t, keyHolder.getKey().shortValue());
					}
				} catch (Exception e) {
					logger.error("设置主键值异常", e);
				}
				return i;
			}else if(DbKind.isOracle()){
				return 0;
			}
		}else{
			return this.jdbcTemplate.update(ormValue.getSql(), ormValue.getArgs());
		}
		return 0;
	}
	
	/**
	 * 批量保存对象，效率更高，不会返回主键
	 * @param list
	 */
	@Override
	public int[] saveBatch(List<T> list){
		if(list == null || list.size() < 1)
			return new int[0];
		if(list.size() == 1){
			int[] intArr = new int[1];
			int i =this.save(list.get(0));
			intArr[0] = i;
			return intArr;
		}
		OrmValue ormValues = this.valueBuilder.getSave(list);
		if(ormValues == null)
			return new int[0];
		
		return this.batch(ormValues);
    }
	
	/**
	 * 更新对象
	 * @param t
	 */
	@Override
	public int update(T t){
		OrmValue ormValues = this.valueBuilder.getUpdate(t);
		if(ormValues == null) 
			return 0;
		
		int col = this.jdbcTemplate.update(ormValues.getSql(), ormValues.getArgs());
		//乐观锁， 锁已过期，更新记录数为0，抛出异常
		if(this._getOrmTable().getOptimisticLockCol() != null && col == 0){
			throw new StaleObjectStateException("乐观锁，当前版本 ["+AnnotationUtil.getFieldValue(t, this._getOrmTable().getOptimisticLockCol().getField())+"] 已过期，更新失败！");
		}
		return col;
	}
	
	@Override
	public int updateNotNull(T t) {
		OrmValue ormValue = this.valueBuilder.getUpdateNotNull(t);
		if(ormValue == null)
			return 0;
		int col = this.jdbcTemplate.update(ormValue.getSql(), ormValue.getArgs());
		//乐观锁， 锁已过期，更新记录数为0，抛出异常
		if(this._getOrmTable().getOptimisticLockCol() != null && col == 0){
			throw new StaleObjectStateException("乐观锁，当前版本 ["+AnnotationUtil.getFieldValue(t, this._getOrmTable().getOptimisticLockCol().getField())+"] 已过期，更新失败！");
		}
		return col;
	}
	
	/**
	 * 批量更新对象，效率更高
	 * @param list
	 */
	@Override
	public int[] updateBatch(List<T> list){
		if(list == null || list.size() < 1) return new int[0];
		if(list.size() == 1){
			int[] intArr = new int[1];
			int i = this.update(list.get(0));
			intArr[0] = i;
			return intArr;
		}
		
		OrmValue ormValues = this.valueBuilder.getUpdate(list);
		if(ormValues == null)
			return new int[0];
		
		//乐观锁， 锁已过期，更新记录数为!=1，抛出异常
		int[] rs = this.batch(ormValues);
		for(int i : rs){
			if(i != 1){
				throw new StaleObjectStateException("乐观锁，批量更新失败！");
			}
		}
		return rs;
	}
	
	/**
	 * 删除对象
	 * @param id
	 */
	@Override
	public int delete(PK id){
		OrmValue ormValues = this.valueBuilder.getDelete(getClazz(), id);
		if(ormValues == null) 
			return 0;
		
		return this.jdbcTemplate.update(ormValues.getSql(), ormValues.getArgs());
	}
	
	/**
	 * 删除多个对象
	 * @param ids
	 */
	@Override
	public int[] deleteBatch(List<PK> ids) {
		if(ids == null || ids.size() < 1) return new int[0];
		if(ids.size() == 1){
			int[] intArr = new int[1];
			int i = this.delete(ids.get(0));
			intArr[0] = i;
			return intArr;
		}
		OrmValue ormValues = this.valueBuilder.getClear(getClazz(), ids);
		if(ormValues == null)
			return new int[0];
		
		return this.batch(ormValues);
	}
	
	/**
	 * 按自定义条件删除
	 */
	@Override
	public int delete(Iquery query){
		return this.jdbcTemplate.update(query.getSql(SqlType.DEL), query.getArgs());
	}
	
	/**
	 * 获取对象
	 * @param id
	 * @return
	 */
	@Override
	public T get(PK id){
		OrmValue ormValues = this.valueBuilder.getGet(getClazz(), id);
		if(ormValues == null) return null;
		List<T> list = this.jdbcTemplate.query(ormValues.getSql(), ormValues.getArgs(), oneRowMapperResultSetExtractor);
		if(list == null || list.size() < 1) return null;
		return list.get(0);
	}
	
	/**
	 * 按自定义条件获取
	 */
	@Override
	public T get(Iquery query){
		List<T> list = this.jdbcTemplate.query(query.getSql(SqlType.GET), query.getArgs(), oneRowMapperResultSetExtractor);
		
		if(list == null || list.size() < 1)
			return null;
		return list.get(0);
	}

	/**
	 * 获取所有记录
	 * @return
	 */
	@Override
	public List<T> queryAll(){
		OrmValue ormValues = this.valueBuilder.getAll(this.getClazz());
		if(ormValues == null) return null;
		return this.jdbcTemplate.query(ormValues.getSql(), anyRowMapperResultSetExtractor);
	}
	
	/**
	 * 按自定义条件查询
	 */
	@Override
	public List<T> query(Iquery query){
		return this.jdbcTemplate.query(query.getSql(SqlType.QUERY), query.getArgs(), anyRowMapperResultSetExtractor);
	}
	
	/**
	 * 分页查询
	 * @param page
	 */
	@Override
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
					countSql = "select count(*) "+sql.substring(index);
				}else{
					countSql = "select count(*) from ("+sql+")";
				}
			}
			page.setTotal(this.jdbcTemplate.queryForObject(countSql, params.toArray(), Long.class));
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
			//未指定返回类型，则采用List<Map, Object>模式返回
			if (page.getDataClass() == null){
				page.setData((List<Map<String, Object>>)this.jdbcTemplate.query(sql, params.toArray(), new RowMapperResultSetExtractor(new ColumnMapRowMapper(), page.getPageSize())));
			//指定返回类型，通常用于接口，这直接返回该类型结果
			}else{
				if (page.getDataClass().equals(this.getClazz())){
					page.setData((List<?>) this.jdbcTemplate.query(sql, params.toArray(), page.getPageSize() > 50 ? anyRowMapperResultSetExtractor : new RowMapperResultSetExtractor(RowMapperBuilder.get(page.getDataClass()), page.getPageSize())));
				}else{
					page.setData(this.jdbcTemplate.queryForList(sql, params.toArray()));
				}			}
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
		page.setQ(null);
		page.setDataClass(null);
	}

	/**
	 * 记录总数
	 * @param query
	 * @return
	 */
	@Override
	public Integer count(Iquery query) {
		return this.jdbcTemplate.queryForObject(query.getSql(SqlType.COUNT), query.getArgs(), Integer.class);
	}

	/**
	 * 数据库是否存在本记录
	 * @param id 记录ID
	 * @return
	 */
	@Override
	public boolean contain(PK id){
		return this.count(this.q().eq(this._getOrmTable().getIdCol().getSqlName(), id)) > 0;
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
	 *创建人: wjs
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

	public void setRowMapper(RowMapper<T> rowMapper) {
		this.oneRowMapperResultSetExtractor = new RowMapperResultSetExtractor(rowMapper, 1);
		this.anyRowMapperResultSetExtractor = new RowMapperResultSetExtractor(rowMapper);
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
	public Q q() {
		return Q.newQ(clazz);
	}

}
