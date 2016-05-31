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

import org.apache.commons.lang3.Validate;

import com.baozun.nebula.sdk.command.shoppingcart.ShoppingCartLineCommand;
import com.baozun.nebula.web.controller.shoppingcart.form.ImmediatelyBuyForm;
import com.feilong.core.bean.ConvertUtil;

/**
 * The Class OneShoppingCartLineCommandImmediatelyBuyAdaptor.
 *
 * @author <a href="http://feitianbenyue.iteye.com/">feilong</a>
 * @since 5.3.1
 */
public abstract class OneShoppingCartLineCommandImmediatelyBuyAdaptor implements ImmediatelyBuyAdaptor{

    /*
     * (non-Javadoc)
     * 
     * @see
     * com.baozun.nebula.web.controller.shoppingcart.factory.ImmediatelyBuyAdaptor#buildShoppingCartLineCommandList(com.baozun.nebula.web.
     * controller.shoppingcart.form.ImmediatelyBuyForm)
     */
    @Override
    public List<ShoppingCartLineCommand> buildShoppingCartLineCommandList(ImmediatelyBuyForm immediatelyBuyForm){
        Validate.notNull(immediatelyBuyForm, "immediatelyBuyForm can't be null!");

        ShoppingCartLineCommand shoppingCartLineCommand = buildShoppingCartLineCommand(immediatelyBuyForm);
        Validate.notNull(shoppingCartLineCommand, "shoppingCartLineCommand can't be null!");

        return ConvertUtil.toList(shoppingCartLineCommand);
    }

    /**
     * Builds the shopping cart line command.
     *
     * @param immediatelyBuyForm
     *            the immediately buy form
     * @return the shopping cart line command
     */
    protected abstract ShoppingCartLineCommand buildShoppingCartLineCommand(ImmediatelyBuyForm immediatelyBuyForm);

}
