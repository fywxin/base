package org.whale.ext.domain;

import java.lang.reflect.Type;

import org.whale.system.base.BaseEntry;
import org.whale.system.jdbc.annotation.Column;
import org.whale.system.jdbc.annotation.Id;
import org.whale.system.jdbc.annotation.Table;
import org.whale.system.jdbc.annotation.Validate;

/**
 * 属性字段
 *
 * @author 王金绍
 * 2014年9月10日-下午4:02:37
 */
@Table(value="sys_attr", cnName="属性字段")
public class Attr extends BaseEntry{

	private static final long serialVersionUID = -324398431L;

	@Id
	@Column(cnName="id")
	private Long id;
	
	@Validate(required=true)
	@Column(cnName="所属实体Id")
	private Long domainId;
	
	@Validate(required=true)
	@Column(cnName="字段名")
	private String name;
	
	@Validate(required=true)
	@Column(cnName="中文名")
	private String cnName;
	
	@Validate(required=true)
	@Column(cnName="数据库名")
	private String sqlName;
	
	@Column(cnName="默认值")
	private String defVal;
	
	//1.String 2.Long  3.Integer 4.Float 5.Double  6.Short 7.Byte 8.Char 9.Boolean 10. Date
	@Column(cnName="字段类型")
	private String type = "String";

	@Column(cnName="数据库类型")
	private int dbType = 5;
	
	@Column(cnName="宽度")
	private int width;
	
	@Column(cnName="精确度")
	private int preci;
	
	@Column(cnName="是否ID")
	private boolean isId = false;
	
	@Column(cnName="是否可空")
	private boolean nullAble = true;
	
	@Column(cnName="是否可更新")
	private boolean updateAble = true;
	
	@Column(cnName="是否唯一")
	private boolean uniqueAble = false;
	
	@Column(cnName="是否列表展示")
	private boolean inList = true;
	
	@Column(cnName="是否表单展示")
	private boolean inForm = true;
	
	@Column(cnName="是否查询条件")
	private boolean inQuery = false;
	
	@Column(cnName="排序")
	private int inOrder;
	
	@Column(cnName="默认查询@order")
	private int orderNum = 0;

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getCnName() {
		return cnName;
	}

	public void setCnName(String cnName) {
		this.cnName = cnName;
	}

	public String getSqlName() {
		return sqlName;
	}

	public void setSqlName(String sqlName) {
		this.sqlName = sqlName;
	}

	public String getDefVal() {
		return defVal;
	}

	public void setDefVal(String defVal) {
		this.defVal = defVal;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public int getDbType() {
		return dbType;
	}

	public void setDbType(int dbType) {
		this.dbType = dbType;
	}

	public int getWidth() {
		return width;
	}

	public void setWidth(int width) {
		this.width = width;
	}

	public int getPreci() {
		return preci;
	}

	public void setPreci(int preci) {
		this.preci = preci;
	}

	public boolean getIsId() {
		return isId;
	}

	public void setIsId(boolean isId) {
		this.isId = isId;
	}

	public boolean getNullAble() {
		return nullAble;
	}

	public void setNullAble(boolean nullAble) {
		this.nullAble = nullAble;
	}

	public boolean getUpdateAble() {
		return updateAble;
	}

	public void setUpdateAble(boolean updateAble) {
		this.updateAble = updateAble;
	}

	public boolean getUniqueAble() {
		return uniqueAble;
	}

	public void setUniqueAble(boolean uniqueAble) {
		this.uniqueAble = uniqueAble;
	}

	public boolean getInList() {
		return inList;
	}

	public void setInList(boolean inList) {
		this.inList = inList;
	}

	public boolean getInForm() {
		return inForm;
	}

	public void setInForm(boolean inForm) {
		this.inForm = inForm;
	}

	public boolean getInQuery() {
		return inQuery;
	}

	public void setInQuery(boolean inQuery) {
		this.inQuery = inQuery;
	}

	public int getInOrder() {
		return inOrder;
	}

	public void setInOrder(int inOrder) {
		this.inOrder = inOrder;
	}

	public Long getDomainId() {
		return domainId;
	}

	public void setDomainId(Long domainId) {
		this.domainId = domainId;
	}
	
	public int getOrderNum() {
		return orderNum;
	}

	public void setOrderNum(int orderNum) {
		this.orderNum = orderNum;
	}

	//1.String 2.Long  3.Integer 4.Float 5.Double  6.Short 7.Byte 8.Char 9.Boolean 10. Date
	public static String parseType(Type type){
		String t = type.toString();
		if(t.equals("class java.lang.Long") || t.equals("long")){
			return "Long";
		}
		if(t.equals("class java.lang.Integer") || t.equals("int")){
			return "Integer";
		}
		if(t.equals("class java.lang.Short") || t.equals("short")){
			return "Short";
		}
		if(t.equals("class java.lang.Byte") || t.equals("byte")){
			return "Byte";
		}
		if(t.equals("class java.lang.Double") || t.equals("double")){
			return "Double";
		}
		if(t.equals("class java.lang.Float") || t.equals("float")){
			return "Float";
		}
		if(t.equals("class java.lang.Boolean") || t.equals("boolean")){
			return "Boolean";
		}
		if(t.equals("class java.util.Date")){
			return "Date";
		}
		return "String";
	}
	
}
