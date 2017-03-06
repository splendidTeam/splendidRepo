package com.baozun.nebula.web.controller.order.converter;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;

import com.baozun.nebula.manager.salesorder.OrderLineManager;
import com.baozun.nebula.sdk.command.PayInfoCommand;
import com.baozun.nebula.sdk.command.SimpleOrderCommand;
import com.baozun.nebula.sdk.manager.SdkPaymentManager;
import com.baozun.nebula.web.controller.BaseConverter;
import com.baozun.nebula.web.controller.UnsupportDataTypeException;
import com.baozun.nebula.web.controller.order.viewcommand.SimpleOrderLineSubViewCommand;
import com.baozun.nebula.web.controller.order.viewcommand.SimpleOrderViewCommand;
import com.feilong.core.bean.PropertyUtil;

/**
 * 
 * 说明：SimpleOrderCommand convert to SimpleOrderViewCommand
 * 
 * @author 张乃骐
 * @version 1.0
 * @date 2016年5月10日
 */
public class SimpleOrderViewCommandConverter extends BaseConverter<SimpleOrderViewCommand>{

    private static final long serialVersionUID = 3683409497583268509L;

    @Autowired
    @Qualifier("OrderLineManager")
    private OrderLineManager orderLineManager;

    @Autowired
    private SdkPaymentManager sdkPaymentManager;

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
     * @since 5.3.2.11-Personalise
     */
    //TODO 性能比较差的
    protected SimpleOrderViewCommand toSimpleOrderViewCommand(SimpleOrderCommand simpleOrderCommand){
        SimpleOrderViewCommand simpleOrderViewCommand = new SimpleOrderViewCommand();
        PropertyUtil.copyProperties(simpleOrderViewCommand, simpleOrderCommand, "orderId", "orderCode", "createTime", "logisticsStatus", "financialStatus", "payment", "total", "discount", "actualFreight", "isRate");

        List<SimpleOrderLineSubViewCommand> simpleOrderLineSubViewCommandList = orderLineManager.findByOrderID(simpleOrderCommand.getOrderId());
        simpleOrderViewCommand.setSimpleOrderLineSubViewCommandList(simpleOrderLineSubViewCommandList);

        if (simpleOrderCommand.getFinancialStatus() == 1){
            List<PayInfoCommand> findPayInfoCommandByOrderId = sdkPaymentManager.findPayInfoCommandByOrderId(simpleOrderCommand.getOrderId());
            simpleOrderViewCommand.setSubOrdinate(findPayInfoCommandByOrderId.get(0).getSubOrdinate());
        }

        return simpleOrderViewCommand;
    }
}
