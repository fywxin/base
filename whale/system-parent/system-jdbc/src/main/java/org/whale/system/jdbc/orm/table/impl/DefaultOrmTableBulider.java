package org.whale.system.jdbc.orm.table.impl;

import java.lang.reflect.Type;
import java.sql.Types;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.OrderComparator;
import org.springframework.stereotype.Component;
import org.whale.system.annotation.jdbc.Column;
import org.whale.system.annotation.jdbc.Id;
import org.whale.system.annotation.jdbc.OptimisticLock;
import org.whale.system.annotation.jdbc.Order;
import org.whale.system.annotation.jdbc.Sql;
import org.whale.system.annotation.jdbc.Table;
import org.whale.system.annotation.jdbc.Table.ColumnFormat;
import org.whale.system.annotation.jdbc.UnColumn;
import org.whale.system.annotation.jdbc.Validate;
import org.whale.system.common.exception.OrmException;
import org.whale.system.common.util.Strings;
import org.whale.system.jdbc.orm.EntryContext;
import org.whale.system.jdbc.orm.entry.Acolumn;
import org.whale.system.jdbc.orm.entry.Atable;
import org.whale.system.jdbc.orm.entry.OrmColumn;
import org.whale.system.jdbc.orm.entry.OrmColumnSql;
import org.whale.system.jdbc.orm.entry.OrmOrder;
import org.whale.system.jdbc.orm.entry.OrmTable;
import org.whale.system.jdbc.orm.entry.OrmValidate;
import org.whale.system.jdbc.orm.event.OrmColumnGenEvent;
import org.whale.system.jdbc.orm.event.OrmEventMuliter;
import org.whale.system.jdbc.orm.event.OrmTableGenEvent;
import org.whale.system.jdbc.orm.table.ColumnExtInfoReader;
import org.whale.system.jdbc.orm.table.OrmTableBulider;
import org.whale.system.jdbc.orm.table.TableExtInfoReader;
import org.whale.system.jdbc.util.AnnotationUtil;
import org.whale.system.jdbc.util.DbKind;

/**
 * 从实体中解析ORM元注释，构建OrmTable对象
 *
 * @author 王金绍
 * 2014年9月6日-下午1:59:55
 */
@Component
public class DefaultOrmTableBulider implements OrmTableBulider {
	
	private static Logger logger = LoggerFactory.getLogger(DefaultOrmTableBulider.class);
	
	@Autowired
	private CamelTableNameParser camelTableNameParser;
	@Autowired
	private SameTableNameParser sameTableNameParser;
	@Autowired
	private Camel2SqlUpperColumnNameParser camel2SqlUpperColumnNameParser;
	@Autowired
	private Camel2SqlLowerColumnNameParser camel2SqlLowerColumnNameParser;
	@Resource(name="entryContext")
	private EntryContext entryContext;
	@Autowired
	private TableBulider tableBulider;
	@Autowired
	private OrmEventMuliter omEventMuliter;
	
	//------------------------------------外部扩展实现-------------------------
	@Autowired(required=false)
	private List<TableExtInfoReader> tableExtInfoReaders;
	@Autowired(required=false)
	private List<ColumnExtInfoReader> columnExtInfoReaders;
	
	private boolean sort = true;
	
	
	/**
	 * 
	 *功能说明: 解析实体类元注释，生成OrmTable对象
	 *创建人:王金绍
	 *创建时间:2013-3-28 上午9:09:51
	 *@param clazz
	 *@return
	 *
	 */
	@Override
	public OrmTable parse(Class<?> clazz) {
		Atable table = this.entryContext.getTable(clazz);
		if(table == null){
			table = this.tableBulider.parse(clazz);
			this.entryContext.putTable(clazz, table);
		}
		OrmTable ormTable = new OrmTable(table); //设置类基本属性
		logger.info("ORM:设置类clazz={}基本属性完成, aCols={}", clazz,ormTable.getCols());
		this.exeTableExtInfoReaders(ormTable); //设置数据库，序列，拥有者
		logger.info("ORM:设置类clazz={}设置数据库，序列，拥有者完成, tableDbName={}, tableCnName={}", clazz, ormTable.getTableDbName(), ormTable.getTableCnName());
		List<OrmColumn> cols = this.getColumns(ormTable); //获取所有@Column
		ormTable.setOrmCols(cols); //设置字段
		logger.info("ORM:转换 类clazz={} Acolumn->OrmColumn 完成，ormCols={}", clazz, cols);
		this.reParseTable(ormTable); //设置idCol,pkCols
		logger.info("ORM:提取 类clazz={} ID字段完成 idColumn={}", clazz, ormTable.getIdCol());
		this.exeTableExtInfoReaders(ormTable);
		logger.info("ORM:执行 类table={} 扩展信息读取完成:{}", ormTable.getIdCol(), ormTable);
		return ormTable;
	}
	
