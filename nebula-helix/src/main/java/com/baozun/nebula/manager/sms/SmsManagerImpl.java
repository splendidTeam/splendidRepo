package com.baozun.nebula.manager.sms;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.baozun.nebula.command.MessageCommand;
import com.baozun.nebula.manager.sms.validator.MessageCommandValidator;

/**
 * 短信的发送Manager
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
		if(MessageCommandValidator.validator(messageCommand)){
			System.out.println("the message is being sended");
			System.out.println("the mobileNumber is" + messageCommand.getMobile());
			System.out.println("the message content is" + messageCommand.getContent());
			System.out.println("the message is sended");
			return true;
		}
		return false;
	}
}
