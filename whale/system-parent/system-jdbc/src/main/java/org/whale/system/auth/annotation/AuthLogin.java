package org.whale.system.auth.annotation;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * 只要是登录用户，都可以访问
 * 
 * @author 王金绍
 * @date 2015年5月7日 下午2:57:00
 */
@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.METHOD })
@Documented
public @interface AuthLogin {

}
