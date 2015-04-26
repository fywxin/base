package org.whale.ext.mixDao.orm;

import java.util.HashMap;
import java.util.Map;

import org.springframework.stereotype.Component;
import org.whale.system.jdbc.orm.entry.OrmTable;
import org.whale.system.jdbc.orm.table.TableExtInfoReader;

/**
 * 
 *
 * @author 王金绍
 * 2015年4月26日 上午9:26:22
 */
@Component
public class MixOrmReader implements TableExtInfoReader {

	public int getOrder() {
		return 10;
	}

	public void readExtInfo(OrmTable ormTable) {
		MixOrm mixCache = (MixOrm)ormTable.getClazz().getAnnotation(MixOrm.class);
		
		MixOrmEntry ormMixCache = new MixOrmEntry();
		ormMixCache.setCacheName(mixCache.cacheName());
		ormMixCache.setCacheTime(mixCache.cacheTime());
		ormMixCache.setOrmTable(ormTable);
		
		Map<String, Object> extInfo = ormTable.getExtInfo();
		if(extInfo == null){
			extInfo = new HashMap<String, Object>();
			ormTable.setExtInfo(extInfo);
		}
		
		extInfo.put(MixOrmEntry.MIX_ORM_KEY, ormMixCache);
	}

	public boolean match(OrmTable ormTable) {
		MixOrm mixCache = (MixOrm)ormTable.getClazz().getAnnotation(MixOrm.class);
		return mixCache != null;
	}

}
