package com.baozun.nebula.payment.convert;

import com.baozun.nebula.sdk.command.SalesOrderCommand;
import com.baozun.nebula.utilities.common.command.BasePayParamCommandAdaptor;

public interface PayParamCommandAdaptor extends BasePayParamCommandAdaptor {

	public SalesOrderCommand getSalesOrderCommand();

	public void setSalesOrderCommand(SalesOrderCommand salesOrderCommand);

	
}
