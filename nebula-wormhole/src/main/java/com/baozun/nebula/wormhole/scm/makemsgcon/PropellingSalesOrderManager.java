package com.baozun.nebula.wormhole.scm.makemsgcon;

import com.baozun.nebula.model.system.MsgSendContent;
import com.baozun.nebula.model.system.MsgSendRecord;

/**
 * 推送订单相关信息
 * 只保存到MsgSendContent表中,补偿机制会从表中取出发送
 * @author Justin Hu
 *
 */
public interface PropellingSalesOrderManager {

	/**
	 * 生成订单消息内容，加密后，保存到MsgSendContent表中
	 * @param msr
	 */
	public MsgSendContent propellingSalesOrder(MsgSendRecord msr);
	
	
	/**
	 * 生成支付消息内容，加密后，保存到MsgSendContent表中
	 * @param msr
	 */
	public MsgSendContent propellingPayment(MsgSendRecord msr);
	
	
	/**
	 * 生成订单变更消息内容，加密后，保存到MsgSendContent表中
	 * @param msr
	 */
	public MsgSendContent propellingSoStatus(MsgSendRecord msr);
}
