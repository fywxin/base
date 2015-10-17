package org.whale.system.service;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.OrderComparator;
import org.springframework.stereotype.Component;
import org.whale.system.base.Page;
import org.whale.system.cache.service.DictCacheService;
import org.whale.system.common.exception.SmsException;
import org.whale.system.common.util.Bootable;
import org.whale.system.common.util.SimpleHttpClient;
import org.whale.system.common.util.Strings;
import org.whale.system.common.util.TimeUtil;
import org.whale.system.dao.SmsDao;
import org.whale.system.domain.Sms;
import org.whale.system.spring.SpringContextHolder;

/**
 * 短信 管理
 *
 * @author 王金绍
 * 2014-10-16 10:31:44
 */
@Component
public class SmsService implements Bootable{
	
	private static final Logger logger = LoggerFactory.getLogger(SmsService.class);
	
	private static List<SmsSendEvent> smsSendListeners = null;
	
	private static final String SMS_ENCODE = "UTF-8";
	
	//private static final String SMS_SIGN = "【伯庸金融】";
	
	/**
	 * 获取短信信息
	 * @param id
	 * @return
	 */
	public static Sms get(Long id){
		if(id == null)
			return null;
		return getSmsDao().get(id);
	}
	
	/**
	 * 短信分页显示
	 * @param page
	 */
	public static void queryPage(Page page){
		getSmsDao().queryPage(page);
	}
	
	/**
	 * 实时发送短信
	 * @param sms
	 * @return
	 */
	public static boolean sendRealTime(Sms sms){		
		sms.setSendRealTime(true);
		brocastBeforSend(sms);
		validate(sms);
		List<Sms> smss = DiviedSms(sms);
		for(Sms s : smss){
			doSend(s);
			getSmsDao().save(s);
		}
		brocastAfterSend(sms);
		
		return sms.smsSendSucess();
	}
	
	/**
	 * 发送短信， 非实时
	 * @param sms
	 */
	public static void sendSms(Sms sms){
		sms.setSendRealTime(false);
		brocastBeforSend(sms);
		validate(sms);
		List<Sms> smss = DiviedSms(sms);
		for(Sms s : smss){
			getSmsDao().save(s);
		}
	}
	
	/**
	 * 发送短信， 非实时
	 * @param smss
	 */
	public static void sendSms(List<Sms> smss){
		if(smss != null){
			for(Sms sms : smss){
				sendSms(sms);
			}
		}
	}
	
	/**
	 * 接收短信回复接口
	 * @return
	 */
	public static String getSmsReceiveInfo(){
		String url = DictCacheService.getThis().getItemValue("SMS", "SMS_RECV_URL", "http://116.213.72.20/SMSHttpService/getmsg.aspx");
		String uid = DictCacheService.getThis().getItemValue("SMS", "SMS_UID", "byjr3");
		String pwd = DictCacheService.getThis().getItemValue("SMS", "SMS_PWD", "byjr3");
		
		if(url.indexOf("?") != -1){
			url+="?";
		}else{
			url+="&";
		}
		try {
			url+="username="+uid+"&password="+URLEncoder.encode(pwd, SMS_ENCODE);
		} catch (UnsupportedEncodingException e) {
			logger.error("接收短信回复失败：内容编码异常 url="+url, e);
			throw new SmsException("接收短信回复失败：内容编码异常 url="+url, e);
		}
		return SimpleHttpClient.get(url);		
	}
	
	/**
	 * 短信帐号余额查询
	 * @return
	 */
	public static String getSmsBalance(){
		String url = DictCacheService.getThis().getItemValue("SMS", "SMS_MONEY_URL", "http://116.213.72.20/SMSHttpService/Balance.aspx");
		String uid = DictCacheService.getThis().getItemValue("SMS", "SMS_UID", "byjr3");
		String pwd = DictCacheService.getThis().getItemValue("SMS", "SMS_PWD", "byjr3");
		
		if(url.indexOf("?") != -1){
			url+="?";
		}else{
			url+="&";
		}
		try {
			url+="username="+uid+"&password="+URLEncoder.encode(pwd, SMS_ENCODE);
		} catch (UnsupportedEncodingException e) {
			logger.error("短信帐号余额查询失败：内容编码异常 url="+url, e);
			throw new SmsException("短信帐号余额查询失败：内容编码异常 url="+url, e);
		}
		return SimpleHttpClient.get(url);
	}
	
