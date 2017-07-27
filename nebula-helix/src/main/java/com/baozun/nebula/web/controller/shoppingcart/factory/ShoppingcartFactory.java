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
package com.baozun.nebula.web.controller.shoppingcart.factory;

import java.util.List;

import javax.servlet.http.HttpServletRequest;

import com.baozun.nebula.sdk.command.shoppingcart.ShoppingCartLineCommand;
import com.baozun.nebula.web.MemberDetails;
import com.baozun.nebula.web.controller.shoppingcart.resolver.GuestShoppingcartResolver;
import com.baozun.nebula.web.controller.shoppingcart.resolver.MemberShoppingcartResolver;
import com.baozun.nebula.web.controller.shoppingcart.resolver.ShoppingcartResolver;

/**
 * 购物车工厂.
 *
 * @author <a href="http://feitianbenyue.iteye.com/">feilong</a>
 * @version 5.3.1 2016年5月22日 下午2:51:08
 * @see GuestShoppingcartResolver
 * @see MemberShoppingcartResolver
 * @since 5.3.1
 */
public interface ShoppingcartFactory{

    /**
     * 获得 shoppingcart resolver.
     *
     * @param memberDetails
     *            the member details
     * @return 如果 null== memberDetails,返回 {@link GuestShoppingcartResolver}; 否则返回 {@link MemberShoppingcartResolver}
     */
    ShoppingcartResolver getShoppingcartResolver(MemberDetails memberDetails);

    /**
     * 获得购物车行list.
     *
     * @param memberDetails
     *            the member details
     * @param request
     *            the request
     * @return 如果 null== memberDetails,将从 {@link GuestShoppingcartResolver}中取; 否则从 {@link MemberShoppingcartResolver}取
     * @see #getShoppingcartResolver(MemberDetails)
     */
    List<ShoppingCartLineCommand> getShoppingCartLineCommandList(MemberDetails memberDetails,HttpServletRequest request);

    /**
     * 获得 shopping cart line command list.
     *
     * @param memberDetails
     *            the member details
     * @param key
     *            如果key是null 或者 empty,那么就直接调用 {@link #getShoppingCartLineCommandList(MemberDetails, HttpServletRequest)}<br>
     *            如果key不是null或者empty,那么表示是立即购买途径
     * @param request
     *            the request
     * @return 如果key是null 或者 empty,那么就直接调用 {@link #getShoppingCartLineCommandList(MemberDetails, HttpServletRequest)}<br>
     *         如果key不是null或者empty,那么表示是立即购买途径,从寄存器中获取
     * @see #getShoppingCartLineCommandList(MemberDetails, HttpServletRequest)
     * @see #getShoppingcartResolver(MemberDetails)
     */
    List<ShoppingCartLineCommand> getShoppingCartLineCommandList(MemberDetails memberDetails,String key,HttpServletRequest request);
}
