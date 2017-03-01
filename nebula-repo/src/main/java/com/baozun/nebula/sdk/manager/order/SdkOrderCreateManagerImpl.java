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
package com.baozun.nebula.sdk.manager.order;

import static org.apache.commons.lang3.ObjectUtils.defaultIfNull;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.Validate;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.baozun.nebula.api.salesorder.OrderCodeCreatorManager;
import com.baozun.nebula.exception.BusinessException;
import com.baozun.nebula.model.salesorder.SalesOrder;
import com.baozun.nebula.model.system.MataInfo;
import com.baozun.nebula.sdk.command.CouponCodeCommand;
import com.baozun.nebula.sdk.command.SalesOrderCommand;
import com.baozun.nebula.sdk.command.SalesOrderCreateOptions;
import com.baozun.nebula.sdk.command.shoppingcart.PromotionBrief;
import com.baozun.nebula.sdk.command.shoppingcart.PromotionSKUDiscAMTBySetting;
import com.baozun.nebula.sdk.command.shoppingcart.ShopCartCommandByShop;
import com.baozun.nebula.sdk.command.shoppingcart.ShoppingCartCommand;
import com.baozun.nebula.sdk.command.shoppingcart.ShoppingCartLineCommand;
import com.baozun.nebula.sdk.constants.Constants;
import com.baozun.nebula.sdk.manager.SdkEffectiveManager;
import com.baozun.nebula.sdk.manager.SdkEngineManager;
import com.baozun.nebula.sdk.manager.SdkMataInfoManager;
import com.baozun.nebula.sdk.manager.SdkPayCodeManager;
import com.baozun.nebula.sdk.manager.order.handler.OrderCreateByShopManager;
import com.baozun.nebula.sdk.manager.promotion.SdkPromotionCalculationShareToSKUManager;
import com.baozun.nebula.sdk.manager.promotion.SdkPromotionCouponCodeManager;
import com.baozun.nebula.sdk.manager.shoppingcart.SdkShoppingCartManager;
import com.feilong.core.Validator;
import com.feilong.core.util.CollectionsUtil;
import com.feilong.core.util.MapUtil;

/**
 * 提取出来,专注于创建订单.
 * 
 * @author <a href="http://feitianbenyue.iteye.com/">feilong</a>
 * @version 5.3.1 2016年5月14日 下午6:55:04
 * @since 5.3.1
 */
@Transactional
@Service("sdkOrderCreateManager")
public class SdkOrderCreateManagerImpl implements SdkOrderCreateManager{

    /** The Constant LOGGER. */
    private static final Logger LOGGER = LoggerFactory.getLogger(SdkOrderCreateManagerImpl.class);

    /** The Constant SEPARATOR_FLAG. */
    private static final String SEPARATOR_FLAG = "\\|\\|";

    /** The sdk pay code manager. */
    @Autowired
    private SdkPayCodeManager sdkPayCodeManager;

    /** The sdk shopping cart manager. */
    @Autowired
    private SdkShoppingCartManager sdkShoppingCartManager;

    /** The sdk engine manager. */
    @Autowired
    private SdkEngineManager sdkEngineManager;

    /** The sdk promotion coupon code manager. */
    @Autowired
    private SdkPromotionCouponCodeManager sdkPromotionCouponCodeManager;

    /** The sdk mata info manager. */
    @Autowired
    private SdkMataInfoManager sdkMataInfoManager;

    /** The sdk effective manager. */
    @Autowired
    private SdkEffectiveManager sdkEffectiveManager;

    /** The sdk order email manager. */
    @Autowired
    private SdkOrderEmailManager sdkOrderEmailManager;

    /** The order code creator. */
    @Autowired(required = false)
    private OrderCodeCreatorManager orderCodeCreator;

    /** The sdk promotion calculation share to sku manager. */
    @Autowired
    private SdkPromotionCalculationShareToSKUManager sdkPromotionCalculationShareToSKUManager;

    @Autowired
    private OrderCreateByShopManager orderCreateByShopManager;

