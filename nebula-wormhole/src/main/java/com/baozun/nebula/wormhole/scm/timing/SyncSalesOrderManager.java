package com.baozun.nebula.wormhole.scm.timing;

import java.util.List;

import com.baozun.nebula.wormhole.mq.entity.order.OrderStatusV5;

/**
 * 定时取出MsgReceiveContent表订单信息接口相关未处理的数据
 * 进行处理
 * @author Justin Hu
 *
 */
public interface SyncSalesOrderManager {

	/**
	 * 同步订单状态
	 * scm推送订单状态到商城
	 */
	public void syncSoStatus();
	
	/**
	 * 获取已经从oms推送过来并且没有同步到商城的记录
	 * @return
	 */
	public List<OrderStatusV5> getNotHandledSoOrder();
}
