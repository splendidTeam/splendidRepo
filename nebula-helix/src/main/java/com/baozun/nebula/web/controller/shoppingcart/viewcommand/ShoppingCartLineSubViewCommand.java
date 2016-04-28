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
package com.baozun.nebula.web.controller.shoppingcart.viewcommand;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;
import java.util.Map;

import com.baozun.nebula.model.product.Sku;
import com.baozun.nebula.model.shoppingcart.ShoppingCartLine;
import com.baozun.nebula.sdk.command.SkuProperty;

/**
 * 购物车里面的每行明细.
 *
 * @author feilong
 * @version 5.3.1 2016年4月25日 上午11:13:26
 * @see com.baozun.nebula.sdk.command.shoppingcart.ShoppingCartLineCommand
 * @see com.baozun.nebula.model.shoppingcart.ShoppingCartLine
 * @see com.baozun.nebula.dao.shoppingcart.SdkShoppingCartLineDao
 * @see com.baozun.nebula.sdk.manager.SdkShoppingCartManager#findShoppingCartLinesByMemberId(Long, Integer)
 * @since 5.3.1
 */
public class ShoppingCartLineSubViewCommand implements Serializable{

    /** The Constant serialVersionUID. */
    private static final long serialVersionUID = -6308223304441399376L;

    /**
     * 购物车行的唯一标识,如果是会员购物车,那么此处的id={@link ShoppingCartLine#id},如果是游客的购物车,那么自己算出id,以遍对这个id进行删除/修改.
     */
    private Long              id;

    /** 添加时间,此处的时间通常用于页面购物车行的排序,仅此而已. */
    private Date              addTime;

    //**************************************************************

    /** 买的哪个店铺 . */
    private Long              shopId;

    /** 买的什么商品id. */
    private Long              itemId;

    /** 买的什么商品code. */
    private String            itemCode;

    /** 商品名称是什么. */
    private String            itemName;

    /** 买的哪个sku. */
    private Long              skuId;

    /** 外部编码 {@link Sku#outid},是扣减库存以及和后端对接数据的核心参数. */
    private String            extentionCode;

    //TODO 销售属性map 是什么,此处应该可以和 PDP 骨架里面的相关view Command 通用 
    //参见 stander架构里面的  Map<PropertySubViewCommand, List<PropertyValueSubViewCommand>> salesPropertiesMap  
    //但是可能的结构是  Map<PropertySubViewCommand, PropertyValueSubViewCommand> salesPropertiesMap  
    //也可能的结构是  Map<String, String> salesPropertiesMap  
    /** The map. key:property名称，value：property对象*/
    private Map<String, SkuProperty>               propertiesMap;

	/** 数量几个. */
    private Integer           quantity;

    //**************************************************************

    /** 商品图片. */
    private String            itemPic;
    //**************************************************************

    /**
     * 是否选中,主要用来渲染view里面的checkbox checked状态.
     * 
     * 参见 {@link com.baozun.nebula.model.shoppingcart.ShoppingCartLine#getSettlementState()} 结算状态,表结构里面是使用int来标识的,"0未选中结算,1选中结算"
     * ,其实就两个状态,在前端view里面 重新设计为boolean,true为选中,false为不选中
     * 
     * @see com.baozun.nebula.model.shoppingcart.ShoppingCartLine#getSettlementState()
     * @see com.baozun.nebula.sdk.command.shoppingcart.ShoppingCartLineCommand#getSettlementState()
     */
    private boolean           checked;

    /**
     * 是否是赠品.
     * 
     * 参见 {@link com.baozun.nebula.model.shoppingcart.ShoppingCartLine#isGift()}
     */
    private boolean           isGift;

    //***********************价格信息*****************************************************

    /** 销售价. */
    private BigDecimal        salePrice;

    /** 吊牌价(原单价). */
    private BigDecimal        listPrice;

    /**
     * 获得 购物车行的唯一标识,如果是会员购物车,那么此处的id={@link ShoppingCartLine#id},如果是游客的购物车,那么自己算出id,以遍对这个id进行删除/修改.
     *
     * @return the id
     */
    public Long getId(){
        return id;
    }

    /**
     * 设置 购物车行的唯一标识,如果是会员购物车,那么此处的id={@link ShoppingCartLine#id},如果是游客的购物车,那么自己算出id,以遍对这个id进行删除/修改.
     *
     * @param id
     *            the id to set
     */
    public void setId(Long id){
        this.id = id;
    }

    /**
     * 获得 添加时间,此处的时间通常用于页面购物车行的排序,仅此而已.
     *
     * @return the addTime
     */
    public Date getAddTime(){
        return addTime;
    }

