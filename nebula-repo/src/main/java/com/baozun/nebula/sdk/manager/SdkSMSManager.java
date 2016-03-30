package com.baozun.nebula.sdk.manager;

import com.baozun.nebula.command.SMSCommand;
import com.baozun.nebula.manager.BaseManager;

/**
 * 短信发送
 * @author D.C
 * @version 2016年3月24日 上午10:35:44
 */
public interface SdkSMSManager extends BaseManager{
	/**
	 * 发送短信
	 * @param messageCommand 短信对象
	 * @return false为发送失败，true为发送成功
	 * @throws Exception 
	 */
	SendResult send(SMSCommand messageCommand);
	enum SendResult {
		SUCESS, FAILURE, ERROR, INVALIDATE_PARAM;
	}
}