	/**
	 * 绑定事件 
	 * @param ormTable
	 */
	private void fireAndParseTable(OrmTable ormTable){
		OrmTableGenEvent ormTableGenEvent = new OrmTableGenEvent(this, ormTable, true, false);
		this.omEventMuliter.multicater(ormTableGenEvent);
		
		this.parseTable(ormTable);
		
		OrmTableGenEvent ormTableGenEventAfter = new OrmTableGenEvent(this, ormTable, false, true);
		this.omEventMuliter.multicater(ormTableGenEventAfter);
	}
	/**
	 * 
	 *功能说明: 封装table
	 *创建人: 王金绍
	 *创建时间:2013-3-19 下午12:44:08
	 *@param clazz
	 *@param ormTable void
	 *
	 */
	private void parseTable(OrmTable ormTable){
		Table table = (Table)ormTable.getClazz().getAnnotation(Table.class);
		if(table == null)
			throw new OrmException(ormTable.getEntityName() +" 找不到@Table 注释");
		//数据库表名
		if(Strings.isBlank(table.value())){
			if(DbKind.isMysql()){
				ormTable.setTableDbName(this.sameTableNameParser.getDbTableName(ormTable.getEntityName()));
			}else{
				ormTable.setTableDbName(this.camelTableNameParser.getDbTableName(ormTable.getEntityName()));
			}
		}else{
			//TODO oracle 与 mysql 表名 大小写是否有要求
			//ormTable.setTableDbName(table.value().trim().toUpperCase());
			ormTable.setTableDbName(table.value().trim());
		}
		
		//数据库序列
		if(Strings.isBlank(table.sequence())){
			//mysql 无序列
			ormTable.setSequence(this.camelTableNameParser.getDbSequence(ormTable.getEntityName()));
		}else{
			ormTable.setSequence(table.sequence().trim().toUpperCase());
		}
		//数据库拥有者
		if(Strings.isNotBlank(table.schema())){
			ormTable.setTableSchemaOwner(table.schema().trim());
		}
		if(Strings.isNotBlank(table.cnName())){
			ormTable.setTableCnName(table.cnName());
		}
		//当javabean 字段没有定义@Column是，javabean属性转为数据库字段的规则
		ormTable.setColumnFormat(table.colFormat());
		while (ormTable.getParent() != null) {
			ormTable.getParent().setColumnFormat(table.colFormat());
		}
	}
	
	/**
	 * 
	 *功能说明: 获取所有的@Column 列表
	 *创建人: 王金绍
	 *创建时间:2013-3-19 下午1:44:18
	 *@param table
	 *@return List<OrmColumn>
	 *
	 */
	private List<OrmColumn> getColumns(Atable table){
		//表中定义的所有字段
		List<Acolumn> acolumns = table.getCols();
		List<OrmColumn> list = new ArrayList<OrmColumn>(acolumns.size());
		OrmColumn ormColumn = null;
		
		boolean hasColAnnotation = this.searchColumnAnnotation(table);
		
		for(Acolumn acolumn : acolumns){
			ormColumn = this.fireAndParseColumn(table, acolumn, hasColAnnotation);
			if(ormColumn == null)
				continue;
			list.add(ormColumn);
		}
		
		//获取父对象，向上递归解析字段结构
		if(table.getParent() != null){
			List<OrmColumn> superList = getColumns(table.getParent());
			if(superList != null && superList.size() > 0){
				list.addAll(superList);
			}
		}
		
		return list;
	}
	
	/**
	 * JavaBean 中所有属性 是否存在@Column 标签
	 * 有，则  @Column 字段为数据库字段
	 * 否，则 除 @UnColumn 外的java字段为数据库字段
	 * @param table
	 * @return
	 */
	private boolean searchColumnAnnotation(Atable table){
		List<Acolumn> acolumns = table.getCols();
		for(Acolumn acolumn : acolumns){
			if(acolumn.getField().getAnnotation(Column.class) != null){
				return true;
			}
		}
		return false;
	}
	
