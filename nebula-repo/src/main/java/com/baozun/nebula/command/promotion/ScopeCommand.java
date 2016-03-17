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
public class ScopeCommand implements Command {

	private static final long serialVersionUID = -8137162286452411794L;

	/** PK */
	private Long id;

	/** 促销 */
	private Long promotionId;

	/** 商品组合编号 */
	private Long comboId;

	/** 商品范围类型：1商品，2目录，3自定义，4组合 */
	private Integer comboType;

	/** 商品组合名称 */
	private String comboName;

	/** 商品组合名称 */
	private String comboExpression;

	/** 备用字段: 1.有效 0.无效 */
	private Integer activeMark = 1;

	@Column("SCOPE_ID")
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

	@Column("PRODUCT_COMBO_ID")
	public Long getComboId() {
		return comboId;
	}

	public void setComboId(Long comboId) {
		this.comboId = comboId;
	}

	@Column("PRODUCT_COMBO_TYPE")
	public Integer getComboType() {
		return comboType;
	}

	public void setComboType(Integer comboType) {
		this.comboType = comboType;
	}

	@Column("PRODUCT_COMBO_NAME")
	public String getComboName() {
		return comboName;
	}

	public void setComboName(String comboName) {
		this.comboName = comboName;
	}

	@Column("PRODUCT_COMBO_EXPRESSION")
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
