package com.baozun.nebula.wormhole.scm;

import com.baozun.nebula.model.system.MsgSendContent;
import com.baozun.nebula.model.system.MsgSendRecord;

/**
 * 公共消息的功能
 * 直接与MQ进行交互
 * 形成消息头以及消息体
 * @author Justin Hu
 *
 */
public interface MessageCommonManager {

	/**
	 * 发送消息到MQ
	 * 通过sendRecordId找到消息内容，发送到MQ中
	 * 1.生成消息头相关信息，如签名，接口标识，MSGID等
	 * 2.消息正文就是MsgSendContent表中msgBody的数据
	 * 3.发送消息到MQ
	 * @param msr 消息记录对象
	 * @return
	 */
	public Boolean sendToMq(MsgSendRecord msr);
	
	
	/**
	 * 发送消息到MQ
	 * 通过sendRecordId找到消息内容，发送到MQ中
	 * 1.生成消息头相关信息，如签名，接口标识，MSGID等
	 * 2.消息正文就是MsgSendContent表中msgBody的数据
	 * 3.发送消息到MQ
	 * @param msr 消息记录对象
	 * @param msc 消息内容对象
	 * @return
	 */
	public Boolean sendToMq(MsgSendRecord msr,MsgSendContent msc);
	
	/**
	 * 发送消息到MQ后，接收SCM返回的反馈
	 * 通过反馈更新MsgSendRecord表中的信息，如feedbackTime
	 * @param content
	 * @return
	 */
	public void receiveFeedback(String content);
	
	/**
	 * 通过MQ接收SCM消息
	 * 保存到消息内容中
	 * 1.接收mq的消息进行base64解密,将json串转成消息对象  
	 * 2.验证消息签名等，如验证不通过，则抛弃消息，log文件中记录
	 * 3.验证通过则插入到MsgReceiveContent表中
	 * 4.无论是否重复都发送反馈给SCM
	 * @param content
	 * @return
	 */
	public String receiveFromScm(String content);
}
