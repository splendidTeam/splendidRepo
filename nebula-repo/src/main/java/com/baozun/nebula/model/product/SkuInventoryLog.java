/**
 * Copyright (c) 2013 Baozun All Rights Reserved.
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
import javax.persistence.Version;

import org.hibernate.annotations.Index;
import org.hibernate.annotations.OptimisticLockType;

import com.baozun.nebula.model.BaseModel;

/**
 * sku库存表 修改记录
 * 
 * @author Tianlong.Zhang
 * 
 */
@Entity
@Table(name = "T_PD_SKU_INVENTORY_LOG")
public class SkuInventoryLog extends BaseModel {

	private static final long serialVersionUID = 2712146398430443523L;
	
	/**
	 * pts库存管理
	 */
	public static final Integer TYPE_PTS=1;
	
	/**
	 * OMS增量同步
	 */
	public static final Integer TYPE_OMS_INCREMENT=2;

	/** PK. */
	private Long id;

	/** 修改为qty的库存数量. */
	private Integer qty;

	/** 和oms 沟通交互的 唯一编码,extension1. */
	private String extentionCode;

	/** 更新时间. */
	private Date modifyTime;
	
	/**
	 * 类型:
	 * 1.库存管理修改
	 * 2.OMS库存增量同步
	 */
	private Integer type;
	
	/**
	 * 修改者的用户id
	 * 在库存管理时,进行保存
	 */
	private Long userId;

	/**
	 * @return the id
	 */
	@Id
	@Column(name = "ID")
	@SequenceGenerator(name = "SEQ_T_PD_SKU_INVENTORY_LOG", sequenceName = "SEQ_T_PD_SKU_INVENTORY_LOG", allocationSize = 1)
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "SEQ_T_PD_SKU_INVENTORY_LOG")
	public Long getId() {
		return id;
	}

	/**
	 * @param id
	 *            the id to set
	 */
	public void setId(Long id) {
		this.id = id;
	}

	/**
	 * @return the availableQty
	 */
	@Column(name = "QTY")
	public Integer getQty() {
		return qty;
	}

	/**
	 * @param availableQty
	 *            the availableQty to set
	 */
	public void setQty(Integer qty) {
		this.qty = qty;
	}

	/**
	 * @return the extentionCode
	 */
	@Column(name="EXTENTION_CODE")
    @Index(name = "IDX_SKU_INVENTORY_LOG_EXTENTION_CODE")
	public String getExtentionCode() {
		return extentionCode;
	}

	/**
	 * @param extentionCode
	 *            the extentionCode to set
	 */
	public void setExtentionCode(String extentionCode) {
		this.extentionCode = extentionCode;
	}

	/**
	 * @return the lastSyncTime
	 */
	@Column(name="MODIFY_TIME")
	public Date getModifyTime() {
		return modifyTime;
	}

	/**
	 * @param lastSyncTime
	 *            the lastSyncTime to set
	 */
	public void setModifyTime(Date modifyTime) {
		this.modifyTime = modifyTime;
	}

	@Column(name="TYPE")
    @Index(name = "IDX_SKU_INVENTORY_LOG_TYPE")
	public Integer getType() {
		return type;
	}

	public void setType(Integer type) {
		this.type = type;
	}

	@Column(name="USER_ID")
    @Index(name = "IDX_SKU_INVENTORY_LOG_USER_ID")
	public Long getUserId() {
		return userId;
	}

	public void setUserId(Long userId) {
		this.userId = userId;
	}


}
