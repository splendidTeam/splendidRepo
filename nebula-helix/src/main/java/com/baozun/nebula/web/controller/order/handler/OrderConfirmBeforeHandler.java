/**
 * @author <a href="mailto:yaohua.wang@baozun.cn">王耀华</a>
 * @version 2016-10-20
 */
package com.baozun.nebula.web.controller.order.handler;

import javax.servlet.http.HttpServletRequest;

import com.baozun.nebula.sdk.command.shoppingcart.ShoppingCartCommand;
import com.baozun.nebula.web.MemberDetails;

/**
 * @Description
 * @author  <a href="mailto:yaohua.wang@baozun.cn">王耀华</a>
 * @version 2016-10-20
 */
public interface OrderConfirmBeforeHandler {

	/**
	 * 
	 * @Description 进入Checkout之前,自行扩展点.
	 * @param shoppingCartCommand
	 * @param memberDetails
	 * @param request
	 * @param key
	 * @author <a href="mailto:yaohua.wang@baozun.cn">王耀华</a>
	 * @version 2016-10-9
	 */
	void beforeOrderConfirm(ShoppingCartCommand shoppingCartCommand,MemberDetails memberDetails,HttpServletRequest request, String key);
	
}
