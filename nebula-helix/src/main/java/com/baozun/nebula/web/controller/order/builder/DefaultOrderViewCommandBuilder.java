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

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.baozun.nebula.sdk.command.SalesOrderCommand;
import com.baozun.nebula.web.controller.order.builder.subview.ConsigneeSubViewCommandBuilder;
import com.baozun.nebula.web.controller.order.builder.subview.CouponInfoSubViewCommandBuilder;
import com.baozun.nebula.web.controller.order.builder.subview.InvoiceInfoSubViewCommandBuilder;
import com.baozun.nebula.web.controller.order.builder.subview.LogisticsInfoSubViewCommandBuilder;
import com.baozun.nebula.web.controller.order.builder.subview.OrderBaseInfoSubViewCommandBuilder;
import com.baozun.nebula.web.controller.order.builder.subview.OrderLineSubViewCommandListBuilder;
import com.baozun.nebula.web.controller.order.builder.subview.PaymentInfoSubViewCommandBuilder;
import com.baozun.nebula.web.controller.order.viewcommand.OrderViewCommand;

/**
 * 
 * @author <a href="http://feitianbenyue.iteye.com/">feilong</a>
 * @since 5.3.2.13
 */
@Component("orderViewCommandBuilder")
public class DefaultOrderViewCommandBuilder implements OrderViewCommandBuilder{

    @Autowired
    private OrderBaseInfoSubViewCommandBuilder orderBaseInfoSubViewCommandBuilder;

    @Autowired
    private ConsigneeSubViewCommandBuilder consigneeSubViewCommandBuilder;

    @Autowired
    private PaymentInfoSubViewCommandBuilder paymentInfoSubViewCommandBuilder;

    @Autowired
    private OrderLineSubViewCommandListBuilder orderLineSubViewCommandListBuilder;

    @Autowired
    private LogisticsInfoSubViewCommandBuilder logisticsInfoSubViewCommandBuilder;

    @Autowired
    private InvoiceInfoSubViewCommandBuilder invoiceInfoSubViewCommandBuilder;

    @Autowired
    private CouponInfoSubViewCommandBuilder couponInfoSubViewCommandBuilder;

    //---------------------------------------------------------------

    /*
     * (non-Javadoc)
     * 
     * @see com.baozun.nebula.web.controller.order.builder.OrderViewCommandBuilder#build(com.baozun.nebula.sdk.command.SalesOrderCommand)
     */
    @Override
    public OrderViewCommand build(SalesOrderCommand salesOrderCommand){
        OrderViewCommand orderViewCommand = new OrderViewCommand();
        orderViewCommand.setOrderBaseInfoSubViewCommand(orderBaseInfoSubViewCommandBuilder.build(salesOrderCommand));
        orderViewCommand.setConsigneeSubViewCommand(consigneeSubViewCommandBuilder.build(salesOrderCommand));
        orderViewCommand.setCouponInfoSubViewCommand(couponInfoSubViewCommandBuilder.build(salesOrderCommand));
        orderViewCommand.setInvoiceInfoSubViewCommand(invoiceInfoSubViewCommandBuilder.build(salesOrderCommand));
        orderViewCommand.setLogisticsInfoSubViewCommand(logisticsInfoSubViewCommandBuilder.build(salesOrderCommand));
        orderViewCommand.setOrderLineSubViewCommandList(orderLineSubViewCommandListBuilder.build(salesOrderCommand));
        orderViewCommand.setPaymentInfoSubViewCommand(paymentInfoSubViewCommandBuilder.build(salesOrderCommand));
        return orderViewCommand;
    }

}
