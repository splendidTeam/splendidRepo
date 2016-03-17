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
package com.baozun.nebula.model.salesorder;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;

import org.hibernate.annotations.OptimisticLockType;

import com.baozun.nebula.model.BaseModel;
/**
 * 订单物流信息
 * 
 * @author chuanyang.zheng
 * @creattime 2013-11-20
 */
@Entity
@Table(name = "t_so_logistics")
@org.hibernate.annotations.Entity(optimisticLock = OptimisticLockType.VERSION)
public class Logistics extends BaseModel{


	private static final long serialVersionUID = 6573163550256506180L;

	/** PK. */
	private Long				id;
	
	/** 订单id */
	private Long           orderId;
	
	/** 订单是否完成 */
	private Boolean				isFinish;
	
	/** 物流跟踪描述 */
	private String				trackingDescription;
	
	/** 修改时间 */
	private Date				modifyTime;
	
	/** version*/
	private Date				version;
	
	
	@Id
	@Column(name = "ID")
	@SequenceGenerator(name = "SEQ_T_SAL_LOGISTICS",sequenceName = "S_T_SAL_LOGISTICS",allocationSize = 1)
	@GeneratedValue(strategy = GenerationType.SEQUENCE,generator = "SEQ_T_SAL_LOGISTICS")
	public Long getId() {
		return id;
	}


	public void setId(Long id) {
		this.id = id;
	}
	
	@Column(name = "ORDER_ID")
	public Long getOrderId() {
		return orderId;
	}


	public void setOrderId(Long orderId) {
		this.orderId = orderId;
	}

	@Column(name = "IS_FINISH")
	public Boolean getIsFinish() {
		return isFinish;
	}


	public void setIsFinish(Boolean isFinish) {
		this.isFinish = isFinish;
	}

	@Column(name = "TRACKING_DESCRIPTION",columnDefinition="text")
	public String getTrackingDescription() {
		return trackingDescription;
	}


	public void setTrackingDescription(String trackingDescription) {
		this.trackingDescription = trackingDescription;
	}

	@Column(name = "MODIFY_TIME")
	public Date getModifyTime() {
		return modifyTime;
	}


	public void setModifyTime(Date modifyTime) {
		this.modifyTime = modifyTime;
	}


	@Column(name = "VERSION")
	public Date getVersion() {
		return version;
	}


	public void setVersion(Date version) {
		this.version = version;
	}

	


	
}
