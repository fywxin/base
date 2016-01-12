package org.whale.system.cache.impl.jvm;

import java.lang.ref.SoftReference;

/**
 * jvm缓存实体对象
 *
 * @author 王金绍
 * 2014年10月24日-上午11:49:25
 */
@SuppressWarnings("all")
public class CacheEntry {
	
	private Object value;
	/**不会过期 */
	private boolean forever = true;
	/**有效时间，单位秒 */
	private int expTime;
	
	private long lastActiveTime;
	
	private boolean useSoftReference = false;
	
	public CacheEntry(Object value){
		this.setVaule(value);
	}
	
	public CacheEntry(Object value, boolean useSoftReference){
		this.useSoftReference = useSoftReference;
		this.setVaule(value);
	}
	
	public CacheEntry(Object value, Integer expTime){
		if(expTime != null && expTime > 0){
			this.lastActiveTime = System.currentTimeMillis();
			this.expTime = expTime;
			this.forever = false;
		}else{
			this.forever = true;
		}
		this.setVaule(value);
	}
	
	public CacheEntry(Object value, Integer expTime, boolean useSoftReference){
		this.useSoftReference = useSoftReference;
		if(expTime != null && expTime > 0){
			this.lastActiveTime = System.currentTimeMillis();
			this.expTime = expTime;
			this.forever = false;
		}else{
			this.forever = true;
		}
		this.setVaule(value);
	}
	
	public void updateLastActiveTime(){
		this.lastActiveTime=System.currentTimeMillis();
	}
	
	/**
	 * 缓存是否过期
	 * 
	 * @return
	 */
	public boolean isOutOfDate() {
        return !this.forever && (System.currentTimeMillis() - lastActiveTime) >= this.expTime*1000L;
    }

	public Object getValue() {
		if(!this.forever){
			lastActiveTime = System.currentTimeMillis();
		}
		if(value == null)
			return null;
		if(useSoftReference){
			 return ((java.lang.ref.SoftReference) value).get();
		}else{
			return this.value;
		}
	}
	
	public void setVaule(Object value){
		if(useSoftReference){
			this.value = new SoftReference(value);
		}else{
			this.value = value;
		}
	}

	public Boolean getForever() {
		return forever;
	}

	public Integer getExpTime() {
		return expTime;
	}

	public long getLastActiveTime() {
		return lastActiveTime;
	}

	public boolean isUseSoftReference() {
		return useSoftReference;
	}

	@Override
	public String toString() {
		return "CacheEntry [value=" + value 
				+ ", forever=" + forever + ", expTime=" + expTime
				+ ", lastActiveTime=" + lastActiveTime + ", useSoftReference="
				+ useSoftReference + "]";
	}
	
	
}
