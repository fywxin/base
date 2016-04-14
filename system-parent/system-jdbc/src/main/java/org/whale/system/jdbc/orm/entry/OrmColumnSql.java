package org.whale.system.jdbc.orm.entry;

import java.util.ArrayList;
import java.util.List;

import org.whale.system.common.exception.OrmException;
import org.whale.system.common.util.Strings;

/**
 * 对应@Sql
 *
 * @author wjs
 * 2014年9月6日-下午1:51:36
 */
public class OrmColumnSql {

	//注释的SQL语句
	private String sql;
	//将@ 替换为 ? 后的实际执行sql
	private String newSql;
	//保护@的字段
	private List<String> params = new ArrayList<String>();
	//插入数据库前执行
	private boolean isPre;
	//值为空时执行sql，将结果替换
	private boolean replaceWhenNull;
	
	public void setSql(String sql) {
		if(sql != null){
			this.sql = sql.trim()+" ";
			this.parsePlaceHolders();
		}
	}

	/**
	 * 
	 *功能说明: 获取@占位符内容，并将其替换为 ?, 同时将@@ 替换为 @
	 *创建人: wjs
	 *创建时间:2013-3-18 下午2:16:42
	 *@return List<String>
	 *
	 */
	private void parsePlaceHolders(){
		if(sql == null || "".equals(sql.trim())) return ;
		if(this.sql.indexOf("@") == -1){
			this.newSql = this.sql.trim();
			return ;
		}
		StringBuilder strb = new StringBuilder();
		int start = 0;
		int index = -1;
		List<String> list = new ArrayList<String>();
		while(start < sql.length() && (index = sql.indexOf("@", start)) != -1 ){
			strb.append(sql.substring(start, index)).append("? ");
			String str = sql.substring(index, sql.indexOf(" ", index));
			if(Strings.isBlank(str)){
				throw new OrmException("sql ["+this.sql+"] 格式错误");
			}
			list.add(str.trim().substring(1));
			start = index+str.length();
		}
		if(start < sql.length()){
			strb.append(sql.substring(start));
		}
		this.newSql = strb.toString().trim().toUpperCase();
		this.params = list;
	}
	
	public String getSql() {
		return sql;
	}
	public String getNewSql() {
		return newSql;
	}
	public List<String> getParams() {
		return params;
	}
	public boolean getIsPre() {
		return isPre;
	}
	public void setIsPre(boolean isPre) {
		this.isPre = isPre;
	}
	public boolean getReplaceWhenNull() {
		return replaceWhenNull;
	}
	public void setReplaceWhenNull(boolean replaceWhenNull) {
		this.replaceWhenNull = replaceWhenNull;
	}
}
