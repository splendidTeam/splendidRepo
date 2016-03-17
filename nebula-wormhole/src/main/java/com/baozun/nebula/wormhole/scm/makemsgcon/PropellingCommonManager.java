package com.baozun.nebula.wormhole.scm.makemsgcon;

import com.baozun.nebula.model.system.MsgSendContent;

/**
 * 推送公共相关的方法
 * 
 * @author Justin Hu
 * 
 */
public interface PropellingCommonManager {

	/**
	 * 将obj转成json串，并且通过aes加密最后保存到发送消息内容表中
	 * 
	 * @param obj
	 *            加密对象
	 * @param id
	 *            唯一标识(t_sys_msg_send_record表ID)
	 */
	public MsgSendContent saveMsgBody(Object obj, Long id);
}
