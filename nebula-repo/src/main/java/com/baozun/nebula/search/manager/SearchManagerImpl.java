package com.baozun.nebula.search.manager;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import org.apache.solr.client.solrj.SolrQuery;
import org.apache.solr.common.params.DisMaxParams;
import org.apache.solr.common.params.GroupParams;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.baozun.nebula.constant.CacheKeyConstant;
import com.baozun.nebula.exception.BusinessException;
import com.baozun.nebula.manager.CacheManager;
import com.feilong.core.TimeInterval;
import com.baozun.nebula.sdk.command.SearchConditionCommand;
import com.baozun.nebula.sdk.command.SearchConditionItemCommand;
import com.baozun.nebula.sdk.manager.SdkSearchConditionItemManager;
import com.baozun.nebula.sdk.manager.SdkSearchConditionManager;
import com.baozun.nebula.search.Boost;
import com.baozun.nebula.search.Facet;
import com.baozun.nebula.search.FacetFilterHelper;
import com.baozun.nebula.search.FacetGroup;
import com.baozun.nebula.search.FacetType;
import com.baozun.nebula.search.command.MetaDataCommand;
import com.baozun.nebula.search.command.SearchResultPage;
import com.baozun.nebula.solr.Param.SkuItemParam;
import com.baozun.nebula.solr.command.ItemForSolrCommand;
import com.baozun.nebula.solr.command.SolrGroup;
import com.baozun.nebula.solr.command.SolrGroupCommand;
import com.baozun.nebula.solr.command.SolrGroupData;
import com.baozun.nebula.solr.manager.SolrManager;
import com.baozun.nebula.solr.manager.SolrManagerImpl;
import com.baozun.nebula.utilities.common.LangUtil;
import com.feilong.core.Validator;

import loxia.dao.Pagination;

@Transactional
@Service("searchManager")
public class SearchManagerImpl implements SearchManager{

	private static final Logger				LOG						= LoggerFactory.getLogger(SolrManagerImpl.class);

	private final static String				conditionCacheKey		= "findConditionByCategoryIdListKey";
	
	private final static String				conditionItemCacheKey	= "findCoditionItemByCoditionIdtKey";

	@Autowired
	private SdkSearchConditionManager		sdkSearchConditionManager;

	@Autowired
	private SdkSearchConditionItemManager	sdkSearchConditionItemManager;

	@Autowired
	private CacheManager					cacheManager;

	@Autowired
	private SolrManager						solrManager;
	
	@Autowired(required=false)
	@Qualifier("facetFilterHelper")
	private FacetFilterHelper				facetFilterHelper;

	@SuppressWarnings("unchecked")
	@Override
	public SearchResultPage<ItemForSolrCommand> search(SolrQuery solrQuery){
		LOG.debug(solrQuery.toString());

		// 多少行
		Integer rows = solrQuery.getRows();
		// 第几页
		Integer start = solrQuery.getStart();

		// 返回对象
		SearchResultPage<ItemForSolrCommand> searchResultPage = null;

		SolrGroupData<ItemForSolrCommand> solrGroup = new SolrGroupData<ItemForSolrCommand>();

		// 是否分组显示
		String isGroup = solrQuery.get(GroupParams.GROUP);

		// 如果需要分组显示
		if (Validator.isNotNullOrEmpty(isGroup) && Boolean.parseBoolean(isGroup)) {
			// 分组查询
			solrGroup = solrManager.findItemCommandFormSolrBySolrQueryWithGroup(solrQuery);

			// 将查询结果转换为searchResultPage对象
			searchResultPage = solrGroupConverterSearchResultPageWithGroup(solrGroup, start, rows);
		}else{
			// 不分组查询
			solrGroup = solrManager.findItemCommandFormSolrBySolrQueryWithOutGroup(solrQuery);

			// 将查询结果转换为searchResultPage对象
			searchResultPage = solrGroupConverterSearchResultPageWithOutGroup(solrGroup, start, rows);
		}

		return searchResultPage;

	}

