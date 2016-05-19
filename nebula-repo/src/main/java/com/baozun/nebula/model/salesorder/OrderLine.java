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
package com.baozun.nebula.model.salesorder;

import java.math.BigDecimal;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;

import org.hibernate.annotations.OptimisticLockType;

import com.baozun.nebula.model.BaseModel;

/**
 * 订单行.
 *
 * @author chuanyang.zheng
 * @creattime 2013-11-20
 * @see com.baozun.nebula.model.shoppingcart.ShoppingCartLine
 */
@Entity
@Table(name = "t_so_orderline")
@org.hibernate.annotations.Entity(optimisticLock = OptimisticLockType.VERSION)
public class OrderLine extends BaseModel{

    /** The Constant serialVersionUID. */
    private static final long serialVersionUID = 2540443429165056436L;

    /** PK. */
    private Long              id;

    //**************************************************

    /** 订单id. */
    private Long              orderId;

    /** sku表中的id. */
    private Long              skuId;

    /** UPC. */
    private String            extentionCode;

    /** 销售属性信息. */
    private String            saleProperty;

    /** 商品数量. */
    private Integer           count;

    //**************************************************

    /** 商品id. */
    private Long              itemId;

    /** 商品名称. */
    private String            itemName;

    /** 商品主图. */
    private String            itemPic;

    /**
     * 对应关联关系的商品id.
     * 
     * <p>
     * 普通商品下单,这个属性是没有值,<br>
     * 如果商品是bundle商品,比如3个不同的item 的不同sku 进行下单,那么会有3条 soline记录,在需要显示订单行的场合,比如订单明细页面,下单之后的邮件模板,<br>
     * 为了和以往的数据兼容(尽量不更改或者少更改以往的SQL),那么soline里面的 itemId,存放这个sku原本的itemId,而relatedItemId存放的是衍生的itemId
     * </p>
     * 
     * @since 5.3.1
     */
    private Long              relatedItemId;

    //**************************************************

    /** 原销售单价,吊牌价. */
    private BigDecimal        MSRP;

    /** 现销售单价. */
    private BigDecimal        salePrice;

    /** 行小计 行小计=现销售单价X数量-折扣. */
    private BigDecimal        subtotal;

    /** 折扣-包含整单优惠分摊. */
    private BigDecimal        discount;

    /** 折扣单价-不包含整单优惠分摊. */
    private BigDecimal        discountPrice;

    //**************************************************

    /** 行类型. */
    private Integer           type;

    /** 分组号. */
    private Integer           groupId;

    /** 评价状态. */
    private Integer           evaluationStatus;

    //**************************************************

    /** 商品快照版本. */
    private Integer           snapshot;

    /** version. */
    private Date              version;

    //**************************************************

    /**
     * 获得 pK.
     *
     * @return the pK
     */
    @Id
    @Column(name = "ID")
    @SequenceGenerator(name = "SEQ_T_SAL_ORDERDETAIL",sequenceName = "S_T_SAL_ORDERDETAIL",allocationSize = 1)
    @GeneratedValue(strategy = GenerationType.SEQUENCE,generator = "SEQ_T_SAL_ORDERDETAIL")
    public Long getId(){
        return id;
    }

    /**
     * 设置 pK.
     *
     * @param id
     *            the new pK
     */
    public void setId(Long id){
        this.id = id;
    }

    /**
     * 获得 订单id.
     *
     * @return the 订单id
     */
    @Column(name = "ORDER_ID")
    public Long getOrderId(){
        return orderId;
    }

    /**
     * 设置 订单id.
     *
     * @param orderId
     *            the new 订单id
     */
    public void setOrderId(Long orderId){
        this.orderId = orderId;
    }

    /**
     * 获得 uPC.
     *
     * @return the uPC
     */
    @Column(name = "EXTENTION_CODE",length = 200)
    public String getExtentionCode(){
        return extentionCode;
    }

    /**
     * 设置 uPC.
     *
     * @param extentionCode
     *            the new uPC
     */
    public void setExtentionCode(String extentionCode){
        this.extentionCode = extentionCode;
    }

