package org.whale.system.service;

import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.OrderComparator;
import org.springframework.stereotype.Service;
import org.whale.system.base.BaseCrudEvent;
import org.whale.system.base.Page;
import org.whale.system.common.constant.SysConstant;
import org.whale.system.common.exception.SysException;
import org.whale.system.common.util.LangUtil;
import org.whale.system.common.util.Strings;
import org.whale.system.dao.UserDao;
import org.whale.system.dao.UserRoleDao;
import org.whale.system.domain.User;
import org.whale.system.domain.UserRole;
import org.whale.system.jdbc.IOrmDao;
import org.whale.system.service.event.UserEvent;

@Service
public class UserService extends BaseService<User, Long> {

	@Autowired
	private UserDao userDao;
	@Autowired
	private UserRoleDao userRoleDao;
	@Autowired(required = false)
	private List<UserEvent> list;
	
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
		user.setCreateTime(new Date());
		user.setStatus(SysConstant.STATUS_NORMAL);
		try {
			user.setPassword(getEncryptedPwd(user.getPassword()));
		} catch (Exception e) {
			throw new SysException("设置密码出现异常");
		}
		LangUtil.trim(user);
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
		LangUtil.trim(user);
		this.fireEvent(user, BaseCrudEvent.BEFORE_UPDATE);
		this.userDao.update(user);
		this.fireEvent(user, BaseCrudEvent.AFTER_UPDATE);
	}
	
	public void updateState(Long userId, int status) {
		if(null == userId){
			return ;
		}
		User user = this.userDao.get(userId);
		if(user == null)
			return ;
		
		LangUtil.trim(user);
		this.fireEvent(user, UserEvent.BEFORE_SETSTATUS, status);
		user.setStatus(status);
		this.userDao.update(user);
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
		LangUtil.trim(user);
		this.userDao.update(user);
	}
	
	public void saveUserRole(Long userId, List<Long> roleIds){
		if(userId == null)
			throw new SysException("userId == null");
		
		this.userRoleDao.deleteByUserId(userId);
		if(roleIds != null && roleIds.size() > 0){
			UserRole userRole = null;
			for(Long roleId : roleIds){
				//TODO check duplicate
				userRole = new UserRole();
				userRole.setRoleId(roleId);
				userRole.setUserId(userId);
				this.userRoleDao.save(userRole);
			}
		}
	}
	

	@Override
	public void queryPage(Page page) {
		StringBuilder strb = new StringBuilder();
		strb.append(" FROM sys_user t WHERE t.isAdmin=0 ");
		if(Strings.isNotBlank(page.getParamStr("userName"))){
			strb.append(" AND t.userName like ?");
			page.addArg("%"+page.getParamStr("userName")+"%");
		}
		if(Strings.isNotBlank(page.getParamStr("realName"))){
			strb.append(" AND t.realName like ?");
			page.addArg("%"+page.getParamStr("realName")+"%");
		}
		if(page.getParamInteger("deptId") != null){
			strb.append(" AND t.deptId=?");
			page.addArg(page.getParamInteger("deptId"));
		}
		
		page.setCountSql("SELECT count(1) "+strb.toString());
		page.setSql("SELECT t.*, (select d.deptName from sys_dept d where d.id = t.deptId) as deptName "+strb.toString()+this.userDao._getOrmTable().getSqlOrderSuffix());
	
		this.userDao.queryPage(page);
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
		return Strings.encrypt(passwd);
	}
	
	public boolean validPasswd(String passwd, String dbPasswd){
		return Strings.decrypt(passwd, dbPasswd);
	}
	
	public List<Map<String, Object>> queryDeptTree(){
		return this.userDao.queryDeptTree();
	}
	
}
