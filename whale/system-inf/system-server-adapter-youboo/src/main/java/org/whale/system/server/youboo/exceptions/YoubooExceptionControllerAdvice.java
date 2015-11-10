package org.whale.system.server.youboo.exceptions;

import javax.servlet.http.HttpServletResponse;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.whale.system.common.exception.FieldValidErrorException;
import org.whale.system.common.util.ThreadContext;
import org.whale.system.inf.ErrorCode;
import org.whale.system.inf.Result;
import org.whale.system.server.ServerException;
import org.whale.system.server.youboo.context.YoubooContext;

@ControllerAdvice
public class YoubooExceptionControllerAdvice {

	@ExceptionHandler(FieldValidErrorException.class)
	@ResponseStatus(HttpStatus.ACCEPTED)
	@ResponseBody
	public Result<?> fieldValidException(HttpServletResponse response, FieldValidErrorException e) {
		YoubooContext context = (YoubooContext)ThreadContext.getContext().get(YoubooContext.THREAD_KEY);
		response.addHeader("reqno", context.getReqParam().getReqno());
		response.addHeader("encrypt", "0");
		
		return Result.fail(ErrorCode.FIELD_VALID_ERROR, e.getError());
	}
	
	@ExceptionHandler(ServerException.class)
	@ResponseStatus(HttpStatus.ACCEPTED)
	@ResponseBody
	public Result<?> serverException(HttpServletResponse response, ServerException e) {
		YoubooContext context = (YoubooContext)ThreadContext.getContext().get(YoubooContext.THREAD_KEY);
		response.addHeader("reqno", context.getReqParam().getReqno());
		response.addHeader("encrypt", "0");
		
		return Result.fail(e.getCode(), e.getMessage());
	}
	
	@ExceptionHandler(Exception.class)
	@ResponseStatus(HttpStatus.ACCEPTED)
	@ResponseBody
	public Result<?> unknowException(HttpServletResponse response, Exception e) {
		YoubooContext context = (YoubooContext)ThreadContext.getContext().get(YoubooContext.THREAD_KEY);
		response.addHeader("reqno", context.getReqParam().getReqno());
		response.addHeader("encrypt", "0");
		
		return Result.fail(ErrorCode.UNKNOW_ERROR, e.getMessage());
	}
}
