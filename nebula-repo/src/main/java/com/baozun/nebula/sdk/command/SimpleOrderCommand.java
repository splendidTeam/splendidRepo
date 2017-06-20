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
    private int  isRate;
    
    /** 实付运费. */
    private BigDecimal          actualFreight;
    
    /** 折扣 */
    private BigDecimal discount;
    
    /** 订单类型 1-普通订单(默认), 2-预售订单,3-miadidas订单,4-hypelaunch订单. 
     * @since 5.3.2.18
     * */
    private Integer            orderType;

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

    public BigDecimal getActualFreight() {
        return actualFreight;
    }

    public void setActualFreight(BigDecimal actualFreight) {
        this.actualFreight = actualFreight;
    }
    
    /**
     * 获取 订单类型
     * 
     * @return the 订单类型 1-普通订单(默认), 2-预售订单,3-miadidas订单,4-hypelaunch订单.
     * @since 5.3.2.18
     */
    public Integer getOrderType(){
        return orderType;
    }
    
    /**
     * 设置 订单类型1-普通订单(默认), 2-预售订单,3-miadidas订单,4-hypelaunch订单.
     * s
     * @param orderType
     *          the new 订单类型1-普通订单(默认), 2-预售订单,3-miadidas订单,4-hypelaunch订单.
     * @since 5.3.2.18
     */
    public void setOrderType(Integer orderType){
        this.orderType = orderType;
    }
    
    
}
