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
package com.baozun.nebula.web.command;

import java.io.Serializable;
import java.math.BigDecimal;

/**
 * @author yue.ch
 * @time 2016年6月13日 上午11:34:09
 */
public class BundleSkuViewCommand implements Serializable,Comparable<BundleSkuViewCommand> {

	private static final long serialVersionUID = -5611494630803303719L;
	
	private Long skuId;
	
	private BigDecimal originalSalesPrice;
	
	private BigDecimal salesPrice;
	
	private boolean isParticipation;
	
	private String property;

	public Long getSkuId() {
		return skuId;
	}

	public void setSkuId(Long skuId) {
		this.skuId = skuId;
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

	public boolean getIsParticipation() {
		return isParticipation;
	}

	public void setIsParticipation(boolean isParticipation) {
		this.isParticipation = isParticipation;
	}

	public String getProperty() {
		return property;
	}

	public void setProperty(String property) {
		this.property = property;
	}

	@Override
	public int compareTo(BundleSkuViewCommand o) {
		return getSkuId().intValue() - o.getSkuId().intValue();
	}

}
