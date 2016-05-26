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
 * 订单促销.
 *
 * @author chuanyang.zheng
 * @creattime 2013-11-20
 */
@Entity
@Table(name = "t_so_orderpromotion")
@org.hibernate.annotations.Entity(optimisticLock = OptimisticLockType.VERSION)
public class OrderPromotion extends BaseModel{

    /** The Constant serialVersionUID. */
    private static final long serialVersionUID = 7013653290362562965L;

    /** PK. */
    private Long              id;

    /** 订单id. */
    private Long              orderId;

    /** 订单行id. */
    private Long              orderLineId;

    /** 活动id(是否是促销以活动id为准). */
    private Long              activityId;

    /** 促销码 (暂时没用). */
    private String            promotionNo;

    /** 促销类型. */
    private String            promotionType;

    /** 折扣金额. */
    private BigDecimal        discountAmount;

    /** 优惠券. */
    private String            coupon;

    /** 描述. */
    private String            describe;

    /** 是否是整单优惠设置 *. */
    private Boolean           baseOrder;

    /** 是否是运费优惠 *. */
    private Boolean           isShipDiscount;

    /** version. */
    private Date              version;

    /**
     * 获得 pK.
     *
     * @return the pK
     */
    @Id
    @Column(name = "ID")
    @SequenceGenerator(name = "SEQ_T_SAL_ORDERPROMOTION",sequenceName = "S_T_SAL_ORDERPROMOTION",allocationSize = 1)
    @GeneratedValue(strategy = GenerationType.SEQUENCE,generator = "SEQ_T_SAL_ORDERPROMOTION")
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
     * 获得 订单行id.
     *
     * @return the 订单行id
     */
    @Column(name = "ORDER_LINE_ID")
    public Long getOrderLineId(){
        return orderLineId;
    }

    /**
     * 设置 订单行id.
     *
     * @param orderLineId
     *            the new 订单行id
     */
    public void setOrderLineId(Long orderLineId){
        this.orderLineId = orderLineId;
    }

    /**
     * 获得 活动id(是否是促销以活动id为准).
     *
     * @return the 活动id(是否是促销以活动id为准)
     */
    @Column(name = "ACTIVITY_ID")
    public Long getActivityId(){
        return activityId;
    }

    /**
     * 设置 活动id(是否是促销以活动id为准).
     *
     * @param activityId
     *            the new 活动id(是否是促销以活动id为准)
     */
    public void setActivityId(Long activityId){
        this.activityId = activityId;
    }

    /**
     * 获得 促销码 (暂时没用).
     *
     * @return the 促销码 (暂时没用)
     */
    @Column(name = "PROMOTION_NO",length = 100)
    public String getPromotionNo(){
        return promotionNo;
    }

    /**
     * 设置 促销码 (暂时没用).
     *
     * @param promotionNo
     *            the new 促销码 (暂时没用)
     */
    public void setPromotionNo(String promotionNo){
        this.promotionNo = promotionNo;
    }

    /**
     * 获得 促销类型.
     *
     * @return the 促销类型
     */
    @Column(name = "PROMOTION_TYPE",length = 100)
    public String getPromotionType(){
        return promotionType;
    }

    /**
     * 设置 促销类型.
     *
     * @param promotionType
     *            the new 促销类型
     */
    public void setPromotionType(String promotionType){
        this.promotionType = promotionType;
    }

    /**
     * 获得 折扣金额.
     *
     * @return the 折扣金额
     */
    @Column(name = "DISCOUNT_AMOUNT")
    public BigDecimal getDiscountAmount(){
        return discountAmount;
    }

    /**
     * 设置 折扣金额.
     *
     * @param discountAmount
     *            the new 折扣金额
     */
    public void setDiscountAmount(BigDecimal discountAmount){
        this.discountAmount = discountAmount;
    }

    /**
     * 获得 优惠券.
     *
     * @return the 优惠券
     */
    @Column(name = "COUPON",length = 100)
    public String getCoupon(){
        return coupon;
    }

    /**
     * 设置 优惠券.
     *
     * @param coupon
     *            the new 优惠券
     */
    public void setCoupon(String coupon){
        this.coupon = coupon;
    }

    /**
     * 获得 描述.
     *
     * @return the 描述
     */
    @Column(name = "DESCRIBE",length = 200)
    public String getDescribe(){
        return describe;
    }

    /**
     * 设置 描述.
     *
     * @param describe
     *            the new 描述
     */
    public void setDescribe(String describe){
        this.describe = describe;
    }

    /**
     * 获得 是否是整单优惠设置 *.
     *
     * @return the 是否是整单优惠设置 *
     */
    @Column(name = "BASEORDER")
    public Boolean getBaseOrder(){
        return baseOrder;
    }

    /**
     * 设置 是否是整单优惠设置 *.
     *
     * @param baseOrder
     *            the new 是否是整单优惠设置 *
     */
    public void setBaseOrder(Boolean baseOrder){
        this.baseOrder = baseOrder;
    }

    /**
     * 获得 是否是运费优惠 *.
     *
     * @return the 是否是运费优惠 *
     */
    @Column(name = "IS_SHIPDISCOUNT")
    public Boolean getIsShipDiscount(){
        return isShipDiscount;
    }

    /**
     * 设置 是否是运费优惠 *.
     *
     * @param isShipDiscount
     *            the new 是否是运费优惠 *
     */
    public void setIsShipDiscount(Boolean isShipDiscount){
        this.isShipDiscount = isShipDiscount;
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

}
