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
package com.baozun.nebula.sdk.command;

import java.math.BigDecimal;

import com.baozun.nebula.command.Command;

/**
 * SkuCommand.
 *
 * @author chenguang.zhou
 * @date 2014-3-12 下午02:20:34
 */
public class SkuCommand implements Command{

    /** The Constant serialVersionUID. */
    private static final long serialVersionUID = -669062912005060008L;

    /** skuId. */
    private Long              id;

    //************************************************************************

    /** 和oms 沟通交互的 唯一编码,extension1. */
    private String            extentionCode;

    /** 属性值，用于表征商品和SKU基于销售属性的划分对照，其标准格式目前定义为： 现在更改为([商品属性值id];)*([商品属性值id]) 这里的商品属性值id为ItemProperties实体的id. */
    private String            properties;

    /** 销售价. */
    private BigDecimal        salePrice;

    /** 吊牌价(原单价). */
    private BigDecimal        listPrice;

    /** 商品可用量. */
    private Integer           availableQty;

    //*****************************************************************************

    /** 1 商品已上架 0 商品未上架. */
    private String            state;

    //*****************************************************************************

    /**
     * 获得 skuId.
     *
     * @return the skuId
     */
    public Long getId(){
        return id;
    }

    /**
     * 设置 skuId.
     *
     * @param id
     *            the new skuId
     */
    public void setId(Long id){
        this.id = id;
    }

    /**
     * 获得 销售价.
     *
     * @return the 销售价
     */
    public BigDecimal getSalePrice(){
        return salePrice;
    }

    /**
     * 设置 销售价.
     *
     * @param salePrice
     *            the new 销售价
     */
    public void setSalePrice(BigDecimal salePrice){
        this.salePrice = salePrice;
    }

    /**
     * 获得 吊牌价(原单价).
     *
     * @return the 吊牌价(原单价)
     */
    public BigDecimal getListPrice(){
        return listPrice;
    }

    /**
     * 设置 吊牌价(原单价).
     *
     * @param listPrice
     *            the new 吊牌价(原单价)
     */
    public void setListPrice(BigDecimal listPrice){
        this.listPrice = listPrice;
    }

    /**
     * 获得 和oms 沟通交互的 唯一编码,extension1.
     *
     * @return the 和oms 沟通交互的 唯一编码,extension1
     */
    public String getExtentionCode(){
        return extentionCode;
    }

    /**
     * 设置 和oms 沟通交互的 唯一编码,extension1.
     *
     * @param extentionCode
     *            the new 和oms 沟通交互的 唯一编码,extension1
     */
    public void setExtentionCode(String extentionCode){
        this.extentionCode = extentionCode;
    }

    /**
     * 获得 商品可用量.
     *
     * @return the 商品可用量
     */
    public Integer getAvailableQty(){
        return availableQty;
    }

    /**
     * 设置 商品可用量.
     *
     * @param availableQty
     *            the new 商品可用量
     */
    public void setAvailableQty(Integer availableQty){
        this.availableQty = availableQty;
    }

    /**
     * 获得 1 商品已上架 0 商品未上架.
     *
     * @return the 1 商品已上架 0 商品未上架
     */
    public String getState(){
        return state;
    }

    /**
     * 设置 1 商品已上架 0 商品未上架.
     *
     * @param state
     *            the new 1 商品已上架 0 商品未上架
     */
    public void setState(String state){
        this.state = state;
    }

    /**
     * 获得 属性值，用于表征商品和SKU基于销售属性的划分对照，其标准格式目前定义为： 现在更改为([商品属性值id];)*([商品属性值id]) 这里的商品属性值id为ItemProperties实体的id.
     *
     * @return the 属性值，用于表征商品和SKU基于销售属性的划分对照，其标准格式目前定义为： 现在更改为([商品属性值id];)*([商品属性值id]) 这里的商品属性值id为ItemProperties实体的id
     */
    public String getProperties(){
        return properties;
    }

    /**
     * 设置 属性值，用于表征商品和SKU基于销售属性的划分对照，其标准格式目前定义为： 现在更改为([商品属性值id];)*([商品属性值id]) 这里的商品属性值id为ItemProperties实体的id.
     *
     * @param properties
     *            the new 属性值，用于表征商品和SKU基于销售属性的划分对照，其标准格式目前定义为： 现在更改为([商品属性值id];)*([商品属性值id]) 这里的商品属性值id为ItemProperties实体的id
     */
    public void setProperties(String properties){
        this.properties = properties;
    }
}
