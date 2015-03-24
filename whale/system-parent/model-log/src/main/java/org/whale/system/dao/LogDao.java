package org.whale.system.dao;

import org.springframework.stereotype.Repository;
import org.whale.system.base.BaseDao;
import org.whale.system.base.Page;
import org.whale.system.common.util.Strings;
import org.whale.system.common.util.TimeUtil;
import org.whale.system.domain.Log;

@Repository
public class LogDao extends BaseDao<Log, Long>{

	@Override
	public void queryPage(Page page) {
		StringBuilder strb = new StringBuilder();
		StringBuilder param = new StringBuilder();
		
		strb.append("SELECT t.id, t.opt, t.cnName, t.tableName, t.ip, t.createTime, t.userName, t.uri, t.callOrder, t.methodCostTime, t.costTime, t.rsType ")
			.append("FROM sys_log t ")
			.append("WHERE 1=1 ");
		
		if(Strings.isNotBlank(page.getParamStr("opt"))){
			param.append("AND t.opt = ? ");
			page.addArg(page.getParamStr("opt"));
		}
		
		if(Strings.isNotBlank(page.getParamStr("cnName"))){
			param.append("AND t.cnName like ? ");
			page.addArg("%"+page.getParamStr("cnName").trim()+"%");
		}
		
		if(Strings.isNotBlank(page.getParamStr("tableName"))){
			param.append("AND t.tableName like ? ");
			page.addArg("%"+page.getParamStr("tableName").trim()+"%");
		}
		
		if(Strings.isNotBlank(page.getParamStr("uri"))){
			param.append("AND t.uri like ? ");
			page.addArg("%"+page.getParamStr("uri").trim()+"%");
		}
		
		if(Strings.isNotBlank(page.getParamStr("userName"))){
			param.append("AND t.userName like ? ");
			page.addArg("%"+page.getParamStr("userName").trim()+"%");
		}
		
		if(Strings.isNotBlank(page.getParamStr("startTime"))){
			param.append("AND t.createTime >= ? ");
			page.addArg(TimeUtil.parseTime(page.getParamStr("startTime"), TimeUtil.COMMON_FORMAT).getTime());
		}
		
		if(Strings.isNotBlank(page.getParamStr("endTime"))){
			param.append("AND t.createTime<=? ");
			page.addArg(TimeUtil.parseTime(page.getParamStr("endTime"), TimeUtil.COMMON_FORMAT).getTime());
		}
	
		page.setCountSql("SELECT count(1) FROM sys_log t where 1=1 "+param.toString());
		page.setSql(strb.append(param.toString())+" ORDER BY t.createTime desc, t.callOrder asc");
		
		super.queryPage(page);
	}

	
}
