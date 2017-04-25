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
package com.baozun.nebula.web.controller.shoppingcart.handler;

import java.util.List;

import javax.servlet.http.HttpServletRequest;

import com.baozun.nebula.sdk.command.shoppingcart.CalcFreightCommand;
import com.baozun.nebula.sdk.command.shoppingcart.ShoppingCartLineCommand;
import com.baozun.nebula.web.MemberDetails;

/**
 * 渲染选中的购物车行的扩展器.
 * 
 * @author <a href="http://feitianbenyue.iteye.com/">feilong</a>
 * @since 5.3.2.15
 */
public interface ShoppingCartCommandCheckedBuilderHandler{

    /**
     * 执行 {@link com.baozun.nebula.sdk.manager.shoppingcart.SdkShoppingCartCommandBuilder#buildShoppingCartCommand(Long, List, CalcFreightCommand, List, java.util.Set)}渲染之前.
     *
     * <p>
     * 如果没有什么需要特殊处理的,实现可以不写任何代码
     * </p>
     * 
     * @param memberDetails
     * @param checkedShoppingCartLineCommandList
     * @param calcFreightCommand
     * @param couponList
     * @param request
     */
    void preHandle(//
                    MemberDetails memberDetails,
                    List<ShoppingCartLineCommand> checkedShoppingCartLineCommandList,
                    CalcFreightCommand calcFreightCommand,
                    List<String> couponList,
                    HttpServletRequest request);

    /**
     * 
     * 执行 {@link com.baozun.nebula.sdk.manager.shoppingcart.SdkShoppingCartCommandBuilder#buildShoppingCartCommand(Long, List, CalcFreightCommand, List, java.util.Set)}渲染之后.
     * 
     * <p>
     * 如果没有什么需要特殊处理的,实现可以不写任何代码
     * </p>
     * 
     * @param memberDetails
     * @param checkedShoppingCartLineCommandList
     * @param calcFreightCommand
     * @param couponList
     * @param request
     */
    void postHandle(//
                    MemberDetails memberDetails,
                    List<ShoppingCartLineCommand> checkedShoppingCartLineCommandList,
                    CalcFreightCommand calcFreightCommand,
                    List<String> couponList,
                    HttpServletRequest request);
}
