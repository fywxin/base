package org.whale.inf.api;

public interface UserInf {

	public User get(long id);
	
	public void sayHello(String userName, Dept dept);
}
