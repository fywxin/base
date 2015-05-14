package org.whale.system.validation;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.springframework.expression.EvaluationContext;
import org.springframework.expression.ExpressionParser;
import org.springframework.expression.spel.standard.SpelExpressionParser;
import org.springframework.expression.spel.support.StandardEvaluationContext;
import org.whale.system.annotation.jdbc.Validate;
import org.whale.system.common.util.Strings;
import org.whale.system.jdbc.orm.entry.OrmValidate;
import org.whale.system.jdbc.util.AnnotationUtil;

/**
 * 校验工具
 *
 * @author 王金绍
 * @Date 2015年3月9日 下午5:37:07
 */
public class ValidationUtils {

	private static final String mobile_regex = "1\\d{10}";
	private static final String email_regex = "^([\\w-\\.]+)@((\\[[0-9]{1,3}\\.[0-9]{1,3}\\.[0-9]{1,3}\\.)|(([\\w-]+\\.)+))([a-zA-Z]{2,4}|[0-9]{1,3})(\\]?)$";
	private static final String chinese_regex = "[\u4e00-\u9fa5]+";
	private static final String qq_regex = "[1-9][0-9]{4,}";
	private static final String post_regex = "[1-9]\\d{5}(?!\\d)";
	private static final String account_regex = "^[a-zA-Z][a-zA-Z0-9_]+";
	private static final String ipv4_regex = "^(1\\d{2}|2[0-4]\\d|25[0-5]|[1-9]\\d|[1-9])\\."  
								            +"(00?\\d|1\\d{2}|2[0-4]\\d|25[0-5]|[1-9]\\d|\\d)\\."  
								            +"(00?\\d|1\\d{2}|2[0-4]\\d|25[0-5]|[1-9]\\d|\\d)\\."  
								            +"(00?\\d|1\\d{2}|2[0-4]\\d|25[0-5]|[1-9]\\d|\\d)$"; 
	
	
	public static boolean valid(Object obj, Field field, ValidateErrors validateErrors){
		// 检查该字段是否声明了需要验证
		Validate vals = field.getAnnotation(Validate.class);
		
		String fieldName = field.getName();
		String defErrMsg = vals.errorMsg();
		Object value = AnnotationUtil.getFieldValue(obj, field);

		boolean pass = true;
		if (vals.required()) {
			pass =required(fieldName, value, validateErrors) ? pass : false;
		}
		if (vals.account()) {
			pass = account(fieldName, value, validateErrors) ? pass : false;
		}
		if (vals.mobile()) {
			pass = mobile(fieldName, value, validateErrors) ? pass : false;
		}
		if (vals.email()) {
			pass = email(fieldName, value, validateErrors) ? pass : false;
		}
		if (vals.qq()) {
			pass = qq(fieldName, value, validateErrors) ? pass : false;
		}
		if (vals.chinese()) {
			pass = chinese(fieldName, value, validateErrors) ? pass : false;
		}
		if (vals.post()) {
			pass = post(fieldName, value, validateErrors) ? pass : false;
		}
		if (!Strings.isBlank(vals.regex())) {
			pass = regex(fieldName, value, vals.regex(), "值错误", validateErrors) ? pass : false;
		}
		if (vals.strLen().length > 0) {
			pass = stringLength(fieldName, value, vals.strLen(), validateErrors) ? pass : false;
		}
		if (Strings.isNotBlank(vals.repeat())) {
			Field repeatField = null;
			Field[] fields = obj.getClass().getDeclaredFields();
			for (Field f : fields) {
				if(f.getName().endsWith(vals.repeat())){
					repeatField = f;
					break;
				}
			}
			
			Object repeatValue = AnnotationUtil.getFieldValue(obj, repeatField);
			
			pass = repeat(fieldName, value, repeatValue, validateErrors) ? pass : false;
		}
		if (vals.limit().length > 0) {
			pass = limit(fieldName, value, vals.limit(), validateErrors) ? pass : false;
		}
		if (Strings.isNotBlank(vals.el())) {
			pass = el(fieldName, value, vals.el(), validateErrors) ? pass : false;
		}
		if (Strings.isNotBlank(vals.custom())) {
			pass = custom(fieldName, obj, vals.custom(), validateErrors) ? pass : false;
		}
		if(!pass){
			validateErrors.addDefErrMsg(fieldName, defErrMsg);
		}
		
		return pass;
	}
	
	
	public static boolean valid(Object obj, OrmValidate ormValidate, ValidateErrors validateErrors){
		String fieldName = ormValidate.getOrmColumn().getCnName();
		String defErrMsg = ormValidate.getErrorMsg();
		Object value = AnnotationUtil.getFieldValue(obj, ormValidate.getOrmColumn().getField());
		
		boolean pass = true;
		if(ormValidate.isAccount())
			pass = account(fieldName, value, validateErrors) ? pass : false;
		if(ormValidate.isChinese())
			pass = chinese(fieldName, value, validateErrors) ? pass : false;
		if(ormValidate.isEmail())
			pass = email(fieldName, value, validateErrors) ? pass : false;
		if(ormValidate.isIp())
			pass = ip(fieldName, value, validateErrors) ? pass : false;
		if(ormValidate.isMobile())
			pass = mobile(fieldName, value, validateErrors) ? pass : false;
		if(ormValidate.isPost())
			pass = post(fieldName, value, validateErrors) ? pass : false;
		if(ormValidate.isQq())
			pass = qq(fieldName, value, validateErrors) ? pass : false;
		if(ormValidate.isRequired())
			pass = required(fieldName, value, validateErrors) ? pass : false;
		if(ormValidate.isUrl())
			pass = url(fieldName, value, validateErrors) ? pass : false;
		if(Strings.isNotBlank(ormValidate.getCustom()))
			pass = custom(fieldName, obj, ormValidate.getCustom(), validateErrors) ? pass : false;
		if(Strings.isNotBlank(ormValidate.getEl()))
			pass = el(fieldName, obj, ormValidate.getEl(), validateErrors) ? pass : false;
		if(ormValidate.getEnums() != null && ormValidate.getEnums().length > 0)
			pass = enmus(fieldName, value, validateErrors, ormValidate.getEnums()) ? pass : false;
		if(ormValidate.getLimit() != null && ormValidate.getLimit().length > 0)
			pass = limit(fieldName, value, ormValidate.getLimit(), validateErrors) ? pass : false;
		if(Strings.isNotBlank(ormValidate.getRegex()))
			pass = regex(fieldName, value, ormValidate.getRegex(), "值错误", validateErrors) ? pass : false;
		if(Strings.isNotBlank(ormValidate.getRepeat())){
			Field repeatField = null;
			Field[] fields = obj.getClass().getDeclaredFields();
			for (Field f : fields) {
				if(f.getName().endsWith(ormValidate.getRepeat())){
					repeatField = f;
					break;
				}
			}
			Object repeatValue = AnnotationUtil.getFieldValue(obj, repeatField);
			pass = repeat(fieldName, value, repeatValue, validateErrors) ? pass : false;
		}
		if(ormValidate.getStrLen() != null && ormValidate.getStrLen().length > 0)
			pass = stringLength(fieldName, value, ormValidate.getStrLen(), validateErrors) ? pass : false;
		
		if(!pass){
			validateErrors.addDefErrMsg(fieldName, defErrMsg);
		}
		
		return pass;
	}
	
