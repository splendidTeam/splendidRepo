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
package com.baozun.nebula.sdk.manager.shoppingcart.behaviour.proxy;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.baozun.nebula.sdk.command.CouponCodeCommand;
import com.baozun.nebula.sdk.command.shoppingcart.PromotionSKUDiscAMTBySetting;
import com.baozun.nebula.sdk.command.shoppingcart.ShoppingCartLineCommand;
import com.baozun.nebula.sdk.manager.shoppingcart.behaviour.createline.ShoppingCartLineCommandCreateLineBehaviour;
import com.baozun.nebula.sdk.manager.shoppingcart.behaviour.organizecount.ShoppingCartLineCommandOrganizeCountBehaviour;
import com.baozun.nebula.sdk.manager.shoppingcart.behaviour.pack.ShoppingCartLineCommandPackBehaviour;

/**
 * bundle类商品的行为.
 *
 * @author <a href="http://feitianbenyue.iteye.com/">feilong</a>
 * @since 5.3.1
 */
@Transactional
@Service("shoppingCartLineCommandBundleBehaviour")
public class ShoppingCartLineCommandBundleBehaviour extends AbstractShoppingCartLineCommandBehaviour{

    /** The shopping cart line command organize count behaviour. */
    @Autowired
    @Qualifier("shoppingCartLineCommandOrganizeCountBundleBehaviour")
    private ShoppingCartLineCommandOrganizeCountBehaviour shoppingCartLineCommandOrganizeCountBehaviour;

    /** The shopping cart line command create line behaviour. */
    @Autowired
    @Qualifier("shoppingCartLineCommandCreateLineBundleBehaviour")
    private ShoppingCartLineCommandCreateLineBehaviour    shoppingCartLineCommandCreateLineBehaviour;

    /** The shopping cart line command pack behaviour. */
    @Autowired
    @Qualifier("shoppingCartLineCommandPackBundleBehaviour")
    private ShoppingCartLineCommandPackBehaviour          shoppingCartLineCommandPackBehaviour;

    /**
     * {@inheritDoc}
     * 
     * <h3>流程.</h3>
     * 
     * <blockquote>
     * <p>
     * <img src="http://venusdrogon.github.io/feilong-platform/mysource/store/bundle库存扣减流程.png"/>
     * </p>
     * </blockquote>
     */
    @Override
    public void organizeExtentionCodeAndCountMapForDeductSkuInventory(
                    ShoppingCartLineCommand shoppingCartLineCommand,
                    Map<String, Integer> extentionCodeAndCountMap){
        shoppingCartLineCommandOrganizeCountBehaviour
                        .organizeExtentionCodeAndCountMapForDeductSkuInventory(shoppingCartLineCommand, extentionCodeAndCountMap);
    }

    /*
     * (non-Javadoc)
     * 
     * @see com.baozun.nebula.sdk.manager.shoppingcart.behaviour.ShoppingCartLineCommandBehaviour#saveOrderLine(java.lang.Long,
     * java.util.List, java.util.List, com.baozun.nebula.sdk.command.shoppingcart.ShoppingCartLineCommand)
     */
    @Override
    public void saveOrderLine(
                    Long orderId,
                    List<CouponCodeCommand> couponCodes,
                    List<PromotionSKUDiscAMTBySetting> promotionSKUDiscAMTBySettingList,
                    ShoppingCartLineCommand shoppingCartLineCommand){
        shoppingCartLineCommandCreateLineBehaviour
                        .saveOrderLine(orderId, couponCodes, promotionSKUDiscAMTBySettingList, shoppingCartLineCommand);
    }

    /*
     * (non-Javadoc)
     * 
     * @see
     * com.baozun.nebula.sdk.manager.shoppingcart.behaviour.ShoppingCartLineCommandBehaviour#packShoppingCartLine(com.baozun.nebula.sdk.
     * command.shoppingcart.ShoppingCartLineCommand)
     */
    @Override
    public void packShoppingCartLine(ShoppingCartLineCommand shoppingCartLineCommand){
        shoppingCartLineCommandPackBehaviour.packShoppingCartLine(shoppingCartLineCommand);
    }
}
