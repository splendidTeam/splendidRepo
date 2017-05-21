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
package com.baozun.nebula.web.controller.order.builder;

import static com.baozun.nebula.model.salesorder.SalesOrder.SALES_ORDER_FISTATUS_NO_PAYMENT;
import static com.baozun.nebula.model.salesorder.SalesOrder.SALES_ORDER_STATUS_CANCELED;
import static com.baozun.nebula.model.salesorder.SalesOrder.SALES_ORDER_STATUS_DELIVERIED;
import static com.baozun.nebula.model.salesorder.SalesOrder.SALES_ORDER_STATUS_FINISHED;
import static com.baozun.nebula.model.salesorder.SalesOrder.SALES_ORDER_STATUS_SYS_CANCELED;
import static com.baozun.nebula.model.salesorder.SalesOrder.SO_PAYMENT_TYPE_COD;

import org.apache.commons.lang3.Validate;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.baozun.nebula.web.controller.order.viewcommand.OrderViewStatus;

/**
 * 默认的订单显示状态的构造器.
 * 
 * <h3>流程:</h3>
 * <blockquote>
 * <ol>
 * <li>如果订单物流状态是 个人取消(9)或者是 系统取消(10), 那么显示状态是 订单取消 {@link OrderViewStatus#CANCEL}</li>
 * <li>如果物流状态是 在途(6) ,显示为 待收货 {@link OrderViewStatus#TO_BE_RECEIVED}</li>
 * <li>如果物流状态是 交易完成(15) 如果全部评价了,表示完成 {@link OrderViewStatus#COMPLETED}, 如果没有全部评价表示 待评价 {@link OrderViewStatus#TO_BE_RATE}</li>
 * <li>如果财务状态未付款, 且支付方式不是COD, 表示 待支付 {@link OrderViewStatus#TO_BE_PAY}</li>
 * <li>其余表示 待发货 {@link OrderViewStatus#TO_BE_DELIVERED}</li>
 * </ol>
 * </blockquote>
 * 
 * @author <a href="http://feitianbenyue.iteye.com/">feilong</a>
 * @since 5.3.2.16
 */
public class DefaultOrderViewStatusBuilder implements OrderViewStatusBuilder{

    /**  */
    private static final Logger LOGGER = LoggerFactory.getLogger(DefaultOrderViewStatusBuilder.class);

    /** Static instance. */
    // the static instance works for all types
    public static final OrderViewStatusBuilder INSTANCE = new DefaultOrderViewStatusBuilder();

    /*
     * (non-Javadoc)
     * 
     * @see com.baozun.nebula.web.controller.order.builder.OrderViewStatusBuilder#build(com.baozun.nebula.web.controller.order.builder.OrderViewStatusParam)
     */
    public OrderViewStatus build(OrderViewStatusParam orderViewStatusParam){
        Validate.notNull(orderViewStatusParam, "orderViewStatusParam can't be null!");

        Integer logisticsStatus = orderViewStatusParam.getLogisticsStatus();
        Integer financialStatus = orderViewStatusParam.getFinancialStatus();
        Integer payment = orderViewStatusParam.getPayment();
        boolean isFullRated = orderViewStatusParam.getIsFullRated();

        //-------------------------------------------------------------

        //如果是 9 或者是 10 表示是 取消
        if (SALES_ORDER_STATUS_CANCELED == logisticsStatus || SALES_ORDER_STATUS_SYS_CANCELED == logisticsStatus){
            return OrderViewStatus.CANCEL;
        }

        //-------------------------------------------------------------
        //如果是 6 表示是 在途  ,显示为 待收货
        if (SALES_ORDER_STATUS_DELIVERIED == logisticsStatus){
            return OrderViewStatus.TO_BE_RECEIVED;
        }

        //-------------------------------------------------------------
        //如果是15 表示签收
        if (SALES_ORDER_STATUS_FINISHED == logisticsStatus){
            //如果评价了,表示完成, 如果没有评价表示 待评价  必须全部评价
            return isFullRated ? OrderViewStatus.COMPLETED : OrderViewStatus.TO_BE_RATE;
        }

        //-------------------------------------------------------------

        Validate.notNull(payment, "payment can't be null!");
        //未支付 财务状态未付款, 且不是Cod
        if (SALES_ORDER_FISTATUS_NO_PAYMENT == financialStatus && !SO_PAYMENT_TYPE_COD.equals(payment.toString())){
            return OrderViewStatus.TO_BE_PAY;
        }

        //-------------------------------------------------------------

        // 其他状态都归属到 待发货.
        return OrderViewStatus.TO_BE_DELIVERED;
    }

}
