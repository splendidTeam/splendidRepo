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
package com.baozun.nebula.web.controller.shoppingcart.persister;

import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.baozun.nebula.sdk.command.shoppingcart.ShoppingCartLineCommand;
import com.baozun.nebula.web.controller.shoppingcart.handler.ShoppingCartCountHandler;
import com.feilong.accessor.cookie.CookieAccessor;

/**
 * 基于cookie保存的用户购物车数量持久化.
 *
 * @author <a href="http://feitianbenyue.iteye.com/">feilong</a>
 * @since 5.3.1
 */
public class ShoppingcartCountCookiePersister implements ShoppingcartCountPersister{

    /** The shopping cart count handler. */
    private ShoppingCartCountHandler shoppingCartCountHandler;

    /** cookie寄存器. */
    private CookieAccessor           cookieAccessor;

    /*
     * (non-Javadoc)
     * 
     * @see com.baozun.nebula.web.controller.shoppingcart.resolver.persister.ShoppingcartCountPersister#save(java.util.List,
     * javax.servlet.http.HttpServletRequest, javax.servlet.http.HttpServletResponse)
     */
    @Override
    public void save(List<ShoppingCartLineCommand> shoppingCartLineCommandList,HttpServletRequest request,HttpServletResponse response){
        String count = "" + shoppingCartCountHandler.buildCount(shoppingCartLineCommandList);
        cookieAccessor.save(count, response);
    }

    /*
     * (non-Javadoc)
     * 
     * @see com.baozun.nebula.web.controller.shoppingcart.resolver.persister.ShoppingcartCountPersister#clear(javax.servlet.http.
     * HttpServletRequest, javax.servlet.http.HttpServletResponse)
     */
    @Override
    public void clear(HttpServletRequest request,HttpServletResponse response){
        cookieAccessor.remove(response);
    }

    /**
     * 设置 cookie寄存器.
     *
     * @param cookieAccessor
     *            the cookieAccessor to set
     */
    public void setCookieAccessor(CookieAccessor cookieAccessor){
        this.cookieAccessor = cookieAccessor;
    }

    /**
     * 设置 the shopping cart count handler.
     *
     * @param shoppingCartCountHandler
     *            the shoppingCartCountHandler to set
     */
    public void setShoppingCartCountHandler(ShoppingCartCountHandler shoppingCartCountHandler){
        this.shoppingCartCountHandler = shoppingCartCountHandler;
    }
}
