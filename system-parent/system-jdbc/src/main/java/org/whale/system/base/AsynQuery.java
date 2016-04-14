package org.whale.system.base;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.whale.system.common.util.Strings;
import org.whale.system.jdbc.orm.OrmContext;
import org.whale.system.jdbc.orm.entry.OrmTable;
import org.whale.system.spring.SpringContextHolder;

/**
 * 单表查询构造器, AND 条件联合查询
 * 不支持OR
 * 
 * @TODO 参考mybatis SQL
 * 		@org.apache.ibatis.jdbc.SQL
 * 		https://github.com/nutzam/nutz/blob/master/src/org/nutz/dao/Cnd.java
 * 
 * @author wjs
 */
public class AsynQuery implements Iquery{

	private Class<?> clazz;
	/**等于 */
	private Map<String, Object> eqParams;
	/**等于 */
	private Map<String, Object> likeParams;
	/**不等于 */
	private Map<String, Object> neParams;
	/**大于 */
	private Map<String, Object> gtParams;
	/**大于等于 */
	private Map<String, Object> gtEqParams;
	/**小于 */
	private Map<String, Object> ltParams;
	/**小于等于 */
	private Map<String, Object> ltEqParams;
	/**in */
	private Map<String, Set<Object>> inArrayParams;
	/**between */
	private Map<String, AsynQuery.Between> betweenParams;
	/**排序 */
	private List<AsynQuery.Order> orders;
	
	private String sql;
	
	private List<Object> args = new ArrayList<Object>();
	
	public static AsynQuery newQuery(Class<?> clazz){
		return new AsynQuery(clazz);
	}
	
