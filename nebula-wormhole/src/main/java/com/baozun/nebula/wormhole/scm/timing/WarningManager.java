package com.baozun.nebula.wormhole.scm.timing;

public interface WarningManager {

	/**
	 * 24小时已付款的订单状态没有发生过改变(指的是scm同步过来的状态)
	 */
	void payOrderNoChangStatus();
	
	/**
	 * 非取消状态的订单10天还没有变更为交易完成状态，发告警邮件
	 */
	void notCancelOrderNoFinish();
	
	/**
	 * 全量库存同步延迟
	 * 如48小时还未发现全量库存同步
	 */
	void fullInventorySyncLater();
}
