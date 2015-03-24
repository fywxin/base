package org.whale.ext.session;

import java.io.IOException;
import java.util.HashSet;
import java.util.Set;
import java.util.UUID;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.whale.system.common.util.Strings;

public class CacheSessionFilter implements Filter {
	
	private static Logger logger = LoggerFactory.getLogger(CacheSessionFilter.class);

	private String cookieValuePrefix = "session_";
	private String cookieName = "JSESSIONID";
	private String cookiePath = "/";
	private int cookieMaxAge = -1;
	private String domain;
	private int sessionTimeOut = 30;
	
	//静态资源，不需要session，直接返回
	private Set<String> ignoreUrlSuffix = new HashSet<String>();

	@Override
	public void destroy() {
	}

	@Override
	public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {
		HttpServletRequest httpRequest = (HttpServletRequest) request;
		String uri = httpRequest.getRequestURI();
		
		boolean contain = false;
		int point = uri.lastIndexOf(".");
		if(point != -1){
			String type = uri.substring(point+1).trim().toUpperCase();
			contain = ignoreUrlSuffix.contains(type);
		}
		//属于静态页面，直接返回
		if(contain){
			chain.doFilter(request, response);
			return ;
		}
		
		Cookie[] cookies = httpRequest.getCookies();
		Cookie cookie = null;
		if (cookies != null) {
			for (int i = 0; i < cookies.length; i++) {
				cookie = cookies[i];
				if (cookieName.equals(cookie.getName())) {
					break;
				}
			}
		}

		String uuidCookieValue = null;
		if ((cookie == null) || "".equals((uuidCookieValue = cookie.getValue()))) {
			uuidCookieValue = this.cookieValuePrefix+ UUID.randomUUID().toString();
			Cookie uuidSessionCookie = new Cookie(this.cookieName, uuidCookieValue);
			if (this.domain != null) {
				uuidSessionCookie.setDomain(this.domain);
			}
			uuidSessionCookie.setPath(this.cookiePath);
			uuidSessionCookie.setMaxAge(this.cookieMaxAge);
			((HttpServletResponse) response).addCookie(uuidSessionCookie);
			if(logger.isDebugEnabled()){
				logger.debug("SESSION: "+cookieName+" 的cookie不存在，创建cookie："+uuidSessionCookie+"; ip:"+httpRequest.getRemoteAddr());
			}
		}
		
		CacheSession session = null;
		try {
			//从分布式缓存中读取session，如果不存在就创建，存在则保存
			CacheSessionHttpServletRequest requestWarpper = new CacheSessionHttpServletRequest(httpRequest, uuidCookieValue,this.sessionTimeOut);
			session = (CacheSession)requestWarpper.getSession();
			chain.doFilter(requestWarpper, response);
		}finally{
			//session 发送改变，将变化的session保存回缓存
			if(session.isAvailable() && session.isChange()){
				if(logger.isDebugEnabled()){
					logger.debug("SESSION: 值改变，重新保存session对象："+session);
				}
				CacheSessionStoreImpl.getThis().putSession(session.getId(), session, session.getMaxInactiveInterval() * 1000);
			}
		}
	}

	@Override
	public void init(FilterConfig config) throws ServletException {
		if(config.getInitParameter("sessionTimeOut") != null) {
			try {
				this.sessionTimeOut = Integer.parseInt(config.getInitParameter("sessionTimeOut"));
			} catch (NumberFormatException e) {
				throw new ServletException("sessionTimeOut参数类型必须是int.");
			}
		}

		if(config.getInitParameter("cookieName") != null) {
			this.cookieName = config.getInitParameter("cookieName");
		}
		if(config.getInitParameter("cookiePath") != null) {
			this.cookiePath = config.getInitParameter("cookiePath");
		}
		if(config.getInitParameter("domain") != null) {
			this.domain = config.getInitParameter("domain");
		}
		if(config.getInitParameter("cookieValuePrefix") != null) {
			this.cookieValuePrefix = config.getInitParameter("cookieValuePrefix");
		}
		if(config.getInitParameter("cookieMaxAge") != null) {
			try {
				this.cookieMaxAge = Integer.parseInt(config.getInitParameter("cookieMaxAge"));
			} catch (NumberFormatException e) {
				throw new ServletException("uuidCookieMaxAge 配置项必须是整数，单位秒，含义：uuidCookieMaxAge <0 表示30分钟；uuidCookieMaxAge=0 表示用不过期； uuidCookieMaxAge>0 表示过期时间");
			}
		}
		if(config.getInitParameter("ignoreUrlSuffix") != null) {
			String[] ignoreUrlSuffixS = config.getInitParameter("ignoreUrlSuffix").split(",");
			for(String str : ignoreUrlSuffixS){
				if(Strings.isNotBlank(str)){
					ignoreUrlSuffix.add(str.trim().toUpperCase());
				}
			}
		}
		
		logger.info("SESSION: 分布式缓存过滤器初始化完成！"+ this);
		
		//TODO 直接从servlet容器获取 HttpSessionListener
	}

	@Override
	public String toString() {
		return "CacheSessionFilter [cookieValuePrefix=" + cookieValuePrefix
				+ ", cookieName=" + cookieName + ", cookiePath=" + cookiePath
				+ ", cookieMaxAge=" + cookieMaxAge + ", domain=" + domain
				+ ", sessionTimeOut=" + sessionTimeOut + ", ignoreUrlSuffix="
				+ ignoreUrlSuffix + "]";
	}

}
