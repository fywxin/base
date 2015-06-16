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
		String sql = this.sqlHead()+"where t.menuType != 3"+this.sqlOrder();
		
		return this.query(sql);
	}
	
	public List<Menu> getByParentId(Long parentId){
		Menu menu = this.newT();
		menu.setParentId(parentId);
		return this.query(menu);
//		StringBuilder strb = this.getSqlHead();
//		strb.append("and t.parentId=?");
//		
//		return this.query(strb.toString(), parentId);
	}
	
	public List<Menu> getMenuByType(Integer menuType){
//		StringBuilder strb = this.getSqlHead();
//		strb.append("and t.menuType=? order by t.parentId, t.orderNo");
//		return this.query(strb.toString(), menuType);
		
		Menu menu = this.newT();
		menu.setMenuType(menuType);
		return this.query(menu);
	}
	
	public List<Menu> getPublicMenus(){
//		StringBuilder strb = this.getSqlHead();
//		strb.append("and t.menuType=3 and t.isPublic=1");
//		return this.query(strb.toString());
		
		Menu menu = this.newT();
		menu.setMenuType(3);
		menu.setIsPublic(1);
		return this.query(menu);
	}
	
	public Menu getByMenuName(String menuName){
		Menu menu = new Menu();
		menu.setMenuName(menuName);
		return this.getObject(menu);
		
//		StringBuilder strb = this.getSqlHead();
//		strb.append("and t.menuName=?");
//		
//		return this.getObject(strb.toString(), menuName);
	}
	
	
	final String getCurOrder_SQL = "select max(orderNo) from sys_menu where parentId = ?";
	@SuppressWarnings("all")
	public Integer getCurOrder(Long parentId){
		
		return this.queryForInt(getCurOrder_SQL, parentId);
	}
}
