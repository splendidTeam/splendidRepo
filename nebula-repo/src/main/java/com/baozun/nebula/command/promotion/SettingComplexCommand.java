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

import com.baozun.nebula.command.Command;

/**
 * Step5复合优惠活动设置。一档一笔记录优惠；一个选购商品一条优惠记录
 * 
 * @author - 项硕
 */
public class SettingComplexCommand implements Command {

	private static final long serialVersionUID = 4462598026233209449L;

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
	
	/** 优惠数值 */
	private Integer stepNumber;
	
	/** 选购类型时的范围类型 */
	private String choiceScope;
	
	
	@Column("SETTING_ID")
	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	@Column("COMPLEX_CONDITION_ID")
	public Long getComplexConditionId() {
		return complexConditionId;
	}

	public void setComplexConditionId(Long complexConditionId) {
		this.complexConditionId = complexConditionId;
	}

	@Column("PROMOTION_ID")
	public Long getPromotionId() {
		return promotionId;
	}

	public void setPromotionId(Long promotionId) {
		this.promotionId = promotionId;
	}

	@Column("SETTING_NAME")
	public String getSettingName() {
		return settingName;
	}

	public void setSettingName(String settingName) {
		this.settingName = settingName;
	}

	@Column("SETTING_EXPRESSION")
	public String getSettingExpression() {
		return settingExpression;
	}

	public void setSettingExpression(String settingExpression) {
		this.settingExpression = settingExpression;
	}

	@Column("ACTIVE_MARK")
	public Integer getActiveMark() {
		return activeMark;
	}

	public void setActiveMark(Integer activeMark) {
		this.activeMark = activeMark;
	}

	public Integer getStepNumber() {
		return stepNumber;
	}

	public void setStepNumber(Integer stepNumber) {
		this.stepNumber = stepNumber;
	}

	public String getChoiceScope() {
		return choiceScope;
	}

	public void setChoiceScope(String choiceScope) {
		this.choiceScope = choiceScope;
	}
	
}
