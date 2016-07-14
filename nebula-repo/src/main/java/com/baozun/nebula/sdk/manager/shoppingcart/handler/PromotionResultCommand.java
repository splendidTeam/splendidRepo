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
package com.baozun.nebula.sdk.manager.shoppingcart.handler;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import com.baozun.nebula.sdk.command.shoppingcart.ShoppingCartLineCommand;

/**
 * 匹配到促销之后,的结果.
 *
 * @author <a href="http://feitianbenyue.iteye.com/">feilong</a>
 * @since 5.3.1.6
 */
public class PromotionResultCommand{

    /** 商品行优惠金额. */
    private BigDecimal                    disAmtOnOrder        = BigDecimal.ZERO;

    /** 整单优惠金额. */
    private BigDecimal                    baseOnOrderDisAmt    = BigDecimal.ZERO;

    /** 运费整单优惠金额. */
    private BigDecimal                    offersShippingDisAmt = BigDecimal.ZERO;

    /** 礼品行. */
    private List<ShoppingCartLineCommand> giftList             = new ArrayList<ShoppingCartLineCommand>();

    /**
     * 获得 商品行优惠金额.
     *
     * @return the disAmtOnOrder
     */
    public BigDecimal getDisAmtOnOrder(){
        return disAmtOnOrder;
    }

    /**
     * 设置 商品行优惠金额.
     *
     * @param disAmtOnOrder
     *            the disAmtOnOrder to set
     */
    public void setDisAmtOnOrder(BigDecimal disAmtOnOrder){
        this.disAmtOnOrder = disAmtOnOrder;
    }

    /**
     * 获得 整单优惠金额.
     *
     * @return the baseOnOrderDisAmt
     */
    public BigDecimal getBaseOnOrderDisAmt(){
        return baseOnOrderDisAmt;
    }

    /**
     * 设置 整单优惠金额.
     *
     * @param baseOnOrderDisAmt
     *            the baseOnOrderDisAmt to set
     */
    public void setBaseOnOrderDisAmt(BigDecimal baseOnOrderDisAmt){
        this.baseOnOrderDisAmt = baseOnOrderDisAmt;
    }

    /**
     * 获得 运费整单优惠金额.
     *
     * @return the offersShippingDisAmt
     */
    public BigDecimal getOffersShippingDisAmt(){
        return offersShippingDisAmt;
    }

    /**
     * 设置 运费整单优惠金额.
     *
     * @param offersShippingDisAmt
     *            the offersShippingDisAmt to set
     */
    public void setOffersShippingDisAmt(BigDecimal offersShippingDisAmt){
        this.offersShippingDisAmt = offersShippingDisAmt;
    }

    /**
     * 获得 礼品行.
     *
     * @return the giftList
     */
    public List<ShoppingCartLineCommand> getGiftList(){
        return giftList;
    }

    /**
     * 设置 礼品行.
     *
     * @param giftList
     *            the giftList to set
     */
    public void setGiftList(List<ShoppingCartLineCommand> giftList){
        this.giftList = giftList;
    }

}
