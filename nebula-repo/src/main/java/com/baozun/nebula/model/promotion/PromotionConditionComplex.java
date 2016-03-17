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
 * Step4存放复合促销条件，存放阶梯和附件。阶梯一档一笔记录；选购主商品表达式一条记录，一个选购商品一条件记录
 * 
 * @author - 项硕
 */
@Entity
@Table(name = "t_prm_promotioncondition_complex")
@org.hibernate.annotations.Entity(optimisticLock = OptimisticLockType.VERSION)
public class PromotionConditionComplex extends BaseModel {

	private static final long serialVersionUID = 6160934139326747759L;
	
	/** 主商品类型 */
	public static final String CHOICE_MAIN = "prmprd";
	/** 选购商品类型 */
	public static final String CHOICE_DEPUTY = "addtprd";

	/** 
	 * PK
	 * 阶梯档次，一档一条记录，和normal条件表关联；附件，主商品条件表达式，选购商品表达式 
	 */
	private Long id;

	/** 常规条件ID */
	private Long normalConditionId;

	/** 促销ID */
	private Long promotionId;

	/** 
	 * 条件类型:
	 * step阶梯
	 * choice附件
	 */
	private String complexType;
	
	/** 阶梯式，起到比较循序的作用 */
	private Integer stepPriority;
	
	/** prmprd是主商品，addtprd不是主商品。主商品表达式一条记录，一个选购商品一条件记录 */
	private String choiceMark;
	
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
	@SequenceGenerator(name = "SEQ_T_PRM_PROMOTIONCONDITION_COMPLEX",sequenceName = "S_T_PRM_PROMOTIONCONDITION_COMPLEX",allocationSize = 1)
	@GeneratedValue(strategy = GenerationType.SEQUENCE,generator = "SEQ_T_PRM_PROMOTIONCONDITION_COMPLEX")
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

	@Column(name = "NORMAL_CONDITION_ID")
	public Long getNormalConditionId() {
		return normalConditionId;
	}

	public void setNormalConditionId(Long normalConditionId) {
		this.normalConditionId = normalConditionId;
	}

	@Column(name = "COMPLEX_TYPE", length = 10)
	public String getComplexType() {
		return complexType;
	}

	public void setComplexType(String complexType) {
		this.complexType = complexType;
	}

	@Column(name = "STEP_PRIORITY")
	public Integer getStepPriority() {
		return stepPriority;
	}

	public void setStepPriority(Integer stepPriority) {
		this.stepPriority = stepPriority;
	}

	@Column(name = "CHOICE_MARK", length = 10)
	public String getChoiceMark() {
		return choiceMark;
	}

	public void setChoiceMark(String choiceMark) {
		this.choiceMark = choiceMark;
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
