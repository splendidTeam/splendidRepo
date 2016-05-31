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
import java.util.Map;

import org.apache.commons.lang3.Validate;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.baozun.nebula.sdk.command.shoppingcart.ShoppingCartLineCommand;
import com.baozun.nebula.web.controller.shoppingcart.form.ImmediatelyBuyForm;

/**
 * The Class ImmediatelyBuyShoppingCartLineCommandListFactoryImpl.
 *
 * @author <a href="http://feitianbenyue.iteye.com/">feilong</a>
 * @since 5.3.1
 */
public class ImmediatelyBuyShoppingCartLineCommandListFactoryImpl implements ImmediatelyBuyShoppingCartLineCommandListFactory{

    /** The Constant LOGGER. */
    private static final Logger                LOGGER = LoggerFactory.getLogger(ImmediatelyBuyShoppingCartLineCommandListFactoryImpl.class);

    /** The immediately buy form and adaptor. */
    private Map<String, ImmediatelyBuyAdaptor> immediatelyBuyFormAndAdaptorMap;

    /*
     * (non-Javadoc)
     * 
     * @see com.baozun.nebula.web.controller.shoppingcart.factory.ImmediatelyBuyShoppingCartLineCommandListFactory#
     * buildShoppingCartLineCommandList(com.baozun.nebula.web.controller.shoppingcart.form.ImmediatelyBuyForm)
     */
    @Override
    public List<ShoppingCartLineCommand> buildShoppingCartLineCommandList(ImmediatelyBuyForm immediatelyBuyForm){
        Validate.notNull(immediatelyBuyForm, "immediatelyBuyForm can't be null!");

        ImmediatelyBuyAdaptor immediatelyBuyAdaptor = immediatelyBuyFormAndAdaptorMap.get(immediatelyBuyForm.getClass().getName());

        Validate.notNull(immediatelyBuyAdaptor, "immediatelyBuyAdaptor can't be null!");
        return immediatelyBuyAdaptor.buildShoppingCartLineCommandList(immediatelyBuyForm);
    }

    /**
     * 设置 the immediately buy form and adaptor.
     *
     * @param immediatelyBuyFormAndAdaptorMap
     *            the immediatelyBuyFormAndAdaptorMap to set
     */
    public void setImmediatelyBuyFormAndAdaptorMap(Map<String, ImmediatelyBuyAdaptor> immediatelyBuyFormAndAdaptorMap){
        this.immediatelyBuyFormAndAdaptorMap = immediatelyBuyFormAndAdaptorMap;
    }

}
