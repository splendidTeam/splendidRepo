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
package com.baozun.nebula.sdk.manager.promotion;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.baozun.nebula.dao.salesorder.SdkOrderPromotionDao;
import com.baozun.nebula.model.salesorder.OrderPromotion;
import com.baozun.nebula.sdk.command.CouponCodeCommand;
import com.baozun.nebula.sdk.command.shoppingcart.PromotionSKUDiscAMTBySetting;

/**
 * The Class SdkOrderPromotionManagerImpl.
 *
 * @author feilong
 * @since 5.3.1
 */
@Transactional
@Service("sdkOrderPromotionManager")
public class SdkOrderPromotionManagerImpl implements SdkOrderPromotionManager{

    /** The sdk order promotion dao. */
    @Autowired
    private SdkOrderPromotionDao sdkOrderPromotionDao;

    /*
     * (non-Javadoc)
     * 
     * @see com.baozun.nebula.sdk.manager.SdkOrderPromotionManager#saveOrderPromotion(java.lang.Long,
     * com.baozun.nebula.sdk.command.shoppingcart.PromotionSKUDiscAMTBySetting)
     */
    @Override
    public void saveOrderShipPromotion(Long orderId,PromotionSKUDiscAMTBySetting promotionSKUDiscAMTBySetting){
        Long promotionId = promotionSKUDiscAMTBySetting.getPromotionId();

        OrderPromotion orderPromotion = new OrderPromotion();
        // 是否运费折扣
        orderPromotion.setIsShipDiscount(true);

        // 订单id
        orderPromotion.setOrderId(orderId);
        // 活动id
        orderPromotion.setActivityId(promotionId);
        // 促销码 (暂时没用)
        orderPromotion.setPromotionNo(promotionId.toString());
        // 促销类型
        orderPromotion.setPromotionType(promotionSKUDiscAMTBySetting.getPromotionType());
        // 折扣金额
        orderPromotion.setDiscountAmount(promotionSKUDiscAMTBySetting.getDiscountAmount());

        // 优惠券
        Set<String> couponCodes = promotionSKUDiscAMTBySetting.getCouponCodes();
        if (couponCodes != null){
            orderPromotion.setCoupon(couponCodes.toString());
        }
        // 描述 ...name
        orderPromotion.setDescribe(promotionSKUDiscAMTBySetting.getPromotionName());
        // 是否基于整单
        orderPromotion.setBaseOrder(promotionSKUDiscAMTBySetting.getBaseOrder());
        sdkOrderPromotionDao.save(orderPromotion);
    }

    /*
     * (non-Javadoc)
     * 
     * @see com.baozun.nebula.sdk.manager.SdkOrderPromotionManager#savaOrderPromotion(java.lang.Long,
     * com.baozun.nebula.sdk.command.shoppingcart.PromotionSKUDiscAMTBySetting, java.lang.Long,
     * com.baozun.nebula.sdk.command.SalesOrderCommand)
     */
    @Override
    public void savaOrderPromotion(
                    Long orderId,
                    Long orderLineId,
                    PromotionSKUDiscAMTBySetting promotionSKUDiscAMTBySetting,
                    List<CouponCodeCommand> couponCodes){

        OrderPromotion orderPromotion = new OrderPromotion();

        // 是否运费折扣
        orderPromotion.setIsShipDiscount(false);

        // 订单id
        orderPromotion.setOrderId(orderId);

        // 活动id
        orderPromotion.setActivityId(promotionSKUDiscAMTBySetting.getPromotionId());

        // 订单行
        orderPromotion.setOrderLineId(orderLineId);

        // 促销码 (暂时没用)
        orderPromotion.setPromotionNo(promotionSKUDiscAMTBySetting.getPromotionId().toString());
        // 促销类型
        orderPromotion.setPromotionType(promotionSKUDiscAMTBySetting.getPromotionType());
        // 折扣金额
        orderPromotion.setDiscountAmount(promotionSKUDiscAMTBySetting.getDiscountAmount());

        // 优惠券
        Set<String> couponCodesSet = promotionSKUDiscAMTBySetting.getCouponCodes();
        Set<String> newSet = new HashSet<String>();
        if (couponCodesSet != null){
            List<String> list = new ArrayList<String>(couponCodesSet);
            for (String couponcode : list){
                if (couponcode.equals(CouponCodeCommand.BRUSHHEAD_COUPON)){

                    for (CouponCodeCommand couponCodeCommand : couponCodes){
                        if (couponCodeCommand.getIsOut()){
                            newSet.add(couponCodeCommand.getCouponCode());
                        }
                    }
                }else{
                    newSet.add(couponcode);
                }
            }
            orderPromotion.setCoupon(newSet.toString());
        }

        // 描述 ...name
        orderPromotion.setDescribe(promotionSKUDiscAMTBySetting.getPromotionName());
        // 是否基于整单
        orderPromotion.setBaseOrder(promotionSKUDiscAMTBySetting.getBaseOrder());
        sdkOrderPromotionDao.save(orderPromotion);
    }
}