    /**
     * 获得 sku表中的id.
     *
     * @return the sku表中的id
     */
    @Column(name = "SKU_ID")
    public Long getSkuId(){
        return skuId;
    }

    /**
     * 设置 sku表中的id.
     *
     * @param skuId
     *            the new sku表中的id
     */
    public void setSkuId(Long skuId){
        this.skuId = skuId;
    }

    /**
     * 获得 商品id.
     *
     * @return the 商品id
     */
    @Column(name = "ITEM_ID")
    public Long getItemId(){
        return itemId;
    }

    /**
     * 设置 商品id.
     *
     * @param itemId
     *            the new 商品id
     */
    public void setItemId(Long itemId){
        this.itemId = itemId;
    }

    /**
     * 获得 商品数量.
     *
     * @return the 商品数量
     */
    @Column(name = "COUNT")
    public Integer getCount(){
        return count;
    }

    /**
     * 设置 商品数量.
     *
     * @param count
     *            the new 商品数量
     */
    public void setCount(Integer count){
        this.count = count;
    }

    /**
     * 获得 msrp.
     *
     * @return the msrp
     */
    @Column(name = "MSRP")
    public BigDecimal getMSRP(){
        return MSRP;
    }

    /**
     * 设置 msrp.
     *
     * @param mSRP
     *            the msrp
     */
    public void setMSRP(BigDecimal mSRP){
        MSRP = mSRP;
    }

    /**
     * 获得 现销售单价.
     *
     * @return the 现销售单价
     */
    @Column(name = "SALE_PRICE")
    public BigDecimal getSalePrice(){
        return salePrice;
    }

    /**
     * 设置 现销售单价.
     *
     * @param salePrice
     *            the new 现销售单价
     */
    public void setSalePrice(BigDecimal salePrice){
        this.salePrice = salePrice;
    }

    /**
     * 获得 行小计 行小计=现销售单价X数量-折扣.
     *
     * @return the 行小计 行小计=现销售单价X数量-折扣
     */
    @Column(name = "SUBTOTAL")
    public BigDecimal getSubtotal(){
        return subtotal;
    }

    /**
     * 设置 行小计 行小计=现销售单价X数量-折扣.
     *
     * @param subtotal
     *            the new 行小计 行小计=现销售单价X数量-折扣
     */
    public void setSubtotal(BigDecimal subtotal){
        this.subtotal = subtotal;
    }

    /**
     * 获得 折扣-包含整单优惠分摊.
     *
     * @return the 折扣-包含整单优惠分摊
     */
    @Column(name = "DISCOUNT")
    public BigDecimal getDiscount(){
        return discount;
    }

    /**
     * 设置 折扣-包含整单优惠分摊.
     *
     * @param discount
     *            the new 折扣-包含整单优惠分摊
     */
    public void setDiscount(BigDecimal discount){
        this.discount = discount;
    }

    /**
     * 获得 商品名称.
     *
     * @return the 商品名称
     */
    @Column(name = "ITEM_NAME",length = 100)
    public String getItemName(){
        return itemName;
    }

    /**
     * 设置 商品名称.
     *
     * @param itemName
     *            the new 商品名称
     */
    public void setItemName(String itemName){
        this.itemName = itemName;
    }

    /**
     * 获得 商品主图.
     *
     * @return the 商品主图
     */
    @Column(name = "ITEM_PIC",length = 200)
    public String getItemPic(){
        return itemPic;
    }

    /**
     * 设置 商品主图.
     *
     * @param itemPic
     *            the new 商品主图
     */
    public void setItemPic(String itemPic){
        this.itemPic = itemPic;
    }

    /**
     * 获得 销售属性信息.
     *
     * @return the 销售属性信息
     */
    @Column(name = "SALE_PROPERTY",length = 200)
    public String getSaleProperty(){
        return saleProperty;
    }