	/**
	 * 
	 * 枚举校验
	 * 
	 * @param fieldName
	 * @param value
	 * @param errorMsg
	 * @param ValidateErrors
	 * @param enums
	 * @return
	 * @Date 2015年3月9日 下午5:23:17
	 */
	public static boolean enmus(String fieldName, Object value, ValidateErrors validateErrors, String[] enums){
		boolean notIn = true;
		if (null == value) {
			for(String str : enums){
				if(str == null){
					notIn = false;
					break;
				}
			}
		}else{
			for(String str : enums){
				if(value.toString().equals(str)){
					notIn = false;
					break;
				}
			}
		}
		
		if(notIn){
			validateErrors.add(fieldName, "非法枚举值");
			return false;
		}
		return true;
	}
	
	/**
	 * URL 校验
	 * @param fieldName
	 * @param value
	 * @param errorMsg
	 * @param ValidateErrors
	 * @return
	 * @Date 2015年3月9日 下午5:36:49
	 */
	public static boolean url(String fieldName, Object value, ValidateErrors ValidateErrors){
		if (null != value) {
			String valueStr = value.toString();
			if (valueStr.startsWith("https://"))
				valueStr = "http://" + valueStr.substring(8);
			try {
				new URL(valueStr);
			} catch (MalformedURLException e) {
				ValidateErrors.add(fieldName, "非法URL");
				return false;
			}
		}
		return true;
	}
	
	/**
	 * ipV4校验，缺失IpV6
	 * 
	 * @param fieldName
	 * @param value
	 * @param errorMsg
	 * @param ValidateErrors
	 * @return
	 * @Date 2015年3月9日 下午5:00:51
	 */
	public static boolean ip(String fieldName, Object value, ValidateErrors ValidateErrors){
		return regex(fieldName, value, ipv4_regex, "非法ip", ValidateErrors);
	}

	/**
	 * 必填字段验证
	 * 
	 * @param fieldName
	 *            待验证字段名
	 * @param obj
	 *            待验证对象
	 * @param errorMsg
	 *            验证错误后的提示语
	 * @param ValidateErrors
	 *            存储错误信息的对象
	 * @return 返回是否通过验证
	 */
	public static boolean required(String fieldName, Object value, ValidateErrors ValidateErrors) {
		if (null == value) {
			ValidateErrors.add(fieldName, "值不能为空");
			return false;
		}
		if (value instanceof String && Strings.isBlank((String) value)) {
			ValidateErrors.add(fieldName, "值不能为空");
			return false;
		}
		return true;
	}

