package com.baozun.nebula.sdk.manager.order;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.baozun.nebula.dao.promotion.PromotionMarkdownPriceDao;
import com.baozun.nebula.model.promotion.PromotionMarkdownPrice;
import com.baozun.nebula.sdk.manager.promotion.SdkPromotionMarkdownPriceManager;
@Transactional
@Service("sdkPromotionMarkdownPriceManager")
public class SdkPromotionMarkdownPriceManagerImpl implements SdkPromotionMarkdownPriceManager{
	
	@Autowired
	private PromotionMarkdownPriceDao markdownPriceDao;
	
	@Override
	@Transactional(readOnly=true)
	public List<PromotionMarkdownPrice> getPromotionMarkdownPriceList() {
		return markdownPriceDao.getPromotionMarkdownPriceList();
	}
}
