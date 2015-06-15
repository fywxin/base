package org.whale.inf.api;

import java.io.Serializable;

public class User implements Serializable{

	private static final long serialVersionUID = 1212174213L;

	private Long id;
	
	private String userName;
	
	private Integer age;
	
	private Dept dept;

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getUserName() {
		return userName;
	}

	public void setUserName(String userName) {
		this.userName = userName;
	}

	public Integer getAge() {
		return age;
	}

	public void setAge(Integer age) {
		this.age = age;
	}

	public Dept getDept() {
		return dept;
	}

	public void setDept(Dept dept) {
		this.dept = dept;
	}
	
	
}
