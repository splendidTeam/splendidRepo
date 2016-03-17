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

import org.hibernate.annotations.OptimisticLockType;

import com.baozun.nebula.model.BaseModel;

/**
 * sku库存表
 * 
 * @author Tianlong.Zhang
 * 
 */
@Entity
@Table(name = "t_pd_sku_inventory")
@org.hibernate.annotations.Entity(optimisticLock = OptimisticLockType.VERSION)
public class SkuInventory extends BaseModel {

	private static final long serialVersionUID = 2712146398430443523L;

	/** PK. */
	private Long id;

	/** 商品可用量. */
	private Integer availableQty;

	/** 和oms 沟通交互的 唯一编码,extension1. */
	private String extentionCode;

	/** 最近增量更新时间. */
	private Date lastSyncTime;
	
	/** 基线时间 ,全量更新时更新此时间,增量更新时需要对比这个时间 */
	private Date baselineTime;

	/** 创建时间. */
	private Date createTime = new Date();
	
	/** version. */
	private Date version;

	/**
	 * @return the id
	 */
	@Id
	@Column(name = "ID")
	@SequenceGenerator(name = "SEQ_T_PD_SKU_INVENTORY", sequenceName = "SEQ_T_PD_SKU_INVENTORY", allocationSize = 1)
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "SEQ_T_PD_SKU_INVENTORY")
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
	@Column(name = "AVAILABLE_QTY")
	public Integer getAvailableQty() {
		return availableQty;
	}

	/**
	 * @param availableQty
	 *            the availableQty to set
	 */
	public void setAvailableQty(Integer availableQty) {
		this.availableQty = availableQty;
	}

	/**
	 * @return the extentionCode
	 */
	@Column(name="EXTENTION_CODE")
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
	@Column(name="LAST_SYNS_TIME")
	public Date getLastSyncTime() {
		return lastSyncTime;
	}

	/**
	 * @param lastSyncTime
	 *            the lastSyncTime to set
	 */
	public void setLastSyncTime(Date lastSyncTime) {
		this.lastSyncTime = lastSyncTime;
	}
	
	
	
	@Column(name="BASELINE_TIME")
	public Date getBaselineTime() {
		return baselineTime;
	}

	public void setBaselineTime(Date baselineTime) {
		this.baselineTime = baselineTime;
	}

	/**
	 * @return the createTime
	 */
	@Column(name = "CREATE_TIME")
	public Date getCreateTime() {
		return createTime;
	}

	/**
	 * @param createTime
	 *            the createTime to set
	 */
	public void setCreateTime(Date createTime) {
		this.createTime = createTime;
	}

	/**
	 * @param version the version to set
	 */
	public void setVersion(Date version) {
		this.version = version;
	}

	/**
	 * @return the version
	 */
	@Version
	@Column(name = "VERSION")
	public Date getVersion() {
		return version;
	}

}
