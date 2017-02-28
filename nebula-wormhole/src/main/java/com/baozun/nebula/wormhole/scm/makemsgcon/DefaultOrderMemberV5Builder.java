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

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.baozun.nebula.sdk.command.SalesOrderCommand;
import com.baozun.nebula.wormhole.mq.entity.order.OrderMemberV5;

/**
 * 
 * @author <a href="http://feitianbenyue.iteye.com/">feilong</a>
 * @since 5.3.2.11-Personalise
 */
@Transactional
@Service("orderMemberV5Builder")
public class DefaultOrderMemberV5Builder implements OrderMemberV5Builder{

    private static final Logger LOGGER = LoggerFactory.getLogger(DefaultOrderMemberV5Builder.class);

    /*
     * (non-Javadoc)
     * 
     * @see com.baozun.nebula.wormhole.scm.makemsgcon.OrderMemberV5Builder#buildOrderMemberV5(com.baozun.nebula.sdk.command.SalesOrderCommand)
     */
    @Override
    public OrderMemberV5 buildOrderMemberV5(SalesOrderCommand salesOrderCommand){
        OrderMemberV5 orderMemberV5 = new OrderMemberV5();
        orderMemberV5.setEmail(salesOrderCommand.getEmail());
        orderMemberV5.setMemberId(salesOrderCommand.getMemberId());
        orderMemberV5.setLoginName(salesOrderCommand.getMemberName());
        return orderMemberV5;
    }
}
