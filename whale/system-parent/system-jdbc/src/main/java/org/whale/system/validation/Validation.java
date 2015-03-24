package org.whale.system.validation;

/**
 * 验证工具类的接口
 *
 * @author 王金绍
 * @date 2015年3月9日 上午10:37:16
 */
public interface Validation {

	public ValidateErrors validate(Object target);

	public void validate(Object target, ValidateErrors errors);
}
