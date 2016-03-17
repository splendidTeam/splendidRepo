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
 * Step5复合优惠活动设置。一档一笔记录优惠；一个选购商品一条优惠记录
 * 
 * @author - 项硕
 */
@Entity
@Table(name = "t_prm_promotionsetting_complex")
@org.hibernate.annotations.Entity(optimisticLock = OptimisticLockType.VERSION)
public class PromotionSettingComplex extends BaseModel {

	private static final long serialVersionUID = 1730547695114049941L;

	/** PK */
	private Long id;
	
	/** 优惠对应的条件。阶梯一档条件对应一个优惠设置；选购：一个选购商品条件对应一条优惠设置记录 */
	private Long complexConditionId;
	
	/** 促销id */
	private Long promotionId;

	/** 促销id */
	private String settingName;

	/** 促销id */
	private String settingExpression;
	
	/** 备用字段: 1.有效 0.无效 */
	private Integer activeMark = 1;
	
	/** version. */
	private Date version;

	@Id
	@Column(name = "SETTING_ID")
	@SequenceGenerator(name = "SEQ_T_PRM_PROMOTIONSETTING_COMPLEX",sequenceName = "S_T_PRM_PROMOTIONSETTING_COMPLEX",allocationSize = 1)
	@GeneratedValue(strategy = GenerationType.SEQUENCE,generator = "SEQ_T_PRM_PROMOTIONSETTING_COMPLEX")
	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	@Column(name = "COMPLEX_CONDITION_ID")
	public Long getComplexConditionId() {
		return complexConditionId;
	}

	public void setComplexConditionId(Long complexConditionId) {
		this.complexConditionId = complexConditionId;
	}

	@Column(name = "PROMOTION_ID")
	public Long getPromotionId() {
		return promotionId;
	}

	public void setPromotionId(Long promotionId) {
		this.promotionId = promotionId;
	}

	@Column(name = "SETTING_NAME", length = 2000)
	public String getSettingName() {
		return settingName;
	}

	public void setSettingName(String settingName) {
		this.settingName = settingName;
	}

	@Column(name = "SETTING_EXPRESSION", length = 2000)
	public String getSettingExpression() {
		return settingExpression;
	}

	public void setSettingExpression(String settingExpression) {
		this.settingExpression = settingExpression;
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
}
