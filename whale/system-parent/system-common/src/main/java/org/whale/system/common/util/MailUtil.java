package org.whale.system.common.util;

import java.io.File;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.concurrent.ConcurrentHashMap;

import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.mail.javamail.JavaMailSenderImpl;
import org.springframework.mail.javamail.MimeMessageHelper;

import com.alibaba.fastjson.JSON;

/**
 * 邮件发送器
 * 
 * @author 王金绍
 * 2014年10月26日 下午7:59:17
 */
public class MailUtil {
	
	private static final Logger logger = LoggerFactory.getLogger("邮件：");
	
    private static Integer mailRetry = PropertiesUtil.getValueInt("mail.retry", 1);
    private static Integer mailSleep = PropertiesUtil.getValueInt("mail.sleep", 60);
    
    private static final ConcurrentHashMap<MimeMessage, Integer> retryMap = new ConcurrentHashMap<MimeMessage, Integer>();
    
    /**
     * 发送邮件
     * @param to 收件人
     * @param subject 邮件主题
     * @param text 邮件内容
     * @throws MessagingException
     */
    public static void sendMail(String to, String subject, String text) throws MessagingException{
    	List<String> tos = new ArrayList<String>(1);
    	tos.add(to);
    	sendMail(tos, subject, text, null, null);
    }
	
	/**
	 * 发送邮件
	 * @param to 收件人列表
	 * @param subject 邮件主题
	 * @param text  邮件内容
	 * @param files 附件
	 * @param personal  私人信息
	 * @throws MessagingException 邮件异常信
	 */
	public static void sendMail(Collection<String> to, String subject, String text, Collection<File> files, String personal) throws MessagingException{
		if(to == null || to.size() < 1){
			logger.error("主题: "+subject+ "收件人地址为空");
			throw new MessagingException("主题: ["+subject+ "] 收件人地址为空");
		}
		logger.info("开始发送邮件= 收件人:"+JSON.toJSONString(to)+", 主题: "+subject);
		
        System.setProperty("mail.mime.encodefilename", "true");
		MimeMessage mailMessage = getMailSender().createMimeMessage();
		// 注意这里的boolean,等于真的时候才能嵌套图片，在构建MimeMessageHelper时候，所给定的值是true表示启用，  
        // multipart模式 为true时发送附件 可以设置html格式  
		MimeMessageHelper messageHelper = new MimeMessageHelper(mailMessage, true, "utf-8"); 
		
		messageHelper.setTo(to.toArray(new String[to.size()]));
		try {
			messageHelper.setFrom(getMailSender().getUsername(),personal);
		} catch (UnsupportedEncodingException e1) {
			logger.error("发件人邮件格式错误:"+getMailSender().getUsername());
			throw new MessagingException("发件人邮件格式错误:"+getMailSender().getUsername(), e1);
		}
		messageHelper.setSubject(subject);
        messageHelper.setText(text, true);
        if(files != null){
    		for(File file:files)
                messageHelper.addAttachment(file.getName(), file);
    	}
        doSendMail(mailMessage);
        logger.info("完成发送邮件= 收件人:"+JSON.toJSONString(to)+", 主题: "+subject);
	}

	
	private static void doSendMail(MimeMessage mailMessage){
		try{
            getMailSender().send(mailMessage);
            if(retryMap.contains(mailMessage)){
    			retryMap.remove(mailMessage);
    		}
        }catch(Exception e){
            Integer time = retryMap.get(mailMessage);
            if(time != null && time >= mailRetry){
            	retryMap.remove(mailMessage);
            	try {
					logger.error("邮件["+mailMessage.getSubject()+"]发生失败，超过重试次数[mailSendRetry="+mailRetry+"] 不再发送！", e);
				} catch (MessagingException e1) {
					logger.error("邮件 获取主题异常：",e1);
				}
            }else{
            	if(time == null){
            		time = 0;
            	}
            	time++;
            	retryMap.put(mailMessage, time);
            	retrySendMail();
            	try {
					logger.warn("发送邮件["+mailMessage.getSubject()+"]第 ["+time+"] 次异常 "+getMailSender().getHost()+":"+getMailSender().getPort()
							+" "+getMailSender().getUsername()+"/"+getMailSender().getPassword()+", 加入到重试队列中！");
				} catch (MessagingException e1) {
					logger.error("邮件 获取主题异常：",e1);
				}
            }
        }
	}
	
	/**
	 * 重试发送失败的邮件
	 */
	private static void retrySendMail(){
		if(retryMap.size() == 0){
			logger.info("邮件都发送成功，无需再重试！");
		}else{
			logger.info("邮件共有 ["+retryMap.size()+"] 份需要重试，现在开始重试发送....");
			while(retryMap.size() > 0){
				for(MimeMessage mailMessage : retryMap.keySet()){
					doSendMail(mailMessage);
					try {
						Thread.sleep(mailSleep * 1000L);
					} catch (InterruptedException e) {
					}
				}
			}
			logger.info("邮件重试发送完成！");
		}
	}
	
	private static JavaMailSenderImpl getMailSender(){
		return SpringContextHolder.getBean(JavaMailSenderImpl.class);
	}
	
}
