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
 * 商品的价格
 * <p>
 * 这里的价格是对商品价格的封装，主要用于页面展示。</br>
 * 在Nebula中会从两个维度定义商品的价格：商品维度和sku维度。该Command对这两个维度的价格做汇总，分别放置在item*Price和sku*Price两类属性里边。因为一个商品会存在多个sku，每个sku的价格可能不同，所以
 * 对于sku的价格是个区间的形式表述。如果所有sku的价格相同，那么区间的大值和小值相同。
 * </p>
 *
 */
public class PriceViewCommand extends BaseViewCommand {

	private static final long serialVersionUID = 4300757752577261432L;

	/** 商品上定义的吊牌价 */
	private BigDecimal itemListPrice;
	
	/** 商品上定义的销售价 */
	private BigDecimal itemSalesPrice;
	
	/** 该商品所有sku吊牌价的最小值 */
	private BigDecimal skuMinListPrice;
	
	/** 该商品所有sku吊牌价的最大值 */
	private BigDecimal skuMaxListPrice;
	
	/** 该商品所有sku销售价的最小值 */
	private BigDecimal skuMinSalesPrice;
	
	/** 该商品所有sku销售价的最大值 */
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
