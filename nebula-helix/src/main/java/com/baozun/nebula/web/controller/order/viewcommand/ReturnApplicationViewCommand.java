package com.baozun.nebula.web.controller.order.viewcommand;

import java.util.List;

import com.baozun.nebula.command.ReturnApplicationCommand;
import com.baozun.nebula.sdk.command.OrderLineCommand;
import com.baozun.nebula.web.controller.BaseViewCommand;

public class ReturnApplicationViewCommand  extends BaseViewCommand{
	
	/**
	 * 
	 */
	private static final long serialVersionUID = -2389091935575015185L;

	private ReturnApplicationCommand returnApplicationCommand;
	
	private List<OrderLineCommand>  orderLineCommands ;

	public ReturnApplicationCommand getReturnApplicationCommand() {
		return returnApplicationCommand;
	}

	public void setReturnApplicationCommand(
			ReturnApplicationCommand returnApplicationCommand) {
		this.returnApplicationCommand = returnApplicationCommand;
	}

	public List<OrderLineCommand> getOrderLineCommands() {
		return orderLineCommands;
	}

	public void setOrderLineCommands(List<OrderLineCommand> orderLineCommands) {
		this.orderLineCommands = orderLineCommands;
	}
	

}
