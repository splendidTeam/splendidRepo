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
import java.util.Date;

import com.baozun.nebula.api.RiskControl;
import com.baozun.nebula.api.RiskLevel;
import com.baozun.nebula.command.Command;
import com.baozun.nebula.model.BaseModel;


public class OrderPromotionCommand extends BaseModel implements Command{

	
	/**
	 * 
	 */
	private static final long	serialVersionUID	= -8170527142261241167L;

	/** PK. */
	private Long				id;
	
	/** 订单id */
	private Long				orderId;
	
	/** 订单行id */
	private Long				orderLineId;
	
	/** 活动id */
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
	
	public Boolean getBaseOrder() {
		return baseOrder;
	}

	public void setBaseOrder(Boolean baseOrder) {
		this.baseOrder = baseOrder;
	}

	public Boolean getIsShipDiscount() {
		return isShipDiscount;
	}

	public void setIsShipDiscount(Boolean isShipDiscount) {
		this.isShipDiscount = isShipDiscount;
	}

	/** version*/
	private Date				version;

	@RiskControl(RiskLevel.MEDIUM)
	public Long getActivityId() {
		return activityId;
	}

	public void setActivityId(Long activityId) {
		this.activityId = activityId;
	}

	@RiskControl(RiskLevel.MEDIUM)
	public String getPromotionNo() {
		return promotionNo;
	}

	public void setPromotionNo(String promotionNo) {
		this.promotionNo = promotionNo;
	}

	@RiskControl(RiskLevel.MEDIUM)
	public BigDecimal getDiscountAmount() {
		return discountAmount;
	}

	public String getPromotionType() {
		return promotionType;
	}
	@RiskControl(RiskLevel.MEDIUM)
	public void setPromotionType(String promotionType) {
		this.promotionType = promotionType;
	}

	public void setDiscountAmount(BigDecimal discountAmount) {
		this.discountAmount = discountAmount;
	}

	@RiskControl(RiskLevel.MEDIUM)
	public String getCoupon() {
		return coupon;
	}

	public void setCoupon(String coupon) {
		this.coupon = coupon;
	}

	@RiskControl(RiskLevel.MEDIUM)
	public String getDescribe() {
		return describe;
	}

	public void setDescribe(String describe) {
		this.describe = describe;
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public Long getOrderId() {
		return orderId;
	}

	public void setOrderId(Long orderId) {
		this.orderId = orderId;
	}

	public Long getOrderLineId() {
		return orderLineId;
	}

	public void setOrderLineId(Long orderLineId) {
		this.orderLineId = orderLineId;
	}

	public Date getVersion() {
		return version;
	}

	public void setVersion(Date version) {
		this.version = version;
	}
}
