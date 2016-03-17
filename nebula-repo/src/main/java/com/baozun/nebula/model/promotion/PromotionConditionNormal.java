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
@Table(name = "t_prm_promotioncondition_normal")
@org.hibernate.annotations.Entity(optimisticLock = OptimisticLockType.VERSION)
public class PromotionConditionNormal extends BaseModel {

	private static final long serialVersionUID = 80974515411990138L;
	/*
	public static final String TYPE_NORMAL = "Normal";
	public static final String TYPE_STEP = "Step";
	public static final String TYPE_CHOICE = "Choice";
	public static final String TYPE_NORMAL_STEP = "NormalStep";
	public static final String TYPE_NORMAL_CHOICE = "NormalChoice";
	
	public static final String[] CONDITION_EXPRESSION_ARRAY = 
			new String[]{"nolmt","ordamt","ordpcs","scpordamt","scpordpcs","scpprdamt","scpprdpcs","ordcoupon","scpcoupon"};
	*/
	/** PK */
	private Long id;

	/** 促销 */
	private Long promotionId;

	/** 条件类型:
	 *  normal常规
	 *  step阶梯
	 *  choice选购
	 *  normalstep常规加阶梯
	 *  normalchoice常规加选购
	 */
	private String conditionType;

	/** 促销条件表达式名称 */
	private String conditionName;

	/** 促销条件表达式名称  */
	private String conditionExpress;

	/** 备用字段: 1.有效 0.无效 */
	private Integer activeMark = 1;
	
	/** version. */
	private Date version;

	@Id
	@Column(name = "CONDITION_ID")
	@SequenceGenerator(name = "SEQ_T_PRM_PROMOTIONCONDITION_NORMAL",sequenceName = "S_T_PRM_PROMOTIONCONDITION_NORMAL",allocationSize = 1)
	@GeneratedValue(strategy = GenerationType.SEQUENCE,generator = "SEQ_T_PRM_PROMOTIONCONDITION_NORMAL")
	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	@Column(name = "PROMOTION_ID")
	public Long getPromotionId() {
		return promotionId;
	}

	public void setPromotionId(Long promotionId) {
		this.promotionId = promotionId;
	}

	@Column(name = "CONDITION_TYPE", length = 20)
	public String getConditionType() {
		return conditionType;
	}

	public void setConditionType(String conditionType) {
		this.conditionType = conditionType;
	}

	@Column(name = "CONDITION_NAME", length = 2000)
	public String getConditionName() {
		return conditionName;
	}

	public void setConditionName(String conditionName) {
		this.conditionName = conditionName;
	}

	@Column(name = "CONDITION_EXPRESS", length = 2000)
	public String getConditionExpress() {
		return conditionExpress;
	}

	public void setConditionExpress(String conditionExpress) {
		this.conditionExpress = conditionExpress;
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
