package com.baozun.nebula.web.controller.search;

import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.solr.client.solrj.SolrQuery;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;

import com.baozun.nebula.search.Boost;
import com.baozun.nebula.search.FacetFilterHelper;
import com.baozun.nebula.search.FacetGroup;
import com.baozun.nebula.search.SearchManager;
import com.baozun.nebula.search.command.SearchCommand;
import com.baozun.nebula.search.command.SearchResultPage;
import com.baozun.nebula.search.convert.SolrQueryConvert;
import com.baozun.nebula.solr.command.ItemForSolrCommand;
import com.baozun.nebula.web.controller.BaseController;
import com.baozun.nebula.web.controller.search.form.SearchForm;

public abstract class NebulaAbstractSearchController extends BaseController{
	
	private SolrQueryConvert solrQueryConvert;
	
	private SearchManager	searchManager;
	
	private FacetFilterHelper	facetFilterHelper;
	
	public String search(@ModelAttribute SearchForm searchForm,HttpServletRequest request,HttpServletResponse response,Model model){
		//将 searchForm 转成 searchCommand
		SearchCommand searchCommand = null;
		
		//将 searchCommand 中 filterConditionStr 转成FacetParameter
		seachParamProcess(searchCommand);
		
		//创建solrquery对象
		SolrQuery solrQuery = solrQueryConvert.convert(searchCommand);
		
		//set facet相关信息
		setFacet(solrQuery,searchCommand);
		
		//设置权重信息
		Boost boost = this.createBoost();
		searchManager.setSolrBoost(solrQuery, boost);
		
		//查询
		SearchResultPage<ItemForSolrCommand> solrDataPage= searchManager.search(solrQuery);
		
		//页面左侧筛选项
		List<FacetGroup> facetGroups = facetFilterHelper.createFilterResult(solrDataPage);
		
		//将SearchResultPage<ItemForSolrCommand> 转换成页面需要的itemListView对象
		
		return "item.list";
	}
	

	/**
	 * 将 searchCommand 里面的filterConditionStr 转成 FacetParameter
	 * 用做后面solr查询
	 * @param searchCommand
	 */
	public void seachParamProcess(SearchCommand searchCommand){
		
		//FacetParameter facetParameter = searc
		//searchCommand.setFacetParameter(facetParameter);
	}
	
	/**
	 * 设置solrfacet信息，需要结合t_pd_search_con 设置
	 * @param solrQuery
	 * @param searchCommand
	 */
	public void setFacet(SolrQuery solrQuery,SearchCommand searchCommand){
		
	}
	
	/**
	 * 创建solr boost对象，用做默认排序的权重打分
	 * @return
	 */
	public Boost createBoost(){
		Boost boost = new Boost();
		return boost;
	}
	
	
	
	
	

}
