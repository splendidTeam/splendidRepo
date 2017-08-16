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

import org.springframework.stereotype.Component;

import com.baozun.nebula.sdk.command.SalesOrderCommand;
import com.baozun.nebula.web.controller.order.viewcommand.PaymentInfoSubViewCommand;
import com.feilong.core.bean.PropertyUtil;

/**
 * 专门用来构造 {@link PaymentInfoSubViewCommand} .
 * 
 * @author <a href="http://feitianbenyue.iteye.com/">feilong</a>
 * @since 5.3.2.18
 */
@Component("paymentInfoSubViewCommandBuilder")
public class DefaultPaymentInfoSubViewCommandBuilder implements PaymentInfoSubViewCommandBuilder{

    /*
     * (non-Javadoc)
     * 
     * @see com.baozun.nebula.web.controller.order.builder.PaymentInfoSubViewCommandBuilder#buildPaymentInfoSubViewCommand(com.baozun.nebula.sdk.command.SalesOrderCommand)
     */
    @Override
    public PaymentInfoSubViewCommand build(SalesOrderCommand salesOrderCommand){
        // 支付信息
        PaymentInfoSubViewCommand paymentInfoSubViewCommand = new PaymentInfoSubViewCommand();
        PropertyUtil.copyProperties(paymentInfoSubViewCommand, salesOrderCommand, "payment");
        if (salesOrderCommand.getFinancialStatus() == 1){
            paymentInfoSubViewCommand.setSubOrdinate(salesOrderCommand.getPayInfo().get(0).getSubOrdinate());
        }
        return paymentInfoSubViewCommand;
    }
}
