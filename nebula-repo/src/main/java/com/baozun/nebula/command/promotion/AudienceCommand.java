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
 * @author - 项硕
 */
public class AudienceCommand implements Command {

	private static final long serialVersionUID = -3014162074383963158L;

	/** PK */
	private Long id;

	/** 促销id */
	private Long promotionId;

	/** t_mem_custommembergroup组合表关联，组合编号 */
	private Long comboId;

	/** 会员组合类型：1会员列表，2会员分组，3自定义,4组合 */
	private Integer comboType;

	/** 人群组合表达式对应的名称 */
	private String comboName;

	/** 人群组合表达式对应的名称 */
	private String comboExpression;

	/** 备用字段: 1.有效 0.无效 */
	private Integer activeMark = 1;

	@Column("AUDIENCES_ID")
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

	@Column("MEM_COMBO_ID")
	public Long getComboId() {
		return comboId;
	}

	public void setComboId(Long comboId) {
		this.comboId = comboId;
	}

	@Column("MEM_COMBO_TYPE")
	public Integer getComboType() {
		return comboType;
	}

	public void setComboType(Integer comboType) {
		this.comboType = comboType;
	}

	@Column("MEM_COMBO_NAME")
	public String getComboName() {
		return comboName;
	}

	public void setComboName(String comboName) {
		this.comboName = comboName;
	}

	@Column("MEM_COMBO_EXPRESSION")
	public String getComboExpression() {
		return comboExpression;
	}

	public void setComboExpression(String comboExpression) {
		this.comboExpression = comboExpression;
	}

	@Column("ACTIVE_MARK")
	public Integer getActiveMark() {
		return activeMark;
	}

	public void setActiveMark(Integer activeMark) {
		this.activeMark = activeMark;
	}

}