	/**
	 * 绑定OrmColumn创建事件
	 * @param table
	 * @param acolumn
	 * @return
	 * @Date 2015年3月9日 下午4:34:07
	 */
	private OrmColumn fireAndParseColumn(Atable table, Acolumn acolumn, boolean hasColAnnotation){
		OrmColumnGenEvent beforeOrmColumnGenEvent = new OrmColumnGenEvent(this, table, acolumn, null, true, false);
		this.omEventMuliter.multicater(beforeOrmColumnGenEvent);
		
		OrmColumn ormColumn = null;
		if(hasColAnnotation){ //存在 @Column 则采用默认规则
			ormColumn = this.parseColumnWithAnnotation(table, acolumn);
		}else{//不存在 则所有非 @UnColumn的都ok
			ormColumn = this.parseColumnUnAnnotation(table, acolumn);
		}
		
		OrmColumnGenEvent afterOrmColumnGenEvent = new OrmColumnGenEvent(this, table, acolumn, ormColumn, false, true);
		this.omEventMuliter.multicater(afterOrmColumnGenEvent);
		
		return ormColumn;
	}
	
	/**
	 * 创建OrmColumn 对象 JavaBean不存在 @Column 标签
	 * @param table
	 * @param acolumn
	 * @return
	 */
	private OrmColumn parseColumnUnAnnotation(Atable table, Acolumn acolumn){
		UnColumn unColumn = acolumn.getField().getAnnotation(UnColumn.class);
		if(unColumn != null){
			return null;
		}
		
		OrmColumn ormColumn = new OrmColumn(acolumn);
		
		ormColumn.setSqlName(this.parseDbColumnName(acolumn.getAttrName(), table.getColumnFormat()));
		ormColumn.setCnName(acolumn.getAttrName());
		ormColumn.setUnique(true);
		ormColumn.setUpdateAble(false);
		//字段类型
		ormColumn.setType(this.getFieldType(acolumn));

		this.readOtherAnnotation(table, acolumn, ormColumn);
		
		return ormColumn;
	}
	
	/**
	 * 创建OrmColumn 对象 JavaBean存在 @Column 标签
	 * @param table
	 * @param acolumn
	 * @return
	 * @Date 2015年3月9日 下午4:34:59
	 */
	private OrmColumn parseColumnWithAnnotation(Atable table, Acolumn acolumn){
		Column column = acolumn.getField().getAnnotation(Column.class);
		UnColumn unColumn = acolumn.getField().getAnnotation(UnColumn.class);
		if(unColumn != null){
			throw new OrmException("实体 ["+table.getEntityName()+"]中字段["+acolumn.getAttrName()+"]不能同时包含 @Column 与 @UnColumn 标签");
		}
		
		OrmColumn ormColumn = new OrmColumn(acolumn);
		//字段数据库名
		ormColumn.setSqlName(Strings.isBlank(column.name()) ? acolumn.getAttrName() : column.name().trim());
		//字段中文名
		if(Strings.isNotBlank(column.cnName())){
			ormColumn.setCnName(column.cnName().replaceAll("'", "").replaceAll("\"", ""));
			acolumn.setCnName(column.cnName().replaceAll("'", "").replaceAll("\"", ""));
		}else{
			ormColumn.setCnName(ormColumn.getSqlName());
			acolumn.setCnName(ormColumn.getSqlName());
		}
		
		//建表和判断数据是否超过大小时有用
//		ormColumn.setWidth(column.width());
//		ormColumn.setPrecision(column.precision());
		
		//字段约束
		ormColumn.setUnique(!column.unique());
//		ormColumn.setNullAble(column.nullable());
		ormColumn.setUpdateAble(column.updateable());
		ormColumn.setDefaultValue("".equals(column.defaultValue()) ?column.defaultValue() : null);
		//字段类
		ormColumn.setType(this.getFieldType(acolumn));
		
		this.readOtherAnnotation(table, acolumn, ormColumn);
		
		return ormColumn;
	}
	
