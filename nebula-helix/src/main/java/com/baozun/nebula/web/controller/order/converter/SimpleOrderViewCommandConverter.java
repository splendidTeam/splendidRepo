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
package com.baozun.nebula.web.controller.order.converter;

import static org.apache.commons.lang3.ObjectUtils.defaultIfNull;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;

import com.baozun.nebula.manager.salesorder.OrderLineManager;
import com.baozun.nebula.sdk.command.PayInfoCommand;
import com.baozun.nebula.sdk.command.SimpleOrderCommand;
import com.baozun.nebula.sdk.manager.SdkPaymentManager;
import com.baozun.nebula.web.controller.BaseConverter;
import com.baozun.nebula.web.controller.UnsupportDataTypeException;
import com.baozun.nebula.web.controller.order.builder.DefaultOrderViewStatusBuilder;
import com.baozun.nebula.web.controller.order.builder.OrderViewStatusBuilder;
import com.baozun.nebula.web.controller.order.builder.OrderViewStatusParam;
import com.baozun.nebula.web.controller.order.viewcommand.SimpleOrderLineSubViewCommand;
import com.baozun.nebula.web.controller.order.viewcommand.SimpleOrderViewCommand;
import com.feilong.core.bean.PropertyUtil;

/**
 * 说明：SimpleOrderCommand convert to SimpleOrderViewCommand.
 *
 * @author 张乃骐
 * @author <a href="http://feitianbenyue.iteye.com/">feilong</a>
 */
public class SimpleOrderViewCommandConverter extends BaseConverter<SimpleOrderViewCommand>{

    /**  */
    private static final long serialVersionUID = 3683409497583268509L;

    /**  */
    @Autowired
    @Qualifier("OrderLineManager")
    private OrderLineManager orderLineManager;

    /**  */
    @Autowired
    private SdkPaymentManager sdkPaymentManager;

    /** 用来构造 OrderViewStatus */
    private OrderViewStatusBuilder orderViewStatusBuilder;

    /*
     * (non-Javadoc)
     * 
     * @see com.baozun.nebula.web.controller.BaseConverter#convert(java.lang.Object)
     */
    @Override
    public SimpleOrderViewCommand convert(Object data){
        if (data == null){
            return null;
        }

        if (data instanceof SimpleOrderCommand){
            SimpleOrderCommand simpleOrderCommand = (SimpleOrderCommand) data;
            return toSimpleOrderViewCommand(simpleOrderCommand);
        }

        throw new UnsupportDataTypeException(data.getClass() + " cannot convert to " + SimpleOrderCommand.class + "yet.");
    }

    /**
     * @param simpleOrderCommand
     * @return
     * @since 5.3.2.13
     */
    //TODO 性能比较差的
    protected SimpleOrderViewCommand toSimpleOrderViewCommand(SimpleOrderCommand simpleOrderCommand){
        SimpleOrderViewCommand simpleOrderViewCommand = new SimpleOrderViewCommand();
        PropertyUtil.copyProperties(
                        simpleOrderViewCommand,
                        simpleOrderCommand, //
                        "orderId",
                        "orderCode",
                        "createTime",
                        "logisticsStatus",
                        "financialStatus",
                        "payment",
                        "total",
                        "discount",
                        "actualFreight",
                        "isRate");

        List<SimpleOrderLineSubViewCommand> simpleOrderLineSubViewCommandList = orderLineManager.findByOrderID(simpleOrderCommand.getOrderId());
        simpleOrderViewCommand.setSimpleOrderLineSubViewCommandList(simpleOrderLineSubViewCommandList);

        if (simpleOrderCommand.getFinancialStatus() == 1){
            List<PayInfoCommand> findPayInfoCommandByOrderId = sdkPaymentManager.findPayInfoCommandByOrderId(simpleOrderCommand.getOrderId());
            simpleOrderViewCommand.setSubOrdinate(findPayInfoCommandByOrderId.get(0).getSubOrdinate());
        }

        //-----------------------------------------------------------------------------------------------------------

        OrderViewStatusBuilder useOrderViewStatusBuilder = defaultIfNull(orderViewStatusBuilder, DefaultOrderViewStatusBuilder.INSTANCE);
        simpleOrderViewCommand.setOrderViewStatus(useOrderViewStatusBuilder.build(new OrderViewStatusParam(//
                        simpleOrderViewCommand.getLogisticsStatus(),
                        simpleOrderViewCommand.getFinancialStatus(),
                        simpleOrderViewCommand.getPayment(),
                        1 == simpleOrderViewCommand.getIsRate())));
        return simpleOrderViewCommand;
    }

    /**
     * @return the orderViewStatusBuilder
     */
    public OrderViewStatusBuilder getOrderViewStatusBuilder(){
        return orderViewStatusBuilder;
    }

}
