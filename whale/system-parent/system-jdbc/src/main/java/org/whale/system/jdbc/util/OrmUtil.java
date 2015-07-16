package org.whale.system.jdbc.util;

import java.util.Map;

import org.whale.system.base.Page;
import org.whale.system.common.util.Strings;
import org.whale.system.jdbc.IOrmDao;
import org.whale.system.jdbc.orm.entry.OrmTable;

public class OrmUtil {
	
	/**
	 * 创建分页sql语句，默认单表简单查询
	 * 
	 * 子类可以覆盖此方法
	 * @param page
	 */
	@SuppressWarnings("all")
	public static void _createPageSql(IOrmDao ormDao, Page page){
		StringBuilder strb = new StringBuilder(" where 1=1 ");
		if(page.getParam().size() > 0){
			for(Map.Entry<String, Object> entry : page.getParam().entrySet()){
				if(entry.getValue() != null){
					if((entry.getValue() instanceof String)){
						if(Strings.isNotBlank(entry.getValue().toString())){
							strb.append(" AND t.").append(entry.getKey()).append(" like ? ");
							page.addArg("%"+entry.getValue().toString().trim()+"%");;
						}
					}else{
						strb.append(" AND t.").append(entry.getKey()).append(" = ? ");
						page.addArg(entry.getValue());;
					}
				}
			}
		}
		
		OrmTable ormTable = ormDao.getOrmTable();
		page.setCountSql("SELECT count(1) FROM " + ormTable.getTableDbName() +" t "+strb.toString());
		
		if(page.getOrderColumn().size() < 1){
			strb.append(ormTable.getSqlOrderSuffix());
		}else{
			for(int i=0; i<page.getOrderColumn().size(); i++){
				strb.append(" ORDER BY t.").append(page.getOrderColumn().get(i)).append(page.getOrderAsc().get(i).booleanValue() ? " asc" : " desc");
				if(i != page.getOrderColumn().size()-1){
					strb.append(",");
				}
			}
		}
		
		page.setSql(ormTable.getSqlHeadPrefix()+strb.toString());
	}

}
