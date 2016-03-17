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

import com.baozun.nebula.command.Command;

/**
 * 
 * @Description: 常规和复杂类型合并
 * @author 何波
 * @date 2014年7月9日 上午10:51:15
 * 
 */
public class PromotionSettingQueryCommand implements Command {

	private static final long serialVersionUID = -5324765849945546635L;

	/** PK */
	private Long id;

	/** 促销id */
	private Long promotionId;

	/** 促销id */
	private String settingName;

	/** 促销id */
	private String settingExpression;

	/** 备用字段: 1.有效 0.无效 */
	private Integer activeMark = 1;

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public Long getPromotionId() {
		return promotionId;
	}

	public void setPromotionId(Long promotionId) {
		this.promotionId = promotionId;
	}

	public String getSettingName() {
		return settingName;
	}

	public void setSettingName(String settingName) {
		this.settingName = settingName;
	}

	public String getSettingExpression() {
		return settingExpression;
	}

	public void setSettingExpression(String settingExpression) {
		this.settingExpression = settingExpression;
	}

	public Integer getActiveMark() {
		return activeMark;
	}

	public void setActiveMark(Integer activeMark) {
		this.activeMark = activeMark;
	}

}
