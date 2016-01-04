package org.whale.inf.server;

import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.whale.inf.common.InfException;
import org.whale.inf.common.Result;
import org.whale.inf.common.ResultCode;
import org.whale.system.common.exception.FieldValidErrorException;


@ControllerAdvice
public class InfExceptionControllerAdvice {

	private static final Logger logger = LoggerFactory.getLogger(InfExceptionControllerAdvice.class);
	
	@Autowired
	private ServerIntfFilterRunner filterRunner;


	@ExceptionHandler(FieldValidErrorException.class)
	@ResponseStatus(HttpStatus.ACCEPTED)
	@ResponseBody
	public Result<?> fieldValidException(HttpServletResponse response, FieldValidErrorException e) {
		ServerContext context = ServerContext.get();
		response.addHeader("reqno", context.getReqno());
		
		filterRunner.exeException(context, e);
		
		return Result.fail(ResultCode.FIELD_VALID_ERROR, e.getError());
	}
	
	@ExceptionHandler(InfException.class)
	@ResponseStatus(HttpStatus.ACCEPTED)
	@ResponseBody
	public Result<?> infException(HttpServletResponse response, InfException e) {
		logger.error("接口异常", e);
		ServerContext context = ServerContext.get();
		response.addHeader("reqno", context.getReqno());
		
		filterRunner.exeException(context, e);
		
		return Result.fail(e.getCode(), e.getMessage());
	}
	
	@ExceptionHandler(Exception.class)
	@ResponseStatus(HttpStatus.ACCEPTED)
	@ResponseBody
	public Result<?> unknowException(HttpServletResponse response, Exception e) {
		logger.error("未知异常", e);
		ServerContext context = ServerContext.get();
		response.addHeader("reqno", context.getReqno());
		
		filterRunner.exeException(context, e);
		
		return Result.fail(ResultCode.UNKNOW_ERROR, e.getMessage());
	}
}
