package org.whale.system.base;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import org.whale.system.common.util.SpringContextHolder;
import org.whale.system.jdbc.orm.OrmContext;
import org.whale.system.jdbc.orm.entry.OrmTable;

/**
 * 单表查询构造器, AND 条件联合查询
 * 参考nutz  OR 以后加入
 * 
 * @author 王金绍
 *
 */
public class Cmd implements Iquery{
	
	private Class<?> clazz;
	
	private StringBuilder sql = new StringBuilder();
	
	private StringBuilder order;
	
	private List<Object> args = new ArrayList<Object>();
	
	public static Cmd newCmd(Class<?> clazz){
		return new Cmd(clazz);
	}
	
	public Cmd(Class<?> clazz) {
        this.clazz = clazz;
    }
	
	@Override
	public String getDelSql() {
		OrmContext ormContext = SpringContextHolder.getBean(OrmContext.class);
		OrmTable ormTable = ormContext.getOrmTable(clazz);
		
		StringBuilder strb = new StringBuilder();
		strb.append("DELETE FROM ").append(ormTable.getTableDbName()).append(" t WHERE 1=1 ").append(sql);
		
		return strb.toString();
	}

	@Override
	public String getQuerySql() {
		OrmContext ormContext = SpringContextHolder.getBean(OrmContext.class);
		OrmTable ormTable = ormContext.getOrmTable(clazz);
		
		StringBuilder strb = new StringBuilder();
		
		strb.append(ormTable.getSqlHeadPrefix()).append(" WHERE 1=1 ").append(sql);
		if(order == null){
			strb.append(ormTable.getSqlOrderSuffix());
		}else{
			strb.append(" ORDER BY ").append(order.deleteCharAt(order.length()-1));
		}
		return strb.toString();
	}

	@Override
	public List<Object> getArgs() {
		return args;
	}
	
	public Cmd and(String col, Object value){
		return this.and(col, "=", value);
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
			if(value == null){
				throw new IllegalArgumentException("value == null");
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
				throw new IllegalArgumentException("value == null");
			}else{
				sql.append(" AND t.").append(col).append(" > ?");
				args.add(value);
			}
		} else if(">=".equals(opt)){
			if(value == null){
				throw new IllegalArgumentException("value == null");
			}else{
				sql.append(" AND t.").append(col).append(" >= ?");
				args.add(value);
			}
		} else if("<".equals(opt)){
			if(value == null){
				throw new IllegalArgumentException("value == null");
			}else{
				sql.append(" AND t.").append(col).append(" < ?");
				args.add(value);
			}
		} else if("<=".equals(opt)){
			if(value == null){
				throw new IllegalArgumentException("value == null");
			}else{
				sql.append(" AND t.").append(col).append(" <= ?");
				args.add(value);
			}
		} else if("in".equalsIgnoreCase(opt)){
			if(value == null){
				throw new IllegalArgumentException("value == null");
			}else{
				List<Object> list = (List<Object>)value;
				if(list.size() < 1){
					throw new IllegalArgumentException("value length = 0");
				}else if(list.size() == 1){
					sql.append(" AND t.").append(col).append(" = ?");
					args.add(list.get(0));
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
				throw new IllegalArgumentException("value == null");
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
		order.append(col).append(" ASC,");
		return this;
	}
	
	public Cmd desc(String col){
		if(order == null){
			order = new StringBuilder();
		}
		order.append(col).append(" DESC,");
		return this;
	}
}
