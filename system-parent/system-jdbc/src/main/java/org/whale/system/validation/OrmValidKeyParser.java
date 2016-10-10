package org.whale.system.validation;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.whale.system.common.util.ValidKeyParser;
import org.whale.system.jdbc.orm.OrmContext;
import org.whale.system.jdbc.orm.entry.OrmColumn;
import org.whale.system.jdbc.orm.entry.OrmTable;

@Component
public class OrmValidKeyParser implements ValidKeyParser {

	@Autowired
	private OrmContext ormContext;
	
	@Override
	public String parseKey(Object obj, String key) {
		OrmTable ormTable = this.ormContext.getOrmTable(obj.getClass());
		if(ormTable == null)
			return key;
		OrmColumn ormColumn = ormTable.getOrmColMap().get(key);
		if(ormColumn == null)
			return key;
		return ormColumn.getCnName();
	}

	@Override
	public boolean support(Object obj) {
		return this.ormContext.contain(obj.getClass());
	}
	
}
