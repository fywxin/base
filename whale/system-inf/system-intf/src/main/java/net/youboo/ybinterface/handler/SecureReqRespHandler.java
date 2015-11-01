package net.youboo.ybinterface.handler;

import java.io.UnsupportedEncodingException;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.youboo.ybinterface.constant.ErrorCode;
import net.youboo.ybinterface.context.InfContext;
import net.youboo.ybinterface.context.ReqParam;
import net.youboo.ybinterface.context.Result;
import net.youboo.ybinterface.domain.AppSession;
import net.youboo.ybinterface.domain.ClientVersion;
import net.youboo.ybinterface.exceptions.InfException;
import net.youboo.ybinterface.param.LoginInfoParam;
import net.youboo.ybinterface.service.AppSessionService;
import net.youboo.ybinterface.service.ClientVersionService;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.MethodParameter;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.support.WebDataBinderFactory;
import org.springframework.web.context.request.NativeWebRequest;
import org.springframework.web.method.support.ModelAndViewContainer;
import org.whale.system.annotation.web.ReqBody;
import org.whale.system.common.encrypt.AESUtil;
import org.whale.system.common.encrypt.EncryptUtil;
import org.whale.system.common.exception.SysException;
import org.whale.system.common.util.Strings;
import org.whale.system.common.util.ThreadContext;
import org.whale.system.spring.ReqRespHandler;

/**
 * 安全校验处理类
 * 
 * @author 王金绍
 * 2015年11月2日 上午1:10:55
 */
@Component("secureReqRespHandler")
public class SecureReqRespHandler implements ReqRespHandler {
	
	private static final Logger logger = LoggerFactory.getLogger(SecureReqRespHandler.class);
	
	@Autowired
	ClientVersionService clientVersionService;
	
	@Autowired
	AppSessionService appSessionService;
	

	/**
	 * 流程：
	 * 	1. 获取请求URL 参数信息
	 * 	2. 根据参数获取请求接口应用信息，包括loginKey 与 signKey
	 * 	3. 根据ReqBody(secure=true)判断是否要加密
	 *  4. 获取加密串和解密串
	 *  5. 解密报文
	 *  6. signKey 签名校验
	 *  7. 处理业务方法
	 *  8. 获取返回报文
	 * 
	 */
	public void beforeResolveArgument(MethodParameter methodParam, ModelAndViewContainer mavContainer, NativeWebRequest webRequest, WebDataBinderFactory binderFactory) {
		if(logger.isDebugEnabled()){
			logger.debug("接口-请求-参数：{}", this.getParameterMap(webRequest));
		}
		
		InfContext context = new InfContext();
		ThreadContext.getContext().put(InfContext.THREAD_KEY, context);
		
		context.setReqParam(this.getReqParam(webRequest));
		context.setUri(this.getUri(webRequest));
		context.setIsLogin("/login".equals(context.getUri()));
		
		ClientVersion clientVersion = this.clientVersionService.getByAppKeyAndVersion(context.getReqParam().getAppKey(), context.getReqParam().getVersion());
		context.setClientVersion(clientVersion);
		if(clientVersion == null){
			logger.error("接口-请求-参数：获取ClientVersion为空! [{} , {}]", context.getReqParam().getAppKey(), context.getReqParam().getVersion());
			throw new InfException(ErrorCode.PARAM_ERROR);
		}
		context.setSignKey(clientVersion.getSignKey());
		if(!this.chkReqParam(context.getReqParam())){
			throw new InfException(ErrorCode.PARAM_ERROR);
		}
		
		if(context.getIsLogin()){
			context.setReqSecure(clientVersion.getLoginKey());
			context.setResStr(clientVersion.getLoginKey());
		}else{
			AppSession appSession = this.appSessionService.getBySessionId(context.getReqParam().getSession());
			if(appSession == null || Strings.isBlank(appSession.getUserName())){
				logger.warn("接口-请求-参数：应用登录过期");
				throw new InfException(ErrorCode.SESSION_INVAIAL);
			}
			ReqBody annot = methodParam.getParameterAnnotation(ReqBody.class);
			if(annot.secure()){
				context.setReqSecure(appSession.getUserName());
				context.setResSecure(appSession.getUserName());
			}
		}
		
	}

	
	public void validateArgument(MethodParameter parameter,
			ModelAndViewContainer mavContainer, NativeWebRequest webRequest,
			WebDataBinderFactory binderFactory, WebDataBinder binder,
			Object argument) {
		
	}

