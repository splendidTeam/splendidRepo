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

import static java.math.BigDecimal.ZERO;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.baozun.nebula.sdk.command.shoppingcart.CalcFreightCommand;
import com.baozun.nebula.sdk.command.shoppingcart.PromotionBrief;
import com.baozun.nebula.sdk.command.shoppingcart.ShopCartCommandByShop;
import com.baozun.nebula.sdk.command.shoppingcart.ShoppingCartCommand;
import com.baozun.nebula.sdk.command.shoppingcart.ShoppingCartLineCommand;
import com.baozun.nebula.sdk.manager.SdkFreightFeeManager;
import com.baozun.nebula.utils.ShoppingCartUtil;
import com.feilong.core.bean.ConvertUtil;
import com.feilong.core.date.DateExtensionUtil;
import com.feilong.core.lang.NumberUtil;
import com.feilong.core.util.CollectionsUtil;

/**
 * The Class ShopCartCommandByShopBuilderImpl.
 *
 * @author <a href="http://feitianbenyue.iteye.com/">feilong</a>
 * @since 5.3.1.6
 */
@Transactional
@Service("shopCartCommandByShopBuilder")
public class ShopCartCommandByShopBuilderImpl implements ShopCartCommandByShopBuilder{

    /** The Constant log. */
    private static final Logger LOGGER = LoggerFactory.getLogger(ShopCartCommandByShopBuilderImpl.class);

    /** The sdk freight fee manager. */
    @Autowired
    private SdkFreightFeeManager sdkFreightFeeManager;

    /** The promotion result command builder. */
    @Autowired
    private PromotionResultCommandBuilder promotionResultCommandBuilder;

    /** The share discount to line manager. */
    @Autowired
    private ShareDiscountToLineManager shareDiscountToLineManager;

    /*
     * (non-Javadoc)
     * 
     * @see com.baozun.nebula.sdk.manager.shoppingcart.handler.ShopCartCommandByShopBuilder#build(java.lang.Long,
     * com.baozun.nebula.sdk.command.shoppingcart.ShoppingCartCommand, com.baozun.nebula.sdk.command.shoppingcart.CalcFreightCommand)
     */
    @Override
    public ShopCartCommandByShop build(Long shopId,ShoppingCartCommand shoppingCartCommand,CalcFreightCommand calcFreightCommand){
        List<ShoppingCartLineCommand> shoppingCartLineCommandList = shoppingCartCommand.getShoppingCartLineCommands();
        BigDecimal originShippingFee = sdkFreightFeeManager.getFreightFee(shopId, calcFreightCommand, shoppingCartLineCommandList);

        //*****************************************************************************************
        ShopCartCommandByShop shopCartCommandByShop = new ShopCartCommandByShop();
        // 应付运费
        shopCartCommandByShop.setOriginShoppingFee(originShippingFee);

        // 应付小计
        BigDecimal originPayAmount = ShoppingCartUtil.getOriginPayAmount(shoppingCartLineCommandList);
        shopCartCommandByShop.setSubtotalCurrentPayAmount(originPayAmount);

        // 应付合计(应付小计+应付运费)
        BigDecimal sumCurrentPayAmt = originShippingFee.add(originPayAmount);
        shopCartCommandByShop.setSumCurrentPayAmount(sumCurrentPayAmt);

        //****************************************************************************************

        //TODO feilong  shoppingCartCommand 和ShopCartCommandByShop 搞在一起了, 很挫,   
        // 应付金额
        shoppingCartCommand.setOriginPayAmount(sumCurrentPayAmt);

        //*************************************************************************
        BigDecimal bundleDiscount = ShoppingCartUtil.getBundleDiscount(shoppingCartLineCommandList);

        // 实付金额
        shoppingCartCommand.setCurrentPayAmount(sumCurrentPayAmt.subtract(bundleDiscount));

        // 实付合计
        shopCartCommandByShop.setRealPayAmount(sumCurrentPayAmt.subtract(bundleDiscount));

        //********************************************************************************* 

        // 该店铺的商品数量
        shopCartCommandByShop.setQty(ShoppingCartUtil.getSumQuantity(shoppingCartLineCommandList));
        shopCartCommandByShop.setShopId(shopId);
        return shopCartCommandByShop;
    }

