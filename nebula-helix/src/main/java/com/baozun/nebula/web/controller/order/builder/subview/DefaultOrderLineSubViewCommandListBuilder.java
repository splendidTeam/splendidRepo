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

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;

import com.baozun.nebula.manager.salesorder.OrderLineManager;
import com.baozun.nebula.sdk.command.SalesOrderCommand;
import com.baozun.nebula.web.controller.order.viewcommand.OrderLineSubViewCommand;
import com.baozun.nebula.web.controller.order.viewcommand.SimpleOrderLineSubViewCommand;
import com.feilong.core.util.CollectionsUtil;

/**
 * 专门用来构造 {@link OrderLineSubViewCommand} list.
 * 
 * @author <a href="http://feitianbenyue.iteye.com/">feilong</a>
 * @since 5.3.2.18
 */
@Component("orderLineSubViewCommandListBuilder")
public class DefaultOrderLineSubViewCommandListBuilder implements OrderLineSubViewCommandListBuilder{

    /** The order line manager. */
    @Autowired
    @Qualifier("OrderLineManager")
    private OrderLineManager orderLineManager;

    /*
     * (non-Javadoc)
     * 
     * @see com.baozun.nebula.web.controller.order.builder.subview.OrderLineSubViewCommandListBuilder#build(com.baozun.nebula.sdk.command.SalesOrderCommand)
     */
    @Override
    public List<OrderLineSubViewCommand> build(SalesOrderCommand salesOrderCommand){
        // orderline信息
        List<SimpleOrderLineSubViewCommand> simpleOrderLineSubViewCommand = orderLineManager.findByOrderID(salesOrderCommand.getId());
        return CollectionsUtil.collect(simpleOrderLineSubViewCommand, OrderLineSubViewCommand.class);
    }
}
