package org.whale.system.domain;

import org.whale.system.annotation.jdbc.Column;
import org.whale.system.annotation.jdbc.Id;
import org.whale.system.annotation.jdbc.Table;
import org.whale.system.base.BaseEntry;


@Table(value="sys_role_auth", cnName="角色权限")
public class RoleAuth extends BaseEntry {
	private static final long serialVersionUID = 1L;

	@Id
	@Column(cnName="roleAuthId")
	private Long roleAuthId;
	
	@Column(cnName="roleId")
	private Long roleId;
	
	@Column(cnName="authId")
	private Long authId;

	public Long getRoleAuthId() {
		return roleAuthId;
	}

	public void setRoleAuthId(Long roleAuthId) {
		this.roleAuthId = roleAuthId;
	}

	public Long getRoleId() {
		return roleId;
	}

	public void setRoleId(Long roleId) {
		this.roleId = roleId;
	}

	public Long getAuthId() {
		return authId;
	}

	public void setAuthId(Long authId) {
		this.authId = authId;
	}
	
	
}
