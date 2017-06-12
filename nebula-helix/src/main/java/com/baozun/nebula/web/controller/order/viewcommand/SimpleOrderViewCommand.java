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

import java.math.BigDecimal;
import java.util.List;

/**
 * The Class SimpleOrderViewCommand.
 *
 * @author <a href="http://feitianbenyue.iteye.com/">feilong</a>
 * @version 5.3.1 2016年5月6日 下午5:48:06
 * @see com.baozun.nebula.model.salesorder.SalesOrder
 * @see OrderViewCommand
 * @since 5.3.1
 */
public class SimpleOrderViewCommand extends AbstractOrderViewCommand{

    /** The Constant serialVersionUID. */
    private static final long serialVersionUID = -290693655189583423L;

    /** 支付方式. */
    private Integer payment;

    /** 每个订单行. */
    private List<SimpleOrderLineSubViewCommand> simpleOrderLineSubViewCommandList;

    /**
     * 是否评论 0 未评论 1位评论
     */
    private int isRate;

    /** 拆单号 **/
    private String subOrdinate;

    /** 折扣 */
    private BigDecimal discount;

    //----------------------------------------------------

    /**
     * 获得 支付方式.
     *
     * @return the payment
     */
    public Integer getPayment(){
        return payment;
    }

    /**
     * 设置 支付方式.
     *
     * @param payment
     *            the payment to set
     */
    public void setPayment(Integer payment){
        this.payment = payment;
    }

    /**
     * 获得 每个订单行.
     *
     * @return the simpleOrderLineSubViewCommandList
     */
    public List<SimpleOrderLineSubViewCommand> getSimpleOrderLineSubViewCommandList(){
        return simpleOrderLineSubViewCommandList;
    }

    /**
     * 设置 每个订单行.
     *
     * @param simpleOrderLineSubViewCommandList
     *            the simpleOrderLineSubViewCommandList to set
     */
    public void setSimpleOrderLineSubViewCommandList(List<SimpleOrderLineSubViewCommand> simpleOrderLineSubViewCommandList){
        this.simpleOrderLineSubViewCommandList = simpleOrderLineSubViewCommandList;
    }

    public int getIsRate(){
        return isRate;
    }

    public void setIsRate(int isRate){
        this.isRate = isRate;
    }

    public String getSubOrdinate(){
        return subOrdinate;
    }

    public void setSubOrdinate(String subOrdinate){
        this.subOrdinate = subOrdinate;
    }

    public BigDecimal getDiscount(){
        return discount;
    }

    public void setDiscount(BigDecimal discount){
        this.discount = discount;
    }
}