    /*
     * (non-Javadoc)
     * 
     * @see
     * com.baozun.nebula.sdk.manager.order.SdkOrderCreateManager#saveOrder(com.baozun.nebula.sdk.command.shoppingcart.ShoppingCartCommand,
     * com.baozun.nebula.sdk.command.SalesOrderCommand, java.util.Set)
     */
    @Override
    public String saveOrder(ShoppingCartCommand shoppingCartCommand,SalesOrderCommand salesOrderCommand,Set<String> memCombos){
        return saveOrderInfo(salesOrderCommand, shoppingCartCommand, null);
    }

    /*
     * (non-Javadoc)
     * 
     * @see
     * com.baozun.nebula.sdk.manager.order.SdkOrderCreateManager#saveOrder(com.baozun.nebula.sdk.command.shoppingcart.ShoppingCartCommand,
     * com.baozun.nebula.sdk.command.SalesOrderCommand, java.util.Set, com.baozun.nebula.sdk.command.SalesOrderCreateOptions)
     */
    @Override
    public String saveOrder(ShoppingCartCommand shoppingCartCommand,SalesOrderCommand salesOrderCommand,Set<String> memCombos,SalesOrderCreateOptions salesOrderCreateOptions){
        Validate.notNull(shoppingCartCommand, "shoppingCartCommand can't be null!");
        Validate.notNull(salesOrderCommand, "salesOrderCommand can't be null!");

        preCreateOrder(shoppingCartCommand, salesOrderCommand, memCombos);

        return saveOrderInfo(salesOrderCommand, shoppingCartCommand, defaultIfNull(salesOrderCreateOptions, new SalesOrderCreateOptions()));
    }

    /**
     * Pre create order.
     *
     * @param shoppingCartCommand
     *            the shopping cart command
     * @param salesOrderCommand
     *            the sales order command
     * @param memCombos
     *            the mem combos
     */
    private void preCreateOrder(ShoppingCartCommand shoppingCartCommand,SalesOrderCommand salesOrderCommand,Set<String> memCombos){
        //去除抬头和未选中的商品
        refactoringShoppingCartCommand(shoppingCartCommand);

        // 下单之前的引擎检查
        sdkEngineManager.createOrderDoEngineChck(salesOrderCommand.getMemberId(), memCombos, shoppingCartCommand);
    }

    /*
     * (non-Javadoc)
     * 
     * @see com.baozun.nebula.sdk.manager.OrderManager#saveManualOrder(com.baozun.nebula.sdk.command.shoppingcart.ShoppingCartCommand,
     * com.baozun.nebula.sdk.command.SalesOrderCommand)
     */
    @Override
    public String saveManualOrder(ShoppingCartCommand shoppingCartCommand,SalesOrderCommand salesOrderCommand,SalesOrderCreateOptions salesOrderCreateOptions){
        if (salesOrderCommand == null || shoppingCartCommand == null){
            throw new BusinessException(Constants.SHOPCART_IS_NULL);
        }
        // 下单之前的库存检查
        for (ShoppingCartLineCommand shoppingCartLine : shoppingCartCommand.getShoppingCartLineCommands()){
            Boolean retflag = sdkEffectiveManager.chckInventory(shoppingCartLine.getExtentionCode(), shoppingCartLine.getQuantity());
            if (!retflag){
                // 库存不足
                throw new BusinessException(Constants.THE_ORDER_CONTAINS_INVENTORY_SHORTAGE_ITEM, new Object[] { shoppingCartLine.getItemName() });
            }
        }
        return saveOrderInfo(salesOrderCommand, shoppingCartCommand, salesOrderCreateOptions);
    }

