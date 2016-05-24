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
import java.util.Collections;
import java.util.Date;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import com.baozun.nebula.constant.CacheKeyConstant;
import com.baozun.nebula.manager.CacheManager;
import com.baozun.nebula.manager.TimeInterval;
import com.baozun.nebula.model.product.Property;
import com.baozun.nebula.model.product.SearchCondition;
import com.baozun.nebula.sdk.command.SearchConditionCommand;
import com.baozun.nebula.sdk.command.SearchConditionItemCommand;
import com.baozun.nebula.sdk.manager.SdkCategoryManager;
import com.baozun.nebula.sdk.manager.SdkNavigationManager;
import com.baozun.nebula.sdk.manager.SdkSearchConditionManager;
import com.baozun.nebula.sdk.manager.product.SdkPropertyManager;
import com.baozun.nebula.search.command.MetaDataCommand;
import com.baozun.nebula.search.command.SearchResultPage;
import com.baozun.nebula.search.manager.SearchManager;
import com.baozun.nebula.solr.Param.SkuItemParam;
import com.baozun.nebula.solr.command.ItemForSolrCommand;
import com.baozun.nebula.solr.utils.FilterUtil;
import com.baozun.nebula.utilities.common.LangUtil;
import com.feilong.core.Validator;
import com.feilong.core.util.comparator.PropertyComparator;

/**
 * FacetFilterHelper的默认实现
 * 
 * @author 冯明雷
 * @version 1.0
 * @time 2016年4月28日 下午2:34:42
 */
public class FacetFilterHelperImpl implements FacetFilterHelper{

	private static final Logger			LOG								= LoggerFactory.getLogger(FacetFilterHelperImpl.class);

	/** 分类元数据在缓存中的key，完整的key还要加上语言 */
	private final static String			categoryMetaCacheKey			= "categoryMetaCacheKey_";

	/** 属性元数据在缓存中的key，完整的key还要加上语言 */
	private final static String			propertyMetaCacheKey			= "propertyMetaCacheKey_";

	/** 属性值元数据在缓存中的key，完整的key还要加上语言 */
	private final static String			propertyValueMetaCacheKey		= "propertyValueMetaCacheKey_";

	/** 搜索条件元数据在缓存中的key，完整的key还要加上语言 */
	private final static String			searchConditionMetaCacheKey		= "searchConditionMetaCacheKey_";

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

	@Autowired
	private SearchManager				searchManager;

