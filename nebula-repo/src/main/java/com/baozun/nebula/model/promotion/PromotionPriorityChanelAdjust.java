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

import org.hibernate.annotations.Index;
import org.hibernate.annotations.OptimisticLockType;

import com.baozun.nebula.model.BaseModel;

/**
 * 按照渠道（人群）定制的，按活动调整的
 * 
 * @author - 项硕
 */
@Entity
@Table(name = "T_PRM_PROMOTIONPRIORITYCHANELADJUST")
@org.hibernate.annotations.Entity(optimisticLock = OptimisticLockType.VERSION)
public class PromotionPriorityChanelAdjust extends BaseModel {

	private static final long serialVersionUID = -4140267105748160179L;

	/** PK */
	private Long id;

	/** 渠道编号：受益人群（渠道） */
	private Long chanelId;

	/** 促销活动id */
	private Long promotionId;

	/** 优先级 */
	private Integer priority;

	/** 生效标志：1参与，0不参与计算 */
	private Integer effectMark;

	/** 修改人员id */
	private Long lastUpdateId;

	/** 修改时间 */
	private Date lastUpdateTime;
	
	private Date version;

	@Id
	@Column(name = "ID")
	@SequenceGenerator(name = "SEQ_T_PROMOTIONPRIORITYCHANELADJUST",sequenceName = "S_T_PRM_PROMOTIONPRIORITYCHANELADJUST",allocationSize = 1)
	@GeneratedValue(strategy = GenerationType.SEQUENCE,generator = "SEQ_T_PROMOTIONPRIORITYCHANELADJUST")
	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	@Column(name = "CHANEL_ID")
    @Index(name = "IDX_PROMOTIONPRIORITYCHANELADJUST_CHANEL_ID")
	public Long getChanelId() {
		return chanelId;
	}

	public void setChanelId(Long chanelId) {
		this.chanelId = chanelId;
	}

	@Column(name = "PROMOTION_ID")
    @Index(name = "IDX_PROMOTIONPRIORITYCHANELADJUST_PROMOTION_ID")
	public Long getPromotionId() {
		return promotionId;
	}

	public void setPromotionId(Long promotionId) {
		this.promotionId = promotionId;
	}

	@Column(name = "PRIORITY")
	public Integer getPriority() {
		return priority;
	}

	public void setPriority(Integer priority) {
		this.priority = priority;
	}

	@Column(name = "EFFECT_MARK")
	public Integer getEffectMark() {
		return effectMark;
	}

	public void setEffectMark(Integer effectMark) {
		this.effectMark = effectMark;
	}

	@Column(name = "LAST_UPDATE_ID")
    @Index(name = "IDX_PROMOTIONPRIORITYCHANELADJUST_LAST_UPDATE_ID")
	public Long getLastUpdateId() {
		return lastUpdateId;
	}

	public void setLastUpdateId(Long lastUpdateId) {
		this.lastUpdateId = lastUpdateId;
	}

	@Column(name = "LAST_UPDATE_TIME")
	public Date getLastUpdateTime() {
		return lastUpdateTime;
	}

	public void setLastUpdateTime(Date lastUpdateTime) {
		this.lastUpdateTime = lastUpdateTime;
	}
	
	@Version
	@Column(name = "VERSION")
	public Date getVersion() {
		return version;
	}

	public void setVersion(Date version) {
		this.version = version;
	}

}