    /**
     * 设置 销售属性信息.
     *
     * @param saleProperty
     *            the new 销售属性信息
     */
    public void setSaleProperty(String saleProperty){
        this.saleProperty = saleProperty;
    }

    /**
     * 获得 行类型.
     *
     * @return the 行类型
     */
    @Column(name = "TYPE")
    public Integer getType(){
        return type;
    }

    /**
     * 设置 行类型.
     *
     * @param type
     *            the new 行类型
     */
    public void setType(Integer type){
        this.type = type;
    }

    /**
     * 获得 分组号.
     *
     * @return the 分组号
     */
    @Column(name = "GROUP_ID")
    public Integer getGroupId(){
        return groupId;
    }

    /**
     * 设置 分组号.
     *
     * @param groupId
     *            the new 分组号
     */
    public void setGroupId(Integer groupId){
        this.groupId = groupId;
    }

    /**
     * 获得 评价状态.
     *
     * @return the 评价状态
     */
    @Column(name = "EVALUATION_STATUS")
    public Integer getEvaluationStatus(){
        return evaluationStatus;
    }

    /**
     * 设置 评价状态.
     *
     * @param evaluationStatus
     *            the new 评价状态
     */
    public void setEvaluationStatus(Integer evaluationStatus){
        this.evaluationStatus = evaluationStatus;
    }

    /**
     * 获得 商品快照版本.
     *
     * @return the 商品快照版本
     */
    @Column(name = "SNAPSHOT")
    public Integer getSnapshot(){
        return snapshot;
    }

    /**
     * 设置 商品快照版本.
     *
     * @param snapshot
     *            the new 商品快照版本
     */
    public void setSnapshot(Integer snapshot){
        this.snapshot = snapshot;
    }

    /**
     * 获得 折扣单价-不包含整单优惠分摊.
     *
     * @return the 折扣单价-不包含整单优惠分摊
     */
    @Column(name = "DISCOUNT_PRICE")
    public BigDecimal getDiscountPrice(){
        return discountPrice;
    }

    /**
     * 设置 折扣单价-不包含整单优惠分摊.
     *
     * @param discountPrice
     *            the new 折扣单价-不包含整单优惠分摊
     */
    public void setDiscountPrice(BigDecimal discountPrice){
        this.discountPrice = discountPrice;
    }

    /**
     * 获得 version.
     *
     * @return the version
     */
    @Column(name = "VERSION")
    public Date getVersion(){
        return version;
    }

    /**
     * 设置 version.
     *
     * @param version
     *            the new version
     */
    public void setVersion(Date version){
        this.version = version;
    }

    /**
     * 对应关联关系的商品id.
     * 
     * <p>
     * 普通商品下单,这个属性是没有值,<br>
     * 如果商品是bundle商品,比如3个不同的item 的不同sku 进行下单,那么会有3条 soline记录,在需要显示订单行的场合,比如订单明细页面,下单之后的邮件模板,<br>
     * 为了和以往的数据兼容(尽量不更改或者少更改以往的SQL),那么soline里面的 itemId,存放这个sku原本的itemId,而relatedItemId存放的是衍生的itemId
     * </p>
     *
     * @return the relatedItemId
     * @since 5.3.1
     */
    @Column(name = "RELATED_ITEM_ID")
    public Long getRelatedItemId(){
        return relatedItemId;
    }

    /**
     * 对应关联关系的商品id.
     * 
     * <p>
     * 普通商品下单,这个属性是没有值,<br>
     * 如果商品是bundle商品,比如3个不同的item 的不同sku 进行下单,那么会有3条 soline记录,在需要显示订单行的场合,比如订单明细页面,下单之后的邮件模板,<br>
     * 为了和以往的数据兼容(尽量不更改或者少更改以往的SQL),那么soline里面的 itemId,存放这个sku原本的itemId,而relatedItemId存放的是衍生的itemId
     * </p>
     *
     * @param relatedItemId
     *            the relatedItemId to set
     * @since 5.3.1
     */
    public void setRelatedItemId(Long relatedItemId){
        this.relatedItemId = relatedItemId;
    }
}
