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
import java.math.BigDecimal;

import com.baozun.nebula.model.bundle.BundleSku;


public class BundleSkuCommand extends BundleSku{

	private static final long serialVersionUID = -3663016675036644108L;

	/**
	 * sku原销售价
	 * 
	 */
	private BigDecimal originalSalesPrice;
	
	/**
	 * sku原吊牌价
	 */
	private BigDecimal originalListPrice;
	
	public BigDecimal getOriginalSalesPrice() {
		return originalSalesPrice;
	}

	public void setOriginalSalesPrice(BigDecimal originalSalesPrice) {
		this.originalSalesPrice = originalSalesPrice;
	}

	public BigDecimal getOriginalListPrice() {
		return originalListPrice;
	}

	public void setOriginalListPrice(BigDecimal originalListPrice) {
		this.originalListPrice = originalListPrice;
	}
	
}
