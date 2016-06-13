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
package com.baozun.nebula.web.controller.payment.resolver;

import java.util.Objects;

import com.baozun.nebula.model.salesorder.SalesOrder;
import com.baozun.nebula.sdk.command.SalesOrderCommand;


public abstract class BasePaymentResolver {
	
    protected static final String PAYMENT_SUCCESS = "PAYMENT_SUCCESS";
    
    /**
     * @param responseStatus
     * @param salesOrderCommand
     * @return
     */
    protected boolean canUpdatePayInfos(String responseStatus,SalesOrderCommand salesOrderCommand){
        if (null == salesOrderCommand){
            return false;
        }

        Integer logisticsStatus = salesOrderCommand.getLogisticsStatus();
        Integer financialStatus = salesOrderCommand.getFinancialStatus();
        return PAYMENT_SUCCESS.equals(responseStatus)
                        && (Objects.equals(SalesOrder.SALES_ORDER_STATUS_NEW, logisticsStatus)
                                        || Objects.equals(SalesOrder.SALES_ORDER_STATUS_TOOMS, logisticsStatus))
                        && Objects.equals(
                                        SalesOrder.SALES_ORDER_FISTATUS_NO_PAYMENT,
                                        financialStatus);
    }
}
