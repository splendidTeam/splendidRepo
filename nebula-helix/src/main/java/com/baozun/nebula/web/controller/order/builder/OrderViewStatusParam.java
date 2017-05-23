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

import java.io.Serializable;

/**
 * 
 * @author <a href="http://feitianbenyue.iteye.com/">feilong</a>
 * @since 5.3.2.16
 */
public class OrderViewStatusParam implements Serializable{

    /**  */
    private static final long serialVersionUID = -4241460654392834256L;

    /** 物流状态. */
    private Integer logisticsStatus;

    /** 财务状态. */
    private Integer financialStatus;

    /** 支付方式. */
    private Integer payment;

    /** 是否全部评价. */
    private boolean isFullRated;

    /**
     * 
     */
    public OrderViewStatusParam(){
        super();
    }

    /**
     * @param logisticsStatus
     *            物流状态
     * @param financialStatus
     *            财务状态
     * @param payment
     *            支付方式
     * @param isFullRated
     *            是否全部评价
     */
    public OrderViewStatusParam(Integer logisticsStatus, Integer financialStatus, Integer payment, boolean isFullRated){
        super();
        this.logisticsStatus = logisticsStatus;
        this.financialStatus = financialStatus;
        this.payment = payment;
        this.isFullRated = isFullRated;
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
     * 获得 是否全部评价.
     *
     * @return the isFullRated
     */
    public boolean getIsFullRated(){
        return isFullRated;
    }

    /**
     * 设置 是否全部评价.
     *
     * @param isFullRated
     *            the isFullRated to set
     */
    public void setIsFullRated(boolean isFullRated){
        this.isFullRated = isFullRated;
    }

}