	/**
	 * 正则表达式验证
	 * 
	 * @param fieldName
	 * @param obj
	 * @param regex
	 * @param errorMsg
	 * @param ValidateErrors
	 * @return
	 */
	public static boolean regex(String fieldName, Object value, String regex, String errorMsg, ValidateErrors ValidateErrors) {
		if (null == value || !(value instanceof String)) {
			return true;
		}

		final Matcher m = Pattern.compile(regex, Pattern.MULTILINE + Pattern.DOTALL).matcher((String) value);
		if (!m.matches()) {
			ValidateErrors.add(fieldName, errorMsg);
			return false;
		}
		return true;
	}

	/**
	 * 手机号验证
	 * 
	 * @param fieldName
	 * @param obj
	 * @param errorMsg
	 * @param ValidateErrors
	 * @return
	 */
	public static boolean mobile(String fieldName, Object value, ValidateErrors ValidateErrors) {
		return regex(fieldName, value, mobile_regex, "非法手机号码", ValidateErrors);
	}

	/**
	 * 国内邮政编码验证
	 * 
	 * @param fieldName
	 * @param value
	 * @param errorMsg
	 * @param ValidateErrors
	 * @return
	 */
	public static boolean post(String fieldName, Object value, ValidateErrors ValidateErrors) {
		return regex(fieldName, value, post_regex, "非法邮政编码", ValidateErrors);
	}

	/**
	 * Email 验证
	 * 
	 * @param fieldName
	 * @param obj
	 * @param errorMsg
	 * @param ValidateErrors
	 * @return
	 */
	public static boolean email(String fieldName, Object value, ValidateErrors ValidateErrors) {
		return regex(fieldName, value, email_regex, "非法邮箱", ValidateErrors);
	}

	/**
	 * 只允许中文验证
	 * 
	 * @param fieldName
	 * @param obj
	 * @param errorMsg
	 * @param ValidateErrors
	 * @return
	 */
	public static boolean chinese(String fieldName, Object value, ValidateErrors ValidateErrors) {
		return regex(fieldName, value, chinese_regex, "非中文", ValidateErrors);
	}

	/**
	 * QQ号 验证
	 * 
	 * @param fieldName
	 * @param obj
	 * @param errorMsg
	 * @param ValidateErrors
	 * @return
	 */
	public static boolean qq(String fieldName, Object value, ValidateErrors ValidateErrors) {
		return regex(fieldName, value, qq_regex, "非法qq", ValidateErrors);
	}

	/**
	 * 账号验证
	 * 
	 * @param fieldName
	 * @param value
	 * @param errorMsg
	 * @param ValidateErrors
	 * @return
	 */
	public static boolean account(String fieldName, Object value, ValidateErrors ValidateErrors) {
		return regex(fieldName, value, account_regex, "非法账号", ValidateErrors);
	}

	/**
	 * 重复性验证。两个字段的值必须一致，允许空值。
	 * 
	 * @param fieldName
	 * @param value
	 * @param repeatValue
	 * @param errorMsg
	 * @param ValidateErrors
	 * @return
	 */
	public static boolean repeat(String fieldName,
									Object value,
									Object repeatValue,
									ValidateErrors ValidateErrors) {
		if (!(null == value) && !value.equals(repeatValue)) {
			ValidateErrors.add(fieldName, "值不一致");
			return false;
		}
		if (!(null == repeatValue) && !repeatValue.equals(value)) {
			ValidateErrors.add(fieldName, "值不一致");
			return false;
		}
		return true;
	}

	/**
	 * 字符串长度必须在一定区间范围内验证
	 * 
	 * @param fieldName
	 * @param obj
	 * @param interval
	 *            长度限定区间，如果传空的数组也不会报错，但验证将总是会通过
	 * @param errorMsg
	 * @param ValidateErrors
	 * @return
	 */
	public static boolean stringLength(	String fieldName,
										Object value,
										int[] interval,
										ValidateErrors ValidateErrors) {
		int minLength = 0;
		int maxLength = Integer.MAX_VALUE;
		
		if (interval.length >= 1) {
			minLength = interval[0];
		}
		if (interval.length >= 2) {
			maxLength = interval[1];
		}
		return stringLength(fieldName, value, minLength, maxLength,  ValidateErrors);
	}