	/**
	 * 读取其他非 @Column 的字段元注释
	 * 包括 @Id @Sql @Order @OrmValidate @OptimisticLock 和扩展信息读取
	 * @param table
	 * @param acolumn
	 * @param ormColumn
	 */
	private void readOtherAnnotation(Atable table, Acolumn acolumn, OrmColumn ormColumn){
		//主键
		Id id = acolumn.getField().getAnnotation(Id.class);
		if(id != null){
			ormColumn.setIsId(true);
			ormColumn.setIdAuto(id.auto());
		}
		
		//字段附加SQL定义
		Sql sql = acolumn.getField().getAnnotation(Sql.class);
		if(sql != null){
			if(Strings.isNotBlank(sql.value())) {
				this.setColumnSql(ormColumn, sql);
			}
		}
		
		//读取设置@Order
		this.setColumnOrder(ormColumn);
		
		//读取设置OrmValidate
		this.setColumnValidate(ormColumn);
		
		//读取OrmColumn 扩展信息读取
		this.extColumnExtInfoReaders(ormColumn);
		
		//是否为乐观锁字段信息读取
		OptimisticLock optimisticLock = acolumn.getField().getAnnotation(OptimisticLock.class);
		if(optimisticLock != null){
			Type type = acolumn.getAttrType();
			String t = type.toString();
			if(t.equals("class java.lang.String")){
				try{
					Object obj = table.getClazz().newInstance();
					
					if(AnnotationUtil.getFieldValue(obj, acolumn.getField()) != null){
						throw new OrmException(table.getEntityName() +" 乐观锁 "+ormColumn.getAttrName()+" 字段类型定义初始值必须为null");
					}
					ormColumn.setIsOptimisticLock(true);
				} catch(Exception e) {
					throw new OrmException("实体类"+table.getClass()+"默认构造方法不能访问！", e);
				} 
			}else{
				throw new OrmException(table.getEntityName() +" 乐观锁 "+ormColumn.getAttrName()+" 字段类型必须为包装类型 Long 或 Integer");
			}
		}
	}
	
	/**
	 * 读取保存校验信息
	 * 
	 * @param ormColumn
	 * @date 2015年3月9日 下午1:55:43
	 */
	private void setColumnValidate(OrmColumn ormColumn){
		Validate valid = ormColumn.getField().getAnnotation(Validate.class);
		if(valid != null){
			OrmValidate ormValidate = new OrmValidate();
			ormValidate.setAccount(valid.account());
			ormValidate.setChinese(valid.chinese());
			ormValidate.setCustom(valid.custom());
			ormValidate.setEl(valid.el());
			ormValidate.setEmail(valid.email());
			ormValidate.setEnums(valid.enums());
			ormValidate.setErrorMsg(valid.errorMsg());
			ormValidate.setLimit(valid.limit());
			ormValidate.setMobile(valid.mobile());
			ormValidate.setPost(valid.post());
			ormValidate.setQq(valid.qq());
			ormValidate.setRegex(valid.regex());
			ormValidate.setRepeat(valid.repeat());
			ormValidate.setRequired(valid.required());
			ormValidate.setStrLen(valid.strLen());
			ormValidate.setUrl(valid.url());
			ormValidate.setIp(valid.ip());
			ormValidate.setOrmColumn(ormColumn);
			ormColumn.setOrmValidate(ormValidate);
		}
	}
	
	/**
	 * 读取@Order标签
	 * @date 2015年1月5日 下午8:03:43
	 */
	private void setColumnOrder(OrmColumn ormColumn){
		Order order = ormColumn.getField().getAnnotation(Order.class);
		if(order != null){
			OrmOrder ormOrder = new OrmOrder();
			ormOrder.setAsc(order.asc());
			ormOrder.setIndex(order.index());
			ormColumn.setOrmOrder(ormOrder);
		}
	}
	
	/**
	 * 
	 *功能说明:设置状态字段
	 *创建人: 王金绍
	 *创建时间:2013-4-23 下午1:38:26
	 *@param ormColumn void
	 *
	 */
	private void setColumnSql(OrmColumn ormColumn, Sql sql){
		OrmColumnSql ormColumnSql = new OrmColumnSql();
		ormColumnSql.setSql(sql.value());
		ormColumnSql.setIsPre(sql.isPre());
		ormColumnSql.setReplaceWhenNull(sql.onlyNull());
		ormColumn.setOrmColumnSql(ormColumnSql);
	}
	
