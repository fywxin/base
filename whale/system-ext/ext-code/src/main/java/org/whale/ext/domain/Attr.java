package org.whale.ext.domain;

import org.whale.system.annotation.jdbc.Column;
import org.whale.system.annotation.jdbc.Id;
import org.whale.system.annotation.jdbc.Order;
import org.whale.system.annotation.jdbc.Table;
import org.whale.system.annotation.jdbc.Validate;
import org.whale.system.base.BaseEntry;

@Table(value="sys_attr", cnName="实体属性")
public class Attr extends BaseEntry{

	private static final long serialVersionUID = -324398431L;

	@Id
	@Column(cnName="attrId")
	private Long attrId;
	
	@Validate(required=true)
	@Column(cnName="所属实体Id")
	private Long domainId;
	
	@Validate(required=true)
	@Column(cnName="列名")
	private String sqlName;
	
	@Validate(required=true)
	@Column(cnName="字段名")
	private String name;
	
	@Validate(required=true)
	@Column(cnName="中文名")
	private String cnName;
	
	@Column(cnName="数据库类型")
	private String dbType;
	
	//1.String 2.Long  3.Integer 4.Float 5.Double  6.Short 7.Byte 8.Char 9.Boolean 10. Date
	@Column(cnName="字段类型")
	private String type = "String";
	
	@Column(cnName="是否ID")
	private Boolean isId = false;
	
	@Column(cnName="是否可空")
	private Boolean isNull = true;
	
	@Column(cnName="是否可更新")
	private Boolean isEdit = true;
	
	@Column(cnName="是否唯一")
	private Boolean isUnique = false;
	
	@Column(cnName="是否列表展示")
	private Boolean inList = true;
	
	@Column(cnName="是否表单展示")
	private Boolean inForm = true;
	
	@Column(cnName="是否查询条件")
	private Boolean inQuery = false;
	
	@Column(cnName="查询匹配方式")
	private String queryType;
	
	@Column(cnName="表单类型")
	private String formType;
	
	@Column(cnName="所属字典")
	private String dictName;
	
	@Column(cnName="最大长度")
	private Integer maxLength=0;
	
	@Order
	@Column(cnName="排序")
	private Integer inOrder;

	
	public Long getAttrId() {
		return attrId;
	}


	public void setAttrId(Long attrId) {
		this.attrId = attrId;
	}


	public Long getDomainId() {
		return domainId;
	}


	public void setDomainId(Long domainId) {
		this.domainId = domainId;
	}


	public String getSqlName() {
		return sqlName;
	}


	public void setSqlName(String sqlName) {
		this.sqlName = sqlName;
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


	public String getDbType() {
		return dbType;
	}


	public void setDbType(String dbType) {
		this.dbType = dbType;
	}


	public String getType() {
		return type;
	}


	public void setType(String type) {
		this.type = type;
	}


	public Boolean getIsId() {
		return isId;
	}


	public void setIsId(Boolean isId) {
		this.isId = isId;
	}


	public Boolean getIsNull() {
		return isNull;
	}


	public void setIsNull(Boolean isNull) {
		this.isNull = isNull;
	}


	public Boolean getIsEdit() {
		return isEdit;
	}


	public void setIsEdit(Boolean isEdit) {
		this.isEdit = isEdit;
	}


	public Boolean getIsUnique() {
		return isUnique;
	}


	public void setIsUnique(Boolean isUnique) {
		this.isUnique = isUnique;
	}


	public Boolean getInList() {
		return inList;
	}


	public void setInList(Boolean inList) {
		this.inList = inList;
	}


	public Boolean getInForm() {
		return inForm;
	}


	public void setInForm(Boolean inForm) {
		this.inForm = inForm;
	}


	public Boolean getInQuery() {
		return inQuery;
	}


	public void setInQuery(Boolean inQuery) {
		this.inQuery = inQuery;
	}


	public String getQueryType() {
		return queryType;
	}


	public void setQueryType(String queryType) {
		this.queryType = queryType;
	}


	public String getFormType() {
		return formType;
	}


	public void setFormType(String formType) {
		this.formType = formType;
	}


	public String getDictName() {
		return dictName;
	}


	public void setDictName(String dictName) {
		this.dictName = dictName;
	}


	public Integer getInOrder() {
		return inOrder;
	}


	public void setInOrder(Integer inOrder) {
		this.inOrder = inOrder;
	}


	public Integer getMaxLength() {
		return maxLength;
	}


	public void setMaxLength(Integer maxLength) {
		this.maxLength = maxLength;
	}
	
}
