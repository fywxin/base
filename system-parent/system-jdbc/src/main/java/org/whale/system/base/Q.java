package org.whale.system.base;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import com.alibaba.fastjson.JSON;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
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
public class Q implements Iquery{

	private static Logger logger = LoggerFactory.getLogger(Q.class);

	private OrmTable ormTable;
	
	//返回字段
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
	
	
	public static Q newQ(Class<?> clazz){
		return new Q(clazz);
	}
	
	public Q(Class<?> clazz) {
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

	public String getDelSql() {
		if(delSql == null){
			StringBuilder strb = new StringBuilder();
			strb.append("DELETE t FROM ").append(ormTable.getTableDbName()).append(" t WHERE 1=1 ").append(where.toString());
			if(limit != null){
				if(order == null){
					strb.append(ormTable.getSqlOrderSuffix());
				}else{
					strb.append(" ORDER BY ").append(order.deleteCharAt(order.length()-1));
				}
				strb.append(limit);
			}
			delSql = strb.toString();
		}
		if(logger.isDebugEnabled()){
			logger.debug("del: [\n"+delSql+"\n"+JSON.toJSONString(args)+"\n]");
		}
		return delSql;
	}
	
	public String getGetSql() {
		if(getSql == null){
			StringBuilder strb = new StringBuilder(50);
			strb.append(ormTable.getSqlHeadPrefix()).append(" WHERE 1=1 ").append(where.toString());
			if(limit != null){
				if(order == null){
					strb.append(ormTable.getSqlOrderSuffix());
				}else{
					strb.append(" ORDER BY ").append(order.deleteCharAt(order.length()-1));
				}
				strb.append(limit);
			}
			getSql = strb.toString();

		}

		if(logger.isDebugEnabled()){
			logger.debug("get: [\n"+getSql+"\n"+JSON.toJSONString(args)+"\n]");
		}
		return getSql;
	}

	public String getQuerySql() {
		if(querySql == null){
			StringBuilder strb = new StringBuilder(200);
			
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
		if(logger.isDebugEnabled()){
			logger.debug("query: [\n"+querySql+"\n"+JSON.toJSONString(args)+"\n]");
		}
		return querySql;
	}
	
	
	public String getCountSql() {
		if(countSql == null){
			StringBuilder strb = new StringBuilder(100);
			strb.append("SELECT COUNT(1) FROM ").append(ormTable.getTableDbName()).append(" t WHERE 1=1 ").append(where.toString());
			if(limit != null){
				strb.append(limit);
			}
			countSql = strb.toString();
		}
		if(logger.isDebugEnabled()){
			logger.debug("count: [\n" + countSql + "\n" + JSON.toJSONString(args) + "\n]");
		}
		return countSql;
	}

//------------------------------------------------------------------------------------------------------

	/**
	 * 加入查询返回字段
	 * 只适用于分页
	 *
	 * select {col1}, {col2}, {col3}... from
	 * 
	 * @param cols
	 * @return
	 */
	public Q select(String... cols){
		if(this.select == null){
			select = new StringBuilder(100);
		}
		
		if(cols == null || cols.length < 1){
			select.append(ormTable.getSqlColPrexs());
		}else{
			for(String col : cols){
				//自定义字段
				if(col.indexOf(")") != -1 || col.toLowerCase().indexOf("from") != -1){
					select.append(col).append(",");
				}else{
					select.append("t.").append(this.fixCol(col)).append(",");
				}
			}
			select.deleteCharAt(select.length() - 1);
		}
		
		return this;
	}
	
	/**
	 * 加入自定义Sql
	 *  select {sql} from
	 *
	 *  select t.*, {appendSql} from -> this.select().selectWrap("{sql}")
	 *
	 * @param sql
	 * @return
	 */
	public Q selectWrap(String sql){
		if(this.select == null){
			select = new StringBuilder(50);
		}
		select.append(sql.trim());
		return this;
	}
	
//------------------------------------------------------------------------------------------------------

	/**
	 * 等于 =
	 * null 不查询
	 * @param col
	 * @param value
	 * @return
	 */
	public Q eq(String col, Object value){
		if(value == null){
			where.append(" AND t.").append(col).append(" IS NULL");
		}else{
			where.append(" AND t.").append(col).append(" = ?");
			args.add(value);
		}
		return this;
	}
	
	/**
	 * like
	 * null 不查询
	 * @param col
	 * @param value
	 * @return
	 */
	public Q like(String col, Object value){
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
		return this;
	}



	/**
	 * 不等于 !=
	 * @param col
	 * @param value
	 * @return
	 */
	public Q notEq(String col, Object value){
		if(value == null){
			where.append(" AND t.").append(col).append(" IS NOT NULL");
		}else{
			where.append(" AND t.").append(col).append(" != ?");
			args.add(value);
		}
		return this;
	}

	/**
	 * 大于 >
	 * @param col
	 * @param value
	 * @return
	 */
	public Q gt(String col, Object value){
		if(value == null){
			return this;
		}else{
			where.append(" AND t.").append(col).append(" > ?");
			args.add(value);
		}
		return this;
	}

	/**
	 * 大于等于 >=
	 * @param col
	 * @param value
	 * @return
	 */
	public Q gtEq(String col, Object value){
		if(value == null){
			return this;
		}else{
			where.append(" AND t.").append(col).append(" >= ?");
			args.add(value);
		}
		return this;
	}

	/**
	 * 小于  <
	 * @param col
	 * @param value
	 * @return
	 */
	public Q lt(String col, Object value) {
		if(value == null){
			return this;
		}else{
			where.append(" AND t.").append(col).append(" < ?");
			args.add(value);
		}
		return this;
	}

	/**
	 * 小于等于
	 * @param col
	 * @param value
	 * @return
	 */
	public Q ltEq(String col, Object value) {
		if(value == null){
			return this;
		}else{
			where.append(" AND t.").append(col).append(" <= ?");
			args.add(value);
		}
		return this;
	}

	/**
	 * in
	 * @param col
	 * @param list
	 * @return
	 */
	public Q in(String col, Collection<Object> list){
		if(list == null || list.size() < 1){
			return this;
		}else if(list.size() == 1){
			return this.eq(col, list.iterator().next());
		}else{
			where.append(" AND t.").append(col).append(" IN (");
			for(Object val : list){
				where.append("?,");
			}
			where.deleteCharAt(where.length()-1);
			where.append(") ");
			args.addAll(list);
		}
		return this;
	}

	/**
	 * between
	 * @param col
	 * @param from
	 * @param to
	 * @return
	 */
	public Q between(String col, Object from, Object to){
		if(from == null || to == null){
			throw new IllegalArgumentException("value must not null");
		}

		where.append(" AND t.").append(col).append(" BETWEEN ? AND ? ");
		args.add(from);
		args.add(to);
		return this;
	}


	/**
	 * 自适应模式
	 *
	 * @param condition
	 * @return
	 */
	public Q and(String condition){
		where.append(" AND ").append(condition);
		return this;
	}

	/**
	 * 自适应模式
	 *
	 * @param condition
	 * @param value
	 * @return
	 */
	public Q and(String condition, Object value){
		where.append(" AND ").append(condition);
		if (value != null){
			args.add(value);
		}
		return this;
	}

	/**
	 * 自适应模式
	 *
	 * @param condition
	 * @param values
	 * @return
	 */
	public Q and(String condition, List<Object> values){
		where.append(" AND ").append(condition);
		if (values != null && values.size() > 0){
			args.addAll(values);
		}
		return this;
	}
	
	/**
	 * 创建and 查询条件语句
	 * 
	 * @param col
	 * @param opt 可选值 [=, !=, like, >, >=, <, <=, in, between]
	 * @param value
	 * @return

	@SuppressWarnings("all")
	@Deprecated
	public Q and(String col, String opt, Object value){
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
				Collection<Object> list = (Collection<Object>)value;
				if(list.size() < 1){
					return this;
				}else if(list.size() == 1){
					Object data = list.iterator().next();
					if(data == null){
						where.append(" AND t.").append(col).append(" IS NULL");
					}else{
						where.append(" AND t.").append(col).append(" = ?");
						args.add(data);
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
				Collection<Object> list = (Collection<Object>)value;
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
	 */

	/**
	 * 按字段递增
	 * @param col
	 * @return
	 */
	public Q asc(String col){
		if(order == null){
			order = new StringBuilder(20);
		}
		col = this.fixCol(col);
		order.append("t.").append(col).append(" ASC,");
		return this;
	}

	/**
	 * 按字段递减
	 * @param col
	 * @return
	 */
	public Q desc(String col){
		if(order == null){
			order = new StringBuilder(20);
		}
		col = this.fixCol(col);
		order.append("t.").append(col).append(" DESC,");
		return this;
	}

	/**
	 * 记录查询范围限制
	 * @param from
	 * @param size
	 * @return
	 */
	public Q limit(int from, int size){
		if(this.limit == null){
			limit = new StringBuilder(20);
		}
		limit.append(" LIMIT ").append(from).append(", ").append(size);

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

	@Override
	public Object[] getArgs() {
		return args.toArray();
	}
}
