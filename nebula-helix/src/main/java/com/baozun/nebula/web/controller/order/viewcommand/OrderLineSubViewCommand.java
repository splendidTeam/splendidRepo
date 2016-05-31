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
package com.baozun.nebula.web.controller.order.viewcommand;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;
import java.util.Map;

import com.baozun.nebula.model.product.Sku;
import com.baozun.nebula.model.salesorder.OrderLine;
import com.baozun.nebula.sdk.command.SkuProperty;
import com.baozun.nebula.web.controller.BaseViewCommand;

/**
 * The Class SimpleOrderLineSubViewCommand.
 *
 * @author <a href="http://feitianbenyue.iteye.com/">feilong</a>
 * @version 5.3.1 2016年5月6日 下午5:49:58
 * @see com.baozun.nebula.model.salesorder.OrderLine
 * @since 5.3.1
 */
public class OrderLineSubViewCommand extends BaseViewCommand{

    /** The Constant serialVersionUID. */
    private static final long        serialVersionUID = -4308131349902245508L;

    /** orderline行的唯一标识,那么此处的id={@link OrderLine#id}. */
    private Long                     id;

    /** 添加时间,此处的时间通常用于页面orderline行的排序,仅此而已. */
    private Date                     addTime;

    /** 买的什么商品id. */
    private Long                     itemId;

    /** 买的什么商品code. */
    private String                   itemCode;

    /** 商品名称是什么. */
    private String                   itemName;

    /** 买的哪个sku. */
    private Long                     skuId;

    /** 外部编码 {@link Sku#outid},是扣减库存以及和后端对接数据的核心参数. */
    private String                   extentionCode;

    //XXX feilong 销售属性map 是什么,此处应该可以和 PDP 骨架里面的相关view Command 通用 
    //参见 stander架构里面的  Map<PropertySubViewCommand, List<PropertyValueSubViewCommand>> salesPropertiesMap  
    //但是可能的结构是  Map<PropertySubViewCommand, PropertyValueSubViewCommand> salesPropertiesMap  
    //也可能的结构是  Map<String, String> salesPropertiesMap  
    /** The map. */
    private Map<String, SkuProperty> propertiesMap;

   /**
    * 销售属性 
    */
    private List<SkuProperty>                   skuPropertys;
    /** 数量几个. */
    private Integer                  quantity;

    //**************************************************************

    /** 商品图片. */
    private String                   itemPic;

    //***********************价格信息*****************************************************

    /** 销售价. */
    private BigDecimal               salePrice;

    /** 吊牌价(原单价). */
    private BigDecimal               listPrice;

    /** orderline行 金额小计 *. */
    private BigDecimal               subTotalAmt      = BigDecimal.ZERO;

    /**
     * 获得 orderline行的唯一标识,那么此处的id={@link OrderLine#id}.
     *
     * @return the id
     */
    public Long getId(){
        return id;
    }

    /**
     * 设置 orderline行的唯一标识,那么此处的id={@link OrderLine#id}.
     *
     * @param id
     *            the id to set
     */
    public void setId(Long id){
        this.id = id;
    }

    /**
     * 获得 添加时间,此处的时间通常用于页面orderline行的排序,仅此而已.
     *
     * @return the addTime
     */
    public Date getAddTime(){
        return addTime;
    }

    /**
     * 设置 添加时间,此处的时间通常用于页面orderline行的排序,仅此而已.
     *
     * @param addTime
     *            the addTime to set
     */
    public void setAddTime(Date addTime){
        this.addTime = addTime;
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
     * 获得 the map.
     *
     * @return the propertiesMap
     */
    public Map<String, SkuProperty> getPropertiesMap(){
        return propertiesMap;
    }

    /**
     * 设置 the map.
     *
     * @param propertiesMap
     *            the propertiesMap to set
     */
    public void setPropertiesMap(Map<String, SkuProperty> propertiesMap){
        this.propertiesMap = propertiesMap;
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

    /**
     * 获得 orderline行 金额小计 *.
     *
     * @return the subTotalAmt
     */
    public BigDecimal getSubTotalAmt(){
        return subTotalAmt;
    }

    /**
     * 设置 orderline行 金额小计 *.
     *
     * @param subTotalAmt
     *            the subTotalAmt to set
     */
    public void setSubTotalAmt(BigDecimal subTotalAmt){
        this.subTotalAmt = subTotalAmt;
    }

    public List<SkuProperty> getSkuPropertys() {
        return skuPropertys;
    }

    public void setSkuPropertys(List<SkuProperty> skuPropertys) {
        this.skuPropertys = skuPropertys;
    }

}
