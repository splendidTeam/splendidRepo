/**
 * 
 */
package com.baozun.nebula.manager.returnapplication;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.baozun.nebula.command.ReturnApplicationCommand;
import com.baozun.nebula.model.returnapplication.ReturnApplication;
import com.baozun.nebula.sdk.command.OrderLineCommand;
import com.baozun.nebula.sdk.command.SalesOrderCommand;
import com.baozun.nebula.sdk.command.SkuProperty;
import com.baozun.nebula.sdk.manager.SdkSkuManager;
import com.baozun.nebula.sdk.manager.order.OrderManager;
import com.baozun.nebula.web.controller.returnapplication.viewcommand.ReturnApplicationViewCommand;
import com.feilong.core.Validator;

/**
 * @author yaohua.wang@baozun.cn
 *
 */
@Service("returnApplicationManager")
public class ReturnApplicationManagerImpl implements ReturnApplicationManager{

    @Autowired
    private OrderManager orderManager;

    @Autowired
    private SdkSkuManager sdkSkuManager;

    @Override
    public List<ReturnApplicationViewCommand> findReturnApplicationViewCommand(List<ReturnApplicationCommand> returnApplications){
        List<ReturnApplicationViewCommand> viewCommands = new ArrayList<ReturnApplicationViewCommand>();

        for (ReturnApplicationCommand returnApp : returnApplications){
            ReturnApplicationViewCommand viewCommand = new ReturnApplicationViewCommand();
            ReturnApplication returnApplication = returnApp.getReturnApplication();
            if (Validator.isNotNullOrEmpty(returnApplication)){
                SalesOrderCommand salesOrder = orderManager.findOrderById(Long.valueOf(returnApp.getReturnApplication().getSoOrderId()), null);
                for (OrderLineCommand line : salesOrder.getOrderLines()){
                    String properties = line.getSaleProperty();
                    List<SkuProperty> propList = sdkSkuManager.getSkuPros(properties);
                    line.setSkuPropertys(propList);
                }
                viewCommand.setOrderLineCommands(salesOrder.getOrderLines());
                viewCommand.setReturnApplicationCommand(returnApp);
                viewCommands.add(viewCommand);
            }
        }
        return viewCommands;
    }

}
