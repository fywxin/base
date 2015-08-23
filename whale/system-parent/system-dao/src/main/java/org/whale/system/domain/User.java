package org.whale.system.domain;

import java.util.Date;

import org.whale.system.annotation.jdbc.Column;
import org.whale.system.annotation.jdbc.Id;
import org.whale.system.annotation.jdbc.Table;
import org.whale.system.annotation.jdbc.Validate;
import org.whale.system.base.BaseEntry;


@Table(value="sys_user", cnName="用户")
public class User extends BaseEntry {
	private static final long serialVersionUID = 1L;
	
	@Id
	@Column(cnName="userId")
	private Long userId;
	
	@Validate(required=true)
	@Column(updateable=false, unique=true, cnName="用户名")
	private String userName;
	
	@Validate(required=true)
	@Column(cnName="密码")
	private String password;
	
	@Column(cnName="真实姓名")
	private String realName;
	
	@Validate(required=true)
	@Column(cnName="所属部门")
	private Long deptId;
	
	@Column(cnName="邮件")
	private String email;
	
	@Column(cnName="电话")
	private String phone;
	
	@Column(updateable=false,cnName="创建时间")
	private Date createTime;
	
	@Column(updateable=false,cnName="创建人")
	private Long createUserId;
	
	@Column(cnName="登录次数")
	private Long loginNum;
	
	@Column(cnName="最后登录时间")
	private Date lastLoginTime;
	
	@Column(cnName="登录IP")
	private String loginIp;
	
	@Column(cnName="附加信息")
	private String addOn;
	
	@Column(cnName="状态")
	private Integer status;
	
	@Column(cnName="用户类型")
	private Integer userType;
	
	@Column(cnName="备注")
	private String remark;
	
	
	
	//TODO 按实体类模糊查询时，怎么过滤有初始值的字段
	@Column(updateable=false,cnName="是否管理员")
	private Boolean isAdmin = false;

	public String getUserName() {
		return userName;
	}

	public void setUserName(String userName) {
		this.userName = userName;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public String getRealName() {
		return realName;
	}

	public void setRealName(String realName) {
		this.realName = realName;
	}

	public Long getDeptId() {
		return deptId;
	}

	public void setDeptId(Long deptId) {
		this.deptId = deptId;
	}

	public Date getCreateTime() {
		return createTime;
	}

	public void setCreateTime(Date createTime) {
		this.createTime = createTime;
	}

	public Date getLastLoginTime() {
		return lastLoginTime;
	}

	public void setLastLoginTime(Date lastLoginTime) {
		this.lastLoginTime = lastLoginTime;
	}

	public String getLoginIp() {
		return loginIp;
	}

	public void setLoginIp(String loginIp) {
		this.loginIp = loginIp;
	}

	public String getAddOn() {
		return addOn;
	}

	public void setAddOn(String addOn) {
		this.addOn = addOn;
	}

	public Integer getStatus() {
		return status;
	}

	public void setStatus(Integer status) {
		this.status = status;
	}

	public Integer getUserType() {
		return userType;
	}

	public void setUserType(Integer userType) {
		this.userType = userType;
	}

	public String getRemark() {
		return remark;
	}

	public void setRemark(String remark) {
		this.remark = remark;
	}

	public Long getUserId() {
		return userId;
	}

	public void setUserId(Long userId) {
		this.userId = userId;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getPhone() {
		return phone;
	}

	public void setPhone(String phone) {
		this.phone = phone;
	}

	public Long getCreateUserId() {
		return createUserId;
	}

	public void setCreateUserId(Long createUserId) {
		this.createUserId = createUserId;
	}

	public Boolean getIsAdmin() {
		return isAdmin;
	}

	public void setIsAdmin(Boolean isAdmin) {
		this.isAdmin = isAdmin;
	}

	public Long getLoginNum() {
		return loginNum;
	}

	public void setLoginNum(Long loginNum) {
		this.loginNum = loginNum;
	}
	
	
	
}
