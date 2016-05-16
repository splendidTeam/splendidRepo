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
package com.baozun.nebula.model.shoppingcart;

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
 * 购物车行信息.
 *
 * @author chuanyang.zheng
 * @creattime 2013-11-26
 * @see com.baozun.nebula.model.salesorder.OrderLine
 */
@Entity
@Table(name = "t_sc_shoppingcartline")
@org.hibernate.annotations.Entity(optimisticLock = OptimisticLockType.VERSION)
public class ShoppingCartLine extends BaseModel{

    /** The Constant serialVersionUID. */
    private static final long serialVersionUID = 2250754313263734498L;

    /** PK. */
    private Long              id;

    //********************************************************************************

    /** 会员id. */
    private Long              memberId;

    /** 店铺id *. */
    private Long              shopId;

    /** 商品id 其实是sku表中的id. */
    private Long              skuId;

    /** 商品条形码sku. */
    private String            extentionCode;

    /** 商品数量. */
    private Integer           quantity;

    /** 结算状态 0未选中结算 1选中结算. */
    private Integer           settlementState;

    //********************************************************************************
    /**
     * 商品id.
     * 
     * @since 5.3.1
     */
    private Long              itemId;

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

    //********************************************************************************

    /** 行分组. */
    private String            lineGroup;

    /** 促销号. */
    private Long              promotionId;

    /** 保存用户选择的赠品行，备选赠品行有引擎提供, 有行合并时，需要标识购物车更新. */
    private boolean           isGift;

    //********************************************************************************

    /** 加入时间. */
    private Date              createTime;

    /**
     * 获得 pK.
     *
     * @return the pK
     */
    @Id
    @Column(name = "ID")
    @SequenceGenerator(name = "SEQ_T_SC_SHOPPINGCARTLINE",sequenceName = "S_T_SC_SHOPPINGCARTLINE",allocationSize = 1)
    @GeneratedValue(strategy = GenerationType.SEQUENCE,generator = "SEQ_T_SC_SHOPPINGCARTLINE")
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
     * 获得 商品id 其实是sku表中的id.
     *
     * @return the 商品id 其实是sku表中的id
     */
    @Column(name = "SKU_ID")
    public Long getSkuId(){
        return skuId;
    }

    /**
     * 设置 商品id 其实是sku表中的id.
     *
     * @param skuId
     *            the new 商品id 其实是sku表中的id
     */
    public void setSkuId(Long skuId){
        this.skuId = skuId;
    }

    /**
     * 获得 商品数量.
     *
     * @return the 商品数量
     */
    @Column(name = "QUANTITY")
    public Integer getQuantity(){
        return quantity;
    }

    /**
     * 设置 商品数量.
     *
     * @param quantity
     *            the new 商品数量
     */
    public void setQuantity(Integer quantity){
        this.quantity = quantity;
    }

    /**
     * 获得 商品条形码sku.
     *
     * @return the 商品条形码sku
     */
    @Column(name = "EXTENTION_CODE",length = 200)
    public String getExtentionCode(){
        return extentionCode;
    }

    /**
     * 设置 商品条形码sku.
     *
     * @param extentionCode
     *            the new 商品条形码sku
     */
    public void setExtentionCode(String extentionCode){
        this.extentionCode = extentionCode;
    }

    /**
     * 获得 会员id.
     *
     * @return the 会员id
     */
    @Column(name = "MEMBER_ID")
    public Long getMemberId(){
        return memberId;
    }

    /**
     * 设置 会员id.
     *
     * @param memberId
     *            the new 会员id
     */
    public void setMemberId(Long memberId){
        this.memberId = memberId;
    }

    /**
     * 获得 加入时间.
     *
     * @return the 加入时间
     */
    @Column(name = "CREATE_TIME")
    public Date getCreateTime(){
        return createTime;
    }

    /**
     * 设置 加入时间.
     *
     * @param createTime
     *            the new 加入时间
     */
    public void setCreateTime(Date createTime){
        this.createTime = createTime;
    }

    /**
     * 获得 结算状态 0未选中结算 1选中结算.
     *
     * @return the 结算状态 0未选中结算 1选中结算
     */
    @Column(name = "SETTLEMENT_STATE")
    public Integer getSettlementState(){
        return settlementState;
    }

    /**
     * 设置 结算状态 0未选中结算 1选中结算.
     *
     * @param settlementState
     *            the new 结算状态 0未选中结算 1选中结算
     */
    public void setSettlementState(Integer settlementState){
        this.settlementState = settlementState;
    }

    /**
     * 获得 店铺id *.
     *
     * @return the 店铺id *
     */
    @Column(name = "SHOP_ID")
    public Long getShopId(){
        return shopId;
    }

    /**
     * 设置 店铺id *.
     *
     * @param shopId
     *            the new 店铺id *
     */
    public void setShopId(Long shopId){
        this.shopId = shopId;
    }

    /**
     * 获得 行分组.
     *
     * @return the 行分组
     */
    @Column(name = "LINE_GROUP")
    public String getLineGroup(){
        return lineGroup;
    }

    /**
     * 设置 行分组.
     *
     * @param lineGroup
     *            the new 行分组
     */
    public void setLineGroup(String lineGroup){
        this.lineGroup = lineGroup;
    }

    /**
     * 获得 促销号.
     *
     * @return the 促销号
     */
    @Column(name = "PROMOTION_ID")
    public Long getPromotionId(){
        return promotionId;
    }

    /**
     * 设置 促销号.
     *
     * @param promotionId
     *            the new 促销号
     */
    public void setPromotionId(Long promotionId){
        this.promotionId = promotionId;
    }

    /**
     * Checks if is gift.
     *
     * @return true, if checks if is gift
     */
    @Column(name = "IS_GIFT")
    public boolean isGift(){
        return isGift;
    }

    /**
     * 设置 gift.
     *
     * @param isGift
     *            the gift
     */
    public void setGift(boolean isGift){
        this.isGift = isGift;
    }

    /**
     * 获得 商品id.
     *
     * @return the itemId
     * @since 5.3.1
     */
    public Long getItemId(){
        return itemId;
    }

    /**
     * 设置 商品id.
     *
     * @param itemId
     *            the itemId to set
     * @since 5.3.1
     */
    public void setItemId(Long itemId){
        this.itemId = itemId;
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
