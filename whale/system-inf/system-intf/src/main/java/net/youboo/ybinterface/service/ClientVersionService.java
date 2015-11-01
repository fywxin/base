package net.youboo.ybinterface.service;

import net.youboo.ybinterface.dao.ClientVersionDao;
import net.youboo.ybinterface.domain.ClientVersion;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.whale.system.cache.ICacheService;
import org.whale.system.common.util.Strings;
import org.whale.system.jdbc.IOrmDao;
import org.whale.system.service.BaseService;

@Service
public class ClientVersionService extends BaseService<ClientVersion, Long>{

	@Autowired
	private ClientVersionDao clientVersionDao;
	@Autowired(required=false)
	private ICacheService<ClientVersion> cacheService;
	
	public ClientVersion getByAppKeyAndVersion(String appKey, String version){
		if(Strings.isBlank(appKey) || Strings.isBlank(version)){
			return null;
		}
		ClientVersion clientVer = null;
		if(cacheService != null){
			clientVer = cacheService.get(ClientVersion.CACHE_KEY, appKey+":"+version);
		}
		if(clientVer == null){
			clientVer = this.clientVersionDao.getByAppKeyAndVersion(appKey, version);
			if(clientVer != null && cacheService != null){
				this.cacheService.put(ClientVersion.CACHE_KEY, appKey+":"+version, clientVer, 60 * 60 * 24 * 15);
			}
		}
		return clientVer;
	}

	@Override
	public IOrmDao<ClientVersion, Long> getDao() {
		return clientVersionDao;
	}
}
