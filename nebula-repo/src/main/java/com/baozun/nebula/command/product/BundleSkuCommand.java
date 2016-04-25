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
package com.baozun.nebula.command.product;
import java.math.BigDecimal;

import com.baozun.nebula.model.bundle.BundleSku;


public class BundleSkuCommand extends BundleSku{

	private static final long serialVersionUID = -3663016675036644108L;

	/**
	 * sku原销售价
	 * 
	 */
	private BigDecimal originalSalesPrice = BigDecimal.ZERO;
	
	/**
	 * sku吊牌价
	 */
	private BigDecimal listPrice = BigDecimal.ZERO;
	
	/**
	 * 库存数量
	 */
	private int quantity ;
	
	/**
	 * 销售属性
	 */
	private String properties;
	
	/**
	 * 外部编码
	 */
	private String extentionCode;
	
	/**
	 * 生命周期
	 */
	private Integer lifeCycle;
	
	public Integer getLifeCycle() {
		return lifeCycle;
	}

	public void setLifeCycle(Integer lifeCycle) {
		this.lifeCycle = lifeCycle;
	}

	public int getQuantity() {
		return quantity;
	}

	public void setQuantity(int quantity) {
		this.quantity = quantity;
	}

	public BigDecimal getOriginalSalesPrice() {
		return originalSalesPrice;
	}

	public void setOriginalSalesPrice(BigDecimal originalSalesPrice) {
		this.originalSalesPrice = originalSalesPrice;
	}

	public BigDecimal getListPrice() {
		return listPrice;
	}

	public void setListPrice(BigDecimal listPrice) {
		this.listPrice = listPrice;
	}

	public String getProperties() {
		return properties;
	}

	public void setProperties(String properties) {
		this.properties = properties;
	}

	public String getExtentionCode() {
		return extentionCode;
	}

	public void setExtentionCode(String extentionCode) {
		this.extentionCode = extentionCode;
	}
	
}
