package com.baozun.nebula.payment.convert;

import java.util.Map;

import com.baozun.nebula.sdk.command.SalesOrderCommand;
import com.baozun.nebula.utilities.common.command.BasePayParamCommandAdaptor;

public interface PayParamCommandAdaptor extends BasePayParamCommandAdaptor {

	public SalesOrderCommand getSalesOrderCommand();

	public void setSalesOrderCommand(SalesOrderCommand salesOrderCommand);

	/**
	 * @Description
	 * @param requestParams
	 * @author <a href="mailto:yaohua.wang@baozun.cn">王耀华</a>
	 * @version 2016-11-16
	 */
	void setRequestParams(Map<String, Object> requestParams);

}
