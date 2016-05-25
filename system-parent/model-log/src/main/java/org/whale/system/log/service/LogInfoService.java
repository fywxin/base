package org.whale.system.log.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.whale.system.base.Q;
import org.whale.system.base.Page;
import org.whale.system.common.util.Strings;
import org.whale.system.common.util.TimeUtil;
import org.whale.system.jdbc.IOrmDao;
import org.whale.system.log.dao.LogInfoDao;
import org.whale.system.log.domain.LogInfo;
import org.whale.system.log.domain.LogQueryReq;
import org.whale.system.service.BaseService;

/**
 * Created by 王金绍 on 2016/4/25.
 */
@Service
public class LogInfoService extends BaseService<LogInfo, Long> {

    @Autowired
    private LogInfoDao logInfoDao;

    public void queryPage(Page page, LogQueryReq logQueryReq){

        Q q = page.newQ(LogInfo.class)
                .eq("module", logQueryReq.getModule())
                .eq("method", logQueryReq.getMethod())
                .eq("clazz", logQueryReq.getClazz())
                .eq("rs", logQueryReq.getRs())
                .like("info", logQueryReq.getInfo())
                .like("ip", logQueryReq.getIp())
                .like("userName", logQueryReq.getUserName())
                .and("costTime", ">=", logQueryReq.getCostTime())
                .and("createTime", ">=", Strings.isNotBlank(logQueryReq.getStartTime()) ? TimeUtil.parseTime(logQueryReq.getStartTime()).getTime() : null)
                .and("createTime", "<=", Strings.isNotBlank(logQueryReq.getEndTime()) ? TimeUtil.parseTime(logQueryReq.getEndTime()).getTime() : null)
                .desc("createTime");

        this.logInfoDao.queryPage(page);
    }

    @Override
    public IOrmDao<LogInfo, Long> getDao() {
        return logInfoDao;
    }
}
