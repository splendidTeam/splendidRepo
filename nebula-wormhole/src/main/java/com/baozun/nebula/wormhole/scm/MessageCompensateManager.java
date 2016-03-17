package com.baozun.nebula.wormhole.scm;

/**
 * 消息补偿机制
 * @author Justin Hu
 *
 */
public interface MessageCompensateManager {

	/**
	 * 消息补偿机制(定时调用)
	 * 发送次数=0时，表示新发送，需要生成对应的消息发送内容后，再发到mq, 发送次数加1
	 * 发送次数>0 且 <5 且 消息反馈时间==null 且 now-sendTime>半小时,重发消息内容，发送次数加1 
     * 发送次数>5时，此时需要发送告警邮件
	 * 定时间隔定义为五分钟。
	 */
	void timeMessageCompensate();
}
