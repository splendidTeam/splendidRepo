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

import com.baozun.nebula.web.controller.BaseViewCommand;

/**
 * 订单的基本信息.
 *
 * @author <a href="http://feitianbenyue.iteye.com/">feilong</a>
 * @version 5.3.1 2016年5月12日 下午3:16:25
 * @since 5.3.1
 */
public class OrderBaseInfoSubViewCommand extends BaseViewCommand {

    /** The Constant serialVersionUID. */
    private static final long serialVersionUID = -6400008677521495969L;

    /** 订单id. */
    private Long orderId;

    /** 订单code. */
    private String orderCode;

    /** 创建时间. */
    private Date createTime;

    /** 物流状态. */
    private Integer logisticsStatus;

    /** 财务状态. */
    private Integer financialStatus;

    /** //XXX feilong 总价. */
    private BigDecimal total;

    /** 应付运费 */
    private BigDecimal payableFreight;

    /** 实付运费 */
    private BigDecimal actualFreight;

    /**
     * 整单折扣 整单折扣-sum（行折扣）= 由于整单促销/商城积分形成的未分摊到行上的折扣总额
     */
    private BigDecimal discount;

    public BigDecimal getPayableFreight() {
        return payableFreight;
    }

    public void setPayableFreight(BigDecimal payableFreight) {
        this.payableFreight = payableFreight;
    }

    public BigDecimal getActualFreight() {
        return actualFreight;
    }

    public void setActualFreight(BigDecimal actualFreight) {
        this.actualFreight = actualFreight;
    }

    public BigDecimal getDiscount() {
        return discount;
    }

    public void setDiscount(BigDecimal discount) {
        this.discount = discount;
    }

    /**
     * 获得 订单id.
     *
     * @return the orderId
     */
    public Long getOrderId() {
        return orderId;
    }

    /**
     * 设置 订单id.
     *
     * @param orderId the orderId to set
     */
    public void setOrderId(Long orderId) {
        this.orderId = orderId;
    }

    /**
     * 获得 订单code.
     *
     * @return the orderCode
     */
    public String getOrderCode() {
        return orderCode;
    }

    /**
     * 设置 订单code.
     *
     * @param orderCode the orderCode to set
     */
    public void setOrderCode(String orderCode) {
        this.orderCode = orderCode;
    }

    /**
     * 获得 创建时间.
     *
     * @return the createTime
     */
    public Date getCreateTime() {
        return createTime;
    }

    /**
     * 设置 创建时间.
     *
     * @param createTime the createTime to set
     */
    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }

    /**
     * 获得 物流状态.
     *
     * @return the logisticsStatus
     */
    public Integer getLogisticsStatus() {
        return logisticsStatus;
    }

    /**
     * 设置 物流状态.
     *
     * @param logisticsStatus the logisticsStatus to set
     */
    public void setLogisticsStatus(Integer logisticsStatus) {
        this.logisticsStatus = logisticsStatus;
    }

    /**
     * 获得 财务状态.
     *
     * @return the financialStatus
     */
    public Integer getFinancialStatus() {
        return financialStatus;
    }

    /**
     * 设置 财务状态.
     *
     * @param financialStatus the financialStatus to set
     */
    public void setFinancialStatus(Integer financialStatus) {
        this.financialStatus = financialStatus;
    }

    /**
     * 获得.
     *
     * @return the total
     */
    public BigDecimal getTotal() {
        return total;
    }

    /**
     * 设置.
     *
     * @param total the total to set
     */
    public void setTotal(BigDecimal total) {
        this.total = total;
    }

}
