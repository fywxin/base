package org.whale.system.jdbc.orm.entry;


public class OrmValidate {

	//是否必填
	private boolean required;
	//是否帐号
	private boolean account;
	//是否手机
	private boolean mobile;
	//邮箱
	private boolean email;
	//qq
	private boolean qq;
	//中文
	private boolean chinese;
	//邮编
	private boolean post;
	//ipv4 或 ipV6
	private boolean ip;
	//地址
	private boolean url;
	//是否与某字段值重复
	private String repeat;
	//el表示式
	private String el;
	//正则表达式
	private String regex;
	//自定义校验
	private String custom;
	//校验错误统一提示信息
	private String errorMsg;
	//字符串最大、最小长度验证规则
	private int[] strLen;
	//值范围
	private double[] limit;
	//值枚举范围
	private String[] enums;
	//所属字段
	private OrmColumn ormColumn;
	
	
	
	public boolean isRequired() {
		return required;
	}
	public void setRequired(boolean required) {
		this.required = required;
	}
	public boolean isAccount() {
		return account;
	}
	public void setAccount(boolean account) {
		this.account = account;
	}
	public boolean isMobile() {
		return mobile;
	}
	public void setMobile(boolean mobile) {
		this.mobile = mobile;
	}
	public boolean isEmail() {
		return email;
	}
	public void setEmail(boolean email) {
		this.email = email;
	}
	public boolean isQq() {
		return qq;
	}
	public void setQq(boolean qq) {
		this.qq = qq;
	}
	public boolean isChinese() {
		return chinese;
	}
	public void setChinese(boolean chinese) {
		this.chinese = chinese;
	}
	public boolean isPost() {
		return post;
	}
	public void setPost(boolean post) {
		this.post = post;
	}
	public boolean isUrl() {
		return url;
	}
	public void setUrl(boolean url) {
		this.url = url;
	}
	public String getRepeat() {
		return repeat;
	}
	public void setRepeat(String repeat) {
		this.repeat = repeat;
	}
	public String getEl() {
		return el;
	}
	public void setEl(String el) {
		this.el = el;
	}
	public String getRegex() {
		return regex;
	}
	public void setRegex(String regex) {
		this.regex = regex;
	}
	public String getCustom() {
		return custom;
	}
	public void setCustom(String custom) {
		this.custom = custom;
	}
	public String getErrorMsg() {
		return errorMsg;
	}
	public void setErrorMsg(String errorMsg) {
		this.errorMsg = errorMsg;
	}
	public int[] getStrLen() {
		return strLen;
	}
	public void setStrLen(int[] strLen) {
		this.strLen = strLen;
	}
	public double[] getLimit() {
		return limit;
	}
	public void setLimit(double[] limit) {
		this.limit = limit;
	}
	public String[] getEnums() {
		return enums;
	}
	public void setEnums(String[] enums) {
		this.enums = enums;
	}
	public OrmColumn getOrmColumn() {
		return ormColumn;
	}
	public void setOrmColumn(OrmColumn ormColumn) {
		this.ormColumn = ormColumn;
	}
	public boolean isIp() {
		return ip;
	}
	public void setIp(boolean ip) {
		this.ip = ip;
	}
	
}
