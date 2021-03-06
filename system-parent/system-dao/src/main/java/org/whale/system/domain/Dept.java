package org.whale.system.domain;


import org.whale.system.annotation.jdbc.Column;
import org.whale.system.annotation.jdbc.Id;
import org.whale.system.annotation.jdbc.Order;
import org.whale.system.annotation.jdbc.Table;
import org.whale.system.annotation.jdbc.Validate;
import org.whale.system.base.BaseEntry;
import org.whale.system.domain.Dept;

/**
 * 根部门 需要sql脚本初始化
 *
 * @author wjs
 * @Date 2014-9-16
 */
@Table(value="sys_dept", cnName="部门")
public class Dept extends BaseEntry {

	private static final long serialVersionUID = -1410859166676l;

	/**部门名称 */
	public static final String F_deptName = "deptName";
	/**部门编码 */
	public static final String F_deptCode = "deptCode";
	/**父部门 */
	public static final String F_pid = "pid";
	/**排序 */
	public static final String F_orderNo = "orderNo";
	
	@Id
	@Column(name="id", cnName="id")
	private Long id;
	
	@Validate(required=true)
  	@Column(name="deptName", cnName="部门名称")
	private String deptName;
	
	@Validate(required=true)
  	@Column(name="deptCode", cnName="部门编码", unique=true)
	private String deptCode;
	
  	@Order
  	@Column(name="orderNo", cnName="排序")
	private Integer orderNo;
  	
  	@Column(name="remark", cnName="备注")
	private String remark;
  	
  	@Column(name="pid", cnName="父部门")
	private Long pid;
  	
  	@Column(name="deptTel", cnName="联系电话")
	private String deptTel;
  	
  	@Column(name="deptAddr", cnName="联系地址")
	private String deptAddr;
  	
  	@Column(cnName="部门类型")
  	private String deptType;
	
	/**id */
	public Long getId(){
		return id;
	}
	
	/**id */
	public void setId(Long id){
		this.id = id;
	}
	
	/**部门名称 */
	public String getDeptName(){
		return deptName;
	}
	
	/**部门名称 */
	public void setDeptName(String deptName){
		this.deptName = deptName;
	}
	
	/**部门编码 */
	public String getDeptCode(){
		return deptCode;
	}
	
	/**部门编码 */
	public void setDeptCode(String deptCode){
		this.deptCode = deptCode;
	}
	
	/**排序 */
	public Integer getOrderNo(){
		return orderNo;
	}
	
	/**排序 */
	public void setOrderNo(Integer orderNo){
		this.orderNo = orderNo;
	}
	
	/**备注 */
	public String getRemark(){
		return remark;
	}
	
	/**备注 */
	public void setRemark(String remark){
		this.remark = remark;
	}
	
	/**父id */
	public Long getPid(){
		return pid;
	}
	
	/**父id */
	public void setPid(Long pid){
		this.pid = pid;
	}
	
	/**联系电话 */
	public String getDeptTel(){
		return deptTel;
	}
	
	/**联系电话 */
	public void setDeptTel(String deptTel){
		this.deptTel = deptTel;
	}
	
	/**联系地址 */
	public String getDeptAddr(){
		return deptAddr;
	}
	
	/**联系地址 */
	public void setDeptAddr(String deptAddr){
		this.deptAddr = deptAddr;
	}

	public String getDeptType() {
		return deptType;
	}

	public void setDeptType(String deptType) {
		this.deptType = deptType;
	}
}