package org.whale.system.domain;

import org.whale.system.base.BaseEntry;
import org.whale.system.common.constant.SysConstant;
import org.whale.system.jdbc.annotation.Column;
import org.whale.system.jdbc.annotation.Id;
import org.whale.system.jdbc.annotation.Order;
import org.whale.system.jdbc.annotation.Table;
import org.whale.system.jdbc.annotation.Validate;


@Table(value="sys_role", cnName="角色")
public class Role extends BaseEntry{
	private static final long serialVersionUID = 1L;
	
	@Id
	@Column(cnName="id")
    private Long roleId;
	
	@Validate(required=true)
	@Column(cnName="角色名", unique=true)
    private String roleName;
	
	@Order
	@Validate(required=true)
	@Column(cnName="角色编码", unique=true)
    private String roleCode;
	
	@Column(cnName="状态")
    private Integer status = SysConstant.STATUS_NORMAL;
	
	@Column(cnName="备注")
    private String remark;

    public Long getRoleId() {
		return roleId;
	}

	public void setRoleId(Long roleId) {
		this.roleId = roleId;
	}


    public String getRoleName() {
		return roleName;
	}

	public void setRoleName(String roleName) {
		this.roleName = roleName;
	}

	public String getRoleCode() {
		return roleCode;
	}

	public void setRoleCode(String roleCode) {
		this.roleCode = roleCode;
	}

	public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }

}