	@Override
	public void setSolrBoost(SolrQuery solrQuery,Boost boost){
		if (Validator.isNotNullOrEmpty(boost.getDeftype())) {
			solrQuery.set("defType", boost.getDeftype());
		}
		if (Validator.isNotNullOrEmpty(boost.getBq())) {
			solrQuery.set(DisMaxParams.BQ, boost.getBq());
		}
		if (Validator.isNotNullOrEmpty(boost.getQf())) {
			solrQuery.set(DisMaxParams.QF, boost.getQf());
		}
		if (Validator.isNotNullOrEmpty(boost.getPf())) {
			solrQuery.set(DisMaxParams.PF, boost.getPf());
		}
		if (Validator.isNotNullOrEmpty(boost.getBf())) {
			solrQuery.set(DisMaxParams.BF, boost.getBf());
		}

	}

	@Override
	public List<SearchConditionCommand> findConditionByCategoryIdsWithCache(List<Long> categoryIds){
		List<SearchConditionCommand> searchConditionCommands = null;

		try{
			searchConditionCommands = cacheManager.getObject(conditionCacheKey);
		}catch (Exception e){
			LOG.error("[SOLR_SEARCH_SEARCHCONDITION] cacheManager getObect() error. time:{}", new Date());
		}

		if (Validator.isNullOrEmpty(searchConditionCommands)) {
			searchConditionCommands = sdkSearchConditionManager.findConditionByCategoryIdList(categoryIds);

			try{
				cacheManager.setObject(conditionCacheKey, searchConditionCommands, TimeInterval.SECONDS_PER_DAY);
			}catch (Exception e){
				LOG.error("[SOLR_SEARCH_SEARCHCONDITION] cacheManager setObject() error. time:{}", new Date());
			}

		}
		return searchConditionCommands;
	}

	@Override
	public List<SearchConditionItemCommand> findCoditionItemByCoditionIdWithCache(Long coditionId){
		List<SearchConditionItemCommand> searchConditionItemCommands = null;

		String lang = LangUtil.getCurrentLang();

		String key = conditionItemCacheKey + coditionId + "-" + lang;

		try{
			searchConditionItemCommands = cacheManager.getObject(key);
		}catch (Exception e){
			LOG.error("[SOLR_SEARCH_SEARCHCONDITION] cacheManager getObect() error. time:{}", new Date());
		}

		if (Validator.isNullOrEmpty(searchConditionItemCommands)) {
			searchConditionItemCommands = sdkSearchConditionItemManager.findItemBySIdAndLang(coditionId, lang);

			try{
				cacheManager.setObject(key, searchConditionItemCommands, TimeInterval.SECONDS_PER_DAY);
			}catch (Exception e){
				LOG.error("[SOLR_SEARCH_SEARCHCONDITION] cacheManager setObject() error. time:{}", new Date());
			}

		}
		return searchConditionItemCommands;
	}

	/**
	 * 将solrGroup转换为SearchResultPage(分组显示)
	 * 
	 * @return SearchResultPage<ItemForSolrCommand>
	 * @param solrGroupData
	 * @param currentPage
	 * @param size
	 * @author 冯明雷
	 * @time 2016年4月26日下午3:58:16
	 */
	private SearchResultPage<ItemForSolrCommand> solrGroupConverterSearchResultPageWithGroup(
			SolrGroupData<ItemForSolrCommand> solrGroupData,
			Integer start,
			Integer rows){
		Long numFound = solrGroupData.getNumFound();

		Pagination<List<ItemForSolrCommand>> pagination = new Pagination<List<ItemForSolrCommand>>();
		List<List<ItemForSolrCommand>> items = new ArrayList<List<ItemForSolrCommand>>();

		Map<String, SolrGroupCommand<ItemForSolrCommand>> it = solrGroupData.getSolrGroupCommandMap();
		for (String key : it.keySet()){
			SolrGroupCommand<ItemForSolrCommand> solrGroupCommand = it.get(key);
			List<SolrGroup<ItemForSolrCommand>> solrGroupList = solrGroupCommand.getItemForSolrCommandList();
			for (SolrGroup<ItemForSolrCommand> solrGroup : solrGroupList){
				items.add(solrGroup.getBeans());
			}
		}
		pagination.setItems(items);
		pagination.setCurrentPage(rows == 0 ? 1 : (start / rows + 1));
		pagination.setCount(numFound);
		pagination.setSize(rows);
		pagination.setStart(start);
		pagination.setTotalPages(rows == 0 ? 0 : numFound.intValue() / rows + (numFound.intValue() % rows == 0 ? 0 : 1));
		List<FacetGroup> facetGroups = getFacetGroups(solrGroupData);

		SearchResultPage<ItemForSolrCommand> searchResultPage = new SearchResultPage<ItemForSolrCommand>();
		searchResultPage.setItemsListWithGroup(pagination);
		searchResultPage.setFacetGroups(facetGroups);
		searchResultPage.setFieldStatsInfo(solrGroupData.getFieldStatsInfo());
		return searchResultPage;

	}

