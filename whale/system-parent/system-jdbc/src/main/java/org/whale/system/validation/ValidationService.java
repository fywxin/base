package org.whale.system.validation;

import java.lang.reflect.Field;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.whale.system.annotation.jdbc.Validate;
import org.whale.system.common.util.Strings;
import org.whale.system.jdbc.orm.OrmContext;
import org.whale.system.jdbc.orm.entry.OrmColumn;

@Component
public class ValidationService implements Validation {
	
	@Autowired
	private OrmContext ormContext;

	/**
	 * 通过注解对一个Pojo进行验证
	 */
	public ValidateErrors validate(Object target) {
		ValidateErrors ValidateErrors = new ValidateErrors();
		validate(target, ValidateErrors);
		return ValidateErrors;
	}

	/**
	 * 通过注解对一个Pojo进行验证
	 */
	public void validate(Object obj, ValidateErrors validateErrors) {
		if (null == obj) {
			return;
		}
		
		String fieldName = null;
		boolean pass = false;
		if(ormContext.contain(obj.getClass())){
			List<OrmColumn> cols = ormContext.getOrmTable(obj.getClass()).getValidateCols();
			if(cols == null || cols.size() < 1)
				return ;
			for(OrmColumn col : cols){
				fieldName = col.getCnName();
				pass = ValidationUtils.valid(obj, col.getOrmValidate(), validateErrors);
				
				if(!pass){
					if(Strings.isNotBlank(validateErrors.getDefErrMsg(fieldName))){
						validateErrors.putFinal(fieldName, validateErrors.getDefErrMsg(fieldName));
					}else{
						validateErrors.putFinal(fieldName, "字段[<span style='color:black;font-style:normal;'>"+fieldName+"</span>] ： "+validateErrors.get(fieldName));
					}
				}
			}
		}else{
			Field[] fields = obj.getClass().getDeclaredFields();
			for(Field field : fields){
				Validate validCol = field.getAnnotation(Validate.class);
				if(validCol == null)
					continue;
				
				fieldName = field.getName();
				pass = ValidationUtils.valid(obj, field, validateErrors);
				
				if(!pass){
					if(Strings.isNotBlank(validateErrors.getDefErrMsg(fieldName))){
						validateErrors.putFinal(fieldName, validateErrors.getDefErrMsg(fieldName));
					}else{
						validateErrors.putFinal(fieldName, "字段["+fieldName+"]"+validateErrors.get(fieldName));
					}
				}
			}
		}
		
		
	}

}
