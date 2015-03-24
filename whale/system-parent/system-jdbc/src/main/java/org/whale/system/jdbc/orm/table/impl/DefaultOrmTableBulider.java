package org.whale.system.jdbc.orm.table.impl;

import java.lang.reflect.Type;
import java.sql.Types;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import javax.annotation.Resource;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.OrderComparator;
import org.springframework.stereotype.Component;
import org.whale.system.common.exception.OrmException;
import org.whale.system.common.util.Strings;
import org.whale.system.jdbc.annotation.Column;
import org.whale.system.jdbc.annotation.Id;
import org.whale.system.jdbc.annotation.Order;
import org.whale.system.jdbc.annotation.Sql;
import org.whale.system.jdbc.annotation.Table;
import org.whale.system.jdbc.annotation.Validate;
import org.whale.system.jdbc.common.Acolumn;
import org.whale.system.jdbc.common.Atable;
import org.whale.system.jdbc.common.EntryContext;
import org.whale.system.jdbc.common.TableBulider;
import org.whale.system.jdbc.orm.entry.OrmColumn;
import org.whale.system.jdbc.orm.entry.OrmColumnSql;
import org.whale.system.jdbc.orm.entry.OrmOrder;
import org.whale.system.jdbc.orm.entry.OrmTable;
import org.whale.system.jdbc.orm.entry.OrmValidate;
import org.whale.system.jdbc.orm.event.OrmColumnGenEvent;
import org.whale.system.jdbc.orm.event.OrmEventMuliter;
import org.whale.system.jdbc.orm.event.OrmTableGenEvent;
import org.whale.system.jdbc.orm.table.OrmTableBulider;
import org.whale.system.jdbc.orm.table.UserParseColumnName;
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
	private HumpTableNameParser humpTableNameParser;
	@Autowired
	private SameTableNameParser sameTableNameParser;
	@Autowired
	private HumpColumnNameParser humpColumnNameParser;
	@Autowired
	private SameColumnNameParser sameColumnNameParser;
	@Autowired(required=false)
	private List<UserParseColumnName> userColParsers;
	@Resource(name="entryContext")
	private EntryContext entryContext;
	@Autowired
	private TableBulider tableBulider;
	@Autowired
	private OrmEventMuliter omEventMuliter;
	
	private boolean noSortFlag = true;
	
	
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
		logger.info("ORM:设置类clazz="+clazz+"基本属性完成, aCols="+ormTable.getCols());
		this.fireAndParseTable(ormTable); //设置数据库，序列，拥有者
		logger.info("ORM:设置类clazz="+clazz+"设置数据库，序列，拥有者完成, tableDbName="+ormTable.getTableDbName()+", tableCnName="+ormTable.getTableCnName());
		List<OrmColumn> cols = this.getColumns(ormTable); //获取所有@Column
		ormTable.setOrmCols(cols); //设置字段
		logger.info("ORM:转换 类clazz="+clazz+" Acolumn->OrmColumn 完成，ormCols="+cols);
		this.reParseTable(ormTable); //设置idCol,pkCols
		logger.info("ORM:提取 类clazz="+clazz+" ID字段完成 idColumn="+ormTable.getIdCol());
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
				ormTable.setTableDbName(this.humpTableNameParser.getDbTableName(ormTable.getEntityName()));
			}
		}else{
			//TODO oracle 与 mysql 表名 大小写是否有要求
			//ormTable.setTableDbName(table.value().trim().toUpperCase());
			ormTable.setTableDbName(table.value().trim());
		}
		
		//数据库序列
		if(Strings.isBlank(table.sequence())){
			//mysql 无序列
			ormTable.setSequence(this.humpTableNameParser.getDbSequence(ormTable.getEntityName()));
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
		
		for(Acolumn acolumn : acolumns){
			ormColumn = this.fireAndParseColumn(table, acolumn);
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
	 * 绑定OrmColumn创建事件
	 * @param table
	 * @param acolumn
	 * @return
	 * @Date 2015年3月9日 下午4:34:07
	 */
	private OrmColumn fireAndParseColumn(Atable table, Acolumn acolumn){
		OrmColumnGenEvent beforeOrmColumnGenEvent = new OrmColumnGenEvent(this, table, acolumn, null, true, false);
		this.omEventMuliter.multicater(beforeOrmColumnGenEvent);
		
		OrmColumn ormColumn = this.parseColumn(table, acolumn);
		
		OrmColumnGenEvent afterOrmColumnGenEvent = new OrmColumnGenEvent(this, table, acolumn, ormColumn, false, true);
		this.omEventMuliter.multicater(afterOrmColumnGenEvent);
		
		return ormColumn;
	}
	
	/**
	 * 创建OrmColumn 对象
	 * @param table
	 * @param acolumn
	 * @return
	 * @Date 2015年3月9日 下午4:34:59
	 */
	private OrmColumn parseColumn(Atable table, Acolumn acolumn){
		Column column = acolumn.getField().getAnnotation(Column.class);
		if(column == null) 
			return null;
		OrmColumn ormColumn = new OrmColumn(acolumn);
		//主键
		Id id = acolumn.getField().getAnnotation(Id.class);
		if(id != null){
			ormColumn.setIsId(true);
			ormColumn.setIdAuto(id.auto());
		}
		//字段数据库名
		ormColumn.setSqlName(this.getDbColumnName(acolumn.getAttrName(), column, table.getEntityName()));
		//字段中文名
		if(Strings.isNotBlank(column.cnName())){
			ormColumn.setCnName(column.cnName().replaceAll("'", "").replaceAll("\"", ""));
			acolumn.setCnName(column.cnName().replaceAll("'", "").replaceAll("\"", ""));
		}
		
		//建表和判断数据是否超过大小时有用
		ormColumn.setWidth(column.width());
		ormColumn.setPrecision(column.precision());
		
		//字段约束
		ormColumn.setUnique(column.unique());
//		ormColumn.setNullAble(column.nullable());
		ormColumn.setUpdateAble(column.updateable());
		ormColumn.setDefaultValue(column.defaultValue());
		
		//字段附加SQL定义
		Sql sql = acolumn.getField().getAnnotation(Sql.class);
		if(sql != null){
			if(Strings.isNotBlank(sql.value())) {
				this.setColumnSql(ormColumn, sql);
			}
		}
		
		//字段类型
		ormColumn.setType(this.getFieldType(acolumn, column));
		
		//读取设置@Order
		this.setColumnOrder(ormColumn);
		
		//读取设置OrmValidate
		this.setColumnValidate(ormColumn);
		
		return ormColumn;
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
		
		OrmColumn idCol = null;
		for(OrmColumn col : cols){
			//排序字段
			if(col.getOrmOrder() != null){
				orderCols.add(col);
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
				System.out.println("实体 ["+ormTable.getEntityName()+"] 主键采用外部赋值策略, 舍弃序列["+ormTable.getSequence()+"]");
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
	private String getDbColumnName(String fieldName, Column column, String entryName){
		//实体中提供数据库字段名
		if(Strings.isNotBlank(column.name()))
			return column.name();
		//开发人员定义的字段转换规则
		if(this.getUserColParsers() != null && this.getUserColParsers().size() > 0){
			for(UserParseColumnName parser : this.getUserColParsers()){
				if(parser.match(entryName))
					return parser.getDbColName(fieldName);
			}
		}
		//默认驼峰字段转换规则
		if(DbKind.isMysql()){
			return this.sameColumnNameParser.getDbColName(fieldName);
		}else{
			return this.humpColumnNameParser.getDbColName(fieldName);
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
	private int getFieldType(Acolumn acolumn, Column column){
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
		}
		if(t.equals("class java.sql.Time")){
			return Types.TIME;
		}
		if(t.equals("class java.sql.Timestamp")){
			return Types.TIMESTAMP;
		}
		return column.type();
	}
	
	/**
	 * 
	 *功能说明: 第一次排序
	 *创建人: 王金绍
	 *创建时间:2013-4-11 上午11:39:09
	 *@return List<UserParseColumnName>
	 *
	 */
	public List<UserParseColumnName> getUserColParsers(){
		if(noSortFlag && userColParsers != null && userColParsers.size() > 0){
			synchronized (this) {
				if(noSortFlag){
					Collections.sort(userColParsers, new OrderComparator());
					noSortFlag = false;
				}
			}
		}
		return userColParsers;
	}
	
}