    /**
     * Refactoring shopping cart command.
     *
     * @param shoppingCartCommand
     *            the shopping cart command
     */
    private void refactoringShoppingCartCommand(ShoppingCartCommand shoppingCartCommand){
        Map<Long, ShoppingCartCommand> shoppingCartByShopIdMap = shoppingCartCommand.getShoppingCartByShopIdMap();

        Map<Long, ShoppingCartCommand> newShoppingCartByShopIdMap = new HashMap<Long, ShoppingCartCommand>();
        List<ShoppingCartLineCommand> newAllShoppingCartLineCommandList = new ArrayList<ShoppingCartLineCommand>();

        for (Map.Entry<Long, ShoppingCartCommand> entry : shoppingCartByShopIdMap.entrySet()){
            ShoppingCartCommand shopShoppingCartCommand = entry.getValue();
            List<ShoppingCartLineCommand> lines = new ArrayList<ShoppingCartLineCommand>();
            for (ShoppingCartLineCommand shoppingCartLineCommand : shopShoppingCartCommand.getShoppingCartLineCommands()){
                // 排除是标题行 或者（是赠品 1需要用户选择 0未选中） 
                boolean noCheck = shoppingCartLineCommand.getSettlementState() == null || shoppingCartLineCommand.getSettlementState() == 0;
                if (!(shoppingCartLineCommand.isCaptionLine() || (shoppingCartLineCommand.isGift() && shoppingCartLineCommand.getGiftChoiceType() == 1 && noCheck))){
                    lines.add(shoppingCartLineCommand);
                    newAllShoppingCartLineCommandList.add(shoppingCartLineCommand);
                }
            }
            shopShoppingCartCommand.setShoppingCartLineCommands(lines);
            newShoppingCartByShopIdMap.put(entry.getKey(), shopShoppingCartCommand);
        }

        shoppingCartCommand.setShoppingCartByShopIdMap(newShoppingCartByShopIdMap);
        shoppingCartCommand.setShoppingCartLineCommands(newAllShoppingCartLineCommandList);
    }

    /**
     * 保存订单信息.
     *
     * @param salesOrderCommand
     *            the sales order command
     * @param shoppingCartCommand
     *            the shopping cart command
     * @param salesOrderCreateOptions
     *            the sales order create options
     * @return the string
     */
    private String saveOrderInfo(SalesOrderCommand salesOrderCommand,ShoppingCartCommand shoppingCartCommand,SalesOrderCreateOptions salesOrderCreateOptions){
        String subOrdinate = orderCodeCreator.createOrderSerialNO();
        Validate.notBlank(subOrdinate, "subOrdinate can't be blank!");

        //基于店铺的 订单行列表.
        Map<Long, List<ShoppingCartLineCommand>> shopIdAndShoppingCartLineCommandListMap = buildShopIdAndShoppingCartLineCommandListMap(shoppingCartCommand);

        //基于店铺的促销.
        Map<Long, List<PromotionSKUDiscAMTBySetting>> shopIdAndPromotionSKUDiscAMTBySettingMap = buildShopIdAndPromotionSKUDiscAMTBySettingMap(shoppingCartCommand);

        //基于店铺的ShopCartCommandByShop.
        Map<Long, ShopCartCommandByShop> shopIdAndShopCartCommandByShopMap = buildShopIdAndShopCartCommandByShopMap(shoppingCartCommand);

        //*****************************************************************************************
        boolean isSendEmail = isSendEmail();
        List<Map<String, Object>> emailDataMapList = new ArrayList<>();
        for (Map.Entry<Long, List<ShoppingCartLineCommand>> entry : shopIdAndShoppingCartLineCommandListMap.entrySet()){
            Long shopId = entry.getKey();
            List<ShoppingCartLineCommand> shoppingCartLineCommandList = entry.getValue();

            ShopCartCommandByShop shopCartCommandByShop = shopIdAndShopCartCommandByShopMap.get(shopId);
            List<PromotionSKUDiscAMTBySetting> shopPromotionSKUDiscAMTBySettingList = shopIdAndPromotionSKUDiscAMTBySettingMap.get(shopId);
            SalesOrder salesOrder = orderCreateByShopManager.doWithPerShopCreateOrder(shopId, subOrdinate, shoppingCartLineCommandList, salesOrderCommand, shopCartCommandByShop, shopPromotionSKUDiscAMTBySettingList);

            // 封装发送邮件数据
            if (isSendEmail){
                Map<String, Object> dataMap = sdkOrderEmailManager.buildDataMapForCreateOrder(subOrdinate, salesOrder, salesOrderCommand, shoppingCartLineCommandList, shopCartCommandByShop, shopPromotionSKUDiscAMTBySettingList, salesOrderCreateOptions);

                CollectionUtils.addIgnoreNull(emailDataMapList, dataMap);
            }
        }

        //*************************************************************************************
        // 保存支付流水
        BigDecimal paySum = getPaySum(shoppingCartCommand.getCurrentPayAmount(), salesOrderCommand.getSoPayMentDetails());
        sdkPayCodeManager.savaPayCode(subOrdinate, paySum);

        //*************************************************************************************
        // 优惠券状体置为已使用 isUsed = 1
        List<CouponCodeCommand> couponCodes = salesOrderCommand.getCouponCodes();
        if (Validator.isNotNullOrEmpty(couponCodes)){
            sdkPromotionCouponCodeManager.batchUseCouponCode(couponCodes);
        }

        // 如果不是立即购买 清空购物车
        if (!salesOrderCreateOptions.getIsImmediatelyBuy()){
            sdkShoppingCartManager.emptyShoppingCart(salesOrderCommand.getMemberId());
        }

        // 发邮件
        sdkOrderEmailManager.sendEmailOfCreateOrder(emailDataMapList);
        return subOrdinate;
    }

