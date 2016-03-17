/**
 * Copyright (c) 2010 Jumbomart All Rights Reserved.
 *
 * This software is the confidential and proprietary information of Jumbomart.
 * You shall not disclose such Confidential Information and shall use it only in
 * accordance with the terms of the license agreement you entered into
 * with Jumbo.
 *
 * JUMBOMART MAKES NO REPRESENTATIONS OR WARRANTIES ABOUT THE SUITABILITY OF THE
 * SOFTWARE, EITHER EXPRESS OR IMPLIED, INCLUDING BUT NOT LIMITED TO THE
 * IMPLIED WARRANTIES OF MERCHANTABILITY, FITNESS FOR A PARTICULAR
 * PURPOSE, OR NON-INFRINGEMENT. JUMBOMART SHALL NOT BE LIABLE FOR ANY DAMAGES
 * SUFFERED BY LICENSEE AS A RESULT OF USING, MODIFYING OR DISTRIBUTING
 * THIS SOFTWARE OR ITS DERIVATIVES.
 *
 */
package com.baozun.nebula.command.product;

import java.util.Date;

import com.baozun.nebula.command.Command;

/**
 * 
 * @Description: 用于商品的默认排序规则分数 按照分数从大往小排
 * @author 何波
 * @date 2014年8月14日 上午9:26:33
 * 
 */
public class ItemSortScoreCommand implements Command {

	/**
	 * 
	 */
	private static final long serialVersionUID = -5118521165204106838L;

	/** PK. */
	private Long id;

	/** 类型 */
	private Integer type;

	/**
	 * 分类id
	 */
	private Long categoryId;

	private String categoryName;
	/**
	 * 属性id
	 */
	private Long propertyId;

	private String propertyName;

	/** 运算符. */
	private String operator;
	/** 运算符. */
	private String operatorText;

	/** 参数. */
	private String param;

	/** 权值 */
	private Integer score;

	// ************************************************************************************

	/** 生命周期. */
	private Integer lifecycle;

	/** 创建时间. */
	private Date createTime = new Date();

	/**
	 * Gets the pK.
	 * 
	 * @return the pK
	 */
	public Long getId() {
		return id;
	}

	/**
	 * Sets the pK.
	 * 
	 * @param id
	 *            the new pK
	 */
	public void setId(Long id) {
		this.id = id;
	}

	/**
	 * Sets the 创建时间.
	 * 
	 * @param createTime
	 *            the createTime to set
	 */
	public void setCreateTime(Date createTime) {
		this.createTime = createTime;
	}

	/**
	 * Gets the 创建时间.
	 * 
	 * @return the createTime
	 */
	public Date getCreateTime() {
		return createTime;
	}

	public Integer getType() {
		return type;
	}

	public void setType(Integer type) {
		this.type = type;
	}

	public String getOperator() {
		return operator;
	}

	public void setOperator(String operator) {
		this.operator = operator;
	}

	public String getOperatorText() {
		return operatorText;
	}

	public void setOperatorText(String operatorText) {
		this.operatorText = operatorText;
	}

	public String getParam() {
		return param;
	}

	public void setParam(String param) {
		this.param = param;
	}

	/**
	 * Gets the 生命周期.
	 * 
	 * @return the 生命周期
	 */
	public Integer getLifecycle() {
		return lifecycle;
	}

	/**
	 * Sets the 生命周期.
	 * 
	 * @param lifecycle
	 *            the new 生命周期
	 */
	public void setLifecycle(Integer lifecycle) {
		this.lifecycle = lifecycle;
	}

	/**
	 * @return the score
	 */
	public Integer getScore() {
		return score;
	}

	/**
	 * @param score
	 *            the score to set
	 */
	public void setScore(Integer score) {
		this.score = score;
	}

	public Long getCategoryId() {
		return categoryId;
	}

	public void setCategoryId(Long categoryId) {
		this.categoryId = categoryId;
	}

	public Long getPropertyId() {
		return propertyId;
	}

	public void setPropertyId(Long propertyId) {
		this.propertyId = propertyId;
	}


	public String getCategoryName() {
		return categoryName;
	}

	public void setCategoryName(String categoryName) {
		this.categoryName = categoryName;
	}

	public String getPropertyName() {
		return propertyName;
	}

	public void setPropertyName(String propertyName) {
		this.propertyName = propertyName;
	}
	
	
}
