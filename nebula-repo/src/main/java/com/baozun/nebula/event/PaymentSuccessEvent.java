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
 * 支付成功的事件.
 * 
 * @author <a href="http://feitianbenyue.iteye.com/">feilong</a>
 * @since 5.3.2.22
 */
public class PaymentSuccessEvent extends AbstractPaymentEvent{

    /** The Constant serialVersionUID. */
    private static final long serialVersionUID = -921743007085078417L;

    /** 订单流水号. */
    private String subOrdinate;

    /** 解过密的 salesOrderCommand. */
    private SalesOrderCommand salesOrderCommand;

    //---------------------------------------------------------------------

    /**
     * Create a new ApplicationEvent.
     *
     * @param source
     *            the component that published the event (never {@code null})
     * @param subOrdinate
     *            the sub ordinate
     * @param salesOrderCommand
     *            the sales order command
     */
    public PaymentSuccessEvent(Object source, String subOrdinate, SalesOrderCommand salesOrderCommand){
        super(source);
        this.subOrdinate = subOrdinate;
        this.salesOrderCommand = salesOrderCommand;
    }

    //---------------------------------------------------------------------

    /**
     * 获得 订单流水号.
     *
     * @return the 订单流水号
     */
    public String getSubOrdinate(){
        return subOrdinate;
    }

    /**
     * 设置 订单流水号.
     *
     * @param subOrdinate
     *            the new 订单流水号
     */
    public void setSubOrdinate(String subOrdinate){
        this.subOrdinate = subOrdinate;
    }

    /**
     * 获得 解过密的 salesOrderCommand.
     *
     * @return the salesOrderCommand
     */
    public SalesOrderCommand getSalesOrderCommand(){
        return salesOrderCommand;
    }

    /**
     * 设置 解过密的 salesOrderCommand.
     *
     * @param salesOrderCommand
     *            the salesOrderCommand to set
     */
    public void setSalesOrderCommand(SalesOrderCommand salesOrderCommand){
        this.salesOrderCommand = salesOrderCommand;
    }
}
