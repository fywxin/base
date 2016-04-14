package org.whale.system.jdbc.orm.entry;

import java.util.List;

import org.springframework.jdbc.core.RowMapper;

public class OrmClass {

	private OrmTable ormTable;
	
	//保存不同操作类型的OrmSql结构，OrmSql操作类型为其在OrmSql中的下标
	private List<OrmSql> ormSqls;
	
	private RowMapper<?> rowMapper;

	public OrmTable getOrmTable() {
		return ormTable;
	}

	public void setOrmTable(OrmTable ormTable) {
		this.ormTable = ormTable;
	}

	public List<OrmSql> getOrmSqls() {
		return ormSqls;
	}

	public void setOrmSqls(List<OrmSql> ormSqls) {
		this.ormSqls = ormSqls;
	}

	public RowMapper<?> getRowMapper() {
		return rowMapper;
	}

	public void setRowMapper(RowMapper<?> rowMapper) {
		this.rowMapper = rowMapper;
	}

	@Override
	public String toString() {
		return "OrmClass [ormTable=" + ormTable + ", ormSqls=" + ormSqls
				+ ", rowMapper=" + rowMapper + "]";
	}
}
