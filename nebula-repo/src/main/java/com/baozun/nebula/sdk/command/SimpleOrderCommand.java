package com.baozun.nebula.sdk.command;

import java.math.BigDecimal;
import java.util.Date;

public class SimpleOrderCommand {
   
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
    
    /** 总价 */
    private BigDecimal total;
    
    /**
     * 是否评论 0 未评论 1位评论
     */
    private int                                  isRate;
    
    /** 折扣 */
    private BigDecimal discount;

    public Long getOrderId() {
        return orderId;
    }

    public void setOrderId(Long orderId) {
        this.orderId = orderId;
    }

    public String getOrderCode() {
        return orderCode;
    }

    public void setOrderCode(String orderCode) {
        this.orderCode = orderCode;
    }

    public Date getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }

    public Integer getPayment() {
        return payment;
    }

    public void setPayment(Integer payment) {
        this.payment = payment;
    }

    public BigDecimal getTotal() {
        return total;
    }

    public void setTotal(BigDecimal total) {
        this.total = total;
    }

    public BigDecimal getDiscount() {
        return discount;
    }

    public void setDiscount(BigDecimal discount) {
        this.discount = discount;
    }


    public int getIsRate() {
        return isRate;
    }

    public void setIsRate(int isRate) {
        this.isRate = isRate;
    }

    public Integer getLogisticsStatus() {
        return logisticsStatus;
    }

    public void setLogisticsStatus(Integer logisticsStatus) {
        this.logisticsStatus = logisticsStatus;
    }

    public Integer getFinancialStatus() {
        return financialStatus;
    }

    public void setFinancialStatus(Integer financialStatus) {
        this.financialStatus = financialStatus;
    }
    
    
}
