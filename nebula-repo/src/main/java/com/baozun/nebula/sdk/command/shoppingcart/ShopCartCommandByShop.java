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
package com.baozun.nebula.sdk.command.shoppingcart;

import java.math.BigDecimal;

import com.baozun.nebula.command.Command;

/**
 * 基于店铺的金额和商品数量统计.
 *
 * @author 阳羽
 * @createtime 2014-4-1 下午01:15:08
 */
public class ShopCartCommandByShop implements Command{

    /** The Constant serialVersionUID. */
    private static final long serialVersionUID         = -5235640413320319863L;

    /** The shop id. */
    private Long              shopId;

    /** 商品数量. */
    private Integer           qty;

    /** 订单优惠. */
    private BigDecimal        disAmtOnOrder            = BigDecimal.ZERO;

    /** 整单优惠. */
    private BigDecimal        disAmtSingleOrder        = BigDecimal.ZERO;

    /** 应付小计. */
    private BigDecimal        subtotalCurrentPayAmount = BigDecimal.ZERO;

    /** 应付运费. */
    private BigDecimal        originShoppingFee        = BigDecimal.ZERO;

    /** 应付合计. */
    private BigDecimal        sumCurrentPayAmount;

    /** 优惠合计. */
    private BigDecimal        offersTotal              = BigDecimal.ZERO;

    /** 实付合计. */
    private BigDecimal        realPayAmount;

    /** 运费优惠. */
    private BigDecimal        offersShipping           = BigDecimal.ZERO;

    /**
     * 获得 shop id.
     *
     * @return the shop id
     */
    public Long getShopId(){
        return shopId;
    }

    /**
     * 设置 shop id.
     *
     * @param shopId
     *            the shop id
     */
    public void setShopId(Long shopId){
        this.shopId = shopId;
    }

    /**
     * 获得 运费优惠.
     *
     * @return the 运费优惠
     */
    public BigDecimal getOffersShipping(){
        return offersShipping;
    }

    /**
     * 设置 运费优惠.
     *
     * @param offersShipping
     *            the new 运费优惠
     */
    public void setOffersShipping(BigDecimal offersShipping){
        this.offersShipping = offersShipping;
    }

    /**
     * 获得 订单优惠.
     *
     * @return the 订单优惠
     */
    public BigDecimal getDisAmtOnOrder(){
        return disAmtOnOrder;
    }

    /**
     * 设置 订单优惠.
     *
     * @param disAmtOnOrder
     *            the new 订单优惠
     */
    public void setDisAmtOnOrder(BigDecimal disAmtOnOrder){
        this.disAmtOnOrder = disAmtOnOrder;
    }

    /**
     * 获得 整单优惠.
     *
     * @return the 整单优惠
     */
    public BigDecimal getDisAmtSingleOrder(){
        return disAmtSingleOrder;
    }

    /**
     * 设置 整单优惠.
     *
     * @param disAmtSingleOrder
     *            the new 整单优惠
     */
    public void setDisAmtSingleOrder(BigDecimal disAmtSingleOrder){
        this.disAmtSingleOrder = disAmtSingleOrder;
    }

    /**
     * 获得 应付小计.
     *
     * @return the 应付小计
     */
    public BigDecimal getSubtotalCurrentPayAmount(){
        return subtotalCurrentPayAmount;
    }

    /**
     * 设置 应付小计.
     *
     * @param subtotalCurrentPayAmount
     *            the new 应付小计
     */
    public void setSubtotalCurrentPayAmount(BigDecimal subtotalCurrentPayAmount){
        this.subtotalCurrentPayAmount = subtotalCurrentPayAmount;
    }

    /**
     * 获得 商品数量.
     *
     * @return the 商品数量
     */
    public Integer getQty(){
        return qty;
    }

    /**
     * 设置 商品数量.
     *
     * @param qty
     *            the new 商品数量
     */
    public void setQty(Integer qty){
        this.qty = qty;
    }

    /**
     * 获得 应付运费.
     *
     * @return the 应付运费
     */
    public BigDecimal getOriginShoppingFee(){
        return originShoppingFee;
    }

    /**
     * 设置 应付运费.
     *
     * @param originShoppingFee
     *            the new 应付运费
     */
    public void setOriginShoppingFee(BigDecimal originShoppingFee){
        this.originShoppingFee = originShoppingFee;
    }

    /**
     * 获得 应付合计.
     *
     * @return the 应付合计
     */
    public BigDecimal getSumCurrentPayAmount(){
        return sumCurrentPayAmount;
    }

    /**
     * 设置 应付合计.
     *
     * @param sumCurrentPayAmount
     *            the new 应付合计
     */
    public void setSumCurrentPayAmount(BigDecimal sumCurrentPayAmount){
        this.sumCurrentPayAmount = sumCurrentPayAmount;
    }

    /**
     * 获得 优惠合计.
     *
     * @return the 优惠合计
     */
    public BigDecimal getOffersTotal(){
        return offersTotal;
    }

    /**
     * 设置 优惠合计.
     *
     * @param offersTotal
     *            the new 优惠合计
     */
    public void setOffersTotal(BigDecimal offersTotal){
        this.offersTotal = offersTotal;
    }

    /**
     * 获得 实付合计.
     *
     * @return the 实付合计
     */
    public BigDecimal getRealPayAmount(){
        return realPayAmount;
    }

    /**
     * 设置 实付合计.
     *
     * @param realPayAmount
     *            the new 实付合计
     */
    public void setRealPayAmount(BigDecimal realPayAmount){
        this.realPayAmount = realPayAmount;
    }
}
