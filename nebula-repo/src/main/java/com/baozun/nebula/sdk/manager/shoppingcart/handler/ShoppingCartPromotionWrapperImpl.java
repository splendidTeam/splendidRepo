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

import static java.math.BigDecimal.ROUND_HALF_UP;
import static java.math.BigDecimal.ZERO;

import java.math.BigDecimal;
import java.util.ArrayList;
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
import com.baozun.nebula.sdk.manager.shoppingcart.SdkShoppingCartGroupManager;
import com.baozun.nebula.utils.ShoppingCartUtil;
import com.feilong.tools.jsonlib.JsonUtil;

import static com.feilong.core.Validator.isNotNullOrEmpty;
import static com.feilong.core.util.AggregateUtil.sum;
import static com.feilong.core.util.CollectionsUtil.group;

/**
 * 
 * @author <a href="http://feitianbenyue.iteye.com/">feilong</a>
 * @since 5.3.2.15
 */
@Transactional
@Service("shoppingCartPromotionWrapper")
public class ShoppingCartPromotionWrapperImpl implements ShoppingCartPromotionWrapper{

    private static final Logger LOGGER = LoggerFactory.getLogger(ShoppingCartPromotionWrapperImpl.class);

    /** The promotion brief builder. */
    @Autowired
    private PromotionBriefBuilder promotionBriefBuilder;

    /** The sdk shopping cart group manager. */
    @Autowired
    private SdkShoppingCartGroupManager sdkShoppingCartGroupManager;

    /** The shop cart command by shop builder. */
    @Autowired
    private ShopCartCommandByShopBuilder shopCartCommandByShopBuilder;

    /*
     * (non-Javadoc)
     * 
     * @see com.baozun.nebula.sdk.manager.shoppingcart.handler.ShoppingCartPromotionHandler#setShopCartPromotionInfos(com.baozun.nebula.sdk.command.shoppingcart.ShoppingCartCommand, com.baozun.nebula.sdk.command.shoppingcart.CalcFreightCommand)
     */
    @Override
    public void wrapPromotion(ShoppingCartCommand shoppingCartCommand,CalcFreightCommand calcFreightCommand){
        // 获取促销数据.需要调用促销引擎计算优惠价格
        List<PromotionBrief> promotionBriefList = promotionBriefBuilder.getPromotionBriefList(shoppingCartCommand);

        if (LOGGER.isDebugEnabled()){
            LOGGER.debug(JsonUtil.format(promotionBriefList));
        }

        //-----------------------------------------------------------------------------------------------

        Map<Long, List<PromotionBrief>> shopIdAndPromotionBriefListMap = group(promotionBriefList, "shopId");

        // 区分店铺的购物车
        Map<Long, ShoppingCartCommand> shopIdAndShoppingCartCommandMap = shoppingCartCommand.getShoppingCartByShopIdMap();

        List<ShopCartCommandByShop> summaryShopCartList = buildShopCartCommandByShopList(calcFreightCommand, shopIdAndPromotionBriefListMap, shopIdAndShoppingCartCommandMap);

        //-----------------------------------------------------------------------------------------------

        shoppingCartCommand.setSummaryShopCartList(summaryShopCartList);

        // 购物车促销简介信息
        shoppingCartCommand.setCartPromotionBriefList(promotionBriefList);

        // 所有购物车行(可能包含了礼品\有效、无效)
        List<ShoppingCartLineCommand> allShoppingCartLines = getAllShoppingCartLines(shopIdAndShoppingCartCommandMap);
        shoppingCartCommand.setShoppingCartLineCommands(allShoppingCartLines);

        setShoppingCartCommandPrice(shoppingCartCommand, summaryShopCartList, allShoppingCartLines);
    }

    /**
     * 返回整个购物车数据.
     *
     * @param shopCartMap
     *            the shop cart map
     * @return the all shopping cart lines
     */
    private static List<ShoppingCartLineCommand> getAllShoppingCartLines(Map<Long, ShoppingCartCommand> shopCartMap){
        List<ShoppingCartLineCommand> allLines = new ArrayList<>();

        for (Map.Entry<Long, ShoppingCartCommand> entry : shopCartMap.entrySet()){
            allLines.addAll(entry.getValue().getShoppingCartLineCommands());
        }
        return allLines;
    }

