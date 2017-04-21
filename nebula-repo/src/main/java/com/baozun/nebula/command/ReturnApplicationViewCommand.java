package com.baozun.nebula.command;

import java.util.List;

import com.baozun.nebula.command.ReturnApplicationCommand;
import com.baozun.nebula.sdk.command.OrderLineCommand;

public class ReturnApplicationViewCommand  {
	

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
