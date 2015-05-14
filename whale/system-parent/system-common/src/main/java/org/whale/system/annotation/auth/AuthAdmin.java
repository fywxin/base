package org.whale.system.annotation.auth;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * 是否管理员权限
 *
 * @author 王金绍
 * @date 2015年1月5日 下午5:17:06
 */
@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.METHOD })
@Documented
public @interface AuthAdmin {

}
