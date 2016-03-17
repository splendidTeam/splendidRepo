package com.baozun.nebula.sdk.command.shoppingcart;

import com.baozun.nebula.model.BaseModel;

/**
 * 购物车中满足活动的购物车行。用于在购物车中分组显示。优惠套餐主商品和选购商品要绑定，礼品要和条件商品一起显示（整单的条件除外）。
 * 所以该实体记录的是购物车行SKU Level的。
 * @author it
 *
 */
public class PromotionConditionSKU extends BaseModel {
	private static final long serialVersionUID = 1540833196365861582L;
	
	private Long shopId;
	
	private Long promotionId;
	
	private String promotionName;
	/*
	 * Step阶Choice选购
	 */
	private String complexType;

	private long normalConditionId;
	
	private long complexConditionId;
	
	private Long skuId;
	
	private Long itemId;
	
	public Long getShopId() {
		return shopId;
	}

	public void setShopId(Long shopId) {
		this.shopId = shopId;
	}

	public Long getPromotionId() {
		return promotionId;
	}

	public void setPromotionId(Long promotionId) {
		this.promotionId = promotionId;
	}

	public String getPromotionName() {
		return promotionName;
	}

	public void setPromotionName(String promotionName) {
		this.promotionName = promotionName;
	}

	public Long getSkuId() {
		return skuId;
	}

	public void setSkuId(Long skuId) {
		this.skuId = skuId;
	}

	public Long getItemId() {
		return itemId;
	}

	public void setItemId(Long itemId) {
		this.itemId = itemId;
	}

	public long getNormalConditionId() {
		return normalConditionId;
	}

	public void setNormalConditionId(long normalConditionId) {
		this.normalConditionId = normalConditionId;
	}

	public long getComplexConditionId() {
		return complexConditionId;
	}

	public void setComplexConditionId(long complexConditionId) {
		this.complexConditionId = complexConditionId;
	}

	public String getComplexType() {
		return complexType;
	}

	public void setComplexType(String complexType) {
		this.complexType = complexType;
	}
}
