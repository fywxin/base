package net.youboo.ybinterface.domain;

import java.util.Date;

import org.whale.system.annotation.jdbc.Column;
import org.whale.system.annotation.jdbc.Id;
import org.whale.system.annotation.jdbc.Table;
import org.whale.system.annotation.jdbc.Validate;
import org.whale.system.base.BaseEntry;

/**
 * 终端版本
 *
 * @author 王金绍
 * @Date 2015-10-31
 */
@Table(value="client_version", cnName="终端版本")
public class ClientVersion extends BaseEntry {
	private static final long serialVersionUID = -1446262917944l;
	
	public static final String CACHE_KEY = "clientKey";
	
	@Id
	@Column(name="CLIENT_VERSION_ID", cnName="终端版本ID")
	private Long clientVersionId;
	
	@Column(name="APP_KEY", cnName="平台分配给应用的app_key")
	private String appKey;
	
	@Validate(required=true)
  	@Column(cnName="终端操作系统", name="CLIENT_OS")
	private String clientOs;
	
	@Validate(required=true)
  	@Column(cnName="支持操作系统版本", name="OS_VERSION")
	private String osVersion;
	
	@Validate(required=true)
  	@Column(cnName="版本号", name="CLIENT_VER")
	private String clientVer;
	
	@Validate(required=true)
  	@Column(cnName="更新包地址", name="PACKAGE_URL")
	private String packageUrl;
	
	@Validate(required=true)
  	@Column(cnName="是否取值升级", name="IS_FORCED_UPGRADE")
	private Boolean isForcedUpgrade;
	
	@Validate(required=true)
  	@Column(cnName="是否支持跨版本升级", name="IS_SKIP_UPGRADE")
	private Boolean isSkipUpgrade;
	
	@Column(cnName="状态", name="CLIENT_VERSION_STATUS")
	private Integer clientVersionStatus;
	
	@Validate(required=true)
  	@Column(cnName="接口签名密钥", name="SIGN_KEY")
	private String signKey;
	
	@Validate(required=true)
  	@Column(cnName="接口登录密钥", name="LOGIN_KEY")
	private String loginKey;
	
	@Column(cnName="创建时间", name="CREATE_TIME")
	private Date createTime;
	
	@Column(cnName="更新时间", name="UPDATE_TIME")
	private Date updateTime;
	
	@Column(cnName="更新者", name="UPDATER")
	private Long updater;
	
	
	/**终端版本ID */
	public Long getClientVersionId(){
		return clientVersionId;
	}
	
	/**终端版本ID */
	public void setClientVersionId(Long clientVersionId){
		this.clientVersionId = clientVersionId;
	}
	
	/**终端操作系统 */
	public String getClientOs(){
		return clientOs;
	}
	
	/**终端操作系统 */
	public void setClientOs(String clientOs){
		this.clientOs = clientOs;
	}
	
	/**支持操作系统版本 */
	public String getOsVersion(){
		return osVersion;
	}
	
	/**支持操作系统版本 */
	public void setOsVersion(String osVersion){
		this.osVersion = osVersion;
	}
	
	/**版本号 */
	public String getClientVer(){
		return clientVer;
	}
	
	/**版本号 */
	public void setClientVer(String clientVer){
		this.clientVer = clientVer;
	}
	
	/**更新包地址 */
	public String getPackageUrl(){
		return packageUrl;
	}
	
	/**更新包地址 */
	public void setPackageUrl(String packageUrl){
		this.packageUrl = packageUrl;
	}
	
	/**是否取值升级 */
	public Boolean getIsForcedUpgrade(){
		return isForcedUpgrade;
	}
	
	/**是否取值升级 */
	public void setIsForcedUpgrade(Boolean isForcedUpgrade){
		this.isForcedUpgrade = isForcedUpgrade;
	}
	
	/**是否支持跨版本升级 */
	public Boolean getIsSkipUpgrade(){
		return isSkipUpgrade;
	}
	
	/**是否支持跨版本升级 */
	public void setIsSkipUpgrade(Boolean isSkipUpgrade){
		this.isSkipUpgrade = isSkipUpgrade;
	}
	
	/**状态 */
	public Integer getClientVersionStatus(){
		return clientVersionStatus;
	}
	
	/**状态 */
	public void setClientVersionStatus(Integer clientVersionStatus){
		this.clientVersionStatus = clientVersionStatus;
	}
	
	/**接口签名密钥 */
	public String getSignKey(){
		return signKey;
	}
	
	/**接口签名密钥 */
	public void setSignKey(String signKey){
		this.signKey = signKey;
	}
	
	/**接口登录密钥 */
	public String getLoginKey(){
		return loginKey;
	}
	
	/**接口登录密钥 */
	public void setLoginKey(String loginKey){
		this.loginKey = loginKey;
	}
	
	/**创建时间 */
	public Date getCreateTime(){
		return createTime;
	}
	
	/**创建时间 */
	public void setCreateTime(Date createTime){
		this.createTime = createTime;
	}
	
	/**更新时间 */
	public Date getUpdateTime(){
		return updateTime;
	}
	
	/**更新时间 */
	public void setUpdateTime(Date updateTime){
		this.updateTime = updateTime;
	}
	
	/**更新者 */
	public Long getUpdater(){
		return updater;
	}
	
	/**更新者 */
	public void setUpdater(Long updater){
		this.updater = updater;
	}

	public String getAppKey() {
		return appKey;
	}

	public void setAppKey(String appKey) {
		this.appKey = appKey;
	}

}