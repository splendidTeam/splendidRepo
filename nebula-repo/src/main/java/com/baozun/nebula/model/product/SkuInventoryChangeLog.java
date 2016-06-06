/**
 * Copyright (c) 2015 Baozun All Rights Reserved.
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
package com.baozun.nebula.model.product;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;

import com.baozun.nebula.model.BaseModel;

/**   
 * @Description 
 * @author dongliang ma
 * @date 2016年6月3日 下午3:59:10 
 * @version   
 */
@Entity
@Table(name = "t_pd_sku_inventory_change_log")
public class SkuInventoryChangeLog extends BaseModel {
	
	private static final long serialVersionUID = -6021721136859477051L;
	
	/** 类型 ： 1. 全量*/
	public static final Integer TYPE_FULL			=1;
	/** 类型 ： 1. 增*/
	public static final Integer TYPE_INCREASE		=2;
	/** 类型 ： 1. 减*/
	public static final Integer TYPE_REDUCE			=3;
	/** 来源 ： 1. 前端*/
	public static final Integer SOURCE_FRONTEND		=1;
	/** 来源 ： 1. pts*/
	public static final Integer	SOURCE_PTS			=2;
	/** 来源 ： 1. 定时任务*/
	public static final Integer	SOURCE_TIMING		=3;
	/** 来源 ： 1. oms*/
	public static final Integer	SOURCE_OMS			=4;
	
	
	/** PK. */
	private Long id;
	
	/** 和oms 沟通交互的 唯一编码,extension*/
	private String extentionCode;
	
	/** 数据变化量*/
	private Integer qty;
	
	/** 
	 * 变化类型:
	 * 	1.全量
	 * 	2.增
	 * 	3.减
	 * */
	private Integer type;
	
	/** 
	 * 变化来源：
	 * 	1.前端
	 * 	2.pts
	 * 	3.定时任务
	 * 	4.oms
	 * */
	private Integer source;
	
	/** 操作者*/
	private Long operator;
	
	/** 创建时间*/
	private Date createTime;

	/**
	 * @return the id
	 */
	@Id
	@Column(name = "ID")
	@SequenceGenerator(name = "SEQ_T_PD_SKU_INVENTORY_CHANGE_LOG", sequenceName = "SEQ_T_PD_SKU_INVENTORY_CHANGE_LOG", allocationSize = 1)
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "SEQ_T_PD_SKU_INVENTORY_CHANGE_LOG")
	public Long getId() {
		return id;
	}

	/**
	 * @param id the id to set
	 */
	public void setId(Long id) {
		this.id = id;
	}

	/**
	 * @return the extentionCode
	 */
	@Column(name = "EXTENTION_CODE")
	public String getExtentionCode() {
		return extentionCode;
	}

	/**
	 * @param extentionCode the extentionCode to set
	 */
	public void setExtentionCode(String extentionCode) {
		this.extentionCode = extentionCode;
	}

	/**
	 * @return the qty
	 */
	@Column(name = "QTY")
	public Integer getQty() {
		return qty;
	}

	/**
	 * @param qty the qty to set
	 */
	public void setQty(Integer qty) {
		this.qty = qty;
	}

	/**
	 * @return the type
	 */
	@Column(name = "TYPE")
	public Integer getType() {
		return type;
	}

	/**
	 * @param type the type to set
	 */
	public void setType(Integer type) {
		this.type = type;
	}

	/**
	 * @return the source
	 */
	@Column(name = "SOURCE")
	public Integer getSource() {
		return source;
	}

	/**
	 * @param source the source to set
	 */
	public void setSource(Integer source) {
		this.source = source;
	}

	/**
	 * @return the operator
	 */
	@Column(name = "OPERATOR")
	public Long getOperator() {
		return operator;
	}

	/**
	 * @param operator the operator to set
	 */
	public void setOperator(Long operator) {
		this.operator = operator;
	}

	/**
	 * @return the createTime
	 */
	@Column(name = "CREATE_TIME")
	public Date getCreateTime() {
		return createTime;
	}

	/**
	 * @param createTime the createTime to set
	 */
	public void setCreateTime(Date createTime) {
		this.createTime = createTime;
	}
	
	
	
}
