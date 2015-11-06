package org.whale.system.common.util;

import java.io.BufferedOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Map;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.alibaba.fastjson.JSON;

public class WebUtil {
	public static final Logger logger = LoggerFactory.getLogger(WebUtil.class);
	
	private static final String DEFAULT_SUCCESS_MSG = "成功";
	private static final String DEFAULT_FAIL_MSG = "失败";
	
	public static HttpServletRequest getRequest(){
		return (HttpServletRequest)ThreadContext.getContext().get(ThreadContext.KEY_REQUEST);
	}
	
	public static HttpServletResponse getResponse(){
		return (HttpServletResponse)ThreadContext.getContext().get(ThreadContext.KEY_RESPONSE);
	}
	
	public static HttpSession getSession(){
		return getRequest().getSession();
	}

	/**
	 * 将处理成功结果(默认提示信息）返回给客服端ajax脚本
	 * @param response
	 */
	public static void success(HttpServletResponse response) {
		success(response, DEFAULT_SUCCESS_MSG, null);
	}
	
	/**
	 * 将处理成功结果(用户设置提示信息）返回给客服端ajax脚本
	 * @param response
	 * @param msg
	 */
	public static void success(HttpServletResponse response, String msg) {
		success(response, msg, null);
	}
	
	/**
	 * 将处理成功结果(用户返回的数据）返回给客服端ajax脚本
	 * @param response
	 * @param map 用户返回的数据
	 */
	public static void success(HttpServletResponse response, Object obj) {
		success(response, DEFAULT_SUCCESS_MSG, obj);
	}
	
	/**
	 * 将处理成功结果(用户设置提示信息,用户返回的数据）返回给客服端ajax脚本
	 * @param response
	 * @param msg  用户设置提示信息
	 * @param map  用户返回的数据
	 */
	public static void success(HttpServletResponse response, String msg, Object obj) {
		doPrint(response, buildRs(true, msg, obj));
	}
	
	/**
	 * 将处理失败结果（用户设置失败提示信息） 返回给客服端ajax脚本
	 * @param response
	 * @param msg  用户设置失败提示信息
	 */
	public static void fail(HttpServletResponse response, String msg) {
		fail(response, msg, null);
	}
	
	/**
	 * 将处理失败结果（用户返回失败相关数据） 返回给客服端ajax脚本
	 * @param response
	 * @param map  用户返回失败相关数据
	 */
	public static void fail(HttpServletResponse response, Object obj) {
		fail(response, DEFAULT_FAIL_MSG, obj);
	}
	
	/**
	 * 将处理失败结果（用户设置失败提示信息,用户返回失败相关数据） 返回给客服端ajax脚本
	 * @param response
	 * @param msg  用户设置失败提示信息
	 * @param map  用户返回失败相关数据
	 */
	public static void fail(HttpServletResponse response, String msg, Object obj) {
		doPrint(response, buildRs(false, msg, obj));
	}
	
	/**
	 * 将处理失败结果（用户设置失败提示信息,用户返回失败相关数据） 返回给客服端ajax脚本
	 * @param request
	 * @param response
	 * @param msg
	 * @param code 编码
	 */
	public static void fail(HttpServletResponse response, String msg, String code) {
		doPrint(response, new Result(false, msg, code).toString());
	}
	
	/**
	 * 将处理失败结果（用户设置失败提示信息,用户返回失败相关数据） 返回给客服端ajax脚本
	 * @param request
	 * @param response
	 * @param msg 信息
	 * @param code 编码
	 * @param obj 数据
	 */
	public static void fail(HttpServletResponse response, String msg, String code, Object obj) {
		doPrint(response, new Result(false, msg, obj, code).toString());
	}
	
	
	/**
	 * 将用户数据返回给相应的客服端请求ajax页面
	 * @param response
	 * @param str
	 */
	public static void print(HttpServletResponse response, Object obj){
		doPrint(response, JSON.toJSONString(obj));
	}
	
	/**
	 * 返回信息最后JSON格式
	 * @param success  处理结果是否成功
	 * @param msg  提示信息
	 * @param map  用户数据
	 * @return
	 */
	private static String buildRs(boolean success, String msg, Object datas){
		return new Result(success, msg, datas).toString();
	}
	
	public static void doPrint(HttpServletResponse response, byte[] datas){
		response.setDateHeader("Expires", 1L);
		response.addHeader("Pragma", "no-cache");
		response.setHeader("Cache-Control", "no-cache, no-store, max-age=0");
		response.setHeader("Content-type", "application/json");  
		response.setContentType("text/xml;charset=utf-8");
		ServletOutputStream out = null;
		try {
			out = response.getOutputStream();
			out.write(datas);
			out.flush();
		}catch (IOException e) {
			logger.error("AJAX返回数据异常", e);		
		}finally{
			if(null != out){
				try {
					out.close();
				} catch (IOException e) {}
			}
		}
	}
	
