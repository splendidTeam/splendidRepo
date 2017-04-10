/**
 * 
 */
package com.baozun.nebula.web.command;

import com.baozun.nebula.sdk.command.OrderLineCommand;

/**
 * 用于记录对应订单行已完成的退货数
 * @author 黄金辉
 *
 */
public class SoReturnLineViewCommand {

	//订单行id
	private OrderLineCommand orderLineCommand;
	//已完成的退货数量
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
