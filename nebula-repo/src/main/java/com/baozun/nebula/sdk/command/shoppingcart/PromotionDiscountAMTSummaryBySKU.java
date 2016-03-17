package com.baozun.nebula.sdk.command.shoppingcart;

import java.math.BigDecimal;

import com.baozun.nebula.command.Command;

public class PromotionDiscountAMTSummaryBySKU  implements Command{
	/**
	 * 
	 */
	private static final long	serialVersionUID	= 3761607163208160007L;

	private Long shopId;
	
	private Long skuId;//除去orddisc,ordcoupon，和freeship，其余的都base on SKU
	
	private Long itemId;
	
	private String itemName;
	
	private BigDecimal salesPrice;
	
	private Integer qty;
	
	private BigDecimal discountAmount = BigDecimal.ZERO;

	private boolean giftMark = false;
	
	private boolean freeShippingMark = false;//freeship
	
	private boolean baseOnOrderMark = false;//orddisc,ordcoupon两种,累加这两个标签下Setting的DiscountAMT

	public boolean getFreeShippingMark() {
		return freeShippingMark;
	}

	public void setFreeShippingMark(boolean free) {
		this.freeShippingMark = free;
	}
	
	public Long getSkuId() {
		return skuId;
	}

	public void setSkuId(Long skuId) {
		this.skuId = skuId;
	}

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

	public String getItemName() {
		return itemName;
	}

	public void setItemName(String itemName) {
		this.itemName = itemName;
	}
	
	public Integer getQty() {
		return qty;
	}

	public void setQty(Integer qty) {
		this.qty = qty;
	}

	public BigDecimal getSalesPrice() {
		return salesPrice;
	}

	public void setSalesPrice(BigDecimal salesPrice) {
		this.salesPrice = salesPrice;
	}

	public void setDiscountAmount(BigDecimal discountAmount) {
		this.discountAmount = discountAmount;
	}

	public boolean isGiftMark() {
		return giftMark;
	}

	public void setGiftMark(boolean giftMark) {
		this.giftMark = giftMark;
	}

	public boolean isBaseOnOrderMark() {
		return baseOnOrderMark;
	}

	public void setBaseOnOrderMark(boolean baseOnOrderMark) {
		this.baseOnOrderMark = baseOnOrderMark;
	}

	public BigDecimal getDiscountAmount() {
		return discountAmount;
	}

}