	/**
	 * 
	 *功能说明: 设置 ormTable 中的 idCol 和 pkCols
	 *			检查乐观锁字段是否超过1个
	 *创建人: 王金绍
	 *创建时间:2013-3-19 下午1:59:36
	 *@param ormTable void
	 *
	 */
	private void reParseTable(OrmTable ormTable){
		List<OrmColumn> cols = ormTable.getOrmCols();
		List<OrmColumn> orderCols = new ArrayList<OrmColumn>(1);
		
//		/**非空检查字段集合 */
//		List<OrmColumn> notNullCheckCols = new ArrayList<OrmColumn>();
		/**唯一性检查字段集合 */
		List<OrmColumn> uniqueCheckCols = new ArrayList<OrmColumn>();
		/**预先执行SQL集合 */
		List<OrmColumn> sqlExecCols = new ArrayList<OrmColumn>();
		/**带检查字段集合 */
		List<OrmColumn> validateCols = new ArrayList<OrmColumn>();
		
		/**java字段名对应数据库字段名 */
		Map<String, String> javaAsSqlColumn = new HashMap<String, String>();
		/**数据库字段名对应java字段名 */
		Map<String, String> sqlAsJavaColumn = new HashMap<String, String>();
		
		OrmColumn idCol = null;
		for(OrmColumn col : cols){
			javaAsSqlColumn.put(col.getAttrName(), col.getSqlName());
			sqlAsJavaColumn.put(col.getSqlName(), col.getAttrName());
			
			//排序字段
			if(col.getOrmOrder() != null){
				orderCols.add(col);
			}
			//乐观锁字段 检测设置
			if(col.getIsOptimisticLock()){
				if(ormTable.getOptimisticLockCol() != null){
					throw new OrmException("实体 ["+ormTable.getEntityName()+"] 存在多个@OptimisticLock 乐观锁字段");
				}else{
					ormTable.setOptimisticLockCol(col);
				}
			}
			
			if(col.getIsId()){
				if(idCol != null)
					throw new OrmException("实体 ["+ormTable.getEntityName()+"] 存在多个@id主键字段");
				idCol = col;
			}else{
				if(col.getUnique()){
					uniqueCheckCols.add(col);
				}
//				if(!col.getNullAble()){
//					notNullCheckCols.add(col);
//				}
				if(col.getOrmColumnSql() != null){
					sqlExecCols.add(col);
				}
				if(col.getOrmValidate() != null){
					validateCols.add(col);
				}
			}
		}
		
		//设置排序字段集合
		if(orderCols.size() > 0){
			Collections.sort(orderCols, new Comparator<OrmColumn>() {
				@Override
				public int compare(OrmColumn o1, OrmColumn o2) {
					return o1.getOrmOrder().getIndex() - o2.getOrmOrder().getIndex();
				}
			});
			ormTable.setOrderCols(orderCols);
		}
		
		//单个@Id主键定义
		if(idCol != null){
			ormTable.setIdCol(idCol);
			//主键采用外部赋值策略
			if(!idCol.getIdAuto()){
				logger.warn("实体 [{}] 主键采用外部赋值策略, 舍弃序列[{}]",  ormTable.getEntityName(), ormTable.getSequence());
				ormTable.setSequence(null);
			}
		}else{
			throw new OrmException("没有找到主键定义");
		}
		
//		//设置 非空检查字段集合，唯一性检查字段集合， 预先执行SQL集合
//		if(notNullCheckCols.size() > 0){
//			ormTable.setNotNullCheckCols(notNullCheckCols);
//		}
		if(uniqueCheckCols.size() > 0){
			ormTable.setUniqueCheckCols(uniqueCheckCols);
		}
		if(sqlExecCols.size() > 0){
			ormTable.setSqlExecCols(sqlExecCols);
		}
		if(validateCols.size() > 0){
			ormTable.setValidateCols(validateCols);
		}
		ormTable.setJavaAsSqlColumn(javaAsSqlColumn);
		ormTable.setSqlAsJavaColumn(sqlAsJavaColumn);
		
		//读取该类存在默认值的字段集合
		this.readValCols(ormTable);
	}
	
	/**
	 * 获取初始值非空的字段
	 * 2015-4-30 下午3:42:18
	 * @param ormTable
	 */
	private void readValCols(OrmTable ormTable){
		try {
			Object obj = ormTable.getClazz().newInstance();
			List<OrmColumn> hasValCols = new ArrayList<OrmColumn>();
			
			List<OrmColumn> cols = ormTable.getOrmCols();
			Object rs = null;
			for(OrmColumn col : cols){
				rs = AnnotationUtil.getFieldValue(obj, col.getField());
				if(rs != null){
					hasValCols.add(col);
					col.setValue(rs);//获取其默认值
				}
			}
			if(hasValCols.size() > 0){
				ormTable.setValueCols(hasValCols);
			}
		} catch (Exception e) {
			throw new OrmException("实体类"+ormTable.getClass()+"默认构造方法不能访问！", e);
		} 
	}
	
