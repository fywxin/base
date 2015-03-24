package org.whale.system.jdbc.orm.entry;

import java.lang.reflect.Field;
import java.util.List;

/**
 * orm对象元注释相对应不同操作类型的sql语句与字段信息，本结构在系统初始化时生成
 *
 * @author 王金绍
 * 2014年9月6日-下午1:51:46
 */
public class OrmSql {
	/**添加操作 */
	public static final int OPT_SAVE = 0;
	/**批量添加操作 */
	public static final int OPT_SAVE_BATCH = 1;
	/**更新操作 */
	public static final int OPT_UPDATE = 2;
	/**删除操作 */
	public static final int OPT_DELETE = 3;
	/**获取对象操作 */
	public static final int OPT_GET = 4;
	/**获取所有对象操作 */
	public static final int OPT_GET_ALL = 5;
	/**最大下标操作  标记作用 */
	public static final int OPT_MAX = 6;
	
	
	
	//未做缓存，动态生成的sql语句
	/**查询操作 */
	public static final int OPT_QUERY = -1;
	/**更新非空字段操作 */
	public static final int OPT_UPDATE_ONLY = -2;
	/**按字段删除操作 */
	public static final int OPT_DELETE_BY = -3;
	
	/**操作类型 */
	private int opType;
	/**生成的sql */
	private String sql;
	/**sql中参数字段 */
	private List<OrmColumn> cols;
	/**sql中参数字段反射 */
	private List<Field> fields;
	/**sql中参数字段对应的类型 */
	private int[] argTypes;
	
	/**所属实体 */
	private OrmTable table;

	public int getOpType() {
		return opType;
	}

	public void setOpType(int opType) {
		this.opType = opType;
	}

	public String getSql() {
		return sql;
	}

	public void setSql(String sql) {
		this.sql = sql;
	}

	public List<OrmColumn> getCols() {
		return cols;
	}

	public void setCols(List<OrmColumn> cols) {
		this.cols = cols;
	}

	public List<Field> getFields() {
		return fields;
	}

	public void setFields(List<Field> fields) {
		this.fields = fields;
	}

	public int[] getArgTypes() {
		return argTypes;
	}

	public void setArgTypes(int[] argTypes) {
		this.argTypes = argTypes;
	}

	public OrmTable getTable() {
		return table;
	}

	public void setTable(OrmTable table) {
		this.table = table;
	}

	@Override
	public String toString() {
		return "[sql=" + sql + "]";
	}
	
}
