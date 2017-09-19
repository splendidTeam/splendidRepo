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
package com.baozun.nebula.web.controller.order.resolver;

/**
 * 购物车操作的结果.
 *
 * @author jumbo
 * @author <a href="http://feitianbenyue.iteye.com/">feilong</a>
 */
public enum SalesOrderResult{

    /** 成功. */
    SUCCESS,

    //---------------------------------------------------------------------

    /** 基于 shoppingcart line 查找SHOPPING_CART_LINE_COMMAND 可能已经被删掉了 （比如 打开了双窗口）. */
    ORDER_SHOPPING_CART_LINE_COMMAND_NOT_FOUND,

    //---------------------------------------------------------------------

    /** 优惠券不可用. */
    ORDER_COUPON_NOT_AVALIBLE,

    //---------------------------------------------------------------------

    /**
     * 物流方式不可用.
     * 
     * @since 5.3.2.22
     */
    DELIVERY_TYPE_NOT_AVALIBLE,

    /**
     * 支付方式不可用.
     * 
     * @since 5.3.2.22
     */
    PAYMENT_TYPE_NOT_AVALIBLE,

}
