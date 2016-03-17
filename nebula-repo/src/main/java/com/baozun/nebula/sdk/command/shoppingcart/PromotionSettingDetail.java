package com.baozun.nebula.sdk.command.shoppingcart;

import java.math.BigDecimal;
import java.util.List;
import java.util.Set;

import com.baozun.nebula.command.Command;

public class PromotionSettingDetail  implements Command{
	/**
	 * 
	 */
	private static final long	serialVersionUID	= 7548695255948077696L;

	private Long id;
	
	private Long promotionId;
	
	private String promotionName;
	
	/**
	 * Normal常规Step阶Choice选购NormalStep常规加阶梯NormalChoice常规加选购
	 */
	private String conditionType;
	
	private String settingTypeTag;
	
	private String settingName;
	
	private String settingExpression;
	
	private BigDecimal discountAmount = BigDecimal.ZERO;;

	private Long shopId;
	
	private Set<String> couponCodes;
	
	private boolean freeShippingMark = false;
	
	private List<PromotionSKUDiscAMTBySetting> affectSKUDiscountAMTList;
	
	public List<PromotionSKUDiscAMTBySetting> getAffectSKUDiscountAMTList() {
		return affectSKUDiscountAMTList;
	}

	public void setAffectSKUDiscountAMTList(List<PromotionSKUDiscAMTBySetting> discList) {
		this.affectSKUDiscountAMTList = discList;
	}
	
	public boolean getFreeShippingMark() {
		return freeShippingMark;
	}

	public void setFreeShippingMark(boolean free) {
		this.freeShippingMark = free;
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

	public Set<String> getCouponCodes() {
		return couponCodes;
	}

	public void setCouponCode(Set<String> couponCodes) {
		this.couponCodes = couponCodes;
	}

	public Long getShopId() {
		return shopId;
	}

	public void setShopId(Long shopId) {
		this.shopId = shopId;
	}
	
	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public Long getPromotionId() {
		return promotionId;
	}

	public void setPromotionId(Long promotionId) {
		this.promotionId = promotionId;
	}

	public BigDecimal getDiscountAmount() {		
		if (affectSKUDiscountAMTList !=null && affectSKUDiscountAMTList.size()>0)
		{
			discountAmount = BigDecimal.ZERO;
			for(PromotionSKUDiscAMTBySetting setting :affectSKUDiscountAMTList)
			{
				discountAmount = discountAmount.add(setting.getDiscountAmount() == null?BigDecimal.ZERO:setting.getDiscountAmount());
			}
		}
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

	public String getConditionType() {
		return conditionType;
	}

	public void setConditionType(String conditionType) {
		this.conditionType = conditionType;
	}
	
}
