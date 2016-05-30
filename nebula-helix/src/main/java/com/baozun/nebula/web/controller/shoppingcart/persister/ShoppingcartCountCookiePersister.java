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

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.baozun.nebula.sdk.command.shoppingcart.ShoppingCartLineCommand;
import com.baozun.nebula.web.constants.CookieKeyConstants;
import com.baozun.nebula.web.controller.shoppingcart.handler.ShoppingCartCountHandler;
import com.feilong.servlet.http.CookieUtil;

/**
 * 基于cookie保存的用户购物车数量持久化.
 *
 * @author feilong
 * @version 5.3.1 2016年5月6日 下午10:09:28
 * @since 5.3.1
 */
@Component("shoppingcartCountPersister")
public class ShoppingcartCountCookiePersister implements ShoppingcartCountPersister{

    /** cookie的名称. */
    private String                   cookieNameShoppingcartCount = CookieKeyConstants.SHOPPING_CART_COUNT;

    @Autowired
    private ShoppingCartCountHandler shoppingCartCountHandler;

    /*
     * (non-Javadoc)
     * 
     * @see com.baozun.nebula.web.controller.shoppingcart.resolver.persister.ShoppingcartCountPersister#save(java.util.List,
     * javax.servlet.http.HttpServletRequest, javax.servlet.http.HttpServletResponse)
     */
    @Override
    public void save(List<ShoppingCartLineCommand> shoppingCartLineCommandList,HttpServletRequest request,HttpServletResponse response){
        String count = "" + shoppingCartCountHandler.buildCount(shoppingCartLineCommandList);
        CookieUtil.addCookie(cookieNameShoppingcartCount, count, response);

    }

    /*
     * (non-Javadoc)
     * 
     * @see com.baozun.nebula.web.controller.shoppingcart.resolver.persister.ShoppingcartCountPersister#clear(javax.servlet.http.
     * HttpServletRequest, javax.servlet.http.HttpServletResponse)
     */
    @Override
    public void clear(HttpServletRequest request,HttpServletResponse response){
        CookieUtil.deleteCookie(cookieNameShoppingcartCount, response);
    }

    /**
     * 设置 cookie的名称.
     *
     * @param cookieNameShoppingcartCount
     *            the cookieNameShoppingcartCount to set
     */
    public void setCookieNameShoppingcartCount(String cookieNameShoppingcartCount){
        this.cookieNameShoppingcartCount = cookieNameShoppingcartCount;
    }
}
