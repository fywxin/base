package org.whale.system.domain;

import org.whale.system.base.BaseEntry;
import org.whale.system.jdbc.annotation.Column;
import org.whale.system.jdbc.annotation.Id;
import org.whale.system.jdbc.annotation.Table;


@Table(value="sys_user_role", cnName="用户角色")
public class UserRole extends BaseEntry {

	private static final long serialVersionUID = 1L;

	@Id
	@Column(cnName="userRoleId")
	private Long userRoleId;
	
	@Column(cnName="userId")
	private Long userId;
	
	@Column(cnName="roleId")
	private Long roleId;

	public Long getUserRoleId() {
		return userRoleId;
	}

	public void setUserRoleId(Long userRoleId) {
		this.userRoleId = userRoleId;
	}

	public Long getUserId() {
		return userId;
	}

	public void setUserId(Long userId) {
		this.userId = userId;
	}

	public Long getRoleId() {
		return roleId;
	}

	public void setRoleId(Long roleId) {
		this.roleId = roleId;
	}
	
}
