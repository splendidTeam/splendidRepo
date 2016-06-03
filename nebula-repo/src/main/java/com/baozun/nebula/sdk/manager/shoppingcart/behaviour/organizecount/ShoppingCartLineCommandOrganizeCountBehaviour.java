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
package com.baozun.nebula.sdk.manager.shoppingcart.behaviour.organizecount;

import java.util.Map;

import com.baozun.nebula.sdk.command.shoppingcart.ShoppingCartLineCommand;

/**
 * The Interface SdkShoppingCartLineCommandBehaviour.
 *
 * @author <a href="http://feitianbenyue.iteye.com/">feilong</a>
 * @since 5.3.1
 */
public interface ShoppingCartLineCommandOrganizeCountBehaviour{

    /**
     * Organize extention code and count map for deduct sku inventory.
     * 
     * @param shoppingCartLineCommand
     *            the shopping cart line command
     * @param extentionCodeAndCountMap
     *            the extention code and count map
     */
    void organizeExtentionCodeAndCountMapForDeductSkuInventory(
                    ShoppingCartLineCommand shoppingCartLineCommand,
                    Map<String, Integer> extentionCodeAndCountMap);

}
