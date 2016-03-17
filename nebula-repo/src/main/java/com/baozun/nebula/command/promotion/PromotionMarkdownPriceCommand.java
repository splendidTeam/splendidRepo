package com.baozun.nebula.command.promotion;

import java.math.BigDecimal;

import com.baozun.nebula.command.Command;

public class PromotionMarkdownPriceCommand implements Command {

	/**
	 * item,sku,disc_price,lifecycle
	 */
	private static final long serialVersionUID = 5553522330525935336L;
	/** PK */
	private Long id;
	
	/**
	 * 店铺
	 */
	private Long				shopId;
	
	/** 商品Id */
	private Long				itemId;

	/**
	 * sku Id
	 */
	private Long skuId ;
	
	/**
	 * 活动价格，一口价，限期内减价销售，结束后价格自动恢复
	 */
	private BigDecimal			markDownPrice;

	/** 生命周期：0没生效，1生效*/
	private Integer lifecycle;

	public Long getShopId() {
		return shopId;
	}

	public void setShopId(Long shopId) {
		this.shopId = shopId;
	}

	public Long getItemId() {
		return itemId;
	}

	public void setItemId(Long itemId) {
		this.itemId = itemId;
	}

	public Long getSkuId() {
		return skuId;
	}

	public void setSkuId(Long skuId) {
		this.skuId = skuId;
	}

	public BigDecimal getMarkDownPrice() {
		return markDownPrice;
	}

	public void setMarkDownPrice(BigDecimal markDownPrice) {
		this.markDownPrice = markDownPrice;
	}

	public Integer getLifecycle() {
		return lifecycle;
	}

	public void setLifecycle(Integer lifecycle) {
		this.lifecycle = lifecycle;
	}
}
