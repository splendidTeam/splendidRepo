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
package com.baozun.nebula.web.controller.order.builder.subview;

import java.util.List;

import org.springframework.stereotype.Component;

import com.baozun.nebula.sdk.command.OrderPromotionCommand;
import com.baozun.nebula.sdk.command.SalesOrderCommand;
import com.baozun.nebula.web.controller.order.viewcommand.CouponInfoSubViewCommand;
import com.feilong.core.Validator;

/**
 * 专门用来构造 {@link CouponInfoSubViewCommand}.
 * 
 * @author <a href="http://feitianbenyue.iteye.com/">feilong</a>
 * @since 5.3.2.18
 */
@Component("couponInfoSubViewCommandBuilder")
public class DefaultCouponInfoSubViewCommandBuilder implements CouponInfoSubViewCommandBuilder{

    /*
     * (non-Javadoc)
     * 
     * @see com.baozun.nebula.web.controller.order.builder.subview.CouponInfoSubViewCommandBuilder#buildCouponInfoSubViewCommand(com.baozun.nebula.sdk.command.SalesOrderCommand)
     */
    @Override
    public CouponInfoSubViewCommand build(SalesOrderCommand salesOrderCommand){
        // 优惠券信息
        CouponInfoSubViewCommand couponInfoSubViewCommand = new CouponInfoSubViewCommand();
        List<OrderPromotionCommand> orderPromotions = salesOrderCommand.getOrderPromotions();
        if (null != orderPromotions && orderPromotions.size() != 0){
            String coupon = orderPromotions.get(0).getCoupon();
            if (Validator.isNotNullOrEmpty(coupon)){
                coupon = coupon.replace("[", "").replace("]", "");
                couponInfoSubViewCommand.setCouponCode(coupon);
            }
        }
        return couponInfoSubViewCommand;
    }
}
