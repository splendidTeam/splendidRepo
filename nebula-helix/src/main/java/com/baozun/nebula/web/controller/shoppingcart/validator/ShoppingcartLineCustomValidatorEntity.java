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
package com.baozun.nebula.web.controller.shoppingcart.validator;

import java.io.Serializable;

import com.baozun.nebula.command.ItemCommand;
import com.baozun.nebula.model.product.Sku;

/**
 * 自定义校验的相关参数属性.
 * 
 * @author <a href="http://feitianbenyue.iteye.com/">feilong</a>
 * @since 5.3.2.22
 */
public class ShoppingcartLineCustomValidatorEntity implements Serializable{

    /** The Constant serialVersionUID. */
    private static final long serialVersionUID = -914468006103291200L;

    /** 指定的sku. */
    private Sku sku;

    /** 对应的商品信息. */
    private ItemCommand itemCommand;

    /** 购买的数量. */
    private Integer count;

    //---------------------------------------------------------------------

    /**
     * Instantiates a new shoppingcart line custom validator entity.
     */
    public ShoppingcartLineCustomValidatorEntity(){
        super();
    }

    /**
     * Instantiates a new shoppingcart line custom validator entity.
     *
     * @param sku
     *            the sku
     * @param itemCommand
     *            the item command
     * @param count
     *            the count
     */
    public ShoppingcartLineCustomValidatorEntity(Sku sku, ItemCommand itemCommand, Integer count){
        super();
        this.sku = sku;
        this.itemCommand = itemCommand;
        this.count = count;
    }

    //---------------------------------------------------------------------

    /**
     * 获得 指定的sku.
     *
     * @return the sku
     */
    public Sku getSku(){
        return sku;
    }

    /**
     * 设置 指定的sku.
     *
     * @param sku
     *            the sku to set
     */
    public void setSku(Sku sku){
        this.sku = sku;
    }

    /**
     * 获得 对应的商品信息.
     *
     * @return the itemCommand
     */
    public ItemCommand getItemCommand(){
        return itemCommand;
    }

    /**
     * 设置 对应的商品信息.
     *
     * @param itemCommand
     *            the itemCommand to set
     */
    public void setItemCommand(ItemCommand itemCommand){
        this.itemCommand = itemCommand;
    }

    /**
     * 获得 购买的数量.
     *
     * @return the count
     */
    public Integer getCount(){
        return count;
    }

    /**
     * 设置 购买的数量.
     *
     * @param count
     *            the count to set
     */
    public void setCount(Integer count){
        this.count = count;
    }

}
