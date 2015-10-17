package org.whale.system.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.whale.system.common.util.Strings;
import org.whale.system.dao.FileInfoDao;
import org.whale.system.domain.FileInfo;
import org.whale.system.jdbc.IOrmDao;
import org.whale.system.spring.SpringContextHolder;

/**
 * 文件 管理
 *
 * @author 王金绍
 * 2014-9-27 17:08:39
 */
@Service
public class FileInfoService extends BaseService<FileInfo, Long> {

	@Autowired
	private FileInfoDao fileInfoDao;
	
    /**
	 * 按 原文件名 获取 文件
	 * @param realFileName 原文件名
	 * @return
	 */
    public FileInfo getByRealFileName(String realFileName) {
    	if(Strings.isBlank(realFileName)){
    		return null;
    	}
    	
    	return this.fileInfoDao.getByRealFileName(realFileName);
    }
    
    /**
     * 根据url地址获取文件对象
     * @param urlPath
     * @return
     */
    public FileInfo getByUrlPath(String urlPath) {
    	if(Strings.isBlank(urlPath)){
    		return null;
    	}
    	
    	return this.fileInfoDao.getByUrlPath(urlPath.trim());
    }
    
	
	@Override
	public IOrmDao<FileInfo, Long> getDao() {
		return fileInfoDao;
	}

	public static FileInfoService getThis(){
		return SpringContextHolder.getBean(FileInfoService.class);
	}
	
	
}