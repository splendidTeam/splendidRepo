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

public class PriceViewCommand extends BaseViewCommand {

	private static final long serialVersionUID = 4300757752577261432L;

	/** 商品上定义的吊牌价 */
	private BigDecimal itemListPrice;
	
	private BigDecimal itemSalesPrice;
	
	private BigDecimal skuMinListPrice;
	
	private BigDecimal skuMaxListPrice;
	
	private BigDecimal skuMinSalesPrice;
	
	private BigDecimal skuMaxSalesPrice;

	public BigDecimal getItemListPrice() {
		return itemListPrice;
	}

	public void setItemListPrice(BigDecimal itemListPrice) {
		this.itemListPrice = itemListPrice;
	}

	public BigDecimal getItemSalesPrice() {
		return itemSalesPrice;
	}

	public void setItemSalesPrice(BigDecimal itemSalesPrice) {
		this.itemSalesPrice = itemSalesPrice;
	}

	public BigDecimal getSkuMinListPrice() {
		return skuMinListPrice;
	}

	public void setSkuMinListPrice(BigDecimal skuMinListPrice) {
		this.skuMinListPrice = skuMinListPrice;
	}

	public BigDecimal getSkuMaxListPrice() {
		return skuMaxListPrice;
	}

	public void setSkuMaxListPrice(BigDecimal skuMaxListPrice) {
		this.skuMaxListPrice = skuMaxListPrice;
	}

	public BigDecimal getSkuMinSalesPrice() {
		return skuMinSalesPrice;
	}

	public void setSkuMinSalesPrice(BigDecimal skuMinSalesPrice) {
		this.skuMinSalesPrice = skuMinSalesPrice;
	}

	public BigDecimal getSkuMaxSalesPrice() {
		return skuMaxSalesPrice;
	}

	public void setSkuMaxSalesPrice(BigDecimal skuMaxSalesPrice) {
		this.skuMaxSalesPrice = skuMaxSalesPrice;
	}
	
	
}
