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
package com.baozun.nebula.web.controller.order.builder;

import static com.baozun.nebula.model.salesorder.SalesOrder.SALES_ORDER_FISTATUS_NO_PAYMENT;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import com.baozun.nebula.web.command.OrderQueryCommand;
import com.baozun.nebula.web.controller.order.form.OrderQueryForm;
import com.baozun.nebula.web.controller.order.form.OrderTimeType;
import com.feilong.core.Validator;
import com.feilong.core.bean.PropertyUtil;

/**
 * 
 * @author <a href="http://feitianbenyue.iteye.com/">feilong</a>
 * @since 5.3.2.11-Personalise
 */
@Component("orderQueryCommandBuilder")
public class DefaultOrderQueryCommandBuilder implements OrderQueryCommandBuilder{

    private static final Logger LOGGER = LoggerFactory.getLogger(DefaultOrderQueryCommandBuilder.class);

    /**
     * 新建（未支付）
     */
    private static final String ORDER_STATUS_NEW = "new";

    /*
     * (non-Javadoc)
     * 
     * @see com.baozun.nebula.web.controller.order.builder.OrderQueryCommandBuilder#build(com.baozun.nebula.web.controller.order.form.OrderQueryForm)
     */
    @Override
    public OrderQueryCommand build(OrderQueryForm orderQueryForm){
        OrderQueryCommand orderQueryCommand = new OrderQueryCommand();

        PropertyUtil.copyProperties(orderQueryCommand, orderQueryForm, "itemName", "itemCode", "orderCode");

        //订单的时间类型判断及转换
        String orderTimeType = orderQueryForm.getOrderTimeType();
        if (Validator.isNotNullOrEmpty(orderTimeType)){
            OrderTimeType orderTimeTypeEnum = OrderTimeType.getInstance(orderTimeType);
            Date[] beginAndEndDate = orderTimeTypeEnum.getBeginAndEndDate();
            orderQueryCommand.setStartDate(beginAndEndDate[0]);
            orderQueryCommand.setEndDate(beginAndEndDate[1]);
        }

        //订单类型（未支付，发货中，以完成，取消）
        String orderStatus = orderQueryForm.getOrderStatus();
        if (Validator.isNotNullOrEmpty(orderStatus)){
            List<Integer> financestatusList = new ArrayList<>();
            //未完成的订单
            if (orderStatus.equals(ORDER_STATUS_NEW)){
                financestatusList.add(SALES_ORDER_FISTATUS_NO_PAYMENT);
            }
            //TODO 其他状态的判断
        }
        return orderQueryCommand;
    }
}
