package com.baozun.nebula.calculateEngine.condition;

import java.io.Serializable;
import java.math.BigDecimal;

public class AtomicSetting implements Serializable{

	private long settingId;

	private long promotionid;

	private long complexconditionid;

	private String operateTag = "&";

	private String settingTag;

	private BigDecimal settingValue;

	private String scopeTag;

	private long scopeValue;

	private Integer stepPriority;

	private String settingExpression;// 原子条件表达式

	private Integer multiplicationFactor = 1;

	private boolean onePieceMark;// 是否单件计

	private Integer giftChoiceType = 0;// 礼品显示类型，0不需要用户选择，1需要用户选择

	private Integer giftCountLimited = 1;// 用户不参与选择时，直接推送礼品的数量；用户参与选择时，最多选择的数量

	public String getSettingExpression() {
		return settingExpression;
	}

	public void setSettingExpression(String value) {
		this.settingExpression = value;
	}

	public Integer getStepPriority() {
		return stepPriority;
	}

	public void setStepPriority(Integer value) {
		this.stepPriority = value;
	}

	public long getSettingId() {
		return settingId;
	}

	public void setSettingId(long value) {
		this.settingId = value;
	}

	public long getPromotionId() {
		return promotionid;
	}

	public void setPromotionId(long value) {
		this.promotionid = value;
	}

	public long getComplexConditionId() {
		return complexconditionid;
	}

	public void setComplexConditionId(long value) {
		this.complexconditionid = value;
	}

	public String getOperateTag() {
		return operateTag;
	}

	public void setOperateTag(String op) {
		this.operateTag = op;
	}

	public String getSettingTag() {
		return settingTag;
	}

	public void setSettingTag(String tag) {
		this.settingTag = tag;
	}

	public BigDecimal getSettingValue() {
		return settingValue;
	}

	public void setSettingValue(BigDecimal value) {
		this.settingValue = value;
	}

	public String getScopeTag() {
		return scopeTag;
	}

	public void setScopeTag(String tag) {
		this.scopeTag = tag;
	}

	public long getScopeValue() {
		return scopeValue;
	}

	public void setScopeValue(long value) {
		this.scopeValue = value;
	}

	public Integer getMultiplicationFactor() {
		return multiplicationFactor;
	}

	public void setMultiplicationFactor(Integer multiplicationFactor) {
		this.multiplicationFactor = multiplicationFactor;
	}

	public boolean getOnePieceMark() {
		return onePieceMark;
	}

	public void setOnePieceMark(boolean onePieceMark) {
		this.onePieceMark = onePieceMark;
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
}