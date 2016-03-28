package com.baozun.nebula.manager.sms;

import com.baozun.nebula.command.MessageCommand;
import com.baozun.nebula.manager.BaseManager;

/**
 * 短信发送的manager
 * @author shouqun.li
 * @version 2016年3月24日 上午10:35:44
 */
public interface SmsManager extends BaseManager{
	/**
	 * 发送短信
	 * @param messageCommand 短信对象
	 * @return false为发送失败，true为发送成功
	 * @throws Exception 
	 */
	public boolean sendMessage(MessageCommand messageCommand) throws Exception;
}
