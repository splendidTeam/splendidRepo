package com.baozun.nebula.web.controller.order.resolver;

import com.baozun.nebula.command.ReturnApplicationCommand;
import com.baozun.nebula.sdk.command.SalesOrderCommand;
import com.baozun.nebula.web.MemberDetails;
import com.baozun.nebula.web.controller.order.form.ReturnOderForm;

public interface ReturnApplicationResolver {
	
	  ReturnApplicationCommand toReturnApplicationCommand(MemberDetails memberDetails,ReturnOderForm returnOrderForm,SalesOrderCommand salesOrder);

}
