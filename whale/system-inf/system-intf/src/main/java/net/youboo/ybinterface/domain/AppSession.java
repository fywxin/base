package net.youboo.ybinterface.domain;


import org.whale.system.annotation.jdbc.Column;
import org.whale.system.annotation.jdbc.Id;
import org.whale.system.annotation.jdbc.Table;
import org.whale.system.annotation.jdbc.Validate;
import org.whale.system.base.BaseEntry;

/**
 * 应用用户登录信息
 *
 * @author 王金绍
 * @Date 2015-10-31
 */
@Table(value="app_session", cnName="应用用户登录信息")
public class AppSession extends BaseEntry {
	private static final long serialVersionUID = -1446264575010l;
	
	public static final String CACHE_KEY = "appSession";
	
	/** 过期时间，30分钟 */
	public static final Integer CACHE_EXPRIE_TIME = 60 * 30;
	
	@Id
	@Column(name="ID", cnName="id")
	private Long id;
	
	@Validate(required=true)
  	@Column(cnName="sessionId", name="SESSION_ID")
	private String sessionId;
	
	@Column(cnName="用户名，手机号", name="USER_NAME")
	private String userName;
	
	@Column(cnName="创建时间", name="CREATE_TIME")
	private Long createTime;
	
	@Column(cnName="失效时间", name="DEAD_TIME")
	private Long deadTime;
	
	@Column(cnName="状态  1:正常， 2：失效", name="STATUS")
	private Integer status;
	
	
	/**id */
	public Long getId(){
		return id;
	}
	
	/**id */
	public void setId(Long id){
		this.id = id;
	}
	
	/**sessionId */
	public String getSessionId(){
		return sessionId;
	}
	
	/**sessionId */
	public void setSessionId(String sessionId){
		this.sessionId = sessionId;
	}
	
	/**创建时间 */
	public Long getCreateTime(){
		return createTime;
	}
	
	/**创建时间 */
	public void setCreateTime(Long createTime){
		this.createTime = createTime;
	}
	
	/**失效时间 */
	public Long getDeadTime(){
		return deadTime;
	}
	
	/**失效时间 */
	public void setDeadTime(Long deadTime){
		this.deadTime = deadTime;
	}
	
	/**状态 */
	public Integer getStatus(){
		return status;
	}
	
	/**状态 */
	public void setStatus(Integer status){
		this.status = status;
	}

	public String getUserName() {
		return userName;
	}

	public void setUserName(String userName) {
		this.userName = userName;
	}

	@Override
	public String toString() {
		return "AppSession [id=" + id + ", sessionId=" + sessionId
				+ ", userName=" + userName
				+ ", createTime=" + createTime + ", deadTime=" + deadTime
				+ ", status=" + status + "]";
	}

	
}