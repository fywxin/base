package org.whale.system.base;

import java.util.List;

public interface Iquery {

	public String getDelSql();
	
	public String getGetSql();
	
	public String getQuerySql();
	
	public List<Object> getArgs();
}
