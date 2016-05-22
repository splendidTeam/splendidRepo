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
package com.baozun.nebula.web.controller.shoppingcart;

import java.util.List;

import javax.servlet.http.HttpServletRequest;

import com.baozun.nebula.sdk.command.shoppingcart.ShoppingCartLineCommand;
import com.baozun.nebula.web.MemberDetails;
import com.baozun.nebula.web.controller.shoppingcart.resolver.ShoppingcartResolver;

/**
 * 购物车工厂.
 *
 * @author feilong
 * @version 5.3.1 2016年5月22日 下午2:51:08
 * @since 5.3.1
 */
public interface ShoppingcartFactory{

    /**
     * 获得 shoppingcart resolver.
     *
     * @param memberDetails
     *            the member details
     * @return the shoppingcart resolver
     */
    ShoppingcartResolver getShoppingcartResolver(MemberDetails memberDetails);

    /**
     * 获得 shopping cart line command list.
     *
     * @param memberDetails
     *            the member details
     * @param request
     *            the request
     * @return the shopping cart line command list
     */
    List<ShoppingCartLineCommand> getShoppingCartLineCommandList(MemberDetails memberDetails,HttpServletRequest request);

    /**
     * 获得 shopping cart line command list.
     *
     * @param memberDetails
     *            the member details
     * @param key
     *            如果key是null 或者 empty,那么就直接调用 {@link #getShoppingCartLineCommandList(MemberDetails, HttpServletRequest)}
     * @param request
     *            the request
     * @return the shopping cart line command list
     */
    List<ShoppingCartLineCommand> getShoppingCartLineCommandList(MemberDetails memberDetails,String key,HttpServletRequest request);
}
