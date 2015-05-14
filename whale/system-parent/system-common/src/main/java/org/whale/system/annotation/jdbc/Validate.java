package org.whale.system.annotation.jdbc;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * 错误校验元注释
 * 
 * @author 王金绍
 *
 */
@Retention(RetentionPolicy.RUNTIME)
@Target({ ElementType.PARAMETER, ElementType.FIELD })
public @interface Validate {

	/**
	 * 必填字段验证规则
	 */
	public boolean required() default false;
	
	/**
	 * 正则表达式验证规则
	 */
	public String regex() default "";
	
	/**
	 * 错误提示语
	 */
	public String errorMsg() default "";
	
	/**
	 * 手机号验证规则
	 */
	public boolean mobile() default false;
	
	/**
	 * 帐号验证规则(字母开头，允许字母数字下划线)，常与字串长度验证规则混合使用
	 */
	public boolean account() default false;

	/**
	 * Email 验证规则
	 */
	public boolean email() default false;

	/**
	 * QQ 号验证规则
	 */
	public boolean qq() default false;

	/**
	 * 字串必须为中文验证规则
	 */
	public boolean chinese() default false;
	
	/**
	 * 邮政编码验证规则
	 */
	public boolean post() default false;
	
	/**
	 * 重复性验证规则。请放置待比较的字段名
	 */
	public String repeat() default "";

	/**
	 * 字符串最大、最小长度验证规则
	 */
	public int[] strLen() default {};

	/**
	 * 数值型数据取值范围区间验证规则，兼容 int、long、float、double
	 */
	public double[] limit() default {};
	
	/**
	 * 通过spring自带的el表达式进行验证。注意该表达式返回值应该为布尔型<br>
	 * 例如：value*10<100 
	 */
	public String el() default "";
	
	/**
	 * 自定义效验规则,可以自行指定验证的方法名称 <br/> 该方法必须是public的，且没有参数返回值为boolean型
	 */
	public String custom() default "";
	
	/**
	 * 是否为ip地址
	 * @return
	 */
	public boolean ip() default false;
	
	/**
	 * 是否url地址
	 */
	public boolean url() default false;
	
	/**
	 * 枚举值范围， 非该枚举范围内的值，则抛出异常
	 * 兼容 int、long、float、double
	 */
	public String[] enums() default {};
}
