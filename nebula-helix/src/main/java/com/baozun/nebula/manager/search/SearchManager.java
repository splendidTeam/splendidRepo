package com.baozun.nebula.manager.search;

import java.util.List;

import org.apache.solr.client.solrj.SolrQuery;

import com.baozun.nebula.search.Boost;
import com.baozun.nebula.search.FacetParameter;
import com.baozun.nebula.solr.command.ItemForSolrCommand;

import loxia.dao.Page;
import loxia.dao.Pagination;
import loxia.dao.Sort;

/**
 * 
 * @author long.xia
 *
 * @param <T>
 */
public interface SearchManager<T> {

	/**
	 * solr查询接口，返回分页数据
	 * @param page
	 * 			分页对象
	 * @param solrQuery
	 * 			solr查询对象
	 * @return
	 */
	Pagination<T> search(Page page,SolrQuery solrQuery);
	
	/**
	 * 返回list
	 * @param solrQuery
	 * 			solr查询对象
	 * @return
	 */
	List<T> search(SolrQuery solrQuery);
	
	/**
	 * 将页面传入参数转换成solr查询对象
	 * @param facetParameter
	 * @return
	 */
	SolrQuery solrQueryBuild(FacetParameter facetParameter,Sort sort);
	
	/**
	 * 设置solr查询权重，一般用作默认排序
	 * @param solrQuery
	 * @param qf
	 * @param bf
	 * @param bq
	 */
	void setSolrBoost(SolrQuery solrQuery,Boost boost);

	/**
	 * 将solr doc转成页面使用的solr数据
	 * @param itemForSolrCommands
	 */
	T convertSolrDoc(List<ItemForSolrCommand> itemForSolrCommands);
	
}
