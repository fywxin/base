package org.whale.system.server.youboo.dao;


import org.springframework.stereotype.Repository;
import org.whale.system.base.BaseDao;
import org.whale.system.server.youboo.domain.ClientVersion;

@Repository
public class ClientVersionDao extends BaseDao<ClientVersion, Long> {

	/**
	 * 获取应用
	 * @param appKey
	 * @param version
	 * @return
	 */
	public ClientVersion getByAppKeyAndVersion(String appKey, String version) {
		return this.get(this.cmd().eq("appKey", appKey).eq("clientVer", version));
	}
}
