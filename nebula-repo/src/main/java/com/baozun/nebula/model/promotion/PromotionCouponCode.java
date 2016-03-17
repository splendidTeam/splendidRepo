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

package com.baozun.nebula.model.promotion;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;
import javax.persistence.Version;

import org.hibernate.annotations.OptimisticLockType;

import com.baozun.nebula.model.BaseModel;

/**
 * @author - 项硕
 */
/**
 * @author 项硕
 */
@Entity
@Table(name = "t_prm_promotioncouponcode")
@org.hibernate.annotations.Entity(optimisticLock = OptimisticLockType.VERSION)
public class PromotionCouponCode extends BaseModel {

	private static final long serialVersionUID = 471543420659834589L;
	
	/** 有效 */
	public static final Integer ACTIVE_MARK_ON = 1;
	/** 失效 */
	public static final Integer ACTIVE_MARK_OFF = 0;
	/** coupon  0未使用 */
	public static final Integer NOT_USED = 0;
	/** coupon  1已使用 */
	public static final Integer USED = 1;
	

	/** PK */
	private Long id;

	/** 促销 */
	private String couponCode;
	
	/** 优惠券id */
	private Long couponId;

	/** 店铺id */
	private Long shopId;

	/** 起始时间 */
	private Date startTime;

	/** 结束时间 */
	private Date endTime;

	/** 创建人 */
	private Long createId;
	
	/** 0未使用 1已使用 */
	private Integer isused = 0;
 
	/** 创建时间 */
	private Date createTime;

	/** 1有效 0无效 */
	private Integer activeMark = 1;
	
	/** version. */
	private Date version;
	
	/**
	 * 使用次数
	 */
	private Integer limitTimes;

	@Id
	@Column(name = "ID")
	@SequenceGenerator(name = "SEQ_T_PRM_PROMOTIONCOUPONCODE",sequenceName = "S_T_PRM_PROMOTIONCOUPONCODE",allocationSize = 1)
	@GeneratedValue(strategy = GenerationType.SEQUENCE,generator = "SEQ_T_PRM_PROMOTIONCOUPONCODE")
	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	@Column(name = "COUPON_ID")
	public Long getCouponId() {
		return couponId;
	}

	public void setCouponId(Long couponId) {
		this.couponId = couponId;
	}

	@Column(name = "SHOP_ID")
	public Long getShopId() {
		return shopId;
	}

	public void setShopId(Long shopId) {
		this.shopId = shopId;
	}

	@Column(name = "COUPON_CODE")
	public String getCouponCode() {
		return couponCode;
	}

	public void setCouponCode(String couponCode) {
		this.couponCode = couponCode;
	}

	@Column(name = "START_TIME")
	public Date getStartTime() {
		return startTime;
	}

	public void setStartTime(Date startTime) {
		this.startTime = startTime;
	}

	@Column(name = "END_TIME")
	public Date getEndTime() {
		return endTime;
	}

	public void setEndTime(Date endTime) {
		this.endTime = endTime;
	}
	
	
	@Column(name = "ISUSED")
	public Integer getIsused() {
		return isused;
	}

	public void setIsused(Integer isused) {
		this.isused = isused;
	}
	
	@Column(name = "CREATE_ID")
	public Long getCreateId() {
		return createId;
	}

	public void setCreateId(Long createId) {
		this.createId = createId;
	}

	@Column(name = "CREATE_TIME")
	public Date getCreateTime() {
		return createTime;
	}

	public void setCreateTime(Date createTime) {
		this.createTime = createTime;
	}

	@Column(name = "ACTIVE_MARK")
	public Integer getActiveMark() {
		return activeMark;
	}

	public void setActiveMark(Integer activeMark) {
		this.activeMark = activeMark;
	}

	@Version
	@Column(name = "VERSION")
	public Date getVersion() {
		return version;
	}

	public void setVersion(Date version) {
		this.version = version;
	}
	@Column(name = "LIMIT_TIMES")
	public Integer getLimitTimes() {
		return limitTimes;
	}

	public void setLimitTimes(Integer limitTimes) {
		this.limitTimes = limitTimes;
	}

}
