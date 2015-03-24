package org.whale.system.addon.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.whale.system.addon.EmptyBaseDaoAddon;
import org.whale.system.base.BaseDao;
import org.whale.system.common.exception.BusinessException;
import org.whale.system.common.util.LangUtil;
import org.whale.system.validation.ValidateErrors;
import org.whale.system.validation.ValidationService;

/**
 * @Valition 注释解释拦截器
 *
 * @author 王金绍
 * @Date 2015年3月9日 下午7:34:24
 */
@SuppressWarnings("all")
@Component
public class ValidateCheckAddon extends EmptyBaseDaoAddon {
	
	@Autowired
	private ValidationService validationService;

	@Override
	public void beforeSave(Object obj, BaseDao baseDao) {
		ValidateErrors validateErrors = this.validationService.validate(obj);
		if(validateErrors.hasError()){
			throw new BusinessException(LangUtil.joinList(validateErrors.getErrorsList(), "</br>"));
		}
	}

	@Override
	public void beforeUpdate(Object obj, BaseDao baseDao) {
		ValidateErrors validateErrors = this.validationService.validate(obj);
		if(validateErrors.hasError()){
			throw new BusinessException(LangUtil.joinList(validateErrors.getErrorsList(), "</br>"));
		}
	}

	@Override
	public void beforeSaveBatch(List<Object> objs, BaseDao baseDao) {
		if(objs == null){
			throw new BusinessException("批量保存值不能为空");
		}
		for(Object obj : objs){
			ValidateErrors validateErrors = this.validationService.validate(obj);
			if(validateErrors.hasError()){
				throw new BusinessException(LangUtil.joinList(validateErrors.getErrorsList(), "</br>"));
			}
		}
	}

	@Override
	public void beforeUpdateBatch(List<Object> objs, BaseDao baseDao) {
		if(objs == null){
			throw new BusinessException("批量更新值不能为空");
		}
		for(Object obj : objs){
			ValidateErrors validateErrors = this.validationService.validate(obj);
			if(validateErrors.hasError()){
				throw new BusinessException(LangUtil.joinList(validateErrors.getErrorsList(), "</br>"));
			}
		}
	}

	@Override
	public int getOrder() {
		
		return 100;
	}

}