	/**
	 * 字符串长度必须在一定区间范围内验证
	 * 
	 * @param fieldName
	 * @param obj
	 * @param minLength
	 * @param maxLength
	 * @param errorMsg
	 * @param ValidateErrors
	 * @return
	 */
	public static boolean stringLength(	String fieldName,
										Object value,
										int minLength,
										int maxLength,
										ValidateErrors ValidateErrors) {
		if (null == value || !(value instanceof String)) return true;
		String errorMsg = null;
		String str = (String) value;
		if (str.length() < minLength){
			errorMsg = "小于最小长度["+minLength+"]";
		}
		if(str.length() > maxLength) {
			errorMsg = "超过最大长度["+maxLength+"]";
		}
		if(errorMsg != null){
			ValidateErrors.add(fieldName, errorMsg);
			return false;
		}
		return true;
	}

	/**
	 * 判断指定值是否在某个区间
	 * 
	 * @param fieldName
	 * @param value
	 * @param interval
	 * @param errorMsg
	 * @param ValidateErrors
	 * @return
	 */
	public static boolean limit(String fieldName, Object value,	double[] interval,	ValidateErrors ValidateErrors) {
		double minLength = 0;
		double maxLength = Double.MAX_VALUE;
		if (interval.length >= 1) {
			minLength = interval[0];
		}
		if (interval.length >= 2) {
			maxLength = interval[1];
		}
		return limit(fieldName, value, minLength, maxLength, ValidateErrors);
	}

	/**
	 * 判断指定值是否在某个区间,兼容 int、long、float、double
	 * 
	 * @param fieldName
	 * @param value
	 * @param interval
	 * @param errorMsg
	 * @param ValidateErrors
	 * @return
	 */
	public static boolean limit(String fieldName, Object value,	double minValue, double maxValue, ValidateErrors ValidateErrors) {
		if (null == value) return true;
		Double d = null;
		if (value instanceof Double) {
			d = (Double) value;
		} else if (value instanceof Integer) {
			d = ((Integer) value).doubleValue();
		} else if (value instanceof Long) {
			d = ((Long) value).doubleValue();
		} else if (value instanceof Float) {
			d = ((Float) value).doubleValue();
		}
		String errorMsg = null;
		if (d < minValue){
			errorMsg ="小于最小值["+d+"]";
		}
		if(d > maxValue) {
			errorMsg ="大于最大值["+d+"]";
		}
		if(errorMsg != null){
			ValidateErrors.add(fieldName, errorMsg);
			return false;
		}
		return true;
	}

	/**
	 * 通过 spring 自带的 el 表达式进行验证
	 * @param fieldName
	 * @param obj
	 * @param el 表达式
	 * @param errorMsg
	 * @param ValidateErrors
	 * @return
	 */
	public static boolean el(String fieldName, Object obj, String el, ValidateErrors ValidateErrors) {
		ExpressionParser parser = new SpelExpressionParser();
		EvaluationContext context = new StandardEvaluationContext();
		context.setVariable("field", fieldName);
		context.setVariable("value", obj);
		
		if(!parser.parseExpression(el).getValue(context, Boolean.class)){
			ValidateErrors.add(fieldName, "表达式校验不通过");
			return false;
		}
		return true;
	}

	/**
	 * 自定义验证方法
	 * 
	 * @param fieldName
	 * @param obj
	 * @param customFunction
	 *            自定义验证方法名称，注意该方法必须在 obj 里用 public 声明，且返回值为 boolean 型，否则会抛出异常
	 * @param errorMsg
	 * @param ValidateErrors
	 * @return
	 */
	public static boolean custom(String fieldName, Object obj, String customFunction, ValidateErrors ValidateErrors) {
		Method[] mds = obj.getClass().getDeclaredMethods();
		boolean find = false;
		for (Method md : mds) {
			if (md.getName().equals(customFunction)) {
				find = true;
				try {
					boolean ret = (Boolean) md.invoke(obj);
					if (!ret) {
						ValidateErrors.add(fieldName, "校验不通过");
						return false;
					}
				}catch (Exception e) {
					ValidateErrors.add(fieldName, "执行自定义校验方法"+customFunction+"()出错");
					e.printStackTrace();
					return false;
				}
			}
		}
		// 没有找到指定的方法
		if (!find) {
			ValidateErrors.add(fieldName, "没有找到自定义校验方法"+customFunction+"()");
			return false;
		}
		return true;
	}

	/**
	 * 检查方法的参数中是否存在 ValidateErrors 的对象，没有则返回空
	 * 
	 * @param argsClass
	 * @param args
	 * @return
	 */
	public static ValidateErrors checkArgs(Class<?>[] argsClass, Object... args) {
		ValidateErrors es = null;
		for (int i = 0; i < argsClass.length; i++) {
			if (argsClass[i] == ValidateErrors.class) {
				if (args[i] == null) {
					args[i] = es = new ValidateErrors();
				} else {
					es = (ValidateErrors) args[i];
				}
				break;
			}
		}

		return es;
	}
}
