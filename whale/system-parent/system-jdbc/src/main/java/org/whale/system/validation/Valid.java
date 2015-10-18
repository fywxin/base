package org.whale.system.validation;

import java.lang.reflect.Field;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.whale.system.annotation.jdbc.Validate;
import org.whale.system.jdbc.orm.OrmContext;
import org.whale.system.jdbc.orm.entry.OrmColumn;
import org.whale.system.spring.SpringContextHolder;

@Component
public class Valid {
	
	@Autowired
	private OrmContext ormContext;
	
	public static ValidRs check(Object target){
		return SpringContextHolder.getBean(Valid.class).validate(target);
	}

	/**
	 * 通过注解对一个Pojo进行验证
	 */
	public ValidRs validate(Object target) {
		ValidRs validRs = new ValidRs();
		validate(target, validRs);
		return validRs;
	}

	/**
	 * 通过注解对一个Pojo进行验证
	 */
	public void validate(Object obj, ValidRs validRs) {
		if (null == obj) {
			return ;
		}
		if(ormContext.contain(obj.getClass())){
			List<OrmColumn> cols = ormContext.getOrmTable(obj.getClass()).getValidateCols();
			if(cols == null || cols.size() < 1)
				return ;
			for(OrmColumn col : cols){
				ValidationUtils.valid(obj, col.getOrmValidate(), validRs);
			}
		}else{
			Field[] fields = obj.getClass().getDeclaredFields();
			for(Field field : fields){
				Validate validCol = field.getAnnotation(Validate.class);
				if(validCol == null)
					continue;
				ValidationUtils.valid(obj, field, validRs);
			}
		}
		
		
	}

}
