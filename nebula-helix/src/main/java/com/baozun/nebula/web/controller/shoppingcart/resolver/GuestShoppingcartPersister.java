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
package com.baozun.nebula.web.controller.shoppingcart.resolver;

import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.baozun.nebula.sdk.command.shoppingcart.ShoppingCartLineCommand;

/**
 * 游客购物车 持久化.
 *
 * @author feilong
 * @version 5.3.1 2016年5月3日 下午4:32:39
 * @since 5.3.1
 */
public interface GuestShoppingcartPersister{

    /**
     * 加载.
     *
     * @param request
     *            the request
     * @return the shopping cart line command list
     */
    List<ShoppingCartLineCommand> load(HttpServletRequest request);

    /**
     * 保存.
     *
     * @param shoppingCartLineCommandList
     *            the need change checked command list
     * @param request
     *            the request
     * @param response
     *            the response
     */
    void save(List<ShoppingCartLineCommand> shoppingCartLineCommandList,HttpServletRequest request,HttpServletResponse response);
}
