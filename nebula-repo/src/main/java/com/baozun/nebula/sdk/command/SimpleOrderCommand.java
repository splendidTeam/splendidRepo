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
    private Integer                             logisticsstatus;

    /** 财务状态. */
    private Integer                             financialstatus;

    /** 支付方式. */
    private Integer                             payment;
    
    /** 总价 */
    private BigDecimal total;

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

    public Integer getLogisticsStatus() {
        return logisticsstatus;
    }

    public void setLogisticsStatus(Integer logisticsStatus) {
        this.logisticsstatus = logisticsStatus;
    }

    public Integer getFinancialStatus() {
        return financialstatus;
    }

    public void setFinancialStatus(Integer financialStatus) {
        this.financialstatus = financialStatus;
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
    
    
}
