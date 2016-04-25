package org.whale.system.log.domain;

import org.whale.system.base.Page;

/**
 * Created by 王金绍 on 2016/4/25.
 */
public class LogQueryReq extends Page {

    private String module;

    private String info;

    private String userName;

    private String ip;

    private Long startTime;

    private Long endTime;

    private Integer rsType;

    public String getModule() {
        return module;
    }

    public void setModule(String module) {
        this.module = module;
    }

    public String getInfo() {
        return info;
    }

    public void setInfo(String info) {
        this.info = info;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getIp() {
        return ip;
    }

    public void setIp(String ip) {
        this.ip = ip;
    }

    public Long getStartTime() {
        return startTime;
    }

    public void setStartTime(Long startTime) {
        this.startTime = startTime;
    }

    public Long getEndTime() {
        return endTime;
    }

    public void setEndTime(Long endTime) {
        this.endTime = endTime;
    }

    public Integer getRsType() {
        return rsType;
    }

    public void setRsType(Integer rsType) {
        this.rsType = rsType;
    }
}
