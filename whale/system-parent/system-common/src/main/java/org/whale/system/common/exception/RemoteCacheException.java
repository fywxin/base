package org.whale.system.common.exception;

/**
 * 远程缓存不可用
 *
 * @author 王金绍
 * 2014年9月19日-下午5:39:28
 */
public class RemoteCacheException extends SysException {
	private static final long serialVersionUID = -121221L;

	public RemoteCacheException(String msg){
		super(msg);
	}

	public RemoteCacheException(String msg, Exception cause){
		super(msg, cause);
	}
}
