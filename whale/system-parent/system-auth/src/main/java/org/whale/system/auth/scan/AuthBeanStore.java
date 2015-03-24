package org.whale.system.auth.scan;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.whale.system.auth.domain.AuthBean;
import org.whale.system.common.util.Strings;
import org.whale.system.dao.AuthDao;
import org.whale.system.domain.Auth;

/**
 * 
 * 
 * @author 王金绍
 * @date 2014年12月23日 下午10:56:48
 */
public class AuthBeanStore {

	@Autowired
	private AuthDao authDao;
	
	public void doStore(){
		AuthAnnotationScaner.authScan();
		
		List<Auth> auths = this.authDao.queryAll();
		Map<String, Auth> dbAuthMap = new HashMap<String, Auth>();
		if(auths != null){
			for(Auth auth : auths){
				dbAuthMap.put(auth.getAuthCode(), auth);
			}
		}
		
		Auth auth = null;
		for(AuthBean authBean : AuthBean.AUTH_HOLDER.values()){
			auth = dbAuthMap.get(authBean.getAuthCode());
			if(auth == null){
				auth = new Auth();
				auth.setAuthCode(authBean.getAuthCode());
				auth.setAuthName(authBean.getAuthName());
				
				this.authDao.save(auth);
			}else{
				if(Strings.isNotBlank(authBean.getAuthName())){
					auth.setAuthName(authBean.getAuthName());
				}
				
				this.authDao.update(auth);
				dbAuthMap.remove(authBean.getAuthCode());
			}
		}
		
		if(dbAuthMap.size() > 0){
			for(Auth delAuth : dbAuthMap.values()){
				this.authDao.delete(delAuth.getAuthId());
			}
		}
	}
}
