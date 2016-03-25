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
			throw new MobileException("手机号码为空！");
		}
		if(StringUtil.isNull(messageCommand.getContent())){
			throw new MobileException("短信内容为空！");
		}
		return true;
	}
}
