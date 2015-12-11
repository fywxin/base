package org.whale.inf.api;

import java.util.List;

import org.whale.inf.common.Result;

public interface UserInf {

	public User get(long id);
	
	public void sayHello(String userName, Dept dept);
	
	public Result<User> testList(List<Dept> depts);
	
	public Integer emptyBodyTest();
}
