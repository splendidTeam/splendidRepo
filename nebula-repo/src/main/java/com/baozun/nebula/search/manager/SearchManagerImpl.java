package com.baozun.nebula.search.manager;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.solr.client.solrj.SolrQuery;
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
import com.baozun.nebula.search.command.SearchResultPage;
import com.baozun.nebula.solr.Param.SkuItemParam;
import com.baozun.nebula.solr.command.ItemForSolrCommand;
import com.baozun.nebula.solr.command.SolrGroup;
import com.baozun.nebula.solr.command.SolrGroupCommand;
import com.baozun.nebula.solr.command.SolrGroupData;
import com.baozun.nebula.solr.manager.SolrManager;
import com.baozun.nebula.solr.manager.SolrManagerImpl;
import com.baozun.nebula.solr.utils.PaginationForSolr;
import com.feilong.core.Validator;

@Service("searchManager")
@Transactional
public class SearchManagerImpl<T, PK extends Serializable> implements SearchManager{

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
	public SearchResultPage<ItemForSolrCommand> search(SolrQuery solrQuery){
		LOG.debug(solrQuery.toString());

		// 多少行
		Integer rows = solrQuery.getRows();
		// 第几页
		Integer currentPage = solrQuery.getStart();

		SearchResultPage<ItemForSolrCommand> searchResultPage = null;
		SolrGroupData<T> solrGroup = new SolrGroupData<T>();

		// 是否分组显示
		String isGroup = solrQuery.get(GroupParams.GROUP);
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
		// TODO Auto-generated method stub

	}

	@Override
	public List<SearchConditionCommand> findConditionByCategoryIdsWithCache(List<Long> categoryIds){
		List<SearchConditionCommand> searchConditionCommands = cacheManager.getObject(conditionCacheKey);

		if (Validator.isNullOrEmpty(searchConditionCommands)) {
			searchConditionCommands = sdkSearchConditionManager.findConditionByCategoryIdList(categoryIds);
			cacheManager.setObject(conditionCacheKey, searchConditionCommands);
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
	private SearchResultPage<ItemForSolrCommand> solrGroupConverterSearchResultPageWithGroup(
			SolrGroupData<T> solrGroupData,
			Integer currentPage,
			Integer size){
		SearchResultPage<ItemForSolrCommand> searchResultPage = new SearchResultPage<>();

		Map<String, String> categoryMap = new HashMap<String, String>();
		List<ItemForSolrCommand> list = new ArrayList<ItemForSolrCommand>();

		Map<String, SolrGroupCommand<T>> it = solrGroupData.getSolrGroupCommandMap();
		for (String key : it.keySet()){
			SolrGroupCommand<T> solrGroupCommand = it.get(key);
			List<SolrGroup<T>> solrGroupList = solrGroupCommand.getItemForSolrCommandList();
			for (SolrGroup<T> solrGroup : solrGroupList){
				List<ItemForSolrCommand> itemForSolrCommandList = (List<ItemForSolrCommand>) solrGroup.getBeans();
				list.addAll(itemForSolrCommandList);
				for (ItemForSolrCommand itemForSolrCommand : itemForSolrCommandList){
					if (null != itemForSolrCommand.getCategoryName() && itemForSolrCommand.getCategoryName().size() > 0) {
						Map<String, String> itemCategoryMap = itemForSolrCommand.getCategoryName();
						for (String categoryKey : itemCategoryMap.keySet()){
							String categoryId = categoryKey.replace(SkuItemParam.categoryname, "");
							categoryMap.put(categoryId, categoryId);
						}
					}
				}
			}
		}

		Integer start = (currentPage - 1) * size;
		PaginationForSolr<ItemForSolrCommand> pagination = getPagination(start, size, solrGroupData.getNumFound(), list);

		sdfsdfs(solrGroupData, size, searchResultPage, start, pagination);
		// dataFromSolr.setFacetMap(facetMap);
		// dataFromSolr.setFacetQueryMap(facetQueryMap);
		// dataFromSolr.setCategoryMap(categoryMap);

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
			SolrGroupData<T> solrGroupData,
			Integer currentPage,
			Integer size){
		SearchResultPage<ItemForSolrCommand> searchResultPage = new SearchResultPage<>();

		Map<String, String> categoryMap = new HashMap<String, String>();
		List<ItemForSolrCommand> list = new ArrayList<ItemForSolrCommand>();
		List<ItemForSolrCommand> it = (List<ItemForSolrCommand>) solrGroupData.getSolrCommandMap();

		if (null != it && it.size() > 0) {
			for (ItemForSolrCommand itemForSolrCommand : it){
				list.add(itemForSolrCommand);
				if (null != itemForSolrCommand.getCategoryName() && itemForSolrCommand.getCategoryName().size() > 0) {
					Map<String, String> itemCategoryMap = itemForSolrCommand.getCategoryName();
					for (String categoryKey : itemCategoryMap.keySet()){
						String categoryId = categoryKey.replace(SkuItemParam.categoryname, "");
						categoryMap.put(categoryId, categoryId);
					}
				}
			}
		}

		Integer start = (currentPage - 1) * size;
		Long count = Long.parseLong(list.size() + "");
		PaginationForSolr<ItemForSolrCommand> pagination = getPagination(start, size, count, list);

		sdfsdfs(solrGroupData, size, searchResultPage, start, pagination);

		return searchResultPage;
	}

	private void sdfsdfs(
			SolrGroupData<T> solrGroupData,
			Integer size,
			SearchResultPage<ItemForSolrCommand> searchResultPage,
			Integer start,
			PaginationForSolr<ItemForSolrCommand> pagination){
		searchResultPage.setItems(pagination.getCurrentPageItem());
		searchResultPage.setCount(pagination.getCount());
		searchResultPage.setSize(size);
		searchResultPage.setStart(start);
		searchResultPage.setTotalPages(pagination.getTotalPages());

		Map<String, Map<String, Long>> facetMap = solrGroupData.getFacetMap();
		Map<String, Integer> facetQueryMap = solrGroupData.getFacetQueryMap();
	}

	private PaginationForSolr<ItemForSolrCommand> getPagination(int start,int rows,Long count,List<ItemForSolrCommand> items){
		PaginationForSolr<ItemForSolrCommand> page = new PaginationForSolr<ItemForSolrCommand>();
		page.setCount(count);
		page.setCurrentPage(rows == 0 ? 1 : (start / rows + 1));
		page.setStart(start);
		page.setSize(rows);
		page.setTotalPages(rows == 0 ? 0 : (int) page.getCount() / rows + (page.getCount() % rows == 0 ? 0 : 1));
		page.setItems(items);
		return page;
	}

}
