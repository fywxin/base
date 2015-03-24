package org.whale.system.domain;

import java.util.List;

import org.whale.system.base.BaseEntry;
import org.whale.system.common.constant.SysConstant;
import org.whale.system.jdbc.annotation.Column;
import org.whale.system.jdbc.annotation.Id;
import org.whale.system.jdbc.annotation.Order;
import org.whale.system.jdbc.annotation.Table;
import org.whale.system.jdbc.annotation.Validate;

@Table(value="sys_dict", cnName="字典")
public class Dict extends BaseEntry {

	private static final long serialVersionUID = 1L;

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
	
	private List<DictItem> items;
	

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
