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

import com.baozun.nebula.command.Command;

/**
 * @author - 项硕
 */
public class PriorityCommand implements Command {
	
	private static final long serialVersionUID = 7637852502039166824L;

	/** PK */
	private Long id;

	/** 渠道编号：受益人群（渠道） */
	private Long chanelId;

	/** 渠道名称：受益人群（渠道） */
	private String chanelName;

	/** 促销活动id */
	private Long promotionId;

	/** 促销活动名称 */
	private String promotionName;
	
	/** 促销开始时间 */
	private Date startTime;

	/** 促销结束时间 */
	private Date endTime;

	/** 优先级 */
	private Integer priority;

	/** 生效标志：1参与，0不参与计算 */
	private Integer effectMark;

	/** 修改人员id */
	private Long lastUpdateId;

	/** 修改时间 */
	private Date lastUpdateTime;
	
	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public Long getChanelId() {
		return chanelId;
	}

	public void setChanelId(Long chanelId) {
		this.chanelId = chanelId;
	}

	public String getChanelName() {
		return chanelName;
	}

	public void setChanelName(String chanelName) {
		this.chanelName = chanelName;
	}

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

	public Date getStartTime() {
		return startTime;
	}

	public void setStartTime(Date startTime) {
		this.startTime = startTime;
	}

	public Date getEndTime() {
		return endTime;
	}

	public void setEndTime(Date endTime) {
		this.endTime = endTime;
	}

	public Integer getPriority() {
		return priority;
	}

	public void setPriority(Integer priority) {
		this.priority = priority;
	}

	public Integer getEffectMark() {
		return effectMark;
	}

	public void setEffectMark(Integer effectMark) {
		this.effectMark = effectMark;
	}

	public Long getLastUpdateId() {
		return lastUpdateId;
	}

	public void setLastUpdateId(Long lastUpdateId) {
		this.lastUpdateId = lastUpdateId;
	}

	public Date getLastUpdateTime() {
		return lastUpdateTime;
	}

	public void setLastUpdateTime(Date lastUpdateTime) {
		this.lastUpdateTime = lastUpdateTime;
	}
}