	public AsynQuery(Class<?> clazz) {
        this.clazz = clazz;
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
	
	/**
	 * SQL语句 参数值 列表
	 * @return
	 */
	public Object[] getArgs() {
		if(args.size() < 1){
			this.bulidAfterFromSql();
		}
		return args.toArray();
	}

	/**
	 * delBy SQL 语句
	 * @return
	 */
	public String getDelSql(){
		OrmContext ormContext = SpringContextHolder.getBean(OrmContext.class);
		OrmTable ormTable = ormContext.getOrmTable(clazz);
		
		StringBuilder strb = new StringBuilder();
		strb.append("DELETE FROM ").append(ormTable.getTableDbName()).append(" t ").append(this.bulidAfterFromSql());
		
		return strb.toString();
	}
	
	public String getGetSql() {
		OrmContext ormContext = SpringContextHolder.getBean(OrmContext.class);
		OrmTable ormTable = ormContext.getOrmTable(clazz);
		
		StringBuilder strb = new StringBuilder();
		strb.append(ormTable.getSqlHeadPrefix()).append(this.bulidAfterFromSql());
		return strb.toString();
	}
	
	/**
	 * queryBy getBy SQL 语句
	 * 
	 * @return
	 */
	public String getQuerySql() {
		OrmContext ormContext = SpringContextHolder.getBean(OrmContext.class);
		OrmTable ormTable = ormContext.getOrmTable(clazz);
		
		StringBuilder strb = new StringBuilder();
		strb.append(ormTable.getSqlHeadPrefix()).append(this.bulidAfterFromSql());
		if(orders == null){
			strb.append(ormTable.getSqlOrderSuffix());
		}else{
			strb.append(" ORDER BY ");
			for(AsynQuery.Order order : orders){
				strb.append("t.").append(order.getCol()).append(order.isAsc() ? " ASC,": " DESC,");
			}
			strb.deleteCharAt(strb.length()-1);
		}
		return strb.toString();
	}
	
	public String getCountSql() {
		OrmContext ormContext = SpringContextHolder.getBean(OrmContext.class);
		OrmTable ormTable = ormContext.getOrmTable(clazz);
		StringBuilder strb = new StringBuilder();
		strb.append("SELECT COUNT(1) FROM ").append(ormTable.getTableDbName()).append(" t ").append(this.bulidAfterFromSql());
		
		return strb.toString();
	}
	
	
	
	/**
	 * 创建 查询条件后缀
	 * 注意线程安全与效率
	 * @return
	 */
	private String bulidAfterFromSql(){
		if(sql != null){
			return sql;
		}
		
		synchronized (this) {
			if(sql != null){
				return sql;
			}
			StringBuilder strb = new StringBuilder();
			strb.append(" WHERE 1=1 ");
			if(eqParams != null){
				for(Map.Entry<String, Object> eq : eqParams.entrySet()){
					if(eq.getValue() == null){
						strb.append(" AND t.").append(eq.getKey()).append(" IS NULL ");
					}else{
						strb.append(" AND t.").append(eq.getKey()).append(" = ? ");
						args.add(eq.getValue());
					}
				}
			}
			if(likeParams != null){
				for(Map.Entry<String, Object> like : likeParams.entrySet()){
					strb.append(" AND t.").append(like.getKey()).append(" LIKE ? ");
					String val = like.getValue().toString().trim();
					if(val.startsWith("%") || val.endsWith("%")){
						args.add(val);
					}else{
						args.add("%"+val+"%");
					}
				}
			}
			if(neParams != null){
				for(Map.Entry<String, Object> ne : neParams.entrySet()){
					if(ne.getValue() == null){
						strb.append(" AND t.").append(ne.getKey()).append(" IS NOT NULL ");
					}else{
						strb.append(" AND t.").append(ne.getKey()).append(" != ? ");
						args.add(ne.getValue());
					}
				}
			}
			if(gtParams != null){
				for(Map.Entry<String, Object> gt : gtParams.entrySet()){
					strb.append(" AND t.").append(gt.getKey()).append(" > ? ");
					args.add(gt.getValue());
				}
			}
			if(gtEqParams != null){
				for(Map.Entry<String, Object> gt : gtEqParams.entrySet()){
					strb.append(" AND t.").append(gt.getKey()).append(" >= ? ");
					args.add(gt.getValue());
				}
			}
			if(ltParams != null){
				for(Map.Entry<String, Object> lt : ltParams.entrySet()){
					strb.append(" AND t.").append(lt.getKey()).append(" < ? ");
					args.add(lt.getValue());
				}
			}
			if(ltEqParams != null){
				for(Map.Entry<String, Object> lt : ltEqParams.entrySet()){
					strb.append(" AND t.").append(lt.getKey()).append(" <= ? ");
					args.add(lt.getValue());
				}
			}
			if(inArrayParams != null){
				for(Map.Entry<String, Set<Object>> in : inArrayParams.entrySet()){
					strb.append(" AND t.").append(in.getKey()).append(" IN (");
					for(Object val : in.getValue()){
						strb.append("?,");
						args.add(val);
					}
					strb.deleteCharAt(strb.length()-1);
					strb.append(") "); 
				}
			}
			if(betweenParams != null){
				for(Map.Entry<String, AsynQuery.Between> between : betweenParams.entrySet()){
					strb.append(" AND t.").append(between.getKey()).append(" BETWEEN ? AND ? ");
					args.add(between.getValue().getBegin());
					args.add(between.getValue().getEnd());
				}
			}
			sql = strb.toString();
			return sql;
		}
	}
	
	
	

	/**
	 * 增加 等于[=] 查询条件
	 * @param col
	 * @param value
	 * @return
	 */
	public AsynQuery eq(String col, Object value){
		if(eqParams == null){
			eqParams = new HashMap<String, Object>();
		}
		eqParams.put(col, value);
		return this;
	}
	
	/**
	 * 添加  不等于[!=] 查询条件
	 * @param col
	 * @param value
	 * @return
	 */
	public AsynQuery ne(String col, Object value){
		if(neParams == null){
			neParams = new HashMap<String, Object>();
		}
		neParams.put(col, value);
		return this;
	}
	
	/**
	 * 添加  大于[>] 查询条件
	 * @param col
	 * @param value
	 * @return
	 */
	public AsynQuery gt(String col, Object value){
		if(value == null)
			throw new IllegalArgumentException("value == null");
		if(gtParams == null){
			gtParams = new HashMap<String, Object>();
		}
		gtParams.put(col, value);
		return this;
	}
	
	/**
	 * 添加  大于等于[>=] 查询条件
	 * @param col
	 * @param value
	 * @return
	 */
	public AsynQuery gtEq(String col, Object value){
		if(value == null)
			throw new IllegalArgumentException("value == null");
		if(gtEqParams == null){
			gtEqParams = new HashMap<String, Object>();
		}
		gtEqParams.put(col, value);
		return this;
	}
	
	/**
	 * 添加  小于[<] 查询条件
	 * @param col
	 * @param value
	 * @return
	 */
	public AsynQuery lt(String col, Object value){
		if(value == null)
			throw new IllegalArgumentException("value == null");
			
		if(ltParams == null){
			ltParams = new HashMap<String, Object>();
		}
		ltParams.put(col, value);
		return this;
	}
	
	/**
	 * 添加  小于等于[<=] 查询条件
	 * @param col
	 * @param value
	 * @return
	 */
	public AsynQuery ltEq(String col, Object value){
		if(value == null)
			throw new IllegalArgumentException("value == null");
			
		if(ltEqParams == null){
			ltEqParams = new HashMap<String, Object>();
		}
		ltEqParams.put(col, value);
		return this;
	}
	
	/**
	 * 添加  like[like] 查询条件
	 * @param col
	 * @param value
	 * @return
	 */
	public AsynQuery like(String col, Object value){
		if(Strings.isNotBlank(value)){
			if(likeParams == null){
				likeParams = new HashMap<String, Object>();
			}
			likeParams.put(col, value);
		}
		return this;
	}
	
	/**
	 * 添加  between[between] 查询条件
	 * @param col
	 * @param small
	 * @param big
	 * @return
	 */
	public AsynQuery between(String col, Object small, Object big){
		if(small == null || big == null)
			throw new IllegalArgumentException("between 值不能为null");
		if(likeParams == null){
			likeParams = new HashMap<String, Object>();
		}
		likeParams.put(col, new Between(small, big));
		return this;
	}
	
	/**
	 * 添加  in[in] 范围查找
	 * @param col
	 * @param objs
	 * @return
	 */
	public AsynQuery in(String col, Object... objs){
		if(objs == null || objs.length < 1){
			this.eq(col, null);
		}
		if(objs.length == 1){
			this.eq(col, objs[0]);
		}
		Set<Object> values = new HashSet<Object>(objs.length * 2);
		for(Object obj : objs){
			values.add(obj);
		}
		if(this.inArrayParams == null){
			this.inArrayParams = new HashMap<String, Set<Object>>();
		}
		this.inArrayParams.put(col, values);
		
		return this;
	}
	
	/**
	 * 添加自定义排序字段，为空则以@Order 的为准
	 * @param col 排序字段
	 * @param asc 是否递增
	 * @return
	 */
	public AsynQuery order(String col, boolean asc){
		if(this.orders == null)
			this.orders = new ArrayList<AsynQuery.Order>();
		this.orders.add(new Order(col, asc));
		return this;
	}

	private static class Between {
        Object begin;
        Object end;

        public Between(Object begin, Object end) {
            this.begin = begin;
            this.end = end;
        }

        public Object getBegin() {
            return this.begin;
        }

        public Object getEnd() {
            return this.end;
        }
    }
	
	private static class Order{
		
		private boolean asc = true;
		
		private String col;
		
		public Order(String col, boolean asc){
			this.col = col;
			this.asc = asc;
		}

		public boolean isAsc() {
			return asc;
		}

		public String getCol() {
			return col;
		}
	}

	@Override
	public Class<?> rsClazz() {
		// TODO Auto-generated method stub
		return null;
	}
	
}
