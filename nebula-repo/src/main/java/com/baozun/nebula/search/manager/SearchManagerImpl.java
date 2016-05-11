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
import com.baozun.nebula.manager.TimeInterval;
import com.baozun.nebula.sdk.command.SearchConditionCommand;
import com.baozun.nebula.sdk.command.SearchConditionItemCommand;
import com.baozun.nebula.sdk.manager.SdkSearchConditionItemManager;
import com.baozun.nebula.sdk.manager.SdkSearchConditionManager;
import com.baozun.nebula.search.Boost;
import com.baozun.nebula.search.Facet;
import com.baozun.nebula.search.FacetGroup;
import com.baozun.nebula.search.FacetType;
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

	@SuppressWarnings("unchecked")
	@Override
	public SearchResultPage<ItemForSolrCommand> search(SolrQuery solrQuery){
		LOG.debug(solrQuery.toString());

		// 多少行
		Integer rows = solrQuery.getRows();
		// 第几页
		Integer currentPage = solrQuery.getStart();

		//返回对象
		SearchResultPage<ItemForSolrCommand> searchResultPage = null;
		
		SolrGroupData<ItemForSolrCommand> solrGroup = new SolrGroupData<ItemForSolrCommand>();

		// 是否分组显示
		String isGroup = solrQuery.get(GroupParams.GROUP);

		// 如果需要分组显示
		if (Validator.isNotNullOrEmpty(isGroup) && Boolean.parseBoolean(isGroup)) {
			//分组查询
			solrGroup = solrManager.findItemCommandFormSolrBySolrQueryWithGroup(solrQuery);
			
			//将查询结果转换为searchResultPage对象
			searchResultPage = solrGroupConverterSearchResultPageWithGroup(solrGroup, currentPage, rows);
		}else{
			//不分组查询
			solrGroup = solrManager.findItemCommandFormSolrBySolrQueryWithOutGroup(solrQuery);
			
			//将查询结果转换为searchResultPage对象
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

		String key = conditionItemCacheKey+coditionId+"-"+lang;
		
		try{
			searchConditionItemCommands = cacheManager.getObject(key);
		}catch (Exception e){
			LOG.error("[SOLR_SEARCH_SEARCHCONDITION] cacheManager getObect() error. time:{}", new Date());
		}

		if (Validator.isNullOrEmpty(searchConditionItemCommands)) {
			searchConditionItemCommands = sdkSearchConditionItemManager.findItemBySIdAndLang(coditionId,lang);

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
			Integer currentPage,
			Integer size){

		List<ItemForSolrCommand> list = new ArrayList<ItemForSolrCommand>();
		
		Map<String, SolrGroupCommand<ItemForSolrCommand>> it = solrGroupData.getSolrGroupCommandMap();
		for (String key : it.keySet()){
			SolrGroupCommand<ItemForSolrCommand> solrGroupCommand = it.get(key);
			List<SolrGroup<ItemForSolrCommand>> solrGroupList = solrGroupCommand.getItemForSolrCommandList();
			for (SolrGroup<ItemForSolrCommand> solrGroup : solrGroupList){
				list.addAll(solrGroup.getBeans());
			}
		}

		Integer start = (currentPage - 1) * size;

		return convertSearchPageFacet(start,size,solrGroupData.getNumFound(),list,solrGroupData.getFacetQueryMap(),solrGroupData.getFacetMap());
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
			Integer currentPage,
			Integer size){
		List<ItemForSolrCommand> list = new ArrayList<ItemForSolrCommand>();

		List<ItemForSolrCommand> it = solrGroupData.getSolrCommandMap();
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
	private SearchResultPage<ItemForSolrCommand> convertSearchPageFacet(
			Integer start,
			Integer rows,
			Long numFound,
			List<ItemForSolrCommand> items,
			Map<String, Integer> facetQueryMap,
			Map<String, Map<String, Long>> facetMap){

		SearchResultPage<ItemForSolrCommand> searchResultPage = new SearchResultPage<ItemForSolrCommand>();
		searchResultPage.setItems(items);
		searchResultPage.setCurrentPage(rows == 0 ? 1 : (start / rows + 1));
		searchResultPage.setCount(numFound);
		searchResultPage.setSize(rows);
		searchResultPage.setStart(start);
		searchResultPage.setTotalPages(rows == 0 ? 0 : numFound.intValue() / rows + (numFound.intValue() % rows == 0 ? 0 : 1));

		List<FacetGroup> facetGroups = new ArrayList<FacetGroup>();

		//****************************属性和分类的facet
		if(Validator.isNotNullOrEmpty(facetMap)){
			for (Entry<String, Map<String, Long>> entry : facetMap.entrySet()){
				String key = entry.getKey();
				Map<String, Long> valueMap = entry.getValue();

				FacetGroup facetGroup = new FacetGroup();
				// 如果key等于category_tree代表是分类的facet
				if (SkuItemParam.category_tree.equals(key)) {
					facetGroup = convertFacetGroup(valueMap);
					facetGroup.setCategory(true);
					facetGroup.setType(FacetType.CATEGORY.toString());
				}else{
					// 否则是属性的facet
					facetGroup = convertFacetGroup(valueMap);
					facetGroup.setCategory(false);
					facetGroup.setType(FacetType.PROPERTY.toString());
					facetGroup.setId(Long.valueOf(key.replace(SkuItemParam.dynamicCondition, "")));
				}
				facetGroups.add(facetGroup);
			}
		}
		
		//**************************价格范围的facetGroup
		if(Validator.isNotNullOrEmpty(facetQueryMap)){
			FacetGroup priceGroup = new FacetGroup();
			priceGroup.setCategory(false);
			priceGroup.setType(FacetType.RANGE.toString());		
			List<Facet> facets = new ArrayList<Facet>();
			for (Entry<String, Integer> entry : facetQueryMap.entrySet()){
				String key = entry.getKey();
				Integer count = entry.getValue();
				if(key.indexOf(SkuItemParam.sale_price)!=-1){
					Facet facet=new Facet();
					facet.setValue(key);
					facet.setCount(count);
					facets.add(facet);
				}
			}
			if(facets.size()>0){
				priceGroup.setFacets(facets);
				facetGroups.add(priceGroup);
			}
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
