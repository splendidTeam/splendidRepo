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
package com.baozun.nebula.sdk.manager.order.event;

import com.baozun.nebula.event.AbstractOrderEvent;
import com.baozun.nebula.model.salesorder.SalesOrder;

/**
 * 订单创建成功的事件.
 * 
 * @author <a href="http://feitianbenyue.iteye.com/">feilong</a>
 * @since 5.3.2.22
 */
public class OrderCreateSuccessEvent extends AbstractOrderEvent{

    /** The Constant serialVersionUID. */
    private static final long serialVersionUID = 501740044662191911L;

    /** The sales order. */
    private SalesOrder salesOrder;

    /**
     * Instantiates a new order create success event.
     *
     * @param source
     *            the source
     * @param salesOrder
     *            the sales order
     */
    public OrderCreateSuccessEvent(Object source, SalesOrder salesOrder){
        super(source);
        this.salesOrder = salesOrder;
    }

    //---------------------------------------------------------------------

    /**
     * Gets the sales order.
     *
     * @return the salesOrder
     */
    public SalesOrder getSalesOrder(){
        return salesOrder;
    }

    /**
     * Sets the sales order.
     *
     * @param salesOrder
     *            the salesOrder to set
     */
    public void setSalesOrder(SalesOrder salesOrder){
        this.salesOrder = salesOrder;
    }

}
