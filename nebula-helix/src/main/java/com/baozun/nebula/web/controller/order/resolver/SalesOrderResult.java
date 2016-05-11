package com.baozun.nebula.web.controller.order.resolver;

/**
 * 购物车操作的结果
 * 
 * @author jumbo
 *
 */
public enum SalesOrderResult {

	/**
	 * 成功
	 */
	SUCCESS,

	/**
	 * 基于 shoppingcart line 查找SHOPPING_CART_LINE_COMMAND 可能已经被删掉了 （比如 打开了双窗口）
	 */
	ORDER_SHOPPING_CART_LINE_COMMAND_NOT_FOUND,
	
	/**
	 * 优惠券不可用
	 */
	ORDER_COUPON_NOT_AVALIBLE

}