    /*
     * (non-Javadoc)
     * 
     * @see
     * com.baozun.nebula.sdk.manager.shoppingcart.handler.ShopCartCommandByShopBuilder#build(com.baozun.nebula.sdk.command.shoppingcart.
     * ShoppingCartCommand, com.baozun.nebula.sdk.command.shoppingcart.CalcFreightCommand, java.util.List)
     */
    @Override
    public ShopCartCommandByShop build(ShoppingCartCommand inputShoppingCartCommand,CalcFreightCommand calcFreightCommand,List<PromotionBrief> promotionBriefList){
        Date beginDate = new Date();

        PromotionResultCommand promotionResultCommand = buildPromotionResultCommand(inputShoppingCartCommand, promotionBriefList);

        //******************************************************************************************************
        List<ShoppingCartLineCommand> shoppingCartLineCommandList = inputShoppingCartCommand.getShoppingCartLineCommands();// 有效的购物车行
        Long shopId = shoppingCartLineCommandList.get(0).getShopId();// 店铺id

        //***************************************************************************************
        ShopCartCommandByShop shopCartCommandByShop = buildInner(inputShoppingCartCommand, calcFreightCommand, promotionResultCommand);

        //XXX feilong 原来是这里加礼品的
        CollectionsUtil.addAllIgnoreNull(shoppingCartLineCommandList, promotionResultCommand.getGiftList());// 将礼品放入购物车当中

        inputShoppingCartCommand.setShoppingCartLineCommands(shoppingCartLineCommandList);
        inputShoppingCartCommand.setOriginPayAmount(shopCartCommandByShop.getSumCurrentPayAmount());// 应付金额
        inputShoppingCartCommand.setCurrentPayAmount(shopCartCommandByShop.getRealPayAmount()); // 实付金额
        inputShoppingCartCommand.setShoppingCartLineCommands(updateLines(shopId, inputShoppingCartCommand, promotionBriefList));

        LOGGER.info("build ShopCartCommandByShop use time:[{}]", DateExtensionUtil.formatDuration(beginDate, new Date()));
        return shopCartCommandByShop;
    }

    /**
     * Builds the promotion result command.
     *
     * @param inputShoppingCartCommand
     *            the input shopping cart command
     * @param promotionBriefList
     *            the promotion brief list
     * @return the promotion result command
     * @since 5.3.1.6
     */
    private PromotionResultCommand buildPromotionResultCommand(ShoppingCartCommand inputShoppingCartCommand,List<PromotionBrief> promotionBriefList){
        PromotionResultCommand promotionResultCommand = promotionResultCommandBuilder.build(promotionBriefList);
        BigDecimal sumDisAmtOnOrder = NumberUtil.getAddValue(promotionResultCommand.getDisAmtOnOrder(), ShoppingCartUtil.getBundleDiscount(inputShoppingCartCommand.getShoppingCartLineCommands()));
        promotionResultCommand.setDisAmtOnOrder(sumDisAmtOnOrder);
        return promotionResultCommand;
    }

