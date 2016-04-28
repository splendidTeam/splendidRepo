/**
 * Copyright (c) 2015 Baozun All Rights Reserved.
 *
 * This software is the confidential and proprietary information of Baozun.
 * You shall not disclose such Confidential Information and shall use it only in
 * accordance with the terms of the license agreement you entered into
 * with Baozun.
 *
 * BAOZUN MAKES NO REPRESENTATIONS OR WARRANTIES ABOUT THE SUITABILITY OF THE
 * SOFTWARE, EITHER EXPRESS OR IMPLIED, INCLUDING BUT NOT LIMITED TO THE
 * IMPLIED WARRANTIES OF MERCHANTABILITY, FITNESS FOR A PARTICULAR
 * PURPOSE, OR NON-INFRINGEMENT. BAOZUN SHALL NOT BE LIABLE FOR ANY DAMAGES
 * SUFFERED BY LICENSEE AS A RESULT OF USING, MODIFYING OR DISTRIBUTING
 * THIS SOFTWARE OR ITS DERIVATIVES.
 *
 */
package com.baozun.nebula.search;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import com.baozun.nebula.manager.CacheManager;
import com.baozun.nebula.search.command.SearchResultPage;
import com.baozun.nebula.solr.command.ItemForSolrI18nCommand;
import com.baozun.nebula.utilities.common.LangUtil;
import com.feilong.core.Validator;

/**
 * FacetFilterHelper的默认实现
 * 
 * @author 冯明雷
 * @version 1.0
 * @time 2016年4月28日 下午2:34:42
 */
public class FacetFilterHelperImpl implements FacetFilterHelper{

	private static final Logger	LOG							= LoggerFactory.getLogger(FacetFilterHelperImpl.class);

	/** 分类元数据在缓存中的key，完整的key还要加上语言 */
	private final static String	categoryMetaCacheKey		= "categoryMetaCacheKey_";

	/** 属性元数据在缓存中的key，完整的key还要加上语言 */
	private final static String	propertyMetaCacheKey		= "propertyMetaCacheKey_";

	/** 属性值元数据在缓存中的key，完整的key还要加上语言 */
	private final static String	propertyValueMetaCacheKey	= "propertyValueMetaCacheKey_";

	/** 导航元数据在缓存中的key，完整的key还要加上语言 */
	private final static String	navigationMetaCacheKey		= "navigationMetaCacheKey_";

	@Autowired
	private CacheManager		cacheManager;

	@Override
	public FacetFilterMetaData loadFacetFilterMetaData(Long storeId){
		FacetFilterMetaData facetFilterMetaData=new FacetFilterMetaData();
		
		Map<String, Object> categoryMetaMap = null;
		Map<String, Object> propertyMetaMap = null;
		Map<String, Object> propertyValueMetaMap = null;
		Map<String, Object> navigationMetaMap = null;

		String lang = LangUtil.getCurrentLang();
		try{
			categoryMetaMap = cacheManager.getObject(categoryMetaCacheKey + lang);
			propertyMetaMap = cacheManager.getObject(propertyMetaCacheKey + lang);
			propertyValueMetaMap = cacheManager.getObject(propertyValueMetaCacheKey + lang);
			navigationMetaMap = cacheManager.getObject(navigationMetaCacheKey + lang);
		}catch (Exception e){
			LOG.error("[SOLR_LOADFACETFILTERMETADATA] cacheManager getObect() error. time:{}", new Date());
		}
		
		if(Validator.isNullOrEmpty(categoryMetaMap)){
			//List<Category>
			
			cacheManager.setObject(categoryMetaCacheKey + lang,categoryMetaMap);
		}
		
		
		
		
		
		
		
		
		
		
		
		
		
		
		try{
			
			cacheManager.setObject(propertyMetaCacheKey + lang,propertyMetaMap);
			cacheManager.setObject(propertyValueMetaCacheKey + lang,propertyValueMetaMap);
			cacheManager.setObject(navigationMetaCacheKey + lang,navigationMetaMap);
		}catch (Exception e){
			LOG.error("[SOLR_LOADFACETFILTERMETADATA] cacheManager setObect() error. time:{}", new Date());
		}

		return facetFilterMetaData;
	}

	@Override
	public List<FacetGroup> createFilterResult(SearchResultPage<ItemForSolrI18nCommand> pagination){
		return null;
	}

}
