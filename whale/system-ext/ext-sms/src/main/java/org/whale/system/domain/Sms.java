package org.whale.system.domain;

import java.util.Date;

import org.whale.system.annotation.jdbc.Column;
import org.whale.system.annotation.jdbc.Id;
import org.whale.system.annotation.jdbc.Table;
import org.whale.system.annotation.jdbc.Validate;
import org.whale.system.base.BaseEntry;
import org.whale.system.domain.Sms;

/**
 * 短信
 *
 * @author 王金绍
 * @Date 2014-10-16
 */
@Table(value="sys_sms", cnName="短信")
public class Sms extends BaseEntry {

	private static final long serialVersionUID = -1413426704622l;
	
	/**待发送 */
	public static final Integer STATUS_SEND_WAITing = 1;
	/**发送成功*/
	public static final Integer STATUS_SEND_SUCCESS = 2;
	/**发送失败 */
	public static final Integer STATUS_SEND_FAIL = 3;
	/**忽略发送 */
	public static final Integer STATUS_SEND_IGNORE = 4;
	
	/**短信内容最大长度*/
	public static final int MAX_CONTENT_LENGTH = 70;
	
	public Sms(){}
	
	public Sms(Integer smsType, String content, String toPhones){
		this.smsType = smsType;
		this.content = content;
		this.toPhones = toPhones;
	}
	
	@Id
	@Column(name="id", cnName="id")
	private Long id;
	
	@Validate(required=true)
  	@Column(name="content", cnName="短信内容")
	private String content;
	
	@Validate(required=true)
  	@Column(name="smsType", cnName="短信类型", updateable=false)
	private Integer smsType;
	
	@Validate(required=true)
  	@Column(name="toPhones", cnName="接收号码")
	private String toPhones;
	
  	@Column(name="sendTime", cnName="定时发送时间")
	private Date sendTime;
  	
  	@Column(name="encode", cnName="自定义扩展号")
	private String encode;
  	
  	@Column(name="resStatus", cnName="返回状态")
	private String resStatus;
  	
  	@Column(name="resMsg", cnName="返回信息")
	private String resMsg;
  	
  	@Column(name="sid", cnName="sid")
	private String sid;
  	
  	@Column(name="overLengthIgnore", cnName="内容超70忽略")
	private Boolean overLengthIgnore = true;
  	
  	@Column(name="sendRealTime", cnName="是否实时发送")
	private Boolean sendRealTime = false;
  	
  	@Column(name="retryTime", cnName="重发次数")
	private Integer retryTime = 3;
  	
  	@Column(name="curRetryTime", cnName="当前发送次数")
	private Integer curRetryTime = 0;
  	
  	@Column(name="createTime", cnName="创建时间")
	private Date createTime;
  	
  	@Column(name="recTime", cnName="返回时间")
	private Date recTime;
  	
  	@Column(name="status", cnName="消息状态 1.待发送， 2. 发送成功， 3. 发送失败 ")
	private Integer status = 1;
  	
  	@Column(name="bisExpInfo", cnName="异常信息")
  	private String bisExpInfo;
  	
  	@Column(cnName="原始id，适应与短信内容超长分条发送")
  	private Long originalId;
	
	/**id */
	public Long getId(){
		return id;
	}
	
	/**id */
	public void setId(Long id){
		this.id = id;
	}
	
	/**短信内容 */
	public String getContent(){
		return content;
	}
	
	/**短信内容 */
	public void setContent(String content){
		this.content = content;
	}
	
	/**短信类型 */
	public Integer getSmsType(){
		return smsType;
	}
	
	/**短信类型 */
	public void setSmsType(Integer smsType){
		this.smsType = smsType;
	}
	
	/**接收号码 */
	public String getToPhones(){
		return toPhones;
	}
	
	/**接收号码 */
	public void setToPhones(String toPhones){
		this.toPhones = toPhones;
	}
	
	/**定时发送时间 */
	public Date getSendTime(){
		return sendTime;
	}
	
