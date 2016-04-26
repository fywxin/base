package org.whale.system.annotation.log;

import java.lang.annotation.*;

/**
 * 日志元注释
 * 建议注释在controller层，且方法不能为重构方法，否则可能导致日志错误
 *
 * Created by 王金绍 on 2016/4/25.
 */
@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.METHOD, ElementType.TYPE})
@Documented
public @interface Log {

    /**
     * 模块名，可以定义在类上，则该类所有方法默认使用该模块名
     * @return
     */
    String module() default "";

    /**
     * 描述 占位符{}
     * 值通过线程上下文传递
     *
     * @return
     */
    String value();
}
