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
package com.baozun.nebula.web.controller.shoppingcart.builder.predicate;

import java.util.List;

import org.apache.commons.collections4.Predicate;

import com.baozun.nebula.sdk.command.shoppingcart.ShoppingCartLineCommand;
import com.baozun.nebula.web.controller.shoppingcart.validator.ShoppingCartInventoryValidator;

/**
 * 有库存的条件选择器.
 * 
 * @author <a href="http://feitianbenyue.iteye.com/">feilong</a>
 * @since 5.3.2.14
 */
public class HasInventoryShoppingCartLinePredicate implements Predicate<ShoppingCartLineCommand>{

    private List<ShoppingCartLineCommand> shoppingCartLineCommandList;

    /** The shopping cart inventory validator. */
    private ShoppingCartInventoryValidator shoppingCartInventoryValidator;

    //---------------------------------------------------------------------

    /**
     * @param shoppingCartLineCommandList
     * @param shoppingCartInventoryValidator
     */
    public HasInventoryShoppingCartLinePredicate(List<ShoppingCartLineCommand> shoppingCartLineCommandList, ShoppingCartInventoryValidator shoppingCartInventoryValidator){
        this.shoppingCartLineCommandList = shoppingCartLineCommandList;
        this.shoppingCartInventoryValidator = shoppingCartInventoryValidator;
    }

    //---------------------------------------------------------------------

    /*
     * (non-Javadoc)
     * 
     * @see org.apache.commons.collections4.Predicate#evaluate(java.lang.Object)
     */
    @Override
    public boolean evaluate(ShoppingCartLineCommand shoppingCartLineCommand){
        Long skuId = shoppingCartLineCommand.getSkuId();
        return !shoppingCartInventoryValidator.isMoreThanInventory(shoppingCartLineCommandList, skuId);
    }

}
