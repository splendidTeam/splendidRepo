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
package com.baozun.nebula.sdk.manager.order.handler;

import java.math.BigDecimal;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.baozun.nebula.constant.IfIdentifyConstants;
import com.baozun.nebula.model.salesorder.PayInfo;
import com.baozun.nebula.model.salesorder.SalesOrder;
import com.baozun.nebula.sdk.command.CouponCodeCommand;
import com.baozun.nebula.sdk.command.SalesOrderCommand;
import com.baozun.nebula.sdk.command.shoppingcart.PromotionSKUDiscAMTBySetting;
import com.baozun.nebula.sdk.command.shoppingcart.ShopCartCommandByShop;
import com.baozun.nebula.sdk.command.shoppingcart.ShoppingCartLineCommand;
import com.baozun.nebula.sdk.manager.SdkConsigneeManager;
import com.baozun.nebula.sdk.manager.SdkMsgManager;
import com.baozun.nebula.sdk.manager.SdkPayInfoLogManager;
import com.baozun.nebula.sdk.manager.SdkPayInfoManager;
import com.baozun.nebula.sdk.manager.SdkSkuInventoryManager;
import com.baozun.nebula.sdk.manager.promotion.SdkOrderPromotionManager;
import com.baozun.nebula.sdk.manager.shoppingcart.behaviour.SdkShoppingCartLineCommandBehaviourFactory;
import com.baozun.nebula.sdk.manager.shoppingcart.behaviour.proxy.ShoppingCartLineCommandBehaviour;
import com.feilong.core.Validator;

/**
 *
 * @author <a href="http://feitianbenyue.iteye.com/">feilong</a>
 * @since 5.3.1
 */
@Transactional
@Service("orderCreateByShopManager")
public class OrderCreateByShopManagerImpl implements OrderCreateByShopManager{

    private static final Logger                        LOGGER         = LoggerFactory.getLogger(OrderCreateByShopManagerImpl.class);

    /** The Constant SEPARATOR_FLAG. */
    private static final String                        SEPARATOR_FLAG = "\\|\\|";

    /** The sdk pay info manager. */
    @Autowired
    private SdkPayInfoManager                          sdkPayInfoManager;

    /** The sdk pay info log manager. */
    @Autowired
    private SdkPayInfoLogManager                       sdkPayInfoLogManager;

    /** The sdk sku inventory manager. */
    @Autowired
    private SdkSkuInventoryManager                     sdkSkuInventoryManager;

    /** The sdk order manager. */
    @Autowired
    private OrderSaveManager                           orderSaveManager;

    /** The sdk order promotion manager. */
    @Autowired
    private SdkOrderPromotionManager                   sdkOrderPromotionManager;

    /** The sdk order line manager. */
    @Autowired
    private SdkConsigneeManager                        sdkConsigneeManager;

    /** The sdk msg manager. */
    @Autowired
    private SdkMsgManager                              sdkMsgManager;

    @Autowired
    private SdkShoppingCartLineCommandBehaviourFactory sdkShoppingCartLineCommandBehaviourFactory;

    /*
     * (non-Javadoc)
     * 
     * @see com.baozun.nebula.sdk.manager.order.OrderCreateByShopManager#doWithPerShopCreateOrder(java.lang.Long, java.lang.String,
     * java.util.List, com.baozun.nebula.sdk.command.SalesOrderCommand, com.baozun.nebula.sdk.command.shoppingcart.ShopCartCommandByShop,
     * java.util.List)
     */
    @Override
    public SalesOrder doWithPerShopCreateOrder(
                    Long shopId,
                    String subOrdinate,
                    List<ShoppingCartLineCommand> shoppingCartLineCommandList,
                    SalesOrderCommand salesOrderCommand,
                    ShopCartCommandByShop shopCartCommandByShop,
                    List<PromotionSKUDiscAMTBySetting> promotionSKUDiscAMTBySettingList){
        SalesOrder salesOrder = orderSaveManager.savaOrder(shopId, salesOrderCommand, shopCartCommandByShop);
        Long orderId = salesOrder.getId();

        List<CouponCodeCommand> couponCodes = salesOrderCommand.getCouponCodes();

        // 保存订单行、订单行优惠
        for (ShoppingCartLineCommand shoppingCartLineCommand : shoppingCartLineCommandList){
            ShoppingCartLineCommandBehaviour shoppingCartLineCommandBehaviour = sdkShoppingCartLineCommandBehaviourFactory
                            .getShoppingCartLineCommandBehaviour(shoppingCartLineCommand);
            shoppingCartLineCommandBehaviour.saveOrderLine(orderId, couponCodes, promotionSKUDiscAMTBySettingList, shoppingCartLineCommand);
        }

        // 免运费
        if (Validator.isNotNullOrEmpty(promotionSKUDiscAMTBySettingList)){
            for (PromotionSKUDiscAMTBySetting promotionSKUDiscAMTBySetting : promotionSKUDiscAMTBySettingList){
                if (promotionSKUDiscAMTBySetting.getFreeShippingMark()){
                    sdkOrderPromotionManager.saveOrderShipPromotion(orderId, promotionSKUDiscAMTBySetting);
                }
            }
        }

        // 保存支付详细
        BigDecimal payMainMoney = getPayMainMoney(shopId, salesOrder, salesOrderCommand.getSoPayMentDetails());
        PayInfo payInfo = sdkPayInfoManager.savePayInfoOfPayMain(salesOrderCommand, orderId, payMainMoney);
        sdkPayInfoLogManager.savePayInfoLogOfPayMain(subOrdinate, salesOrderCommand, payInfo);

        // 保存收货人信息
        sdkConsigneeManager.saveConsignee(orderId, shopId, salesOrderCommand);

        // 保存OMS消息发送记录(销售订单信息推送给SCM)
        sdkMsgManager.saveMsgSendRecord(IfIdentifyConstants.IDENTIFY_ORDER_SEND, orderId, null);

        // 扣减库存
        sdkSkuInventoryManager.deductSkuInventory(shoppingCartLineCommandList);

        return salesOrder;
    }

    /**
     * 获得 pay main money.
     *
     * @param shopId
     *            the shop id
     * @param salesOrder
     *            the sales order
     * @param soPayMentDetails
     *            the so pay ment details
     * @return the pay main money
     */
    private BigDecimal getPayMainMoney(Long shopId,SalesOrder salesOrder,List<String> soPayMentDetails){
        BigDecimal actualFreight = salesOrder.getActualFreight();
        BigDecimal total = salesOrder.getTotal();
        BigDecimal payMainMoney = total.add(actualFreight);
        // 除主支付方式之外的付款

        Long orderId = salesOrder.getId();
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
}
