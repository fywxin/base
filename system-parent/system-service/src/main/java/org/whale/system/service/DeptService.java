package org.whale.system.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.whale.system.common.util.Strings;
import org.whale.system.dao.DeptDao;
import org.whale.system.domain.Dept;
import org.whale.system.jdbc.IOrmDao;

/**
 * 部门 管理
 *
 * @author wjs
 * 2014-9-16 15:20:11
 */
@Service
public class DeptService extends BaseService<Dept, Long> {

	@Autowired
	private DeptDao deptDao;
	
	
    /**
	 * 按 部门编码 获取 部门
	 * @param deptCode 部门编码
	 * @return
	 */
    public Dept getByDeptCode(String deptCode) {
    	if(Strings.isBlank(deptCode)){
    		return null;
    	}
    	
    	return this.deptDao.getByDeptCode(deptCode);
    }
    
    /**
     * 获取子部门列表
     * @param pid
     * @return
     */
    public List<Dept> getByPid(Long pid){
    	if(pid == null)
    		pid = 0L;
    	return this.deptDao.getByPid(pid);
    }
    
    /**
     * 获取下一个排序
     * @param pid
     * @return
     */
    public Integer getNextOrder(Long pid){
    	if(pid == null)
    		pid = 0L;
    	return this.deptDao.getNextOrder(pid);
    }
	
	@Override
	public IOrmDao<Dept, Long> getDao() {
		return deptDao;
	}

}