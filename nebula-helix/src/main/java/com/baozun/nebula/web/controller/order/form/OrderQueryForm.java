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

import java.text.ParseException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import com.baozun.nebula.model.salesorder.SalesOrder;
import com.baozun.nebula.web.command.OrderQueryCommand;
import com.baozun.nebula.web.controller.BaseForm;
import com.feilong.core.Validator;
import com.feilong.core.bean.PropertyUtil;

/**
 * The Class OrderQueryForm.
 *
 * @author feilong
 * @version 5.3.1 2016年5月6日 下午3:49:17
 * @since 5.3.1
 */
public class OrderQueryForm extends BaseForm{

    /** The Constant serialVersionUID. */
    private static final long serialVersionUID = 7412305764293905289L;
    
   /**
    * 新建（未支付）
    */
    private static final String     orderStatus_new = "new";

    /** 商品名称. */
    private String            itemName;

    /** 商品code. */
    private String            itemCode;

    /** 订单code. */
    private String            orderCode;

    /** 订单状态 完成 等待付款 等待收获, 这里的值 在前端是允许定制的, 传入到后端 需要解析成 系统识别的 值(可能是是 财务状态+物流状态的组合). */
    private String            orderStatus;

    /** 订单的时间类型, 比如 3个月之前的订单, 最近的订单, 2015年的订单, 等等等. */
    private String            orderTimeType;

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

    
    /**
     * 
     * 说明：转换为OrderQueryCommand，前台条件转换
     * 
     * @param orderqueryform
     * @return
     * @author 张乃骐
     * @throws ParseException
     * @time：2016年5月9日 下午5:01:05
     */
    public OrderQueryCommand convertToOrderQueryCommand(OrderQueryCommand orderQueryCommand){
        PropertyUtil.copyProperties(orderQueryCommand, this, "itemName", "itemCode", "orderCode");
        //订单的时间类型判断及转换
        String orderTimeType = this.getOrderTimeType();
        if (Validator.isNotNullOrEmpty(orderTimeType)){
            OrderTimeType orderTimeTypeEnum = OrderTimeType.getInstance(orderTimeType);
            Date[] beginAndEndDate = orderTimeTypeEnum.getBeginAndEndDate();
            orderQueryCommand.setStartDate(beginAndEndDate[0]);
            orderQueryCommand.setEndDate(beginAndEndDate[1]);
        }
        //订单类型（未支付，发货中，以完成，取消）
        String orderStatus = this.getOrderStatus();
        if (Validator.isNotNullOrEmpty(orderStatus)){
            List<Integer> financestatusList = new ArrayList<Integer>();
            //未完成的订单
            if (orderStatus.equals(orderStatus_new)){
                financestatusList.add(SalesOrder.SALES_ORDER_FISTATUS_NO_PAYMENT);
            }
            //TODO 其他状态的判断
        }
        return orderQueryCommand;
    }
}