	public void afterResolveArgument(MethodParameter parameter,
			ModelAndViewContainer mavContainer, NativeWebRequest webRequest,
			WebDataBinderFactory binderFactory, Object argument) {
		
		InfContext context = (InfContext)ThreadContext.getContext().get(InfContext.THREAD_KEY);
		context.setArgument(argument);
		//sign 校验
		StringBuilder strb = new StringBuilder();
		strb.append(context.getReqParam().buildSignStr())
			.append(context.getSignKey());
		if(argument != null){
			strb.append(context.getReqStr());
		}
		
		try {
			System.out.println(strb.toString());
			if(!EncryptUtil.md5(strb.toString().getBytes("utf-8")).equals(context.getReqParam().getSign())){
				throw new InfException(ErrorCode.SIGN_ERROR);
			}
		} catch (UnsupportedEncodingException e) {
			logger.error("接口-请求-参数：参数签名md5错误", e);
			throw new InfException(ErrorCode.SIGN_ERROR);
		}
	}
	
	
	public void beforeHandleReturnValue(Object returnValue,
			MethodParameter returnParam, ModelAndViewContainer mavContainer,
			NativeWebRequest webRequest) {
		InfContext context = (InfContext)ThreadContext.getContext().get(InfContext.THREAD_KEY);
		if(context.getIsLogin()){
			if(returnValue != null && (returnValue instanceof Result)){
				Result<?> result = (Result<?>)returnValue;
				if(result.getCode().equals(0)){
					LoginInfoParam loginInfoParam = (LoginInfoParam)context.getArgument();
					AppSession appSession = new AppSession();
					appSession.setSessionId(webRequest.getSessionId());
					appSession.setCreateTime(System.currentTimeMillis());
					appSession.setStatus(1);
					appSession.setUserName(loginInfoParam.getUserName());
					appSession.setDeadTime(appSession.getCreateTime()+AppSession.CACHE_EXPRIE_TIME * 1000);
					this.appSessionService.save(appSession);
				}
			}
		}
		
	}

	public void afterHandleReturnValue(Object returnValue,
			MethodParameter returnParam, ModelAndViewContainer mavContainer,
			NativeWebRequest webRequest) {
		
		InfContext context = (InfContext)ThreadContext.getContext().get(InfContext.THREAD_KEY);
		HttpServletResponse response = (HttpServletResponse)webRequest.getNativeResponse();
		
		response.addHeader("reqno", context.getReqParam().getReqno());
		if(Strings.isNotBlank(context.getResSecure())){
			response.addHeader("encrypt", "1");
		}else{
			response.addHeader("encrypt", "0");
		}
		
//		StringBuilder strb = new StringBuilder();
//		strb.append(context.getReqParam().buildSignStr())
//			.append(context.getSignKey());
//		if(returnValue != null){
//			strb.append(context.getResStr());
//		}
//		strb.append(context.getSignKey());
//		try {
//			response.addHeader("sign", new String(DigestUtils.md5Digest(strb.toString().getBytes("UTF-8"))));
//		} catch (UnsupportedEncodingException e) {
//			logger.error("接口-响应-头部：头部签名md5错误", e);
//			throw new InfException(ErrorCode.SIGN_ERROR);
//		}
	}
	
	@SuppressWarnings("all")
	private Map getParameterMap(NativeWebRequest webRequest){
		HttpServletRequest request = (HttpServletRequest)webRequest.getNativeRequest();
		return request.getParameterMap();
	}
	
	private String getUri(NativeWebRequest webRequest){
		HttpServletRequest request = (HttpServletRequest)webRequest.getNativeRequest();
		String ctx = request.getContextPath();
		if(ctx != null && ctx.length() > 1){
			return request.getRequestURI().substring(ctx.length());
		}
		return request.getRequestURI();
	}
	
	public byte[] onRead(byte[] datas) {
		InfContext context = (InfContext)ThreadContext.getContext().get(InfContext.THREAD_KEY);
		if(context == null){
			throw new SysException("接口系统参数对象为空，请copy线程上下文数据");
		}
		if(Strings.isNotBlank(context.getReqSecure()) && datas != null && datas.length > 0){
			datas = AESUtil.decrypt(datas, context.getReqSecure().getBytes());
		}
		context.setReqStr(new String(datas));
		if(datas.length == 0){
			datas = "{}".getBytes();
		}
		return datas;
	}

	public byte[] onWrite(byte[] datas) {
		InfContext context = (InfContext)ThreadContext.getContext().get(InfContext.THREAD_KEY);
		if(context == null){
			throw new SysException("接口系统参数对象为空，请copy线程上下文数据");
		}
		
		context.setResStr(new String(datas));
		
		if(Strings.isNotBlank(context.getResSecure()) && datas != null && datas.length > 0){
			datas = AESUtil.encrypt(datas, context.getResSecure().getBytes());
		}
		
		return datas;
	}
	
	private boolean chkReqParam(ReqParam reqParam){
		if(Strings.isBlank(reqParam.getTimestamp()) || reqParam.getTimestamp().length() != 12){
			logger.error("接口-请求-参数：时间戳错误 "+reqParam.getTimestamp());
			return false;
		}
		if(Strings.isBlank(reqParam.getReqno()) || reqParam.getReqno().length() != 20){
			logger.error("接口-请求-参数：流水号错误 "+reqParam.getReqno());
			return false;
		}
		return true; 
	}
	
	private ReqParam getReqParam(NativeWebRequest request){
		ReqParam reqParam = new ReqParam();
		reqParam.setAppKey(request.getParameter("app_key"));
		reqParam.setMethod(request.getParameter("method"));
		reqParam.setSession(request.getParameter("session"));
		reqParam.setTimestamp(request.getParameter("timestamp"));
		reqParam.setReqno(request.getParameter("reqno"));
		reqParam.setFormat(request.getParameter("format"));
		reqParam.setVersion(request.getParameter("version"));
		reqParam.setGzip(request.getParameter("gzip"));
		reqParam.setEncrypt(request.getParameter("encrypt"));
		reqParam.setSignMethod(request.getParameter("sign_method"));
		reqParam.setSign(request.getParameter("sign"));
		return reqParam;
	}

	

}
