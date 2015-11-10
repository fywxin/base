package org.whale.system.common.util;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.springframework.expression.EvaluationContext;
import org.springframework.expression.ExpressionParser;
import org.springframework.expression.spel.standard.SpelExpressionParser;
import org.springframework.expression.spel.support.StandardEvaluationContext;
import org.whale.system.annotation.jdbc.Validate;

public class ValidUtil {

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
	
	/**
	 * 校验
	 * @param obj  对象
	 * @return <属性，错误信息>
	 */
	public static Map<String, String> valid(Object obj){
		return valid(obj, false);
	}
	
	/**
	 * 校验
	 * 
	 * @param obj 对象
	 * @param append 是否累积属性的错误信息
	 * @return
	 */
	public static Map<String, String> valid(Object obj, boolean append){
		Map<String, String> map = new HashMap<String, String>();
		Class<?> clazz = obj.getClass();
		while(!(clazz.getSuperclass() instanceof Object)){
			vaild(obj, append, clazz, map);
			clazz = clazz.getSuperclass();
		}
		return map;
	}
	
	private static void vaild(Object obj, boolean append, Class<?> clazz, Map<String, String> map) {
		Field[] fields = clazz.getDeclaredFields();
		String msg = null;
		for(Field field : fields){
			msg = valid(obj, field, append);
			if(msg != null){
				map.put(field.getName(), msg);
			}
		}
	}
	
	public static String valid(Object obj, Field field){
		return valid(obj, field, false);
	}
	
	public static String valid(Object obj, Field field, boolean append){
		Validate vals = field.getAnnotation(Validate.class);
		if(vals == null){
			return null;
		}
		Object value = ReflectionUtil.readField(field, obj, true);
		
		String defErrMsg = vals.errorMsg();
		boolean useDefErrMsg = Strings.isNotBlank(defErrMsg);
		
		
		List<String> errors = null;
		if(append && !useDefErrMsg){
			errors = new ArrayList<String>();
		}
		String msg = null;
		
		if (vals.required()) {
			msg = required(value);
			if(useDefErrMsg){
				return defErrMsg;
			}else{
				if(append){
					errors.add(msg);
				}else{
					return msg;
				}
			}
		}
		if (vals.account()) {
			msg = account(value);
			if(useDefErrMsg){
				return defErrMsg;
			}else{
				if(append){
					errors.add(msg);
				}else{
					return msg;
				}
			}
		}
		if (vals.mobile()) {
			msg = mobile(value);
			if(useDefErrMsg){
				return defErrMsg;
			}else{
				if(append){
					errors.add(msg);
				}else{
					return msg;
				}
			}
		}
		if (vals.email()) {
			msg = email(value);
			if(useDefErrMsg){
				return defErrMsg;
			}else{
				if(append){
					errors.add(msg);
				}else{
					return msg;
				}
			}
		}
		if (vals.qq()) {
			msg = qq(value);
			if(useDefErrMsg){
				return defErrMsg;
			}else{
				if(append){
					errors.add(msg);
				}else{
					return msg;
				}
			}
		}
		if (vals.chinese()) {
			msg = chinese(value);
			if(useDefErrMsg){
				return defErrMsg;
			}else{
				if(append){
					errors.add(msg);
				}else{
					return msg;
				}
			}
		}
		if (vals.post()) {
			msg = post(value);
			if(useDefErrMsg){
				return defErrMsg;
			}else{
				if(append){
					errors.add(msg);
				}else{
					return msg;
				}
			}
		}
		if (!Strings.isBlank(vals.regex())) {
			msg = regex(value, vals.regex(), "值错误");
			if(useDefErrMsg){
				return defErrMsg;
			}else{
				if(append){
					errors.add(msg);
				}else{
					return msg;
				}
			}
		}
		if (vals.strLen().length > 0) {
			msg = stringLength(value, vals.strLen());
			if(useDefErrMsg){
				return defErrMsg;
			}else{
				if(append){
					errors.add(msg);
				}else{
					return msg;
				}
			}
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
			
			Object repeatValue = ReflectionUtil.readField(repeatField, obj);
			
			msg = repeat(value, repeatValue);
			if(useDefErrMsg){
				return defErrMsg;
			}else{
				if(append){
					errors.add(msg);
				}else{
					return msg;
				}
			}
		}
		if (vals.limit().length > 0) {
			msg = limit(value, vals.limit());
			if(useDefErrMsg){
				return defErrMsg;
			}else{
				if(append){
					errors.add(msg);
				}else{
					return msg;
				}
			}
		}
		if (Strings.isNotBlank(vals.el())) {
			msg = el(field.getName(), value, vals.el());
			if(useDefErrMsg){
				return defErrMsg;
			}else{
				if(append){
					errors.add(msg);
				}else{
					return msg;
				}
			}
		}
		if (Strings.isNotBlank(vals.custom())) {
			msg = custom(obj, vals.custom());
			if(useDefErrMsg){
				return defErrMsg;
			}else{
				if(append){
					errors.add(msg);
				}else{
					return msg;
				}
			}
		}
		if(errors != null && errors.size() > 0){
			return ListUtil.join(errors, ",");
		}
		
		return null;
	}
	
	
	
