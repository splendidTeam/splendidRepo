package com.baozun.nebula.wormhole.scm.makemsgcon;

import com.baozun.nebula.model.system.MsgSendContent;
import com.baozun.nebula.model.system.MsgSendRecord;

/**
 * 推送商品相关信息
 * 只保存到MsgSendContent表中,补偿机制会从表中取出发送
 * @author Justin Hu
 *
 */
public interface PropellingItemManager {

	/**
	 * 生成在售商品列表
	 * @param msr
	 */
	public MsgSendContent propellingOnSalesItems(MsgSendRecord msr);
}
