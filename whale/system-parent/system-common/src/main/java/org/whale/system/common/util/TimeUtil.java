package org.whale.system.common.util;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * 时间工具类
 *
 * @author 王金绍
 * 2014年9月6日-下午1:33:16
 */
public class TimeUtil {
	
	public static final String COMMON_FORMAT = "yyyy-MM-dd HH:mm:ss";
	
	public static final String DAY_FORMAT = "yyyy-MM-dd";

	/**
	 * 获取当前时间
	 * @return
	 */
	public static Long getCurrentTime(){
		return System.currentTimeMillis();
	}
	
	/**
	 * 获取几个秒后时间
	 * @param minute
	 * @return
	 */
	public static Long getNextSecond(Integer second){
		Long time = second * 1000L;
		return System.currentTimeMillis()+time;
	}
	
	/**
	 * 获取几个分钟后时间
	 * @param minute
	 * @return
	 */
	public static Long getNextMinute(Integer minute){
		Long time = minute * 60000L;
		return System.currentTimeMillis()+time;
	}
	
	/**
	 * 获取几个小时后时间
	 * @param hour
	 * @return
	 */
	public static Long getNextHourTime(Integer hour){
		Long time = hour * 3600000L;
		return System.currentTimeMillis()+time;
	}
	
	/**
	 * 获取几天后时间
	 * @param day
	 * @return
	 */
	public static Long getNextDay(Integer day){
		Long time = day * 86400000L;
		return System.currentTimeMillis()+time;
	}
	
	/**
	 * 获取几个秒前时间
	 * @param minute
	 * @return
	 */
	public static Long getPrexSecond(Integer second){
		Long time = second * 1000L;
		return System.currentTimeMillis() - time;
	}
	
	/**
	 * 获取几个分钟前时间
	 * @param minute
	 * @return
	 */
	public static Long getPrexMinute(Integer minute){
		Long time = minute * 60000L;
		return System.currentTimeMillis() - time;
	}
	
	/**
	 * 获取几个小时前时间
	 * @param hour
	 * @return
	 */
	public static Long getPrexHourTime(Integer hour){
		Long time = hour * 3600000L;
		return System.currentTimeMillis() - time;
	}
	
	/**
	 * 获取几天前时间
	 * @param day
	 * @return
	 */
	public static Long getPrexDay(Integer day){
		Long time = day * 86400000L;
		return System.currentTimeMillis() - time;
	}
	
	/**
	 * Long 转 日期Date
	 * @param time
	 * @return
	 */
	public static Date parse2Date(Long time){
		return new Date(time);
	}
	
	public static String formatTime(Long time, String format){
		return formatTime(parse2Date(time), format);
	}
	
	public static String formatTime(Date date, String format){
		DateFormat df = new SimpleDateFormat(format);
		return df.format(date);
	}
	
	public static Date parseTime(String time){
		DateFormat df =  null;
		if(time.length() > 10){
			df = new SimpleDateFormat(COMMON_FORMAT);
		}else{
			df = new SimpleDateFormat(DAY_FORMAT);
		}
		try {
			return df.parse(time);
		} catch (ParseException e) {
			throw new RuntimeException("时间 "+time +" 与格式不匹配！");
		}
	}
	
	public static Integer countDay(Long time){
		Long days = time / 86400000;
		return days.intValue();
	}
	
	public static Integer countHour(Long time){
		Long days = time / 3600000;
		return days.intValue();
	}
	
	public static Integer countMinute(Long time){
		Long days = time / 60000;
		return days.intValue();
	}
	
	public static Long countSecond(Long time){
		return time / 1000;
	}
	
	

}
