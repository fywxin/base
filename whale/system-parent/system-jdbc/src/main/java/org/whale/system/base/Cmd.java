package org.whale.system.base;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.whale.system.common.util.SpringContextHolder;
import org.whale.system.common.util.Strings;
import org.whale.system.jdbc.orm.OrmContext;
import org.whale.system.jdbc.orm.entry.OrmTable;

/**
 * 单表查询构造器, AND 条件联合查询
 * col 支持java字段和sql字段
 * 参考nutz  OR 以后加入
 * 
 * @author 王金绍
 *
 */
public class Cmd implements Iquery{
	
	private StringBuilder sql = new StringBuilder();
	
	private StringBuilder order;
	
	private List<Object> args = new ArrayList<Object>();
	
	private Set<String> fields;
	
	private boolean selectAll = false;
	
	private OrmTable ormTable;
	
	public static Cmd newCmd(Class<?> clazz){
		return new Cmd(clazz);
	}
	
	public Cmd(Class<?> clazz) {
        OrmContext ormContext = SpringContextHolder.getBean(OrmContext.class);
		ormTable = ormContext.getOrmTable(clazz);
    }
	
	@Override
	public String getDelSql() {
//		OrmContext ormContext = SpringContextHolder.getBean(OrmContext.class);
//		OrmTable ormTable = ormContext.getOrmTable(clazz);
		
		StringBuilder strb = new StringBuilder();
		strb.append("DELETE FROM ").append(ormTable.getTableDbName()).append(" WHERE 1=1 ").append(sql.toString().replaceAll("t.", ""));
		
		return strb.toString();
	}
	
	@Override
	public String getGetSql() {
//		OrmContext ormContext = SpringContextHolder.getBean(OrmContext.class);
//		OrmTable ormTable = ormContext.getOrmTable(clazz);
		
		StringBuilder strb = new StringBuilder();
		
		strb.append(ormTable.getSqlHeadPrefix()).append(" WHERE 1=1 ").append(sql);
		
		return strb.toString();
	}

	@Override
	public String getQuerySql() {
//		OrmContext ormContext = SpringContextHolder.getBean(OrmContext.class);
//		OrmTable ormTable = ormContext.getOrmTable(clazz);
		
		StringBuilder strb = new StringBuilder();
		
		if(fields != null && fields.size() > 0){
			strb.append("SELECT ");
			if(selectAll){
				strb.append(ormTable.getSqlColPrexs()).append(",");
			}
			for(String field : fields){
				strb.append(field).append(",");
			}
			strb.deleteCharAt(strb.length()-1).append(" FROM ").append(ormTable.getTableDbName()).append(" t ");
		}else{
			strb.append(ormTable.getSqlHeadPrefix());
		}
		
		strb.append("WHERE 1=1 ").append(sql);
		if(order == null){
			strb.append(ormTable.getSqlOrderSuffix());
		}else{
			strb.append(" ORDER BY ").append(order.deleteCharAt(order.length()-1));
		}
		return strb.toString();
	}
	
	@Override
	public String getCountSql() {
//		OrmContext ormContext = SpringContextHolder.getBean(OrmContext.class);
//		OrmTable ormTable = ormContext.getOrmTable(clazz);
		
		StringBuilder strb = new StringBuilder();
		strb.append("SELECT COUNT(1) FROM ").append(ormTable.getTableDbName()).append(" t WHERE 1=1 ").append(sql);
		
		return strb.toString();
	}

	@Override
	public List<Object> getArgs() {
		return args;
	}
	
	/**
	 * 相当于 select t.*
	 * @return
	 */
	public Cmd selectAll(){
		this.selectAll = true;
		return this;
	}
	
	/**
	 * 加入查询返回字段
	 * 
	 * @param field
	 * @return
	 */
	public Cmd select(String field){
		if(this.fields == null){
			this.fields = new HashSet<String>();
		}
		this.fields.add(field);
		return this;
	}
	
	/**
	 * 创建 and = 条件语句
	 * @param col
	 * @param value
	 * @return
	 */
	public Cmd and(String col, Object value){
		return this.and(col, "=", value);
	}
	
