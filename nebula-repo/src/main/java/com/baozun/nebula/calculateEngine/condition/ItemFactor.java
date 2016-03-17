package com.baozun.nebula.calculateEngine.condition;

import java.math.BigDecimal;

public class ItemFactor {
	/** 商品款的id **/
	private Long itemId = 0L;
	
	/** 购物数量 **/
	private Integer factor = 0;
	
	/** 优惠数值AMT，Rate **/
	private BigDecimal value = BigDecimal.ZERO;

	public Long getItemId() {
		return itemId;
	}
	public void setItemId(Long itemId) {
		this.itemId = itemId;
	}
	public Integer getFactor() {
		return factor;
	}
	public void setFactor(Integer factor) {
		this.factor = factor;
	}
	public BigDecimal getValue() {
		return value;
	}
	public void setValue(BigDecimal value) {
		this.value = value;
	}
}
