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

import java.util.ArrayList;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.baozun.nebula.sdk.command.PayInfoCommand;
import com.baozun.nebula.sdk.command.SalesOrderCommand;
import com.baozun.nebula.wormhole.mq.entity.order.OrderPaymentV5;

/**
 * 
 * @author <a href="http://feitianbenyue.iteye.com/">feilong</a>
 * @since 5.3.2.13
 */
@Transactional
@Service("orderPaymentV5ListBuilder")
public class DefaultOrderPaymentV5ListBuilder implements OrderPaymentV5ListBuilder{

    private static final Logger LOGGER = LoggerFactory.getLogger(DefaultOrderPaymentV5ListBuilder.class);

    /*
     * (non-Javadoc)
     * 
     * @see com.baozun.nebula.wormhole.scm.makemsgcon.OrderPaymentV5ListBuilder#buildOrderPaymentV5List(com.baozun.nebula.sdk.command.SalesOrderCommand)
     */
    @Override
    public List<OrderPaymentV5> buildOrderPaymentV5List(SalesOrderCommand salesOrderCommand){
        List<OrderPaymentV5> soPayments = new ArrayList<OrderPaymentV5>();
        if (salesOrderCommand.getPayInfo() != null){
            for (PayInfoCommand payInfoCommand : salesOrderCommand.getPayInfo()){
                OrderPaymentV5 orderPaymentV5 = new OrderPaymentV5();
                //目前是取的订单表里面的payment
                orderPaymentV5.setPaymentType(payInfoCommand.getPayType().toString());
                orderPaymentV5.setPayActual(payInfoCommand.getPayMoney());
                //附加价值说明：对于某些支付方式，可以使用此信息记录额外内容，如外部积分可以使用此信息来记录积分分值，预付卡可以用此来记录实际价值
                orderPaymentV5.setAdditionalWorth(null);
                soPayments.add(orderPaymentV5);
            }
        }
        return soPayments;
    }
}
