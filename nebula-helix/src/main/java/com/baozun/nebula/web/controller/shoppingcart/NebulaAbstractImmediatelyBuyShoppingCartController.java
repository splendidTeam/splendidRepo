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

import org.apache.commons.lang3.Validate;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;

import com.baozun.nebula.web.controller.BaseController;
import com.baozun.nebula.web.controller.DefaultResultMessage;
import com.baozun.nebula.web.controller.DefaultReturnResult;
import com.baozun.nebula.web.controller.NebulaReturnResult;
import com.baozun.nebula.web.controller.shoppingcart.handler.ImmediatelyBuyCheckoutUrlHandler;
import com.feilong.accessor.AutoKeyAccessor;

/**
 * nebula 立即购买 base ShoppingCart Controller类.
 *
 * @author <a href="http://feitianbenyue.iteye.com/">feilong</a>
 * @since 5.3.1
 */
public abstract class NebulaAbstractImmediatelyBuyShoppingCartController extends BaseController{

    /** The Constant log. */
    private static final Logger              LOGGER = LoggerFactory.getLogger(NebulaAbstractImmediatelyBuyShoppingCartController.class);

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
        String immediatelyBuyCheckoutUrl = immediatelyBuyCheckoutUrlHandler.getImmediatelyBuyCheckoutUrl(key, request);
        Validate.notBlank(immediatelyBuyCheckoutUrl, "immediatelyBuyCheckoutUrl can't be blank!");
        LOGGER.debug("key:{},immediatelyBuyCheckoutUrl:{}", key, immediatelyBuyCheckoutUrl);
        return immediatelyBuyCheckoutUrl;
    }

    /**
     * To nebula return result.
     *
     * @param checkoutUrl
     *            the checkout url
     * @return the nebula return result
     */
    protected NebulaReturnResult toNebulaReturnResult(String checkoutUrl){
        DefaultReturnResult result = new DefaultReturnResult();
        result.setResult(true);
        DefaultResultMessage message = new DefaultResultMessage();
        message.setMessage(checkoutUrl);
        result.setResultMessage(message);
        return result;
    }

}
