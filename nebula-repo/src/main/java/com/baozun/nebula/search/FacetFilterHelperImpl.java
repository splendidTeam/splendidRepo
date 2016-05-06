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
import java.util.Arrays;
import java.util.Date;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import org.hibernate.hql.ast.tree.BooleanLiteralNode;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import com.baozun.nebula.manager.CacheManager;
import com.baozun.nebula.manager.TimeInterval;
import com.baozun.nebula.model.product.SearchCondition;
import com.baozun.nebula.sdk.command.SearchConditionCommand;
import com.baozun.nebula.sdk.command.SearchConditionItemCommand;
import com.baozun.nebula.sdk.manager.SdkCategoryManager;
import com.baozun.nebula.sdk.manager.SdkNavigationManager;
import com.baozun.nebula.sdk.manager.SdkSearchConditionItemManager;
import com.baozun.nebula.sdk.manager.SdkSearchConditionManager;
import com.baozun.nebula.sdk.manager.product.SdkPropertyManager;
import com.baozun.nebula.search.command.MetaDataCommand;
import com.baozun.nebula.search.command.SearchResultPage;
import com.baozun.nebula.search.comparatable.FacetComparer;
import com.baozun.nebula.search.manager.SearchManager;
import com.baozun.nebula.solr.Param.SkuItemParam;
import com.baozun.nebula.solr.command.ItemForSolrCommand;
import com.baozun.nebula.solr.utils.FilterUtil;
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

	private static final Logger				LOG								= LoggerFactory.getLogger(FacetFilterHelperImpl.class);

	/** 分类元数据在缓存中的key，完整的key还要加上语言 */
	private final static String				categoryMetaCacheKey			= "categoryMetaCacheKey_";

	/** 属性元数据在缓存中的key，完整的key还要加上语言 */
	private final static String				propertyMetaCacheKey			= "propertyMetaCacheKey_";

	/** 属性值元数据在缓存中的key，完整的key还要加上语言 */
	private final static String				propertyValueMetaCacheKey		= "propertyValueMetaCacheKey_";

	/** 导航元数据在缓存中的key，完整的key还要加上语言 */
	private final static String				navigationMetaCacheKey			= "navigationMetaCacheKey_";

	/** 搜索条件元数据在缓存中的key，完整的key还要加上语言 */
	private final static String				searchConditionMetaCacheKey		= "searchConditionMetaCacheKey_";


	@Autowired
	private CacheManager					cacheManager;

	@Autowired
	private SdkCategoryManager				sdkCategoryManager;

	@Autowired
	private SdkPropertyManager				sdkPropertyManager;

	@Autowired
	private SdkNavigationManager			sdkNavigationManager;

	@Autowired
	private SdkSearchConditionManager		sdkSearchConditionManager;
	
	@Autowired
	private SearchManager					searchManager;

	@Override
	public FacetFilterMetaData loadFacetFilterMetaData(){
		FacetFilterMetaData facetFilterMetaData = new FacetFilterMetaData();

		Map<Long, MetaDataCommand> categoryMetaMap = null;
		Map<Long, MetaDataCommand> propertyMetaMap = null;
		Map<Long, MetaDataCommand> propertyValueMetaMap = null;
		Map<Long, MetaDataCommand> navigationMetaMap = null;
		Map<Long, SearchConditionCommand> searchConditionMetaMap = null;

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
				categoryMetaMap = new LinkedHashMap<Long, MetaDataCommand>();
				for (MetaDataCommand metaDataCommand : allCategorys){
					categoryMetaMap.put(metaDataCommand.getId(), metaDataCommand);
				}

				cacheManager.setObject(categoryMetaCacheKey + lang, categoryMetaMap, TimeInterval.SECONDS_PER_DAY);
			}
		}

		if (propertyMetaMap == null) {
			List<MetaDataCommand> properties = sdkPropertyManager.findPropertyMetaDataByLang(lang);
			if (Validator.isNotNullOrEmpty(properties)) {
				propertyMetaMap = new LinkedHashMap<Long, MetaDataCommand>();
				for (MetaDataCommand metaDataCommand : properties){
					propertyMetaMap.put(metaDataCommand.getId(), metaDataCommand);
				}
				cacheManager.setObject(propertyMetaCacheKey + lang, propertyMetaMap, TimeInterval.SECONDS_PER_DAY);
			}
		}

		if (propertyValueMetaMap == null) {
			List<MetaDataCommand> propertyValues = sdkPropertyManager.findPropertyValueMetaDataByLang(lang);
			if (Validator.isNotNullOrEmpty(propertyValues)) {
				propertyValueMetaMap = new LinkedHashMap<Long, MetaDataCommand>();
				for (MetaDataCommand metaDataCommand : propertyValues){
					propertyValueMetaMap.put(metaDataCommand.getId(), metaDataCommand);
				}
				cacheManager.setObject(propertyValueMetaCacheKey + lang, propertyValueMetaMap, TimeInterval.SECONDS_PER_DAY);
			}
		}

		if (navigationMetaMap == null) {
			List<MetaDataCommand> navigations = sdkNavigationManager.findNavigationMetaDataBylang(lang);
			if (Validator.isNotNullOrEmpty(navigations)) {
				navigationMetaMap = new LinkedHashMap<Long, MetaDataCommand>();
				for (MetaDataCommand metaDataCommand : navigations){
					navigationMetaMap.put(metaDataCommand.getId(), metaDataCommand);
				}

				cacheManager.setObject(navigationMetaCacheKey + lang, navigationMetaMap, TimeInterval.SECONDS_PER_DAY);
			}
		}

		if (searchConditionMetaMap == null) {
			List<SearchConditionCommand> searchConditions = sdkSearchConditionManager.findSearchConditionMetDataByLang(lang);
			if (Validator.isNotNullOrEmpty(searchConditions)) {
				searchConditionMetaMap = new LinkedHashMap<Long, SearchConditionCommand>();
				for (SearchConditionCommand searchConditionCommand : searchConditions){
					searchConditionMetaMap.put(searchConditionCommand.getPropertyId(), searchConditionCommand);
				}
				cacheManager.setObject(searchConditionMetaCacheKey + lang, searchConditionMetaMap, TimeInterval.SECONDS_PER_DAY);
			}
		}

		

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
	public List<FacetGroup> createFilterResult(SearchResultPage<ItemForSolrCommand> searchResultPage,List<FacetParameter> facetParameters){
		FacetFilterMetaData facetFilterMetaData = loadFacetFilterMetaData();		
		List<FacetGroup> facetGroups = new ArrayList<FacetGroup>();	
		
		//分类的facetGroup转换
		for (FacetGroup facetGroup : searchResultPage.getFacetGroups()){
			if (facetGroup.isCategory()) {
				// 如果是分类的facet
				List<Facet> facets = FacetTreeUtil.createFacetTree(facetGroup);
				facets=covertCategoryFacets(facets, facetFilterMetaData.getCategoryMetaMap(),facetParameters);
				facetGroup.setFacets(facets);
				facetGroups.add(facetGroup);
				break;
			}
		}
		
		//属性和价格范围
		Map<Long, SearchConditionCommand> searchConditionMetaMap=facetFilterMetaData.getSearchConditionMetaMap();
		for (Entry<Long, SearchConditionCommand> entry : searchConditionMetaMap.entrySet()){
			SearchConditionCommand searchConditionCommand=entry.getValue();
			Long propertyId=searchConditionCommand.getPropertyId();
			
			for (FacetGroup facetGroup : searchResultPage.getFacetGroups()){
				boolean isBreak=false;
				if (!facetGroup.isCategory()) {
					//如果是属性
					if(FacetType.PROPERTY.toString().equals(facetGroup.getType())){
						if(propertyId!=null&&propertyId.equals(facetGroup.getId())){
							facetGroup = covertPropertyFacetGroup(facetGroup, facetFilterMetaData,facetParameters);
							facetGroups.add(facetGroup);
							isBreak=true;
						}
					}else if(FacetType.RANGE.toString().equals(facetGroup.getType())){
						//价格范围
						if(propertyId==null&&SearchCondition.SALE_PRICE_TYPE.equals(searchConditionCommand.getType())){
							covertPriceAreaFacetGroup(facetGroup, facetFilterMetaData,facetParameters);
							facetGroups.add(facetGroup);
							isBreak=true;							
						}						
					}
					
					if(isBreak){
						//facet的排序
						List<Facet> facets = facetGroup.getFacets();
						Facet[] inputs = new Facet[facets.size()];
						facets.toArray(inputs);						
						Arrays.sort(inputs, new FacetComparer());
						
						facetGroup.setFacets(Arrays.asList(inputs));
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
	private List<Facet> covertCategoryFacets(List<Facet> facets,Map<Long, MetaDataCommand> categoryMetaMap,List<FacetParameter> facetParameters){
		if (Validator.isNotNullOrEmpty(categoryMetaMap)) {
			for (Facet facet : facets){
				//设置显示文案和顺序
				MetaDataCommand metaDataCommand = categoryMetaMap.get(facet.getId());
				if (metaDataCommand != null) {
					facet.setTitle(metaDataCommand.getName());
					facet.setSortNo(metaDataCommand.getSortNo());
				}
				
				//判断是否选中
				for (FacetParameter facetParameter : facetParameters){
					if(FacetType.CATEGORY.equals(facetParameter.getFacetType())){
						if(facetParameter.containsValue(facet.getValue())){
							facet.setSelected(true);
							break;
						}
					}
				}

				List<Facet> list = facet.childrens;
				if (Validator.isNotNullOrEmpty(list)) {
					covertCategoryFacets(list, categoryMetaMap,facetParameters);
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
	private FacetGroup covertPropertyFacetGroup(FacetGroup facetGroup,FacetFilterMetaData facetFilterMetaData,List<FacetParameter> facetParameters){
		Map<Long, MetaDataCommand> propertyValueMetaMap = facetFilterMetaData.getPropertyValueMetaMap();
		Map<Long, SearchConditionCommand> searchConditionMetaMap = facetFilterMetaData.getSearchConditionMetaMap();

		SearchConditionCommand searchObj = searchConditionMetaMap.get(facetGroup.getId().toString());
		if (searchObj != null)
			facetGroup.setTitle(searchObj.getName());

		List<Facet> facets = facetGroup.getFacets();
		if (facets != null && facets.size() > 0) {
			for (Facet facet : facets){
				MetaDataCommand propertyValueObj = propertyValueMetaMap.get(facet.getId());
				if (propertyValueObj != null){
					facet.setTitle(propertyValueObj.getName());
					facet.setSortNo(propertyValueObj.getSortNo());
				}
				
				//判断是否选中
				for (FacetParameter facetParameter : facetParameters){
					if(FacetType.PROPERTY.equals(facetParameter.getFacetType())){
						if(facetParameter.containsValue(facet.getValue())){
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
	private FacetGroup covertPriceAreaFacetGroup(FacetGroup facetGroup,FacetFilterMetaData facetFilterMetaData,List<FacetParameter> facetParameters){
		Map<Long, SearchConditionCommand> searchConditionMetaMap = facetFilterMetaData.getSearchConditionMetaMap();
		
		SearchConditionCommand searchObj = searchConditionMetaMap.get(facetGroup.getId().toString());
		if (searchObj != null){
			facetGroup.setTitle(searchObj.getName());
		}else{
			return facetGroup;
		}

		
		List<SearchConditionItemCommand> searchConditionItemCommands=searchManager.findCoditionItemByCoditionIdWithCache(searchObj.getId());
		if(Validator.isNullOrEmpty(searchConditionItemCommands)){
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
					if(sb.toString().equals(value)){
						facet.setTitle(scItemCmd.getName());
						facet.setSortNo(scItemCmd.getSort());
						//判断是否选中
						for (FacetParameter facetParameter : facetParameters){
							if(FacetType.RANGE.equals(facetParameter.getFacetType())){
								value=value.replace(SkuItemParam.sale_price+":","");
								if(facetParameter.containsValue(value)){
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
