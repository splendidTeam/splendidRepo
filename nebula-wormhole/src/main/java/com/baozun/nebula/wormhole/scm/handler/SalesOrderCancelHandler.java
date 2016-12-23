/**
 * @author <a href="mailto:yaohua.wang@baozun.cn">王耀华</a>
 * @version 2016-11-10
 */
package com.baozun.nebula.wormhole.scm.handler;

import java.util.List;

import com.baozun.nebula.sdk.command.SalesOrderCommand;

/**
 * @Description
 * @author  <a href="mailto:yaohua.wang@baozun.cn">王耀华</a>
 * @version 2016-11-10
 */
public interface SalesOrderCancelHandler {

	/**
	 * 
	 * @Description
	 * @param salesOrderCommandList
	 * @author <a href="mailto:yaohua.wang@baozun.cn">王耀华</a>
	 * @version 2016-11-10
	 */
	public void cancelSalesOrder(List<SalesOrderCommand> salesOrderCommandList);
	
	public List<SalesOrderCommand> findCustomCancelOrder(List<SalesOrderCommand> salesOrderCommandList);
}
