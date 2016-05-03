package com.baozun.nebula.manager.shoppingcart;

/**
 * 购物车操作的结果
 * 
 * @author jumbo
 *
 */
public enum ShoppingcartResult {

	/**
	 * 成功
	 */
	SUCCESS,

	/**
	 * sku不存在
	 */
	SKU_NOT_EXIST,

	/**
	 * sku不可用
	 */
	SKU_NOT_ENABLE,

	/**
	 * 商品状态不可用
	 */
	ITEM_STATUS_NOT_ENABLE,

	/**
	 * 没到可以购买的时间
	 */
	ITEM_NOT_ACTIVE_TIME,

	/**
	 * 商品是 gift 不可以操作
	 */
	ITEM_IS_GIFT,

	/**
	 * 购物车主卖行超过大小
	 */
	MAIN_LINE_MAX_THAN_COUNT,

	/**
	 * 超过库存数
	 */
	MAX_THAN_INVENTORY,

	// ******************修改*************************************************

	/**
	 * 基于 shoppingcart line 查找SHOPPING_CART_LINE_COMMAND 可能已经被删掉了 （比如 打开了双窗口）
	 */
	SHOPPING_CART_LINE_COMMAND_NOT_FOUND,
	
	/**
	 * 数据库层操作失败
	 */
	OPERATE_ERROR

}
