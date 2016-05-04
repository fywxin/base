package org.whale.system.auth.cache;

import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.whale.system.auth.domain.UserAuth;
import org.whale.system.cache.ICacheService;
import org.whale.system.common.constant.OrderNumConstant;
import org.whale.system.common.exception.RemoteCacheException;
import org.whale.system.common.util.Bootable;
import org.whale.system.common.util.PropertiesUtil;
import org.whale.system.dao.AuthDao;
import org.whale.system.dao.MenuDao;
import org.whale.system.dao.UserDao;
import org.whale.system.domain.Auth;
import org.whale.system.domain.Menu;
import org.whale.system.domain.User;
import org.whale.system.spring.SpringContextHolder;

/**
 * 用户权限数据
 *
 * @author wjs
 * 2014年9月19日-下午9:14:14
 */
public class UserAuthCacheService implements Bootable{
	
	private static final Logger logger = LoggerFactory.getLogger(UserAuthCacheService.class);
	
	public static final String CACHE_PREX = "c_Auth";
	
	@Autowired
	private UserDao userDao;
	@Autowired
	private AuthDao authDao;
	@Autowired
	private MenuDao menuDao;

	private ICacheService<UserAuth> cacheService;
	
	public void initUserAuths(){
		logger.info("AUTH: 权限初始化开始....");
		
		if(cacheService == null){
			logger.warn("缓存插件未配置，将已无缓存模式运行");
			return ;
		}
		
		List<User> users = this.userDao.queryAll();
		if(users != null && users.size() > 0){
			for(User user : users){
				this.putUserAuth(user.getUserId());
			}
		}else{
			logger.warn("AUTH: 没有用户数据");
		}
		logger.info("AUTH: 权限初始化完成!");
	}
	
	public void putUserAuth(Long userId){
		UserAuth userAuth = this.getUserAuthFromDb(userId);
		if(userAuth != null){
			try{
				if(cacheService == null){
					logger.warn("缓存被禁用，采用无缓存模式运行！");
				}else{
					this.cacheService.put(CACHE_PREX, userId.toString(), userAuth, PropertiesUtil.getValueInt("cache.auth.expTime", 2592000));
					if(logger.isDebugEnabled()){
						logger.debug("AUTH: 获取用户["+userId+"]权限数据："+userAuth);
					}
					
				}
			}catch(RemoteCacheException e){
				logger.error("CACHE: 远程缓存不可用, 设值失败！userId="+userId, e);
			}
		}else{
			logger.warn("AUTH: 数据库中查看不到userId="+userId);
		}
	}
	
	public void clearUserAuths(){
		if(logger.isDebugEnabled()){
			logger.debug("AUTH: 权限初清空开始....");
		}
		
		List<User> users = this.userDao.queryAll();
		if(users != null && users.size() > 0){
			for(User user : users){
				this.delUserAuth(user.getUserId());
				if(logger.isDebugEnabled()){
					logger.debug("AUTH: 用户["+user.getUserId()+"]权限清空...");
				}
			}
		}
		if(logger.isDebugEnabled()){
			logger.debug("AUTH: 权限清空完成!");
		}
		
	}
	
	public void delUserAuth(Long userId){
		try{
			if(cacheService == null){
				logger.warn("缓存被禁用，采用无缓存模式运行！");
			}else{
				this.cacheService.del(CACHE_PREX, userId.toString());
				if(logger.isDebugEnabled()){
					logger.debug("AUTH: 用户["+userId+"]权限删除完成！");
				}
				
			}
		}catch(RemoteCacheException e){
			logger.error("CACHE: 远程缓存不可用！", e);
		}
	}
	
	public UserAuth getUserAuth(Long userId){
		if(userId == null)
			return null;
		UserAuth userAuth = null;
		boolean cacheDown = false;
		try{
			if(cacheService == null){
				logger.warn("缓存被禁用，采用无缓存模式运行！");
			}else{
				userAuth = (UserAuth)this.cacheService.get(CACHE_PREX, userId.toString());
			}
		}catch(RemoteCacheException e){
			logger.error("CACHE: 远程缓存不可用, 取值失败！userId="+userId, e);
			cacheDown = true;
		}
		if(userAuth == null){
			if(logger.isDebugEnabled()){
				logger.debug("CACHE: 缓存不存在用户["+userId+"]权限数据，开始从数据库查找...");
			}
			
			userAuth = this.getUserAuthFromDb(userId);
			if(logger.isDebugEnabled()){
				logger.debug("CACHE: 完成用户["+userId+"]权限数据加载！");
			}
			if(!cacheDown && userAuth != null && this.cacheService != null){
				try{
					this.cacheService.put(CACHE_PREX, userId.toString(), userAuth, PropertiesUtil.getValueInt("cache.auth.expTime", 2592000));
				}catch(RemoteCacheException e){
					logger.error("CACHE: 远程缓存不可用, 设值失败！userId="+userId, e);
					cacheDown = true;
				}
			}
		}
		return userAuth;
	}
	
	public UserAuth getUserAuthFromDb(Long userId){
		UserAuth userAuth = new UserAuth();
		userAuth.setUserId(userId);
		
		List<Auth> auths = this.authDao.queryByUserId(userId);
		if(auths != null){
			Set<String> authCodes = new HashSet<String>(auths.size() * 2);
			Set<Long> leafMenuIds = new HashSet<Long>(auths.size());
			Set<Long> menuIds = new HashSet<Long>(auths.size() * 2);
			
			Menu menu = null;
			for(Auth auth : auths){
				authCodes.add(auth.getAuthCode());
				leafMenuIds.add(auth.getMenuId());
				
				if(!menuIds.contains(auth.getMenuId())){
					menuIds.add(auth.getMenuId());
					menu = this.menuDao.get(auth.getMenuId());
					while(menu.getParentId() != null && menu.getParentId() != 0L){
						menuIds.add(menu.getParentId());
						menu = this.menuDao.get(menu.getParentId());
					}
				}
			}
			userAuth.setLeafMenuIds(leafMenuIds);
			userAuth.setAuthCodes(authCodes);
			userAuth.setMenuIds(menuIds);
		}
				
		return userAuth;
	}

	@Override
	public Object init(Map<String, Object> context) {
		this.initUserAuths();
		return null;
	}

	@Override
	public boolean access() {
		return true;
	}
	
	public static UserAuthCacheService getThis(){
		return SpringContextHolder.getBean(UserAuthCacheService.class);
	}

	@Override
	public int getOrder() {
		return OrderNumConstant.AUTH_CACHE_INIT_ORDER;
	}

	public void setCacheService(ICacheService<UserAuth> cacheService) {
		this.cacheService = cacheService;
	}
}
