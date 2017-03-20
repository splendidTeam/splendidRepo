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
package com.baozun.nebula.wormhole.scm.makemsgcon;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.baozun.nebula.sdk.command.OrderPromotionCommand;
import com.baozun.nebula.sdk.command.SalesOrderCommand;
import com.baozun.nebula.wormhole.constants.PromotionTypeConstants;
import com.baozun.nebula.wormhole.mq.entity.order.OrderPromotionV5;

/**
 * 
 * @author <a href="http://feitianbenyue.iteye.com/">feilong</a>
 * @since 5.3.2.13
 */
@Transactional
@Service("orderPromotionV5ListBuilder")
public class DefaultOrderPromotionV5ListBuilder implements OrderPromotionV5ListBuilder{

    private static final Logger LOGGER = LoggerFactory.getLogger(DefaultOrderPromotionV5ListBuilder.class);

    /*
     * (non-Javadoc)
     * 
     * @see com.baozun.nebula.wormhole.scm.makemsgcon.OrderPromotionV5ListBuilder#buildOrderPromotionV5List(com.baozun.nebula.sdk.command.SalesOrderCommand)
     */
    @Override
    public List<OrderPromotionV5> buildOrderPromotionV5List(SalesOrderCommand salesOrderCommand){
        //订单促销 
        List<OrderPromotionV5> promotions = new ArrayList<OrderPromotionV5>();

        Map<Long, OrderPromotionCommand> map = getOrderPromotionCommandMap(salesOrderCommand);

        Iterator it = map.keySet().iterator();
        while (it.hasNext()){
            Long key = (Long) it.next();
            OrderPromotionCommand obj = map.get(key);
            if (obj.getBaseOrder()){
                OrderPromotionV5 orderPromotionV5 = new OrderPromotionV5();
                //促销类型值域由商城前端预定义(商城共用一套),维护进omsChooseOption
                orderPromotionV5.setType(PromotionTypeConstants.PROMOTION_ALL);
                //促销编码该字段目前在oms中无需备案,oms中仅记录该信息商城定义的活动编号
                orderPromotionV5.setCode(obj.getActivityId().toString());
                orderPromotionV5.setDescription(obj.getDescribe());
                orderPromotionV5.setDiscountFee(obj.getDiscountAmount());
                orderPromotionV5.setCouponCode(obj.getCoupon());
                orderPromotionV5.setRemark(null);
                promotions.add(orderPromotionV5);
            }
        }

        return promotions;
    }

    private Map<Long, OrderPromotionCommand> getOrderPromotionCommandMap(SalesOrderCommand salesOrderCommand){
        Map<Long, OrderPromotionCommand> map = new HashMap<Long, OrderPromotionCommand>();
        if (salesOrderCommand.getOrderPromotions() != null){
            for (OrderPromotionCommand orderPromotionCommand : salesOrderCommand.getOrderPromotions()){
                if (!orderPromotionCommand.getIsShipDiscount()){
                    OrderPromotionCommand obj = map.get(orderPromotionCommand.getActivityId());
                    if (obj == null){
                        map.put(orderPromotionCommand.getActivityId(), orderPromotionCommand);
                    }else{
                        orderPromotionCommand.setDiscountAmount(orderPromotionCommand.getDiscountAmount().add(obj.getDiscountAmount()));
                        map.put(orderPromotionCommand.getActivityId(), orderPromotionCommand);
                    }
                }
            }
        }
        return map;
    }

}
