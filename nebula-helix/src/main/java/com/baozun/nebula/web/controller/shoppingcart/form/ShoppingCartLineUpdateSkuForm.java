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
package com.baozun.nebula.web.controller.shoppingcart.form;

import java.io.Serializable;

/**
 * 购物车行修改sku的form.
 * 
 * <h3>说明:</h3>
 * <blockquote>
 * <ol>
 * <li>封装成对象,便于将来扩展</li>
 * </ol>
 * </blockquote>
 *
 * @author <a href="http://feitianbenyue.iteye.com/">feilong</a>
 * @see com.baozun.nebula.model.shoppingcart.ShoppingCartLine
 * @since 5.3.2.3
 */
public class ShoppingCartLineUpdateSkuForm implements Serializable{

    /** The Constant serialVersionUID. */
    private static final long serialVersionUID = -1175182005329799871L;

    /** 订单行要修改成什么新的sku(如果是null,等同于仅修改数量). */
    private Long newSkuId;

    /** 订单行最终修改的全量数量(必填). */
    private Integer count;

    /**
     * 获得 订单行要修改成什么新的sku(如果是null,等同于仅修改数量).
     *
     * @return the newSkuId
     */
    public Long getNewSkuId(){
        return newSkuId;
    }

    /**
     * 设置 订单行要修改成什么新的sku(如果是null,等同于仅修改数量).
     *
     * @param newSkuId
     *            the newSkuId to set
     */
    public void setNewSkuId(Long newSkuId){
        this.newSkuId = newSkuId;
    }

    /**
     * 获得 订单行最终修改的全量数量(必填).
     *
     * @return the count
     */
    public Integer getCount(){
        return count;
    }

    /**
     * 设置 订单行最终修改的全量数量(必填).
     *
     * @param count
     *            the count to set
     */
    public void setCount(Integer count){
        this.count = count;
    }

}
