/**
 * Copyright (c) 2010 Jumbomart All Rights Reserved.
 *
 * This software is the confidential and proprietary information of Jumbomart.
 * You shall not disclose such Confidential Information and shall use it only in
 * accordance with the terms of the license agreement you entered into
 * with Jumbo.
 *
 * JUMBOMART MAKES NO REPRESENTATIONS OR WARRANTIES ABOUT THE SUITABILITY OF THE
 * SOFTWARE, EITHER EXPRESS OR IMPLIED, INCLUDING BUT NOT LIMITED TO THE
 * IMPLIED WARRANTIES OF MERCHANTABILITY, FITNESS FOR A PARTICULAR
 * PURPOSE, OR NON-INFRINGEMENT. JUMBOMART SHALL NOT BE LIABLE FOR ANY DAMAGES
 * SUFFERED BY LICENSEE AS A RESULT OF USING, MODIFYING OR DISTRIBUTING
 * THIS SOFTWARE OR ITS DERIVATIVES.
 *
 */
package com.baozun.nebula.web.controller.product.viewcommand;

import java.math.BigDecimal;

import com.baozun.nebula.web.controller.BaseViewCommand;

/**
 * Sku
 *
 */
public class SkuViewCommand extends BaseViewCommand {

	private static final long serialVersionUID = -161322431659093080L;

	/** SkuId */
    private Long skuId;
    
    /** 外部对接编码 */
	private String extentionCode;
    
	/** 商品属性值列表 {@link com.baozun.nebula.model.product.Sku#properties} */
    private String properties;
    
    /** Sku的生命周期 */
    private Integer lifecycle;
    
    /** Sku的销售价 */
    private BigDecimal salePrice;

	/** Sku的吊牌价(原单价) */
	private BigDecimal listPrice;
	
	/** 库存（这里的库存并不是一个准确的值，因为该对象会被缓存）. */
	private Integer availableQty;

	public Long getSkuId() {
		return skuId;
	}

	public void setSkuId(Long skuId) {
		this.skuId = skuId;
	}

	public String getExtentionCode() {
		return extentionCode;
	}

	public void setExtentionCode(String extentionCode) {
		this.extentionCode = extentionCode;
	}

	public String getProperties() {
		return properties;
	}

	public void setProperties(String properties) {
		this.properties = properties;
	}

	public Integer getLifecycle() {
		return lifecycle;
	}

	public void setLifecycle(Integer lifecycle) {
		this.lifecycle = lifecycle;
	}

	public BigDecimal getSalePrice() {
		return salePrice;
	}

	public void setSalePrice(BigDecimal salePrice) {
		this.salePrice = salePrice;
	}

	public BigDecimal getListPrice() {
		return listPrice;
	}

	public void setListPrice(BigDecimal listPrice) {
		this.listPrice = listPrice;
	}

	public Integer getAvailableQty() {
		return availableQty;
	}

	public void setAvailableQty(Integer availableQty) {
		this.availableQty = availableQty;
	}
	
}
