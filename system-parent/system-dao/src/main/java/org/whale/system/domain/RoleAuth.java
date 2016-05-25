package org.whale.system.domain;

import org.whale.system.annotation.jdbc.Id;
import org.whale.system.annotation.jdbc.Table;
import org.whale.system.base.BaseEntry;


@Table(value="sys_role_auth", cnName="角色权限")
public class RoleAuth extends BaseEntry {
	private static final long serialVersionUID = 1L;

	/**角色ID */
	public static final String F_roleId = "roleId";

	/**权限编码 */
	public static final String F_authCode = "authCode";

	@Id
	private Long roleAuthId;
	
	private Long roleId;
	
	private String authCode;

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

	public String getAuthCode() {
		return authCode;
	}

	public void setAuthCode(String authCode) {
		this.authCode = authCode;
	}

	
}
