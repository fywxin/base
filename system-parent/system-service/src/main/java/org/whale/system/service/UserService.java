package org.whale.system.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.OrderComparator;
import org.springframework.stereotype.Service;
import org.whale.system.annotation.log.LogHelper;
import org.whale.system.base.BaseCrudEvent;
import org.whale.system.common.constant.SysConstant;
import org.whale.system.common.encrypt.SaltEncrypt;
import org.whale.system.common.exception.SysException;
import org.whale.system.common.util.ListUtil;
import org.whale.system.common.util.Strings;
import org.whale.system.dao.RoleDao;
import org.whale.system.dao.UserDao;
import org.whale.system.dao.UserRoleDao;
import org.whale.system.domain.Role;
import org.whale.system.domain.User;
import org.whale.system.domain.UserRole;
import org.whale.system.jdbc.IOrmDao;
import org.whale.system.service.event.UserEvent;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;

@Service
public class UserService extends BaseService<User, Long> {

	@Autowired
	private UserDao userDao;
	@Autowired
	private UserRoleDao userRoleDao;
	@Autowired(required = false)
	private List<UserEvent> list;
	@Autowired
	private RoleDao roleDao;
	
	private boolean sort = false;
	
	public List<User> getByDeptId(Long deptId){
		if(deptId == null || deptId < 1)
			return null;
		return this.userDao.getByDeptId(deptId);
	}
	
	public User getByUserName(String userName){
		if(userName == null || userName.trim().equals("")){
			return null;
		}
		return this.userDao.getByUserName(userName);
	}
	
	@Override
	public void save(User user) {
		user.setCreateTime(System.currentTimeMillis());
		user.setStatus(SysConstant.STATUS_NORMAL);
		try {
			user.setPassword(getEncryptedPwd(user.getPassword()));
		} catch (Exception e) {
			throw new SysException("设置密码出现异常");
		}
		this.fireEvent(user, BaseCrudEvent.BEFORE_SAVE);
		this.userDao.save(user);
		this.fireEvent(user, BaseCrudEvent.AFTER_SAVE);
	}

	@Override
	public void update(User user) {
		User oldUser = this.get(user.getUserId());
		user.setUserName(oldUser.getUserName());
		user.setPassword(oldUser.getPassword());
		
		//user.setCreateTime(oldUser.getCreateTime());
		//user.setCreateUserId(oldUser.getCreateUserId());
		
		if(user.getLastLoginTime() == null)
			user.setLastLoginTime(oldUser.getLastLoginTime());
		if(Strings.isBlank(user.getLoginIp()))
			user.setLoginIp(oldUser.getLoginIp());
		if(user.getStatus() == null)
			user.setStatus(oldUser.getStatus());
		if(user.getUserType() == null)
			user.setUserType(oldUser.getUserType());
		this.fireEvent(user, BaseCrudEvent.BEFORE_UPDATE);
		this.userDao.update(user);
		this.fireEvent(user, BaseCrudEvent.AFTER_UPDATE);
	}
	
	public void updateState(User user, int status) {
		this.fireEvent(user, UserEvent.BEFORE_SETSTATUS, status);
		User userUpdate = new User();
		userUpdate.setStatus(status);
		userUpdate.setUserId(user.getUserId());
		this.userDao.updateNotNull(userUpdate);
		this.fireEvent(user, UserEvent.AFTER_SETSTATUS);
	}
	
	@Override
	public void delete(Long id) {
		User user = this.get(id);
		this.fireEvent(user, BaseCrudEvent.BEFORE_DEL);
		super.delete(id);
		this.fireEvent(user, BaseCrudEvent.AFTER_DEL);
	}

	public void updatePassword(Long userId, String newPassword){
		User user = this.userDao.get(userId);
		if(user == null)
			throw new SysException("用户 userId ["+userId+"]不存在");
		try {
			user.setPassword(getEncryptedPwd(newPassword));
		} catch (Exception e) {
			throw new SysException("设置密码出现异常");
		}
		this.userDao.update(user);
	}
	
	public void transSaveUserRole(Long userId, List<Long> roleIds){
		User user = this.get(userId);
		if(user == null)
			throw new SysException("userId == null");

		List<String> roleNames = new ArrayList<String>();
		this.userRoleDao.deleteByUserId(userId);
		if(roleIds != null && roleIds.size() > 0){
			UserRole userRole = null;
			Role role = null;
			for(Long roleId : ListUtil.toHashSet(roleIds)){
				role = this.roleDao.get(roleId);
				if (role == null){
					continue;
				}

				userRole = new UserRole();
				userRole.setRoleId(roleId);
				userRole.setUserId(userId);
				this.userRoleDao.save(userRole);
				roleNames.add(role.getRoleName());
			}
		}

		LogHelper.addPlaceHolder(user.getUserName(), ListUtil.join(roleNames));
	}
	
	@Override
	public IOrmDao<User, Long> getDao() {
		return userDao;
	}
	
	private void fireEvent(User user, int EventType){
		this.fireEvent(user, EventType, null);
	}
	
	private void fireEvent(User user, int EventType, Object addon){
		if(list == null || list.size() == 0)
			return ;
		if(!sort){
			Collections.sort(list, new OrderComparator());
			sort = true;
		}
		
		for(UserEvent userEvent : list){
			switch (EventType) {
			case BaseCrudEvent.BEFORE_SAVE:
				userEvent.onBeforeCreate(user);
				break;
			case BaseCrudEvent.AFTER_SAVE:
				userEvent.onAfterCreate(user);
				break;
			case BaseCrudEvent.BEFORE_UPDATE:
				userEvent.onBeforeUpdate(user);
				break;
			case BaseCrudEvent.AFTER_UPDATE:
				userEvent.onAfterUpdate(user);
				break;
			case BaseCrudEvent.BEFORE_DEL:
				userEvent.onBeforeDelete(user);
				break;
			case BaseCrudEvent.AFTER_DEL:
				userEvent.onAfterDelete(user);
				break;
			case UserEvent.BEFORE_SETSTATUS:
				userEvent.onBeforeSetStatus(user, (Integer)addon);
				break;
			case UserEvent.AFTER_SETSTATUS:
				userEvent.onAfterSetStatus(user);
				break;	
			default:
				break;
			}
		}
	}
	
	public String getEncryptedPwd(String passwd){
		return SaltEncrypt.encrypt(passwd);
	}
	
	public boolean validPasswd(String passwd, String dbPasswd){
		return SaltEncrypt.decrypt(passwd, dbPasswd);
	}
	
	public List<Map<String, Object>> queryDeptTree(){
		return this.userDao.queryDeptTree();
	}
	
}
