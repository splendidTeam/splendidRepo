/**
 * Copyright (c) 2010 Jumbomart All Rights Reserved.
 *
 * This software is the confidential and proprietary information of Jumbomart.
 * You shall not disclose such Confidential Information and shall use it only in
 * accordance with the terms of the license agreement you entered into
 * with Jumbo.
 *
 * JUMBOMART MAKES NO REPRESENTATIONS OR WARRANTIES ABOUT THE SUITABILITY OF THE
 * SOFTWARE, EITHER EXPRESS OR IMPLIED, INCLUDING BUT NOT LIMITED TO THE
 * IMPLIED WARRANTIES OF MERCHANTABILITY, FITNESS FOR A PARTICULAR
 * PURPOSE, OR NON-INFRINGEMENT. JUMBOMART SHALL NOT BE LIABLE FOR ANY DAMAGES
 * SUFFERED BY LICENSEE AS A RESULT OF USING, MODIFYING OR DISTRIBUTING
 * THIS SOFTWARE OR ITS DERIVATIVES.
 *
 */
package com.baozun.nebula.sdk.handler;

import java.util.List;
import java.util.Map;

import com.baozun.nebula.model.salesorder.SalesOrder;
import com.baozun.nebula.sdk.command.SalesOrderCommand;
import com.baozun.nebula.sdk.command.shoppingcart.PromotionSKUDiscAMTBySetting;
import com.baozun.nebula.sdk.command.shoppingcart.ShopCartCommandByShop;
import com.baozun.nebula.sdk.command.shoppingcart.ShoppingCartLineCommand;

/**
 * 订单扩展接口点.
 *
 * @author lxy
 * @version
 * @date
 */
public interface SalesOrderHandler extends HandlerBase{

    /**
     * 取消外部Coupon.
     *
     * @param code
     *            the code
     * @param used
     *            the used
     */
    void updateOutCoupon(String code,Integer used);

    /**
     * 下单*邮件所需数据（邮件所需基本数据dataMap已经有了，如果商城需要特殊的数据，请实现该接口，放入dataMap回传）.
     *
     * @param subOrdinate
     *            the sub ordinate
     * @param salesOrder
     *            the sales order
     * @param salesOrderCommand
     *            the sales order command
     * @param sccList
     *            the scc list
     * @param shopCartCommandByShop
     *            the shop cart command by shop
     * @param psdabsList
     *            the psdabs list
     * @param dataMap
     *            the data map
     * @return dataMap 需返回构造好的数据
     */
    Map<String, Object> getEmailDataOfCreateOrder(
                    String subOrdinate,
                    SalesOrder salesOrder,
                    SalesOrderCommand salesOrderCommand,
                    List<ShoppingCartLineCommand> sccList,
                    ShopCartCommandByShop shopCartCommandByShop,
                    List<PromotionSKUDiscAMTBySetting> psdabsList,
                    Map<String, Object> dataMap);

    /**
     * 除下单之外的其他邮件 如取消订单 付款等.
     * 
     * <p>
     * 注意:如果返回 null or empty ,将不会发送邮件
     * </p>
     *
     * @param salesOrderCommand
     *            the sales order command
     * @param dataMap
     *            the data map
     * @param emailTemplete
     *            the email templete
     * @return dataMap 需返回构造好的数据
     */
    Map<String, Object> getEmailData(SalesOrderCommand salesOrderCommand,Map<String, Object> dataMap,String emailTemplete);

}
