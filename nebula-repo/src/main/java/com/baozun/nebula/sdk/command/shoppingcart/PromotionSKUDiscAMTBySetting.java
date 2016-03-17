package com.baozun.nebula.sdk.command.shoppingcart;

import java.math.BigDecimal;
import java.util.List;
import java.util.Set;

import com.baozun.nebula.command.Command;

public class PromotionSKUDiscAMTBySetting  implements Command{
	
	/**
	 * 
	 */
	private static final long	serialVersionUID	= -4788633479644853170L;

	private Long shopId;
	
	private Long promotionId;
	
	private String promotionName;
	/**
	 * Normal常规Step阶Choice选购NormalStep常规加阶梯NormalChoice常规加选购
	 */
	private String conditionType;

	private Long settingId;
	
	private String settingTypeTag;
	
	private String settingName;
	
	private String settingExpression;
	
	private Long skuId;
	
	private Long itemId;
	
	private String itemName;
	
	private BigDecimal salesPrice = BigDecimal.ZERO;
	
	private BigDecimal markdownPrice = BigDecimal.ZERO;
	
	private Integer qty;
	
	private BigDecimal discountAmount = BigDecimal.ZERO;//指那些常规的，可以落到SKU上的优惠，和QTY有关的话，已经计算过QTY的

	private Set<String> couponCodes;//如果没有SKU值的话，就是整单的
	
	private BigDecimal shippingDiscountAmount = BigDecimal.ZERO;//整单的shipping
	
	private boolean baseOrder = false;//是否是整单优惠设置，
	
	private boolean freeShippingMark = false;
	
	/** 分组标签 **/
	private Set<String> comboIds;
	
	private List<Long> categoryList;
	
	/** 促销类型 */
	private String	promotionType;
	
	/**
	 * 是否为礼品
	 */
	private boolean giftMark = false;
	/**
	 * 礼品显示类型，0不需要用户选择，1需要用户选择
	 */
	private Integer giftChoiceType = 0;
	/**
	 * 用户不参与选择时，直接推送礼品的样数；用户参与选择时，直接推送礼品的样数
	 */
	private Integer giftCountLimited = 1;
	/*
	 * 0非卖品，不扣库存。多用做赠品。1或null为正常商品
	 */
	private Integer type = 1;
	
	/**
	 * 1 商品已上架 0 商品未上架
	 */
	private String	state;
	
	public String getPromotionType() {
		return promotionType;
	}

	public void setPromotionType(String promotionType) {
		this.promotionType = promotionType;
	}

	public List<Long> getCategoryList() {
		return categoryList;
	}

	public void setCategoryList(List<Long> categoryList) {
		this.categoryList = categoryList;
	}

	public Set<String> getComboIds() {
		return comboIds;
	}

	public void setComboIds(Set<String> comboIds) {
		this.comboIds = comboIds;
	}

	
	public BigDecimal getShippingDiscountAmount() {
		return shippingDiscountAmount;
	}

	public void setShippingDiscountAmount(BigDecimal disc) {
		this.shippingDiscountAmount = disc;
	}
	
	public String getSettingExpression() {
		return settingExpression;
	}

	public void setSettingExpression(String exp) {
		this.settingExpression = exp;
	}
	
	public String getSettingName() {
		return settingName;
	}

	public void setSettingName(String name) {
		this.settingName = name;
	}
	
	public String getSettingTypeTag() {
		return settingTypeTag;
	}

	public void setSettingTypeTag(String tag) {
		this.settingTypeTag = tag;
	}
	
	public Long getSkuId() {
		return skuId;
	}

	public void setSkuId(Long skuId) {
		this.skuId = skuId;
	}

	public Set<String> getCouponCodes() {
		return couponCodes;
	}

	public void setCouponCodes(Set<String> couponCodes) {
		this.couponCodes = couponCodes;
	}

	public Long getShopId() {
		return shopId;
	}

	public void setShopId(Long shopId) {
		this.shopId = shopId;
	}
	
	public Long getSettingId() {
		return settingId;
	}

	public void setSettingId(Long id) {
		this.settingId = id;
	}

	public Long getPromotionId() {
		return promotionId;
	}

	public void setPromotionId(Long promotionId) {
		this.promotionId = promotionId;
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

	public BigDecimal getSalesPrice() {
		return salesPrice;
	}

	public void setSalesPrice(BigDecimal salesPrice) {
		this.salesPrice = salesPrice;
	}

	public Integer getQty() {
		return qty;
	}

	public void setQty(Integer qty) {
		this.qty = qty;
	}

	public BigDecimal getDiscountAmount() {
		return discountAmount;
	}

	public void setDiscountAmount(BigDecimal discountAmount) {
		this.discountAmount = discountAmount;
	}

	public String getPromotionName() {
		return promotionName;
	}

	public void setPromotionName(String promotionName) {
		this.promotionName = promotionName;
	}

	public boolean getGiftMark() {
		return giftMark;
	}

	public void setGiftMark(boolean giftMark) {
		this.giftMark = giftMark;
	}

	public boolean getBaseOrder() {
		return baseOrder;
	}

	public void setBaseOrder(boolean baseOrder) {
		this.baseOrder = baseOrder;
	}

	public boolean getFreeShippingMark() {
		return freeShippingMark;
	}

	public void setFreeShippingMark(boolean freeShippingMark) {
		this.freeShippingMark = freeShippingMark;
	}

	public String getConditionType() {
		return conditionType;
	}

	public void setConditionType(String conditionType) {
		this.conditionType = conditionType;
	}

	public Integer getGiftChoiceType() {
		return giftChoiceType;
	}

	public void setGiftChoiceType(Integer giftChoiceType) {
		this.giftChoiceType = giftChoiceType;
	}

	public Integer getGiftCountLimited() {
		return giftCountLimited;
	}

	public void setGiftCountLimited(Integer giftCountLimited) {
		this.giftCountLimited = giftCountLimited;
	}

	public Integer getType() {
		return type;
	}

	public void setType(Integer type) {
		this.type = type;
	}

	public String getState() {
		return state;
	}

	public void setState(String state) {
		this.state = state;
	}

	public BigDecimal getMarkdownPrice() {
		return markdownPrice;
	}

	public void setMarkdownPrice(BigDecimal markdownPrice) {
		this.markdownPrice = markdownPrice;
	}
}
