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

import java.math.BigDecimal;
import java.util.List;
import java.util.Objects;

import org.apache.commons.collections4.Predicate;
import org.apache.commons.lang3.ObjectUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.baozun.nebula.sdk.command.shoppingcart.PromotionBrief;
import com.baozun.nebula.sdk.command.shoppingcart.PromotionSKUDiscAMTBySetting;
import com.baozun.nebula.sdk.command.shoppingcart.ShoppingCartCommand;
import com.baozun.nebula.sdk.command.shoppingcart.ShoppingCartLineCommand;
import com.baozun.nebula.sdk.manager.promotion.SdkPromotionCalculationShareToSKUManager;
import com.feilong.core.Validator;
import com.feilong.core.lang.NumberUtil;
import com.feilong.core.util.CollectionsUtil;

/**
 *
 * @author <a href="http://feitianbenyue.iteye.com/">feilong</a>
 * @since 5.3.1
 */
@Transactional
@Service("shareDiscountToLineManager")
public class ShareDiscountToLineManagerImpl implements ShareDiscountToLineManager{

    private static final Logger                      LOGGER = LoggerFactory.getLogger(ShareDiscountToLineManagerImpl.class);

    /** The sdk promotion calculation share to sku manager. */
    @Autowired
    private SdkPromotionCalculationShareToSKUManager sdkPromotionCalculationShareToSKUManager;

    /*
     * (non-Javadoc)
     * 
     * @see com.baozun.nebula.sdk.manager.shoppingcart.handler.ShareDiscountToLineManager#shareDiscountToLine(com.baozun.nebula.sdk.command.
     * shoppingcart.ShoppingCartCommand, java.util.List)
     */
    @Override
    public void shareDiscountToLine(ShoppingCartCommand shoppingCartCommand,List<PromotionBrief> promotionBriefList){
        // 分摊结果
        List<PromotionSKUDiscAMTBySetting> promotionSKUDiscAMTBySettingList = sdkPromotionCalculationShareToSKUManager
                        .sharePromotionDiscountToEachLine(shoppingCartCommand, promotionBriefList);
        if (Validator.isNullOrEmpty(promotionSKUDiscAMTBySettingList)){
            LOGGER.debug("promotionSKUDiscAMTBySettingList is null or empty,no need to shareDiscountToLine");
            return;
        }

        List<ShoppingCartLineCommand> shoppingCartLineCommandList = shoppingCartCommand.getShoppingCartLineCommands();
        for (ShoppingCartLineCommand shoppingCartLineCommand : shoppingCartLineCommandList){
            if (shoppingCartLineCommand.isGift()){
                continue;
            }
            BigDecimal discount = getLineDiscount(shoppingCartLineCommand, promotionSKUDiscAMTBySettingList);
            shoppingCartLineCommand.setDiscount(discount);

            // 购物车行小计
            BigDecimal salePrice = shoppingCartLineCommand.getSalePrice();
            Integer quantity = shoppingCartLineCommand.getQuantity();
            BigDecimal lineSubTotalAmt = NumberUtil.getMultiplyValue(quantity, salePrice,2).subtract(discount);
            BigDecimal subTotalAmt = lineSubTotalAmt.compareTo(BigDecimal.ZERO) < 0 ? BigDecimal.ZERO : lineSubTotalAmt;

            LOGGER.debug("salesprice:[{}],qty:[{}],discount:[{}],subTotalAmt:[{}]", salePrice, quantity, discount, subTotalAmt);
            shoppingCartLineCommand.setSubTotalAmt(subTotalAmt);
        }
    }

    private BigDecimal getLineDiscount(
                    final ShoppingCartLineCommand shoppingCartLineCommand,
                    List<PromotionSKUDiscAMTBySetting> promotionSKUDiscAMTBySettingList){
        final Long skuId = shoppingCartLineCommand.getSkuId();
        //TODO feilong 提取
        BigDecimal discountAmountSum = CollectionsUtil
                        .sum(promotionSKUDiscAMTBySettingList, "discountAmount", new Predicate<PromotionSKUDiscAMTBySetting>(){

                            @Override
                            public boolean evaluate(PromotionSKUDiscAMTBySetting promotionSKUDiscAMTBySetting){
                                boolean freeShipOrGiftMark = promotionSKUDiscAMTBySetting.getFreeShippingMark()
                                                || promotionSKUDiscAMTBySetting.getGiftMark();
                                return !freeShipOrGiftMark && skuId.equals(promotionSKUDiscAMTBySetting.getSkuId());
                            }
                        });
        return ObjectUtils.defaultIfNull(discountAmountSum, BigDecimal.ZERO);
    }
}
