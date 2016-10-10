package org.whale.system.validation;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.whale.system.common.util.ValidUtil;
import org.whale.system.jdbc.orm.OrmContext;
import org.whale.system.jdbc.orm.entry.OrmColumn;

public class ValidOrmUtil {
	
	/**
	 * 校验
	 * @param obj  对象
	 * @return <属性，错误信息>
	 */
	public static Map<String, String> valid(Object obj){
		return valid(obj, false);
	}
	
	/**
	 * 校验
	 * 
	 * @param obj 对象
	 * @param append 是否累积属性的错误信息
	 * @return
	 */
	public static Map<String, String> valid(Object obj, boolean append){
		OrmContext ormContext = OrmContext.getThis();
		if(ormContext.contain(obj.getClass())){
			List<OrmColumn> cols = ormContext.getOrmTable(obj.getClass()).getValidateCols();
			if(cols == null || cols.size() < 1)
				return null;
			Map<String, String> map = new HashMap<String, String>();
			String msg = null;
			for(OrmColumn col : cols){
				msg = ValidUtil.valid(obj, col.getField(), append);
				if(msg != null){
					map.put(col.getCnName(), msg);
				}
			}
			return map;
		}else{
			return ValidUtil.valid(obj, append);
		}
	}

}
