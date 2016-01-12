package org.whale.system.spring;

import java.lang.annotation.Annotation;
import java.lang.reflect.Method;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.reflect.MethodSignature;
import org.whale.system.annotation.jdbc.Validate;
import org.whale.system.common.exception.FieldValidErrorException;
import org.whale.system.common.util.ReflectionUtil;
import org.whale.system.common.util.ValidUtil;

/**
 * 参数合法性校验
 * TODO valid 是否会被spring自动执行
 * 
 * http://chaoren3166gg.iteye.com/blog/2001734
 * @author 王金绍
 * @date 2015年11月10日 下午6:33:53
 */
public class VaildRouterParamAop {

	public void doBefore(JoinPoint joinPoint) {
		Object[] args = joinPoint.getArgs();
		if(args == null || args.length < 1){
			return ;
		}
		MethodSignature signature = (MethodSignature)joinPoint.getSignature();
		Method method = signature.getMethod();
		Annotation[][] parameterAnnotations = method.getParameterAnnotations();
		Class<?>[] paramTypes=method.getParameterTypes();
		Object obj = null;
		Annotation[] anns = null;
		
		
		for(int i=0; i<args.length; i++){
			obj = args[i];
			anns = parameterAnnotations[i];
			if(anns == null || anns.length < 1){
				continue;
			}
			if((obj instanceof HttpServletRequest) || (obj instanceof HttpServletResponse)){
				continue;
			}
			for(Annotation ann : anns){
				if(ann.annotationType() == Validate.class){
					if(ann.annotationType() == Validate.class){
						Validate vals = (Validate)ann;
						if(ReflectionUtil.isBaseDataType(paramTypes[i])){
							String msg = ValidUtil.valid(obj, vals);
							if(msg != null){
								throw new FieldValidErrorException(msg);
							}
						}else{
							if(obj == null){
								throw new FieldValidErrorException("对象为空");
							}
							Map<String, String> error = ValidUtil.valid(obj);
							if(error != null && error.size() > 0){
								throw new FieldValidErrorException(ValidUtil.formatMsg(error, "</br>"), error);
							}
						}
					}
				}
			}
		}
	}
}
