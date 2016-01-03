package org.whale.inf.client.impl;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.whale.inf.client.ClientConf;
import org.whale.inf.client.ClientContext;
import org.whale.inf.common.InfContext;
import org.whale.inf.common.InfException;
import org.whale.inf.common.Result;
import org.whale.inf.common.ResultCode;
import org.whale.inf.common.SignService;
import org.whale.system.common.encrypt.EncryptUtil;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.serializer.SerializerFeature;

/**
 * 默认签名校验器
 * @author Administrator
 *
 */
public class DefaultClientSignService implements SignService{
	
	private static final Logger logger = LoggerFactory.getLogger(DefaultClientSignService.class);
	
	@Autowired
	private ClientConf clientConf;
	
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
		ClientContext clientContext = (ClientContext)context;
		StringBuilder strb = new StringBuilder();
		strb.append(clientContext.getServiceUrl()+"/"+clientContext.getMethod().getName())
			.append(clientConf.getSignKey())
			.append(clientContext.getReqno());
		
		if(clientContext.getArgs() != null ){
			if(clientContext.getReqStr() != null){
				strb.append(clientContext.getReqStr());
			}else{
				strb.append(JSON.toJSONString(clientContext.getArgs(), SerializerFeature.WriteClassName));
			}
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
	 * 	MD5(接口uri + reqno + session + appSign + bodyStr)
	 * @param context
	 * @return
	 */
	private String createRespSign(InfContext context){
		ClientContext clientContext = (ClientContext)context;
		StringBuilder strb = new StringBuilder();
		strb.append(clientContext.getServiceUrl()+"/"+clientContext.getMethod().getName())
			.append(clientContext.getReqno())
			.append(clientContext.getSession() == null ? "" : clientContext.getSession())
			.append(clientConf.getSignKey());
		
		if(clientContext.getRespStr() != null){
			strb.append(clientContext.getRespStr());
		}else{
			strb.append(JSON.toJSONString(Result.get(), SerializerFeature.WriteClassName));
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
