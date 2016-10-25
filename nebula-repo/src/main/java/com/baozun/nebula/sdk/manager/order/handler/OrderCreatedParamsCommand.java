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
package com.baozun.nebula.sdk.manager.order.handler;

import java.util.List;

import com.baozun.nebula.sdk.command.SalesOrderCommand;
import com.baozun.nebula.sdk.command.shoppingcart.PromotionSKUDiscAMTBySetting;
import com.baozun.nebula.sdk.command.shoppingcart.ShopCartCommandByShop;
import com.baozun.nebula.sdk.command.shoppingcart.ShoppingCartLineCommand;

/**
 * 订单创建成功之后使用的参数信息(基于每笔订单).
 *
 * @author <a href="http://feitianbenyue.iteye.com/">feilong</a>
 * @since 5.3.2.2
 */
public class OrderCreatedParamsCommand{

    /** 订单id. */
    private Long orderId;

    /** 该订单所属店铺. */
    private Long shopId;

    /** 该订单流水号. */
    private String subOrdinate;

    /** 该订单订单行信息. */
    private List<ShoppingCartLineCommand> shoppingCartLineCommandList;

    /** 该订单订单相关信息(比如收货地址等). */
    private SalesOrderCommand salesOrderCommand;

    /** 该订单基于店铺的一些优惠统计. */
    private ShopCartCommandByShop shopCartCommandByShop;

    /** 该订单一些促销信息. */
    private List<PromotionSKUDiscAMTBySetting> promotionSKUDiscAMTBySettingList;

    /**
     * 获得 该订单所属店铺.
     *
     * @return the shopId
     */
    public Long getShopId(){
        return shopId;
    }

    /**
     * 设置 该订单所属店铺.
     *
     * @param shopId
     *            the shopId to set
     */
    public void setShopId(Long shopId){
        this.shopId = shopId;
    }

    /**
     * 获得 该订单流水号.
     *
     * @return the subOrdinate
     */
    public String getSubOrdinate(){
        return subOrdinate;
    }

    /**
     * 设置 该订单流水号.
     *
     * @param subOrdinate
     *            the subOrdinate to set
     */
    public void setSubOrdinate(String subOrdinate){
        this.subOrdinate = subOrdinate;
    }

    /**
     * 获得 该订单订单行信息.
     *
     * @return the shoppingCartLineCommandList
     */
    public List<ShoppingCartLineCommand> getShoppingCartLineCommandList(){
        return shoppingCartLineCommandList;
    }

    /**
     * 设置 该订单订单行信息.
     *
     * @param shoppingCartLineCommandList
     *            the shoppingCartLineCommandList to set
     */
    public void setShoppingCartLineCommandList(List<ShoppingCartLineCommand> shoppingCartLineCommandList){
        this.shoppingCartLineCommandList = shoppingCartLineCommandList;
    }

    /**
     * 获得 该订单订单相关信息(比如收货地址等).
     *
     * @return the salesOrderCommand
     */
    public SalesOrderCommand getSalesOrderCommand(){
        return salesOrderCommand;
    }

    /**
     * 设置 该订单订单相关信息(比如收货地址等).
     *
     * @param salesOrderCommand
     *            the salesOrderCommand to set
     */
    public void setSalesOrderCommand(SalesOrderCommand salesOrderCommand){
        this.salesOrderCommand = salesOrderCommand;
    }

    /**
     * 获得 该订单基于店铺的一些优惠统计.
     *
     * @return the shopCartCommandByShop
     */
    public ShopCartCommandByShop getShopCartCommandByShop(){
        return shopCartCommandByShop;
    }

    /**
     * 设置 该订单基于店铺的一些优惠统计.
     *
     * @param shopCartCommandByShop
     *            the shopCartCommandByShop to set
     */
    public void setShopCartCommandByShop(ShopCartCommandByShop shopCartCommandByShop){
        this.shopCartCommandByShop = shopCartCommandByShop;
    }

    /**
     * 获得 该订单一些促销信息.
     *
     * @return the promotionSKUDiscAMTBySettingList
     */
    public List<PromotionSKUDiscAMTBySetting> getPromotionSKUDiscAMTBySettingList(){
        return promotionSKUDiscAMTBySettingList;
    }

    /**
     * 设置 该订单一些促销信息.
     *
     * @param promotionSKUDiscAMTBySettingList
     *            the promotionSKUDiscAMTBySettingList to set
     */
    public void setPromotionSKUDiscAMTBySettingList(List<PromotionSKUDiscAMTBySetting> promotionSKUDiscAMTBySettingList){
        this.promotionSKUDiscAMTBySettingList = promotionSKUDiscAMTBySettingList;
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

}
