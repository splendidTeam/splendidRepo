package com.baozun.nebula.sdk.manager.promotion;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.baozun.nebula.dao.promotion.PromotionMarkdownPriceDao;
import com.baozun.nebula.model.promotion.PromotionMarkdownPrice;
@Transactional
@Service("sdkPromotionMarkdownPriceManager")
public class SdkPromotionMarkdownPriceManagerImpl implements SdkPromotionMarkdownPriceManager{
	
	@Autowired
	private PromotionMarkdownPriceDao markdownPriceDao;
	
	@Override
	@Transactional(readOnly=false)
	public List<PromotionMarkdownPrice> getPromotionMarkdownPriceList() {
		return markdownPriceDao.getPromotionMarkdownPriceList();
	}
}
