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
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.baozun.nebula.sdk.command.shoppingcart.CalcFreightCommand;
import com.baozun.nebula.sdk.command.shoppingcart.PromotionBrief;
import com.baozun.nebula.sdk.command.shoppingcart.PromotionSKUDiscAMTBySetting;
import com.baozun.nebula.sdk.command.shoppingcart.PromotionSettingDetail;
import com.baozun.nebula.sdk.command.shoppingcart.ShopCartCommandByShop;
import com.baozun.nebula.sdk.command.shoppingcart.ShoppingCartCommand;
import com.baozun.nebula.sdk.command.shoppingcart.ShoppingCartLineCommand;
import com.baozun.nebula.sdk.constants.Constants;
import com.baozun.nebula.sdk.manager.SdkFreightFeeManager;
import com.baozun.nebula.sdk.manager.shoppingcart.behaviour.SdkShoppingCartLineCommandBehaviourFactory;
import com.baozun.nebula.sdk.manager.shoppingcart.behaviour.proxy.ShoppingCartLineCommandBehaviour;
import com.baozun.nebula.utils.ShoppingCartUtil;
import com.feilong.core.Validator;
import com.feilong.core.date.DateExtensionUtil;
import com.feilong.core.lang.NumberUtil;

/**
 * The Class PromotionOrderManagerImpl.
 *
 * @author <a href="http://feitianbenyue.iteye.com/">feilong</a>
 * @since 5.3.1
 */
@Transactional
@Service("promotionOrderManager")
public class PromotionOrderManagerImpl implements PromotionOrderManager{

    /** The Constant LOGGER. */
    private static final Logger                        LOGGER = LoggerFactory.getLogger(PromotionOrderManagerImpl.class);

    /** The share discount to line manager. */
    @Autowired
    private ShareDiscountToLineManager                 shareDiscountToLineManager;

    /** The sdk freight fee manager. */
    @Autowired
    private SdkFreightFeeManager                       sdkFreightFeeManager;

    /** The sdk shopping cart line command behaviour factory. */
    @Autowired
    private SdkShoppingCartLineCommandBehaviourFactory sdkShoppingCartLineCommandBehaviourFactory;

