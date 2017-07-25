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
package com.baozun.nebula.web.controller.order.validator;

import javax.servlet.http.HttpServletRequest;

import com.baozun.nebula.sdk.command.shoppingcart.ShoppingCartCommand;
import com.baozun.nebula.web.controller.order.form.OrderForm;
import com.baozun.nebula.web.controller.order.resolver.SalesOrderResult;

/**
 * 购物车和优惠券促销验证的扩展器.
 * 
 * @author daibowen
 * @since 5.3.2.18
 */
public interface SalesOrderCreateValidatorHandler{

    /**
     * 执行 {@link SalesOrderCreateValidatorImpl#validate(ShoppingCartCommand, OrderForm, HttpServletRequest)}渲染之前.
     * 
     * <p>
     * 如果没有什么需要特殊处理的,实现需要返回SalesOrderResult.SUCCESS
     * </p>
     *
     * @param checkStatusShoppingCartCommand
     *            the check status shopping cart command
     * @param orderForm
     *            the order form
     * @param request
     *            the request
     * @return the sales order result
     * 
     * @since 5.3.2.22 change method params
     */
    SalesOrderResult preHandle(ShoppingCartCommand checkStatusShoppingCartCommand,OrderForm orderForm,HttpServletRequest request);

    /**
     * 执行 {@link SalesOrderCreateValidatorImpl#validate(ShoppingCartCommand, OrderForm, HttpServletRequest)}渲染之后.
     * 
     * <p>
     * 如果没有什么需要特殊处理的,实现需要返回SalesOrderResult.SUCCESS
     * </p>
     *
     * @param checkStatusShoppingCartCommand
     *            the check status shopping cart command
     * @param orderForm
     *            the order form
     * @param request
     *            the request
     * @return the sales order result
     * @since 5.3.2.22 change method params
     */
    SalesOrderResult postHandle(ShoppingCartCommand checkStatusShoppingCartCommand,OrderForm orderForm,HttpServletRequest request);
}
