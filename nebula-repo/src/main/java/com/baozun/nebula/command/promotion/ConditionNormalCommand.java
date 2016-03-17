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
public class ConditionNormalCommand implements Command {

	private static final long serialVersionUID = -617447318495595450L;

	/** PK */
	private Long id;

	/** 促销 */
	private Long promotionId;
	
	/** 条件类型:
	 *  Normal常规
	 *  Step阶梯
	 *  Choice选购
	 *  NormalStep常规加阶梯
	 *  NormalChoice常规加选购
	 */
	private String conditionType;

	/** 促销条件表达式名称 */
	private String conditionName;

	/** 促销条件表达式名称 */
	private String conditionExpress;

	/** 备用字段: 1.有效 0.无效 */
	private Integer activeMark = 1;
	
	private List<SimpleExpressionCommand> expressionList = new ArrayList<SimpleExpressionCommand>();
	
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

	@Column("CONDITION_TYPE")
	public String getConditionType() {
		return conditionType;
	}

	public void setConditionType(String conditionType) {
		this.conditionType = conditionType;
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

	public List<SimpleExpressionCommand> getExpressionList() {
		return expressionList;
	}

	public void setExpressionList(List<SimpleExpressionCommand> expressionList) {
		this.expressionList = expressionList;
	}

}
