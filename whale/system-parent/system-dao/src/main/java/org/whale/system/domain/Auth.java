package org.whale.system.domain;

import org.whale.system.base.BaseEntry;
import org.whale.system.jdbc.annotation.Column;
import org.whale.system.jdbc.annotation.Id;
import org.whale.system.jdbc.annotation.Order;
import org.whale.system.jdbc.annotation.Table;
import org.whale.system.jdbc.annotation.Validate;

/**
 *TODO 增加权限状态
 *
 * @author 王金绍
 * 2014年9月23日-上午9:34:26
 */
@Table(value="sys_auth", cnName="权限")
public class Auth extends BaseEntry {
	private static final long serialVersionUID = -43543112425L;

	@Id
	@Column(cnName="权限Id")
	private Long authId;
	
	@Validate(required=true)
	@Column(cnName="菜单ID")
	private Long menuId;
	
	@Validate(required=true)
	@Column(cnName="权限名称")
	private String authName;
	
	@Order
	@Validate(required=true)
	@Column(unique=true, cnName="权限编码")
	private String authCode;
	
	public Long getAuthId() {
		return authId;
	}

	public void setAuthId(Long authId) {
		this.authId = authId;
	}

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
		return "Auth [authId=" + authId + ", menuId=" + menuId + ", authName="
				+ authName + ", authCode=" + authCode + "]";
	}

}
