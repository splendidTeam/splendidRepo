/**
 * Copyright (c) 2012 Baozun All Rights Reserved.
 *
 * This software is the confidential and proprietary information of Baozun.
 * You shall not disclose such Confidential Information and shall use it only in
 * accordance with the terms of the license agreement you entered into
 * with Baozun.
 *
 * BAOZUN MAKES NO REPRESENTATIONS OR WARRANTIES ABOUT THE SUITABILITY OF THE
 * SOFTWARE, EITHER EXPRESS OR IMPLIED, INCLUDING BUT NOT LIMITED TO THE
 * IMPLIED WARRANTIES OF MERCHANTABILITY, FITNESS FOR A PARTICULAR
 * PURPOSE, OR NON-INFRINGEMENT. BAOZUN SHALL NOT BE LIABLE FOR ANY DAMAGES
 * SUFFERED BY LICENSEE AS A RESULT OF USING, MODIFYING OR DISTRIBUTING
 * THIS SOFTWARE OR ITS DERIVATIVES.
 *
 */
package com.baozun.nebula.sdk.command;

import java.math.BigDecimal;
import java.util.List;


import com.baozun.nebula.model.BaseModel;

public class OrderLineCommand extends BaseModel{

    private static final long serialVersionUID = 2120158298004887685L;

    private Long id;

    /** 订单id **/
    private Long orderId;

    /** UPC */
    private String extentionCode;

    /** 商品id */
    private Long itemId;

    /** skuid */
    private Long skuId;

    /** 商品数量 */
    private Integer count;

    /** 原销售单价 */
    private BigDecimal MSRP;

    /** 现销售单价 */
    private BigDecimal salePrice;

    /** 行小计 */
    private BigDecimal subtotal;

    /** 折扣 */
    private BigDecimal discount;

    /** 商品名称 */
    private String itemName;

    /** 商品标签名称 */
    private List<String> propertyName;

    /** 商品主图 */
    private String itemPic;

    /** 销售属性信息 */
    private String saleProperty;

    /** 行类型 */
    private Integer type;

    /** 分组号 */
    private Integer groupId;

    /** 评价状态 */
    private Integer evaluationStatus;

    /** 商品快照版本 */
    private Integer snapshot;

    /** 订单行促销信息 */
    private List<OrderPromotionCommand> orderPromotions;

    /** 销售属性 **/
    private List<SkuProperty> skuPropertys;

    /**
     * 对应的包装信息.
     * 
     * @since 5.3.2.13
     */
    private List<OrderLinePackageInfoCommand> orderLinePackageInfoCommandList;

    /******* 扩展engine所需要的字段begin *******/

    private String productCode; // 商品code

    private List<Long> lableIds;

    private String brandId;

    private List<Long> categoryList;

    private Long storeId;

    private Long indstryId;

    private String state;

    /******** 扩展engine所需要的字段end **********/
    
  //***************************************************************************************************

    /** 杂项 可以存放商品信息. 
     * @since 5.3.2.18
     * */
    private String misc;

    //**********************************************************************************************

    /**
     * @return propertyName
     * @date 2016年1月29日 下午9:45:55
     */
    public List<String> getPropertyName(){
        return propertyName;
    }

    /**
     * @param propertyName
     *            要设置的 propertyName
     * @date 2016年1月29日 下午9:45:55
     */
    public void setPropertyName(List<String> propertyName){
        this.propertyName = propertyName;
    }

    public Long getOrderId(){
        return orderId;
    }

    public void setOrderId(Long orderId){
        this.orderId = orderId;
    }

    public Long getId(){
        return id;
    }

    public void setId(Long id){
        this.id = id;
    }

    public List<OrderPromotionCommand> getOrderPromotions(){
        return orderPromotions;
    }

    public void setOrderPromotions(List<OrderPromotionCommand> orderPromotions){
        this.orderPromotions = orderPromotions;
    }

    public String getExtentionCode(){
        return extentionCode;
    }

    public void setExtentionCode(String extentionCode){
        this.extentionCode = extentionCode;
    }

