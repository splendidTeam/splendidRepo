package com.baozun.nebula.sdk.manager.promotion;

import java.util.List;

import com.baozun.nebula.manager.BaseManager;
import com.baozun.nebula.model.promotion.PromotionMarkdownPrice;

public interface SdkPromotionMarkdownPriceManager extends BaseManager{
	public List<PromotionMarkdownPrice> getPromotionMarkdownPriceList();
}
