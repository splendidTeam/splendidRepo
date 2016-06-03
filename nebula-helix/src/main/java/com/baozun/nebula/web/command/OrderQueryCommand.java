package com.baozun.nebula.web.command;

import java.util.Date;
import java.util.List;

public class OrderQueryCommand {
    
    /** 商品名称. */
    private String            itemName;

    /** 商品code. */
    private String            itemCode;
    
    /** 订单code. */
    private String            orderCode;
    
    /**
     * 查询订单起始时间
     */
    private Date startDate;
    
    /**
     * 查询订单结束时间
     */
    private Date endDate;
    
    /**
     * 查询订单状态码列表
     */
    private List<Integer> logisticsStatusList;
    
    /**
     * 查询订单财务状态码列表
     */
    private List<Integer> financestatusList;

    public String getItemName() {
        return itemName;
    }

    public void setItemName(String itemName) {
        this.itemName = itemName;
    }

    public String getItemCode() {
        return itemCode;
    }

    public void setItemCode(String itemCode) {
        this.itemCode = itemCode;
    }

    public String getOrderCode() {
        return orderCode;
    }

    public void setOrderCode(String orderCode) {
        this.orderCode = orderCode;
    }

    public Date getStartDate() {
        return startDate;
    }

    public void setStartDate(Date startDate) {
        this.startDate = startDate;
    }

    public Date getEndDate() {
        return endDate;
    }

    public void setEndDate(Date endDate) {
        this.endDate = endDate;
    }

    public List<Integer> getLogisticsStatusList() {
        return logisticsStatusList;
    }

    public void setLogisticsStatusList(List<Integer> logisticsStatusList) {
        this.logisticsStatusList = logisticsStatusList;
    }

    public List<Integer> getFinancestatusList() {
        return financestatusList;
    }

    public void setFinancestatusList(List<Integer> financestatusList) {
        this.financestatusList = financestatusList;
    }

    @Override
    public String toString() {
        return "OrderQueryCommand [itemName=" + itemName + ", itemCode=" + itemCode + ", orderCode="
                + orderCode + ", startDate=" + startDate + ", endDate=" + endDate + ", logisticsStatusList="
                + logisticsStatusList + ", financestatusList=" + financestatusList + "]";
    }
    
    
}
