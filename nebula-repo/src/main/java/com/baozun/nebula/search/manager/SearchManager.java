package com.baozun.nebula.search.manager;

import java.util.List;

import org.apache.solr.client.solrj.SolrQuery;

import com.baozun.nebula.manager.BaseManager;
import com.baozun.nebula.sdk.command.SearchConditionCommand;
import com.baozun.nebula.search.Boost;
import com.baozun.nebula.search.command.SearchResultPage;
import com.baozun.nebula.solr.command.ItemForSolrCommand;

public interface SearchManager extends BaseManager{
	/**
	 * solr查询接口，返回分页数据
	 * @param page
	 * 			分页对象
	 * @param solrQuery
	 * 			solr查询对象
	 * @return
	 */
	SearchResultPage<ItemForSolrCommand> search(SolrQuery solrQuery);
	
	/**
	 * 设置solr查询权重，一般用作默认排序
	 * @param solrQuery
	 * @param qf
	 * @param bf
	 * @param bq
	 */
	void setSolrBoost(SolrQuery solrQuery,Boost boost);
	
	
	
	/**
	 * 根据分类id查询筛选条件(加缓存)
	 * @return List<SearchConditionCommand>
	 * @param categoryIds
	 * @author 冯明雷
	 * @time 2016年4月26日上午10:23:10
	 */
	List<SearchConditionCommand> findConditionByCategoryIdsWithCache(List<Long> categoryIds);
	
}
