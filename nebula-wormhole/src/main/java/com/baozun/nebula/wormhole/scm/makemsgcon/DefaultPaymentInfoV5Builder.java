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

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.alibaba.fastjson.JSON;
import com.baozun.nebula.model.salesorder.SalesOrder;
import com.baozun.nebula.sdk.command.PayInfoCommand;
import com.baozun.nebula.sdk.command.SalesOrderCommand;
import com.baozun.nebula.wormhole.mq.entity.pay.PaymentInfoV5;
import com.feilong.core.Validator;

/**
 * 
 * @author <a href="http://feitianbenyue.iteye.com/">feilong</a>
 * @since 5.3.2.11-Personalise
 */
@Transactional
@Service("paymentInfoV5Builder")
public class DefaultPaymentInfoV5Builder implements PaymentInfoV5Builder{

    private static final Logger LOGGER = LoggerFactory.getLogger(DefaultPaymentInfoV5Builder.class);

    /* (non-Javadoc)
     * @see com.baozun.nebula.wormhole.scm.makemsgcon.PaymentInfoV5Builder#buildPaymentInfoV5(com.baozun.nebula.sdk.command.PayInfoCommand, com.baozun.nebula.sdk.command.SalesOrderCommand)
     */
    @Override
    public PaymentInfoV5 buildPaymentInfoV5(PayInfoCommand payInfoCommand,SalesOrderCommand salesOrderCommand){
        PaymentInfoV5 paymentInfoV5 = new PaymentInfoV5();
        //需要关联订单表
        paymentInfoV5.setBsOrderCode(payInfoCommand.getOrderCode());
        paymentInfoV5.setPaymentType(payInfoCommand.getPayType().toString());
        paymentInfoV5.setPaymentBank(null);
        paymentInfoV5.setPayTotal(payInfoCommand.getPayMoney());
        paymentInfoV5.setPayNo(payInfoCommand.getSubOrdinate());
        //since 5.3.2.10
        paymentInfoV5.setPaymentAccount(payInfoCommand.getThirdPayAccount());
        paymentInfoV5.setPaymentTime(payInfoCommand.getModifyTime());
        if (payInfoCommand.getPayType() == payInfoCommand.getMainPayType() || Objects.equals(SalesOrder.SALES_ORDER_FISTATUS_FULL_PAYMENT, salesOrderCommand.getFinancialStatus())){
            paymentInfoV5.setAllComplete(true);
        }else{
            paymentInfoV5.setAllComplete(false);
        }

        Map<String, String> remarkMap = new HashMap<>();
        if (Validator.isNotNullOrEmpty(payInfoCommand.getPayInfo())){
            remarkMap.put("paydetail", payInfoCommand.getPayInfo());
        }

        if (remarkMap.size() > 0){
            paymentInfoV5.setRemark(JSON.toJSONString(remarkMap));
        }else{
            paymentInfoV5.setRemark(null);
        }
        return paymentInfoV5;
    }
}
