package org.whale.inf.api;

import java.io.Serializable;

public class Dept implements Serializable{

	private static final long serialVersionUID = 42L;

	private Long id;
	
	private String deptName;
	
	private boolean isRoot;

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getDeptName() {
		return deptName;
	}

	public void setDeptName(String deptName) {
		this.deptName = deptName;
	}

	public boolean isRoot() {
		return isRoot;
	}

	public void setRoot(boolean isRoot) {
		this.isRoot = isRoot;
	}
	
	
}
