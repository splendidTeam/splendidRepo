package com.baozun.nebula.search;

import org.apache.solr.client.solrj.SolrQuery;

import com.baozun.nebula.search.command.SearchResultPage;
import com.baozun.nebula.solr.command.ItemForSolrCommand;

public interface SearchManager {
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
	
}