    /**
     * 设置 添加时间,此处的时间通常用于页面购物车行的排序,仅此而已.
     *
     * @param addTime
     *            the addTime to set
     */
    public void setAddTime(Date addTime){
        this.addTime = addTime;
    }

    /**
     * 获得 买的哪个店铺 .
     *
     * @return the shopId
     */
    public Long getShopId(){
        return shopId;
    }

    /**
     * 设置 买的哪个店铺 .
     *
     * @param shopId
     *            the shopId to set
     */
    public void setShopId(Long shopId){
        this.shopId = shopId;
    }

    /**
     * 获得 买的什么商品id.
     *
     * @return the itemId
     */
    public Long getItemId(){
        return itemId;
    }

    /**
     * 设置 买的什么商品id.
     *
     * @param itemId
     *            the itemId to set
     */
    public void setItemId(Long itemId){
        this.itemId = itemId;
    }

    /**
     * 获得 买的什么商品code.
     *
     * @return the itemCode
     */
    public String getItemCode(){
        return itemCode;
    }

    /**
     * 设置 买的什么商品code.
     *
     * @param itemCode
     *            the itemCode to set
     */
    public void setItemCode(String itemCode){
        this.itemCode = itemCode;
    }

    /**
     * 获得 商品名称是什么.
     *
     * @return the itemName
     */
    public String getItemName(){
        return itemName;
    }

    /**
     * 设置 商品名称是什么.
     *
     * @param itemName
     *            the itemName to set
     */
    public void setItemName(String itemName){
        this.itemName = itemName;
    }

    /**
     * 获得 买的哪个sku.
     *
     * @return the skuId
     */
    public Long getSkuId(){
        return skuId;
    }

    /**
     * 设置 买的哪个sku.
     *
     * @param skuId
     *            the skuId to set
     */
    public void setSkuId(Long skuId){
        this.skuId = skuId;
    }

    /**
     * 获得 外部编码 {@link Sku#outid},是扣减库存以及和后端对接数据的核心参数.
     *
     * @return the extentionCode
     */
    public String getExtentionCode(){
        return extentionCode;
    }

    /**
     * 设置 外部编码 {@link Sku#outid},是扣减库存以及和后端对接数据的核心参数.
     *
     * @param extentionCode
     *            the extentionCode to set
     */
    public void setExtentionCode(String extentionCode){
        this.extentionCode = extentionCode;
    }

    /**
     * 获得 数量几个.
     *
     * @return the quantity
     */
    public Integer getQuantity(){
        return quantity;
    }

    /**
     * 设置 数量几个.
     *
     * @param quantity
     *            the quantity to set
     */
    public void setQuantity(Integer quantity){
        this.quantity = quantity;
    }

    /**
     * 获得 商品图片.
     *
     * @return the itemPic
     */
    public String getItemPic(){
        return itemPic;
    }

    /**
     * 设置 商品图片.
     *
     * @param itemPic
     *            the itemPic to set
     */
    public void setItemPic(String itemPic){
        this.itemPic = itemPic;
    }

    /**
     * 获得 是否选中,主要用来渲染view里面的checkbox checked状态.
     *
     * @return the checked
     */
    public boolean getChecked(){
        return checked;
    }

    /**
     * 设置 是否选中,主要用来渲染view里面的checkbox checked状态.
     *
     * @param checked
     *            the checked to set
     */
    public void setChecked(boolean checked){
        this.checked = checked;
    }

    /**
     * 获得 是否是赠品.
     *
     * @return the isGift
     */
    public boolean getIsGift(){
        return isGift;
    }

    /**
     * 设置 是否是赠品.
     *
     * @param isGift
     *            the isGift to set
     */
    public void setIsGift(boolean isGift){
        this.isGift = isGift;
    }

    /**
     * 获得 销售价.
     *
     * @return the salePrice
     */
    public BigDecimal getSalePrice(){
        return salePrice;
    }

    /**
     * 设置 销售价.
     *
     * @param salePrice
     *            the salePrice to set
     */
    public void setSalePrice(BigDecimal salePrice){
        this.salePrice = salePrice;
    }

    /**
     * 获得 吊牌价(原单价).
     *
     * @return the listPrice
     */
    public BigDecimal getListPrice(){
        return listPrice;
    }

    /**
     * 设置 吊牌价(原单价).
     *
     * @param listPrice
     *            the listPrice to set
     */
    public void setListPrice(BigDecimal listPrice){
        this.listPrice = listPrice;
    }
    

    public Map<String, SkuProperty> getPropertiesMap() {
		return propertiesMap;
	}

	public void setPropertiesMap(Map<String, SkuProperty> propertiesMap) {
		this.propertiesMap = propertiesMap;
	}

}
