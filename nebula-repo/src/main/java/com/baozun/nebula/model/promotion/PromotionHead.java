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
@Entity
@Table(name = "t_prm_promotionhead")
@org.hibernate.annotations.Entity(optimisticLock = OptimisticLockType.VERSION)
public class PromotionHead extends BaseModel {

	private static final long serialVersionUID = 2541763711816096284L;
	
	/** 生命周期：待启用（包括设置未结束的） */
	public static final Integer LIFECYCLE_UNACTIVATED = 0;
	/** 生命周期：已启用 */
	public static final Integer LIFECYCLE_ACTIVATED = 1;
	/** 生命周期：已生效 */
	public static final Integer LIFECYCLE_EFFECTIVE = 2;
	/** 生命周期：已取消 */
	public static final Integer LIFECYCLE_CANCELED = 3;

	/** PK */
	private Long id;

	/** 店铺id */
	private Long shopId;
	
	/** 促销名 */
	private String promotionName;

	/** 缺省优先级，为空。都为空时，按启用时间倒序排，启用时间和当前时间越近的优先级越高。1~10空冲突时自行调整。 */
	private Integer defaultpriority;

	/** 活动开始时间 */
	private Date startTime;

	/** 活动结束时间 */
	private Date endTime;

	/** 活动角标图片id */
	private Long promotionLogoType;

	/** 创建时间 */
	private Date createTime;

	/** 创建人员id */
	private Long createId;

	/** 最后修改时间 */
	private Date lastUpdateTime;

	/** 最后修改人员id */
	private Long lastUpdateId;

	/** 发布时间 */
	private Date publishTime;

	/** 发布人员id */
	private Long publishId;

	/** 取消启用时间 */
	private Date cancelTime;

	/** 取消启用人员id */
	private Long cancelId;

	/** 生命周期：0待启用（包括设置未结束的），1已启用，2已生效，3已取消（包括按有效期正常结束的） */
	private Integer lifecycle;

	/** 来源促销id：生效期内修改，复制一份待启用（草稿），记录草稿复制来源 */
	private Long copyFrom;
	
	/** version. */
	private Date version;
	
	public PromotionHead() {}

	/**
	 * @param id	促销id
	 */
	public PromotionHead(Long id) {
		this.id = id;
	}

	public PromotionHead(Long id, String name) {
		this.id = id;
		this.promotionName = name;
	}

	@Id
	@Column(name = "PROMOTION_ID")
	@SequenceGenerator(name = "SEQ_T_PRM_PROMOTIONHEAD",sequenceName = "S_T_PRM_PROMOTIONHEAD",allocationSize = 1)
	@GeneratedValue(strategy = GenerationType.SEQUENCE,generator = "SEQ_T_PRM_PROMOTIONHEAD")
	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	@Column(name = "PROMOTION_NAME", nullable = false, unique = true)
	public String getPromotionName() {
		return promotionName;
	}

	public void setPromotionName(String promotionName) {
		this.promotionName = promotionName;
	}

	@Column(name = "START_TIME", nullable = false)
	public Date getStartTime() {
		return startTime;
	}

	public void setStartTime(Date startTime) {
		this.startTime = startTime;
	}

	@Column(name = "END_TIME", nullable = false)
	public Date getEndTime() {
		return endTime;
	}

	public void setEndTime(Date endTime) {
		this.endTime = endTime;
	}

	@Column(name = "PROMOTION_LOGO_TYPE")
	public Long getPromotionLogoType() {
		return promotionLogoType;
	}

	public void setPromotionLogoType(Long promotionLogoType) {
		this.promotionLogoType = promotionLogoType;
	}

	@Column(name = "CREATE_TIME")
	public Date getCreateTime() {
		return createTime;
	}

	public void setCreateTime(Date createTime) {
		this.createTime = createTime;
	}

	@Column(name = "CREATE_ID")
	public Long getCreateId() {
		return createId;
	}

	public void setCreateId(Long createId) {
		this.createId = createId;
	}

	@Column(name = "LAST_UPDATE_TIME")
	public Date getLastUpdateTime() {
		return lastUpdateTime;
	}

	public void setLastUpdateTime(Date lastUpdateTime) {
		this.lastUpdateTime = lastUpdateTime;
	}

	@Column(name = "LAST_UPDATE_ID")
	public Long getLastUpdateId() {
		return lastUpdateId;
	}

	public void setLastUpdateId(Long lastUpdateId) {
		this.lastUpdateId = lastUpdateId;
	}

	@Column(name = "PUBLISH_TIME")
	public Date getPublishTime() {
		return publishTime;
	}

	public void setPublishTime(Date publishTime) {
		this.publishTime = publishTime;
	}

	@Column(name = "PUBLISH_ID")
	public Long getPublishId() {
		return publishId;
	}

	public void setPublishId(Long publishId) {
		this.publishId = publishId;
	}

	@Column(name = "CANCEL_TIME")
	public Date getCancelTime() {
		return cancelTime;
	}

	public void setCancelTime(Date cancelTime) {
		this.cancelTime = cancelTime;
	}

	@Column(name = "CANCEL_ID")
	public Long getCancelId() {
		return cancelId;
	}

	public void setCancelId(Long cancelId) {
		this.cancelId = cancelId;
	}

	@Column(name = "LIFECYCLE")
	public Integer getLifecycle() {
		return lifecycle;
	}

	public void setLifecycle(Integer lifecycle) {
		this.lifecycle = lifecycle;
	}

	@Column(name = "COPY_FROM")
	public Long getCopyFrom() {
		return copyFrom;
	}

	public void setCopyFrom(Long copyFrom) {
		this.copyFrom = copyFrom;
	}
	
	@Version
	@Column(name = "VERSION")
	public Date getVersion() {
		return version;
	}

	public void setVersion(Date version) {
		this.version = version;
	}

	@Column(name = "SHOP_ID")
	public Long getShopId() {
		return shopId;
	}

	public void setShopId(Long shopId) {
		this.shopId = shopId;
	}

	@Column(name = "DEFAULT_PRIORITY")
	public Integer getDefaultpriority() {
		return defaultpriority;
	}

	public void setDefaultpriority(Integer defaultpriority) {
		this.defaultpriority = defaultpriority;
	}
}
