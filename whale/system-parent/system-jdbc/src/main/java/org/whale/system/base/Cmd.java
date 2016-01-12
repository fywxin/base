package org.whale.system.base;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import org.whale.system.common.util.Strings;
import org.whale.system.jdbc.orm.OrmContext;
import org.whale.system.jdbc.orm.entry.OrmTable;
import org.whale.system.spring.SpringContextHolder;

/**
 * 单表查询构造器, AND 条件联合查询
 * col 支持java字段和sql字段
 * 参考nutz  OR 以后加入
 * 
 * @author wjs
 *
 */
public class Cmd implements Iquery{
	
	private OrmTable ormTable;
	
	//查询条件
	private StringBuilder select;
	//查询表对象
	private StringBuilder from;
	//查询条件
	private StringBuilder where  = new StringBuilder();
	//排序
	private StringBuilder order;
	//limit
	private StringBuilder limit;
	//参数
	private List<Object> args = new ArrayList<Object>();
	
	private String delSql;
	
	private String getSql;
	
	private String querySql;
	
	private String countSql;
	
	
	public static Cmd newCmd(Class<?> clazz){
		return new Cmd(clazz);
	}
	
	public Cmd(Class<?> clazz) {
        OrmContext ormContext = SpringContextHolder.getBean(OrmContext.class);
		ormTable = ormContext.getOrmTable(clazz);
    }
	
	@Override
	public String getSql(SqlType sqlType) {
		if(sqlType == null){
			return this.getQuerySql();
		}else if(sqlType == SqlType.GET){
			return this.getGetSql();
		}else if(sqlType == SqlType.COUNT){
			return this.getCountSql();
		}else if(sqlType == SqlType.DEL){
			return this.getDelSql();
		}else{
			return this.getQuerySql();
		}
	}

	@Override
	public Object[] getArgs() {
		return args.toArray();
	}
	
	
	public String getDelSql() {
		if(delSql == null){
			StringBuilder strb = new StringBuilder();
			strb.append("DELETE t FROM ").append(ormTable.getTableDbName()).append(" t WHERE 1=1 ").append(where.toString());
			if(limit != null){
				strb.append(limit);
			}
			delSql = strb.toString();
		}
		
		return delSql;
	}
	
	public String getGetSql() {
		if(getSql == null){
			StringBuilder strb = new StringBuilder();
			strb.append(ormTable.getSqlHeadPrefix()).append(" WHERE 1=1 ").append(where.toString());
			if(limit != null){
				strb.append(limit);
			}
			
			getSql = strb.toString();
		}
		return getSql;
	}

	public String getQuerySql() {
		if(querySql == null){
			StringBuilder strb = new StringBuilder();
			
			if(this.select == null){
				if(this.from == null){
					strb.append(ormTable.getSqlHeadPrefix());
				}else{
					strb.append("SELECT ").append(ormTable.getSqlColPrexs()).append(" FROM ").append(ormTable.getTableDbName()).append(" t").append(from);
				}
			}else{
				if(select.charAt(select.length()-1) == ','){
					select.deleteCharAt(select.length()-1);
				}
				if(this.from == null){
					strb.append("SELECT ").append(select).append(" FROM ").append(ormTable.getTableDbName()).append(" t");
				}else{
					strb.append("SELECT ").append(select).append(" FROM ").append(ormTable.getTableDbName()).append(" t").append(from);
				}
			}
			
			strb.append(" WHERE 1=1 ").append(where);
			
			if(order == null){
				strb.append(ormTable.getSqlOrderSuffix());
			}else{
				strb.append(" ORDER BY ").append(order.deleteCharAt(order.length()-1));
			}
			
			if(this.limit != null){
				strb.append(limit);
			}
			
			querySql = strb.toString();
		}
		return querySql;
	}
	
	
	public String getCountSql() {
		if(countSql == null){
			StringBuilder strb = new StringBuilder();
			strb.append("SELECT COUNT(1) FROM ").append(ormTable.getTableDbName()).append(" t WHERE 1=1 ").append(where.toString());
			if(limit != null){
				strb.append(limit);
			}
			countSql = strb.toString();
		}
		return countSql;
	}
	
	
	
	/**
	 * 加入查询返回字段
	 * 只适用于分页
	 * 
	 * @param field
	 * @return
	 */
	public Cmd select(String... cols){
		if(this.select == null){
			select = new StringBuilder();
		}
		
		if(cols == null || cols.length < 1){
			select.append(ormTable.getSqlHeadPrefix().substring(6));
		}else{
			for(String col : cols){
				//自定义字段
				if(col.indexOf(")") != -1 || col.toLowerCase().indexOf("from") != -1){
					select.append(col).append(",");
				}else{
					select.append("t.").append(this.fixCol(col)).append(",");
				}
			}
			select.deleteCharAt(select.length()-1);
		}
		
		return this;
	}
	
