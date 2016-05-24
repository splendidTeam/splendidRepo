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
package com.baozun.nebula.sdk.manager.impl;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.baozun.nebula.api.salesorder.OrderCodeCreatorManager;
import com.baozun.nebula.constant.IfIdentifyConstants;
import com.baozun.nebula.dao.promotion.PromotionCouponCodeDao;
import com.baozun.nebula.exception.BusinessException;
import com.baozun.nebula.model.salesorder.OrderLine;
import com.baozun.nebula.model.salesorder.PayInfo;
import com.baozun.nebula.model.salesorder.SalesOrder;
import com.baozun.nebula.model.system.MataInfo;
import com.baozun.nebula.sdk.command.CouponCodeCommand;
import com.baozun.nebula.sdk.command.SalesOrderCommand;
import com.baozun.nebula.sdk.command.shoppingcart.PromotionSKUDiscAMTBySetting;
import com.baozun.nebula.sdk.command.shoppingcart.ShopCartCommandByShop;
import com.baozun.nebula.sdk.command.shoppingcart.ShoppingCartCommand;
import com.baozun.nebula.sdk.command.shoppingcart.ShoppingCartLineCommand;
import com.baozun.nebula.sdk.constants.Constants;
import com.baozun.nebula.sdk.manager.SdkConsigneeManager;
import com.baozun.nebula.sdk.manager.SdkEffectiveManager;
import com.baozun.nebula.sdk.manager.SdkEngineManager;
import com.baozun.nebula.sdk.manager.SdkMataInfoManager;
import com.baozun.nebula.sdk.manager.SdkMsgManager;
import com.baozun.nebula.sdk.manager.SdkOrderCreateManager;
import com.baozun.nebula.sdk.manager.SdkOrderEmailManager;
import com.baozun.nebula.sdk.manager.SdkOrderLineManager;
import com.baozun.nebula.sdk.manager.SdkOrderManager;
import com.baozun.nebula.sdk.manager.SdkOrderPromotionManager;
import com.baozun.nebula.sdk.manager.SdkPayCodeManager;
import com.baozun.nebula.sdk.manager.SdkPayInfoLogManager;
import com.baozun.nebula.sdk.manager.SdkPayInfoManager;
import com.baozun.nebula.sdk.manager.SdkPromotionCalculationShareToSKUManager;
import com.baozun.nebula.sdk.manager.SdkSkuInventoryManager;
import com.baozun.nebula.sdk.manager.shoppingcart.SdkShoppingCartManager;
import com.feilong.core.Validator;
import com.feilong.core.util.CollectionsUtil;
import com.feilong.core.util.MapUtil;

/**
 * 提取出来,专注于创建订单.
 * 
 * @author feilong
 * @version 5.3.1 2016年5月14日 下午6:55:04
 * @since 5.3.1
 */
@Transactional
@Service("sdkOrderCreateManager")
public class SdkOrderCreateManagerImpl implements SdkOrderCreateManager{

    private static final Logger                      LOGGER         = LoggerFactory.getLogger(SdkOrderCreateManagerImpl.class);

    /** The Constant SEPARATOR_FLAG. */
    private static final String                      SEPARATOR_FLAG = "\\|\\|";

    /** The sdk order line manager. */
    @Autowired
    private SdkOrderLineManager                      sdkOrderLineManager;

    /** The sdk order line manager. */
    @Autowired
    private SdkConsigneeManager                      sdkConsigneeManager;

    /** The sdk msg manager. */
    @Autowired
    private SdkMsgManager                            sdkMsgManager;

    /** The sdk pay code manager. */
    @Autowired
    private SdkPayCodeManager                        sdkPayCodeManager;

    /** The sdk pay info manager. */
    @Autowired
    private SdkPayInfoManager                        sdkPayInfoManager;

    /** The sdk pay info log manager. */
    @Autowired
    private SdkPayInfoLogManager                     sdkPayInfoLogManager;

    /** The sdk shopping cart manager. */
    @Autowired
    private SdkShoppingCartManager                   sdkShoppingCartManager;

    /** The sdk sku inventory manager. */
    @Autowired
    private SdkSkuInventoryManager                   sdkSkuInventoryManager;

    /** The promotion coupon code dao. */
    @Autowired
    private PromotionCouponCodeDao                   promotionCouponCodeDao;

    /** The sdk order manager. */
    @Autowired
    private SdkOrderManager                          sdkOrderManager;

