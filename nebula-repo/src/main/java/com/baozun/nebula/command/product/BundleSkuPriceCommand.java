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
package com.baozun.nebula.command.product;

import java.io.Serializable;
import java.math.BigDecimal;

/**
 * @author yue.ch
 * @time 2016年5月27日 下午5:10:40
 */
public class BundleSkuPriceCommand implements Serializable {

	private static final long serialVersionUID = -5636106463465948581L;
	
	private Long bundleItemId;
	
	private Long bundleId;
	
	private Long skuId;
	
	private BigDecimal listPrice;
	
	private BigDecimal originalSalesPrice;
	
	private BigDecimal salesPrice;

	public Long getBundleItemId() {
		return bundleItemId;
	}

	public void setBundleItemId(Long bundleItemId) {
		this.bundleItemId = bundleItemId;
	}

	public Long getBundleId() {
		return bundleId;
	}

	public void setBundleId(Long bundleId) {
		this.bundleId = bundleId;
	}

	public Long getSkuId() {
		return skuId;
	}

	public void setSkuId(Long skuId) {
		this.skuId = skuId;
	}

	public BigDecimal getListPrice() {
		return listPrice;
	}

	public void setListPrice(BigDecimal listPrice) {
		this.listPrice = listPrice;
	}

	public BigDecimal getOriginalSalesPrice() {
		return originalSalesPrice;
	}

	public void setOriginalSalesPrice(BigDecimal originalSalesPrice) {
		this.originalSalesPrice = originalSalesPrice;
	}

	public BigDecimal getSalesPrice() {
		return salesPrice;
	}

	public void setSalesPrice(BigDecimal salesPrice) {
		this.salesPrice = salesPrice;
	}
}
