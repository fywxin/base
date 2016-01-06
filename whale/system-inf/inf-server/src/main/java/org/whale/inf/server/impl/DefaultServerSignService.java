package org.whale.inf.server.impl;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.whale.inf.common.InfContext;
import org.whale.inf.common.InfException;
import org.whale.inf.common.ResultCode;
import org.whale.inf.common.SignService;
import org.whale.inf.server.ServerContext;
import org.whale.system.common.encrypt.EncryptUtil;
import org.whale.system.common.util.PropertiesUtil;

public class DefaultServerSignService implements SignService {
	
	private static final Logger logger = LoggerFactory.getLogger("server");

	@Override
	public String signReq(InfContext context) {
		return this.createReqSign(context);
	}

	@Override
	public String signResp(InfContext context) {
		return this.createRespSign(context);
	}
	
	/**
	 * 请求签名
	 * 规则
	 * 		MD5(接口uri + sign + reqno + bodyStr)
	 * 
	 * @param clientContext
	 * @return
	 */
	private String createReqSign(InfContext context){
		ServerContext serverContext = (ServerContext)context;
		StringBuilder strb = new StringBuilder();
		strb.append(serverContext.getUri())
			.append(PropertiesUtil.getValue("inf."+serverContext.getAppId()+".sign"))
			.append(serverContext.getReqno());
		
		if(serverContext.getReqStr() != null){
			strb.append(serverContext.getReqStr());
		}else{
			strb.append("[]");
		}
		
		try {
			String sign = EncryptUtil.md5(strb.toString().getBytes("utf-8"));
			if(logger.isDebugEnabled()){
				logger.debug("签名： {}, 签名字符串：{}", sign, strb.toString());
			}
			return sign;
		}catch(Exception e){
			logger.error("签名编码异常", e);
			throw new InfException(ResultCode.SIGN_ERROR, e);
		}
	}
	
	/**
	 * 响应结果签名
	 * 	MD5(接口uri + reqno + session + appSign +  + bodyStr)
	 * @param context
	 * @return
	 */
	private String createRespSign(InfContext context){
		ServerContext serverContext = (ServerContext)context;
		StringBuilder strb = new StringBuilder();
		strb.append(serverContext.getUri())
			.append(serverContext.getReqno())
			.append(serverContext.getSession() == null ? "" : serverContext.getSession())
			.append(PropertiesUtil.getValue("inf."+serverContext.getAppId()+".sign"));
		
		if(serverContext.getRespStr() != null){
			strb.append(serverContext.getRespStr());
		}
		try {
			String sign = EncryptUtil.md5(strb.toString().getBytes("utf-8"));
			if(logger.isDebugEnabled()){
				logger.debug("签名： {}, 签名字符串：{}", sign, strb.toString());
			}
			return sign;
		}catch(Exception e){
			logger.error("签名编码异常", e);
			throw new InfException(ResultCode.SIGN_ERROR, e);
		}
	}

}
