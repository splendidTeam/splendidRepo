/**
 * 
 */
package com.baozun.nebula.web.controller.order.viewcommand;

import com.baozun.nebula.sdk.command.OrderLineCommand;
import com.baozun.nebula.web.controller.BaseViewCommand;

/**
 * 用于记录对应订单行已完成的退货数
 * @author 黄金辉
 *
 */
public class ReturnLineViewCommand extends BaseViewCommand{

	/**
	 * 
	 */
	private static final long serialVersionUID = 3374821107740450392L;
	//订单行id
	private OrderLineCommand orderLineCommand;
	//剩余可退货数量
	private Integer count;
	/**
	 * @return the orderLineCommand
	 */
	public OrderLineCommand getOrderLineCommand() {
		return orderLineCommand;
	}
	/**
	 * @param orderLineCommand the orderLineCommand to set
	 */
	public void setOrderLineCommand(OrderLineCommand orderLineCommand) {
		this.orderLineCommand = orderLineCommand;
	}
	/**
	 * @return the count
	 */
	public Integer getCount() {
		return count;
	}
	/**
	 * @param count the count to set
	 */
	public void setCount(Integer count) {
		this.count = count;
	}
	
	
	
	
	
	
}
