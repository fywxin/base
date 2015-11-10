package org.whale.system.jdbc.filter.dll.impl;

import java.io.Serializable;
import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Component;
import org.whale.system.common.exception.FieldValidErrorException;
import org.whale.system.jdbc.IOrmDao;
import org.whale.system.jdbc.filter.dll.BaseDaoDllFilterWarpper;
import org.whale.system.validation.ValidOrmUtil;

/**
 * @Valition 注释解释拦截器
 *
 * @author 王金绍
 * 2015年3月9日 下午7:34:24
 */
@Component
public class ValidateCheckFilter<T extends Serializable,PK extends Serializable> extends BaseDaoDllFilterWarpper<T, PK> {
	
	private void validate(T obj){
		Map<String, String> map = ValidOrmUtil.valid(obj);
		if(map != null && map.size() > 0){
			StringBuilder strb = new StringBuilder();
			for(Map.Entry<String, String> entry : map.entrySet()){
				strb.append(entry.getKey()).append(" : ").append(entry.getValue()).append("</br>");
			}
			throw new FieldValidErrorException(strb.toString(), map);
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
