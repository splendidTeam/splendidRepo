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

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.baozun.nebula.sdk.command.shoppingcart.ShoppingCartLineCommand;
import com.feilong.core.util.MapUtil;

/**
 * 普通的购物车行的行为.
 *
 * @author <a href="http://feitianbenyue.iteye.com/">feilong</a>
 * @since 5.3.1
 */
@Transactional
@Service("shoppingCartLineCommandOrganizeCountCommonBehaviour")
public class ShoppingCartLineCommandOrganizeCountCommonBehaviour extends AbstractShoppingCartLineCommandOrganizeCountBehaviour{

    /*
     * (non-Javadoc)
     * 
     * @see
     * com.baozun.nebula.sdk.manager.shoppingcart.SdkShoppingCartLineCommandBehaviour#organizeExtentionCodeAndCountMapForDeductSkuInventory(
     * java.util.Map, com.baozun.nebula.sdk.command.shoppingcart.ShoppingCartLineCommand)
     */
    @Override
    public void organizeExtentionCodeAndCountMapForDeductSkuInventory(
                    ShoppingCartLineCommand shoppingCartLineCommand,
                    Map<String, Integer> extentionCodeAndCountMap){

        Integer quantity = shoppingCartLineCommand.getQuantity();

        //主卖品和赠品都扣库存
        String extentionCode = shoppingCartLineCommand.getExtentionCode();
        MapUtil.putSumValue(extentionCodeAndCountMap, extentionCode, quantity);
    }

}