    /**
     * Builds the shop cart command by shop list.
     *
     * @param calcFreightCommand
     *            the calc freight command
     * @param shopIdAndPromotionBriefListMap
     *            the pro briefs map
     * @param shopIdAndShoppingCartCommandMap
     *            the shop id and shopping cart command map
     * @return the list< shop cart command by shop>
     */
    private List<ShopCartCommandByShop> buildShopCartCommandByShopList(CalcFreightCommand calcFreightCommand,Map<Long, List<PromotionBrief>> shopIdAndPromotionBriefListMap,Map<Long, ShoppingCartCommand> shopIdAndShoppingCartCommandMap){
        List<ShopCartCommandByShop> summaryShopCartList = new ArrayList<>();

        for (Map.Entry<Long, ShoppingCartCommand> entry : shopIdAndShoppingCartCommandMap.entrySet()){
            List<PromotionBrief> promotionBriefList = shopIdAndPromotionBriefListMap.get(entry.getKey());
            ShopCartCommandByShop shopCartCommandByShop = buildShopCartCommandByShop(entry.getKey(), entry.getValue(), calcFreightCommand, promotionBriefList);
            summaryShopCartList.add(shopCartCommandByShop);
        }
        return summaryShopCartList;
    }

    /**
     * Builds the shop cart command by shop.
     *
     * @param shopId
     *            the shop id
     * @param shoppingCartCommand
     *            the shopping cart command
     * @param calcFreightCommand
     *            the calc freight command
     * @param promotionBriefList
     *            the promotion brief list
     * @return the shop cart command by shop
     */
    private ShopCartCommandByShop buildShopCartCommandByShop(Long shopId,ShoppingCartCommand shoppingCartCommand,CalcFreightCommand calcFreightCommand,List<PromotionBrief> promotionBriefList){
        if (isNotNullOrEmpty(promotionBriefList)){
            // 计算分店铺的优惠.只计算有效的购物车行
            ShopCartCommandByShop shopCartCommandByShop = shopCartCommandByShopBuilder.build(shoppingCartCommand, calcFreightCommand, promotionBriefList);

            shoppingCartCommand.setShoppingCartLineCommands(sdkShoppingCartGroupManager.groupShoppingCartLinesToDisplayByLinePromotionResult(shoppingCartCommand, promotionBriefList));
            return shopCartCommandByShop;
        }

        return shopCartCommandByShopBuilder.build(shopId, shoppingCartCommand, calcFreightCommand);
    }

    /**
     * 设置 shopping cart command price.
     *
     * @param shoppingCartCommand
     *            the shopping cart command
     * @param summaryShopCartList
     *            the summary shop cart list
     * @param allShoppingCartLines
     *            the all shopping cart lines
     */
    private static void setShoppingCartCommandPrice(ShoppingCartCommand shoppingCartCommand,List<ShopCartCommandByShop> summaryShopCartList,List<ShoppingCartLineCommand> allShoppingCartLines){
        // 设置应付金额
        shoppingCartCommand.setOriginPayAmount(ShoppingCartUtil.getOriginPayAmount(allShoppingCartLines));

        Map<String, BigDecimal> priceMap = sum(summaryShopCartList, "realPayAmount", "originShoppingFee", "offersShipping");

        // 实际支付金额
        shoppingCartCommand.setCurrentPayAmount(priceMap.get("realPayAmount").setScale(2, ROUND_HALF_UP));

        // 应付运费
        shoppingCartCommand.setOriginShoppingFee(priceMap.get("originShoppingFee").setScale(2, ROUND_HALF_UP));

        // 运费优惠
        BigDecimal offersShippingAmount = priceMap.get("offersShipping");

        // 计算实付运费
        BigDecimal originShoppingFee = shoppingCartCommand.getOriginShoppingFee();
        BigDecimal currentShippingAmountByShopCart = originShoppingFee.compareTo(offersShippingAmount) >= 0 ? originShoppingFee.subtract(offersShippingAmount) : ZERO;
        // 实付运费
        shoppingCartCommand.setCurrentShippingFee(currentShippingAmountByShopCart);
    }

}
