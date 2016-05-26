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
import com.baozun.nebula.sdk.manager.SdkPayCodeManager;
import com.baozun.nebula.sdk.manager.SdkPayInfoLogManager;
import com.baozun.nebula.sdk.manager.SdkPayInfoManager;
import com.baozun.nebula.sdk.manager.SdkSkuInventoryManager;
import com.baozun.nebula.sdk.manager.promotion.SdkPromotionCalculationShareToSKUManager;
import com.baozun.nebula.sdk.manager.promotion.SdkPromotionCouponCodeManager;
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

    /** The Constant LOGGER. */
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

    /** The sdk order manager. */
    @Autowired
    private SdkOrderManager                          sdkOrderManager;

    /** The sdk order promotion manager. */
    @Autowired
    private SdkOrderPromotionManager                 sdkOrderPromotionManager;

    /** The sdk engine manager. */
    @Autowired
    private SdkEngineManager                         sdkEngineManager;

    /** The sdk promotion coupon code manager. */
    @Autowired
    private SdkPromotionCouponCodeManager            sdkPromotionCouponCodeManager;

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

        preCreateOrder(shoppingCartCommand, salesOrderCommand, memCombos);

        return saveOrderInfo(salesOrderCommand, shoppingCartCommand);
    }

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
                boolean noCheck = shoppingCartLineCommand.getSettlementState() == null || shoppingCartLineCommand.getSettlementState() == 0;
                if (!(shoppingCartLineCommand.isCaptionLine()
                                || (shoppingCartLineCommand.isGift() && shoppingCartLineCommand.getGiftChoiceType() == 1 && noCheck))){
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
        // 合并付款
        String subOrdinate = orderCodeCreator.createOrderSerialNO();
        if (subOrdinate == null){
            throw new BusinessException(Constants.CREATE_ORDER_FAILURE);
        }

        // 购物车
        Map<Long, ShoppingCartCommand> shopIdAndShoppingCartCommandMap = shoppingCartCommand.getShoppingCartByShopIdMap();

        // shoppingCartLineCommandMap
        Map<Long, List<ShoppingCartLineCommand>> shopIdAndShoppingCartLineCommandListMap = MapUtil
                        .extractSubMap(shopIdAndShoppingCartCommandMap, "shoppingCartLineCommands", Long.class);

        // shoppingCartPromotionBriefMap
        List<PromotionSKUDiscAMTBySetting> promotionSKUDiscAMTBySettingList = sdkPromotionCalculationShareToSKUManager
                        .sharePromotionDiscountToEachLine(shoppingCartCommand, shoppingCartCommand.getCartPromotionBriefList());

        //基于店铺的促销
        Map<Long, List<PromotionSKUDiscAMTBySetting>> shopIdAndPromotionSKUDiscAMTBySettingMap = CollectionsUtil
                        .group(promotionSKUDiscAMTBySettingList, "shopId");

        // shopCartCommandByShopMap
        Map<Long, ShopCartCommandByShop> shopIdAndShopCartCommandByShopMap = CollectionsUtil
                        .groupOne(shoppingCartCommand.getSummaryShopCartList(), "shopId");

        //*****************************************************************************************
        String isSendEmailConfig = sdkMataInfoManager.findValue(MataInfo.KEY_ORDER_EMAIL);
        boolean isSendEmail = isSendEmailConfig != null && isSendEmailConfig.equals("true");

        List<Map<String, Object>> dataMapList = new ArrayList<Map<String, Object>>();

        for (Map.Entry<Long, List<ShoppingCartLineCommand>> entry : shopIdAndShoppingCartLineCommandListMap.entrySet()){
            Long shopId = entry.getKey();
            List<ShoppingCartLineCommand> shoppingCartLineCommandList = entry.getValue();

            doWithPerShop(
                            shopId,
                            subOrdinate,
                            shoppingCartLineCommandList,
                            salesOrderCommand,
                            shopIdAndShopCartCommandByShopMap.get(shopId),
                            shopIdAndPromotionSKUDiscAMTBySettingMap.get(shopId),
                            isSendEmail,
                            dataMapList);
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
        if (salesOrderCommand.getIsImmediatelyBuy() == null || salesOrderCommand.getIsImmediatelyBuy() == false){
            sdkShoppingCartManager.emptyShoppingCart(salesOrderCommand.getMemberId());
        }

        // 发邮件
        sdkOrderEmailManager.sendEmailOfCreateOrder(dataMapList);
        return subOrdinate;
    }

    /**
     * Do with per shop.
     *
     * @param shopId
     *            the shop id
     * @param subOrdinate
     *            the sub ordinate
     * @param shoppingCartLineCommandList
     *            the shopping cart line command list
     * @param salesOrderCommand
     *            the sales order command
     * @param shopCartCommandByShop
     *            the shop cart command by shop
     * @param promotionSKUDiscAMTBySettingList
     *            the psdabs list
     * @param isSendEmail
     *            the is send email
     * @param dataMapList
     *            the data map list
     */
    private void doWithPerShop(
                    Long shopId,
                    String subOrdinate,
                    List<ShoppingCartLineCommand> shoppingCartLineCommandList,
                    SalesOrderCommand salesOrderCommand,
                    ShopCartCommandByShop shopCartCommandByShop,
                    List<PromotionSKUDiscAMTBySetting> promotionSKUDiscAMTBySettingList,
                    boolean isSendEmail,
                    List<Map<String, Object>> dataMapList){

        SalesOrder salesOrder = sdkOrderManager.savaOrder(shopId, salesOrderCommand, shopCartCommandByShop);
        Long orderId = salesOrder.getId();

        // 保存订单行、订单行优惠
        //TODO feilong 如果是bundle的话 这里多条
        savaOrderLinesAndPromotions(
                        orderId,
                        shoppingCartLineCommandList,
                        salesOrderCommand.getCouponCodes(),
                        promotionSKUDiscAMTBySettingList);

        // 保存支付详细
        BigDecimal payMainMoney = getPayMainMoney(shopId, salesOrder, salesOrderCommand.getSoPayMentDetails());
        PayInfo payInfo = sdkPayInfoManager.savePayInfoOfPayMain(salesOrderCommand, orderId, payMainMoney);
        sdkPayInfoLogManager.savePayInfoLogOfPayMain(subOrdinate, salesOrderCommand, payInfo);

        // 保存收货人信息
        sdkConsigneeManager.saveConsignee(orderId, shopId, salesOrderCommand);

        // 保存OMS消息发送记录(销售订单信息推送给SCM)
        //TODO feilong 如果是bundle的话 这里多条
        sdkMsgManager.saveMsgSendRecord(IfIdentifyConstants.IDENTIFY_ORDER_SEND, orderId, null);

        // 封装发送邮件数据
        if (isSendEmail){
            //TODO feilong 待重构
            Map<String, Object> dataMap = sdkOrderEmailManager.buildDataMapForCreateOrder(
                            subOrdinate,
                            salesOrder,
                            salesOrderCommand,
                            shoppingCartLineCommandList,
                            shopCartCommandByShop,
                            promotionSKUDiscAMTBySettingList);
            dataMapList.add(dataMap);
        }

        // 扣减库存
        sdkSkuInventoryManager.deductSkuInventory(shoppingCartLineCommandList);
    }

    /**
     * 获得 pay sum.
     *
     * @param shoppingCartCommand
     *            the shopping cart command
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

    /**
     * @param shopId
     * @param salesOrder
     * @param salesOrderCommand
     * @return
     */
    private BigDecimal getPayMainMoney(Long shopId,SalesOrder salesOrder,List<String> soPayMentDetails){
        Long orderId = salesOrder.getId();
        BigDecimal payMainMoney = salesOrder.getTotal().add(salesOrder.getActualFreight());
        // 除主支付方式之外的付款

        if (soPayMentDetails != null){
            for (String soPayMentDetail : soPayMentDetails){
                // 支付方式 String格式：shopId||payMentType||金额
                String[] strs = soPayMentDetail.split(SEPARATOR_FLAG);
                if (shopId.toString().equals(strs[0]) && strs.length == 3){
                    int payType = Integer.parseInt(strs[1]);
                    BigDecimal payMoney = new BigDecimal(strs[2]);
                    PayInfo payInfo = sdkPayInfoManager.savePayInfo(orderId, payType, payMoney);
                    payMainMoney = payMainMoney.subtract(payMoney);
                }
            }
        }
        return payMainMoney;
    }

    /**
     * 保存订单行.
     * 
     * @param orderId
     *            the order id
     * @param shoppingCartLineCommandList
     *            the scc list
     * @param promotionSKUDiscAMTBySettingList
     *            the psdabs list
     * @param salesOrderCommand
     *            the sales order command
     */
    private void savaOrderLinesAndPromotions(
                    Long orderId,
                    List<ShoppingCartLineCommand> shoppingCartLineCommandList,
                    List<CouponCodeCommand> couponCodes,
                    List<PromotionSKUDiscAMTBySetting> promotionSKUDiscAMTBySettingList){
        for (ShoppingCartLineCommand shoppingCartLineCommand : shoppingCartLineCommandList){
            //TODO feilong bundle 下单要进行拆分
            OrderLine orderLine = sdkOrderLineManager.saveOrderLine(orderId, shoppingCartLineCommand);
            if (orderLine == null){
                continue;
            }

            savaOrderLinePromotions(orderId, couponCodes, promotionSKUDiscAMTBySettingList, orderLine);
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

    private void savaOrderLinePromotions(
                    Long orderId,
                    List<CouponCodeCommand> couponCodes,
                    List<PromotionSKUDiscAMTBySetting> promotionSKUDiscAMTBySettingList,
                    OrderLine orderLine){
        if (Validator.isNullOrEmpty(promotionSKUDiscAMTBySettingList)){
            return;
        }

        for (PromotionSKUDiscAMTBySetting promotionSKUDiscAMTBySetting : promotionSKUDiscAMTBySettingList){
            boolean giftMark = promotionSKUDiscAMTBySetting.getGiftMark();
            //0代表赠品 1代表主卖品
            Integer type = !giftMark ? 1 : 0;

            // 非免运费
            if (!promotionSKUDiscAMTBySetting.getFreeShippingMark() && promotionSKUDiscAMTBySetting.getSkuId().equals(orderLine.getSkuId())
                            && orderLine.getType().equals(type)){

                sdkOrderPromotionManager.savaOrderPromotion(orderId, orderLine.getId(), promotionSKUDiscAMTBySetting, couponCodes);
            }
        }
    }
}
