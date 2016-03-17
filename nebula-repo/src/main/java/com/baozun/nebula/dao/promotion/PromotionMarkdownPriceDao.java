package com.baozun.nebula.dao.promotion;

import java.util.List;

import loxia.annotation.NativeQuery;
import loxia.dao.GenericEntityDao;
import com.baozun.nebula.model.promotion.PromotionMarkdownPrice;

public interface PromotionMarkdownPriceDao extends GenericEntityDao<PromotionMarkdownPrice, Long> {
	@NativeQuery(model = PromotionMarkdownPrice.class)
	List<PromotionMarkdownPrice> getPromotionMarkdownPriceList();
}