	/**
	 * 将solrGroup转换为SearchResultPage(不分组显示)
	 * 
	 * @return SearchResultPage<ItemForSolrCommand>
	 * @param solrGroupData
	 * @param currentPage
	 * @param size
	 * @author 冯明雷
	 * @time 2016年4月26日下午3:58:16
	 */
	private SearchResultPage<ItemForSolrCommand> solrGroupConverterSearchResultPageWithOutGroup(
			SolrGroupData<ItemForSolrCommand> solrGroupData,
			Integer start,
			Integer rows){
		Long numFound = solrGroupData.getNumFound();

		Pagination<ItemForSolrCommand> pagination = new Pagination<ItemForSolrCommand>();
		List<ItemForSolrCommand> items = new ArrayList<ItemForSolrCommand>();
		List<ItemForSolrCommand> it = solrGroupData.getSolrCommandMap();
		if (null != it && it.size() > 0) {
			items.addAll(it);
		}
		pagination.setItems(items);
		pagination.setCurrentPage(rows == 0 ? 1 : (start / rows + 1));
		pagination.setCount(numFound);
		pagination.setSize(rows);
		pagination.setStart(start);
		pagination.setTotalPages(rows == 0 ? 0 : numFound.intValue() / rows + (numFound.intValue() % rows == 0 ? 0 : 1));

		List<FacetGroup> facetGroups = getFacetGroups(solrGroupData);

		SearchResultPage<ItemForSolrCommand> searchResultPage = new SearchResultPage<ItemForSolrCommand>();
		searchResultPage.setItemsListWithOutGroup(pagination);
		searchResultPage.setFacetGroups(facetGroups);
		searchResultPage.setFieldStatsInfo(solrGroupData.getFieldStatsInfo());
		return searchResultPage;
	}

