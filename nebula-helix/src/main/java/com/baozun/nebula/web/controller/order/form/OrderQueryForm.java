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
package com.baozun.nebula.web.controller.order.form;

import com.baozun.nebula.web.controller.BaseForm;

/**
 * The Class OrderQueryForm.
 *
 * @author <a href="http://feitianbenyue.iteye.com/">feilong</a>
 * @version 5.3.1 2016年5月6日 下午3:49:17
 * @since 5.3.1
 */
public class OrderQueryForm extends BaseForm{

    /** The Constant serialVersionUID. */
    private static final long serialVersionUID = 7412305764293905289L;

    /** 商品名称. */
    private String itemName;

    /** 商品code. */
    private String itemCode;

    /** 订单code. */
    private String orderCode;

    /** 订单状态 完成 等待付款 等待收获, 这里的值 在前端是允许定制的, 传入到后端 需要解析成 系统识别的 值(可能是是 财务状态+物流状态的组合). */
    private String orderStatus;

    /** 订单的时间类型, 比如 3个月之前的订单, 最近的订单, 2015年的订单, 等等等. */
    private String orderTimeType;

    //********************************************************************

    //    /**
    //     * 订单类型, 机票啊, 实务商品啊,虚拟商品啊 等等 类似于 京东的
    //     */
    //    private String              orderType;

    /**
     * 获得 商品名称.
     *
     * @return the itemName
     */
    public String getItemName(){
        return itemName;
    }

    /**
     * 设置 商品名称.
     *
     * @param itemName
     *            the itemName to set
     */
    public void setItemName(String itemName){
        this.itemName = itemName;
    }

    /**
     * 获得 商品code.
     *
     * @return the itemCode
     */
    public String getItemCode(){
        return itemCode;
    }

    /**
     * 设置 商品code.
     *
     * @param itemCode
     *            the itemCode to set
     */
    public void setItemCode(String itemCode){
        this.itemCode = itemCode;
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
     * 获得 订单状态 完成 等待付款 等待收获, 这里的值 在前端是允许定制的, 传入到后端 需要解析成 系统识别的 值(可能是是 财务状态+物流状态的组合).
     *
     * @return the orderStatus
     */
    public String getOrderStatus(){
        return orderStatus;
    }

    /**
     * 设置 订单状态 完成 等待付款 等待收获, 这里的值 在前端是允许定制的, 传入到后端 需要解析成 系统识别的 值(可能是是 财务状态+物流状态的组合).
     *
     * @param orderStatus
     *            the orderStatus to set
     */
    public void setOrderStatus(String orderStatus){
        this.orderStatus = orderStatus;
    }

    /**
     * 获得 订单的时间类型, 比如 3个月之前的订单, 最近的订单, 2015年的订单, 等等等.
     *
     * @return the orderTimeType
     */
    public String getOrderTimeType(){
        return orderTimeType;
    }

    /**
     * 设置 订单的时间类型, 比如 3个月之前的订单, 最近的订单, 2015年的订单, 等等等.
     *
     * @param orderTimeType
     *            the orderTimeType to set
     */
    public void setOrderTimeType(String orderTimeType){
        this.orderTimeType = orderTimeType;
    }

    @Override
    public String toString(){
        return "OrderQueryForm [itemName=" + itemName + ", itemCode=" + itemCode + ", orderCode=" + orderCode + ", orderStatus=" + orderStatus + ", orderTimeType=" + orderTimeType + "]";
    }

}