	/**
	 * 加入自定义Sql
	 * @param sql
	 * @return
	 */
	public Cmd selectWrap(String sql){
		if(this.select == null){
			select = new StringBuilder();
		}
		select.append(sql.trim());
		return this;
	}
	
	/**
	 * 查询当前表
	 * 
	 * @return
	 */
	public Cmd from(){
		
		return this;
	}
	
	/**
	 * 多表查询拼接
	 * @param sql
	 * @return
	 */
	public Cmd fromWrap(String sql){
		if(this.from == null){
			from = new StringBuilder();
		}
		from.append(sql);
		return this;
	}
	
	public Cmd where(){
		return this;
	}
	
	/**
	 * 加入自定义Sql
	 * @param sql
	 * @param value
	 * @return
	 */
	@SuppressWarnings("all")
	public Cmd whereWrap(String sql, Object value){
		if(this.where == null){
			this.where = new StringBuilder();
		}
		this.where.append(sql);
		
		if(value != null){
			if(value instanceof Collection){
				this.args.addAll((Collection)value);
			}else{
				this.args.add(value);
			}
		}
		
		return this;
	}
	
	/**
	 * 按字段递增
	 * @param col
	 * @return
	 */
	public Cmd asc(String col){
		if(order == null){
			order = new StringBuilder();
		}
		col = this.fixCol(col);
		order.append(col).append(" ASC,");
		return this;
	}
	
	/**
	 * 按字段递减
	 * @param col
	 * @return
	 */
	public Cmd desc(String col){
		if(order == null){
			order = new StringBuilder();
		}
		col = this.fixCol(col);
		order.append(col).append(" DESC,");
		return this;
	}
	
	/**
	 * 记录查询范围限制
	 * @param from
	 * @param size
	 * @return
	 */
	public Cmd limit(int from, int size){
		if(this.limit == null){
			limit = new StringBuilder();
		}
		limit.append(" LIMIT ").append(from).append(", ").append(size);
		
		return this;
	}

	/**
	 * 创建 and = 条件语句
	 * null 不查询
	 * @param col
	 * @param value
	 * @return
	 */
	public Cmd eq(String col, Object value){
		if(value == null){
			return this;
		}
		return this.and(col, "=", value);
	}
	
	/**
	 * 创建 and like 条件语句
	 * null 不查询
	 * @param col
	 * @param value
	 * @return
	 */
	public Cmd like(String col, Object value){
		if(value == null){
			return this;
		}
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
				where.append(" AND t.").append(col).append(" IS NULL");
			}else{
				where.append(" AND t.").append(col).append(" = ?");
				args.add(value);
			}
		} else if("!=".equals(opt)){
			if(value == null){
				where.append(" AND t.").append(col).append(" IS NOT NULL");
			}else{
				where.append(" AND t.").append(col).append(" != ?");
				args.add(value);
			}
		} else if("like".equalsIgnoreCase(opt)){
			if(value == null || Strings.isBlank(value.toString())){
				return this;
			}else{
				where.append(" AND t.").append(col).append(" LIKE ?");
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
				where.append(" AND t.").append(col).append(" > ?");
				args.add(value);
			}
		} else if(">=".equals(opt)){
			if(value == null){
				return this;
			}else{
				where.append(" AND t.").append(col).append(" >= ?");
				args.add(value);
			}
		} else if("<".equals(opt)){
			if(value == null){
				return this;
			}else{
				where.append(" AND t.").append(col).append(" < ?");
				args.add(value);
			}
		} else if("<=".equals(opt)){
			if(value == null){
				return this;
			}else{
				where.append(" AND t.").append(col).append(" <= ?");
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
						where.append(" AND t.").append(col).append(" IS NULL");
					}else{
						where.append(" AND t.").append(col).append(" = ?");
						args.add(list.get(0));
					}
				}else{
					where.append(" AND t.").append(col).append(" IN (");
					for(Object val : list){
						where.append("?,");
					}
					where.deleteCharAt(where.length()-1);
					where.append(") "); 
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
				
				where.append(" AND t.").append(col).append(" BETWEEN ? AND ? ");
				args.addAll(list);
			}
		} else {
			throw new IllegalArgumentException("opt["+opt+"] 值必须是 [=, !=, like, >, >=, <, <=, in, between]");
		}
		return this;
	}
	
	private String fixCol(String col){
		String newCol = this.ormTable.getJavaAsSqlColumn().get(col);
		if(Strings.isNotBlank(newCol)){
			col = newCol;
		}
		return col;
	}

	@Override
	public Class<?> rsClazz() {
		return null;
	}

	
}
