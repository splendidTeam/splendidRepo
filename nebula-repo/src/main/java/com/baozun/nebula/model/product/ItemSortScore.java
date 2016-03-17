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
package com.baozun.nebula.model.product;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;
import javax.persistence.Version;

import org.hibernate.annotations.Index;
import org.hibernate.annotations.OptimisticLockType;

import com.baozun.nebula.model.BaseModel;

/**
 * 用于商品的默认排序规则分数
 * 按照分数从大往小排
 * 
 * @author justin.hu
 */
@Entity
@Table(name = "T_PD_ITEM_SORT_SCORE")
@org.hibernate.annotations.Entity(optimisticLock = OptimisticLockType.VERSION)
public class ItemSortScore extends BaseModel{

	/**
	 * 
	 */
	private static final long serialVersionUID = -5118521165204106838L;

	/** PK. */
	private Long				id;

	/** 类型 */
	private Integer				type;
	
	/**
	 * 分类id
	 */
	private Long 				categoryId;
	
	/**
	 * 属性id
	 */
	private Long				propertyId;

	/** 运算符. */
	private String				operator;

	/** 参数.如销量大于50给3个权重,这里就填写50 
	 *  type=分类时，用不上这个参数
	 *  type=属性时，param设置为具体属性的值，如颜色 = 红色  给 5 分 propertyId填写颜色的属性id,operator为等于,param为红色 score为5
	 *  operator,出现大于小于的时候 parmas的值必须能转化为double类型
	 * */
	private String				param;
	
	/**权值*/
	private Integer             score;

	// ************************************************************************************

	/** 生命周期. */
	private Integer				lifecycle;

	/** 创建时间. */
	private Date				createTime			= new Date();

	
	/** version. */
	private Date				version				= new Date();
	

	/**
	 * Gets the pK.
	 * 
	 * @return the pK
	 */
	@Id
	@Column(name = "ID")
	@SequenceGenerator(name = "SEQ_T_PD_ITEM_SORT_SCORE",sequenceName = "S_T_PD_ITEM_SORT_SCORE",allocationSize = 1)
	@GeneratedValue(strategy = GenerationType.SEQUENCE,generator = "SEQ_T_PD_ITEM_SORT_SCORE")
	public Long getId(){
		return id;
	}

	/**
	 * Sets the pK.
	 * 
	 * @param id
	 *            the new pK
	 */
	public void setId(Long id){
		this.id = id;
	}


	/**
	 * Sets the 创建时间.
	 * 
	 * @param createTime
	 *            the createTime to set
	 */
	public void setCreateTime(Date createTime){
		this.createTime = createTime;
	}

	
	/**
	 * Gets the 创建时间.
	 * 
	 * @return the createTime
	 */
	@Column(name = "CREATE_TIME")
	public Date getCreateTime(){
		return createTime;
	}

	
	@Column(name = "TYPE")
	@Index(name = "FINX_PD_ITEM_SORT_SCORE_TYPE")
	public Integer getType() {
		return type;
	}

	public void setType(Integer type) {
		this.type = type;
	}

	@Column(name = "OPERATOR")
	public String getOperator() {
		return operator;
	}

	public void setOperator(String operator) {
		this.operator = operator;
	}

	@Column(name = "PARAM")
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
	@Column(name = "LIFECYCLE")
	public Integer getLifecycle(){
		return lifecycle;
	}

	/**
	 * Sets the 生命周期.
	 * 
	 * @param lifecycle
	 *            the new 生命周期
	 */
	public void setLifecycle(Integer lifecycle){
		this.lifecycle = lifecycle;
	}
	
	/**
	 * @return the score
	 */
	@Column(name = "SCORE")
	public Integer getScore() {
		return score;
	}

	/**
	 * @param score the score to set
	 */
	public void setScore(Integer score) {
		this.score = score;
	}

	@Column(name = "CATEGORY_ID")
	public Long getCategoryId() {
		return categoryId;
	}

	public void setCategoryId(Long categoryId) {
		this.categoryId = categoryId;
	}

	@Column(name = "PROPERTY_ID")
	public Long getPropertyId() {
		return propertyId;
	}

	public void setPropertyId(Long propertyId) {
		this.propertyId = propertyId;
	}

	/**
	 * Gets the version.
	 * 
	 * @return the version
	 */
	@Version
	@Column(name = "VERSION")
	public Date getVersion(){
		return version;
	}

	/**
	 * Sets the version.
	 * 
	 * @param version
	 *            the new version
	 */
	public void setVersion(Date version){
		this.version = version;
	}
}
