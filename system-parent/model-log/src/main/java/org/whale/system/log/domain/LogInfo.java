package org.whale.system.log.domain;

import org.whale.system.annotation.jdbc.Column;
import org.whale.system.annotation.jdbc.Id;
import org.whale.system.annotation.jdbc.Table;
import org.whale.system.base.BaseEntry;
import org.whale.system.common.exception.SysException;
import org.whale.system.common.util.Strings;

import java.util.Arrays;
import java.util.List;

/**
 * Created by 王金绍 on 2016/4/25.
 */
@Table(value = "sys_log_info", cnName = "日志")
public class LogInfo extends BaseEntry {

    public static final Integer RS_SUCCESS = 0;

    public static final Integer RS_SysException = 1;
    public static final Integer RS_OrmException = 2;
    public static final Integer RS_RunTimeException = 3;
    public static final Integer RS_BusinessException = 4;
    public static final Integer RS_OTHER = 10;

    @Id
    @Column(cnName = "id")
    private Long id;

    @Column(cnName = "调用类")
    private String clazz;

    @Column(cnName = "调用方法")
    private String method;

    @Column(cnName = "中文模块名")
    private String module;

    @Column(cnName = "日志描述")
    private String info;

    @Column(cnName = "方法参数，默认异常时，记录请求参数")
    private String params;

    @Column(cnName = "来源ip")
    private String ip;

    @Column(cnName = "当前用户")
    private String userName;

    @Column(cnName = "方法耗时")
    private Integer costTime;

    @Column(cnName = "创建时间")
    private Long createTime;

    @Column(cnName = "结果类型")
    private Integer rs;

    private List<String> splitDescItem = null;

    public LogInfo cloneThis(){
        LogInfo logInfo = new LogInfo();
        logInfo.setClazz(this.getClazz());
        logInfo.setMethod(this.getMethod());
        logInfo.setModule(this.getModule());
        logInfo.setInfo(this.getInfo());
        logInfo.setCreateTime(System.currentTimeMillis());
        logInfo.setSplitDescItem(this.getSplitDescItem());

        return logInfo;
    }

    public void splitDesc(){
        if (Strings.isBlank(info)){
            throw new SysException("日志描述内容不能为空");
        }
        String[] descItems = info.split("\\{\\}");
        if (descItems.length == 1){
            descItems = info.split("\\%s");
        }
        this.splitDescItem = Arrays.asList(descItems);
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getClazz() {
        return clazz;
    }

    public void setClazz(String clazz) {
        this.clazz = clazz;
    }

    public String getMethod() {
        return method;
    }

    public void setMethod(String method) {
        this.method = method;
    }

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

    public String getParams() {
        return params;
    }

    public void setParams(String params) {
        this.params = params;
    }

    public String getIp() {
        return ip;
    }

    public void setIp(String ip) {
        this.ip = ip;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public Integer getCostTime() {
        return costTime;
    }

    public void setCostTime(Integer costTime) {
        this.costTime = costTime;
    }

    public Long getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Long createTime) {
        this.createTime = createTime;
    }

    public Integer getRs() {
        return rs;
    }

    public void setRs(Integer rs) {
        this.rs = rs;
    }

    public List<String> getSplitDescItem() {
        return splitDescItem;
    }

    public void setSplitDescItem(List<String> splitDescItem) {
        this.splitDescItem = splitDescItem;
    }

    @Override
    public String toString() {
        return "LogInfo{" +
                "method='" + method + '\'' +
                ", clazz='" + clazz + '\'' +
                ", info='" + info + '\'' +
                ", module='" + module + '\'' +
                '}';
    }


    public static void main(String[] args){
        String a="删除部门: {} ";
        String[] as = a.split("\\{\\}");
        for (int i=0; i<as.length; i++){
            System.out.println(as[i]);
        }
        System.out.println("2323");
    }
}
