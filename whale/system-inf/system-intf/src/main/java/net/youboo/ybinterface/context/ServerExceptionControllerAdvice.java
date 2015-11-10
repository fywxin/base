package net.youboo.ybinterface.context;

import javax.servlet.http.HttpServletResponse;

import net.youboo.ybinterface.constant.ErrorCode;
import net.youboo.ybinterface.exceptions.InfException;

import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.http.HttpStatus;
import org.whale.system.common.exception.FieldValidErrorException;

@ControllerAdvice
public class ServerExceptionControllerAdvice {

	@ExceptionHandler(FieldValidErrorException.class)
	@ResponseStatus(HttpStatus.ACCEPTED)
	@ResponseBody
	public Result<?> fieldValidException(HttpServletResponse response, FieldValidErrorException e) {
		response.addHeader("encrypt", "0");
		
		return Result.fail(ErrorCode.FIELD_VALID_ERROR, e.getError());
	}
	
	@ExceptionHandler(InfException.class)
	@ResponseStatus(HttpStatus.ACCEPTED)
	@ResponseBody
	public Result<?> infException(HttpServletResponse response, InfException e) {
		response.addHeader("encrypt", "0");
		
		return Result.fail(e.getCode(), e.getMessage());
	}
}
