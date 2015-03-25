package org.whale.system.jdbc.orm.entry;

import java.util.List;

import org.whale.system.common.util.Strings;
import org.whale.system.jdbc.common.Atable;


/**
 * orm实体表信息
 *
 * @author 王金绍
 * 2014年9月6日-下午1:52:01
 */
public class OrmTable extends Atable {

	/** table 数据库名 */
	private String tableDbName;
	/** table 中文名 */
	private String tableCnName;
	/** 序列 */
	private String sequence;
	/** 数据库 所对应的拥有者  */
	private String tableSchemaOwner;
	/** 主键字段 */
	private OrmColumn idCol;
	/** 所有@Column字段 */
	private List<OrmColumn> ormCols;
	/** 所有@Order字段 */
	private List<OrmColumn> orderCols;
	
	//----------------- 提高效率, 预先读取 ------------
	/** */
	private String sqlColPrexs;
	/** @Order 对应sql的Order排序尾巴 */
	private String sqlOrderSuffixStr;
	/** @Order 对应sql的Select 字段集合头部 */
	private String sqlHeadPrefixStr;
//	/**非空检查字段集合 */
//	private List<OrmColumn> notNullCheckCols;
	/**唯一性检查字段集合 */
	private List<OrmColumn> uniqueCheckCols;
	/**预先执行SQL集合 */
	private List<OrmColumn> sqlExecCols;
	/**待校验检查字段集合 */
	private List<OrmColumn> validateCols;
	
	public OrmTable(){
		super();
	}
	
	public OrmTable(Atable table){
		super();
		this.setClazz(table.getClazz());
		this.setCols(table.getCols());
		this.setEntityName(table.getEntityName());
		this.setParent(table.getParent());
		this.setSuperClazz(table.getSuperClazz());
	}
	
	public String getSqlColPrexs(){
		if(Strings.isBlank(sqlColPrexs)){
			this.sqlColPrexs = this.getSqlColPrexs("t");
		}
		return sqlColPrexs;
	}
	
	public String getSqlColPrexs(String as){
		if(Strings.isBlank(as)){
			as = "t";
		}
		StringBuilder strb = new StringBuilder();
		for(OrmColumn ormCol : ormCols){
			strb.append(",").append(as).append(".").append(ormCol.getSqlName());
		}
		if(strb.length() > 1){
			strb.deleteCharAt(0);
		}
		return strb.toString();
	}
	
	/**
	 * 返回SQL排序字符
	 */
	public String getSqlOrderSuffix(){
		if(sqlOrderSuffixStr == null){
			StringBuilder strb = new StringBuilder(" ORDER BY ");
			if(orderCols == null || orderCols.size() < 1){
				strb.append("t.").append(idCol.getSqlName()).append( " desc");
			}else{
				for(OrmColumn orderCol : orderCols){
					strb.append("t.").append(idCol.getSqlName()).append(orderCol.getOrmOrder().getAsc()? " asc," : " desc,");
				}
				strb.deleteCharAt(strb.length()-1);
			}
			sqlOrderSuffixStr = strb.toString();
		}
		
		return sqlOrderSuffixStr;
	}
	
	public String getSqlHeadPrefix(){
		if(sqlHeadPrefixStr == null){
			StringBuilder strb = new StringBuilder();
			strb.append("SELECT ").append(getSqlColPrexs()).append(" ")
				.append("FROM ").append(this.getTableDbName()).append(" t ")
				.append("WHERE 1=1 ");
			sqlHeadPrefixStr = strb.toString();
		}
		return sqlHeadPrefixStr;
	}
	
	/**
	 * 
	 *功能说明: 是否有主键字段
	 *创建人: 王金绍
	 *创建时间:2013-3-19 上午10:01:10
	 *@return boolean
	 *
	 */
//	public boolean hasIdCol(){
//		return idCol != null;
//	}
	
	public String getTableDbName() {
		return tableDbName;
	}
	public void setTableDbName(String tableDbName) {
		this.tableDbName = tableDbName;
	}
	public String getSequence() {
		return sequence;
	}
	public void setSequence(String sequence) {
		this.sequence = sequence;
	}
	public String getTableSchemaOwner() {
		return tableSchemaOwner;
	}
	public void setTableSchemaOwner(String tableSchemaOwner) {
		this.tableSchemaOwner = tableSchemaOwner;
	}
	public OrmColumn getIdCol() {
		return idCol;
	}
	public void setIdCol(OrmColumn idCol) {
		this.idCol = idCol;
	}
	public List<OrmColumn> getOrmCols() {
		return ormCols;
	}
	public void setOrmCols(List<OrmColumn> ormCols) {
		this.ormCols = ormCols;
	}
	public String getTableCnName() {
		return tableCnName;
	}
	public void setTableCnName(String tableCnName) {
		this.tableCnName = tableCnName;
	}
	public List<OrmColumn> getOrderCols() {
		return orderCols;
	}
	public void setOrderCols(List<OrmColumn> orderCols) {
		this.orderCols = orderCols;
	}
	
//	public List<OrmColumn> getNotNullCheckCols() {
//		return notNullCheckCols;
//	}
//
//	public void setNotNullCheckCols(List<OrmColumn> notNullCheckCols) {
//		this.notNullCheckCols = notNullCheckCols;
//	}
	
	public List<OrmColumn> getUniqueCheckCols() {
		return uniqueCheckCols;
	}

	public void setUniqueCheckCols(List<OrmColumn> uniqueCheckCols) {
		this.uniqueCheckCols = uniqueCheckCols;
	}
	
	public List<OrmColumn> getValidateCols() {
		return validateCols;
	}

	public void setValidateCols(List<OrmColumn> validateCols) {
		this.validateCols = validateCols;
	}

	public List<OrmColumn> getSqlExecCols() {
		return sqlExecCols;
	}

	public void setSqlExecCols(List<OrmColumn> sqlExecCols) {
		this.sqlExecCols = sqlExecCols;
	}

	@Override
	public String toString() {
		return "OrmTable [" + tableDbName + ":"+ sequence + ", ormCols=" + ormCols + "]";
	}
}