package org.whale.system.log.router;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;
import org.whale.system.annotation.auth.Auth;
import org.whale.system.annotation.auth.AuthAdmin;
import org.whale.system.base.BaseRouter;
import org.whale.system.base.Page;
import org.whale.system.common.util.TimeUtil;
import org.whale.system.log.domain.LogInfo;
import org.whale.system.log.domain.LogQueryReq;
import org.whale.system.log.service.LogInfoService;


/**
 * 日志查询
 *
 * Created by 王金绍 on 2016/4/25.
 */
@Controller
@RequestMapping("/logInfo")
public class LogInfoRouter extends BaseRouter {

    @Autowired
    private LogInfoService logInfoService;


    @Auth(code = "log:view", name = "日志查看")
    @RequestMapping("/goList")
    public ModelAndView goList(){
        return new ModelAndView("system/log/log_list");
    }

    @Auth(code = "log:view", name = "日志查看")
    @ResponseBody
    @AuthAdmin
    @RequestMapping("/doList")
    public Page queryPage(LogQueryReq logQueryReq){
        Page page = this.newPage();
        this.logInfoService.queryPage(page, logQueryReq);
        return page;
    }

    @Auth(code = "log:view", name = "日志查看")
    @AuthAdmin
    @RequestMapping("/goView")
    public ModelAndView goView(Long id){
        LogInfo logInfo = this.logInfoService.get(id);

        return new ModelAndView("system/log/log_view")
                .addObject("item", logInfo)
                .addObject("createTime", TimeUtil.formatTime(logInfo.getCreateTime(), TimeUtil.COMMON_FORMAT));
    }
}
