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
 * 公用的订单view command.
 * 
 * <h3>说明:</h3>
 * <blockquote>
 * 订单明细 是使用 OrderViewCommand来渲染的 <br>
 * 
 * 订单行信息 使用的是 List{@code <OrderLineSubViewCommand>} (订单列表使用的是 SimpleOrderLineSubViewCommand) 不是一个对象 <br>
 * 
 * 其实发现两者使用相同的 属性 <br>
 * 可能是当年创建的时候 想考虑差异化部分 <br>
 * 现在如果直接合并改动量会比较大 <br>
 * 
 * 此时 把两者提取成 base 父类来处理<br>
 * </blockquote>
 * 
 * @author <a href="http://feitianbenyue.iteye.com/">feilong</a>
 * @since 5.3.2.16
 */
abstract class AbstractOrderViewCommand extends BaseViewCommand{

    /**  */
    private static final long serialVersionUID = 653828676390854211L;

    /** 订单id. */
    private Long orderId;

    /** 订单code. */
    private String orderCode;

    /** 创建时间. */
    private Date createTime;

    /**
     * 修改时间
     * 
     * @since 5.3.2.20
     */
    private Date modifyTime;

    /** 物流状态. */
    private Integer logisticsStatus;

    /** 财务状态. */
    private Integer financialStatus;

    /** 订单的显示状态. */
    private OrderViewStatus orderViewStatus;

    //-------------------------------------------

    /** 总价. */
    private BigDecimal total;

    /** 实付运费. */
    private BigDecimal actualFreight;

    /**
     * 订单类型 (对应salesOrder表中orderType)
     * 
     * @since 5.3.2.18
     */
    private Integer orderType;

    /**
     * 显示时候用的订单总价(包括total加上运费和包装信息相关金额的总和)
     * 
     * @since 5.3.2.18
     */
    private BigDecimal displayTotal;

    /**
     * 商品总数量
     * 
     * @since 5.3.2.22
     */
    private Integer quantity;

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
     * 获得 修改时间.
     *
     * @return the 修改时间
     * @since 5.3.2.20
     */
    public Date getModifyTime(){
        return modifyTime;
    }

    /**
     * 设置 修改时间.
     *
     * @param modifyTime
     *            the new 修改时间
     * @since 5.3.2.20
     */
    public void setModifyTime(Date modifyTime){
        this.modifyTime = modifyTime;
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
     * 获得 订单的显示状态.
     *
     * @return the orderViewStatus
     */
    public OrderViewStatus getOrderViewStatus(){
        return orderViewStatus;
    }

    /**
     * 设置 订单的显示状态.
     *
     * @param orderViewStatus
     *            the orderViewStatus to set
     */
    public void setOrderViewStatus(OrderViewStatus orderViewStatus){
        this.orderViewStatus = orderViewStatus;
    }

    /**
     * 获得 总价.
     *
     * @return the total
     */
    public BigDecimal getTotal(){
        return total;
    }

    /**
     * 设置 总价.
     *
     * @param total
     *            the total to set
     */
    public void setTotal(BigDecimal total){
        this.total = total;
    }

    /**
     * 获得 实付运费.
     *
     * @return the actualFreight
     */
    public BigDecimal getActualFreight(){
        return actualFreight;
    }

    /**
     * 设置 实付运费.
     *
     * @param actualFreight
     *            the actualFreight to set
     */
    public void setActualFreight(BigDecimal actualFreight){
        this.actualFreight = actualFreight;
    }

    /**
     * 获得 订单类型(对应salesOrder表中orderType).
     * 
     * @return the 订单类型
     * @since 5.3.2.18
     */
    public Integer getOrderType(){
        return orderType;
    }

    /**
     * 设置 订单类型 (对应salesOrder表中orderType).
     * 
     * @param orderType
     *            the new 订单类型
     * @since 5.3.2.18
     */
    public void setOrderType(Integer orderType){
        this.orderType = orderType;
    }

    /**
     * 获得 显示时候用的订单总价(包括total加上运费和包装信息相关金额的总和).
     * 
     * @return the 显示时候用的订单总价(包括total加上运费和包装信息相关金额的总和)
     * @since 5.3.2.18
     */
    public BigDecimal getDisplayTotal(){
        return displayTotal;
    }

    /**
     * 设置 显示时候用的订单总价(包括total加上运费和包装信息相关金额的总和).
     * 
     * @param displayTotal
     *            the new 显示时候用的订单总价(包括total加上运费和包装信息相关金额的总和).
     * @since 5.3.2.18
     */
    public void setDisplayTotal(BigDecimal displayTotal){
        this.displayTotal = displayTotal;
    }

    /**
     * 获得 商品总数量.
     * 
     * @return the 商品总数量
     * @since 5.3.2.22
     */
    public Integer getQuantity(){
        return quantity;
    }

    /**
     * 设置 商品总数量.
     * 
     * @param quantity
     *            the new 商品总数量.
     * @since 5.3.2.22
     */
    public void setQuantity(Integer quantity){
        this.quantity = quantity;
    }

}
