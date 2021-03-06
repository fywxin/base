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

/**
 * 单表查询构造器, AND 条件联合查询
 * col 支持java字段和sql字段
 * 参考nutz  OR 以后加入
 *
 * http://www.jooq.org/
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
	//group 分组
	private StringBuilder groupBy;
	//group 分组条件
	private String having;

	//参数
	private List<Object> args = new ArrayList<Object>();
	
	private String delSql;
	
	private String getSql;
	
	private String querySql;
	
	private String countSql;

	private String tail;
	
	
	public static Q newQ(Class<?> clazz){
		return new Q(clazz);
	}
	
	public Q(Class<?> clazz) {
		ormTable = OrmContext.getThis().getOrmTable(clazz);
    }
	
	@Override
	public String getSql(SqlType sqlType) {
		if(sqlType == null || sqlType == SqlType.QUERY){
			return this.sqlQuery();
		}else if(sqlType == SqlType.GET){
			return this.sqlGet();
		}else if(sqlType == SqlType.COUNT){
			return this.sqlCount();
		}else if(sqlType == SqlType.DEL){
			return this.sqlDel();
		}else{
			return this.sqlQuery();
		}
	}

	public String sqlDel() {
		if(delSql == null){
			StringBuilder strb = new StringBuilder();
			strb.append("DELETE FROM ").append(ormTable.getTableDbName()).append(" WHERE 1=1 ").append(where.toString());
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
		if (Strings.isNotBlank(tail)){
			delSql += tail;
		}
		if(logger.isDebugEnabled()){
			logger.debug("del: [\n" + delSql + "\n" + JSON.toJSONString(args) + "\n]");
		}
		return delSql;
	}
	
	public String sqlGet() {
		if(getSql == null){
			StringBuilder strb = new StringBuilder(50);
			strb.append(ormTable.getSqlHeadPrefix()).append(" WHERE 1=1 ").append(where.toString());
			
			if (groupBy != null){
				strb.append(" GROUP BY ").append(groupBy);
				if (having != null){
					strb.append(" HAVING ").append(having);
				}
			}

			if(limit != null){
				if(order == null){
					strb.append(ormTable.getSqlOrderSuffix());
				}else{
					strb.append(" ORDER BY ").append(order.deleteCharAt(order.length()-1));
				}
				strb.append(limit);
			}else{
				strb.append(" limit 1");
			}
			getSql = strb.toString();

		}
		if (Strings.isNotBlank(tail)){
			getSql += tail;
		}

		if(logger.isDebugEnabled()){
			logger.debug("get: [\n"+getSql+"\n"+JSON.toJSONString(args)+"\n]");
		}
		return getSql;
	}

	public String sqlQuery() {
		if(querySql == null){
			StringBuilder strb = new StringBuilder(200);
			
			if(this.select == null){
				if(this.from == null){
					strb.append(ormTable.getSqlHeadPrefix());
				}else{
					strb.append("SELECT ").append(ormTable.getSqlColPrexs()).append(" FROM ").append(ormTable.getTableDbName()).append(from);
				}
			}else{
				if(select.charAt(select.length()-1) == ','){
					select.deleteCharAt(select.length()-1);
				}
				if(this.from == null){
					strb.append("SELECT ").append(select).append(" FROM ").append(ormTable.getTableDbName());
				}else{
					strb.append("SELECT ").append(select).append(" FROM ").append(ormTable.getTableDbName()).append(from);
				}
			}
			
			strb.append(" WHERE 1=1 ").append(where);

			if (groupBy != null){
				strb.append(" GROUP BY ").append(groupBy);
				if (having != null){
					strb.append(" HAVING ").append(having);
				}
			}
			
			if(order == null){
				strb.append(ormTable.getSqlOrderSuffix());
			}else{
				strb.append(" ORDER BY ").append(order.deleteCharAt(order.length() - 1));
			}
			
			if(this.limit != null){
				strb.append(limit);
			}
			
			querySql = strb.toString();
		}
		if (Strings.isNotBlank(tail)){
			querySql += tail;
		}
		if(logger.isDebugEnabled()){
			logger.debug("query: [\n" + querySql + "\n" + JSON.toJSONString(args) + "\n]");
		}
		return querySql;
	}
	
	
	public String sqlCount() {
		if(countSql == null){
			StringBuilder strb = new StringBuilder(100);
			strb.append("SELECT COUNT(*) FROM ").append(ormTable.getTableDbName()).append(" WHERE 1=1 ").append(where.toString());
			if(limit != null){
				strb.append(limit);
			}
			countSql = strb.toString();
		}
		if (Strings.isNotBlank(tail)){
			countSql += tail;
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
				select.append(col).append(",");
			}
			select.deleteCharAt(select.length() - 1);
		}
		
		return this;
	}
	
//------------------------------------------------------------------------------------------------------

	/**
	 * 等于 =
	 *   null 不查询使用and
	 * @param col
	 * @param value
	 * @return
	 */
	public Q eq(String col, Object value){
		if(value != null){
			where.append(" AND ").append(col).append(" = ?");
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
		if(value != null && Strings.isNotBlank((String) value)){
			where.append(" AND ").append(col).append(" LIKE ?");
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
	 * like 不区分大小写
	 * @param col
	 * @param value
	 * @return
	 */
	public Q likeIgnoreCase(String col, Object value){
		if(value != null && Strings.isNotBlank((String) value)){
			where.append(" AND upper(").append(col).append(") LIKE ?");
			String val = value.toString().trim().toUpperCase();
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
	 *   null 不查询使用and
	 * @param col
	 * @param value
	 * @return
	 */
	public Q notEq(String col, Object value){
		if(value != null){
			where.append(" AND ").append(col).append(" != ?");
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
		if(value != null){
			where.append(" AND ").append(col).append(" > ?");
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
		if(value != null){
			where.append(" AND ").append(col).append(" >= ?");
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
		if(value != null){
			where.append(" AND ").append(col).append(" < ?");
			args.add(value);
		}
		return this;
	}

	/**
	 * 小于等于 <=
	 * @param col
	 * @param value
	 * @return
	 */
	public Q ltEq(String col, Object value) {
		if(value != null){
			where.append(" AND ").append(col).append(" <= ?");
			args.add(value);
		}
		return this;
	}

	/**
	 * 范围查询 in
	 * @param col
	 * @param list
	 * @return
	 */
	public Q in(String col, Collection list){
		if(list == null || list.size() < 1){
			return this;
		}else if(list.size() == 1){
			return this.eq(col, list.iterator().next());
		}else{
			where.append(" AND ").append(col).append(" IN (");
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
	 * 不在范围查询 not in
	 * @param col
	 * @param list
	 * @return
	 */
	public Q notIn(String col, Collection list){
		if(list == null || list.size() < 1){
			return this;
		}else if(list.size() == 1){
			return this.eq(col, list.iterator().next());
		}else{
			where.append(" AND ").append(col).append(" NOT IN (");
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
	 * 区间查询 between
	 * @param col
	 * @param from
	 * @param to
	 * @return
	 */
	public Q between(String col, Object from, Object to){
		if(from == null || to == null){
			throw new IllegalArgumentException("value must not null");
		}

		where.append(" AND ").append(col).append(" BETWEEN ? AND ? ");
		args.add(from);
		args.add(to);
		return this;
	}

	/**
	 * 自适应模式
	 *
	 * @param condition
	 * @param values
	 * @return
	 */
	public Q and(String condition, Object... values){
		where.append(" AND ").append(condition);
		if (values != null && values.length > 0){
			for (Object obj : values){
				args.add(obj);
			}
		}
		return this;
	}

	/**
	 * 分组
	 * @param cols
	 * @return
	 */
	public Q groupBy(String... cols) {
		if (cols.length == 0){
			return this;
		}
		if (groupBy == null){
			groupBy = new StringBuilder();
		}
		for (String col : cols){
			groupBy.append(",").append(col);
		}
		groupBy.deleteCharAt(0);
		return this;
	}

	/**
	 * 分组条件
	 * @param groupConditions
	 * @return
	 */
	public Q having(String groupConditions){
		if (Strings.isNotBlank(groupConditions)){
			having = groupConditions;
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
		order.append(col).append(" ASC,");
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
		order.append(col).append(" DESC,");
		return this;
	}

	/**
	 *
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

	public void tail(String tail){
		this.tail = " "+tail;
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
