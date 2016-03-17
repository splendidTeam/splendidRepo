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

import java.util.ArrayList;
import java.util.List;

import loxia.annotation.Column;

import com.baozun.nebula.command.Command;

/**
 * @author - 项硕
 */
public class SettingNormalCommand implements Command {

	private static final long serialVersionUID = 5512536638402756913L;

	/** PK */
	private Long id;
	
	/** 促销id */
	private Long promotionId;

	private String settingName;

	private String settingExpression;
	
	private String conditionType;
	
	/** 备用字段: 1.有效 0.无效 */
	private Integer activeMark = 1;
	
	private List<SimpleExpressionCommand> expressionList = new ArrayList<SimpleExpressionCommand>();
	
	/** 阶梯范围类型 */
	private String stepScopeType;

	/** 阶梯范围*/
	private String stepScope;

	@Column("SETTING_ID")
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

	public String getConditionType() {
		return conditionType;
	}

	public void setConditionType(String conditionType) {
		this.conditionType = conditionType;
	}

	@Column("ACTIVE_MARK")
	public Integer getActiveMark() {
		return activeMark;
	}

	public void setActiveMark(Integer activeMark) {
		this.activeMark = activeMark;
	}

	public List<SimpleExpressionCommand> getExpressionList() {
		return expressionList;
	}

	public void setExpressionList(List<SimpleExpressionCommand> expressionList) {
		this.expressionList = expressionList;
	}

	public String getStepScopeType() {
		return stepScopeType;
	}

	public void setStepScopeType(String stepScopeType) {
		this.stepScopeType = stepScopeType;
	}

	public String getStepScope() {
		return stepScope;
	}

	public void setStepScope(String stepScope) {
		this.stepScope = stepScope;
	}

}