	/**
	 * 校验短信数据合法性
	 * @param sms
	 */
	private static void validate(Sms sms){
		StringBuilder strb = new StringBuilder();
		if(Strings.isBlank(sms.getContent())){
			strb.append("内容不能为空;\n");
		}else{
			if(!sms.getContent().trim().startsWith(DictCacheService.getThis().getItemValue("SMS", "SMS_SIGN", "【伯庸金融】"))){
				sms.setContent(DictCacheService.getThis().getItemValue("SMS", "SMS_SIGN", "【伯庸金融】")+sms.getContent());
			}
		}
		if(sms.getSmsType() == null){
			strb.append("类型不能为空;\n");
		}
		if(Strings.isBlank(sms.getToPhones())){
			strb.append("接收号码列表不能为空;\n");
		}
		if(sms.getSendTime() != null && sms.getSendTime().before(new Date())){
			strb.append("定时发送时间不能早于当前时间;\n");
		}
				
		if(strb.length() > 0)
			throw new SmsException("短信发送失败："+strb.toString());
		if(sms.getCreateTime() == null){
			sms.setCreateTime(new Date());
		}
		if(sms.getStatus() == null){
			sms.setStatus(Sms.STATUS_SEND_WAITing);
		}
		sms.setContent(sms.getContent().trim());
	}
	
	/**
	 * 短信超过限长，且为多条发送时，分离短信
	 * @param sms
	 */
	private static List<Sms> DiviedSms(Sms sms){
		List<Sms> smss = new ArrayList<Sms>();
		if(!sms.getOverLengthIgnore()){
			if(sms.getContent().length() <= Sms.MAX_CONTENT_LENGTH){
				smss.add(sms);
			}else{
				sms.setStatus(Sms.STATUS_SEND_IGNORE);
				getSmsDao().save(sms);
				String content = sms.getContent();
				Sms child = null;
				while(Strings.isNotBlank(content)){
					child = new Sms();
					child.setCreateTime(sms.getCreateTime());
					if(content.length() > Sms.MAX_CONTENT_LENGTH){
						child.setContent(content.substring(0,Sms.MAX_CONTENT_LENGTH).trim());
						content = DictCacheService.getThis().getItemValue("SMS", "SMS_SIGN", "【伯庸金融】")+content.substring(Sms.MAX_CONTENT_LENGTH).trim();
					}else{
						child.setContent(content);
						content = null;
					}
					child.setEncode(sms.getEncode());
					child.setOriginalId(sms.getId());
					child.setOverLengthIgnore(true);
					child.setRetryTime(sms.getRetryTime());
					child.setSendRealTime(sms.getSendRealTime());
					child.setSendTime(sms.getSendTime());
					child.setSmsType(sms.getSmsType());
					child.setToPhones(sms.getToPhones());
					smss.add(child);
				}
			}
		}else{
			if(sms.getContent().length() > Sms.MAX_CONTENT_LENGTH){
				logger.info("短信内容超过限制，忽略超过部分");
				sms.setContent(sms.getContent().substring(0, Sms.MAX_CONTENT_LENGTH).trim());
			}
			smss.add(sms);
		}
		return smss;
	}
	
