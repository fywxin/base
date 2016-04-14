package org.whale.system.jdbc.orm.entry;

import java.util.List;

/**
 * 将运行时objcet对象依照OrmSql对象结构将值取出
 *
 * @author wjs
 * 2014年9月6日-下午1:52:10
 */
public class OrmValue extends OrmSql {

	/**值 */
	private Object[] args;
	/**批量值 */
	private List<Object[]> batchArgs;
	
	public OrmValue(){
		super();
	}
	
	public OrmValue(OrmSql ormSql){
		super();
		this.setArgTypes(ormSql.getArgTypes());
		this.setCols(ormSql.getCols());
		this.setFields(ormSql.getFields());
		this.setOpType(ormSql.getOpType());
		this.setSql(ormSql.getSql());
		this.setTable(ormSql.getTable());
	}

	public Object[] getArgs() {
		return args;
	}

	public void setArgs(Object[] args) {
		this.args = args;
	}

	public List<Object[]> getBatchArgs() {
		return batchArgs;
	}

	public void setBatchArgs(List<Object[]> batchArgs) {
		this.batchArgs = batchArgs;
	}
	
}
