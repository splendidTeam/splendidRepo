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
import java.util.Date;
import java.util.List;

import com.baozun.nebula.web.controller.BaseViewCommand;

/**
 * The Class SimpleOrderViewCommand.
 *
 * @author feilong
 * @version 5.3.1 2016年5月6日 下午5:48:06
 * @see com.baozun.nebula.model.salesorder.SalesOrder
 * @since 5.3.1
 */
public class SimpleOrderViewCommand extends BaseViewCommand{

    /** The Constant serialVersionUID. */
    private static final long                   serialVersionUID = -290693655189583423L;

    /** 订单id. */
    private Long                              orderId;

    /** 订单code. */
    private String                              orderCode;

    /** 创建时间. */
    private Date                                createTime;

    /** 物流状态. */
    private Integer                             logisticsStatus;

    /** 财务状态. */
    private Integer                             financialStatus;

    /** 支付方式. */
    private Integer                             payment;

    /** 每个订单行. */
    private List<SimpleOrderLineSubViewCommand> simpleOrderLineSubViewCommandList;

    /** 总价. */
    private BigDecimal                          total;
    /**
     * 是否评论 0 未评论 1位评论
     */
    private int                                  isRate;
    
    /** 拆单号 **/
    private String                          subOrdinate;

    /**
     * 获得 订单code.
     *
     * @return the orderCode
     */
    public String getOrderCode(){
        return orderCode;
    }

    /**
     * 设置 订单code.
     *
     * @param orderCode
     *            the orderCode to set
     */
    public void setOrderCode(String orderCode){
        this.orderCode = orderCode;
    }

    /**
     * 获得 创建时间.
     *
     * @return the createTime
     */
    public Date getCreateTime(){
        return createTime;
    }

    /**
     * 设置 创建时间.
     *
     * @param createTime
     *            the createTime to set
     */
    public void setCreateTime(Date createTime){
        this.createTime = createTime;
    }

    /**
     * 获得 物流状态.
     *
     * @return the logisticsStatus
     */
    public Integer getLogisticsStatus(){
        return logisticsStatus;
    }

    /**
     * 设置 物流状态.
     *
     * @param logisticsStatus
     *            the logisticsStatus to set
     */
    public void setLogisticsStatus(Integer logisticsStatus){
        this.logisticsStatus = logisticsStatus;
    }

    /**
     * 获得 财务状态.
     *
     * @return the financialStatus
     */
    public Integer getFinancialStatus(){
        return financialStatus;
    }

    /**
     * 设置 财务状态.
     *
     * @param financialStatus
     *            the financialStatus to set
     */
    public void setFinancialStatus(Integer financialStatus){
        this.financialStatus = financialStatus;
    }

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

    /**
     * 获得总价.
     *
     * @return the total
     */
    public BigDecimal getTotal(){
        return total;
    }

    /**
     * 设置总价.
     *
     * @param total
     *            the total to set
     */
    public void setTotal(BigDecimal total){
        this.total = total;
    }

    /**
     * 获得 订单id.
     *
     * @return the orderId
     */
    public Long getOrderId(){
        return orderId;
    }

    /**
     * 设置 订单id.
     *
     * @param orderId
     *            the orderId to set
     */
    public void setOrderId(Long orderId){
        this.orderId = orderId;
    }

    public int getIsRate() {
        return isRate;
    }

    public void setIsRate(int isRate) {
        this.isRate = isRate;
    }

    public String getSubOrdinate() {
        return subOrdinate;
    }

    public void setSubOrdinate(String subOrdinate) {
        this.subOrdinate = subOrdinate;
    }
    
    
}
