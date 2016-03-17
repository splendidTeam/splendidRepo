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
package com.baozun.nebula.sdk.command.logistics;

import java.util.Date;

import com.baozun.nebula.api.RiskControl;
import com.baozun.nebula.api.RiskLevel;
import com.baozun.nebula.command.Command;
import com.baozun.nebula.model.BaseModel;



public class LogisticsCommand extends BaseModel implements Command{


	/**
	 * 
	 */
	private static final long	serialVersionUID	= 3147933380824537723L;

	/** PK. */
	private Long				id;
	
	/** 订单id */
	private Long			orderId;
	
	/** 订单是否完成 */
	private Boolean				isFinish;
	
	/** 物流跟踪描述 */
	private String				trackingDescription;
	
	/** 修改时间 */
	private Date				modifyTime;
	
	@RiskControl(RiskLevel.MEDIUM)
	public Long getId() {
		return id;
	}


	public void setId(Long id) {
		this.id = id;
	}
	
	@RiskControl(RiskLevel.MEDIUM)
	public Long getOrderId() {
		return orderId;
	}


	public void setOrderId(Long orderId) {
		this.orderId = orderId;
	}

	@RiskControl(RiskLevel.MEDIUM)
	public Boolean getIsFinish() {
		return isFinish;
	}


	public void setIsFinish(Boolean isFinish) {
		this.isFinish = isFinish;
	}

	@RiskControl(RiskLevel.MEDIUM)
	public String getTrackingDescription() {
		return trackingDescription;
	}


	public void setTrackingDescription(String trackingDescription) {
		this.trackingDescription = trackingDescription;
	}

	@RiskControl(RiskLevel.MEDIUM)
	public Date getModifyTime() {
		return modifyTime;
	}


	public void setModifyTime(Date modifyTime) {
		this.modifyTime = modifyTime;
	}


	


	
}
