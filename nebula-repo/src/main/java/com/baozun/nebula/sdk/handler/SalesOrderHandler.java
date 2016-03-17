package com.baozun.nebula.sdk.handler;

import java.util.List;
import java.util.Map;

import com.baozun.nebula.model.salesorder.SalesOrder;
import com.baozun.nebula.sdk.command.SalesOrderCommand;
import com.baozun.nebula.sdk.command.shoppingcart.PromotionSKUDiscAMTBySetting;
import com.baozun.nebula.sdk.command.shoppingcart.ShopCartCommandByShop;
import com.baozun.nebula.sdk.command.shoppingcart.ShoppingCartLineCommand;

/**
 * 订单扩展接口点
 * 
 * @author lxy
 * @date
 * @version
 */
public interface SalesOrderHandler extends HandlerBase {

	/**
	 * 取消外部Coupon
	 * 
	 * @param code
	 * @param used
	 */
	public void updateOutCoupon(String code, Integer used);

	/**
	 * 下单*邮件所需数据（邮件所需基本数据dataMap已经有了，如果商城需要特殊的数据，请实现该接口，放入dataMap回传）
	 * 
	 * @param subOrdinate
	 * @param salesOrder
	 * @param salesOrderCommand
	 * @param sccList
	 * @param shopCartCommandByShop
	 * @param psdabsList
	 * @param dataMap
	 * @return dataMap 需返回构造好的数据
	 */
	public Map<String, Object> getEmailDataOfCreateOrder(String subOrdinate, SalesOrder salesOrder, SalesOrderCommand salesOrderCommand, List<ShoppingCartLineCommand> sccList, ShopCartCommandByShop shopCartCommandByShop,
			List<PromotionSKUDiscAMTBySetting> psdabsList, Map<String, Object> dataMap);

	/**
	 * 除下单之外的其他邮件 如取消订单 付款等
	 * 
	 * @param salesOrderCommand
	 * @param dataMap
	 * @return dataMap 需返回构造好的数据
	 */
	public Map<String, Object> getEmailData(SalesOrderCommand salesOrderCommand, Map<String, Object> dataMap, String emailTemplete);

}
