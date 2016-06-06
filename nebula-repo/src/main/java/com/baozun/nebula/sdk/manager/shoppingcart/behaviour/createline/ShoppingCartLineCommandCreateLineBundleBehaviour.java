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
package com.baozun.nebula.sdk.manager.shoppingcart.behaviour.createline;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.baozun.nebula.sdk.command.CouponCodeCommand;
import com.baozun.nebula.sdk.command.shoppingcart.PromotionSKUDiscAMTBySetting;
import com.baozun.nebula.sdk.command.shoppingcart.ShoppingCartLineCommand;
import com.baozun.nebula.sdk.manager.shoppingcart.bundle.SdkShoppingCartBundleNewLineBuilder;

/**
 * bundle类型的 ShoppingCartLineCommand 创建订单行.
 *
 * @author <a href="http://feitianbenyue.iteye.com/">feilong</a>
 * @since 5.3.1
 */
@Transactional
@Service("shoppingCartLineCommandCreateLineBundleBehaviour")
public class ShoppingCartLineCommandCreateLineBundleBehaviour extends AbstractShoppingCartLineCommandCreateLineBehaviour{

    /** The Constant LOGGER. */
    private static final Logger                 LOGGER = LoggerFactory.getLogger(ShoppingCartLineCommandCreateLineBundleBehaviour.class);

    /** The sdk shopping cart bundle new line builder. */
    @Autowired
    private SdkShoppingCartBundleNewLineBuilder sdkShoppingCartBundleNewLineBuilder;

    /*
     * (non-Javadoc)
     * 
     * @see com.baozun.nebula.sdk.manager.order.SdkOrderLineCreateManager#saveOrderLine(java.lang.Long, java.util.List, java.util.List,
     * com.baozun.nebula.sdk.command.shoppingcart.ShoppingCartLineCommand)
     */
    @Override
    public void saveOrderLine(
                    Long orderId,
                    List<CouponCodeCommand> couponCodes,
                    List<PromotionSKUDiscAMTBySetting> promotionSKUDiscAMTBySettingList,
                    ShoppingCartLineCommand shoppingCartLineCommand){

        Long relatedItemId = shoppingCartLineCommand.getRelatedItemId();
        Long[] skuIds = shoppingCartLineCommand.getSkuIds();
        Integer quantity = shoppingCartLineCommand.getQuantity();

        //TODO feilong bundle 下单要进行拆分
        for (Long skuId : skuIds){
            ShoppingCartLineCommand newShoppingCartLineCommand = sdkShoppingCartBundleNewLineBuilder
                            .buildNewShoppingCartLineCommand(relatedItemId, skuId, quantity, shoppingCartLineCommand);
            saveCommonLine(orderId, couponCodes, promotionSKUDiscAMTBySettingList, newShoppingCartLineCommand);
        }
    }

}