    /*
     * (non-Javadoc)
     * 
     * @see
     * com.baozun.nebula.sdk.manager.shoppingcart.handler.PromotionOrderManager#buildShopCartCommandByShop(com.baozun.nebula.sdk.command.
     * shoppingcart.ShoppingCartCommand, com.baozun.nebula.sdk.command.shoppingcart.CalcFreightCommand, java.util.List)
     */
    @Override
    public ShopCartCommandByShop buildShopCartCommandByShop(
                    ShoppingCartCommand inputShoppingCartCommand,
                    CalcFreightCommand calcFreightCommand,
                    List<PromotionBrief> promotionBriefList){

        Date beginDate = new Date();

        BigDecimal disAmtOnOrder = BigDecimal.ZERO;// 商品行优惠金额
        BigDecimal baseOnOrderDisAmt = BigDecimal.ZERO;// 整单优惠金额
        BigDecimal offersShippingDisAmt = BigDecimal.ZERO;// 运费整单优惠金额

        List<ShoppingCartLineCommand> giftList = new ArrayList<ShoppingCartLineCommand>();// 礼品行
        if (Validator.isNotNullOrEmpty(promotionBriefList)){

            for (PromotionBrief promotionBrief : promotionBriefList){
                List<PromotionSettingDetail> promotionSettingDetailList = promotionBrief.getDetails();
                if (Validator.isNullOrEmpty(promotionSettingDetailList)){
                    continue;
                }

                for (PromotionSettingDetail promotionSettingDetail : promotionSettingDetailList){
                    List<PromotionSKUDiscAMTBySetting> affectPromotionSKUDiscAMTBySettingList = promotionSettingDetail
                                    .getAffectSKUDiscountAMTList();

                    if (Validator.isNotNullOrEmpty(affectPromotionSKUDiscAMTBySettingList)){
                        for (PromotionSKUDiscAMTBySetting promotionSKUDiscAMTBySetting : affectPromotionSKUDiscAMTBySettingList){
                            // 如果是礼品
                            if (promotionSKUDiscAMTBySetting.getGiftMark()){
                                giftList.add(toGiftShoppingCartLineCommand(promotionSKUDiscAMTBySetting));
                            }else{
                                BigDecimal skuDisAmt = promotionSKUDiscAMTBySetting.getDiscountAmount();
                                disAmtOnOrder = disAmtOnOrder.add(skuDisAmt);// 非礼品才算优惠
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

        List<ShoppingCartLineCommand> shoppingCartLineCommandList = inputShoppingCartCommand.getShoppingCartLineCommands();// 有效的购物车行
        Long shopId = shoppingCartLineCommandList.get(0).getShopId();// 店铺id

        //***************************************************************************************
        ShopCartCommandByShop shopCartCommandByShop = build(
                        inputShoppingCartCommand,
                        calcFreightCommand,
                        disAmtOnOrder,
                        baseOnOrderDisAmt,
                        offersShippingDisAmt);

        //XXX feilong 原来是这里加礼品的
        shoppingCartLineCommandList.addAll(giftList); // 将礼品放入购物车当中
        inputShoppingCartCommand.setShoppingCartLineCommands(shoppingCartLineCommandList);

        inputShoppingCartCommand.setOriginPayAmount(shopCartCommandByShop.getSumCurrentPayAmount());// 应付金额
        inputShoppingCartCommand.setCurrentPayAmount(shopCartCommandByShop.getRealPayAmount()); // 实付金额

        //***************************************************************************************************

        // 封装数据
        ShoppingCartCommand shoppingCartCommand = new ShoppingCartCommand();
        Map<Long, ShoppingCartCommand> map = new HashMap<Long, ShoppingCartCommand>();
        map.put(shopId, inputShoppingCartCommand);

        shoppingCartCommand.setShoppingCartLineCommands(inputShoppingCartCommand.getShoppingCartLineCommands());
        shoppingCartCommand.setShoppingCartByShopIdMap(map);

        // 设置 行小计 为 行小计减去 整单分摊到行上的小计 的值
        shareDiscountToLineManager.shareDiscountToLine(shoppingCartCommand, promotionBriefList);
        inputShoppingCartCommand.setShoppingCartLineCommands(shoppingCartCommand.getShoppingCartLineCommands());

        LOGGER.info("use time:{}", DateExtensionUtil.getIntervalForView(beginDate, new Date()));

        return shopCartCommandByShop;
    }

    /**
     * Builds the.
     *
     * @param inputShoppingCartCommand
     *            the input shopping cart command
     * @param calcFreightCommand
     *            the calc freight command
     * @param disAmtOnOrder
     *            the dis amt on order
     * @param baseOnOrderDisAmt
     *            the base on order dis amt
     * @param offersShippingDisAmt
     *            the offers shipping dis amt
     * @return the shop cart command by shop
     */
    private ShopCartCommandByShop build(
                    ShoppingCartCommand inputShoppingCartCommand,
                    CalcFreightCommand calcFreightCommand,
                    BigDecimal disAmtOnOrder,
                    BigDecimal baseOnOrderDisAmt,
                    BigDecimal offersShippingDisAmt){

        List<ShoppingCartLineCommand> shoppingCartLineCommandList = inputShoppingCartCommand.getShoppingCartLineCommands();// 有效的购物车行
        Long shopId = shoppingCartLineCommandList.get(0).getShopId();// 店铺id

        //**************************************************************************************************

        ShopCartCommandByShop shopCartCommandByShop = new ShopCartCommandByShop();

        shopCartCommandByShop.setQty(ShoppingCartUtil.getSumQuantity(shoppingCartLineCommandList));// 商品数量

        BigDecimal originPayAmount = ShoppingCartUtil.getOriginPayAmount(shoppingCartLineCommandList);
        shopCartCommandByShop.setSubtotalCurrentPayAmount(originPayAmount); // 应付小计
        shopCartCommandByShop.setSumCurrentPayAmount(originPayAmount); // 应付合计

        BigDecimal shopCartAllDisAmt = disAmtOnOrder.add(baseOnOrderDisAmt);// 购物车的所有优惠(不包含运费)

        shopCartCommandByShop.setOffersShipping(BigDecimal.ZERO);// 计算运费
        shopCartCommandByShop.setOriginShoppingFee(BigDecimal.ZERO);// 源运费

        BigDecimal originShippingFee = BigDecimal.ZERO; // 应付运费
        BigDecimal currentShippingFee = BigDecimal.ZERO;// 实付运费

        if (null != calcFreightCommand){
            originShippingFee = sdkFreightFeeManager.getFreightFee(shopId, calcFreightCommand, shoppingCartLineCommandList);
            shopCartCommandByShop.setOriginShoppingFee(originShippingFee); // 应付运费
            shopCartCommandByShop.setOffersShipping(
                            originShippingFee.compareTo(offersShippingDisAmt) >= 0 ? offersShippingDisAmt : originShippingFee); // 运费优惠
            inputShoppingCartCommand.setOriginShoppingFee(originShippingFee); // 应付运费
            currentShippingFee = shopCartCommandByShop.getOriginShoppingFee().subtract(shopCartCommandByShop.getOffersShipping()); // 实付运费
            inputShoppingCartCommand.setCurrentShippingFee(currentShippingFee);
        }

        // 当应付合计金额 小于订单优惠时
        if (shopCartCommandByShop.getSumCurrentPayAmount().compareTo(disAmtOnOrder) < 0){
            shopCartCommandByShop.setOffersTotal(shopCartCommandByShop.getSubtotalCurrentPayAmount());
            shopCartCommandByShop.setDisAmtOnOrder(shopCartCommandByShop.getSubtotalCurrentPayAmount());
            shopCartCommandByShop.setOffersShipping(BigDecimal.ZERO);
            shopCartCommandByShop.setDisAmtSingleOrder(BigDecimal.ZERO);
        }else{
            // 当应付合计金额 小于优惠合计时
            if (shopCartCommandByShop.getSumCurrentPayAmount().compareTo(shopCartAllDisAmt) < 0){
                shopCartCommandByShop.setOffersTotal(shopCartCommandByShop.getSumCurrentPayAmount());
                BigDecimal differAmt = shopCartCommandByShop.getSumCurrentPayAmount().subtract(disAmtOnOrder);// 整单优惠=应付金额-订单优惠
                shopCartCommandByShop.setDisAmtSingleOrder(differAmt); // 整单优惠
            }else{
                shopCartCommandByShop.setDisAmtSingleOrder(baseOnOrderDisAmt);
                shopCartCommandByShop.setOffersTotal(shopCartAllDisAmt);
            }
            shopCartCommandByShop.setDisAmtOnOrder(disAmtOnOrder);// 订单优惠
        }

        // 实付合计(应付金额+实付运费-商品优惠总额)
        BigDecimal realPayAmt = shopCartCommandByShop.getSumCurrentPayAmount().add(currentShippingFee)
                        .subtract(shopCartCommandByShop.getOffersTotal());
        shopCartCommandByShop.setRealPayAmount(realPayAmt);// 实付合计

        // 应付合计(应付金额+应付运费)
        shopCartCommandByShop.setSumCurrentPayAmount(shopCartCommandByShop.getSubtotalCurrentPayAmount().add(originShippingFee));
        return shopCartCommandByShop;
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