	/**
	 * 发送短信
	 * @param sms
	 */
	private static void doSend(Sms sms){
		logger.info("短信发送 开始： "+sms);
		
		String url = DictCacheService.getThis().getItemValue("SMS", "SMS_SEND_URL", "http://116.213.72.20/SMSHttpService/send.aspx");
		String uid = DictCacheService.getThis().getItemValue("SMS", "SMS_UID", "byjr3");
		String pwd = DictCacheService.getThis().getItemValue("SMS", "SMS_PWD", "byjr3");
		
		if(url.indexOf("?") != -1){
			url+="&";
		}else{
			url+="?";
		}
		try {
			url+="username="+uid+"&password="+URLEncoder.encode(pwd, SMS_ENCODE)+"&mobile="+sms.getToPhones()+"&content="+URLEncoder.encode(sms.getContent(), SMS_ENCODE);
			
			if(Strings.isNotBlank(sms.getEncode())){
				url+="&Extcode="+sms.getEncode();
			}
			if(sms.getSendTime() != null){
				url+="&senddate="+TimeUtil.formatTime(sms.getSendTime(), "yyyy-MM-dd hh:mm:ss");
			}
		} catch (UnsupportedEncodingException e) {
			logger.error("短信发送失败：内容编码异常 url="+url, e);
			throw new SmsException("短信发送失败：内容编码异常 url="+url, e);
		}
		
		try{
			logger.info("短信发送 接口调用开始： URL="+url);
			sms.setCurRetryTime((sms.getCurRetryTime() == null ? 0 : sms.getCurRetryTime())+1);
			
			String rs = SimpleHttpClient.get(url);	
			logger.info("短信发送 接口调用完成：rs="+rs+" URL="+url);
			
			sms.setRecTime(new Date());
			sms.setResMsg(rs);
			sms.setStatus(Sms.STATUS_SEND_SUCCESS);
			sms.setResStatus(rs);
		} catch(Exception e){
			sms.setBisExpInfo("短信发送失败：短信接口调用异常 url="+url+" 异常信息："+e.getMessage());
			if(sms.getCurRetryTime() >= sms.getRetryTime()){
				sms.setStatus(Sms.STATUS_SEND_FAIL);
				logger.error("短信发送失败：短信接口调用异常 url="+url, e);
			}
		}
		
		logger.info("短信发送 完成： "+sms);
	}
	
	/**
	 * 短信发送扫描线程
	 *
	 * @author 王金绍
	 * 2014年10月16日-下午3:50:50
	 */
	class SendSmsThread extends Thread {
		
		@Override
		public void run() {
			while(true){
				try {
					List<Sms> smss = getSmsDao().queryWaitSendSmss(DictCacheService.getThis().getItemIntValue("SMS", "SMS_SCAN_LIMIT", 20));
					if(smss != null){
						for(Sms sms : smss){
							doSend(sms);
							brocastAfterSend(sms);
							getSmsDao().update(sms);
						}
					}else{
						logger.debug("短信发送 没有待发送的短信");
					}
				} catch (Exception e) {
					logger.error("短信发送 获取待发送短信出现异常：", e);
				}
				
				try {
					int interval = DictCacheService.getThis().getItemIntValue("SMS", "SMS_SCAN_INTERVAL", 60000);
					if(interval < 3000){
						interval = 3000;
					}
					Thread.sleep(new Long(interval));
				} catch (InterruptedException e) {
				}
			}
		}
		
	}
	
	private static void brocastBeforSend(Sms sms){
		List<SmsSendEvent> listeners = getSmsSendListeners();
		if(listeners != null){
			for(SmsSendEvent listener : listeners){
				if(listener.match(sms)){
					listener.beforeSend(sms);
				}
			}
		}
	}
	
	private static void brocastAfterSend(Sms sms){
		List<SmsSendEvent> listeners = getSmsSendListeners();
		if(listeners != null){
			for(SmsSendEvent listener : listeners){
				if(listener.match(sms)){
					try {
						listener.afterSend(sms);
					} catch (Exception e) {
						logger.error("调用消息发送回调接口出现异常", e);
					}
				}
			}
		}
	}

	private static List<SmsSendEvent> getSmsSendListeners() {
		if(smsSendListeners == null){
			smsSendListeners = SpringContextHolder.getAutowiredClasses(SmsSendEvent.class);
			if(smsSendListeners != null){
				Collections.sort(smsSendListeners, new OrderComparator());
			}
		}
		return smsSendListeners;
	}

	@Override
	public Object init(Map<String, Object> context) {
		Thread thread = new SendSmsThread();
		thread.setName("短信扫描发送线程");
		thread.setDaemon(true);
		thread.start();
		logger.info("短信:启动短信发送线程...");
		return null;
	}

	@Override
	public boolean access() {
		return true;
	}
	
	private static SmsDao getSmsDao(){
		return SpringContextHolder.getBean(SmsDao.class);
	}

	@Override
	public int getOrder() {
		return 1001;
	}
	
}