    /** The sdk order promotion manager. */
    @Autowired
    private SdkOrderPromotionManager                 sdkOrderPromotionManager;

    /** The sdk engine manager. */
    @Autowired
    private SdkEngineManager                         sdkEngineManager;

    /** The sdk mata info manager. */
    @Autowired
    private SdkMataInfoManager                       sdkMataInfoManager;

    /** The sdk effective manager. */
    @Autowired
    private SdkEffectiveManager                      sdkEffectiveManager;

    /** The sdk order email manager. */
    @Autowired
    private SdkOrderEmailManager                     sdkOrderEmailManager;

    /** The order code creator. */
    @Autowired(required = false)
    private OrderCodeCreatorManager                  orderCodeCreator;

    /** The sdk promotion calculation share to sku manager. */
    @Autowired
    private SdkPromotionCalculationShareToSKUManager sdkPromotionCalculationShareToSKUManager;

    /*
     * (non-Javadoc)
     * 
     * @see com.baozun.nebula.sdk.manager.SdkOrderCreateManager#saveOrder(com.baozun.nebula.sdk.command.shoppingcart.ShoppingCartCommand,
     * com.baozun.nebula.sdk.command.SalesOrderCommand, java.util.Set)
     */
    @Override
    public String saveOrder(ShoppingCartCommand shoppingCartCommand,SalesOrderCommand salesOrderCommand,Set<String> memCombos){
        if (salesOrderCommand == null || shoppingCartCommand == null){
            throw new BusinessException(Constants.SHOPCART_IS_NULL);
        }

        //去除抬头和未选中的商品
        refactoringShoppingCartCommand(shoppingCartCommand);

        // 下单之前的引擎检查
        sdkEngineManager.createOrderDoEngineChck(salesOrderCommand.getMemberId(), memCombos, shoppingCartCommand);

        return saveOrderInfo(salesOrderCommand, shoppingCartCommand);
    }

