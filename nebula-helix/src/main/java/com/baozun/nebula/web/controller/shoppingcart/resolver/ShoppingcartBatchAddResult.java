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
package com.baozun.nebula.web.controller.shoppingcart.resolver;

import java.io.Serializable;
import java.util.Map;

/**
 * 购物车批量添加操作时候的结果实体.
 *
 * @author <a href="http://feitianbenyue.iteye.com/">feilong</a>
 * @see "com.feilong.coreextension.entity.BackWarnEntity"
 * @since 5.3.2.18
 */
public class ShoppingcartBatchAddResult implements Serializable{

    /**  */
    private static final long serialVersionUID = 288232184048495608L;

    /** 是否成功. */
    private boolean isSuccess = false;

    /** 如果有加入失败的,那么这里将返回 对应的sku id 和 原因; 成功的话 这里将是 null. */
    private Map<Long, ShoppingcartResult> skuIdAndShoppingcartResultFailMap;

    //---------------------------------------------------------------------
    /**
     * 
     */
    public ShoppingcartBatchAddResult(){
        super();
    }

    /**
     * @param isSuccess
     * @param skuIdAndShoppingcartResultFailMap
     */
    public ShoppingcartBatchAddResult(boolean isSuccess, Map<Long, ShoppingcartResult> skuIdAndShoppingcartResultFailMap){
        super();
        this.isSuccess = isSuccess;
        this.skuIdAndShoppingcartResultFailMap = skuIdAndShoppingcartResultFailMap;
    }

    //---------------------------------------------------------------------
    /**
     * 获得 是否成功.
     *
     * @return the isSuccess
     */
    public boolean getIsSuccess(){
        return isSuccess;
    }

    /**
     * 设置 是否成功.
     *
     * @param isSuccess
     *            the isSuccess to set
     */
    public void setIsSuccess(boolean isSuccess){
        this.isSuccess = isSuccess;
    }

    /**
     * 获得 如果有加入失败的,那么这里将返回 对应的sku id 和 原因; 成功的话 这里将是 null.
     *
     * @return the skuIdAndShoppingcartResultFailMap
     */
    public Map<Long, ShoppingcartResult> getSkuIdAndShoppingcartResultFailMap(){
        return skuIdAndShoppingcartResultFailMap;
    }

    /**
     * 设置 如果有加入失败的,那么这里将返回 对应的sku id 和 原因; 成功的话 这里将是 null.
     *
     * @param skuIdAndShoppingcartResultFailMap
     *            the skuIdAndShoppingcartResultFailMap to set
     */
    public void setSkuIdAndShoppingcartResultFailMap(Map<Long, ShoppingcartResult> skuIdAndShoppingcartResultFailMap){
        this.skuIdAndShoppingcartResultFailMap = skuIdAndShoppingcartResultFailMap;
    }

}
