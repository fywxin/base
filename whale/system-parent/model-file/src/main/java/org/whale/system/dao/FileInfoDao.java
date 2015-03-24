package org.whale.system.dao;

import org.springframework.stereotype.Repository;
import org.whale.system.base.BaseDao;
import org.whale.system.domain.FileInfo;

@Repository
public class FileInfoDao extends BaseDao<FileInfo, Long> {

	/**
	 * 按 原文件名 获取 文件
	 * @param realFileName 原文件名
	 * @return
	 */
    public FileInfo getByRealFileName(String realFileName) {
    	StringBuilder strb = this.getSqlHead();
    	strb.append("and t.realFileName=?");
    	
    	return this.getObject(strb.toString(), realFileName);
    }
    
    /**
     * 根据url地址获取文件对象
     * @param urlPath
     * @return
     */
    public FileInfo getByUrlPath(String urlPath) {
    	StringBuilder strb = this.getSqlHead();
    	strb.append("and t.urlPath=?");
    	
    	return this.getObject(strb.toString(), urlPath);
    }
    
}