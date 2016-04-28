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
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.baozun.nebula.manager.CacheManager;
import com.baozun.nebula.sdk.command.SearchConditionCommand;
import com.baozun.nebula.sdk.manager.SdkSearchConditionManager;
import com.baozun.nebula.search.Boost;
import com.baozun.nebula.search.Facet;
import com.baozun.nebula.search.FacetGroup;
import com.baozun.nebula.search.command.SearchResultPage;
import com.baozun.nebula.solr.Param.SkuItemParam;
import com.baozun.nebula.solr.command.ItemForSolrI18nCommand;
import com.baozun.nebula.solr.command.SolrGroup;
import com.baozun.nebula.solr.command.SolrGroupCommand;
import com.baozun.nebula.solr.command.SolrGroupData;
import com.baozun.nebula.solr.manager.SolrManager;
import com.baozun.nebula.solr.manager.SolrManagerImpl;
import com.feilong.core.Validator;

@Transactional
@Service("searchManager")
public class SearchManagerImpl implements SearchManager{

	private static final Logger			LOG					= LoggerFactory.getLogger(SolrManagerImpl.class);

	private final static String			conditionCacheKey	= "findConditionByCategoryIdListKey";

	@Autowired
	private SdkSearchConditionManager	sdkSearchConditionManager;

	@Autowired
	private CacheManager				cacheManager;

	@Autowired
	private SolrManager					solrManager;

	@SuppressWarnings("unchecked")
	@Override
	public SearchResultPage<ItemForSolrI18nCommand> search(SolrQuery solrQuery){
		LOG.debug(solrQuery.toString());

		// 多少行
		Integer rows = solrQuery.getRows();
		// 第几页
		Integer currentPage = solrQuery.getStart();

		SearchResultPage<ItemForSolrI18nCommand> searchResultPage = null;
		SolrGroupData<ItemForSolrI18nCommand> solrGroup = new SolrGroupData<ItemForSolrI18nCommand>();

		// 是否分组显示
		String isGroup = solrQuery.get(GroupParams.GROUP);

		// 如果需要分组显示
		if (Validator.isNotNullOrEmpty(isGroup) && Boolean.parseBoolean(isGroup)) {
			solrGroup = solrManager.findItemCommandFormSolrBySolrQueryWithGroup(solrQuery);
			searchResultPage = solrGroupConverterSearchResultPageWithGroup(solrGroup, currentPage, rows);
		}else{
			solrGroup = solrManager.findItemCommandFormSolrBySolrQueryWithOutGroup(solrQuery);
			searchResultPage = solrGroupConverterSearchResultPageWithOutGroup(solrGroup, currentPage, rows);
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
				cacheManager.setObject(conditionCacheKey, searchConditionCommands);
			}catch (Exception e){
				LOG.error("[SOLR_SEARCH_SEARCHCONDITION] cacheManager setObject() error. time:{}", new Date());
			}

		}
		return searchConditionCommands;
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
	private SearchResultPage<ItemForSolrI18nCommand> solrGroupConverterSearchResultPageWithGroup(
			SolrGroupData<ItemForSolrI18nCommand> solrGroupData,
			Integer currentPage,
			Integer size){

		List<ItemForSolrI18nCommand> list = new ArrayList<ItemForSolrI18nCommand>();
		Map<String, SolrGroupCommand<ItemForSolrI18nCommand>> it = solrGroupData.getSolrGroupCommandMap();
		for (String key : it.keySet()){
			SolrGroupCommand<ItemForSolrI18nCommand> solrGroupCommand = it.get(key);
			List<SolrGroup<ItemForSolrI18nCommand>> solrGroupList = solrGroupCommand.getItemForSolrCommandList();
			for (SolrGroup<ItemForSolrI18nCommand> solrGroup : solrGroupList){
				list.addAll(solrGroup.getBeans());
			}
		}

		Integer start = (currentPage - 1) * size;

		return convertSearchPageFacet(
				start,
				size,
				solrGroupData.getNumFound(),
				list,
				solrGroupData.getFacetQueryMap(),
				solrGroupData.getFacetMap());
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
	private SearchResultPage<ItemForSolrI18nCommand> solrGroupConverterSearchResultPageWithOutGroup(
			SolrGroupData<ItemForSolrI18nCommand> solrGroupData,
			Integer currentPage,
			Integer size){
		List<ItemForSolrI18nCommand> list = new ArrayList<ItemForSolrI18nCommand>();

		List<ItemForSolrI18nCommand> it = solrGroupData.getSolrCommandMap();
		if (null != it && it.size() > 0) {
			list.addAll(it);
		}

		Integer start = (currentPage - 1) * size;
		Long count = Long.parseLong(list.size() + "");

		return convertSearchPageFacet(start, size, count, list, solrGroupData.getFacetQueryMap(), solrGroupData.getFacetMap());
	}

	/**
	 * 转换facetMap
	 * 
	 * @return searchResultPage
	 * @param solrGroupData
	 * @param size
	 * @param searchResultPage
	 * @param start
	 * @param pagination
	 * @author 冯明雷
	 * @time 2016年4月28日上午11:02:29
	 */
	private SearchResultPage<ItemForSolrI18nCommand> convertSearchPageFacet(
			Integer start,
			Integer rows,
			Long numFound,
			List<ItemForSolrI18nCommand> items,
			Map<String, Integer> facetQueryMap,
			Map<String, Map<String, Long>> facetMap){

		SearchResultPage<ItemForSolrI18nCommand> searchResultPage = new SearchResultPage<ItemForSolrI18nCommand>();
		searchResultPage.setItems(items);
		searchResultPage.setCurrentPage(rows == 0 ? 1 : (start / rows + 1));
		searchResultPage.setCount(numFound);
		searchResultPage.setSize(rows);
		searchResultPage.setStart(start);
		searchResultPage.setTotalPages(rows == 0 ? 0 : numFound.intValue() / rows + (numFound.intValue() % rows == 0 ? 0 : 1));

		List<FacetGroup> facetGroups = new ArrayList<FacetGroup>();

		for (Entry<String, Map<String, Long>> entry : facetMap.entrySet()){
			String key = entry.getKey();
			Map<String, Long> valueMap = entry.getValue();

			FacetGroup facetGroup = new FacetGroup();
			// 如果key等于category_tree代表是分类的facet
			if (SkuItemParam.category_tree.equals(key)) {
				facetGroup = convertFacetGroup(valueMap);
				facetGroup.setCategory(true);
			}else{
				// 否则是属性的facet
				facetGroup = convertFacetGroup(valueMap);
				facetGroup.setCategory(false);
				facetGroup.setId(Long.valueOf(entry.getKey()));
			}

			facetGroups.add(facetGroup);
		}

		searchResultPage.setFacetGroups(facetGroups);

		return searchResultPage;
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

}
