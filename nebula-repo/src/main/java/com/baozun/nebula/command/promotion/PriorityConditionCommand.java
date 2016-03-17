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

import java.math.BigDecimal;

import loxia.annotation.Column;

import com.baozun.nebula.command.Command;

/**
 * @author - 项硕
 */
public class PriorityConditionCommand implements Command {

	private static final long serialVersionUID = 6138801795219639106L;
	
	/** 促销id */
	private Long promotionId;
	
	/** 商品id */
	private Long itemId;
	
	/** 商品原价格 */
	private BigDecimal price;
	
	/** 促销类型 */
	private Integer type;

	/** 促销条件值 */
	private BigDecimal value;

	/** 促销优惠类型 */
	private Integer settingType;

	/** 促销优惠值 */
	private BigDecimal settingValue;
	
	@Column("PROMOTION_ID")
	public Long getPromotionId() {
		return promotionId;
	}

	public void setPromotionId(Long promotionId) {
		this.promotionId = promotionId;
	}

	@Column("ITEM_ID")
	public Long getItemId() {
		return itemId;
	}

	public void setItemId(Long itemId) {
		this.itemId = itemId;
	}

	@Column("SALE_PRICE")
	public BigDecimal getPrice() {
		return price;
	}

	public void setPrice(BigDecimal price) {
		this.price = price;
	}

	@Column("CONDITION_TYPE")
	public Integer getType() {
		return type;
	}

	public void setType(Integer type) {
		this.type = type;
	}

	@Column("CONDITION_VALUE")
	public BigDecimal getValue() {
		return value;
	}

	public void setValue(BigDecimal value) {
		this.value = value;
	}

	@Column("SETTING_TYPE")
	public Integer getSettingType() {
		return settingType;
	}

	public void setSettingType(Integer settingType) {
		this.settingType = settingType;
	}

	@Column("SETTING_VALUE")
	public BigDecimal getSettingValue() {
		return settingValue;
	}

	public void setSettingValue(BigDecimal settingValue) {
		this.settingValue = settingValue;
	}

}
