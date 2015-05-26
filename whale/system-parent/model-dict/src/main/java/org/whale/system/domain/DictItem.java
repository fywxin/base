package org.whale.system.domain;

import org.whale.system.annotation.jdbc.Column;
import org.whale.system.annotation.jdbc.Id;
import org.whale.system.annotation.jdbc.Order;
import org.whale.system.annotation.jdbc.Table;
import org.whale.system.annotation.jdbc.Validate;
import org.whale.system.base.BaseEntry;
import org.whale.system.common.constant.SysConstant;


@Table(value="sys_dict_item", cnName="字典元素")
public class DictItem extends BaseEntry{

	private static final long serialVersionUID = 1L;

	@Id
	@Column(cnName="id")
	private Long dictItemId;
	
	@Validate(required=true)
	@Column(cnName="所属字典ID")
	private Long dictId;
	
	@Validate(required=true)
	@Column(cnName="元素名称")
	private String itemName;
	
	@Validate(required=true)
	@Column(cnName="元素编码")
	private String itemCode;
	
	@Column(cnName="元素值")
	private String itemVal;
	
	@Column(cnName="元素值扩展")
	private String itemValExt;
	
	@Column(cnName="备注")
	private String remark;
	
	@Column(defaultValue=SysConstant.STATUS_NORMAL+"", cnName="元素状态")
	private Integer status;
	
	@Order
	@Column(cnName="排序")
	private Integer orderNo;

	public Long getDictItemId() {
		return dictItemId;
	}

	public void setDictItemId(Long dictItemId) {
		this.dictItemId = dictItemId;
	}

	public Long getDictId() {
		return dictId;
	}

	public void setDictId(Long dictId) {
		this.dictId = dictId;
	}

	public String getItemName() {
		return itemName;
	}

	public void setItemName(String itemName) {
		this.itemName = itemName;
	}

	public String getItemCode() {
		return itemCode;
	}

	public void setItemCode(String itemCode) {
		this.itemCode = itemCode;
	}

	public String getItemVal() {
		return itemVal;
	}

	public void setItemVal(String itemVal) {
		this.itemVal = itemVal;
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

	public Integer getOrderNo() {
		return orderNo;
	}

	public void setOrderNo(Integer orderNo) {
		this.orderNo = orderNo;
	}
	
	public String getItemValExt() {
		return itemValExt;
	}

	public void setItemValExt(String itemValExt) {
		this.itemValExt = itemValExt;
	}

	@Override
	public String toString() {
		return "DictItem [dictItemId=" + dictItemId + ", dictId=" + dictId
				+ ", itemName=" + itemName + ", itemCode=" + itemCode
				+ ", itemVal=" + itemVal + ", remark=" + remark + ", status="
				+ status + ", orderNo=" + orderNo + "]";
	}

}
