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

import static com.feilong.core.Validator.isNotNullOrEmpty;
import static com.feilong.core.Validator.isNullOrEmpty;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.baozun.nebula.sdk.command.shoppingcart.PromotionBrief;
import com.baozun.nebula.sdk.command.shoppingcart.PromotionSKUDiscAMTBySetting;
import com.baozun.nebula.sdk.command.shoppingcart.PromotionSettingDetail;
import com.baozun.nebula.sdk.command.shoppingcart.ShoppingCartLineCommand;
import com.baozun.nebula.sdk.constants.Constants;
import com.baozun.nebula.sdk.manager.shoppingcart.behaviour.SdkShoppingCartLineCommandBehaviourFactory;
import com.baozun.nebula.sdk.manager.shoppingcart.behaviour.proxy.ShoppingCartLineCommandBehaviour;

/**
 * The Class PromotionResultCommandBuilderImpl.
 *
 * @author <a href="http://feitianbenyue.iteye.com/">feilong</a>
 * @since 5.3.1.6
 */
@Transactional
@Service("promotionResultCommandBuilder")
public class PromotionResultCommandBuilderImpl implements PromotionResultCommandBuilder{

    /** The Constant LOGGER. */
    private static final Logger                        LOGGER = LoggerFactory.getLogger(PromotionResultCommandBuilderImpl.class);

    /** The sdk shopping cart line command behaviour factory. */
    @Autowired
    private SdkShoppingCartLineCommandBehaviourFactory sdkShoppingCartLineCommandBehaviourFactory;

    /*
     * (non-Javadoc)
     * 
     * @see com.baozun.nebula.sdk.manager.shoppingcart.handler.PromotionResultCommandBuilder#build(java.util.List)
     */
    @Override
    public PromotionResultCommand build(List<PromotionBrief> promotionBriefList){
        BigDecimal disAmtOnOrder = BigDecimal.ZERO;// 商品行优惠金额
        BigDecimal baseOnOrderDisAmt = BigDecimal.ZERO;// 整单优惠金额
        BigDecimal offersShippingDisAmt = BigDecimal.ZERO;// 运费整单优惠金额

        List<ShoppingCartLineCommand> giftList = new ArrayList<ShoppingCartLineCommand>();// 礼品行
        if (isNotNullOrEmpty(promotionBriefList)){

            for (PromotionBrief promotionBrief : promotionBriefList){
                List<PromotionSettingDetail> promotionSettingDetailList = promotionBrief.getDetails();
                if (isNullOrEmpty(promotionSettingDetailList)){
                    continue;
                }

                for (PromotionSettingDetail promotionSettingDetail : promotionSettingDetailList){
                    List<PromotionSKUDiscAMTBySetting> affectPromotionSKUDiscAMTBySettingList = promotionSettingDetail
                                    .getAffectSKUDiscountAMTList();

                    if (isNotNullOrEmpty(affectPromotionSKUDiscAMTBySettingList)){
                        for (PromotionSKUDiscAMTBySetting promotionSKUDiscAMTBySetting : affectPromotionSKUDiscAMTBySettingList){
                            // 如果是礼品
                            if (promotionSKUDiscAMTBySetting.getGiftMark()){
                                giftList.add(toGiftShoppingCartLineCommand(promotionSKUDiscAMTBySetting));
                            }else{
                                // 非礼品才算优惠
                                disAmtOnOrder = disAmtOnOrder.add(promotionSKUDiscAMTBySetting.getDiscountAmount());
                            }
                        }
                    }else{
                        BigDecimal disAmt = promotionSettingDetail.getDiscountAmount();// 基于整单的
                        if (promotionSettingDetail.getFreeShippingMark()){
                            offersShippingDisAmt = offersShippingDisAmt.add(disAmt); // 运费优惠
                        }else{
                            baseOnOrderDisAmt = baseOnOrderDisAmt.add(disAmt);
                        }
                    }
                }
            }
        }

        PromotionResultCommand promotionResultCommand = new PromotionResultCommand();
        promotionResultCommand.setBaseOnOrderDisAmt(baseOnOrderDisAmt);
        promotionResultCommand.setDisAmtOnOrder(disAmtOnOrder);
        promotionResultCommand.setGiftList(giftList);
        promotionResultCommand.setOffersShippingDisAmt(offersShippingDisAmt);
        return promotionResultCommand;
    }

    /**
     * 封装礼品购物车行.
     *
     * @param promotionSKUDiscAMTBySetting
     *            the pro sku
     * @return the gift shopping cart line command
     */
    private ShoppingCartLineCommand toGiftShoppingCartLineCommand(PromotionSKUDiscAMTBySetting promotionSKUDiscAMTBySetting){
        Integer qty = promotionSKUDiscAMTBySetting.getQty();
        Long settingId = promotionSKUDiscAMTBySetting.getSettingId();

        ShoppingCartLineCommand shoppingCartLineCommand = new ShoppingCartLineCommand();

        shoppingCartLineCommand.setItemId(promotionSKUDiscAMTBySetting.getItemId());
        shoppingCartLineCommand.setItemName(promotionSKUDiscAMTBySetting.getItemName());
        shoppingCartLineCommand.setQuantity(qty);
        shoppingCartLineCommand.setGift(promotionSKUDiscAMTBySetting.getGiftMark());
        shoppingCartLineCommand.setShopId(promotionSKUDiscAMTBySetting.getShopId());
        shoppingCartLineCommand.setType(Constants.ITEM_TYPE_PREMIUMS);
        shoppingCartLineCommand.setSkuId(promotionSKUDiscAMTBySetting.getSkuId());

        if (settingId != null){
            shoppingCartLineCommand.setLineGroup(settingId);
        }
        //XXX feilong what logic?
        shoppingCartLineCommand.setStock(qty);
        // 赠品都设置为有效
        shoppingCartLineCommand.setValid(true);

        ShoppingCartLineCommandBehaviour shoppingCartLineCommandBehaviour = sdkShoppingCartLineCommandBehaviourFactory
                        .getShoppingCartLineCommandBehaviour(shoppingCartLineCommand);
        shoppingCartLineCommandBehaviour.packShoppingCartLine(shoppingCartLineCommand); // 封装购物车行信息

        shoppingCartLineCommand.setPromotionId(promotionSKUDiscAMTBySetting.getPromotionId());
        shoppingCartLineCommand.setSettingId(settingId);
        shoppingCartLineCommand.setGiftChoiceType(promotionSKUDiscAMTBySetting.getGiftChoiceType());
        shoppingCartLineCommand.setGiftCountLimited(promotionSKUDiscAMTBySetting.getGiftCountLimited());

        return shoppingCartLineCommand;
    }
}