	@Override
	public FacetFilterMetaData loadFacetFilterMetaData(){
		FacetFilterMetaData facetFilterMetaData = new FacetFilterMetaData();

		Map<Long, MetaDataCommand> categoryMetaMap = this.loadCategoryMetaData();
		Map<Long, MetaDataCommand> propertyMetaMap = this.loadPropertyMetaData();
		Map<Long, MetaDataCommand> propertyValueMetaMap = this.loadPropertyValueMetaData();
		Map<Long, MetaDataCommand> navigationMetaMap = this.loadNavigationMetaData();
		
		Map<Long, SearchConditionCommand> searchConditionMetaMap = this.loadSearchConditionMetaData();

		if (categoryMetaMap != null)
			facetFilterMetaData.setCategoryMetaMap(categoryMetaMap);
		if (navigationMetaMap != null)
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
	public Map<Long, MetaDataCommand> loadNavigationMetaData(){
		Map<Long, MetaDataCommand> navigationMetaMap = null;
		String lang = LangUtil.getCurrentLang();
		// 先从缓存中获取数据
		try{
			navigationMetaMap = cacheManager.getObject(CacheKeyConstant.NAVIGATIONMETACACHEKEY + lang);
		}catch (Exception e){
			LOG.error("[SOLR_LOADFACETFILTERMETADATA] cacheManager getObect() error. time:{}", new Date());
		}
		
		// 如果导航的元数据为空
		if (navigationMetaMap == null) {
			// 查询所有导航
			List<MetaDataCommand> navigations = sdkNavigationManager.findNavigationMetaDataBylang(lang);
			if (Validator.isNotNullOrEmpty(navigations)) {
				navigationMetaMap = new LinkedHashMap<Long, MetaDataCommand>();
				for (MetaDataCommand metaDataCommand : navigations){
					navigationMetaMap.put(metaDataCommand.getId(), metaDataCommand);
				}
			}
		}
		// 把值重新设置到缓存当中
		try{
			cacheManager.setObject(CacheKeyConstant.NAVIGATIONMETACACHEKEY+ lang, navigationMetaMap, TimeInterval.SECONDS_PER_HOUR/2);
		}catch (Exception e){
			LOG.error("[SOLR_LOADFACETFILTERMETADATA] cacheManager setObect() error. time:{}", new Date());
		}
		
		return navigationMetaMap;
	}
	
	@Override
	public Map<Long, MetaDataCommand> loadCategoryMetaData() {
		Map<Long, MetaDataCommand> categoryMetaMap = null;

		String lang = LangUtil.getCurrentLang();

		// 先从缓存中获取数据
		try{
			categoryMetaMap = cacheManager.getObject(categoryMetaCacheKey + lang);
		}catch (Exception e){
			LOG.error("[SOLR_LOADFACETFILTERMETADATA] cacheManager getObect() error. time:{}", new Date());
		}

		// 如果分类的元数据为空
		if (categoryMetaMap == null) {
			// 查询所有分类的元数据
			List<MetaDataCommand> allCategorys = sdkCategoryManager.findCategoryMetaDataByLang(lang);

			// 转换为map
			if (Validator.isNotNullOrEmpty(allCategorys)) {
				categoryMetaMap = new LinkedHashMap<Long, MetaDataCommand>();
				for (MetaDataCommand metaDataCommand : allCategorys){
					categoryMetaMap.put(metaDataCommand.getId(), metaDataCommand);
				}
			}
		}


		// 把值重新设置到缓存当中
		try{
			cacheManager.setObject(categoryMetaCacheKey + lang, categoryMetaMap, TimeInterval.SECONDS_PER_HOUR/2);
		}catch (Exception e){
			LOG.error("[SOLR_LOADFACETFILTERMETADATA] cacheManager setObect() error. time:{}", new Date());
		}
		
		return categoryMetaMap;
	}

	@Override
	public Map<Long, MetaDataCommand> loadPropertyMetaData() {
		Map<Long, MetaDataCommand> propertyMetaMap = null;

		String lang = LangUtil.getCurrentLang();

		// 先从缓存中获取数据
		try{
			propertyMetaMap = cacheManager.getObject(propertyMetaCacheKey + lang);
		}catch (Exception e){
			LOG.error("[SOLR_LOADFACETFILTERMETADATA] cacheManager getObect() error. time:{}", new Date());
		}

		// 如果属性的元数据为空
		if (propertyMetaMap == null) {
			// 查询所有属性
			List<MetaDataCommand> properties = sdkPropertyManager.findPropertyMetaDataByLang(lang);

			if (Validator.isNotNullOrEmpty(properties)) {
				propertyMetaMap = new LinkedHashMap<Long, MetaDataCommand>();
				for (MetaDataCommand metaDataCommand : properties){
					propertyMetaMap.put(metaDataCommand.getId(), metaDataCommand);
				}
			}
		}


		// 把值重新设置到缓存当中
		try{
			cacheManager.setObject(propertyMetaCacheKey + lang, propertyMetaMap, TimeInterval.SECONDS_PER_HOUR/2);
		}catch (Exception e){
			LOG.error("[SOLR_LOADFACETFILTERMETADATA] cacheManager setObect() error. time:{}", new Date());
		}
		return propertyMetaMap;
	}

	@Override
	public Map<Long, MetaDataCommand> loadPropertyValueMetaData() {
		Map<Long, MetaDataCommand> propertyValueMetaMap = null;

		String lang = LangUtil.getCurrentLang();

		// 先从缓存中获取数据
		try{
			propertyValueMetaMap = cacheManager.getObject(propertyValueMetaCacheKey + lang);
		}catch (Exception e){
			LOG.error("[SOLR_LOADFACETFILTERMETADATA] cacheManager getObect() error. time:{}", new Date());
		}


		// 如果属性值的元数据为空
		if (propertyValueMetaMap == null) {
			// 查询所有属性值
			List<MetaDataCommand> propertyValues = sdkPropertyManager.findPropertyValueMetaDataByLang(lang);
			if (Validator.isNotNullOrEmpty(propertyValues)) {
				propertyValueMetaMap = new LinkedHashMap<Long, MetaDataCommand>();
				for (MetaDataCommand metaDataCommand : propertyValues){
					propertyValueMetaMap.put(metaDataCommand.getId(), metaDataCommand);
				}
			}
		}


		// 把值重新设置到缓存当中
		try{
			cacheManager.setObject(propertyValueMetaCacheKey + lang, propertyValueMetaMap, TimeInterval.SECONDS_PER_HOUR/2);
		}catch (Exception e){
			LOG.error("[SOLR_LOADFACETFILTERMETADATA] cacheManager setObect() error. time:{}", new Date());
		}
		return propertyValueMetaMap;
	}

	@Override
	public Map<Long, SearchConditionCommand> loadSearchConditionMetaData() {
		Map<Long, SearchConditionCommand> searchConditionMetaMap = null;

		String lang = LangUtil.getCurrentLang();

		// 先从缓存中获取数据
		try{
			searchConditionMetaMap = cacheManager.getObject(searchConditionMetaCacheKey + lang);
		}catch (Exception e){
			LOG.error("[SOLR_LOADFACETFILTERMETADATA] cacheManager getObect() error. time:{}", new Date());
		}

		// 如果筛选条件为空
		if (searchConditionMetaMap == null) {
			// 查询所有筛选条件
			List<SearchConditionCommand> searchConditions = sdkSearchConditionManager.findSearchConditionMetDataByLang(lang);
			if (Validator.isNotNullOrEmpty(searchConditions)) {
				searchConditionMetaMap = new LinkedHashMap<Long, SearchConditionCommand>();
				for (SearchConditionCommand searchConditionCommand : searchConditions){
					searchConditionMetaMap.put(searchConditionCommand.getPropertyId(), searchConditionCommand);
				}
			}
		}

		// 把值重新设置到缓存当中
		try{
			cacheManager.setObject(searchConditionMetaCacheKey + lang, searchConditionMetaMap, TimeInterval.SECONDS_PER_HOUR/2);
		}catch (Exception e){
			LOG.error("[SOLR_LOADFACETFILTERMETADATA] cacheManager setObect() error. time:{}", new Date());
		}
		
		return searchConditionMetaMap;
	}
	

	@Override
	public List<FacetGroup> createFilterResult(SearchResultPage<ItemForSolrCommand> searchResultPage,List<FacetParameter> facetParameters){
		FacetFilterMetaData facetFilterMetaData = loadFacetFilterMetaData();
		List<FacetGroup> facetGroups = new ArrayList<FacetGroup>();

		// 分类的facetGroup转换
		for (FacetGroup facetGroup : searchResultPage.getFacetGroups()){
			if (facetGroup.getIsCategory()) {
				// 如果是分类的facet
				List<Facet> facets = new FacetTreeUtil().createFacetTree(facetGroup);
				facets = covertCategoryFacets(facets, facetFilterMetaData.getCategoryMetaMap(), facetParameters);
				facetGroup.setFacets(facets);
				facetGroups.add(facetGroup);				
			}else{
				if(facetGroup.getType()==null){
					facetGroups.add(facetGroup);
				}
			}
		}
		
		//属性
		Map<Long, MetaDataCommand> propertyMetaMap = facetFilterMetaData.getPropertyMetaMap();

		// 属性和价格范围
		Map<Long, SearchConditionCommand> searchConditionMetaMap = facetFilterMetaData.getSearchConditionMetaMap();		
		for (Entry<Long, SearchConditionCommand> entry : searchConditionMetaMap.entrySet()){
			SearchConditionCommand searchConditionCommand = entry.getValue();
			Long propertyId = searchConditionCommand.getPropertyId();

			for (FacetGroup facetGroup : searchResultPage.getFacetGroups()){
				boolean isBreak = false;
				if (!facetGroup.getIsCategory()) {
					// 如果是属性
					if (FacetType.PROPERTY.toString().equals(facetGroup.getType())) {
						if (propertyId != null && propertyId.equals(facetGroup.getId())) {
							
							MetaDataCommand metaDataCommand=propertyMetaMap.get(propertyId);
							//如果是多选或单选
							if(Property.EDITING_TYPE_MULTI_SELECT.equals(metaDataCommand.getEditingType())
									||Property.EDITING_TYPE_RADIA.equals(metaDataCommand.getEditingType())){
								facetGroup = covertPropertyFacetGroup(facetGroup, facetFilterMetaData, facetParameters);
							}else{
								//其他类型的
								facetGroup=covertCustomPropertyFacetGroup(facetGroup, facetFilterMetaData, facetParameters);
							}
							facetGroups.add(facetGroup);
							isBreak = true;
							
						}
					}else if (FacetType.RANGE.toString().equals(facetGroup.getType())) {
						// 价格范围
						if (propertyId == null && SearchCondition.SALE_PRICE_TYPE.equals(searchConditionCommand.getType())) {
							covertPriceAreaFacetGroup(facetGroup, facetFilterMetaData, facetParameters);
							facetGroups.add(facetGroup);
							isBreak = true;
						}
					}

					if (isBreak) {
						// facet的排序
						List<Facet> facets = facetGroup.getFacets();
						
						if(facets!=null){
							Collections.sort(facets, new PropertyComparator<Facet>("sortNo"));
							facetGroup.setFacets(facets);
						}
						break;
					}
				}
			}
		}

		return facetGroups;
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
	private List<Facet> covertCategoryFacets(
			List<Facet> facets,
			Map<Long, MetaDataCommand> categoryMetaMap,
			List<FacetParameter> facetParameters){
		if (Validator.isNotNullOrEmpty(categoryMetaMap)) {
			for (Facet facet : facets){
				// 设置显示文案和顺序
				MetaDataCommand metaDataCommand = categoryMetaMap.get(facet.getId());
				if (metaDataCommand != null) {
					facet.setTitle(metaDataCommand.getName());
					facet.setSortNo(metaDataCommand.getSortNo());
				}

				// 判断是否选中
				for (FacetParameter facetParameter : facetParameters){
					if (FacetType.CATEGORY.equals(facetParameter.getFacetType())) {
						if (facetParameter.containsValue(facet.getId().toString())) {
							facet.setSelected(true);
							break;
						}
					}
				}

				List<Facet> list = facet.childrens;
				if (Validator.isNotNullOrEmpty(list)) {
					covertCategoryFacets(list, categoryMetaMap, facetParameters);
				}
			}
		}

		return facets;

	}

	/**
	 * 转换属性的facetGroup
	 * 
	 * @return FacetGroup
	 * @param facetGroup
	 * @param facetFilterMetaData
	 * @author 冯明雷
	 * @time 2016年4月28日下午6:17:47
	 */
	private FacetGroup covertPropertyFacetGroup(
			FacetGroup facetGroup,
			FacetFilterMetaData facetFilterMetaData,
			List<FacetParameter> facetParameters){
		Map<Long, MetaDataCommand> propertyValueMetaMap = facetFilterMetaData.getPropertyValueMetaMap();
		Map<Long, SearchConditionCommand> searchConditionMetaMap = facetFilterMetaData.getSearchConditionMetaMap();

		SearchConditionCommand searchObj = searchConditionMetaMap.get(facetGroup.getId());
		if (searchObj != null)
			facetGroup.setTitle(searchObj.getName());

		List<Facet> facets = facetGroup.getFacets();
		if (facets != null && facets.size() > 0) {
			for (Facet facet : facets){
				MetaDataCommand propertyValueObj = propertyValueMetaMap.get(Long.valueOf(facet.getValue()));
				if (propertyValueObj != null) {
					facet.setTitle(propertyValueObj.getName());
					facet.setSortNo(propertyValueObj.getSortNo());
				}

				// 判断是否选中
				for (FacetParameter facetParameter : facetParameters){
					if (FacetType.PROPERTY.equals(facetParameter.getFacetType())) {
						if (facetParameter.containsValue(facet.getValue())) {
							facet.setSelected(true);
							break;
						}
					}
				}
			}
		}

		facetGroup.setFacets(facets);

		return facetGroup;
	}
	
	/**
	 * 转换自定义属性值的facetGroup
	 * @return FacetGroup
	 * @param facetGroup
	 * @param facetFilterMetaData
	 * @param facetParameters
	 * @author 冯明雷
	 * @time 2016年5月20日下午3:56:44
	 */
	private FacetGroup covertCustomPropertyFacetGroup(
			FacetGroup facetGroup,
			FacetFilterMetaData facetFilterMetaData,
			List<FacetParameter> facetParameters){
		
		Map<Long, SearchConditionCommand> searchConditionMetaMap = facetFilterMetaData.getSearchConditionMetaMap();

		SearchConditionCommand searchObj = searchConditionMetaMap.get(facetGroup.getId());
		if (searchObj != null)
			facetGroup.setTitle(searchObj.getName());

		List<Facet> facets = facetGroup.getFacets();
		if (facets != null && facets.size() > 0) {
			for (Facet facet : facets){
				facet.setTitle(facet.getValue());
				// 判断是否选中
				for (FacetParameter facetParameter : facetParameters){
					if (FacetType.PROPERTY.equals(facetParameter.getFacetType())) {
						if (facetParameter.containsValue(facet.getValue())) {
							facet.setSelected(true);
							break;
						}
					}
				}
			}
		}
		facetGroup.setFacets(facets);
		return facetGroup;
	}
	
	

	/**
	 * 转换价格范围的facetGroup
	 * 
	 * @return FacetGroup
	 * @param facetGroup
	 * @param facetFilterMetaData
	 * @author 冯明雷
	 * @time 2016年4月28日下午6:17:47
	 */
	private FacetGroup covertPriceAreaFacetGroup(
			FacetGroup facetGroup,
			FacetFilterMetaData facetFilterMetaData,
			List<FacetParameter> facetParameters){
		Map<Long, SearchConditionCommand> searchConditionMetaMap = facetFilterMetaData.getSearchConditionMetaMap();

		SearchConditionCommand searchObj=null;		
		for (Entry<Long,SearchConditionCommand> entry : searchConditionMetaMap.entrySet()){
			SearchConditionCommand searchConditionCommand=entry.getValue();
			if(SearchCondition.SALE_PRICE_TYPE.equals(searchConditionCommand.getType())){
				searchObj=searchConditionCommand;
				break;
			}
			
		}
		
		if (searchObj != null) {
			facetGroup.setTitle(searchObj.getName());
		}else{
			return facetGroup;
		}

		List<SearchConditionItemCommand> searchConditionItemCommands = searchManager
				.findCoditionItemByCoditionIdWithCache(searchObj.getId());
		if (Validator.isNullOrEmpty(searchConditionItemCommands)) {
			return facetGroup;
		}

		List<Facet> facets = facetGroup.getFacets();
		if (facets != null && facets.size() > 0) {
			for (Facet facet : facets){
				for (SearchConditionItemCommand scItemCmd : searchConditionItemCommands){
					StringBuilder sb = new StringBuilder();

					Integer min = scItemCmd.getAreaMin();
					Integer max = scItemCmd.getAreaMax();
					if (null != min && null != max && min <= max) {
						String areaStr = FilterUtil.paramConverToArea(min.toString(), max.toString());
						sb.append(SkuItemParam.sale_price).append(":").append(areaStr);
					}

					String value = facet.getValue();
					if (sb.toString().equals(value)) {
						facet.setTitle(scItemCmd.getName());
						facet.setSortNo(scItemCmd.getSort());
						// 判断是否选中
						for (FacetParameter facetParameter : facetParameters){
							if (FacetType.RANGE.equals(facetParameter.getFacetType())) {
								value = value.replace(SkuItemParam.sale_price + ":", "");
								if (facetParameter.containsValue(value)) {
									facet.setSelected(true);
									break;
								}
							}
						}

					}
				}
			}
		}

		facetGroup.setFacets(facets);

		return facetGroup;
	}

}
