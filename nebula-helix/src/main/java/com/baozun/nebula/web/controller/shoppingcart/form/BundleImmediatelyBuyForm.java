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

/**
 * 立即购买 bundle需要的参数.
 *
 * @author feilong
 * @version 5.3.1 2016年5月28日 下午3:32:51
 * @since 5.3.1
 */
public class BundleImmediatelyBuyForm implements ImmediatelyBuyForm{

    /** The Constant serialVersionUID. */
    private static final long serialVersionUID = 2326678444030091977L;

    /** 听说bundle目前封装的 都是使用itemId做参数. */
    private Long              relatedItemId;

    /** 购买的 sku ids. */
    private Long[]            skuIds;

    /** bundle数量. */
    private Integer           count;

    /**
     * 获得 听说bundle目前封装的 都是使用itemId做参数.
     *
     * @return the relatedItemId
     */
    public Long getRelatedItemId(){
        return relatedItemId;
    }

    /**
     * 设置 听说bundle目前封装的 都是使用itemId做参数.
     *
     * @param relatedItemId
     *            the relatedItemId to set
     */
    public void setRelatedItemId(Long relatedItemId){
        this.relatedItemId = relatedItemId;
    }

    /**
     * 获得 购买的 sku ids.
     *
     * @return the skuIds
     */
    public Long[] getSkuIds(){
        return skuIds;
    }

    /**
     * 设置 购买的 sku ids.
     *
     * @param skuIds
     *            the skuIds to set
     */
    public void setSkuIds(Long[] skuIds){
        this.skuIds = skuIds;
    }

    /**
     * 获得 bundle数量.
     *
     * @return the count
     */
    public Integer getCount(){
        return count;
    }

    /**
     * 设置 bundle数量.
     *
     * @param count
     *            the count to set
     */
    public void setCount(Integer count){
        this.count = count;
    }
}