	/**
	 * 创建 and like 条件语句
	 * @param col
	 * @param value
	 * @return
	 */
	public Cmd like(String col, Object value){
		return this.and(col, "like", value);
	}
	
	/**
	 * 创建and 查询条件语句
	 * 
	 * @param col
	 * @param opt 可选值 [=, !=, like, >, >=, <, <=, in, between]
	 * @param value
	 * @return
	 */
	@SuppressWarnings("all")
	public Cmd and(String col, String opt, Object value){
		col = this.fixCol(col);
			
		if("=".equals(opt)){
			if(value == null){
				sql.append(" AND t.").append(col).append(" IS NULL");
			}else{
				sql.append(" AND t.").append(col).append(" = ?");
				args.add(value);
			}
		} else if("!=".equals(opt)){
			if(value == null){
				sql.append(" AND t.").append(col).append(" IS NOT NULL");
			}else{
				sql.append(" AND t.").append(col).append(" != ?");
				args.add(value);
			}
		} else if("like".equalsIgnoreCase(opt)){
			if(value == null || Strings.isBlank(value.toString())){
				return this;
			}else{
				sql.append(" AND t.").append(col).append(" LIKE ?");
				String val = value.toString().trim();
				if(val.startsWith("%") || val.endsWith("%")){
					args.add(val);
				}else{
					args.add("%"+val+"%");
				}
			}
		} else if(">".equals(opt)){
			if(value == null){
				return this;
			}else{
				sql.append(" AND t.").append(col).append(" > ?");
				args.add(value);
			}
		} else if(">=".equals(opt)){
			if(value == null){
				return this;
			}else{
				sql.append(" AND t.").append(col).append(" >= ?");
				args.add(value);
			}
		} else if("<".equals(opt)){
			if(value == null){
				return this;
			}else{
				sql.append(" AND t.").append(col).append(" < ?");
				args.add(value);
			}
		} else if("<=".equals(opt)){
			if(value == null){
				return this;
			}else{
				sql.append(" AND t.").append(col).append(" <= ?");
				args.add(value);
			}
		} else if("in".equalsIgnoreCase(opt)){
			if(value == null){
				return this;
			}else{
				List<Object> list = (List<Object>)value;
				if(list.size() < 1){
					return this;
				}else if(list.size() == 1){
					if(list.get(0) == null){
						sql.append(" AND t.").append(col).append(" IS NULL");
					}else{
						sql.append(" AND t.").append(col).append(" = ?");
						args.add(list.get(0));
					}
				}else{
					sql.append(" AND t.").append(col).append(" IN (");
					for(Object val : list){
						sql.append("?,");
					}
					sql.deleteCharAt(sql.length()-1);
					sql.append(") "); 
					args.addAll(list);
				}
			}
		} else if("between".equalsIgnoreCase(opt)){
			if(value == null){
				return this;
			}else{
				List<Object> list = (List<Object>)value;
				if(list.size() != 2){
					throw new IllegalArgumentException("between size must 2");
				}
				
				sql.append(" AND t.").append(col).append(" BETWEEN ? AND ? ");
				args.addAll(list);
			}
		} else {
			throw new IllegalArgumentException("opt["+opt+"] 值必须是 [=, !=, like, >, >=, <, <=, in, between]");
		}
		return this;
	}
	
	/**
	 * 加入自定义条件
	 * @param sql
	 * @param value
	 * @return
	 */
	@SuppressWarnings("all")
	public Cmd wrap(String sql, Object value){
		this.sql.append(sql);
		if(value instanceof Collection){
			this.args.addAll((Collection)value);
		}else{
			this.args.add(value);
		}
		return this;
	}
	
	public Cmd asc(String col){
		if(order == null){
			order = new StringBuilder();
		}
		col = this.fixCol(col);
		order.append(col).append(" ASC,");
		return this;
	}
	
	public Cmd desc(String col){
		if(order == null){
			order = new StringBuilder();
		}
		col = this.fixCol(col);
		order.append(col).append(" DESC,");
		return this;
	}

	private String fixCol(String col){
		String newCol = this.ormTable.getJavaAsSqlColumn().get(col);
		if(Strings.isNotBlank(newCol)){
			col = newCol;
		}
		return col;
	}
}