	/**
	 * 转换facetGroup
	 * 
	 * @return List<FacetGroup>
	 * @param solrGroupData
	 * @author 冯明雷
	 * @time 2016年5月17日下午6:30:13
	 */
	private List<FacetGroup> getFacetGroups(SolrGroupData<ItemForSolrCommand> solrGroupData){
		Map<String, Integer> facetQueryMap = solrGroupData.getFacetQueryMap();
		Map<String, Map<String, Long>> facetMap = solrGroupData.getFacetMap();

		List<FacetGroup> facetGroups = new ArrayList<FacetGroup>();

		// ****************************属性和分类的facet
		if (Validator.isNotNullOrEmpty(facetMap)) {
			for (Entry<String, Map<String, Long>> entry : facetMap.entrySet()){
				String key = entry.getKey();
				Map<String, Long> valueMap = entry.getValue();

				FacetGroup facetGroup = new FacetGroup();
				// 如果key等于category_tree代表是分类的facet
				if (SkuItemParam.category_tree.equals(key)) {
					facetGroup = convertFacetGroup(valueMap);
					facetGroup.setIsCategory(true);
					facetGroup.setType(FacetType.CATEGORY.toString());
				}else if(key.indexOf(SkuItemParam.dynamicCondition)!=-1){
					boolean isProperty=false;
					
					if(facetFilterHelper==null){
						throw new BusinessException("Injection of failure,the facetFilterHelper is null");
					}
					
					//所有属性
					Map<Long, MetaDataCommand> propertyMap=facetFilterHelper.loadPropertyMetaData();
					if(Validator.isNotNullOrEmpty(propertyMap)){
						for (Long id : propertyMap.keySet()){
							String str=SkuItemParam.dynamicCondition+id.toString();
							//属性
							if(key.equals(str)){
								isProperty=true;
								break;
							}
						}
					}
					
					if(isProperty){
						// 属性的facet
						facetGroup = convertFacetGroup(valueMap);
						facetGroup.setIsCategory(false);
						facetGroup.setType(FacetType.PROPERTY.toString());
						facetGroup.setId(Long.valueOf(key.replace(SkuItemParam.dynamicCondition, "")));
					}else{
						facetGroup = convertFacetGroup(valueMap);
						facetGroup.setIsCategory(false);
					}
				}else{
					facetGroup = convertFacetGroup(valueMap);
					facetGroup.setIsCategory(false);
				}
				facetGroups.add(facetGroup);
			}
		}

		// **************************价格范围的facetGroup
		if (Validator.isNotNullOrEmpty(facetQueryMap)) {
			FacetGroup priceGroup = new FacetGroup();
			priceGroup.setIsCategory(false);
			priceGroup.setType(FacetType.RANGE.toString());
			List<Facet> facets = new ArrayList<Facet>();
			for (Entry<String, Integer> entry : facetQueryMap.entrySet()){
				String key = entry.getKey();
				Integer count = entry.getValue();
				if (key.indexOf(SkuItemParam.sale_price) != -1) {
					Facet facet = new Facet();
					facet.setValue(key);
					facet.setCount(count);
					facets.add(facet);
				}
			}
			if (facets.size() > 0) {
				priceGroup.setFacets(facets);
				facetGroups.add(priceGroup);
			}
		}
		return facetGroups;
	}

	/**
	 * 转换FacetGroup
	 * 
	 * @return List<FacetGroup>
	 * @param valueMap
	 * @author 冯明雷
	 * @time 2016年4月28日下午2:04:18
	 */
	private FacetGroup convertFacetGroup(Map<String, Long> valueMap){
		FacetGroup facetGroup = new FacetGroup();

		List<Facet> facets = new ArrayList<Facet>();
		for (Entry<String, Long> entry : valueMap.entrySet()){
			Facet facet = new Facet();
			facet.setValue(entry.getKey());
			facet.setCount(entry.getValue());
			facets.add(facet);
		}
		facetGroup.setFacets(facets);
		return facetGroup;
	}

	@Override
	public List<SearchConditionCommand> findConditionByNavigationWithCache(Long navigationId) {
		List<SearchConditionCommand> searchConditionCommands = null;

		navigationId = Validator.isNullOrEmpty(navigationId)?-1L:navigationId;
		
		try{
			searchConditionCommands = cacheManager.getObject(CacheKeyConstant.CONDITION_NAV_CACHEKEY+navigationId);
		}catch (Exception e){
			LOG.error("[SOLR_SEARCH_SEARCHCONDITION] cacheManager getObect() error. time:{}", new Date());
		}

		if (Validator.isNullOrEmpty(searchConditionCommands)) {
			
			
			searchConditionCommands = sdkSearchConditionManager.findConditionByNavigation(navigationId);

			try{
				cacheManager.setObject(CacheKeyConstant.CONDITION_NAV_CACHEKEY+navigationId, searchConditionCommands, TimeInterval.SECONDS_PER_DAY);
			}catch (Exception e){
				LOG.error("[SOLR_SEARCH_SEARCHCONDITION] cacheManager setObject() error. time:{}", new Date());
			}

		}
		return searchConditionCommands;
	}

}