	/**
	 * 
	 *功能说明: 如果@column为定义name，则安装规则器从字段中生成
	 *创建人: 王金绍
	 *创建时间:2013-3-19 下午1:42:18
	 *@param fieldName
	 *@param column
	 *@param entryName
	 *@return String
	 *
	 */
	private String parseDbColumnName(String fieldName, ColumnFormat columnFormat){
		//默认驼峰字段转换规则
		if(ColumnFormat.CAMEL2UNDERLINE_LOWER.equals(columnFormat)){
			return this.camel2SqlLowerColumnNameParser.getDbColName(fieldName);
		}else if(ColumnFormat.CAMEL2UNDERLINE_UPPER.equals(columnFormat)){
			return this.camel2SqlUpperColumnNameParser.getDbColName(fieldName);
		}else{
			return fieldName;
		}
		
	}
	
	/**
	 * 
	 *功能说明: 获取字段对于的 java.sql.Types 类型值
	 *创建人: 王金绍
	 *创建时间:2013-3-19 下午1:43:33
	 *@param col
	 *@param column
	 *@return int
	 *
	 */
	private int getFieldType(Acolumn acolumn){
		Type type = acolumn.getAttrType();
		String t = type.toString();
		//System.out.println(t + col.getSqlName() + column.type());
		if(t.equals("class java.lang.Long") || t.equals("long")){
			return Types.BIGINT;
		}
		if(t.equals("class java.lang.Integer") || t.equals("int")){
			return Types.INTEGER;
		}
		if(t.equals("class java.lang.Short") || t.equals("short")){
			return Types.SMALLINT;
		}
		if(t.equals("class java.lang.Byte") || t.equals("byte")){
			return Types.TINYINT;
		}
		if(t.equals("class java.lang.Double") || t.equals("double")){
			return Types.DOUBLE;
		}
		if(t.equals("class java.lang.Float") || t.equals("float")){
			return Types.FLOAT;
		}
		if(t.equals("class java.math.BigDecimal")){
			return Types.DECIMAL;
		}
		if(t.equals("class java.lang.Boolean") || t.equals("boolean")){
			return Types.BOOLEAN;
		}
		//http://www.cnblogs.com/shihujiang/archive/2012/06/14/2548981.html
		if(t.equals("class java.util.Date")){
			
			return Types.TIMESTAMP;
			//dateTime获取值错误
			//return Types.TIME;
		}
		if(t.equals("class java.sql.Time")){
			return Types.TIME;
		}
		if(t.equals("class java.sql.Timestamp")){
			return Types.TIME;
		}
		return Types.VARCHAR;
		//return column.type();
	}
	
	
	/**
	 * 执行读取table扩展信息获取功能
	 * 
	 * 2015年4月26日 上午9:19:10
	 */
	public void exeTableExtInfoReaders(OrmTable ormTable) {
		doSort();
		if(this.tableExtInfoReaders != null && this.tableExtInfoReaders.size() > 0){
			for(TableExtInfoReader tableExtInfoReader : this.tableExtInfoReaders){
				if(tableExtInfoReader.match(ormTable))
					tableExtInfoReader.readExtInfo(ormTable);
			}
		}
	}

	/**
	 * 执行读取Column扩展信息读取功能
	 * @param ormColumn
	 * 2015年4月26日 上午9:22:15
	 */
	public void extColumnExtInfoReaders(OrmColumn ormColumn) {
		doSort();
		if(this.columnExtInfoReaders != null && this.columnExtInfoReaders.size() > 0){
			for(ColumnExtInfoReader columnExtInfoReader : this.columnExtInfoReaders){
				if(columnExtInfoReader.match(ormColumn))
					columnExtInfoReader.readExtInfo(ormColumn);
			}
		}
	}

	/**
	 * 排序
	 * 
	 * 2015年4月26日 上午9:12:44
	 */
	private void doSort(){
		if(sort)
			return ;
		synchronized (this) {
			if(!sort){
				if(tableExtInfoReaders != null)
					Collections.sort(tableExtInfoReaders, new OrderComparator());
				if(columnExtInfoReaders != null)
					Collections.sort(columnExtInfoReaders, new OrderComparator());
			}
		}
	}
	
}