	/**
	 * 将用户数据返回给相应的客服端请求ajax页面
	 * @param response
	 * @param str
	 */
	public static void doPrint(HttpServletResponse response, String str){
		response.setDateHeader("Expires", 1L);
		response.addHeader("Pragma", "no-cache");
		response.setHeader("Cache-Control", "no-cache, no-store, max-age=0");
		response.setHeader("Content-type", "application/json");  
		//response.setContentType("text/xml;charset=utf-8");
		response.setContentType("application/json;charset=UTF-8");
		
		PrintWriter out = null;
		try {
			out = response.getWriter();
			out.print(str);
			out.flush();
		} catch (IOException e) {
			logger.error("AJAX返回数据异常", e);
		}finally{
			if(null != out){
				out.close();
			}
		}
	}
	
	/**
	 * 判断是否是ajax请求	
	 * @param request
	 * @return
	 */
	public static boolean isAjaxRequest(HttpServletRequest request) {
		return request.getHeader("X-Requested-With") == null;			
	}
	
	/**
	 * 如果通过了多级反向代理的话，X-Forwarded-For的值并不止一个，而是一串IP值，
	 * 那么真正的用户端的真实IP则是取X-Forwarded-For中第一个非unknown的有效IP字符串。
	 * @param request
	 * @return
	 */
	public static String getIp(){
		HttpServletRequest request = getRequest();
		try {
			request.setCharacterEncoding("UTF-8");
		} catch (UnsupportedEncodingException e) {
			logger.error("获取IP异常，编码不支持", e);
		}
		
		String ip = request.getHeader("X-Forwarder-For");
		if((Strings.isBlank(ip)) || "unknown".equalsIgnoreCase(ip)){
			ip = request.getHeader("Proxy-Client-Ip");
			if(Strings.isBlank(ip) || "unknown".equalsIgnoreCase(ip)){
				ip = request.getHeader("WL-Proxy-Client-Ip");
			}
		}else{
			String[] ipArr = ip.split(",");
			for(String IP : ipArr){
				if(!"unknown".equalsIgnoreCase(IP)){
					ip = IP;
					break;
				}
			}
		}
		if((Strings.isBlank(ip)) || "unknown".equalsIgnoreCase(ip)){
			ip = request.getRemoteAddr();
		}
		if("0:0:0:0:0:0:0:1".equals(ip) || "localhost".equals(ip) || "0.0.0.1".equals(ip)){
			ip="127.0.0.1";
		}
		return ip;
	}
	
	/**
	 * 将request的Attribute转化为map
	 * @param request
	 * @return
	 * @author yangz
	 * @date 2013-1-23 上午10:09:59
	 */
	@SuppressWarnings("all")
	public static Map<String, Object> toAttributeMap(HttpServletRequest request){
		Map<String, Object> result = new HashMap<String, Object>();
		Enumeration<String> enumeration = request.getAttributeNames();
		while(enumeration.hasMoreElements()){
			String key = enumeration.nextElement();
			result.put(key, request.getAttribute(key));
		}
		return result;
	}
	
	
	public static void downLoad(HttpServletRequest request, HttpServletResponse response, String fileName, InputStream is){
		OutputStream os = null;
		try {
			int length = is.available();
			byte b[] = new byte[length];
			is.read(b);
			// 清空response
            response.reset();
			response.setContentType("application/octet-stream");// 设置为下载application/x-download
			response.setCharacterEncoding("UTF-8");
			response.setHeader("Content-Disposition", "attachment;filename="+ URLEncoder.encode(fileName,"utf-8")); // 这个很重要
			response.addHeader("Content-Length", "" + length);
			os = new BufferedOutputStream(response.getOutputStream());
			os.write(b);
			os.flush();
		} catch (Exception e) {
			logger.error("下载出现异常", e);
		}finally{
			if(is!=null){
				try {
					is.close();
				} catch (IOException e) {
					logger.error("流关闭异常", e);
				}
			}
			if(os!=null){
				try {
					os.close();
				} catch (IOException e) {
					logger.error("流关闭异常", e);
				}
			}
		}
	}
	
	static class Result{
		
		private boolean rs = true;
		
		private String code = "1";
		
		private String msg;
		
		private Object datas;
		
		public Result(){}
		
		public Result(boolean rs, String msg){
			this.rs = rs;
			this.msg = msg;
		}
		
		public Result(boolean rs, String msg, Object datas) {
			this.rs = rs;
			this.msg = msg;
			this.datas = datas;
		}
		
		public Result(boolean rs, String msg, String code) {
			this.rs = rs;
			this.msg = msg;
			this.code = code;
		}
		
		public Result(boolean rs, String msg, Object datas, String code) {
			this.rs = rs;
			this.msg = msg;
			this.datas = datas;
			this.code = code;
		}
		
		
		public boolean isRs() {
			return rs;
		}

		public void setRs(boolean rs) {
			this.rs = rs;
		}

		public String getMsg() {
			return msg;
		}

		public void setMsg(String msg) {
			this.msg = msg;
		}

		public Object getDatas() {
			return datas;
		}

		public void setDatas(Object datas) {
			this.datas = datas;
		}

		public String getCode() {
			return code;
		}

		public void setCode(String code) {
			this.code = code;
		}

		@Override
		public String toString() {
			StringBuilder strb = new StringBuilder("{\"rs\":");
			strb.append(rs ? "true" : "false").append(",\"code\":\"").append(code)
				.append("\",\"msg\":\"").append(null == msg ? "" : msg)
				.append("\",\"datas\":").append(null == datas ? "{}" : JSON.toJSONString(datas))
				.append("}");
			return strb.toString();
		}
		
	}
	
}
