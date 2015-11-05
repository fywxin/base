package org.whale.system.common.util;

import java.lang.reflect.Array;
import java.util.Collection;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * 字符串工具类
 *
 * @author 王金绍
 * 2014年9月6日-下午1:33:02
 */
public class Strings {
	
	public static boolean isEmpty(String str)
	  {
	    return (str == null) || (str.length() == 0);
	  }

	  public static boolean isNotEmpty(String str)
	  {
	    return !isEmpty(str);
	  }

	
	public static boolean hasChinese(String str) {
		if(Strings.isBlank(str))
			return false;
		for(int i=0;i<str.length();i++){
			String regEx = "[\u4e00-\u9fa5]";
			Pattern p = Pattern.compile(regEx);
			Matcher m = p.matcher(str.charAt(i) + "");
			if (m.find())
				return true;
		}
		return false;
	}
	
	public static boolean equals(String str1, String str2)
	  {
	    return str1 == null ? false : str2 == null ? true : str1.equals(str2);
	  }

	public static boolean isBlank(CharSequence cs) {
        int strLen;
        if (cs == null || (strLen = cs.length()) == 0) {
            return true;
        }
        for (int i = 0; i < strLen; i++) {
            if (Character.isWhitespace(cs.charAt(i)) == false) {
                return false;
            }
        }
        return true;
    }
	
	public static boolean isNotBlank(CharSequence cs) {
        return !Strings.isBlank(cs);
    }
	
	public static boolean isBlank(Object obj) {
        if(obj == null)
        	return true;
        return isBlank(obj.toString());
    }
	
	public static boolean isNotBlank(Object obj) {
        return !isBlank(obj);
    }
	
	public static boolean isNumeric(String str) {
        if (str == null) {
            return false;
        }
        int sz = str.length();
        for (int i = 0; i < sz; i++) {
            if (Character.isDigit(str.charAt(i)) == false) {
                return false;
            }
        }
        return true;
    }
	
	/**
	 * 首字母小写
	 * @param str
	 * @return
	 */
	public static String capitalize(String str) {
        int strLen;
        if (str == null || (strLen = str.length()) == 0) {
            return str;
        }
        return new StringBuilder(strLen)
            .append(Character.toLowerCase(str.charAt(0)))
            .append(str.substring(1))
            .toString();
    }
	
	/**
     * 去掉字符串前后空白
     * 
     * @param cs
     *            字符串
     * @return 新字符串
     */
    public static String trim(CharSequence cs) {
        if (null == cs)
            return null;
        if (cs instanceof String)
            return ((String) cs).trim();
        int length = cs.length();
        if (length == 0)
            return cs.toString();
        int l = 0;
        int last = length - 1;
        int r = last;
        for (; l < length; l++) {
            if (!Character.isWhitespace(cs.charAt(l)))
                break;
        }
        for (; r > l; r--) {
            if (!Character.isWhitespace(cs.charAt(r)))
                break;
        }
        if (l > r)
            return "";
        else if (l == 0 && r == last)
            return cs.toString();
        return cs.subSequence(l, r + 1).toString();
    }
    
    /**
     * 将一个字符串出现的HMTL元素进行转义，比如
     * 
     * <pre>
     *  escapeHtml("&lt;script&gt;alert("hello world");&lt;/script&gt;") => "&amp;lt;script&amp;gt;alert(&amp;quot;hello world&amp;quot;);&amp;lt;/script&amp;gt;"
     * </pre>
     * 
     * 转义字符对应如下
     * <ul>
     * <li>& => &amp;amp;
     * <li>< => &amp;lt;
     * <li>>=> &amp;gt;
     * <li>' => &amp;#x27;
     * <li>" => &amp;quot;
     * </ul>
     * 
     * @param cs
     *            字符串
     * 
     * @return 转换后字符串
     */
    public static String escapeHtml(CharSequence cs) {
        if (null == cs)
            return null;
        char[] cas = cs.toString().toCharArray();
        StringBuilder sb = new StringBuilder();
        for (char c : cas) {
            switch (c) {
            case '&':
                sb.append("&amp;");
                break;
            case '<':
                sb.append("&lt;");
                break;
            case '>':
                sb.append("&gt;");
                break;
            case '\'':
                sb.append("&#x27;");
                break;
            case '"':
                sb.append("&quot;");
                break;
            default:
                sb.append(c);
            }
        }
        return sb.toString();
    }
    
    /**
     * 将字符串，变成数字对象，现支持的格式为：
     * <ul>
     * <li>null - 整数 0</li>
     * <li>23.78 - 浮点 Float</li>
     * <li>0x45 - 16进制整数 Integer</li>
     * <li>78L - 长整数 Long</li>
     * <li>69 - 普通整数 Integer</li>
     * </ul>
     * 
     * @param s
     *            参数
     * @return 数字对象
     */
    public static Number str2number(String s) {
        // null 值
        if (null == s) {
            return 0;
        }
        s = s.toUpperCase();
        // 浮点
        if (s.indexOf('.') != -1) {
            char c = s.charAt(s.length() - 1);
            if (c == 'F' || c == 'f') {
                return Float.valueOf(s);
            }
            return Double.valueOf(s);
        }
        // 16进制整数
        if (s.startsWith("0X")) {
            return Integer.valueOf(s.substring(2), 16);
        }
        // 长整数
        if (s.charAt(s.length() - 1) == 'L' || s.charAt(s.length() - 1) == 'l') {
            return Long.valueOf(s.substring(0, s.length() - 1));
        }
        // 普通整数
        Long re = Long.parseLong(s);
        if (Integer.MAX_VALUE >= re && re >= Integer.MIN_VALUE)
            return re.intValue();
        return re;
    }
    
    /**
	 * 将数据库大写_ 转为驼峰原则
	 * @param str
	 * @return
	 */
	public static String sql2Camel(String str){
		if(Strings.isBlank(str)) return null;
		str = str.trim();
		char[] chars = str.toCharArray();
		StringBuilder strb = new StringBuilder();
		strb.append(Character.toLowerCase(chars[0]));
		
		for(int i=1; i<chars.length; i++){
			if(chars[i] == ' ') continue;
			if(chars[i] == '_'){
				i++;
				strb.append(Character.toUpperCase(chars[i]));
			}else{
				strb.append(Character.toLowerCase(chars[i]));
			}
		}
		return strb.toString();
	}
}
