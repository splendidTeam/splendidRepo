/**
 
* Copyright (c) 2014 Baozun All Rights Reserved.
 
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
package com.baozun.nebula.web.controller.bundle.viewcommand;

import java.math.BigDecimal;
import java.util.List;

import com.baozun.nebula.web.controller.BaseViewCommand;
import com.baozun.nebula.web.controller.product.viewcommand.PropertyElementViewCommand;

/**
 * @author yue.ch
 *
 */
public class BundleElementViewCommand extends BaseViewCommand {

	private static final long serialVersionUID = 2340192122967455038L;
	
	/**
	 * 最小吊牌价
	 */
	private BigDecimal minListPrice;
	
	/**
	 * 最大吊牌价
	 */
	private BigDecimal maxListPrice;
	
	/**
	 * 捆绑类商品成员最小原价
	 */
	private BigDecimal minOriginalSalesPrice;
	
	/**
	 * 捆绑类商品成员最大原价
	 */
	private BigDecimal maxOriginalSalesPrice;
	
	/**
	 * 捆绑类商品成员中所有商品设置的捆绑销售价的最小值
	 */
	private BigDecimal minSalesPrice;
	
	/**
	 * 捆绑类商品成员中所有商品设置的捆绑销售价的最小值
	 */
	private BigDecimal maxSalesPrice;
	
	/**
	 * 捆绑类商品成员的属性和属性值
	 */
	private List<PropertyElementViewCommand> propertyElementViewCommands;
	
	/**
	 * 捆绑类商品成员中包含的商品
	 */
	private List<BundleItemViewCommand> bundleItemViewCommands;
	

	public BigDecimal getMinListPrice() {
		return minListPrice;
	}

	public void setMinListPrice(BigDecimal minListPrice) {
		this.minListPrice = minListPrice;
	}

	public BigDecimal getMaxListPrice() {
		return maxListPrice;
	}

	public void setMaxListPrice(BigDecimal maxListPrice) {
		this.maxListPrice = maxListPrice;
	}

	public BigDecimal getMinOriginalSalesPrice() {
		return minOriginalSalesPrice;
	}

	public void setMinOriginalSalesPrice(BigDecimal minOriginalSalesPrice) {
		this.minOriginalSalesPrice = minOriginalSalesPrice;
	}

	public BigDecimal getMaxOriginalSalesPrice() {
		return maxOriginalSalesPrice;
	}

	public void setMaxOriginalSalesPrice(BigDecimal maxOriginalSalesPrice) {
		this.maxOriginalSalesPrice = maxOriginalSalesPrice;
	}

	public BigDecimal getMinSalesPrice() {
		return minSalesPrice;
	}

	public void setMinSalesPrice(BigDecimal minSalesPrice) {
		this.minSalesPrice = minSalesPrice;
	}

	public BigDecimal getMaxSalesPrice() {
		return maxSalesPrice;
	}

	public void setMaxSalesPrice(BigDecimal maxSalesPrice) {
		this.maxSalesPrice = maxSalesPrice;
	}

	public List<PropertyElementViewCommand> getPropertyElementViewCommands() {
		return propertyElementViewCommands;
	}

	public void setPropertyElementViewCommands(List<PropertyElementViewCommand> propertyElementViewCommands) {
		this.propertyElementViewCommands = propertyElementViewCommands;
	}

	public List<BundleItemViewCommand> getBundleItemViewCommands() {
		return bundleItemViewCommands;
	}

	public void setBundleItemViewCommands(List<BundleItemViewCommand> bundleItemViewCommands) {
		this.bundleItemViewCommands = bundleItemViewCommands;
	}
	
}
