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
package com.baozun.nebula.sdk.manager.shoppingcart.behaviour.createline;

import java.util.List;
import java.util.Objects;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import com.baozun.nebula.calculateEngine.param.GiftChoiceType;
import com.baozun.nebula.dao.salesorder.SdkOrderLineDao;
import com.baozun.nebula.model.salesorder.OrderLine;
import com.baozun.nebula.sdk.command.CouponCodeCommand;
import com.baozun.nebula.sdk.command.shoppingcart.PromotionSKUDiscAMTBySetting;
import com.baozun.nebula.sdk.command.shoppingcart.ShoppingCartLineCommand;
import com.baozun.nebula.sdk.manager.promotion.SdkOrderPromotionManager;

import static com.feilong.core.Validator.isNotNullOrEmpty;

/**
 * The Class AbstractShoppingCartLineCommandCreateLineBehaviour.
 *
 * @author <a href="http://feitianbenyue.iteye.com/">feilong</a>
 * @since 5.3.1
 */
public abstract class AbstractShoppingCartLineCommandCreateLineBehaviour implements ShoppingCartLineCommandCreateLineBehaviour{

    /** The Constant log. */
    private static final Logger LOGGER = LoggerFactory.getLogger(AbstractShoppingCartLineCommandCreateLineBehaviour.class);

    /** The sdk order line dao. */
    @Autowired
    private SdkOrderLineDao sdkOrderLineDao;

    /** The sdk order promotion manager. */
    @Autowired
    private SdkOrderPromotionManager sdkOrderPromotionManager;

    @Autowired
    private OrderLineBuilder orderLineBuilder;

    @Autowired
    private OrderLinePackInfoCreateManager orderLinePackInfoCreateManager;

    /*
     * (non-Javadoc)
     * 
     * @see com.baozun.nebula.sdk.manager.order.SdkOrderLineCreateManager#saveOrderLine(java.lang.Long, java.util.List, java.util.List,
     * com.baozun.nebula.sdk.command.shoppingcart.ShoppingCartLineCommand)
     */
    @Override
    public void saveOrderLine(Long orderId,List<CouponCodeCommand> couponCodes,List<PromotionSKUDiscAMTBySetting> promotionSKUDiscAMTBySettingList,ShoppingCartLineCommand shoppingCartLineCommand){
        saveCommonLine(orderId, couponCodes, promotionSKUDiscAMTBySettingList, shoppingCartLineCommand);
    }

    /**
     * Save common line.
     *
     * @param orderId
     *            the order id
     * @param couponCodes
     *            the coupon codes
     * @param promotionSKUDiscAMTBySettingList
     *            the promotion sku disc amt by setting list
     * @param shoppingCartLineCommand
     *            the shopping cart line command
     */
    //XXX feilong  这里的参数 shoppingCartLineCommand 最好是单独定制 一个额外的类 这样不会影响原来的对象
    protected void saveCommonLine(Long orderId,List<CouponCodeCommand> couponCodes,List<PromotionSKUDiscAMTBySetting> promotionSKUDiscAMTBySettingList,ShoppingCartLineCommand shoppingCartLineCommand){
        //直推赠品(送完即止) 1：如果数量为零 该行不存入数据库 :2：如果库存量小于购买量时 存入库存量

        //是否是不需要用户选择的礼品
        boolean noNeedChoiceGift = shoppingCartLineCommand.isGift() && GiftChoiceType.NoNeedChoice.equals(shoppingCartLineCommand.getGiftChoiceType());
        if (noNeedChoiceGift){
            // 下架
            boolean isOffsale = !shoppingCartLineCommand.isValid() && shoppingCartLineCommand.getValidType() == 1;
            if (isOffsale){
                return;
            }
            // 无库存
            Integer stock = shoppingCartLineCommand.getStock();
            boolean isNostock = null == stock || stock <= 0;
            if (isNostock){
                return;
            }
            if (stock < shoppingCartLineCommand.getQuantity()){
                shoppingCartLineCommand.setQuantity(stock);// 库存不足
            }
        }

        //保存订单行
        OrderLine orderLine = sdkOrderLineDao.save(orderLineBuilder.buildOrderLine(orderId, shoppingCartLineCommand));

        //保存行促销
        if (isNotNullOrEmpty(promotionSKUDiscAMTBySettingList)){
            savaOrderLinePromotions(orderId, couponCodes, promotionSKUDiscAMTBySettingList, orderLine);
        }

        //保存包装信息
        orderLinePackInfoCreateManager.saveOrderLinePackInfo(orderLine, shoppingCartLineCommand);
    }

    /**
     * Sava order line promotions.
     *
     * @param orderId
     *            the order id
     * @param couponCodes
     *            the coupon codes
     * @param promotionSKUDiscAMTBySettingList
     *            the promotion sku disc amt by setting list
     * @param orderLine
     *            the order line
     */
    protected void savaOrderLinePromotions(Long orderId,List<CouponCodeCommand> couponCodes,List<PromotionSKUDiscAMTBySetting> promotionSKUDiscAMTBySettingList,OrderLine orderLine){

        // 非免运费
        Long orderLineSkuId = orderLine.getSkuId();
        Integer orderLineType = orderLine.getType();
        Long orderLineId = orderLine.getId();

        for (PromotionSKUDiscAMTBySetting promotionSKUDiscAMTBySetting : promotionSKUDiscAMTBySettingList){
            boolean giftMark = promotionSKUDiscAMTBySetting.getGiftMark();
            //0代表赠品 1代表主卖品
            Integer type = !giftMark ? 1 : 0;

            boolean freeShippingMark = promotionSKUDiscAMTBySetting.getFreeShippingMark();
            boolean equalsType = Objects.equals(orderLineType, type);
            boolean equalsSkuId = Objects.equals(promotionSKUDiscAMTBySetting.getSkuId(), orderLineSkuId);

            if (!freeShippingMark && equalsSkuId && equalsType){
                sdkOrderPromotionManager.savaOrderPromotion(orderId, orderLineId, promotionSKUDiscAMTBySetting, couponCodes);
            }
        }
    }

}