	/**定时发送时间 */
	public void setSendTime(Date sendTime){
		this.sendTime = sendTime;
	}
	
	/**自定义扩展号 */
	public String getEncode(){
		return encode;
	}
	
	/**自定义扩展号 */
	public void setEncode(String encode){
		this.encode = encode;
	}
	
	/**重发次数 */
	public Integer getRetryTime(){
		return retryTime;
	}
	
	/**重发次数 */
	public void setRetryTime(Integer retryTime){
		this.retryTime = retryTime;
	}
	
	/**消息状态 1.待发送， 2. 发送成功， 3. 发送失败 */
	public Integer getStatus(){
		return status;
	}
	
	/**消息状态 1.待发送， 2. 发送成功， 3. 发送失败 */
	public void setStatus(Integer status){
		this.status = status;
	}
	
	/**返回状态 */
	public String getResStatus(){
		return resStatus;
	}
	
	/**返回状态 */
	public void setResStatus(String resStatus){
		this.resStatus = resStatus;
	}
	
	/**返回信息 */
	public String getResMsg(){
		return resMsg;
	}
	
	/**返回信息 */
	public void setResMsg(String resMsg){
		this.resMsg = resMsg;
	}
	
	/**sid */
	public String getSid(){
		return sid;
	}
	
	/**sid */
	public void setSid(String sid){
		this.sid = sid;
	}
	
	/**创建时间 */
	public Date getCreateTime(){
		return createTime;
	}
	
	/**创建时间 */
	public void setCreateTime(Date createTime){
		this.createTime = createTime;
	}
	
	/**返回时间 */
	public Date getRecTime(){
		return recTime;
	}
	
	/**返回时间 */
	public void setRecTime(Date recTime){
		this.recTime = recTime;
	}
	
	/**内容超70忽略 */
	public Boolean getOverLengthIgnore(){
		return overLengthIgnore;
	}
	
	/**内容超70忽略 */
	public void setOverLengthIgnore(Boolean overLengthIgnore){
		this.overLengthIgnore = overLengthIgnore;
	}
	
	/**是否实时发送 */
	public Boolean getSendRealTime(){
		return sendRealTime;
	}
	
	/**是否实时发送 */
	public void setSendRealTime(Boolean sendRealTime){
		this.sendRealTime = sendRealTime;
	}

	/**当前发送次数 */
	public Integer getCurRetryTime() {
		return curRetryTime;
	}

	/**当前发送次数 */
	public void setCurRetryTime(Integer curRetryTime) {
		this.curRetryTime = curRetryTime;
	}

	/**异常信息 */
	public String getBisExpInfo() {
		return bisExpInfo;
	}

	/**异常信息 */
	public void setBisExpInfo(String bisExpInfo) {
		this.bisExpInfo = bisExpInfo;
	}
	
	/**原始id，适应与短信内容超长分条发送 */
	public Long getOriginalId() {
		return originalId;
	}

	/**原始id，适应与短信内容超长分条发送*/
	public void setOriginalId(Long originalId) {
		this.originalId = originalId;
	}
	
	/**
	 * 短信是否发送成功
	 * @return
	 */
	public boolean smsSendSucess(){
		return STATUS_SEND_SUCCESS.equals(this.status) && "000".equals(this.resStatus);
	}

	@Override
	public String toString() {
		return "Sms [id=" + id + ", content=" + content + ", smsType="
				+ smsType + ", toPhones=" + toPhones + ", sendTime=" + sendTime
				+ ", encode=" + encode + ", resStatus=" + resStatus
				+ ", resMsg=" + resMsg + ", sid=" + sid + ", overLengthIgnore="
				+ overLengthIgnore + ", sendRealTime=" + sendRealTime
				+ ", retryTime=" + retryTime + ", curRetryTime=" + curRetryTime
				+ ", createTime=" + createTime + ", recTime=" + recTime
				+ ", status=" + status + ", bisExpInfo=" + bisExpInfo
				+ ", originalId=" + originalId + "]";
	}
	
}