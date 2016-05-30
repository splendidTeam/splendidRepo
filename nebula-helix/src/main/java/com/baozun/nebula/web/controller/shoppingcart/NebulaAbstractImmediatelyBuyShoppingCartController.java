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

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;

import com.baozun.nebula.web.controller.BaseController;
import com.baozun.nebula.web.controller.shoppingcart.handler.ImmediatelyBuyCheckoutUrlHandler;
import com.feilong.accessor.AutoKeyAccessor;

/**
 * nebula 立即购买 base ShoppingCart Controller类.
 *
 * @author feilong
 * @since 5.3.1
 */
public abstract class NebulaAbstractImmediatelyBuyShoppingCartController extends BaseController{

    /** The auto key accessor. */
    @Autowired
    @Qualifier("immediatelyBuyAutoKeyAccessor")
    protected AutoKeyAccessor                autoKeyAccessor;

    /** The immediately buy checkout url handler. */
    @Autowired
    private ImmediatelyBuyCheckoutUrlHandler immediatelyBuyCheckoutUrlHandler;

    /**
     * 获得立即购买的地址.
     *
     * @param key
     *            the key
     * @param request
     *            the request
     * @return the immediately buy checkout url
     */
    protected String getImmediatelyBuyCheckoutUrl(String key,HttpServletRequest request){
        return immediatelyBuyCheckoutUrlHandler.getImmediatelyBuyCheckoutUrl(key, request);
    }

}
