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
package com.baozun.nebula.event;

import com.baozun.nebula.sdk.command.SalesOrderCommand;

/**
 * 订单取消成功的事件.
 * 
 * @author <a href="http://feitianbenyue.iteye.com/">feilong</a>
 * @since 5.3.2.22
 */
public class OrderCancelSuccessEvent extends AbstractOrderEvent{

    /** The Constant serialVersionUID. */
    private static final long serialVersionUID = 5282663208793901202L;

    /** 订单号. */
    private SalesOrderCommand salesOrderCommand;

    /**
     * Instantiates a new order cancel success event.
     *
     * @param source
     *            the source
     * @param salesOrderCommand
     *            the sales order command
     */
    public OrderCancelSuccessEvent(Object source, SalesOrderCommand salesOrderCommand){
        super(source);
        this.salesOrderCommand = salesOrderCommand;
    }

    //---------------------------------------------------------------------

    /**
     * 获得 订单号.
     *
     * @return the salesOrderCommand
     */
    public SalesOrderCommand getSalesOrderCommand(){
        return salesOrderCommand;
    }

    /**
     * 设置 订单号.
     *
     * @param salesOrderCommand
     *            the salesOrderCommand to set
     */
    public void setSalesOrderCommand(SalesOrderCommand salesOrderCommand){
        this.salesOrderCommand = salesOrderCommand;
    }

}
