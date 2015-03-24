package org.whale.ext.session;

import java.io.Serializable;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Map;

import javax.servlet.ServletContext;
import javax.servlet.http.HttpSession;
import javax.servlet.http.HttpSessionContext;
import javax.servlet.http.HttpSessionEvent;

import com.alibaba.fastjson.annotation.JSONField;

public class CacheSession implements HttpSession, Serializable {

	private static final long serialVersionUID = 123834230903212190L;
	
	private String id;
	
	private long createTime;
	
	private long lastAccessedTime;
	
	//setMaxInactiveInterval和session-config的比较：   
	//1、setMaxInactiveInterval的优先级高，如果setMaxInactiveInterval没有设置，则默认是session-config中设置的时间。   
	//2、setMaxInactiveInterval设置的是当前会话的失效时间，不是整个web服务的。   
	//3、setMaxInactiveInterval的参数是秒，session-config当中配置的session-timeout是分钟。  
	private int maxInactiveInterval = 30;
	
	private boolean newInstance;
	
	private Map<String, Object> attMap = new HashMap<String, Object>(4);
	
	private Map<String, Object> valMap = new HashMap<String, Object>();
	
	//session 是否有效
	@JSONField(deserialize=false, serialize=false)
	private transient boolean available = true;
	//session 是否改变，如果改变需要把变更后的session返回给缓存
	@JSONField(deserialize=false, serialize=false)
	private transient boolean change = false;
	@JSONField(deserialize=false, serialize=false)
	private transient ServletContext servletContext;
	@JSONField(deserialize=false, serialize=false)
	private transient HttpSessionContext httpSessionContext;
	
	public CacheSession(){
		
	}
	
	public CacheSession(String id) {
		this.id = id;
		this.createTime = System.currentTimeMillis();
		this.lastAccessedTime = this.createTime;
		this.newInstance = true;
		CacheSessionListenerImpl.getThis().broadcastSessionCreated(new HttpSessionEvent(this));
		change();
	}

	@Override
	public Object getAttribute(String key) {
		return attMap.get(key);
	}

	@Override
	public Enumeration<String> getAttributeNames() {
		return new StringEnumeration(attMap.keySet().iterator());
	}

	@Override
	public long getCreationTime() {
		return createTime;
	}

	@Override
	public String getId() {
		return id;
	}

	@Override
	public long getLastAccessedTime() {
		return lastAccessedTime;
	}

	@Override
	public int getMaxInactiveInterval() {
		return maxInactiveInterval;
	}

	@Override
	public ServletContext getServletContext() {
		return servletContext;
	}
	
	public void setServletContext(ServletContext servletContext) {
		this.servletContext = servletContext;
	}

	@Override
	public HttpSessionContext getSessionContext() {
		return new CacheHttpSessionContext();
	}

	@Override
	public Object getValue(String key) {
		return this.valMap.get(key);
	}

	@Override
	public String[] getValueNames() {
		return this.valMap.keySet().toArray(new String[this.valMap.size()]);
	}

	@Override
	public void invalidate() {
		CacheSessionStoreImpl.getThis().removeSession(id);
		CacheSessionListenerImpl.getThis().broadcastSessionDestroyed(new HttpSessionEvent(this));
		unAvailable();
	}

	@Override
	public boolean isNew() {
		return this.newInstance;
	}

	@Override
	public void putValue(String key, Object value) {
		change();
		this.valMap.put(key, value);
	}

	@Override
	public void removeAttribute(String key) {
		change();
		this.attMap.remove(key);
	}

	@Override
	public void removeValue(String key) {
		change();
		this.valMap.remove(key);
	}

	@Override
	public void setAttribute(String key, Object value) {
		change();
		this.attMap.put(key, value);
	}

	@Override
	public void setMaxInactiveInterval(int maxInactiveInterval) {
		this.maxInactiveInterval = maxInactiveInterval;
	}

	public void setLastAccessedTime(long lastAccessedTime) {
		this.lastAccessedTime = lastAccessedTime;
	}

	public void setNewInstance(boolean newInstance) {
		this.newInstance = newInstance;
	}
	
	public long getCreateTime() {
		return createTime;
	}

	public void setCreateTime(long createTime) {
		this.createTime = createTime;
	}

	public Map<String, Object> getAttMap() {
		return attMap;
	}

	public void setAttMap(Map<String, Object> attMap) {
		this.attMap = attMap;
	}

	public Map<String, Object> getValMap() {
		return valMap;
	}

	public void setValMap(Map<String, Object> valMap) {
		this.valMap = valMap;
	}

	public boolean isNewInstance() {
		return newInstance;
	}

	public void setId(String id) {
		this.id = id;
	}
	
	public void change(){
		change = true;
	}

	public boolean isChange() {
		return change;
	}

	public boolean isAvailable() {
		return available;
	}

	public void unAvailable() {
		this.available = false;
	}

	@Override
	public String toString() {
		return "CacheSession [id=" + id + ", createTime=" + createTime
				+ ", lastAccessedTime=" + lastAccessedTime
				+ ", maxInactiveInterval=" + maxInactiveInterval
				+ ", newInstance=" + newInstance + ", attMap=" + attMap
				+ ", valMap=" + valMap + ", available=" + available
				+ ", change=" + change + "]";
	}

}
