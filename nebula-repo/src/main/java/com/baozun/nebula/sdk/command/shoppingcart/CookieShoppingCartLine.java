package com.baozun.nebula.sdk.command.shoppingcart;

import java.util.Date;

import com.baozun.nebula.command.Command;

public class CookieShoppingCartLine  implements Command{

	/**
	 * 
	 */
	private static final long	serialVersionUID	= -6854832037537271539L;

	private String	extentionCode;

	/** 商品id */
	private Long	skuId;

	/** 商品数量 */
	private Integer	quantity;

	/** 会员id */
	// private String guestIndentify;

	/** 加入时间 */
	private Date	createTime;
	// extentionCode,itemId,quantity,createTime

	/** 选中状态 **/
	private Integer	settlementState;

	/** 店铺id **/
	private Long	shopId;

	/**
	 * 是否赠品
	 */
	private Boolean	isGift;

	/** 促销号 */
	private Long	promotionId;

	/** 行分组 **/
	private Long	lineGroup;

	public CookieShoppingCartLine(String extentionCode, Long skuId, Integer quantity, Date createTime,
			Integer settlementState, Long shopId, Boolean isGift, Long promotionId, Long lineGroup) {
		super();
		this.extentionCode = extentionCode;
		this.skuId = skuId;
		this.quantity = quantity;
		this.createTime = createTime;
		this.settlementState = settlementState;
		this.shopId = shopId;
		this.isGift = isGift;
		this.promotionId = promotionId;
		this.lineGroup = lineGroup;
	}

	public CookieShoppingCartLine() {
		super();
	}

	public String getExtentionCode() {
		return extentionCode;
	}

	public void setExtentionCode(String extentionCode) {
		this.extentionCode = extentionCode;
	}

	public Long getSkuId() {
		return skuId;
	}

	public void setSkuId(Long skuId) {
		this.skuId = skuId;
	}

	public Integer getQuantity() {
		return quantity;
	}

	public void setQuantity(Integer quantity) {
		this.quantity = quantity;
	}

	public Date getCreateTime() {
		return createTime;
	}

	public void setCreateTime(Date createTime) {
		this.createTime = createTime;
	}

	public Integer getSettlementState() {
		return settlementState;
	}

	public void setSettlementState(Integer settlementState) {
		this.settlementState = settlementState;
	}

	public Long getShopId() {
		return shopId;
	}

	public void setShopId(Long shopId) {
		this.shopId = shopId;
	}

	public Boolean getIsGift() {
		return isGift;
	}

	public void setIsGift(Boolean isGift) {
		this.isGift = isGift;
	}

	public Long getPromotionId() {
		return promotionId;
	}

	public void setPromotionId(Long promotionId) {
		this.promotionId = promotionId;
	}

	public Long getLineGroup() {
		return lineGroup;
	}

	public void setLineGroup(Long lineGroup) {
		this.lineGroup = lineGroup;
	}
}
