package org.whale.system.common.exception;

import java.util.Map;

public class FieldValidErrorException extends BusinessException {
	
	private static final long serialVersionUID = 9878717967545L;
	
	private Map<String, String> error;

	public FieldValidErrorException(String message) {
		super(message);
	}
	
	public FieldValidErrorException(Map<String, String> error) {
		super("参数交易异常");
		this.error = error;
	}
	
	public FieldValidErrorException(String message, Map<String, String> error) {
		super(message);
		this.error = error;
	}

	public Map<String, String> getError() {
		return error;
	}

	public void setError(Map<String, String> error) {
		this.error = error;
	}
}
