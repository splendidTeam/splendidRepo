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

import java.util.ArrayList;
import java.util.Date;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import com.baozun.nebula.manager.CacheManager;
import com.baozun.nebula.manager.TimeInterval;
import com.baozun.nebula.sdk.manager.SdkCategoryManager;
import com.baozun.nebula.sdk.manager.SdkNavigationManager;
import com.baozun.nebula.sdk.manager.SdkSearchConditionManager;
import com.baozun.nebula.sdk.manager.product.SdkPropertyManager;
import com.baozun.nebula.search.command.MetaDataCommand;
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

	private static final Logger			LOG							= LoggerFactory.getLogger(FacetFilterHelperImpl.class);

	/** 分类元数据在缓存中的key，完整的key还要加上语言 */
	private final static String			categoryMetaCacheKey		= "categoryMetaCacheKey_";

	/** 属性元数据在缓存中的key，完整的key还要加上语言 */
	private final static String			propertyMetaCacheKey		= "propertyMetaCacheKey_";

	/** 属性值元数据在缓存中的key，完整的key还要加上语言 */
	private final static String			propertyValueMetaCacheKey	= "propertyValueMetaCacheKey_";

	/** 导航元数据在缓存中的key，完整的key还要加上语言 */
	private final static String			navigationMetaCacheKey		= "navigationMetaCacheKey_";

	/** 搜索条件元数据在缓存中的key，完整的key还要加上语言 */
	private final static String			searchConditionMetaCacheKey	= "searchConditionMetaCacheKey_";

	@Autowired
	private CacheManager				cacheManager;

	@Autowired
	private SdkCategoryManager			sdkCategoryManager;

	@Autowired
	private SdkPropertyManager			sdkPropertyManager;

	@Autowired
	private SdkNavigationManager		sdkNavigationManager;

	@Autowired
	private SdkSearchConditionManager	sdkSearchConditionManager;

	@Override
	public FacetFilterMetaData loadFacetFilterMetaData(){
		FacetFilterMetaData facetFilterMetaData = new FacetFilterMetaData();

		Map<String, Object> categoryMetaMap = null;
		Map<String, Object> propertyMetaMap = null;
		Map<String, Object> propertyValueMetaMap = null;
		Map<String, Object> navigationMetaMap = null;
		Map<String, Object> searchConditionMetaMap = null;

		String lang = LangUtil.getCurrentLang();
		try{
			categoryMetaMap = cacheManager.getObject(categoryMetaCacheKey + lang);
			propertyMetaMap = cacheManager.getObject(propertyMetaCacheKey + lang);
			propertyValueMetaMap = cacheManager.getObject(propertyValueMetaCacheKey + lang);
			navigationMetaMap = cacheManager.getObject(navigationMetaCacheKey + lang);
			searchConditionMetaMap = cacheManager.getObject(searchConditionMetaCacheKey + lang);
		}catch (Exception e){
			LOG.error("[SOLR_LOADFACETFILTERMETADATA] cacheManager getObect() error. time:{}", new Date());
		}

		if (categoryMetaMap == null) {
			List<MetaDataCommand> allCategorys = sdkCategoryManager.findCategoryMetaDataByLang(lang);
			if (Validator.isNotNullOrEmpty(allCategorys)) {
				categoryMetaMap = new LinkedHashMap<String, Object>();
				for (MetaDataCommand metaDataCommand : allCategorys){
					categoryMetaMap.put(metaDataCommand.getId().toString(), metaDataCommand.getName());
				}

				cacheManager.setObject(categoryMetaCacheKey + lang, categoryMetaMap, TimeInterval.SECONDS_PER_DAY);
			}
		}

		if (propertyMetaMap == null) {
			List<MetaDataCommand> properties = sdkPropertyManager.findPropertyMetaDataByLang(lang);
			if (Validator.isNotNullOrEmpty(properties)) {
				propertyMetaMap = new LinkedHashMap<String, Object>();
				for (MetaDataCommand metaDataCommand : properties){
					propertyMetaMap.put(metaDataCommand.getId().toString(), metaDataCommand.getName());
				}
				cacheManager.setObject(propertyMetaCacheKey + lang, propertyMetaMap, TimeInterval.SECONDS_PER_DAY);
			}
		}

		if (propertyValueMetaMap == null) {
			List<MetaDataCommand> propertyValues = sdkPropertyManager.findPropertyValueMetaDataByLang(lang);
			if (Validator.isNotNullOrEmpty(propertyValues)) {
				propertyValueMetaMap = new LinkedHashMap<String, Object>();
				for (MetaDataCommand metaDataCommand : propertyValues){
					propertyValueMetaMap.put(metaDataCommand.getId().toString(), metaDataCommand.getName());
				}
				cacheManager.setObject(propertyValueMetaCacheKey + lang, propertyValueMetaMap, TimeInterval.SECONDS_PER_DAY);
			}
		}

		if (navigationMetaMap == null) {
			List<MetaDataCommand> navigations = sdkNavigationManager.findNavigationMetaDataBylang(lang);
			if (Validator.isNotNullOrEmpty(navigations)) {
				navigationMetaMap = new LinkedHashMap<String, Object>();
				for (MetaDataCommand metaDataCommand : navigations){
					navigationMetaMap.put(metaDataCommand.getId().toString(), metaDataCommand.getName());
				}

				cacheManager.setObject(navigationMetaCacheKey + lang, navigationMetaMap, TimeInterval.SECONDS_PER_DAY);
			}
		}

		if (searchConditionMetaMap == null) {
			List<MetaDataCommand> searchConditions = sdkSearchConditionManager.findSearchConditionMetDataByLang(lang);
			if (Validator.isNotNullOrEmpty(searchConditions)) {
				searchConditionMetaMap = new LinkedHashMap<String, Object>();
				for (MetaDataCommand metaDataCommand : searchConditions){
					searchConditionMetaMap.put(metaDataCommand.getId().toString(), metaDataCommand.getName());
				}
				cacheManager.setObject(searchConditionMetaCacheKey + lang, searchConditionMetaMap, TimeInterval.SECONDS_PER_DAY);
			}
		}

		if (categoryMetaMap != null)
			facetFilterMetaData.setCategoryMetaMap(categoryMetaMap);
		if (categoryMetaMap != null)
			facetFilterMetaData.setNavigationMetaMap(navigationMetaMap);
		if (propertyMetaMap != null)
			facetFilterMetaData.setPropertyMetaMap(propertyMetaMap);
		if (propertyValueMetaMap != null)
			facetFilterMetaData.setPropertyValueMetaMap(propertyValueMetaMap);
		if (searchConditionMetaMap != null)
			facetFilterMetaData.setSearchConditionMetaMap(searchConditionMetaMap);

		return facetFilterMetaData;
	}

	@Override
	public List<FacetGroup> createFilterResult(SearchResultPage<ItemForSolrI18nCommand> pagination){
		FacetFilterMetaData facetFilterMetaData = loadFacetFilterMetaData();

		List<FacetGroup> newFacetGroups = new ArrayList<FacetGroup>();
		List<FacetGroup> facetGroups = pagination.getFacetGroups();
		for (FacetGroup facetGroup : facetGroups){
			FacetGroup group = new FacetGroup();
			if (facetGroup.isCategory()) {
				// 如果是分类的facet
				List<Facet> facets = FacetTreeUtil.createFacetTree(facetGroup);
				covertCategoryFacets(facets, facetFilterMetaData.getCategoryMetaMap());
				group.setFacets(facets);
			}else{
				group = covertPropertyFacetGroup(facetGroup, facetFilterMetaData);
			}
			if (group != null)
				newFacetGroups.add(group);
		}

		return newFacetGroups;
	}

	/**
	 * 转换分类的facets
	 * 
	 * @return List<Facet>
	 * @param facets
	 * @param categoryMetaMap
	 * @author 冯明雷
	 * @time 2016年4月28日下午6:17:47
	 */
	private List<Facet> covertCategoryFacets(List<Facet> facets,Map<String, Object> categoryMetaMap){
		if (Validator.isNotNullOrEmpty(categoryMetaMap)) {
			for (Facet facet : facets){
				Object obj = categoryMetaMap.get(facet.getId());
				if (obj != null) {
					facet.setTitle(obj.toString());
				}

				List<Facet> list = facet.childrens;
				if (Validator.isNotNullOrEmpty(list)) {
					covertCategoryFacets(list, categoryMetaMap);
				}
			}
		}

		return facets;

	}

	/**
	 * 转换数字能改的facetGroup
	 * 
	 * @return FacetGroup
	 * @param facetGroup
	 * @param facetFilterMetaData
	 * @author 冯明雷
	 * @time 2016年4月28日下午6:17:47
	 */
	private FacetGroup covertPropertyFacetGroup(FacetGroup facetGroup,FacetFilterMetaData facetFilterMetaData){
		Map<String, Object> propertyValueMetaMap = facetFilterMetaData.getPropertyValueMetaMap();
		Map<String, Object> searchConditionMetaMap = facetFilterMetaData.getSearchConditionMetaMap();
		
		Object searchObj=searchConditionMetaMap.get(facetGroup.getId().toString());
		if(searchObj!=null)
			facetGroup.setTitle(searchObj.toString());
		
		List<Facet> facets=facetGroup.getFacets();
		if(facets!=null&&facets.size()>0){
			for (Facet facet : facets){
				Object propertyValueObj=propertyValueMetaMap.get(facet.getId().toString());
				if(propertyValueObj!=null)
					facet.setTitle(searchObj.toString());
			}
		}

		facetGroup.setFacets(facets);
		
		return facetGroup;

	}

}
