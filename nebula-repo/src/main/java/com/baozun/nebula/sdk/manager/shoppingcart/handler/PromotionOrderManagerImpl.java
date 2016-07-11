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
import com.baozun.nebula.sdk.command.shoppingcart.ShopCartCommandByShop;
import com.baozun.nebula.sdk.command.shoppingcart.ShoppingCartCommand;
import com.baozun.nebula.sdk.command.shoppingcart.ShoppingCartLineCommand;
import com.baozun.nebula.sdk.manager.SdkFreightFeeManager;
import com.baozun.nebula.utils.ShoppingCartUtil;
import com.feilong.core.date.DateExtensionUtil;
import com.feilong.core.util.CollectionsUtil;

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
    private static final Logger           LOGGER = LoggerFactory.getLogger(PromotionOrderManagerImpl.class);

    /** The share discount to line manager. */
    @Autowired
    private ShareDiscountToLineManager    shareDiscountToLineManager;

    /** The sdk freight fee manager. */
    @Autowired
    private SdkFreightFeeManager          sdkFreightFeeManager;

    /** The promotion result command builder. */
    @Autowired
    private PromotionResultCommandBuilder promotionResultCommandBuilder;

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

        PromotionResultCommand promotionResultCommand = promotionResultCommandBuilder.build(promotionBriefList);

        //******************************************************************************************************

        List<ShoppingCartLineCommand> shoppingCartLineCommandList = inputShoppingCartCommand.getShoppingCartLineCommands();// 有效的购物车行
        Long shopId = shoppingCartLineCommandList.get(0).getShopId();// 店铺id

        //***************************************************************************************
        ShopCartCommandByShop shopCartCommandByShop = build(inputShoppingCartCommand, calcFreightCommand, promotionResultCommand);

        //XXX feilong 原来是这里加礼品的
        CollectionsUtil.addAllIgnoreNull(shoppingCartLineCommandList, promotionResultCommand.getGiftList());// 将礼品放入购物车当中

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
                    PromotionResultCommand promotionResultCommand){

        BigDecimal disAmtOnOrder = promotionResultCommand.getDisAmtOnOrder();
        BigDecimal baseOnOrderDisAmt = promotionResultCommand.getBaseOnOrderDisAmt();
        BigDecimal offersShippingDisAmt = promotionResultCommand.getOffersShippingDisAmt();

        List<ShoppingCartLineCommand> shoppingCartLineCommandList = inputShoppingCartCommand.getShoppingCartLineCommands();// 有效的购物车行

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
            Long shopId = shoppingCartLineCommandList.get(0).getShopId();// 店铺id
            originShippingFee = sdkFreightFeeManager.getFreightFee(shopId, calcFreightCommand, shoppingCartLineCommandList);

            shopCartCommandByShop.setOriginShoppingFee(originShippingFee); // 应付运费
            shopCartCommandByShop.setOffersShipping(
                            originShippingFee.compareTo(offersShippingDisAmt) >= 0 ? offersShippingDisAmt : originShippingFee); // 运费优惠

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
        BigDecimal realPayAmt = shopCartCommandByShop.getSumCurrentPayAmount().add(currentShippingFee)
                        .subtract(shopCartCommandByShop.getOffersTotal());
        shopCartCommandByShop.setRealPayAmount(realPayAmt);// 实付合计

        // 应付合计(应付金额+应付运费)
        shopCartCommandByShop.setSumCurrentPayAmount(shopCartCommandByShop.getSubtotalCurrentPayAmount().add(originShippingFee));
        return shopCartCommandByShop;
    }

}
