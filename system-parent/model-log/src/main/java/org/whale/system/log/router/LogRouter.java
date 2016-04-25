package org.whale.system.log.router;

import org.whale.system.base.BaseRouter;
import org.whale.system.base.Page;
import org.whale.system.log.domain.LogQueryReq;

/**
 *
 * Created by 王金绍 on 2016/4/25.
 */
public class LogRouter extends BaseRouter {

    public Page queryPage(LogQueryReq logQueryReq){
        Page page = this.newPage();

        return page;
    }
}
