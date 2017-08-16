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
package com.baozun.nebula.sdk.manager.payment;

import static com.baozun.nebula.model.salesorder.SalesOrder.SO_PAYMENT_TYPE_COD;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import com.baozun.nebula.sdk.command.SalesOrderCommand;
import com.baozun.nebula.sdk.command.SalesOrderCreateOptions;

/**
 * 
 * @author <a href="http://feitianbenyue.iteye.com/">feilong</a>
 * @since 5.3.2.20
 */
@Component("orderCreateEmailIsShowPayLinkBuilder")
public class DefaultOrderCreateEmailIsShowPayLinkBuilder implements OrderCreateEmailIsShowPayLinkBuilder{

    private static final Logger LOGGER = LoggerFactory.getLogger(DefaultOrderCreateEmailIsShowPayLinkBuilder.class);

    /*
     * (non-Javadoc)
     * 
     * @see com.baozun.nebula.sdk.manager.payment.OrderCreateEmailIsShowPayLinkBuilder#build(com.baozun.nebula.sdk.command.SalesOrderCommand, com.baozun.nebula.sdk.command.SalesOrderCreateOptions)
     */
    @Override
    public boolean build(SalesOrderCommand salesOrderCommand,SalesOrderCreateOptions salesOrderCreateOptions){
        // 获取付款地址
        boolean isCod = salesOrderCommand.getPayment().toString().equals(SO_PAYMENT_TYPE_COD);

        //return salesOrderCreateOptions.getIsBackCreateOrder() && !isCod;
        return !isCod;
    }
}
