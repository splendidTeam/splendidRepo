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
 * 活动调整详情 
 */
@Entity
@Table(name = "t_prm_priorityadjustdetail")
@org.hibernate.annotations.Entity(optimisticLock = OptimisticLockType.VERSION)
public class PromotionPriorityAdjustDetail extends BaseModel {  
	
	private static final long serialVersionUID = -6644903836973740302L;
	
	/** 参与*/
	public static final Integer ACTIVEMARK_ENABLE = 1;
	/** 不参与计算*/
	public static final Integer ACTIVEMARK_DISABLE = 0; 

	/** 排他*/
	public static final Integer EXCLUSIVE_MARK_ENABLE = 1;
	/** 不排他*/
	public static final Integer EXCLUSIVE_MARK_DISABLE = 0; 
	
	

	/** PK */
	private Long id;

	/** 优先级表id */
	private Long adjustId;
	
	/** 促销 id*/
	private Long promotionId;

	/** 优先级 */
	private Integer priority; 

	/** 排他标识 */
	private Integer exclusiveMark = EXCLUSIVE_MARK_DISABLE;
 
	/** version. */
	private Date version; 
	
	@Id
	@Column(name = "ID")
	@SequenceGenerator(name = "SEQ_T_PRM_PRIORITYADJUSTDETAIL",sequenceName = "S_T_PRM_PRIORITYADJUSTDETAIL",allocationSize = 1)
	@GeneratedValue(strategy = GenerationType.SEQUENCE,generator = "SEQ_T_PRM_PRIORITYADJUSTDETAIL")
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
	@Column(name = "PRIORITY")
	public Integer getPriority() {
		return priority;
	}

	public void setPriority(Integer priority) {
		this.priority = priority;
	}
	
	@Version
	@Column(name = "VERSION")
	public Date getVersion() {
		return version;
	}

	public void setVersion(Date version) {
		this.version = version;
	}

	@Column(name = "exclusiveMark")
	public Integer getexclusiveMark() {
		return exclusiveMark;
	}

	public void setexclusiveMark(Integer exclusiveMark) {
		this.exclusiveMark = exclusiveMark;
	}
}