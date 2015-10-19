package org.whale.system.dao;

import java.util.List;

import org.springframework.stereotype.Repository;
import org.whale.system.base.BaseDao;
import org.whale.system.domain.Menu;

@Repository
public class MenuDao extends BaseDao<Menu, Long> {
	
	/**
	 * 获取所有文件夹类型菜单
	 * @return
	 */
	public List<Menu> getDirMenus(){
		
		return this.query(this.cmd().and("menuType", "!=", 3));
	}
	
	public List<Menu> getByParentId(Long parentId){
		
		return this.query(this.cmd().eq("parentId", parentId));
	}
	
	public List<Menu> getMenuByType(Integer menuType){
		return this.query(this.cmd().eq("menuType", menuType));
	}
	
	public List<Menu> getPublicMenus(){
		return this.query(this.cmd().eq("menuType", 3).eq("isPublic", 1));
	}
	
	public Menu getByMenuName(String menuName){
		
		return this.get(this.cmd().eq("menuName", menuName));
	}
	
	
	final String getCurOrder_SQL = "select max(orderNo) from sys_menu where parentId = ?";
	@SuppressWarnings("all")
	public Integer getCurOrder(Long parentId){
		
		return this.count(getCurOrder_SQL, parentId);
	}
}
