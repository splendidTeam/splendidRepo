package com.baozun.nebula.model.promotion;

import java.util.List;

public class LinePromotionSize {
	/**
	 * 购物车行ID
	 */
	private Long lineId;

	/**
	 * 参与活动列表
	 */
	private List<Long> promotionList;
	
	public Long getLineId() {
		return lineId;
	}

	public void setLineId(Long lineId) {
		this.lineId = lineId;
	}

	public int getPromotionSize() {
		if (promotionList==null)
			return 0;
		else
			return promotionList.size();
	}

	public List<Long> getPromotionList() {
		return promotionList;
	}

	public void setPromotionList(List<Long> promotionList) {
		this.promotionList = promotionList;
	}
}