    /**
     * Builds the inner.
     *
     * @param inputShoppingCartCommand
     *            the input shopping cart command
     * @param calcFreightCommand
     *            the calc freight command
     * @param promotionResultCommand
     *            the promotion result command
     * @return the shop cart command by shop
     */
    private ShopCartCommandByShop buildInner(ShoppingCartCommand inputShoppingCartCommand,CalcFreightCommand calcFreightCommand,PromotionResultCommand promotionResultCommand){

        BigDecimal disAmtOnOrder = promotionResultCommand.getDisAmtOnOrder();
        BigDecimal baseOnOrderDisAmt = promotionResultCommand.getBaseOnOrderDisAmt();
        BigDecimal offersShippingDisAmt = promotionResultCommand.getOffersShippingDisAmt();

        List<ShoppingCartLineCommand> shoppingCartLineCommandList = inputShoppingCartCommand.getShoppingCartLineCommands();// 有效的购物车行
        Long shopId = shoppingCartLineCommandList.get(0).getShopId();// 店铺id
        //**************************************************************************************************
        ShopCartCommandByShop shopCartCommandByShop = new ShopCartCommandByShop();
        shopCartCommandByShop.setQty(ShoppingCartUtil.getSumQuantity(shoppingCartLineCommandList));// 商品数量

        BigDecimal originPayAmount = ShoppingCartUtil.getOriginPayAmount(shoppingCartLineCommandList);
        shopCartCommandByShop.setSubtotalCurrentPayAmount(originPayAmount); // 应付小计
        shopCartCommandByShop.setSumCurrentPayAmount(originPayAmount); // 应付合计

        shopCartCommandByShop.setOffersShipping(ZERO);// 计算运费
        shopCartCommandByShop.setOriginShoppingFee(ZERO);// 源运费

        BigDecimal originShippingFee = ZERO; // 应付运费
        BigDecimal currentShippingFee = ZERO;// 实付运费

        if (null != calcFreightCommand){

            originShippingFee = sdkFreightFeeManager.getFreightFee(shopId, calcFreightCommand, shoppingCartLineCommandList);

            shopCartCommandByShop.setOriginShoppingFee(originShippingFee); // 应付运费
            shopCartCommandByShop.setOffersShipping(originShippingFee.compareTo(offersShippingDisAmt) >= 0 ? offersShippingDisAmt : originShippingFee); // 运费优惠

            //TODO feilong  shoppingCartCommand 和ShopCartCommandByShop 搞在一起了, 很挫,   
            inputShoppingCartCommand.setOriginShoppingFee(originShippingFee); // 应付运费

            // 实付运费
            currentShippingFee = shopCartCommandByShop.getOriginShoppingFee().subtract(shopCartCommandByShop.getOffersShipping());
            inputShoppingCartCommand.setCurrentShippingFee(currentShippingFee);
        }

        // 当应付合计金额 小于订单优惠时
        if (shopCartCommandByShop.getSumCurrentPayAmount().compareTo(disAmtOnOrder) < 0){
            shopCartCommandByShop.setOffersTotal(shopCartCommandByShop.getSubtotalCurrentPayAmount());
            shopCartCommandByShop.setDisAmtOnOrder(shopCartCommandByShop.getSubtotalCurrentPayAmount());
            shopCartCommandByShop.setOffersShipping(ZERO);
            shopCartCommandByShop.setDisAmtSingleOrder(ZERO);
        }else{
            BigDecimal shopCartAllDisAmt = disAmtOnOrder.add(baseOnOrderDisAmt);// 购物车的所有优惠(不包含运费)
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
        BigDecimal realPayAmt = shopCartCommandByShop.getSumCurrentPayAmount().add(currentShippingFee).subtract(shopCartCommandByShop.getOffersTotal());
        shopCartCommandByShop.setRealPayAmount(realPayAmt);// 实付合计

        // 应付合计(应付金额+应付运费)
        shopCartCommandByShop.setSumCurrentPayAmount(shopCartCommandByShop.getSubtotalCurrentPayAmount().add(originShippingFee));

        shopCartCommandByShop.setShopId(shopId);
        return shopCartCommandByShop;
    }

    /**
     * Update.
     *
     * @param shopId
     *            the shop id
     * @param inputShoppingCartCommand
     *            the input shopping cart command
     * @param promotionBriefList
     *            the promotion brief list
     * @return the list
     * @since 5.3.1.6
     */
    private List<ShoppingCartLineCommand> updateLines(Long shopId,ShoppingCartCommand inputShoppingCartCommand,List<PromotionBrief> promotionBriefList){
        // 封装数据
        ShoppingCartCommand shoppingCartCommand = new ShoppingCartCommand();
        shoppingCartCommand.setShoppingCartLineCommands(inputShoppingCartCommand.getShoppingCartLineCommands());
        shoppingCartCommand.setShoppingCartByShopIdMap(ConvertUtil.toMap(shopId, inputShoppingCartCommand));

        // 设置 行小计 为 行小计减去 整单分摊到行上的小计 的值
        shareDiscountToLineManager.shareDiscountToLine(shoppingCartCommand, promotionBriefList);

        return shoppingCartCommand.getShoppingCartLineCommands();
    }
}