    public List<SkuProperty> getSkuPropertys(){
        return skuPropertys;
    }

    public void setSkuPropertys(List<SkuProperty> skuPropertys){
        this.skuPropertys = skuPropertys;
    }

    public Long getItemId(){
        return itemId;
    }

    public void setItemId(Long itemId){
        this.itemId = itemId;
    }

    public Integer getCount(){
        return count;
    }

    public void setCount(Integer count){
        this.count = count;
    }

    public BigDecimal getMSRP(){
        return MSRP;
    }

    public void setMSRP(BigDecimal mSRP){
        MSRP = mSRP;
    }

    public BigDecimal getSalePrice(){
        return salePrice;
    }

    public void setSalePrice(BigDecimal salePrice){
        this.salePrice = salePrice;
    }

    public BigDecimal getSubtotal(){
        return subtotal;
    }

    public void setSubtotal(BigDecimal subtotal){
        this.subtotal = subtotal;
    }

    public BigDecimal getDiscount(){
        return discount;
    }

    public void setDiscount(BigDecimal discount){
        this.discount = discount;
    }

    public String getItemName(){
        return itemName;
    }

    public void setItemName(String itemName){
        this.itemName = itemName;
    }

    public String getItemPic(){
        return itemPic;
    }

    public void setItemPic(String itemPic){
        this.itemPic = itemPic;
    }

    public String getSaleProperty(){
        return saleProperty;
    }

    public void setSaleProperty(String saleProperty){
        this.saleProperty = saleProperty;
    }

    public Integer getType(){
        return type;
    }

    public void setType(Integer type){
        this.type = type;
    }

    public Integer getGroupId(){
        return groupId;
    }

    public void setGroupId(Integer groupId){
        this.groupId = groupId;
    }

    public Integer getEvaluationStatus(){
        return evaluationStatus;
    }

    public void setEvaluationStatus(Integer evaluationStatus){
        this.evaluationStatus = evaluationStatus;
    }

    public Integer getSnapshot(){
        return snapshot;
    }

    public void setSnapshot(Integer snapshot){
        this.snapshot = snapshot;
    }

    public String getProductCode(){
        return productCode;
    }

    public void setProductCode(String productCode){
        this.productCode = productCode;
    }

    public List<Long> getLableIds(){
        return lableIds;
    }

    public void setLableIds(List<Long> lableIds){
        this.lableIds = lableIds;
    }

    public String getBrandId(){
        return brandId;
    }

    public void setBrandId(String brandId){
        this.brandId = brandId;
    }

    public List<Long> getCategoryList(){
        return categoryList;
    }

    public void setCategoryList(List<Long> categoryList){
        this.categoryList = categoryList;
    }

    public Long getStoreId(){
        return storeId;
    }

    public void setStoreId(Long storeId){
        this.storeId = storeId;
    }

    public Long getIndstryId(){
        return indstryId;
    }

    public void setIndstryId(Long indstryId){
        this.indstryId = indstryId;
    }

    public String getState(){
        return state;
    }

    public void setState(String state){
        this.state = state;
    }

    public Long getSkuId(){
        return skuId;
    }

    public void setSkuId(Long skuId){
        this.skuId = skuId;
    }

    /**
     * 对应的包装信息.
     * 
     * @since 5.3.2.13
     */
    public List<OrderLinePackageInfoCommand> getOrderLinePackageInfoCommandList(){
        return orderLinePackageInfoCommandList;
    }

    /**
     * 对应的包装信息.
     * 
     * @since 5.3.2.13
     */
    public void setOrderLinePackageInfoCommandList(List<OrderLinePackageInfoCommand> orderLinePackageInfoCommandList){
        this.orderLinePackageInfoCommandList = orderLinePackageInfoCommandList;
    }
    
    /**
     * 获取 杂项信息
     * @return String
     * @since 5.3.2.18
     */
    public String getMisc(){
        return misc;
    }

    /**
     * 设置 杂项信息
     * @param String
     * @since 5.3.2.18
     */
    public void setMisc(String misc){
        this.misc = misc;
    }
    
}
