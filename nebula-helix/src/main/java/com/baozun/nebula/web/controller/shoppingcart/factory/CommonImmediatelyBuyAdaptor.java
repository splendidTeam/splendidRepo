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

import com.baozun.nebula.sdk.command.shoppingcart.ShoppingCartLineCommand;
import com.baozun.nebula.web.controller.shoppingcart.form.CommonImmediatelyBuyForm;
import com.baozun.nebula.web.controller.shoppingcart.form.ImmediatelyBuyForm;

/**
 * The Class CommonImmediatelyBuyAdaptor.
 *
 * @author <a href="http://feitianbenyue.iteye.com/">feilong</a>
 * @since 5.3.1
 */
public class CommonImmediatelyBuyAdaptor extends OneShoppingCartLineCommandImmediatelyBuyAdaptor{

    @Override
    protected ShoppingCartLineCommand buildShoppingCartLineCommand(ImmediatelyBuyForm immediatelyBuyForm){
        CommonImmediatelyBuyForm commonImmediatelyBuyForm = (CommonImmediatelyBuyForm) immediatelyBuyForm;
        ShoppingCartLineCommand shoppingCartLineCommand = new ShoppingCartLineCommand();

        shoppingCartLineCommand.setSkuId(commonImmediatelyBuyForm.getSkuId());
        shoppingCartLineCommand.setQuantity(commonImmediatelyBuyForm.getCount());

        //选中
        shoppingCartLineCommand.setSettlementState(1);
        return shoppingCartLineCommand;
    }
}
