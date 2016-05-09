package org.whale.system.auth.tag;

import java.io.IOException;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.jsp.JspException;
import javax.servlet.jsp.JspWriter;
import javax.servlet.jsp.tagext.TagSupport;

import org.whale.system.auth.cache.UserAuthCacheService;
import org.whale.system.base.UserContext;
import org.whale.system.common.constant.SysConstant;
import org.whale.system.common.util.Strings;
import org.whale.system.common.util.ThreadContext;

public class AuthTag extends TagSupport {
	private static final long serialVersionUID = 1343L;
	
	/**权限码，开发人员提供与相应的操作的权限码，系统根据此权限码进行过滤 */
	private String authCode;

	private boolean authAdmin;

	private boolean authLogin;
	
	@Override
	public int doStartTag() throws JspException {
		UserContext uc = (UserContext)ThreadContext.getContext().get(ThreadContext.KEY_USER_CONTEXT);
		HttpServletRequest request =null;
		if(uc == null){
			request = (HttpServletRequest) pageContext.getRequest();
			uc = (UserContext)request.getSession().getAttribute(SysConstant.USER_CONTEXT_KEY);
		}
		if(uc == null){
			JspWriter out = pageContext.getOut();
			try {
				StringBuffer strb = new StringBuffer();
				strb.append("<SCRIPT type=\"text/javascript\">")
					.append("alert(\"用户未登入，请重新登入\");")
					.append("window.top.location.href=\"")
					.append(request.getContextPath())
					.append("\";")
					.append("</SCRIPT>");
				out.print(strb.toString());
			} catch (IOException e) {
			}
		}
		
		if(uc != null){
			if(uc.isSuperAdmin())
				return EVAL_BODY_INCLUDE;
			if (authAdmin){
				return SKIP_BODY;
			}
			if (authLogin){
				return EVAL_BODY_INCLUDE;
			}
			if(UserAuthCacheService.getThis().getUserAuth(uc.getUserId()).getAuthCodes().contains(authCode)){
				return EVAL_BODY_INCLUDE;
			}else{
				return SKIP_BODY;
			}
			
		}
		return SKIP_BODY;
	}

	public String getAuthCode() {
		return authCode;
	}

	public void setAuthCode(String authCode) {
		this.authCode = authCode;
	}

	public boolean isAuthAdmin() {
		return authAdmin;
	}

	public void setAuthAdmin(boolean authAdmin) {
		this.authAdmin = authAdmin;
	}

	public boolean isAuthLogin() {
		return authLogin;
	}

	public void setAuthLogin(boolean authLogin) {
		this.authLogin = authLogin;
	}
}
