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
package com.baozun.nebula.web.controller.shoppingcart.builder;

import java.util.List;

import javax.servlet.http.HttpServletRequest;

import com.baozun.nebula.sdk.command.shoppingcart.CalcFreightCommand;
import com.baozun.nebula.sdk.command.shoppingcart.ShoppingCartCommand;
import com.baozun.nebula.sdk.command.shoppingcart.ShoppingCartLineCommand;
import com.baozun.nebula.web.MemberDetails;
import com.baozun.nebula.web.controller.order.form.OrderForm;

/**
 * The Interface ShoppingCartCommandBuilder.
 *
 * @author <a href="http://feitianbenyue.iteye.com/">feilong</a>
 * @version 5.3.1 2016年5月24日 下午4:56:16
 * @since 5.3.1
 */
public interface ShoppingCartCommandBuilder{

    /**
     * Builds the shopping cart command.
     *
     * @param memberDetails
     *            the member details
     * @param shoppingCartLines
     *            the shopping cart lines
     * @return 如果 <code>shoppingCartLines</code> 是null或者 empty,返回 null
     * @see com.baozun.nebula.sdk.manager.shoppingcart.SdkShoppingCartCommandBuilder#buildShoppingCartCommand(Long, List,
     *      com.baozun.nebula.sdk.command.shoppingcart.CalcFreightCommand, List, java.util.Set)
     */
    ShoppingCartCommand buildShoppingCartCommand(MemberDetails memberDetails,List<ShoppingCartLineCommand> shoppingCartLines);

    /**
     * Builds the shopping cart command.
     *
     * @param memberDetails
     *            the member details
     * @param shoppingCartLines
     *            the shopping cart lines
     * @param calcFreightCommand
     *            the calc freight command
     * @param coupons
     *            the coupons
     * @return 如果 <code>shoppingCartLines</code> 是null或者 empty,返回 null
     */
    ShoppingCartCommand buildShoppingCartCommand(MemberDetails memberDetails,List<ShoppingCartLineCommand> shoppingCartLines,CalcFreightCommand calcFreightCommand,List<String> coupons);

    //------------------------------------------------------------------
    /**
     * 基于 calcFreightCommand couponList,来构造选择状态的购物车信息.
     * 
     * <p>
     * 常用于订单确认页面
     * </p>
     * 
     * @param memberDetails
     * @param key
     * @param calcFreightCommand
     * @param couponList
     * @param request
     * @return 如果没有选择状态的购物车信息,将会抛出异常 {@link NullPointerException}
     * @since 5.3.2.15
     */
    ShoppingCartCommand buildShoppingCartCommandWithCheckStatus(MemberDetails memberDetails,String key,CalcFreightCommand calcFreightCommand,List<String> couponList,HttpServletRequest request);

    /**
     * 基于 orderForm 来构造选择状态的购物车信息.
     * 
     * <p>
     * 常用于订单确认页面
     * </p>
     * 
     * @param memberDetails
     * @param key
     * @param orderForm
     * @param request
     * @return 如果没有选择状态的购物车信息,将会抛出异常 {@link NullPointerException}
     * @since 5.3.2.15
     */
    ShoppingCartCommand buildShoppingCartCommandWithCheckStatus(MemberDetails memberDetails,String key,OrderForm orderForm,HttpServletRequest request);
}
