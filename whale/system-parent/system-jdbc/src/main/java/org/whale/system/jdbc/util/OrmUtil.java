package org.whale.system.jdbc.util;


import org.whale.system.common.util.Strings;

public class OrmUtil {
	
	/**
	 * 创建分页sql语句，默认单表简单查询
	 * 
	 * 子类可以覆盖此方法
	 * @param page
	
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
		
		OrmTable ormTable = ormDao._getOrmTable();
		page.setCountSql("SELECT count(1) FROM " + ormTable.getTableDbName() +" t "+strb.toString());
		
		
		if(page.getOrders() == null){
			strb.append(ormTable.getSqlOrderSuffix());
		}else{
			for(int i=0; i<page.getOrders().size(); i++){
				strb.append(" ORDER BY t.").append(page.getOrders().get(i).getCol()).append(page.getOrders().get(i).isAsc() ? " asc" : " desc");
				if(i != page.getOrders().size()-1){
					strb.append(",");
				}
			}
		}
		
		page.setSql(ormTable.getSqlHeadPrefix()+strb.toString());
	}
 */
	
	/**
	 * 
	 *功能说明: 将驼峰原则的字符串转成ORCALE数据库大写形式"_" 分隔的字符串
	 *创建人: 王金绍
	 *创建时间:2013-3-14 下午5:50:39
	 *@param str
	 *@return String
	 *
	 */
	public static String dump2SqlStyle(String str){
		if(Strings.isBlank(str)) return null;
		str = str.trim();
		char[] chars = str.toCharArray();
		StringBuilder strb = new StringBuilder();
		strb.append(Character.toUpperCase(chars[0]));
		
		for(int i=1; i<chars.length; i++){
			if(chars[i] == ' ') continue;
			if(Character.isUpperCase(chars[i])){
				strb.append("_").append(Character.toUpperCase(chars[i]));
			}else{
				strb.append(Character.toUpperCase(chars[i]));
			}
		}
		return strb.toString();
	}
	
	/**
	 * 将数据库大写_ 转为驼峰原则
	 * @param str
	 * @return
	 */
	public static String sql2DumpStyle(String str){
		if(Strings.isBlank(str)) return null;
		str = str.trim();
		char[] chars = str.toCharArray();
		StringBuilder strb = new StringBuilder();
		strb.append(Character.toLowerCase(chars[0]));
		
		for(int i=1; i<chars.length; i++){
			if(chars[i] == ' ') continue;
			if(chars[i] == '_'){
				i++;
				strb.append(Character.toUpperCase(chars[i]));
			}else{
				strb.append(chars[i]);
			}
		}
		return strb.toString();
	}
	
}