    /**
     * 基于店铺的ShopCartCommandByShop.
     *
     * @param shoppingCartCommand
     *            the shopping cart command
     * @return the map< long, shop cart command by shop>
     */
    private Map<Long, ShopCartCommandByShop> buildShopIdAndShopCartCommandByShopMap(ShoppingCartCommand shoppingCartCommand){
        return CollectionsUtil.groupOne(shoppingCartCommand.getSummaryShopCartList(), "shopId");
    }

    /**
     * 基于店铺的 订单行列表.
     *
     * @param shoppingCartCommand
     *            the shopping cart command
     * @return the map< long, list< shopping cart line command>>
     */
    private Map<Long, List<ShoppingCartLineCommand>> buildShopIdAndShoppingCartLineCommandListMap(ShoppingCartCommand shoppingCartCommand){
        Map<Long, ShoppingCartCommand> shopIdAndShoppingCartCommandMap = shoppingCartCommand.getShoppingCartByShopIdMap();
        Map<Long, List<ShoppingCartLineCommand>> shopIdAndShoppingCartLineCommandListMap = MapUtil.extractSubMap(shopIdAndShoppingCartCommandMap, "shoppingCartLineCommands");
        return shopIdAndShoppingCartLineCommandListMap;
    }

    /**
     * 基于店铺的促销.
     *
     * @param shoppingCartCommand
     *            the shopping cart command
     * @return the map< long, list< promotion sku disc amt by setting>>
     */
    private Map<Long, List<PromotionSKUDiscAMTBySetting>> buildShopIdAndPromotionSKUDiscAMTBySettingMap(ShoppingCartCommand shoppingCartCommand){
        List<PromotionBrief> cartPromotionBriefList = shoppingCartCommand.getCartPromotionBriefList();
        List<PromotionSKUDiscAMTBySetting> promotionSKUDiscAMTBySettingList = sdkPromotionCalculationShareToSKUManager.sharePromotionDiscountToEachLine(shoppingCartCommand, cartPromotionBriefList);
        return CollectionsUtil.group(promotionSKUDiscAMTBySettingList, "shopId");
    }

    /**
     * Checks if is send email.
     *
     * @return true, if checks if is send email
     */
    private boolean isSendEmail(){
        String isSendEmailConfig = sdkMataInfoManager.findValue(MataInfo.KEY_ORDER_EMAIL);
        return isSendEmailConfig != null && isSendEmailConfig.equals("true");
    }

    /**
     * 获得 pay sum.
     *
     * @param paySum
     *            the pay sum
     * @param soPayMentDetails
     *            the so pay ment details
     * @return the pay sum
     */
    private BigDecimal getPaySum(BigDecimal paySum,List<String> soPayMentDetails){
        if (soPayMentDetails != null){
            for (String soPayMentDetail : soPayMentDetails){
                // 支付方式 String格式：shopId||payMentType||金额
                String[] strs = soPayMentDetail.split(SEPARATOR_FLAG);
                BigDecimal payMoney = new BigDecimal(strs[2]);
                paySum = paySum.subtract(payMoney);
            }
        }
        return paySum;
    }

}
