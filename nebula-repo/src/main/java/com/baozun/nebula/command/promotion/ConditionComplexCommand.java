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

import loxia.annotation.Column;

import com.baozun.nebula.model.BaseModel;

/**
 * Step4存放复合促销条件，存放阶梯和附件。阶梯一档一笔记录；选购主商品表达式一条记录，一个选购商品一条件记录
 * 
 * @author - 项硕
 */
public class ConditionComplexCommand extends BaseModel {

	private static final long serialVersionUID = -799921060541265356L;
	
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
	
	/** 阶梯条件项中的数值 */
	private String number;
	
	/** 选购条件项中的商品范围 */
	private String scope;


	@Column("CONDITION_ID")
	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	@Column("PROMOTION_ID")
	public Long getPromotionId() {
		return promotionId;
	}

	public void setPromotionId(Long promotionId) {
		this.promotionId = promotionId;
	}

	@Column("NORMAL_CONDITION_ID")
	public Long getNormalConditionId() {
		return normalConditionId;
	}

	public void setNormalConditionId(Long normalConditionId) {
		this.normalConditionId = normalConditionId;
	}

	@Column("COMPLEX_TYPE")
	public String getComplexType() {
		return complexType;
	}

	public void setComplexType(String complexType) {
		this.complexType = complexType;
	}

	@Column("STEP_PRIORITY")
	public Integer getStepPriority() {
		return stepPriority;
	}

	public void setStepPriority(Integer stepPriority) {
		this.stepPriority = stepPriority;
	}

	@Column("CHOICE_MARK")
	public String getChoiceMark() {
		return choiceMark;
	}

	public void setChoiceMark(String choiceMark) {
		this.choiceMark = choiceMark;
	}

	@Column("CONDITION_NAME")
	public String getConditionName() {
		return conditionName;
	}

	public void setConditionName(String conditionName) {
		this.conditionName = conditionName;
	}

	@Column("CONDITION_EXPRESS")
	public String getConditionExpress() {
		return conditionExpress;
	}

	public void setConditionExpress(String conditionExpress) {
		this.conditionExpress = conditionExpress;
	}

	@Column("ACTIVE_MARK")
	public Integer getActiveMark() {
		return activeMark;
	}

	public void setActiveMark(Integer activeMark) {
		this.activeMark = activeMark;
	}

	public String getNumber() {
		return number;
	}

	public void setNumber(String number) {
		this.number = number;
	}

	public String getScope() {
		return scope;
	}

	public void setScope(String scope) {
		this.scope = scope;
	}
}
