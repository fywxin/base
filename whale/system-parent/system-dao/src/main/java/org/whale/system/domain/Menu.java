package org.whale.system.domain;

import org.whale.system.base.BaseEntry;
import org.whale.system.common.util.ITreeNode;
import org.whale.system.jdbc.annotation.Column;
import org.whale.system.jdbc.annotation.Id;
import org.whale.system.jdbc.annotation.Order;
import org.whale.system.jdbc.annotation.Table;
import org.whale.system.jdbc.annotation.Validate;

/**
 * 
 * @author 王金绍
 *
 */
@Table(value="sys_menu", cnName="菜单")
public class Menu extends BaseEntry  implements ITreeNode{

	private static final long serialVersionUID = -122342341L;

	@Id
	@Column(cnName="id")
	private Long menuId;
	
	@Validate(required=true)
	@Order(index=1)
	@Column(cnName="父id")
	private Long parentId;
	
	@Validate(required=true)
	@Column(unique=true, cnName="菜单名称")
	private String menuName;
	
	@Column(cnName="菜单类型")
	private Integer menuType;
	
	
	@Column(cnName="菜单地址")
	private String menuUrl;
	
	@Column(cnName="菜单图标")
	private String inco;
	
	@Column(cnName="打开类型")
	private Integer openType = 1;
	
	@Order(index=2)
	@Column(cnName="排序")
	private Integer orderNo;
	
	@Validate(enums={"0","1"})
	@Column(cnName="打开状态")
	private Integer openState = 1;
	
	@Validate(enums={"0","1"})
	@Column(cnName="是否公共")
	private Integer isPublic = 0;

	public Long getMenuId() {
		return menuId;
	}

	public void setMenuId(Long menuId) {
		this.menuId = menuId;
	}

	public Long getParentId() {
		return parentId;
	}

	public void setParentId(Long parentId) {
		this.parentId = parentId;
	}

	public String getMenuName() {
		return menuName;
	}

	public void setMenuName(String menuName) {
		this.menuName = menuName;
	}

	public Integer getMenuType() {
		return menuType;
	}

	public void setMenuType(Integer menuType) {
		this.menuType = menuType;
	}

	public String getMenuUrl() {
		return menuUrl;
	}

	public void setMenuUrl(String menuUrl) {
		this.menuUrl = menuUrl;
	}

	public String getInco() {
		return inco;
	}

	public void setInco(String inco) {
		this.inco = inco;
	}

	public Integer getOpenType() {
		return openType;
	}

	public void setOpenType(Integer openType) {
		this.openType = openType;
	}

	public Integer getOrderNo() {
		return orderNo;
	}

	public void setOrderNo(Integer orderNo) {
		this.orderNo = orderNo;
	}

	public Integer getOpenState() {
		return openState;
	}

	public void setOpenState(Integer openState) {
		this.openState = openState;
	}

	public Integer getIsPublic() {
		return isPublic;
	}

	public void setIsPublic(Integer isPublic) {
		this.isPublic = isPublic;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((inco == null) ? 0 : inco.hashCode());
		result = prime * result + ((menuId == null) ? 0 : menuId.hashCode());
		result = prime * result
				+ ((menuName == null) ? 0 : menuName.hashCode());
		result = prime * result
				+ ((menuType == null) ? 0 : menuType.hashCode());
		result = prime * result + ((menuUrl == null) ? 0 : menuUrl.hashCode());
		result = prime * result
				+ ((openState == null) ? 0 : openState.hashCode());
		result = prime * result
				+ ((openType == null) ? 0 : openType.hashCode());
		result = prime * result + ((orderNo == null) ? 0 : orderNo.hashCode());
		result = prime * result
				+ ((parentId == null) ? 0 : parentId.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Menu other = (Menu) obj;
	
		return other.getMenuId() == this.menuId;
	}

	@Override
	public Long id() {
		return menuId;
	}

	@Override
	public Long pid() {
		return parentId;
	}

	@Override
	public String name() {
		return menuName;
	}

}
