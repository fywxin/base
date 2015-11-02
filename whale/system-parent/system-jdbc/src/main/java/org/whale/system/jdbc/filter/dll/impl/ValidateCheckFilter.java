package org.whale.system.jdbc.filter.dll.impl;

import java.io.Serializable;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.whale.system.common.exception.BusinessException;
import org.whale.system.common.util.LangUtil;
import org.whale.system.jdbc.IOrmDao;
import org.whale.system.jdbc.filter.dll.BaseDaoDllFilterWarpper;
import org.whale.system.validation.ValidRs;
import org.whale.system.validation.Valid;

/**
 * @Valition 注释解释拦截器
 *
 * @author 王金绍
 * 2015年3月9日 下午7:34:24
 */
@Component
public class ValidateCheckFilter<T extends Serializable,PK extends Serializable> extends BaseDaoDllFilterWarpper<T, PK> {

	@Autowired
	private Valid validationService;
	
	
	private void validate(T obj){
		ValidRs validateErrors = this.validationService.validate(obj);
		if(validateErrors.hasError()){
			throw new BusinessException(LangUtil.joinList(validateErrors.getErrorsList(), "</br>"));
		}
	}
	
	private void validate(List<T> objs){
		for(Object obj : objs){
			ValidRs validateErrors = this.validationService.validate(obj);
			if(validateErrors.hasError()){
				throw new BusinessException(LangUtil.joinList(validateErrors.getErrorsList(), "</br>"));
			}
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
