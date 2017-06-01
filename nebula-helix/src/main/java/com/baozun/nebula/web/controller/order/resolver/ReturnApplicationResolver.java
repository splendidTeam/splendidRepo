package com.baozun.nebula.web.controller.order.resolver;

import java.util.List;

import com.baozun.nebula.command.ReturnApplicationCommand;
import com.baozun.nebula.command.ReturnLineViewCommand;
import com.baozun.nebula.sdk.command.SalesOrderCommand;
import com.baozun.nebula.web.MemberDetails;
import com.baozun.nebula.web.controller.order.form.ReturnOrderForm;

public interface ReturnApplicationResolver {
	
	  ReturnApplicationCommand toReturnApplicationCommand(MemberDetails memberDetails,
														  ReturnOrderForm returnOrderForm,SalesOrderCommand salesOrder);
	  
	   List<ReturnLineViewCommand> toReturnLineViewCommand(List<Long> orderLineIds);
}
