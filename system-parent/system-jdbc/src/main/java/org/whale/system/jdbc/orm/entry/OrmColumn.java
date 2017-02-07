package org.whale.system.jdbc.orm.entry;

import java.util.Map;


/**
 * 字段orm属性
 *
 * @author wjs
 * 2014年9月6日-下午1:51:26
 */
public class OrmColumn extends Acolumn {

	//字段类型 java.sql.Types
	private int type;
	//长度
//	private int width;
	//小数点
//	private int precision;
	//db字段名
	private String sqlName;
	//是否主键
	private boolean isId = false;
	//主键是否自增，非自增主键，值由开发人员提供，如UUID
	private boolean idAuto = true;
	//主键id是否可以忽略
	private boolean idIgnore = false;
	//是否复合主键
	//private boolean isPk = false;
	//是否唯一
	private boolean unique;
//	//是否可为空
//	private boolean nullAble;
	//是否可更新
	private boolean updateAble;
	//默认值
	private String defaultValue = null;
	/**是否为 @OptimisticLock 乐观锁字段 */
	private boolean isOptimisticLock;
	
	//字段定义的默认值
	private Object value;
	
	//-----------------------排序字段--------------------------
	
	private OrmOrder ormOrder = null;
	
	//-----------------------用户SQL执行-----------------------
	private OrmColumnSql ormColumnSql;
	
	
	//-----------------------合法校验------------------------
	private OrmValidate ormValidate;
	
	//-----------------外部扩张对象注释信息读取保存---------------
	
	private Map<String, Object> extInfo;
	
	
	public OrmColumn(Acolumn acolumn){
		super();
		this.setField(acolumn.getField());
		this.setAttrType(acolumn.getAttrType());
		this.setAttrName(acolumn.getAttrName());
		this.setCnName(acolumn.getCnName());
	}
	
	
	public int getType() {
		return type;
	}
	public void setType(int type) {
		this.type = type;
	}
	public String getSqlName() {
		return sqlName;
	}
	public void setSqlName(String sqlName) {
		this.sqlName = sqlName;
	}
	public boolean getIsId() {
		return isId;
	}
	public void setIsId(boolean isId) {
		this.isId = isId;
	}
//	public int getWidth() {
//		return width;
//	}
//	public void setWidth(int width) {
//		this.width = width;
//	}
//	public int getPrecision() {
//		return precision;
//	}
//	public void setPrecision(int precision) {
//		this.precision = precision;
//	}

	//	public boolean isPk() {
//		return isPk;
//	}
//	public void setIsPk(boolean isPk) {
//		this.isPk = isPk;
//	}
	public boolean getUnique() {
		return unique;
	}
	public void setUnique(boolean unique) {
		this.unique = unique;
	}
//	public boolean getNullAble() {
//		return nullAble;
//	}
//	public void setNullAble(boolean nullAble) {
//		this.nullAble = nullAble;
//	}
	public boolean getUpdateAble() {
		return updateAble;
	}
	public void setUpdateAble(boolean updateAble) {
		this.updateAble = updateAble;
	}
	public boolean getIdAuto() {
		return idAuto;
	}
	public void setIdAuto(boolean idAuto) {
		this.idAuto = idAuto;
	}
	public String getDefaultValue() {
		return defaultValue;
	}
	public void setDefaultValue(String defaultValue) {
		this.defaultValue = defaultValue;
	}
	public OrmColumnSql getOrmColumnSql() {
		return ormColumnSql;
	}
	public void setOrmColumnSql(OrmColumnSql ormColumnSql) {
		this.ormColumnSql = ormColumnSql;
	}
	public OrmOrder getOrmOrder() {
		return ormOrder;
	}
	public void setOrmOrder(OrmOrder ormOrder) {
		this.ormOrder = ormOrder;
	}
	public OrmValidate getOrmValidate() {
		return ormValidate;
	}
	public void setOrmValidate(OrmValidate ormValidate) {
		this.ormValidate = ormValidate;
	}
	public Map<String, Object> getExtInfo() {
		return extInfo;
	}
	public void setExtInfo(Map<String, Object> extInfo) {
		this.extInfo = extInfo;
	}
	public boolean getIsOptimisticLock() {
		return isOptimisticLock;
	}
	public void setIsOptimisticLock(boolean isOptimisticLock) {
		this.isOptimisticLock = isOptimisticLock;
	}
	public Object getValue() {
		return value;
	}
	public void setValue(Object value) {
		this.value = value;
	}
	public boolean getIdIgnore() {
		return idIgnore;
	}
	public void setIdIgnore(boolean idIgnore) {
		this.idIgnore = idIgnore;
	}

	@Override
	public String toString() {
		return this.getAttrName();
	}
}
