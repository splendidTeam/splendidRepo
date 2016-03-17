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
 * 订单促销
 * 
 * @author chuanyang.zheng
 * @creattime 2013-11-20
 */
@Entity
@Table(name = "t_so_orderpromotion")
@org.hibernate.annotations.Entity(optimisticLock = OptimisticLockType.VERSION)
public class OrderPromotion extends BaseModel{


	private static final long serialVersionUID = 7013653290362562965L;

	/** PK. */
	private Long				id;
	
	/** 订单id */
	private Long				orderId;
	
	/** 订单行id */
	private Long				orderLineId;
	
	/** 活动id(是否是促销以活动id为准) */
	private Long				activityId;
	
	/** 促销码 */
	private String				promotionNo;
	
	/** 促销类型 */
	private String				promotionType;
	
	/** 折扣金额 */
	private BigDecimal			discountAmount;
	
	/** 优惠券 */
	private String				coupon;
	
	/** 描述 */
	private String				describe;
	
	/**是否是整单优惠设置**/
	private Boolean             baseOrder;
	
	/**是否是运费优惠**/
	private Boolean             isShipDiscount;
	
	/** version*/
	private Date				version;
	
	
	@Id
	@Column(name = "ID")
	@SequenceGenerator(name = "SEQ_T_SAL_ORDERPROMOTION",sequenceName = "S_T_SAL_ORDERPROMOTION",allocationSize = 1)
	@GeneratedValue(strategy = GenerationType.SEQUENCE,generator = "SEQ_T_SAL_ORDERPROMOTION")
	public Long getId() {
		return id;
	}


	public void setId(Long id) {
		this.id = id;
	}
	
	@Column(name = "ORDER_ID")
	public Long getOrderId() {
		return orderId;
	}


	public void setOrderId(Long orderId) {
		this.orderId = orderId;
	}
	
	
	
	@Column(name = "ORDER_LINE_ID")
	public Long getOrderLineId() {
		return orderLineId;
	}


	public void setOrderLineId(Long orderLineId) {
		this.orderLineId = orderLineId;
	}


	@Column(name = "ACTIVITY_ID")
	public Long getActivityId() {
		return activityId;
	}


	public void setActivityId(Long activityId) {
		this.activityId = activityId;
	}

	@Column(name = "PROMOTION_NO", length = 100)
	public String getPromotionNo() {
		return promotionNo;
	}


	public void setPromotionNo(String promotionNo) {
		this.promotionNo = promotionNo;
	}

	@Column(name = "PROMOTION_TYPE" , length = 100)
	public String getPromotionType() {
		return promotionType;
	}

	public void setPromotionType(String promotionType) {
		this.promotionType = promotionType;
	}


	@Column(name = "DISCOUNT_AMOUNT")
	public BigDecimal getDiscountAmount() {
		return discountAmount;
	}


	public void setDiscountAmount(BigDecimal discountAmount) {
		this.discountAmount = discountAmount;
	}

	@Column(name = "COUPON", length = 100)
	public String getCoupon() {
		return coupon;
	}


	public void setCoupon(String coupon) {
		this.coupon = coupon;
	}

	@Column(name = "DESCRIBE", length = 200)
	public String getDescribe() {
		return describe;
	}


	public void setDescribe(String describe) {
		this.describe = describe;
	}

	@Column(name = "BASEORDER")
	public Boolean getBaseOrder() {
		return baseOrder;
	}


	public void setBaseOrder(Boolean baseOrder) {
		this.baseOrder = baseOrder;
	}

	@Column(name = "IS_SHIPDISCOUNT")
	public Boolean getIsShipDiscount() {
		return isShipDiscount;
	}


	public void setIsShipDiscount(Boolean isShipDiscount) {
		this.isShipDiscount = isShipDiscount;
	}


	@Column(name = "VERSION")
	public Date getVersion() {
		return version;
	}


	public void setVersion(Date version) {
		this.version = version;
	}

	


	
}
