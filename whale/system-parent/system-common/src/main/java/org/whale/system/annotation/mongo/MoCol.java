package org.whale.system.annotation.mongo;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * MongoDB 的字段映射关系
 * 
 * @author zozoh(zozohtnt@gmail.com)
 */
@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.FIELD})
@Documented
public @interface MoCol {

    /**
     * 空字符串，表示采用 Java 字段原名
     */
    String value() default "";

    /**
     * 特殊声明一下当前字段的实现类，默认为 Object.class 表示 ZMo 自行决定
     */
    Class<?> type() default Object.class;

}
