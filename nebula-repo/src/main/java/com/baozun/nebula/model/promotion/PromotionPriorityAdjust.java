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
 * 优先级调整
 */
@Entity
@Table(name = "t_prm_priorityadjust")
@org.hibernate.annotations.Entity(optimisticLock = OptimisticLockType.VERSION)
public class PromotionPriorityAdjust extends BaseModel {

	private static final long	serialVersionUID		= -5503719248940491677L;

	/** 生效标志：已启用 */
	public static final Integer	ACTIVEMARK_ENABLE		= 1;
	/** 生效标志：待启用 */
	public static final Integer	ACTIVEMARK_DISABLE		= 0;
	/** 生效标志：已禁用 */
	public static final Integer	ACTIVEMARK_FORBIDDEN	= 3;

	/** PK */
	private Long				id;

	/** 调整名称 */
	private String				adjustName;

	/** 开始时间 **/
	@Deprecated
	private Date				startTime;

	/** 结束时间 **/
	@Deprecated
	private Date				endTime;

	/** 生效标志：1生效，0不生效。针对调整 */
	private Integer				activeMark;

	/** * 所属店铺 */
	private Long				shopId;

	/** 修改人员id */
	private Long				lastUpdateId;

	/** 修改时间 */
	private Date				lastUpdateTime;

	private Date				version;

	@Id
	@Column(name = "ADJUSTID")
	@SequenceGenerator(name = "SEQ_T_PRM_PRIORITYADJUST", sequenceName = "S_T_PRM_PRIORITYADJUST", allocationSize = 1)
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "SEQ_T_PRM_PRIORITYADJUST")
	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	@Column(name = "ADJUSTNAME")
	public String getAdjustName() {
		return adjustName;
	}

	public void setAdjustName(String adjustName) {
		this.adjustName = adjustName;
	}

	@Column(name = "STARTTIME")
	public Date getStartTime() {
		return startTime;
	}

	public void setStartTime(Date startTime) {
		this.startTime = startTime;
	}

	@Column(name = "ENDTIME")
	public Date getEndTime() {
		return endTime;
	}

	public void setEndTime(Date endTime) {
		this.endTime = endTime;
	}

	@Column(name = "ACTIVEMARK")
	public Integer getActiveMark() {
		return activeMark;
	}

	public void setActiveMark(Integer activeMark) {
		this.activeMark = activeMark;
	}

	@Column(name = "SHOP_ID")
	public Long getShopId() {
		return shopId;
	}

	public void setShopId(Long shopId) {
		this.shopId = shopId;
	}

	@Column(name = "LASTUPDATEID")
	public Long getLastUpdateId() {
		return lastUpdateId;
	}

	public void setLastUpdateId(Long lastUpdateId) {
		this.lastUpdateId = lastUpdateId;
	}

	@Column(name = "LASTUPDATETIME")
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
