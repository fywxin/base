package org.whale.system.dao;

import java.util.List;

import org.springframework.stereotype.Repository;
import org.whale.system.base.BaseDao;
import org.whale.system.base.Query;
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
		Query query = Query.newQuery(Menu.class).eq("parentId", parentId);
		return this.queryBy(query);
	}
	
	public List<Menu> getMenuByType(Integer menuType){
		return this.queryBy(Query.newQuery(Menu.class).eq("menuType", menuType));
	}
	
	public List<Menu> getPublicMenus(){
		return this.queryBy(Query.newQuery(Menu.class).eq("menuType", 3).eq("isPublic", 1));
	}
	
	public Menu getByMenuName(String menuName){
		
		return this.getBy(Query.newQuery(Menu.class).eq("menuName", menuName));
	}
	
	
	final String getCurOrder_SQL = "select max(orderNo) from sys_menu where parentId = ?";
	@SuppressWarnings("all")
	public Integer getCurOrder(Long parentId){
		
		return this.queryForInt(getCurOrder_SQL, parentId);
	}
}
