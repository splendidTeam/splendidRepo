package com.baozun.nebula.sdk.command.shoppingcart;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.List;

public class PromotionBrief implements Serializable{
	/**
	 * 
	 */
	private static final long serialVersionUID = 612023437604590706L;

	private Long shopId;

	private Long promotionId;

	private String promotionName;

	private BigDecimal promotionAmount;

	private BigDecimal promotionRate;

	private Integer memComboType;// 会员组合类型：1会员列表，2会员分组，3自定义,4组合

	private Long memComboId;

	private Integer productComboType;// 商品范围类型：1商品，2目录，3自定义，4组合

	private Long productComboId;

	private String conditionType;// Normal常规Step阶Choice选购NormalStep常规加阶梯NormalChoice常规加选购

	private String conditionExpression = "";

	private String conditionExpressionComplex = "";

	private Integer conditionTypeId;// 1常规2阶3选购4常规加阶梯5常规加选购

	private Long normalConditionId;

	private String freeShippingMark;

	private BigDecimal freeShippingFee;

	private String promotionSettingTypeTag;

	private Integer promotionSettingType;

	private List<PromotionSettingDetail> details;

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

	public BigDecimal getPromotionAmount() {
		return promotionAmount;
	}

	public void setPromotionAmount(BigDecimal promotionAmount) {
		this.promotionAmount = promotionAmount;
	}

	public BigDecimal getPromotionRate() {
		return promotionRate;
	}

	public void setPromotionRate(BigDecimal promotionRate) {
		this.promotionRate = promotionRate;
	}

	public Integer getMemComboType() {
		return memComboType;
	}

	public void setMemComboType(Integer type) {
		this.memComboType = type;
	}

	public Integer getPromotionScopeType() {
		return productComboType;
	}

	public void setPromotionScopeType(Integer type) {
		this.productComboType = type;
	}

	public Long getProductComboId() {
		return productComboId;
	}

	public void setProductComboId(Long id) {
		this.productComboId = id;
	}

	public String getConditionType() {
		return conditionType;
	}

	public void setConditionType(String type) {
		this.conditionType = type;
	}

	public Integer getPromotionConditionTypeId() {
		return this.conditionTypeId;
	}

	public void setPromotionConditionType(Integer type) {
		this.conditionTypeId = type;
	}

	public Long getNormalConditionId() {
		return normalConditionId;
	}

	public void setNormalConditionId(Long id) {
		this.normalConditionId = id;
	}

	public String getFreeShippingMark() {
		return freeShippingMark;
	}

	public void setFreeShippingMark(String mark) {
		this.freeShippingMark = mark;
	}

	public BigDecimal getFreeShippingFee() {
		return freeShippingFee;
	}

	public void setFreeShippingMark(BigDecimal fee) {
		this.freeShippingFee = fee;
	}

	public List<PromotionSettingDetail> getDetails() {
		return details;
	}

	public void setDetails(List<PromotionSettingDetail> details) {
		this.details = details;
	}

	public String getPromotionSettingTypeTag() {
		return promotionSettingTypeTag;
	}

	public void setPromotionSettingTypeTag(String tag) {
		this.promotionSettingTypeTag = tag;
	}

	public Integer getPromotionSettingType() {
		return promotionSettingType;
	}

	public void setPromotionSettingType(Integer tag) {
		this.promotionSettingType = tag;
	}

	public Long getMemComboId() {
		return memComboId;
	}

	public void setMemComboId(Long memComboId) {
		this.memComboId = memComboId;
	}

	public Integer getProductComboType() {
		return productComboType;
	}

	public void setProductComboType(Integer productComboType) {
		this.productComboType = productComboType;
	}

	public Integer getConditionTypeId() {
		return conditionTypeId;
	}

	public void setConditionTypeId(Integer conditionTypeId) {
		this.conditionTypeId = conditionTypeId;
	}

	public String getConditionExpression() {
		return conditionExpression;
	}

	public void setConditionExpression(String conditionExpression) {
		this.conditionExpression = conditionExpression;
	}

	public String getConditionExpressionComplex() {
		return conditionExpressionComplex;
	}

	public void setConditionExpressionComplex(String conditionExpressionComplex) {
		this.conditionExpressionComplex = conditionExpressionComplex;
	}
}
