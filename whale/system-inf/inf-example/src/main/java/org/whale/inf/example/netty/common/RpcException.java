package org.whale.inf.example.netty.common;

public class RpcException extends RuntimeException {

	private static final long serialVersionUID = 13435L;

	public RpcException(String message) {
		super(message);
	}

	public RpcException(String message, Throwable cause) {
		super(message, cause);
	}
}
