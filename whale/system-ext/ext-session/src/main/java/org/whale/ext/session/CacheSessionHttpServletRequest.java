package org.whale.ext.session;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletRequestWrapper;
import javax.servlet.http.HttpSession;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * session 获取器
 *
 * @author 王金绍
 * @date 2015年1月16日 下午4:19:20
 */
public class CacheSessionHttpServletRequest extends HttpServletRequestWrapper {
	
	private static Logger logger = LoggerFactory.getLogger(CacheSessionHttpServletRequest.class);
	
	private String id;
	//分钟为单位
	private int sessionTimeout = 30;
	
	private CacheSession session = null;
	

	public CacheSessionHttpServletRequest(HttpServletRequest request) {
		super(request);
	}
	
	public CacheSessionHttpServletRequest(HttpServletRequest request, String id, int sessionTimeout) {
		super(request);
		this.id = id;
		this.sessionTimeout = sessionTimeout;
	}

	@Override
	public HttpSession getSession() {
		if(this.session ==null){
			if(logger.isDebugEnabled()){
				logger.debug("SESSION: 开始从缓存中读取session");
			}
			this.session = CacheSessionStoreImpl.getThis().getSession(id);
			if(this.session == null){
				this.session = newSession(id);
			}else{
				this.session.setLastAccessedTime(System.currentTimeMillis());
				//TODO 因为是分布式，所以servletContext 要重新设置
				this.session.setServletContext(super.getSession().getServletContext());
			}
			if(logger.isDebugEnabled()){
				logger.debug("SESSION: 读取SESSION成功，返回"+session);
			}
		}
		return this.session;
	}

	@Override
	public HttpSession getSession(boolean create) {
		if(this.session ==null){
			if(logger.isDebugEnabled()){
				logger.debug("SESSION: 开始从缓存中读取session");
			}
			this.session = CacheSessionStoreImpl.getThis().getSession(id);
			if(session == null){
				if(create){
					this.session = newSession(id);
				}else{
					if(logger.isDebugEnabled()){
						logger.debug("SESSION: 缓存中读取不到session，不在创建session");
					}
				}
			}else{
				this.session.setLastAccessedTime(System.currentTimeMillis());
				//TODO 因为是分布式，所以servletContext 要重新设置
				this.session.setServletContext(super.getSession().getServletContext());
			}
			if(logger.isDebugEnabled()){
				logger.debug("SESSION: 读取SESSION成功，返回"+session);
			}
		}
		return this.session;
	}

	public boolean isExistsSession(String id){
	    return CacheSessionStoreImpl.getThis().getSession(id) != null;
	}
	
	public CacheSession newSession(String id){
	    HttpSession webSession = super.getSession(true);
	    CacheSession session = new CacheSession(id);
	    session.setLastAccessedTime(System.currentTimeMillis());
	    session.setMaxInactiveInterval(this.sessionTimeout * 60);
	    session.setNewInstance(true);
	    
	    session.setServletContext(webSession.getServletContext());
	    
	    if(logger.isDebugEnabled()){
			logger.debug("SESSION: session不存在，创建新对象："+session);
		}
	    return session;
	  }
}
