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
import com.baozun.nebula.web.controller.product.viewcommand.ItemBaseInfoViewCommand;

/**
 * @author yue.ch
 *
 */
public class BundleViewCommand extends BaseViewCommand {
	
	private static final long serialVersionUID = -542377171565361284L;

	/**
	 * 
	 */
	private Long bundleId ;
	/**
	 * 
	 */
	private Long itemId ;
	/**
	 * 商品编码
	 */
	private String code ;
	/**
	 * 商品生命周期
	 */
	private int lifeCycle;
	
	/**
	 * bundle价格类型
	 */
	private Integer priceType;
	
	/**
	 * 最小原销售价
	 */
	private BigDecimal minOriginalSalesPrice ;
	/**
	 * 最大原销售价
	 */
	private BigDecimal maxOriginalSalesPrice ;
	/**
	 * 最小销售价
	 */
	private BigDecimal minSalesPrice ;
	/**
	 * 最大销售价
	 */
	private BigDecimal maxSalesPrice ;
	
	/**
	 * bundle商品的扩展信息
	 */
	private ItemBaseInfoViewCommand itemBaseInfoViewCommand;
	
	/**
	 * 
	 */
	private List<BundleElementViewCommand> bundleElementViewCommands;

	public Long getBundleId() {
		return bundleId;
	}

	public void setBundleId(Long bundleId) {
		this.bundleId = bundleId;
	}

	public Long getItemId() {
		return itemId;
	}

	public void setItemId(Long itemId) {
		this.itemId = itemId;
	}

	public String getCode() {
		return code;
	}

	public void setCode(String code) {
		this.code = code;
	}

	public int getLifeCycle() {
		return lifeCycle;
	}

	public void setLifeCycle(int lifeCycle) {
		this.lifeCycle = lifeCycle;
	}

	public Integer getPriceType() {
		return priceType;
	}

	public void setPriceType(Integer priceType) {
		this.priceType = priceType;
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

	public ItemBaseInfoViewCommand getItemBaseInfoViewCommand() {
		return itemBaseInfoViewCommand;
	}

	public void setItemBaseInfoViewCommand(
			ItemBaseInfoViewCommand itemBaseInfoViewCommand) {
		this.itemBaseInfoViewCommand = itemBaseInfoViewCommand;
	}

	public List<BundleElementViewCommand> getBundleElementViewCommands() {
		return bundleElementViewCommands;
	}

	public void setBundleElementViewCommands(
			List<BundleElementViewCommand> bundleElementViewCommands) {
		this.bundleElementViewCommands = bundleElementViewCommands;
	}
}