	/**
	 * 
	 * 枚举校验
	 * 
	 * @param value
	 * @param errorMsg
	 * @param ValidRs
	 * @param enums
	 * @return
	 * @Date 2015年3月9日 下午5:23:17
	 */
	public static String enmus(Object value, String[] enums){
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
			return "非法枚举值";
		}
		return null;
	}
	
	/**
	 * URL 校验
	 * @param value
	 * @param errorMsg
	 * @param validRs
	 * @return
	 * @Date 2015年3月9日 下午5:36:49
	 */
	public static String url(Object value){
		if (null != value) {
			String valueStr = value.toString();
			if (valueStr.startsWith("https://"))
				valueStr = "http://" + valueStr.substring(8);
			try {
				new URL(valueStr);
			} catch (MalformedURLException e) {
				return "非法URL";
			}
		}
		return null;
	}
	
	/**
	 * ipV4校验，缺失IpV6
	 * 
	 * @param value
	 * @param errorMsg
	 * @param validRs
	 * @return
	 * @Date 2015年3月9日 下午5:00:51
	 */
	public static String ip(Object value){
		return regex(value, ipv4_regex, "非法ip");
	}

	/**
	 * 必填字段验证
	 * 
	 * @param obj
	 *            待验证对象
	 * @param errorMsg
	 *            验证错误后的提示语
	 * @param validRs
	 *            存储错误信息的对象
	 * @return 返回是否通过验证
	 */
	public static String required(Object value) {
		if ((null == value) || (value instanceof String && Strings.isBlank((String) value))) {
			return "值不能为空";
		}
		return null;
	}

	/**
	 * 正则表达式验证
	 * 
	 * @param obj
	 * @param regex
	 * @param errorMsg
	 * @param validRs
	 * @return
	 */
	public static String regex(Object value, String regex, String errorMsg) {
		if (null == value || !(value instanceof String)) {
			return null;
		}

		final Matcher m = Pattern.compile(regex, Pattern.MULTILINE + Pattern.DOTALL).matcher((String) value);
		if (!m.matches()) {
			return errorMsg;
		}
		return null;
	}

	/**
	 * 手机号验证
	 * 
	 * @param obj
	 * @param errorMsg
	 * @param validRs
	 * @return
	 */
	public static String mobile(Object value) {
		return regex(value, mobile_regex, "手机号码错误");
	}

	/**
	 * 国内邮政编码验证
	 * 
	 * @param value
	 * @param errorMsg
	 * @param validRs
	 * @return
	 */
	public static String post(Object value) {
		return regex(value, post_regex, "邮政编码错误");
	}

	/**
	 * Email 验证
	 * 
	 * @param obj
	 * @param errorMsg
	 * @param validRs
	 * @return
	 */
	public static String email(Object value) {
		return regex(value, email_regex, "邮箱错误");
	}

	/**
	 * 只允许中文验证
	 * 
	 * @param fieldName
	 * @param obj
	 * @param errorMsg
	 * @param validRs
	 * @return
	 */
	public static String chinese(Object value) {
		return regex(value, chinese_regex, "非中文");
	}

	/**
	 * QQ号 验证
	 * 
	 * @param obj
	 * @param errorMsg
	 * @param validRs
	 * @return
	 */
	public static String qq(Object value) {
		return regex(value, qq_regex, "qq号错误");
	}

	/**
	 * 账号验证
	 * 
	 * @param value
	 * @param errorMsg
	 * @param validRs
	 * @return
	 */
	public static String account(Object value) {
		return regex(value, account_regex, "非法账号");
	}

	/**
	 * 重复性验证。两个字段的值必须一致，允许空值。
	 * 
	 * @param value
	 * @param repeatValue
	 * @param errorMsg
	 * @param validRs
	 * @return
	 */
	public static String repeat(Object value, Object repeatValue) {
		if (!(null == value) && !value.equals(repeatValue)) {
			return "值不一致";
		}
		if (!(null == repeatValue) && !repeatValue.equals(value)) {
			return "值不一致";
		}
		return null;
	}

	/**
	 * 字符串长度必须在一定区间范围内验证
	 * 
	 * @param obj
	 * @param interval
	 *            长度限定区间，如果传空的数组也不会报错，但验证将总是会通过
	 * @param errorMsg
	 * @param validRs
	 * @return
	 */
	public static String stringLength(Object value, int[] interval) {
		int minLength = 0;
		int maxLength = Integer.MAX_VALUE;
		
		if (interval.length >= 1) {
			minLength = interval[0];
		}
		if (interval.length >= 2) {
			maxLength = interval[1];
		}
		return stringLength(value, minLength, maxLength);
	}

	/**
	 * 字符串长度必须在一定区间范围内验证
	 * 
	 * @param obj
	 * @param minLength
	 * @param maxLength
	 * @param errorMsg
	 * @param validRs
	 * @return
	 */
	public static String stringLength(Object value, int minLength, int maxLength) {
		if (null == value || !(value instanceof String)) return null;
		String str = (String) value;
		if (str.length() < minLength){
			return "小于最小长度["+minLength+"]";
		}
		if(str.length() > maxLength) {
			return "超过最大长度["+maxLength+"]";
		}
		return null;
	}

	/**
	 * 判断指定值是否在某个区间
	 * 
	 * @param value
	 * @param interval
	 * @param errorMsg
	 * @param validRs
	 * @return
	 */
	public static String limit(Object value, double[] interval) {
		double minLength = 0;
		double maxLength = Double.MAX_VALUE;
		if (interval.length >= 1) {
			minLength = interval[0];
		}
		if (interval.length >= 2) {
			maxLength = interval[1];
		}
		return limit(value, minLength, maxLength);
	}

	/**
	 * 判断指定值是否在某个区间,兼容 int、long、float、double
	 * 
	 * @param value
	 * @param interval
	 * @param errorMsg
	 * @param validRs
	 * @return
	 */
	public static String limit(Object value, double minValue, double maxValue) {
		if (null == value) return null;
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
		if (d < minValue){
			return "小于最小值["+d+"]";
		}
		if(d > maxValue) {
			return "大于最大值["+d+"]";
		}
		return null;
	}

	/**
	 * 通过 spring 自带的 el 表达式进行验证
	 * @param fieldName
	 * @param obj
	 * @param el 表达式
	 * @param errorMsg
	 * @param validRs
	 * @return
	 */
	public static String el(String fieldName, Object obj, String el) {
		ExpressionParser parser = new SpelExpressionParser();
		EvaluationContext context = new StandardEvaluationContext();
		context.setVariable("field", fieldName);
		context.setVariable("value", obj);
		
		if(!parser.parseExpression(el).getValue(context, Boolean.class)){
			return "表达式校验不通过";
		}
		return null;
	}

	/**
	 * 自定义验证方法
	 * 
	 * @param obj
	 * @param customFunction
	 *            自定义验证方法名称，注意该方法必须在 obj 里用 public 声明，且返回值为 boolean 型，否则会抛出异常
	 * @param errorMsg
	 * @param validRs
	 * @return
	 */
	public static String custom(Object obj, String customFunction) {
		Method[] mds = obj.getClass().getDeclaredMethods();
		boolean find = false;
		for (Method md : mds) {
			if (md.getName().equals(customFunction)) {
				find = true;
				try {
					boolean ret = (Boolean) md.invoke(obj);
					if (!ret) {
						return "校验不通过";
					}
				}catch (Exception e) {
					e.printStackTrace();
					return "执行自定义校验方法"+customFunction+"()出错";
				}
			}
		}
		// 没有找到指定的方法
		if (!find) {
			return "没有找到自定义校验方法"+customFunction+"()";
		}
		return null;
	}

	/**
	 * 检查方法的参数中是否存在 validRs 的对象，没有则返回空
	 * 
	 * @param argsClass
	 * @param args
	 * @return
	 
	public static ValidRs checkArgs(Class<?>[] argsClass, Object... args) {
		ValidRs es = null;
		for (int i = 0; i < argsClass.length; i++) {
			if (argsClass[i] == ValidRs.class) {
				if (args[i] == null) {
					args[i] = es = new ValidRs();
				} else {
					es = (ValidRs) args[i];
				}
				break;
			}
		}

		return es;
	}*/
}
