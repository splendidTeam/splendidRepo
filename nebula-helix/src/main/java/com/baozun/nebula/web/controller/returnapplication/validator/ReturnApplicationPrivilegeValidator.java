package com.baozun.nebula.web.controller.returnapplication.validator;

import java.util.Comparator;
import java.util.List;
import java.util.Set;
import java.util.TreeSet;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;

import com.baozun.nebula.sdk.command.OrderLineCommand;
import com.baozun.nebula.sdk.manager.order.SdkOrderLineManager;


public class ReturnApplicationPrivilegeValidator implements Validator{

    @Autowired
    private SdkOrderLineManager orderLineManager;
    
    @Override
    public boolean supports(Class<?> clazz){
        return false;
    }

    @Override
    public void validate(Object target,Errors errors){
        List<Long> orderLineIdsList = (List<Long>) target;  
        List<OrderLineCommand> orderLineCommands = orderLineManager.findOrderLinesByLineIds(orderLineIdsList);
        if(com.feilong.core.Validator.isNotNullOrEmpty(orderLineCommands)){
            Set<OrderLineCommand> orderIdSet = new TreeSet<OrderLineCommand>(new Comparator<OrderLineCommand>(){
                @Override
                public int compare(OrderLineCommand o1,OrderLineCommand o2){
                    return o1.getOrderId().compareTo(o2.getOrderId());
            }});
            orderIdSet.addAll(orderLineCommands);
            if(com.feilong.core.Validator.isNotNullOrEmpty(orderIdSet)&&1<orderIdSet.size()){
                errors.rejectValue("returnApplication", "orderLine.isNotOneOrder");
            }
        }
    }
    
}
