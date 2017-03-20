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

import static com.baozun.nebula.model.salesorder.SalesOrder.SALES_ORDER_STATUS_CANCELED;
import static com.baozun.nebula.model.salesorder.SalesOrder.SALES_ORDER_STATUS_SYS_CANCELED;
import static com.baozun.nebula.wormhole.constants.OrderStatusV5Constants.ORDER_CANCEL;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.baozun.nebula.sdk.command.OrderStatusLogCommand;
import com.baozun.nebula.sdk.manager.order.OrderManager;
import com.baozun.nebula.wormhole.mq.entity.order.OrderStatusV5;

import static com.feilong.core.bean.ConvertUtil.toList;

/**
 * 
 * @author <a href="http://feitianbenyue.iteye.com/">feilong</a>
 * @since 5.3.2.13
 */
@Transactional
@Service("orderStatusV5ListBuilder")
public class DefaultOrderStatusV5ListBuilder implements OrderStatusV5ListBuilder{

    private static final Logger LOGGER = LoggerFactory.getLogger(DefaultOrderStatusV5ListBuilder.class);

    @Autowired
    private OrderManager sdkOrderService;

    /*
     * (non-Javadoc)
     * 
     * @see com.baozun.nebula.wormhole.scm.makemsgcon.OrderStatusV5ListBuilder#buildOrderStatusV5List(java.lang.Long)
     */
    @Override
    public List<OrderStatusV5> buildOrderStatusV5List(Long orderStatusLogId){
        OrderStatusLogCommand orderStatusLogCommand = sdkOrderService.findOrderStatusLogById(orderStatusLogId);
        OrderStatusV5 orderStatusV5 = buildOrderStatusV5(orderStatusLogCommand);

        return toList(orderStatusV5);
    }

    /**
     * @param orderStatusLogCommand
     * @return
     */
    protected OrderStatusV5 buildOrderStatusV5(OrderStatusLogCommand orderStatusLogCommand){
        OrderStatusV5 orderStatusV5 = new OrderStatusV5();

        orderStatusV5.setBsOrderCode(orderStatusLogCommand.getOrderCode());
        //目前 2:订单取消  后续加
        if (orderStatusLogCommand.getAfterStatus().equals(SALES_ORDER_STATUS_CANCELED) || orderStatusLogCommand.getAfterStatus().equals(SALES_ORDER_STATUS_SYS_CANCELED)){
            orderStatusV5.setOpType(ORDER_CANCEL);
        }
        orderStatusV5.setOpTime(orderStatusLogCommand.getCreateTime());

        return orderStatusV5;
    }
}
