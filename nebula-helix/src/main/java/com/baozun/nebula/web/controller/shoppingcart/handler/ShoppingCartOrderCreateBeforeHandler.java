/**
 * @author <a href="mailto:yaohua.wang@baozun.cn">王耀华</a>
 * @version 2016-10-9
 */
package com.baozun.nebula.web.controller.shoppingcart.handler;

import javax.servlet.http.HttpServletRequest;

import com.baozun.nebula.sdk.command.shoppingcart.ShoppingCartCommand;
import com.baozun.nebula.web.MemberDetails;
import com.baozun.nebula.web.controller.order.form.OrderForm;

/**
 * @Description
 * @author <a href="mailto:yaohua.wang@baozun.cn">王耀华</a>
 * @version 2016-10-9
 */
public interface ShoppingCartOrderCreateBeforeHandler {

	/**
	 * 
	 * @Description 创建订单之前,自行扩展点.
	 * @param shoppingCartCommand
	 * @param orderForm
	 * @param memberDetails
	 * @param request
	 * @param key
	 * @author <a href="mailto:yaohua.wang@baozun.cn">王耀华</a>
	 * @version 2016-10-9
	 */
	void beforeCreateSalesOrder(ShoppingCartCommand shoppingCartCommand, OrderForm orderForm, MemberDetails memberDetails, HttpServletRequest request, String key);

}
