package org.whale.system.domain;

import org.whale.system.annotation.jdbc.Column;
import org.whale.system.annotation.jdbc.Id;
import org.whale.system.annotation.jdbc.Table;
import org.whale.system.base.BaseEntry;


@Table(value="sys_user_role", cnName="用户角色")
public class UserRole extends BaseEntry {

	private static final long serialVersionUID = 1L;

	/**角色ID */
	public static final String F_roleId = "roleId";

	/**用户ID */
	public static final String F_userId = "userId";

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
