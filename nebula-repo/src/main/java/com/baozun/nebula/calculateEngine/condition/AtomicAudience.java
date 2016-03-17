package com.baozun.nebula.calculateEngine.condition;

import java.math.BigDecimal;

import com.baozun.nebula.command.Command;

public class AtomicAudience implements Command {

	/**
	 * 
	 */
	private static final long serialVersionUID = -9067791482214793831L;

	private long audienceId;
	
	private long promotionid;
	
	private String operateTag = "&";
	
	private String audienceTag;
	
	private long audienceValue;
	
	public long getAudienceId() {
		return audienceId;
	}

	public void setAudienceId(long value) {
		this.audienceId = value;
	}
	
	public long getPromotionId() {
		return promotionid;
	}

	public void setPromotionId(long value) {
		this.promotionid = value;
	}
	
	public String getOperateTag() {
		return operateTag;
	}

	public void setOperateTag(String op) {
		this.operateTag = op;
	}
	
	public String getAudienceTag() {
		return audienceTag;
	}

	public void setAudienceTag(String tag) {
		this.audienceTag = tag;
	}
	
	public long getAudienceValue() {
		return audienceValue;
	}

	public void setAudienceValue(long value) {
		this.audienceValue = value;
	}
}