    /*
     * (non-Javadoc)
     * 
     * @see com.baozun.nebula.sdk.manager.OrderManager#saveManualOrder(com.baozun.nebula.sdk.command.shoppingcart.ShoppingCartCommand,
     * com.baozun.nebula.sdk.command.SalesOrderCommand)
     */
    @Override
    public String saveManualOrder(ShoppingCartCommand shoppingCartCommand,SalesOrderCommand salesOrderCommand){
        if (salesOrderCommand == null || shoppingCartCommand == null){
            throw new BusinessException(Constants.SHOPCART_IS_NULL);
        }
        // 下单之前的库存检查
        for (ShoppingCartLineCommand shoppingCartLine : shoppingCartCommand.getShoppingCartLineCommands()){
            Boolean retflag = sdkEffectiveManager.chckInventory(shoppingCartLine.getExtentionCode(), shoppingCartLine.getQuantity());
            if (!retflag){
                // 库存不足
                throw new BusinessException(
                                Constants.THE_ORDER_CONTAINS_INVENTORY_SHORTAGE_ITEM,
                                new Object[] { shoppingCartLine.getItemName() });
            }
        }
        String subOrdinate = saveOrderInfo(salesOrderCommand, shoppingCartCommand);
        // 没有成功保存订单
        if (subOrdinate == null){
            LOGGER.warn("savedOrder returns null!");
            throw new BusinessException(Constants.CREATE_ORDER_FAILURE);
        }
        return subOrdinate;
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
                if (!(shoppingCartLineCommand.isCaptionLine() || (shoppingCartLineCommand.isGift()
                                && shoppingCartLineCommand.getGiftChoiceType() == 1 && (shoppingCartLineCommand.getSettlementState() == null
                                                || shoppingCartLineCommand.getSettlementState() == 0)))){
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
     * @return the string
     */
    private String saveOrderInfo(SalesOrderCommand salesOrderCommand,ShoppingCartCommand shoppingCartCommand){
        // 购物车行
        Map<Long, ShoppingCartCommand> shopIdAndShoppingCartCommandMap = shoppingCartCommand.getShoppingCartByShopIdMap();

        // shoppingCartLineCommandMap
        Map<Long, List<ShoppingCartLineCommand>> shopIdAndShoppingCartLineCommandListMap = MapUtil
                        .extractSubMap(shopIdAndShoppingCartCommandMap, "shoppingCartLineCommands", Long.class);

        // shoppingCartPromotionBriefMap
        List<PromotionSKUDiscAMTBySetting> promotionSKUDiscAMTBySettingList = sdkPromotionCalculationShareToSKUManager
                        .sharePromotionDiscountToEachLine(shoppingCartCommand, shoppingCartCommand.getCartPromotionBriefList());

        Map<Long, List<PromotionSKUDiscAMTBySetting>> shopIdAndPromotionSKUDiscAMTBySettingMap = CollectionsUtil
                        .group(promotionSKUDiscAMTBySettingList, "shopId");

        // shopCartCommandByShopMap
        List<ShopCartCommandByShop> shopCartCommandByShopList = shoppingCartCommand.getSummaryShopCartList();
        Map<Long, ShopCartCommandByShop> shopIdAndShopCartCommandByShopMap = CollectionsUtil.groupOne(shopCartCommandByShopList, "shopId");

        //*****************************************************************************************

        // 合并付款
        String subOrdinate = orderCodeCreator.createOrderSerialNO();
        if (subOrdinate == null){
            throw new BusinessException(Constants.CREATE_ORDER_FAILURE);
        }
        //*****************************************************************************************
        String isSendEmail = sdkMataInfoManager.findValue(MataInfo.KEY_ORDER_EMAIL);
        BigDecimal paySum = getPaySum(salesOrderCommand, shoppingCartCommand);

        List<Map<String, Object>> dataMapList = new ArrayList<Map<String, Object>>();

        for (Map.Entry<Long, List<ShoppingCartLineCommand>> entry : shopIdAndShoppingCartLineCommandListMap.entrySet()){
            Long shopId = entry.getKey();
            List<ShoppingCartLineCommand> shoppingCartLineCommandList = shopIdAndShoppingCartLineCommandListMap.get(shopId);

            List<PromotionSKUDiscAMTBySetting> psdabsList = shopIdAndPromotionSKUDiscAMTBySettingMap.get(shopId);
            ShopCartCommandByShop shopCartCommandByShop = shopIdAndShopCartCommandByShopMap.get(shopId);

            //***************************************************************************************
            // 根据shopId保存订单概要
            SalesOrder salesOrder = sdkOrderManager.savaOrder(shopId, salesOrderCommand, shopCartCommandByShop);
            Long orderId = salesOrder.getId();

            // 保存订单行、订单行优惠
            savaOrderLinesAndPromotions(salesOrderCommand, orderId, shoppingCartLineCommandList, psdabsList);

            // 保存支付详细
            savePayInfoAndPayInfoLog(shopId, subOrdinate, salesOrderCommand, salesOrder);

            // 保存收货人信息
            sdkConsigneeManager.saveConsignee(orderId, shopId, salesOrderCommand);

            // 保存OMS消息发送记录(销售订单信息推送给SCM)
            sdkMsgManager.saveMsgSendRecord(IfIdentifyConstants.IDENTIFY_ORDER_SEND, orderId, null);

            // 封装发送邮件数据
            if (isSendEmail != null && isSendEmail.equals("true")){
                Map<String, Object> dataMap = sdkOrderEmailManager.buildDataMapForCreateOrder(
                                subOrdinate,
                                salesOrder,
                                salesOrderCommand,
                                shoppingCartLineCommandList,
                                shopCartCommandByShop,
                                psdabsList);
                if (dataMap != null)
                    dataMapList.add(dataMap);
            }

            // 扣减库存
            sdkSkuInventoryManager.deductSkuInventory(shoppingCartLineCommandList);
        }

        //*************************************************************************************
        // 保存支付流水
        sdkPayCodeManager.savaPayCode(paySum, subOrdinate);

        //*************************************************************************************
        // 优惠券状体置为已使用 isUsed = 1
        if (Validator.isNotNullOrEmpty(salesOrderCommand.getCouponCodes())){
            for (CouponCodeCommand couponCodeCommand : salesOrderCommand.getCouponCodes()){
                List<String> list = new ArrayList<String>();
                if (!couponCodeCommand.getIsOut()){
                    list.add(couponCodeCommand.getCouponCode());
                }
                int res = promotionCouponCodeDao.comsumePromotionCouponCodeByCouponCodes(list);
                if (res != list.size()){
                    throw new BusinessException(res < list.size() ? Constants.COUPON_IS_USED : Constants.CREATE_ORDER_FAILURE);
                }
            }
        }
        // 清空购物车
        if (salesOrderCommand.getIsImmediatelyBuy() == null || salesOrderCommand.getIsImmediatelyBuy() == false){
            sdkShoppingCartManager.emptyShoppingCart(salesOrderCommand.getMemberId());
        }

        // 发邮件
        sdkOrderEmailManager.sendEmailOfCreateOrder(dataMapList);
        return subOrdinate;
    }

    /**
     * 获得 pay sum.
     *
     * @param salesOrderCommand
     *            the sales order command
     * @param shoppingCartCommand
     *            the shopping cart command
     * @return the pay sum
     */
    private BigDecimal getPaySum(SalesOrderCommand salesOrderCommand,ShoppingCartCommand shoppingCartCommand){
        BigDecimal paySum = shoppingCartCommand.getCurrentPayAmount();
        List<String> soPayMentDetails = salesOrderCommand.getSoPayMentDetails();
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

    /**
     * Save pay info and pay info log.
     * 
     * @param shopId
     *            the shop id
     * @param subOrdinate
     *            the sub ordinate
     * @param salesOrderCommand
     *            the sales order command
     * @param salesOrder
     *            the sales order
     */
    private void savePayInfoAndPayInfoLog(Long shopId,String subOrdinate,SalesOrderCommand salesOrderCommand,SalesOrder salesOrder){
        List<String> soPayMentDetails = salesOrderCommand.getSoPayMentDetails();
        BigDecimal payMainMoney = salesOrder.getTotal().add(salesOrder.getActualFreight());
        // 除主支付方式之外的付款
        Long orderId = salesOrder.getId();
        if (soPayMentDetails != null){
            for (String soPayMentDetail : soPayMentDetails){
                // 支付方式 String格式：shopId||payMentType||金额
                String[] strs = soPayMentDetail.split(SEPARATOR_FLAG);
                if (shopId.toString().equals(strs[0]) && strs.length == 3){
                    PayInfo payInfo = sdkPayInfoManager.savePayInfo(orderId, Integer.parseInt(strs[1]), new BigDecimal(strs[2]));
                    payMainMoney = payMainMoney.subtract(payInfo.getPayMoney());
                }
            }
        }
        PayInfo payInfo = sdkPayInfoManager.savePayInfoOfPayMain(salesOrderCommand, orderId, payMainMoney);
        sdkPayInfoLogManager.savePayInfoLogOfPayMain(salesOrderCommand, subOrdinate, payInfo);
    }

    /**
     * 保存订单行.
     *
     * @param salesOrderCommand
     *            the sales order command
     * @param orderId
     *            the order id
     * @param shoppingCartLineCommandList
     *            the scc list
     * @param promotionSKUDiscAMTBySettingList
     *            the psdabs list
     */
    private void savaOrderLinesAndPromotions(
                    SalesOrderCommand salesOrderCommand,
                    Long orderId,
                    List<ShoppingCartLineCommand> shoppingCartLineCommandList,
                    List<PromotionSKUDiscAMTBySetting> promotionSKUDiscAMTBySettingList){
        for (ShoppingCartLineCommand shoppingCartLineCommand : shoppingCartLineCommandList){
            OrderLine orderLine = sdkOrderLineManager.saveOrderLine(orderId, shoppingCartLineCommand);
            if (orderLine == null){
                continue;
            }

            if (Validator.isNullOrEmpty(promotionSKUDiscAMTBySettingList)){
                continue;
            }

            for (PromotionSKUDiscAMTBySetting promotionSKUDiscAMTBySetting : promotionSKUDiscAMTBySettingList){
                boolean giftMark = promotionSKUDiscAMTBySetting.getGiftMark();
                //0代表赠品 1代表主卖品
                Integer type = !giftMark ? 1 : 0;

                // 非免运费
                if (!promotionSKUDiscAMTBySetting.getFreeShippingMark()
                                && promotionSKUDiscAMTBySetting.getSkuId().equals(orderLine.getSkuId())
                                && orderLine.getType().equals(type)){
                    sdkOrderPromotionManager
                                    .savaOrderPromotion(orderId, promotionSKUDiscAMTBySetting, orderLine.getId(), salesOrderCommand);
                }
            }
        }
        // 免运费
        if (Validator.isNotNullOrEmpty(promotionSKUDiscAMTBySettingList)){
            for (PromotionSKUDiscAMTBySetting promotionSKUDiscAMTBySetting : promotionSKUDiscAMTBySettingList){
                if (promotionSKUDiscAMTBySetting.getFreeShippingMark()){
                    sdkOrderPromotionManager.saveOrderShipPromotion(orderId, promotionSKUDiscAMTBySetting);
                }
            }
        }
    }
}
