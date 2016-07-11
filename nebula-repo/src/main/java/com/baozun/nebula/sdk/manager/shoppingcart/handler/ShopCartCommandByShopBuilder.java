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
package com.baozun.nebula.sdk.manager.shoppingcart.handler;

import java.util.List;

import com.baozun.nebula.sdk.command.shoppingcart.CalcFreightCommand;
import com.baozun.nebula.sdk.command.shoppingcart.PromotionBrief;
import com.baozun.nebula.sdk.command.shoppingcart.ShopCartCommandByShop;
import com.baozun.nebula.sdk.command.shoppingcart.ShoppingCartCommand;

/**
 * The Interface ShopCartCommandByShopBuilder.
 *
 * @author <a href="http://feitianbenyue.iteye.com/">feilong</a>
 * @since 5.3.1.6
 */
public interface ShopCartCommandByShopBuilder{

    /**
     * Builds the.
     *
     * @param shopId
     *            the shop id
     * @param shoppingCartCommand
     *            the shopping cart command
     * @param calcFreightCommand
     *            the calc freight command
     * @return the shop cart command by shop
     */
    ShopCartCommandByShop build(Long shopId,ShoppingCartCommand shoppingCartCommand,CalcFreightCommand calcFreightCommand);

    ShopCartCommandByShop build(
                    ShoppingCartCommand inputShoppingCartCommand,
                    CalcFreightCommand calcFreightCommand,
                    List<PromotionBrief> promotionBriefList);

}