package org.whale.system.log.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.whale.system.jdbc.IOrmDao;
import org.whale.system.log.dao.LogInfoDao;
import org.whale.system.log.domain.LogInfo;
import org.whale.system.service.BaseService;

/**
 * Created by 王金绍 on 2016/4/25.
 */
@Service
public class LogInfoService extends BaseService<LogInfo, Long> {

    @Autowired
    private LogInfoDao logInfoDao;

    @Override
    public IOrmDao<LogInfo, Long> getDao() {
        return logInfoDao;
    }
}
