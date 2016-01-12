package org.whale.system.domain;

import org.whale.system.annotation.jdbc.Column;
import org.whale.system.annotation.jdbc.Id;
import org.whale.system.annotation.jdbc.Table;
import org.whale.system.annotation.jdbc.Validate;
import org.whale.system.base.BaseEntry;

/**
 *
 * @author wjs
 * 2014年9月23日-上午9:34:26
 */
@Table(value="sys_auth", cnName="权限")
public class Auth extends BaseEntry {
	private static final long serialVersionUID = -43543112425L;

	@Id(auto=false)
	@Column(cnName="权限编码")
	private String authCode;
	
	@Column(cnName="菜单ID")
	private Long menuId;
	
	@Validate(required=true)
	@Column(cnName="权限名称")
	private String authName;

	public Long getMenuId() {
		return menuId;
	}

	public void setMenuId(Long menuId) {
		this.menuId = menuId;
	}

	public String getAuthName() {
		return authName;
	}

	public void setAuthName(String authName) {
		this.authName = authName;
	}

	public String getAuthCode() {
		return authCode;
	}

	public void setAuthCode(String authCode) {
		this.authCode = authCode;
	}

	@Override
	public String toString() {
		return "Auth [authCode=" + authCode + ", menuId=" + menuId + ", authName="
				+ authName + "]";
	}

}
