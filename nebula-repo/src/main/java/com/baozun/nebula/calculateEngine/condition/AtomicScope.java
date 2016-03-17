package com.baozun.nebula.calculateEngine.condition;

import java.math.BigDecimal;

import com.baozun.nebula.command.Command;

public class AtomicScope implements Command {
	/**
	 * 
	 */
	private static final long serialVersionUID = -6992931575058821955L;

	private long scopeId;
	
	private long promotionid;
	
	private String operateTag = "&";
	
	private String scopeTag;
	
	private long scopeValue;
	
	public long getScopeId() {
		return scopeId;
	}

	public void setScopeId(long value) {
		this.scopeId = value;
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
}