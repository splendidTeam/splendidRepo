package com.baozun.nebula.search.manager;

import java.util.List;

import org.apache.solr.client.solrj.SolrQuery;
import org.springframework.beans.factory.annotation.Autowired;

import com.baozun.nebula.manager.CacheManager;
import com.baozun.nebula.sdk.command.SearchConditionCommand;
import com.baozun.nebula.sdk.manager.SdkSearchConditionManager;
import com.baozun.nebula.search.Boost;
import com.baozun.nebula.search.command.SearchResultPage;
import com.baozun.nebula.solr.command.ItemForSolrCommand;
import com.feilong.core.Validator;

public class SearchManagerImpl implements SearchManager{

	private final static String			conditionCacheKey	= "findConditionByCategoryIdListKey";

	@Autowired
	private SdkSearchConditionManager	sdkSearchConditionManager;

	@Autowired
	private CacheManager				cacheManager;

	@Override
	public SearchResultPage<ItemForSolrCommand> search(SolrQuery solrQuery){
		// TODO Auto-generated method stub
		return null;
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

}
