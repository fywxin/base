package org.whale.system.validation;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.TreeMap;

import org.whale.system.common.util.Strings;

/**
 * 验证错误信息汇总类
 * 
 * @author 王金绍
 *
 */
public class ValidateErrors {

	/**
	 * 存放错误信息的 map
	 */
	private Map<String, String> errorMap = new TreeMap<String, String>();
	
	/**
	 * 字段有用户定义的错误信息？
	 */
	private Map<String, String> defErrMsgMap = new HashMap<String, String>();
	
	public void addDefErrMsg(String fieldName, String defErrMsg){
		if(Strings.isNotBlank(defErrMsg)){
			defErrMsgMap.put(fieldName, defErrMsg);
		}
	}
	
	public String getDefErrMsg(String fieldName){
		return defErrMsgMap.get(fieldName);
	}
	
	public void putFinal(String fieldName, String errorMsg){
		errorMap.put(fieldName, errorMsg);
	}

	/**
	 * @return 是否存在验证错误
	 */
	public boolean hasError() {
		return errorMap.size() > 0;
	}
	
	/**
	 * @return　返回存在错误的总数
	 */
	public int errorCount() {
		return errorMap.size();
	}

	/**
	 * 增加一个错误信息
	 * 
	 * @param fieldName
	 *            存在错误的字段名
	 * @param errorMessage
	 *            错误的详细信息
	 */
	public void add(String fieldName, String errorMessage) {
		if(defErrMsgMap.containsKey(fieldName))
			return ;
		String oldErrorMessage = errorMap.get(fieldName);
		if(Strings.isNotBlank(oldErrorMessage)){
			errorMessage += ", "+oldErrorMessage;
		}
		errorMap.put(fieldName, errorMessage);
	}
	
	public String get(String fieldName){
		return errorMap.get(fieldName);
	}

	/**
	 * 返回错误信息列表
	 * @return
	 */
	public Collection<String> getErrorsList() {
		return errorMap.values();
	}

	/**
	 * 返回详细的错误信息列表，含验证错误的字段名和提示语
	 * @return
	 */
	public Map<String, String> getErrorsMap() {
		return errorMap;
	}
}