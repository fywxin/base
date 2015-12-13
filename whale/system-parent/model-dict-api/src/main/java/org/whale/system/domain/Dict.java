package org.whale.system.domain;

import java.io.Serializable;
import java.util.List;

import org.whale.system.annotation.jdbc.Column;
import org.whale.system.annotation.jdbc.Id;
import org.whale.system.annotation.jdbc.Order;
import org.whale.system.annotation.jdbc.Table;
import org.whale.system.annotation.jdbc.Validate;
import org.whale.system.common.constant.SysConstant;

@Table(value="sys_dict", cnName="字典")
public class Dict implements Serializable{

	private static final long serialVersionUID = 232331L;

	@Id
	@Column(cnName="id")
	private Long dictId;
	
	@Validate(required=true)
	@Column(unique=true, cnName="字典名称")
	private String dictName;
	
	@Order
	@Validate(required=true)
	@Column(unique=true, cnName="字典编码")
	private String dictCode;
	
	@Column(cnName="字典类型")
	private Integer dictType = 0;
	
	@Column(cnName="备注")
	private String remark;
	
	@Column(defaultValue=SysConstant.STATUS_NORMAL+"", cnName="状态")
	private Integer status;
	
	
	private List<DictItem> items = null;
	

	public Long getDictId() {
		return dictId;
	}

	public void setDictId(Long dictId) {
		this.dictId = dictId;
	}

	public String getDictName() {
		return dictName;
	}

	public void setDictName(String dictName) {
		this.dictName = dictName;
	}

	public String getDictCode() {
		return dictCode;
	}

	public void setDictCode(String dictCode) {
		this.dictCode = dictCode;
	}

	public Integer getDictType() {
		return dictType;
	}

	public void setDictType(Integer dictType) {
		this.dictType = dictType;
	}

	public String getRemark() {
		return remark;
	}

	public void setRemark(String remark) {
		this.remark = remark;
	}

	public Integer getStatus() {
		return status;
	}

	public void setStatus(Integer status) {
		this.status = status;
	}

	public List<DictItem> getItems() {
		return items;
	}

	public void setItems(List<DictItem> items) {
		this.items = items;
	}

	@Override
	public String toString() {
		return "Dict [dictId=" + dictId + ", dictName=" + dictName
				+ ", dictCode=" + dictCode + ", dictType=" + dictType
				+ ", remark=" + remark + ", status=" + status + ", items="
				+ items + "]";
	}
}
