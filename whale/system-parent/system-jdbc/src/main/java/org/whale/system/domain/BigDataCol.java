package org.whale.system.domain;

import org.whale.system.annotation.jdbc.Column;
import org.whale.system.annotation.jdbc.Id;
import org.whale.system.annotation.jdbc.Table;
import org.whale.system.annotation.jdbc.Validate;
import org.whale.system.base.BaseEntry;

/**
 * 大数据字段统一保存表
 * refId, domainName，fieldName 设置联合索引
 * 
 * 对应 @BigCol
 * @author 王金绍
 *
 */
@Table(value="sys_big_data_col", cnName="大数据字段保存表")
public class BigDataCol extends BaseEntry{

	private static final long serialVersionUID = -3423452341L;
	
	@Id
	@Column(name="id", cnName="id")
	private Long id;
	
	@Validate(required=true)
	@Column(cnName="对应实体类名")
	private String domainName;
	
	@Validate(required=true)
	@Column(cnName="对应字段名")
	private String fieldName;
	
	@Validate(required=true)
	@Column(cnName="字段对应ID值")
	private Long refId;
	
	@Validate(required=true)
	@Column(cnName="大数据字段值")
	private String bigData;
	
	@Column(cnName="状态")
	private Integer status;

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getDomainName() {
		return domainName;
	}

	public void setDomainName(String domainName) {
		this.domainName = domainName;
	}

	public String getFieldName() {
		return fieldName;
	}

	public void setFieldName(String fieldName) {
		this.fieldName = fieldName;
	}

	public Long getRefId() {
		return refId;
	}

	public void setRefId(Long refId) {
		this.refId = refId;
	}

	public String getBigData() {
		return bigData;
	}

	public void setBigData(String bigData) {
		this.bigData = bigData;
	}

	public Integer getStatus() {
		return status;
	}

	public void setStatus(Integer status) {
		this.status = status;
	}
}
