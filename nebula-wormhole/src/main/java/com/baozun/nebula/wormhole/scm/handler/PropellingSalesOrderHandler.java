package com.baozun.nebula.wormhole.scm.handler;

import com.baozun.nebula.sdk.command.SalesOrderCommand;
import com.baozun.nebula.wormhole.mq.entity.order.OrderStatusV5;
import com.baozun.nebula.wormhole.mq.entity.order.SalesOrderV5;
import com.baozun.nebula.wormhole.mq.entity.pay.PaymentInfoV5;

/**
 * 订单推送相关的handler
 * @author Justin Hu
 *
 */
public interface PropellingSalesOrderHandler extends HandlerBase {

	
	/**
	 * 订单信息的扩展点
	 * @param orderId 订单id
	 */
	public SalesOrderV5 propellingSalesOrder(SalesOrderV5 so,SalesOrderCommand soc);
	
	
	/**
	 * 支付信息的扩展点
	 * @param payId
	 */
	public PaymentInfoV5 propellingPayment(PaymentInfoV5 pi);
	
	
	/**
	 * 订单状态变更的的扩展点
	 * @param orderId
	 */
	public OrderStatusV5 propellingSoStatus(OrderStatusV5 os);
}
