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
import org.whale.system.common.exception.BusinessException;
import org.whale.system.common.exception.FieldValidErrorException;

import com.alibaba.fastjson.JSONException;


@ControllerAdvice
public class InfExceptionControllerAdvice {

	private static final Logger logger = LoggerFactory.getLogger("server");
	
	@Autowired
	private ServerIntfFilterRunner filterRunner;


	@ExceptionHandler(FieldValidErrorException.class)
	@ResponseStatus(HttpStatus.ACCEPTED)
	@ResponseBody
	public Result<?> fieldValidException(HttpServletResponse response, FieldValidErrorException e) {
		logger.error("接口参数校验失败", e);
		try{
			ServerContext context = ServerContext.get();
			if(context != null){
				response.addHeader("reqno", context.getReqno());
			}
			filterRunner.exeException(context, e);
		}finally{
			ServerContext.remove();
		}
		
		return Result.fail(ResultCode.FIELD_VALID_ERROR, e.getError());
	}
	
	@ExceptionHandler(InfException.class)
	@ResponseStatus(HttpStatus.ACCEPTED)
	@ResponseBody
	public Result<?> infException(HttpServletResponse response, InfException e) {
		logger.error("接口异常", e);
		try{
			ServerContext context = ServerContext.get();
			if(context != null){
				response.addHeader("reqno", context.getReqno());
			}
			filterRunner.exeException(context, e);
		}finally{
			ServerContext.remove();
		}
		
		return Result.fail(e.getCode(), e.getMessage());
	}
	
	@ExceptionHandler(Exception.class)
	@ResponseStatus(HttpStatus.ACCEPTED)
	@ResponseBody
	public Result<?> unknowException(HttpServletResponse response, Exception e) {
		logger.error("接口未知异常", e);
		try{
			ServerContext context = ServerContext.get();
			if(context != null){
				response.addHeader("reqno", context.getReqno());
			}
			filterRunner.exeException(context, e);
		}finally{
			ServerContext.remove();
		}
		
		return Result.fail(ResultCode.UNKNOW_ERROR, e.getMessage());
	}
	
	@ExceptionHandler(BusinessException.class)
	@ResponseStatus(HttpStatus.ACCEPTED)
	@ResponseBody
	public Result<?> businessException(HttpServletResponse response, BusinessException e) {
		logger.error("接口业务处理异常", e);
		try{
			ServerContext context = ServerContext.get();
			if(context != null){
				response.addHeader("reqno", context.getReqno());
			}
			filterRunner.exeException(context, e);
		}finally{
			ServerContext.remove();
		}
		
		return Result.fail(ResultCode.BUSINESS_ERROR.getCode(), e.getMessage());
	}
	
	
	@ExceptionHandler(JSONException.class)
	@ResponseStatus(HttpStatus.ACCEPTED)
	@ResponseBody
	public Result<?> JSONException(HttpServletResponse response, JSONException e) {
		logger.error("报文数据异常", e);
		try{
			ServerContext context = ServerContext.get();
			if(context != null){
				response.addHeader("reqno", context.getReqno());
			}
			filterRunner.exeException(context, e);
		}finally{
			ServerContext.remove();
		}
		
		return Result.fail(ResultCode.REQ_DATA_ERROR);
	}
}
