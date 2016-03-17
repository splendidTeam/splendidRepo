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
package com.baozun.nebula.sdk.command;

import java.util.Date;

import com.baozun.nebula.command.RateCommand;
import com.baozun.nebula.model.BaseModel;

/**
 * @author Tianlong.Zhang
 *
 */
public class ItemRateCommand  extends BaseModel{

	private static final long serialVersionUID = 823404773262016870L;
	
	private OrderLineCommand orderLineCommand;
	
	private RateCommand rateCommand;
	 

	/** 创建时间 */
	private Date				createTime;

	public Date getCreateTime() {
		return createTime;
	}

	public void setCreateTime(Date createTime) {
		this.createTime = createTime;
	}
  
	/**
	 * @return the orderLineCommand
	 */
	public OrderLineCommand getOrderLineCommand() {
		return orderLineCommand;
	}

	/**
	 * @param orderLineCommand the orderLineCommand to set
	 */
	public void setOrderLineCommand(OrderLineCommand orderLineCommand) {
		this.orderLineCommand = orderLineCommand;
	}

	/**
	 * @return the rateCommand
	 */
	public RateCommand getRateCommand() {
		return rateCommand;
	}

	/**
	 * @param rateCommand the rateCommand to set
	 */
	public void setRateCommand(RateCommand rateCommand) {
		this.rateCommand = rateCommand;
	}
	
	
}
