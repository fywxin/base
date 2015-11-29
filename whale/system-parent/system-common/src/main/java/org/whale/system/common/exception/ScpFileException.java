package org.whale.system.common.exception;

/**
 * SCP 上传文件异常
 * 
 * @author 王金绍
 * @date 2015年11月29日 下午4:08:28
 */
public class ScpFileException extends BaseException {

	private static final long serialVersionUID = 23947234752341L;

	public ScpFileException(String message, Throwable cause) {
		super(message, cause);
	}
	
	public ScpFileException(Throwable cause){
		super(cause);
	}
	public ScpFileException(String message) {
		super(message);
	}
}
