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
import javax.servlet.http.HttpServletResponse;

import com.baozun.nebula.web.MemberDetails;
import com.baozun.nebula.web.controller.shoppingcart.viewcommand.ShoppingCartViewCommand;

/**
 * 将状态不对的 选中状态的订单行 变成不选中.
 * 
 * @author <a href="http://feitianbenyue.iteye.com/">feilong</a>
 * @since 5.3.2.6
 */
public interface UncheckedInvalidStateShoppingCartLineHandler{

    /**
     * 将状态不对的 选中状态的订单行 变成不选中.
     * 
     * <h3>说明:</h3>
     * <blockquote>
     * <ol>
     * <li>如果 shoppingCartViewCommand 是null,什么都不做</li>
     * <li>提取所有的 ShoppingCartLineSubViewCommand list,如果是null或者empty 什么都不做</li>
     * <li>找出list中,状态是不正常,且选中状态 非礼品的行,提取 id list</li>
     * <li>将这些 id list,设置为 不选中</li>
     * </ol>
     * </blockquote>
     *
     * @param memberDetails
     *            the member details
     * @param shoppingCartViewCommand
     *            the shopping cart view command
     * @param request
     * @param response
     * @see com.baozun.nebula.web.controller.shoppingcart.resolver.ShoppingcartResolver#uncheckShoppingCartLines(MemberDetails, List, boolean, HttpServletRequest, HttpServletResponse)
     * @since 5.3.2.6
     */
    void uncheckedInvalidStateShoppingCartLine(MemberDetails memberDetails,ShoppingCartViewCommand shoppingCartViewCommand,HttpServletRequest request,HttpServletResponse response);
}
