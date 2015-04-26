package org.whale.system.domain;

import org.whale.system.base.BaseEntry;
import org.whale.system.jdbc.annotation.Column;
import org.whale.system.jdbc.annotation.Id;
import org.whale.system.jdbc.annotation.Table;

//@MixOrm(cacheName="log", cacheTime=1000)
@Table(value="sys_log", cnName="日志")
public class Log extends BaseEntry{
	
	private static final long serialVersionUID = 1L;
	
	public static final Integer RS_NORMAL = 1;
	public static final Integer RS_SysException = 2;
	public static final Integer RS_OrmException = 3;
	public static final Integer RS_RunTimeException = 4;
	public static final Integer RS_BusinessException = 5;
	public static final Integer RS_OTHER = 0;

	@Id
	@Column(cnName="id")
	private Long id;
	@Column(cnName="操作方法",width=64)
    private String opt;
	@Column(cnName="对象名称",width=128)
	private String cnName;
	@Column(cnName="表名称",width=64)
	private String tableName;
	@Column(cnName="请求路径",width=128)
	private String uri;
	
	@Column(cnName="执行sql语句",width=512)
	private String sqlStr;
	
	@Column(cnName="数据", width=2048)
    private String datas;
	@Column(cnName="ip地址", width=15)
    private String ip;
	@Column(cnName="创建时间", width=15)
    private Long createTime;
	@Column(cnName="操作人")
    private String userName;
	@Column(cnName="调用链顺序")
	private Integer callOrder;
	@Column(cnName="方法耗时", width=6)
	private Integer methodCostTime;
	@Column(cnName="调用耗时", width=6)
	private Integer costTime;
	//1 正常， 2. SysException 3.OrmException 4.RunTimeException 0.other
	@Column(cnName="结果类型", width=2)
	private Integer rsType;
	//参数
	private Object arg;
	
	
	public Object getArg() {
		return arg;
	}
	public void setArg(Object arg) {
		this.arg = arg;
	}
	public Long getId() {
		return id;
	}
	public void setId(Long id) {
		this.id = id;
	}
	public String getOpt() {
		return opt;
	}
	public void setOpt(String opt) {
		this.opt = opt;
	}
	public String getDatas() {
		return datas;
	}
	public void setDatas(String datas) {
		this.datas = datas;
	}
	public String getIp() {
		return ip;
	}
	public void setIp(String ip) {
		this.ip = ip;
	}
	public Long getCreateTime() {
		return createTime;
	}
	public void setCreateTime(Long createTime) {
		this.createTime = createTime;
	}
	public String getUserName() {
		return userName;
	}
	public void setUserName(String userName) {
		this.userName = userName;
	}
	public String getCnName() {
		return cnName;
	}
	public void setCnName(String cnName) {
		this.cnName = cnName;
	}
	public String getTableName() {
		return tableName;
	}
	public void setTableName(String tableName) {
		this.tableName = tableName;
	}
	public String getUri() {
		return uri;
	}
	public void setUri(String uri) {
		this.uri = uri;
	}

	public Integer getCallOrder() {
		return callOrder;
	}
	public void setCallOrder(Integer callOrder) {
		this.callOrder = callOrder;
	}
	public Integer getMethodCostTime() {
		return methodCostTime;
	}
	public void setMethodCostTime(Integer methodCostTime) {
		this.methodCostTime = methodCostTime;
	}
	public Integer getCostTime() {
		return costTime;
	}
	public void setCostTime(Integer costTime) {
		this.costTime = costTime;
	}
	public Integer getRsType() {
		return rsType;
	}
	public void setRsType(Integer rsType) {
		this.rsType = rsType;
	}
	public String getSqlStr() {
		return sqlStr;
	}
	public void setSqlStr(String sqlStr) {
		this.sqlStr = sqlStr;
	}

	
}