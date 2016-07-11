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

import static com.feilong.core.date.DateExtensionUtil.getIntervalForView;

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
import com.feilong.core.bean.ConvertUtil;
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

    /** The promotion result command builder. */
    @Autowired
    private PromotionResultCommandBuilder promotionResultCommandBuilder;

    /** The share discount to line manager. */
    @Autowired
    private ShareDiscountToLineManager    shareDiscountToLineManager;

    /** The shop cart command by shop builder. */
    @Autowired
    private ShopCartCommandByShopBuilder  shopCartCommandByShopBuilder;

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
        ShopCartCommandByShop shopCartCommandByShop = shopCartCommandByShopBuilder
                        .build(inputShoppingCartCommand, calcFreightCommand, promotionResultCommand);

        //XXX feilong 原来是这里加礼品的
        CollectionsUtil.addAllIgnoreNull(shoppingCartLineCommandList, promotionResultCommand.getGiftList());// 将礼品放入购物车当中

        inputShoppingCartCommand.setShoppingCartLineCommands(shoppingCartLineCommandList);
        inputShoppingCartCommand.setOriginPayAmount(shopCartCommandByShop.getSumCurrentPayAmount());// 应付金额
        inputShoppingCartCommand.setCurrentPayAmount(shopCartCommandByShop.getRealPayAmount()); // 实付金额
        inputShoppingCartCommand.setShoppingCartLineCommands(updateLines(shopId, inputShoppingCartCommand, promotionBriefList));

        LOGGER.info("use time:{}", getIntervalForView(beginDate, new Date()));
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
    private List<ShoppingCartLineCommand> updateLines(
                    Long shopId,
                    ShoppingCartCommand inputShoppingCartCommand,
                    List<PromotionBrief> promotionBriefList){
        // 封装数据
        ShoppingCartCommand shoppingCartCommand = new ShoppingCartCommand();
        shoppingCartCommand.setShoppingCartLineCommands(inputShoppingCartCommand.getShoppingCartLineCommands());
        shoppingCartCommand.setShoppingCartByShopIdMap(ConvertUtil.toMap(shopId, inputShoppingCartCommand));

        // 设置 行小计 为 行小计减去 整单分摊到行上的小计 的值
        shareDiscountToLineManager.shareDiscountToLine(shoppingCartCommand, promotionBriefList);

        return shoppingCartCommand.getShoppingCartLineCommands();
    }

}
