package org.whale.system.auth.domain;

import java.io.Serializable;
import java.util.List;
import java.util.Set;

public class UserAuth implements Serializable {

	private static final long serialVersionUID = -232121242583411L;

	private Long userId;
	
	private Set<String> authCodes;
	
	private Set<Long> leafMenuIds;
	
	private Set<Long> menuIds;
	

	public Long getUserId() {
		return userId;
	}

	public void setUserId(Long userId) {
		this.userId = userId;
	}

	public Set<Long> getLeafMenuIds() {
		return leafMenuIds;
	}

	public void setLeafMenuIds(Set<Long> leafMenuIds) {
		this.leafMenuIds = leafMenuIds;
	}
	
	public Set<Long> getMenuIds() {
		return menuIds;
	}

	public void setMenuIds(Set<Long> menuIds) {
		this.menuIds = menuIds;
	}

	public Set<String> getAuthCodes() {
		return authCodes;
	}

	public void setAuthCodes(Set<String> authCodes) {
		this.authCodes = authCodes;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		UserAuth other = (UserAuth) obj;
		if (userId == null) {
			if (other.userId != null)
				return false;
		} else if (!userId.equals(other.userId))
			return false;
		return true;
	}
	
}
