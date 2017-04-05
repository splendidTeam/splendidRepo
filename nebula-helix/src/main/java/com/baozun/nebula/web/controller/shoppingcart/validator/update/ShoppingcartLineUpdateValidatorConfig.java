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
package com.baozun.nebula.web.controller.shoppingcart.validator.update;

import java.io.Serializable;

/**
 * 更新购物车行 校验器对应的可定制化得配置.
 * 
 * <p>
 * 比如 在修改购物车行的时候,默认都是需要验证被修改行的库存,以及所有行相同sku的库存,<br>
 * 但是 gucci有需求 <a href="http://jira.baozun.cn/browse/NB-509?filter=-1">不需要校验库存</a>,这时,你可以通过实现 ShoppingcartLineUpdateValidatorConfigBuilder 接口来构造 ShoppingcartLineUpdateValidatorConfig 来控制
 * </p>
 *
 * @author <a href="http://feitianbenyue.iteye.com/">feilong</a>
 * @see <a href="http://jira.baozun.cn/browse/NB-509?filter=-1">购物车修改库存的时候不校验库存</a>
 * @since 5.3.2.14
 */
public class ShoppingcartLineUpdateValidatorConfig implements Serializable{

    /**  */
    private static final long serialVersionUID = 288232184048495608L;

    /** Static instance. */
    // the static instance works for all types
    public static final ShoppingcartLineUpdateValidatorConfig INSTANCE = new ShoppingcartLineUpdateValidatorConfig();

    //------------------------------------------------------------------------

    /** 是否校验单行库存. */
    private boolean isCheckSingleLineSkuInventory = true;

    /** 是否校验所有行库存. */
    private boolean isCheckTotalLineSameSkuInventory = true;

    /**
     * 获得 是否校验单行库存.
     *
     * @return the isCheckSingleLineSkuInventory
     */
    public boolean getIsCheckSingleLineSkuInventory(){
        return isCheckSingleLineSkuInventory;
    }

    /**
     * 设置 是否校验单行库存.
     *
     * @param isCheckSingleLineSkuInventory
     *            the isCheckSingleLineSkuInventory to set
     */
    public void setIsCheckSingleLineSkuInventory(boolean isCheckSingleLineSkuInventory){
        this.isCheckSingleLineSkuInventory = isCheckSingleLineSkuInventory;
    }

    /**
     * 获得 是否校验所有行库存.
     *
     * @return the isCheckTotalLineSameSkuInventory
     */
    public boolean getIsCheckTotalLineSameSkuInventory(){
        return isCheckTotalLineSameSkuInventory;
    }

    /**
     * 设置 是否校验所有行库存.
     *
     * @param isCheckTotalLineSameSkuInventory
     *            the isCheckTotalLineSameSkuInventory to set
     */
    public void setIsCheckTotalLineSameSkuInventory(boolean isCheckTotalLineSameSkuInventory){
        this.isCheckTotalLineSameSkuInventory = isCheckTotalLineSameSkuInventory;
    }

}
