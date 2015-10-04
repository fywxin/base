package org.whale.system.dao;

import java.util.List;

import org.springframework.stereotype.Repository;
import org.whale.system.base.BaseDao;
import org.whale.system.base.Query;
import org.whale.system.domain.Dept;

@Repository
public class DeptDao extends BaseDao<Dept, Long> {

	/**
	 * 按 部门名称 获取 部门
	 * @param deptName 部门名称
	 * @return
	 */
    public Dept getByDeptName(String deptName) {
    	return this.get(this.cmd().eq("deptName", deptName));
    }
    /**
	 * 按 部门编码 获取 部门
	 * @param deptCode 部门编码
	 * @return
	 */
    public Dept getByDeptCode(String deptCode) {

    	return this.get(this.cmd().eq("deptCode", deptCode));
    }
    
    /**
     * 获取子部门列表
     * @param pid
     * @return
     */
    public List<Dept> getByPid(Long pid){
    	
    	return this.query(cmd().eq("pid", pid));
    }
	
    /**
     * 获取下一个排序
     * @param pid
     * @return
     */
    final String getNextOrder_SQL = "select max(t.orderNo) from sys_dept t where t.pid = ?";
    @SuppressWarnings("all")
    public Integer getNextOrder(Long pid){
    	int order = this.count(Query.newQuery(getNextOrder_SQL, pid));
    	return order+1;
    }
}