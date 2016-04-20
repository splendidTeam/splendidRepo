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
package com.baozun.nebula.command.bundle;
import java.io.Serializable;
import java.math.BigDecimal;
import java.util.List;


public class BundleItemCommand implements Serializable{

	private static final long serialVersionUID = -2130525552332744535L;
	
	/**
	 * 商品Id
	 */
	private long itemId;
	
	/**
	 * 最小商品原销售价
	 */
	private BigDecimal minOriginalSalesPrice;
	
	/**
	 * 最大商品原销售价
	 */
	private BigDecimal maxOriginalSalesPrice;
	
	/**
	 * bundle最小商品销售价
	 */
	private BigDecimal minBundleSalesPrice;
	
	/**
	 * bundle最大商品销售价
	 */
	private BigDecimal maxBundleSalesPrice;
	
	private List<BundleSkuCommand> bundleSkus;
	
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

	public BigDecimal getMinBundleSalesPrice() {
		return minBundleSalesPrice;
	}

	public void setMinBundleSalesPrice(BigDecimal minBundleSalesPrice) {
		this.minBundleSalesPrice = minBundleSalesPrice;
	}

	public BigDecimal getMaxBundleSalesPrice() {
		return maxBundleSalesPrice;
	}

	public void setMaxBundleSalesPrice(BigDecimal maxBundleSalesPrice) {
		this.maxBundleSalesPrice = maxBundleSalesPrice;
	}

	public List<BundleSkuCommand> getBundleSkus() {
		return bundleSkus;
	}

	public void setBundleSkus(List<BundleSkuCommand> bundleSkus) {
		this.bundleSkus = bundleSkus;
	}

	public long getItemId() {
		return itemId;
	}

	public void setItemId(long itemId) {
		this.itemId = itemId;
	}
	
}
