package org.whale.system.jdbc.filter.dll.impl;

import java.io.Serializable;
import java.util.List;
import java.util.Map;

import org.whale.system.common.exception.FieldValidErrorException;
import org.whale.system.common.util.ValidUtil;
import org.whale.system.jdbc.IOrmDao;
import org.whale.system.jdbc.filter.dll.BaseDaoDllFilterWarpper;
import org.whale.system.validation.ValidOrmUtil;

/**
 * @Valition 注释解释拦截器
 *
 * @author wjs
 * 2015年3月9日 下午7:34:24
 */

public class ValidateCheckFilter<T extends Serializable,PK extends Serializable> extends BaseDaoDllFilterWarpper<T, PK> {
	
	private void validate(T obj){
		Map<String, String> map = ValidOrmUtil.valid(obj);
		if(map != null && map.size() > 0){
			throw new FieldValidErrorException(ValidUtil.formatMsg(map, "</br>"), map);
		}
	}
	
	private void validate(List<T> objs){
		for(T obj : objs){
			this.validate(obj);
		}
	}
	
	@Override
	public void beforeSave(T obj, IOrmDao<T, PK> baseDao) {
		this.validate(obj);
	}

	@Override
	public void beforeSaveBatch(List<T> objs, IOrmDao<T, PK> baseDao) {
		this.validate(objs);
	}

	@Override
	public void beforeUpdate(T obj, IOrmDao<T, PK> baseDao) {
		this.validate(obj);
	}

	@Override
	public void beforeUpdateBatch(List<T> objs, IOrmDao<T, PK> baseDao) {
		this.validate(objs);
	}

	@Override
	public int getOrder() {
		return 100;
	}

	

}
