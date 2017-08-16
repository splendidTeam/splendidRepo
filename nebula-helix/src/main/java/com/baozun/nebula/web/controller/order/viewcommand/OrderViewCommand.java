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
package com.baozun.nebula.web.controller.order.viewcommand;

import java.util.List;

import com.baozun.nebula.web.controller.BaseViewCommand;

/**
 * 主要显示订单明细.
 * 
 *
 * @author <a href="http://feitianbenyue.iteye.com/">feilong</a>
 * @version 5.3.1 2016年5月6日 下午5:48:06
 * @see com.baozun.nebula.model.salesorder.SalesOrder
 * @see com.baozun.nebula.web.controller.order.form.OrderForm
 * @see SimpleOrderViewCommand
 * @since 5.3.1
 */
public class OrderViewCommand extends BaseViewCommand{

    /** The Constant serialVersionUID. */
    private static final long serialVersionUID = -290693655189583423L;

    //---------------------------------------------------------------

    /** 基本信息. */
    private OrderBaseInfoSubViewCommand orderBaseInfoSubViewCommand;

    /** 收获地址信息. */
    private ConsigneeSubViewCommand consigneeSubViewCommand;

    /** 支付信息. */
    private PaymentInfoSubViewCommand paymentInfoSubViewCommand;

    /** 优惠券信息. */
    private CouponInfoSubViewCommand couponInfoSubViewCommand;

    /** 物流信息. */
    private LogisticsInfoSubViewCommand logisticsInfoSubViewCommand;

    /** 发票信息. */
    private InvoiceInfoSubViewCommand invoiceInfoSubViewCommand;

    /** 每个订单行. */
    private List<OrderLineSubViewCommand> orderLineSubViewCommandList;

    //---------------------------------------------------------------

    /**
     * 获得 基本信息.
     *
     * @return the orderBaseInfoSubViewCommand
     */
    public OrderBaseInfoSubViewCommand getOrderBaseInfoSubViewCommand(){
        return orderBaseInfoSubViewCommand;
    }

    /**
     * 设置 基本信息.
     *
     * @param orderBaseInfoSubViewCommand
     *            the orderBaseInfoSubViewCommand to set
     */
    public void setOrderBaseInfoSubViewCommand(OrderBaseInfoSubViewCommand orderBaseInfoSubViewCommand){
        this.orderBaseInfoSubViewCommand = orderBaseInfoSubViewCommand;
    }

    /**
     * 获得 收获地址信息.
     *
     * @return the consigneeSubViewCommand
     */
    public ConsigneeSubViewCommand getConsigneeSubViewCommand(){
        return consigneeSubViewCommand;
    }

    /**
     * 设置 收获地址信息.
     *
     * @param consigneeSubViewCommand
     *            the consigneeSubViewCommand to set
     */
    public void setConsigneeSubViewCommand(ConsigneeSubViewCommand consigneeSubViewCommand){
        this.consigneeSubViewCommand = consigneeSubViewCommand;
    }

    /**
     * 获得 支付信息.
     *
     * @return the paymentInfoSubViewCommand
     */
    public PaymentInfoSubViewCommand getPaymentInfoSubViewCommand(){
        return paymentInfoSubViewCommand;
    }

    /**
     * 设置 支付信息.
     *
     * @param paymentInfoSubViewCommand
     *            the paymentInfoSubViewCommand to set
     */
    public void setPaymentInfoSubViewCommand(PaymentInfoSubViewCommand paymentInfoSubViewCommand){
        this.paymentInfoSubViewCommand = paymentInfoSubViewCommand;
    }

    /**
     * 获得 优惠券信息.
     *
     * @return the couponInfoSubViewCommand
     */
    public CouponInfoSubViewCommand getCouponInfoSubViewCommand(){
        return couponInfoSubViewCommand;
    }

    /**
     * 设置 优惠券信息.
     *
     * @param couponInfoSubViewCommand
     *            the couponInfoSubViewCommand to set
     */
    public void setCouponInfoSubViewCommand(CouponInfoSubViewCommand couponInfoSubViewCommand){
        this.couponInfoSubViewCommand = couponInfoSubViewCommand;
    }

    /**
     * 获得 物流信息.
     *
     * @return the logisticsInfoSubViewCommand
     */
    public LogisticsInfoSubViewCommand getLogisticsInfoSubViewCommand(){
        return logisticsInfoSubViewCommand;
    }

    /**
     * 设置 物流信息.
     *
     * @param logisticsInfoSubViewCommand
     *            the logisticsInfoSubViewCommand to set
     */
    public void setLogisticsInfoSubViewCommand(LogisticsInfoSubViewCommand logisticsInfoSubViewCommand){
        this.logisticsInfoSubViewCommand = logisticsInfoSubViewCommand;
    }

    /**
     * 获得 发票信息.
     *
     * @return the invoiceInfoSubViewCommand
     */
    public InvoiceInfoSubViewCommand getInvoiceInfoSubViewCommand(){
        return invoiceInfoSubViewCommand;
    }

    /**
     * 设置 发票信息.
     *
     * @param invoiceInfoSubViewCommand
     *            the invoiceInfoSubViewCommand to set
     */
    public void setInvoiceInfoSubViewCommand(InvoiceInfoSubViewCommand invoiceInfoSubViewCommand){
        this.invoiceInfoSubViewCommand = invoiceInfoSubViewCommand;
    }

    /**
     * 获得 每个订单行.
     *
     * @return the orderLineSubViewCommandList
     */
    public List<OrderLineSubViewCommand> getOrderLineSubViewCommandList(){
        return orderLineSubViewCommandList;
    }

    /**
     * 设置 每个订单行.
     *
     * @param orderLineSubViewCommandList
     *            the orderLineSubViewCommandList to set
     */
    public void setOrderLineSubViewCommandList(List<OrderLineSubViewCommand> orderLineSubViewCommandList){
        this.orderLineSubViewCommandList = orderLineSubViewCommandList;
    }

}
