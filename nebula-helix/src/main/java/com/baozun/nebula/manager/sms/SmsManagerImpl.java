package com.baozun.nebula.manager.sms;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.baozun.nebula.command.MessageCommand;
import com.baozun.nebula.manager.sms.validator.MessageCommandValidator;

/**
 * @author shouqun.li
 * 2016年3月24日 上午10:35:35
 */
@Transactional
@Service("smsManager")
public class SmsManagerImpl implements SmsManager {
	
	private static final Logger LOG = LoggerFactory.getLogger(SmsManagerImpl.class);
	
	@Override
	public boolean sendMessage(MessageCommand messageCommand) throws Exception {
		
		//等待胡强的短信服务api
		
		/*String url = ProfileConfigUtil.findPro("config/sms.properties").getProperty("sms.url");
		String sn = ProfileConfigUtil.findPro("config/sms.properties").getProperty("sms.sn");
		String pwd = ProfileConfigUtil.findPro("config/sms.properties").getProperty("sms.pwd");
		LOG.info("send sms interface url : "+url);*/
		if(MessageCommandValidator.validator(messageCommand)){
			
			/*String message = messageCommand.getContent();
			if(message.indexOf("&")>=0) {
				message=message.replace("&","&amp;");
			}
			if(message.indexOf("<")>=0) {
				message=message.replace("<","&lt;");
			}
			if(message.indexOf(">")>=0) {
				message=message.replace(">","&gt;");
			}
			String result = "";
			SMSClient smsClient = new SMSClient(sn, url, pwd);
			if(Validator.isNotNullOrEmpty(messageCommand.getFixedSendTime())){
				SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
				String stime = sdf.format(messageCommand.getFixedSendTime());
				result = smsClient.messageSend(messageCommand.getMobile(), message, "", stime, "");
			}else{
				result = smsClient.messageSend(messageCommand.getMobile(), message);
			}*/
			/*if("".equals(result) || result.startsWith("-"))//以负号判断是否发送成功
			{
				LOG.error("SmsManager send error, errorCode:" + result);
				return false;
			}*/
			//输出返回标识，为小于19位的正数，String类型的，记录您发送的批次
			/*else{
				LOG.info("send mobile verification code, mobile:" + messageCommand.getMobile() + ",resultValue:" + result + ",sendTime:" + new Date());
				
				return true;
			}*/
			return true;
		}
		return false;
	}
}
