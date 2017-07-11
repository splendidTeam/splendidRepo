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

import static org.apache.commons.lang3.ObjectUtils.defaultIfNull;

import java.util.List;

import org.apache.commons.lang3.Validate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.baozun.nebula.sdk.command.OrderLineCommand;
import com.baozun.nebula.sdk.command.SalesOrderCommand;
import com.baozun.nebula.web.controller.order.builder.DefaultOrderViewStatusBuilder;
import com.baozun.nebula.web.controller.order.builder.OrderViewStatusBuilder;
import com.baozun.nebula.web.controller.order.builder.OrderViewStatusParam;
import com.baozun.nebula.web.controller.order.viewcommand.OrderBaseInfoSubViewCommand;
import com.baozun.nebula.web.controller.order.viewcommand.OrderViewStatus;
import com.feilong.core.bean.PropertyUtil;

import static com.feilong.core.util.CollectionsUtil.find;

/**
 * 专门用来构造 {@link OrderBaseInfoSubViewCommand}.
 * 
 * @author <a href="http://feitianbenyue.iteye.com/">feilong</a>
 * @since 5.3.2.18
 */
@Component("orderBaseInfoSubViewCommandBuilder")
public class DefaultOrderBaseInfoSubViewCommandBuilder implements OrderBaseInfoSubViewCommandBuilder{

    /** 用来构造 OrderViewStatus */
    @Autowired(required = false)
    private OrderViewStatusBuilder orderViewStatusBuilder;

    @Autowired
    private DisplayTotalBuilder displayTotalBuilder;

    /*
     * (non-Javadoc)
     * 
     * @see com.baozun.nebula.web.controller.order.builder.subview.OrderBaseInfoSubViewCommandBuilder#buildOrderBaseInfoSubViewCommand(com.baozun.nebula.sdk.command.SalesOrderCommand)
     */
    @Override
    public OrderBaseInfoSubViewCommand build(SalesOrderCommand salesOrderCommand){
        Validate.notNull(salesOrderCommand, "salesOrderCommand can't be null!");

        // 订单信息
        OrderBaseInfoSubViewCommand orderBaseInfoSubViewCommand = new OrderBaseInfoSubViewCommand();
        //5.3.2.18增加了对"orderType"字段的转换
        //5.3.2.20增加了对"modifyTime"字段的转换
        PropertyUtil.copyProperties(orderBaseInfoSubViewCommand, salesOrderCommand, "createTime", "logisticsStatus", "financialStatus", "total", "discount", "actualFreight", "orderType","modifyTime");

        orderBaseInfoSubViewCommand.setOrderId(salesOrderCommand.getId());
        orderBaseInfoSubViewCommand.setOrderCode(salesOrderCommand.getCode());

        orderBaseInfoSubViewCommand.setDisplayTotal(displayTotalBuilder.build(salesOrderCommand));

        OrderViewStatusBuilder useOrderViewStatusBuilder = defaultIfNull(orderViewStatusBuilder, DefaultOrderViewStatusBuilder.INSTANCE);
        OrderViewStatus orderViewStatus = useOrderViewStatusBuilder.build(new OrderViewStatusParam(//
                        salesOrderCommand.getLogisticsStatus(),
                        salesOrderCommand.getFinancialStatus(),
                        salesOrderCommand.getPayment(),
                        isFullRated(salesOrderCommand)));

        orderBaseInfoSubViewCommand.setOrderViewStatus(orderViewStatus);
        return orderBaseInfoSubViewCommand;
    }

    private boolean isFullRated(SalesOrderCommand salesOrderCommand){
        List<OrderLineCommand> orderLines = salesOrderCommand.getOrderLines();
        //评价状态是null 表示没有评价
        //如果 找不到 null的,那么表示全部否评价了 
        return null == find(orderLines, "evaluationStatus", null);//FIXME 这里看底层的实现是否正确
    }

}
