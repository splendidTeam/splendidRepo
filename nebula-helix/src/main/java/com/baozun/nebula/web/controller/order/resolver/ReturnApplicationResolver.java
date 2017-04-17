package com.baozun.nebula.web.controller.order.resolver;

import java.util.List;

import com.baozun.nebula.command.ReturnApplicationCommand;
import com.baozun.nebula.model.salesorder.SoReturnApplicationDeliveryInfo;
import com.baozun.nebula.sdk.command.SalesOrderCommand;
import com.baozun.nebula.web.MemberDetails;
import com.baozun.nebula.web.controller.order.form.ReturnOderForm;
import com.baozun.nebula.web.controller.order.viewcommand.ReturnApplicationViewCommand;
import com.baozun.nebula.web.controller.order.viewcommand.ReturnLineViewCommand;

public interface ReturnApplicationResolver {
	
	  ReturnApplicationCommand toReturnApplicationCommand(MemberDetails memberDetails,
														  ReturnOderForm returnOrderForm,SalesOrderCommand salesOrder);
	  
	  List<ReturnLineViewCommand> toReturnLineViewCommand(List<Long> orderLineIds);
	  
	  List<ReturnApplicationViewCommand>  toTurnReturnApplicationViewCommand(List<ReturnApplicationCommand>  returnApplications);
	  //处理returnDelivery对象
	  SoReturnApplicationDeliveryInfo  toReturnApplicationDelivery(ReturnOderForm returnOrderForm);

}
