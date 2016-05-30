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
import com.baozun.nebula.web.controller.shoppingcart.form.BundleImmediatelyBuyForm;
import com.baozun.nebula.web.controller.shoppingcart.form.ImmediatelyBuyForm;

public class BundleImmediatelyBuyAdaptor extends OneShoppingCartLineCommandImmediatelyBuyAdaptor{

    @Override
    protected ShoppingCartLineCommand buildShoppingCartLineCommand(ImmediatelyBuyForm immediatelyBuyForm){
        BundleImmediatelyBuyForm immediatelyBuyBundleForm = (BundleImmediatelyBuyForm) immediatelyBuyForm;

        Long relatedItemId = immediatelyBuyBundleForm.getRelatedItemId();
        Long[] skuIds = immediatelyBuyBundleForm.getSkuIds();
        Integer count = immediatelyBuyBundleForm.getCount();

        ShoppingCartLineCommand shoppingCartLineCommand = new ShoppingCartLineCommand();

        shoppingCartLineCommand.setRelatedItemId(relatedItemId);
        shoppingCartLineCommand.setSkuIds(skuIds);
        shoppingCartLineCommand.setQuantity(count);

        //这里有促销判断逻辑
        //see com.baozun.nebula.sdk.manager.impl.SdkShoppingCartManagerImpl.needContainsLineCalc(Integer, boolean)
        shoppingCartLineCommand.setSettlementState(1);
        shoppingCartLineCommand.setValid(true);
        return shoppingCartLineCommand;
    }
}
