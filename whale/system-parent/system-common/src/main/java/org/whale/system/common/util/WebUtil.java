package org.whale.system.common.util;

import java.io.BufferedOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.io.UnsupportedEncodingException;
import java.net.URL;
import java.net.URLEncoder;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.whale.system.common.exception.SysException;

import com.alibaba.fastjson.JSON;

public class WebUtil {
	public static final Logger logger = LoggerFactory.getLogger(WebUtil.class);
	
	//-- Content Type 定义 --//
	public static final String TEXT_TYPE = "text/plain";
	public static final String JSON_TYPE = "application/json";
	public static final String XML_TYPE = "text/xml";
	public static final String HTML_TYPE = "text/html";
	public static final String JS_TYPE = "text/javascript";
	public static final String EXCEL_TYPE = "application/vnd.ms-excel";
	
	private static final String DEFAULT_SUCCESS_MSG = "操作成功";
	private static final String DEFAULT_FAIL_MSG = "操作失败";
	
	private static String WEB_PATH;
	
	public static Integer getInt(HttpServletRequest request, String key, Integer defVal){
		String obj = request.getParameter(key);
		if(obj == null || "".equals(obj.trim()))
			return defVal;
		
		try {
			return Integer.parseInt(obj);
		} catch (NumberFormatException e) {
			return defVal;
		}
	}
	
	public static Long getLong(HttpServletRequest request, String key, Long defVal){
		String obj = request.getParameter(key);
		if(obj == null || "".equals(obj.trim()))
			return defVal;
		
		try {
			return Long.parseLong(obj);
		} catch (NumberFormatException e) {
			return defVal;
		}
	}
	
	public static String getWebPath(){
		return WEB_PATH;
	}
	
