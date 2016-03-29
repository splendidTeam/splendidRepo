package com.baozun.nebula.manager.sms.validator;

import com.baozun.nebula.command.MessageCommand;
import com.baozun.nebula.exception.MobileException;
import com.baozun.nebula.utilities.common.StringUtil;



/**
 * 对短信的一些简单的validator
 * @author shouqun.li
 * @version 2016年3月25日 下午1:32:09
 */
public class MessageCommandValidator{
	public static boolean validator(MessageCommand messageCommand) throws MobileException{
		if(StringUtil.isNull(messageCommand.getMobile())){
			throw new MobileException("the mobile number is null !");
		}
		if(StringUtil.isNull(messageCommand.getContent())){
			throw new MobileException("the message content is null ！");
		}
		return true;
	}
}
