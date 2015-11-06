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
import net.youboo.ybinterface.request.LoginReq;
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
import org.whale.system.common.util.PropertiesUtil;
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
			logger.debug("接口-请求-参数-开始：{}", this.getParameterMap(webRequest));
		}
		
		InfContext context = new InfContext();
		ThreadContext.getContext().put(InfContext.THREAD_KEY, context);
		
		context.setReqParam(this.getReqParam(webRequest));
		context.setUri(this.getUri(webRequest));
		String useLoginKeyUris = ","+PropertiesUtil.getValue("login.key", "/login,/restpwd");
		
		context.setIsLoginKey(useLoginKeyUris.indexOf(","+context.getUri()) != -1);
		
		//获取客户端对应的签名与登录解密key
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
		
		//客户端加密与解密key一致
		//来自登录接口
		if(context.getIsLoginKey()){
			context.setReqSecure(clientVersion.getLoginKey());
			context.setResStr(clientVersion.getLoginKey());
		}else{//非登录接口
			AppSession appSession = this.appSessionService.getBySessionId(context.getReqParam().getSession());
			if(appSession == null 
					|| Strings.isBlank(appSession.getUserName()) 
					|| appSession.getDeadTime() <= System.currentTimeMillis() 
					|| AppSession.STATUS_DEAD.equals(appSession.getStatus())){
				logger.warn("接口-请求-参数：应用登录过期");
				throw new InfException(ErrorCode.SESSION_INVAIAL);
			}
			ReqBody annot = methodParam.getParameterAnnotation(ReqBody.class);
			if(annot.secure()){
				context.setReqSecure(appSession.getUserName());
				context.setResSecure(appSession.getUserName());
			}
			this.setSession(webRequest, appSession);
		}
		
	}

	
	public void validateArgument(MethodParameter parameter,
			ModelAndViewContainer mavContainer, NativeWebRequest webRequest,
			WebDataBinderFactory binderFactory, WebDataBinder binder,
			Object argument) {
		
	}

	/**
	 * 签名校验
	 */
	public void afterResolveArgument(MethodParameter parameter,
			ModelAndViewContainer mavContainer, NativeWebRequest webRequest,
			WebDataBinderFactory binderFactory, Object argument) {
		
		InfContext context = (InfContext)ThreadContext.getContext().get(InfContext.THREAD_KEY);
		context.setArgument(argument);
		//sign 签名校验
		//sign 签名规则 MD5(请求参数+signKey+body)
		StringBuilder strb = new StringBuilder();
		strb.append(context.getReqParam().buildSignStr())
			.append(context.getSignKey());
		if(argument != null){
			strb.append(context.getReqStr());
		}
		
		try {
			String calSign = EncryptUtil.md5(strb.toString().getBytes("utf-8"));
			if(logger.isDebugEnabled()){
				logger.debug("接口-请求-参数-sign校验: {}!, 客户端 {}，服务端 {}\n signBody：{}", 
						calSign.equals(context.getReqParam().getSign()) ? "成功":"失败",
								context.getReqParam().getSign(), 
								calSign,
								strb.toString());
			}
			if(!calSign.equals(context.getReqParam().getSign())){
				throw new InfException(ErrorCode.SIGN_ERROR);
			}
		} catch (UnsupportedEncodingException e) {
			logger.error("接口-请求-参数：参数签名md5错误", e);
			throw new InfException(ErrorCode.SIGN_ERROR);
		}
		
		if(logger.isDebugEnabled()){
			logger.debug("接口-请求-参数-完成：{}", context);
		}
	}
	
	/**
	 * 如果是登录接口，登录处理成功，则保存sessionKey
	 */
	public void beforeHandleReturnValue(Object returnValue,
			MethodParameter returnParam, ModelAndViewContainer mavContainer,
			NativeWebRequest webRequest) {
		InfContext context = (InfContext)ThreadContext.getContext().get(InfContext.THREAD_KEY);
		if(context.getIsLoginKey() && "/login".equals(context.getUri())){
			if(returnValue != null && (returnValue instanceof Result)){
				Result<?> result = (Result<?>)returnValue;
				if(Result.SUCCESS_CODE.equals(result.getCode())){
					LoginReq loginReq = (LoginReq)context.getArgument();
					AppSession appSession = new AppSession();
					appSession.setSessionId(webRequest.getSessionId());
					appSession.setCreateTime(System.currentTimeMillis());
					appSession.setStatus(AppSession.STATUS_ALIVE);
					appSession.setUserName(loginReq.getLoginName());
					appSession.setDeadTime(appSession.getCreateTime()+PropertiesUtil.getValueInt("appSession.exprie.time", AppSession.CACHE_EXPRIE_TIME) * 1000);
					
					this.appSessionService.save(appSession);
					this.setSession(webRequest, appSession);
				}
			}
		}
		
	}

	public void afterHandleReturnValue(Object returnValue,
			MethodParameter returnParam, ModelAndViewContainer mavContainer,
			NativeWebRequest webRequest) {
		
		InfContext context = (InfContext)ThreadContext.getContext().get(InfContext.THREAD_KEY);
		HttpServletResponse response = (HttpServletResponse)webRequest.getNativeResponse();
		
		//返回响应报文流水号，客户端异步请求时依赖此字段
		response.addHeader("reqno", context.getReqParam().getReqno());
		//返回响应报文是否有加密标志
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
	
	/**
	 * fastJson 解密报文
	 */
	public byte[] onRead(byte[] datas) {
		InfContext context = (InfContext)ThreadContext.getContext().get(InfContext.THREAD_KEY);
		if(context == null){
			throw new SysException("接口系统参数对象为空，请copy线程上下文数据");
		}
		if(Strings.isNotBlank(context.getReqSecure()) && datas != null && datas.length > 0){
			if(logger.isDebugEnabled()){
				logger.debug("接口-请求-解密：密钥 {}", context.getReqSecure());
			}
			try{
				datas = AESUtil.decrypt(datas, context.getReqSecure().getBytes());
			}catch(Exception e){
				logger.error("接口-请求-解密：密钥 "+context.getReqSecure(), e);
				throw new InfException(ErrorCode.ENCRYPT_ERROR);
			}
		}else{
			if(logger.isDebugEnabled()){
				logger.debug("接口-请求-解密：空密钥，非加密报文不解密");
			}
		}
		context.setReqStr(new String(datas));
		if(datas.length == 0){//body 为空时，防止反序列化异常
			datas = "{}".getBytes();
		}
		return datas;
	}

	/**
	 * fastJson 加密报文
	 */
	public byte[] onWrite(byte[] datas) {
		InfContext context = (InfContext)ThreadContext.getContext().get(InfContext.THREAD_KEY);
		if(context == null){
			throw new SysException("接口系统参数对象为空，请copy线程上下文数据");
		}
		
		context.setResStr(new String(datas));
		
		if(Strings.isNotBlank(context.getResSecure()) && datas != null && datas.length > 0){
			if(logger.isDebugEnabled()){
				logger.debug("接口-返回-加密：密钥 {}", context.getResSecure());
			}
			try{
				datas = AESUtil.encrypt(datas, context.getResSecure().getBytes());
			}catch(Exception e){
				logger.error("接口-返回-加密：密钥 " + context.getResSecure(), e);
				throw new InfException(ErrorCode.ENCRYPT_ERROR);
			}
		}else{
			if(logger.isDebugEnabled()){
				logger.debug("接口-返回-加密：空密钥，非加密报文不加密");
			}
		}
		
		return datas;
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
	
	private void setSession(NativeWebRequest webRequest, AppSession appSession){
		HttpServletRequest request = (HttpServletRequest)webRequest.getNativeRequest();
		request.getSession().setAttribute(AppSession.CACHE_KEY, appSession);
	}
	
	private boolean chkReqParam(ReqParam reqParam){
		if(Strings.isBlank(reqParam.getTimestamp()) || reqParam.getTimestamp().length() != 14){
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
