package org.whale.system.common.exception;

/**
 * 远程缓存不可用
 *
 * @author wjs
 * 2014年9月19日-下午5:39:28
 */
public class RemoteCacheException extends CacheException {
	private static final long serialVersionUID = -121221L;

	public RemoteCacheException(String msg){
		super(msg);
	}

	public RemoteCacheException(String msg, Throwable cause){
		super(msg, cause);
	}
}
