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

	void beforeOrderConfirm(ShoppingCartCommand shoppingCartCommand,MemberDetails memberDetails,HttpServletRequest request);
	
}
