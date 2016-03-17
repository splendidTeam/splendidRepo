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

package com.baozun.nebula.command.promotion;

import java.util.Date;
import java.util.List;

import javax.persistence.Column;
import javax.persistence.Id;

import com.baozun.nebula.model.BaseModel;

/**
 * 活动调整详情
 */
public class PromotionPriorityAdjustDetailCommand extends BaseModel {

	private static final long serialVersionUID = 4302130254425614838L;

	/** PK */
	private Long id;

	/** 优先级表id */
	private Long adjustId;

	/** 促销 id */
	private Long promotionId;

	private String promotionName;
	private Date promotionStartTime;
	private Date promotionEndTime;
	private String audienceName;
	private String scopeName;
	private Integer promotionLifecycle;
	private Long logoType;
	private Date promotionUpdateTime;
	private String promotionUpdateName;

	/** 优先级 */
	private Integer priority;

	/** 1参与，0不参与计算，针对活动 */
	private Integer effectMark;
	
	/** 分组名称 */
	private String				groupName;
	
	/** 优先级分组0：N选1排他逻辑，1：共享排他逻辑 */
	private Integer groupType = 0; 
	
	/** 分组内的列表 */
	private List<PromotionPriorityAdjustDetailCommand>  promotionPriorityAdjustDetailCommandList;
	
	/** 排他标识 */
	private Integer exclusiveMark;

	/** version. */
	private Date version;

	@Id
	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	@Column(name = "ADJUSTID")
	public Long getAdjustId() {
		return adjustId;
	}

	public void setAdjustId(Long adjustId) {
		this.adjustId = adjustId;
	}

	@Column(name = "PROMOTIONID")
	public Long getPromotionId() {
		return promotionId;
	}

	public void setPromotionId(Long promotionId) {
		this.promotionId = promotionId;
	}

	public String getPromotionName() {
		return promotionName;
	}

	public void setPromotionName(String promotionName) {
		this.promotionName = promotionName;
	}

	public Date getPromotionStartTime() {
		return promotionStartTime;
	}

	public void setPromotionStartTime(Date promotionStartTime) {
		this.promotionStartTime = promotionStartTime;
	}

	public Date getPromotionEndTime() {
		return promotionEndTime;
	}

	public void setPromotionEndTime(Date promotionEndTime) {
		this.promotionEndTime = promotionEndTime;
	}

	public String getAudienceName() {
		return audienceName;
	}

	public void setAudienceName(String audienceName) {
		this.audienceName = audienceName;
	}

	public String getScopeName() {
		return scopeName;
	}

	public void setScopeName(String scopeName) {
		this.scopeName = scopeName;
	}

	public Integer getPromotionLifecycle() {
		return promotionLifecycle;
	}

	public void setPromotionLifecycle(Integer promotionLifecycle) {
		this.promotionLifecycle = promotionLifecycle;
	}

	public Long getLogoType() {
		return logoType;
	}

	public void setLogoType(Long logoType) {
		this.logoType = logoType;
	}

	public Date getPromotionUpdateTime() {
		return promotionUpdateTime;
	}

	public void setPromotionUpdateTime(Date promotionUpdateTime) {
		this.promotionUpdateTime = promotionUpdateTime;
	}

	public String getPromotionUpdateName() {
		return promotionUpdateName;
	}

	public void setPromotionUpdateName(String promotionUpdateName) {
		this.promotionUpdateName = promotionUpdateName;
	}

	@Column(name = "PRIORITY")
	public Integer getPriority() {
		return priority;
	}

	public void setPriority(Integer priority) {
		this.priority = priority;
	}

	@Column(name = "EFFECTMARK")
	public Integer getEffectMark() {
		return effectMark;
	}

	public void setEffectMark(Integer effectMark) {
		this.effectMark = effectMark;
	}

	@Column(name = "VERSION")
	public Date getVersion() {
		return version;
	}

	public void setVersion(Date version) {
		this.version = version;
	}

	public String getGroupName() {
		return groupName;
	}

	public void setGroupName(String groupName) {
		this.groupName = groupName;
	}

	public Integer getExclusiveMark() {
		return exclusiveMark;
	}

	public void setExclusiveMark(Integer exclusivemark) {
		this.exclusiveMark = exclusivemark;
	}

	public List<PromotionPriorityAdjustDetailCommand> getPromotionPriorityAdjustDetailCommandList() {
		return promotionPriorityAdjustDetailCommandList;
	}

	public void setPromotionPriorityAdjustDetailCommandList(
			List<PromotionPriorityAdjustDetailCommand> promotionPriorityAdjustDetailCommandList) {
		this.promotionPriorityAdjustDetailCommandList = promotionPriorityAdjustDetailCommandList;
	}

	public Integer getGroupType() {
		return groupType;
	}

	public void setGroupType(Integer groupType) {
		this.groupType = groupType;
	}
	
	
	
}
