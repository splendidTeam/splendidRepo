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
package com.baozun.nebula.command.product;

import java.util.Date;

import com.baozun.nebula.command.Command;
import com.baozun.nebula.model.product.SkuInventoryChangeLog;

/**   
 * @Description 
 * @author dongliang ma
 * @date 2016年6月3日 下午3:22:52 
 * @version   
 */
public class SkuInventoryChangeLogCommand implements Command {
	
	/** */
	private static final long serialVersionUID = -8971263741414949450L;
	
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

	/** 以下为冗余字段*/
	private String 	itemCode;
	
	private String 	itemTitle;
	
	private Integer lifecycle;
	
	private String operatorLabel;

	/**
	 * @return the id
	 */
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
	public Date getCreateTime() {
		return createTime;
	}

	/**
	 * @param createTime the createTime to set
	 */
	public void setCreateTime(Date createTime) {
		this.createTime = createTime;
	}

	/**
	 * @return the itemCode
	 */
	public String getItemCode() {
		return itemCode;
	}

	/**
	 * @param itemCode the itemCode to set
	 */
	public void setItemCode(String itemCode) {
		this.itemCode = itemCode;
	}

	/**
	 * @return the itemTitle
	 */
	public String getItemTitle() {
		return itemTitle;
	}

	/**
	 * @param itemTitle the itemTitle to set
	 */
	public void setItemTitle(String itemTitle) {
		this.itemTitle = itemTitle;
	}

	/**
	 * @return the lifecycle
	 */
	public Integer getLifecycle() {
		return lifecycle;
	}

	/**
	 * @param lifecycle the lifecycle to set
	 */
	public void setLifecycle(Integer lifecycle) {
		this.lifecycle = lifecycle;
	}

	/**
	 * @return the operatorLabel
	 */
	public String getOperatorLabel() {
		return operatorLabel;
	}

	/**
	 * @param operatorLabel the operatorLabel to set
	 */
	public void setOperatorLabel(String operatorLabel) {
		this.operatorLabel = operatorLabel;
	}
	
	
	

}
