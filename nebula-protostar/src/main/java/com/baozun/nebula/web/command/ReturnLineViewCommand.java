/**
 * 
 */
package com.baozun.nebula.web.command;

import java.util.List;

import com.baozun.nebula.sdk.command.OrderLineCommand;
import com.baozun.nebula.sdk.command.SkuCommand;

/**
 * 
 * @author 黄金辉
 *
 */
public class ReturnLineViewCommand{

	//订单行id
	private OrderLineCommand orderLineCommand;
	//可提供换货的skuCommand集合
	private List<SkuCommand>  chgSkuCommandList;
	//剩余可退换货数量
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
	public List<SkuCommand> getChgSkuCommandList() {
		return chgSkuCommandList;
	}
	public void setChgSkuCommandList(List<SkuCommand> chgSkuCommandList) {
		this.chgSkuCommandList = chgSkuCommandList;
	}
	
}