	public static void setWebPath(String path){
		WEB_PATH = path;
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
	 * 将处理成功结果(默认提示信息）返回给客服端ajax脚本
	 * @param response
	 */
	public static void printSuccess(HttpServletRequest request, HttpServletResponse response) {
		printSuccess(request, response, DEFAULT_SUCCESS_MSG, null);
	}
	
	/**
	 * 将处理成功结果(用户设置提示信息）返回给客服端ajax脚本
	 * @param response
	 * @param msg
	 */
	public static void printSuccess(HttpServletRequest request, HttpServletResponse response, String msg) {
		printSuccess(request, response, msg, null);
	}
	
	/**
	 * 将处理成功结果(用户返回的数据）返回给客服端ajax脚本
	 * @param response
	 * @param map 用户返回的数据
	 */
	public static void printSuccess(HttpServletRequest request, HttpServletResponse response, Object obj) {
		printSuccess(request, response, DEFAULT_SUCCESS_MSG, obj);
	}
	
	/**
	 * 将处理成功结果(用户设置提示信息,用户返回的数据）返回给客服端ajax脚本
	 * @param response
	 * @param msg  用户设置提示信息
	 * @param map  用户返回的数据
	 */
	public static void printSuccess(HttpServletRequest request, HttpServletResponse response, String msg, Object obj) {
		doPrint(request, response, buildRs(true, msg, obj));
	}
	
	/**
	 * 将处理失败结果（用户设置失败提示信息） 返回给客服端ajax脚本
	 * @param response
	 * @param msg  用户设置失败提示信息
	 */
	public static void printFail(HttpServletRequest request, HttpServletResponse response, String msg) {
		printFail(request, response, msg, null);
	}
	
	/**
	 * 将处理失败结果（用户返回失败相关数据） 返回给客服端ajax脚本
	 * @param response
	 * @param map  用户返回失败相关数据
	 */
	public static void printFail(HttpServletRequest request, HttpServletResponse response, Object obj) {
		printFail(request, response, DEFAULT_FAIL_MSG, obj);
	}
	
	/**
	 * 将处理失败结果（用户设置失败提示信息,用户返回失败相关数据） 返回给客服端ajax脚本
	 * @param response
	 * @param msg  用户设置失败提示信息
	 * @param map  用户返回失败相关数据
	 */
	public static void printFail(HttpServletRequest request, HttpServletResponse response, String msg, Object obj) {
		doPrint(request, response, buildRs(false, msg, obj));
	}
	
	/**
	 * 将处理失败结果（用户设置失败提示信息,用户返回失败相关数据） 返回给客服端ajax脚本
	 * @param request
	 * @param response
	 * @param msg
	 * @param code 编码
	 */
	public static void printFail(HttpServletRequest request, HttpServletResponse response, String msg, String code) {
		doPrint(request, response, new Result(false, msg, code).toString());
	}
	
	/**
	 * 将处理失败结果（用户设置失败提示信息,用户返回失败相关数据） 返回给客服端ajax脚本
	 * @param request
	 * @param response
	 * @param msg 信息
	 * @param code 编码
	 * @param obj 数据
	 */
	public static void printFail(HttpServletRequest request, HttpServletResponse response, String msg, String code, Object obj) {
		doPrint(request, response, new Result(false, msg, obj, code).toString());
	}
	
	
	/**
	 * 返回信息最后JSON格式
	 * @param success  处理结果是否成功
	 * @param msg  提示信息
	 * @param map  用户数据
	 * @return
	 */
	private static String buildRs(boolean success, String msg, Object datas){
//		Result result = new Result(success, msg, datas);
		
//		StringBuilder strb = new StringBuilder("{\"rs\":");
//		strb.append(success ? "true" : "false")
//			.append(",\"msg\":\"").append(null == msg ? "" : msg)
//			.append("\",\"datas\":").append(null == obj ? "{}" : JSON.toJSONString(obj))
//			.append("}");
		
		return new Result(success, msg, datas).toString();
	}
	
	/**
	 * 将用户数据返回给相应的客服端请求ajax页面
	 * @param response
	 * @param str
	 */
	public static void print(HttpServletRequest request, HttpServletResponse response, Object obj){
		doPrint(request, response, JSON.toJSONString(obj));
	}
	
	/**
	 * 将用户数据返回给相应的客服端请求ajax页面
	 * @param response
	 * @param str
	 */
	public static void doPrint(HttpServletRequest request, HttpServletResponse response, String str){
		setDisableCacheHeader(response);
		response.setHeader("Content-type", JSON_TYPE);  
		
		PrintWriter out = null;
		try {
			out = response.getWriter();
			out.print(str);
			out.flush();
		} catch (IOException e) {
			e.printStackTrace();
		}finally{
			if(null != out){
				out.close();
			}
		}
	}
	
	/**
	 * 如果通过了多级反向代理的话，X-Forwarded-For的值并不止一个，而是一串IP值，
	 * 那么真正的用户端的真实IP则是取X-Forwarded-For中第一个非unknown的有效IP字符串。
	 * @param request
	 * @return
	 */
	public static String getIp(HttpServletRequest request){
		try {
			request.setCharacterEncoding("UTF-8");
		} catch (UnsupportedEncodingException e) {
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
		if("0:0:0:0:0:0:0:1".equals(ip) || "localhost".equals(ip) || "0.0.0.1".equals(ip))
			ip="127.0.0.1";
		return ip;
	}
	
	//--------------------------------------------------------------------------------------
	
	/**
	 * 设置客户端缓存过期时间 的Header.
	 */
	public static void setExpiresHeader(HttpServletResponse response, long expiresSeconds) {
		//Http 1.0 header
		response.setDateHeader("Expires", System.currentTimeMillis() + expiresSeconds * 1000);
		//Http 1.1 header
		response.setHeader("Cache-Control", "private, max-age=" + expiresSeconds);
	}

	/**
	 * 设置禁止客户端缓存的Header.
	 */
	public static void setDisableCacheHeader(HttpServletResponse response) {
		//Http 1.0 header
		response.setDateHeader("Expires", 1L);
		response.addHeader("Pragma", "no-cache");
		//Http 1.1 header
		response.setHeader("Cache-Control", "no-cache, no-store, max-age=0");
	}
	
	/**
	 * 设置让浏览器弹出下载对话框的Header.
	 * 
	 * @param fileName 下载后的文件名.
	 */
	public static void setFileDownloadHeader(HttpServletResponse response, String fileName) {
		try {
			//中文文件名支持
			String encodedfileName = new String(fileName.getBytes(), "ISO8859-1");
			response.setHeader("Content-Disposition", "attachment; filename=\"" + encodedfileName + "\"");
		} catch (UnsupportedEncodingException e) {
		}
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
			response.setHeader("Content-Disposition", "attachment;filename="
					+ URLEncoder.encode(fileName,"utf-8")); // 这个很重要
			response.addHeader("Content-Length", "" + length);
			os = new BufferedOutputStream(response.getOutputStream());
			os.write(b);
			os.flush();
		} catch (Exception e) {
		}finally{
			if(is!=null){
				try {
					is.close();
				} catch (IOException e) {
				}
			}
			if(os!=null){
				try {
					os.close();
				} catch (IOException e) {
				}
			}
		}
	}
	
	public static String simpleHttpGet(String url, Map<String, Object> header){
    	URL urlObj ;
    	InputStream inputStream = null;
    	try{
    		logger.info("抓取开始 url="+url+" \n header = " + header);
            urlObj = new URL(url);
            inputStream = urlObj.openStream();
            int c;
            StringBuilder sb = new StringBuilder();
            while((c=inputStream.read())!=-1){
                sb.append((char)c);
            }
            
            String text = new String(sb.toString().getBytes("ISO-8859-1"),"UTF-8");
            logger.info("抓取完成  返回内容["+text+"] url="+url);
            return text;
            
        }catch(Exception e){
        	logger.error("抓取页面url="+url+"出现异常",e);
        	throw new SysException("抓取页面url="+url+"出现异常："+e.getMessage(),e);
        }finally{
            if(inputStream != null){
                try {
                    inputStream.close();
                } catch (IOException e) {
                    logger.error("关闭抓取流出现异常",e);
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
