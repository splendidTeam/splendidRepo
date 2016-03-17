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
package com.baozun.nebula.freight.memory;

import java.util.List;

import com.baozun.nebula.command.Command;
import com.baozun.nebula.freight.command.ShippingTemeplateCommand;
import com.baozun.nebula.sdk.command.logistics.DistributionModeCommand;

/**
 * @author Tianlong.Zhang
 *
 */
public class ShopShippingTemeplateCommand  implements Command{

	/**
	 * 
	 */
	private static final long serialVersionUID = 2384965513768857868L;

	/**
	 * 店铺对应的ShippingTemeplate 列表
	 */
	private List<ShippingTemeplateCommand> shippingTemeplateList;
	
	private List<DistributionModeCommand> distributionModeList;

	/**
	 * @return the shippingTemeplateList
	 */
	public List<ShippingTemeplateCommand> getShippingTemeplateList() {
		return shippingTemeplateList;
	}

	/**
	 * @param shippingTemeplateList the shippingTemeplateList to set
	 */
	public void setShippingTemeplateList(
			List<ShippingTemeplateCommand> shippingTemeplateList) {
		this.shippingTemeplateList = shippingTemeplateList;
	}

	/**
	 * @return the distributionModeList
	 */
	public List<DistributionModeCommand> getDistributionModeList() {
		return distributionModeList;
	}

	/**
	 * @param distributionModeList the distributionModeList to set
	 */
	public void setDistributionModeList(
			List<DistributionModeCommand> distributionModeList) {
		this.distributionModeList = distributionModeList;
	}
	
}
