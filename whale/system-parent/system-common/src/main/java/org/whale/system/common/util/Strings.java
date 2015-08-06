package org.whale.system.common.util;

import java.io.UnsupportedEncodingException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.util.Arrays;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.whale.system.common.exception.BaseException;

/**
 * 字符串工具类
 *
 * @author 王金绍
 * 2014年9月6日-下午1:33:02
 */
public class Strings {
	
	private final static String HEX_NUMS_STR = "0123456789ABCDEF";  
    private final static Integer SALT_LENGTH = 12;  
	
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
     * 将16进制字符串转换成数组 
     *  
     * @return byte[] 
     * @author jacob 
     * */  
    private static byte[] hexStringToByte(String hex) {  
        /* len为什么是hex.length() / 2 ? 
         * 首先，hex是一个字符串，里面的内容是像16进制那样的char数组 
         * 用2个16进制数字可以表示1个byte，所以要求得这些char[]可以转化成什么样的byte[]，首先可以确定的就是长度为这个char[]的一半 
         */  
        int len = (hex.length() / 2);  
        byte[] result = new byte[len];  
        char[] hexChars = hex.toCharArray();  
        for (int i = 0; i < len; i++) {  
            int pos = i * 2;  
            result[i] = (byte) (HEX_NUMS_STR.indexOf(hexChars[pos]) << 4 | HEX_NUMS_STR .indexOf(hexChars[pos + 1]));  
        }  
        return result;  
    }  
      
    /** 
     * 将数组转换成16进制字符串 
     *  
     * @return String 
     * @author jacob 
     * 
     * */  
    private static String byteToHexString(byte[] salt){  
        StringBuffer hexString = new StringBuffer();  
        for (int i = 0; i < salt.length; i++) {  
            String hex = Integer.toHexString(salt[i] & 0xFF);  
            if(hex.length() == 1){  
                hex = '0' + hex;  
            }  
            hexString.append(hex.toUpperCase());  
        }  
        return hexString.toString();  
    }  
      
    /** 
     * 解密 
     * 
     * @param passwd 待比较的密文
     * @param encryptStr 加密后的密文
     * @return 
     * @throws NoSuchAlgorithmException 
     * @throws UnsupportedEncodingException 
     */  
    public static boolean decrypt(String passwd, String encryptStr) {  
    	try{
	        byte[] pwIndb =  hexStringToByte(encryptStr);  
	        //定义salt  
	        byte[] salt = new byte[SALT_LENGTH];  
	        System.arraycopy(pwIndb, 0, salt, 0, SALT_LENGTH);  
	        //创建消息摘要对象  
	        MessageDigest md = MessageDigest.getInstance("MD5");  
	        //将盐数据传入消息摘要对象  
	        md.update(salt);  
	        md.update(passwd.getBytes("UTF-8"));  
	        byte[] digest = md.digest();  
	        //声明一个对象接收数据库中的口令消息摘要  
	        byte[] digestIndb = new byte[pwIndb.length - SALT_LENGTH];  
	        //获得数据库中口令的摘要  
	        System.arraycopy(pwIndb, SALT_LENGTH, digestIndb, 0,digestIndb.length);  
	        //比较根据输入口令生成的消息摘要和数据库中的口令摘要是否相同  
	        return Arrays.equals(digest, digestIndb);
        }catch(Exception e){
        	throw new BaseException("解密出现异常", e);
        }
    }  
      
    /** 
     * 获得md5之后的16进制字符 
     * @param passwd 用户输入密码字符 
     * @return String md5加密后密码字符 
     * @throws NoSuchAlgorithmException 
     * @throws UnsupportedEncodingException 
     */  
    public static String encrypt(String passwd){  
    	try{
	        //拿到一个随机数组，作为盐  
	        byte[] pwd = null;  
	        SecureRandom sc= new SecureRandom();  
	        byte[] salt = new byte[SALT_LENGTH];  
	        sc.nextBytes(salt);  
	          
	        //声明摘要对象，并生成  
	        MessageDigest md = MessageDigest.getInstance("MD5");  
	        md.update(salt);  
	        md.update(passwd.getBytes("UTF-8"));  
	        byte[] digest = md.digest();  
	          
	        pwd = new byte[salt.length + digest.length];  
	        System.arraycopy(salt, 0, pwd, 0, SALT_LENGTH);  
	        System.arraycopy(digest, 0, pwd, SALT_LENGTH, digest.length);  
	        return byteToHexString(pwd); 
        }catch(Exception e){
        	throw new BaseException("加密出现异常", e);
        }
    }
    
    public static void main(String[] args) {
		System.out.println(encrypt("111111"));
	}
}
