package com.baozun.nebula.search.command;

import java.util.List;

import com.baozun.nebula.search.FacetParameter;

public class SearchCommand {
	/**
	 * 搜索关键字
	 */
	private String searchWord;
	/**
	 * 过滤条件
	 */
	private String filterConditionStr;
	
	/**
	 * 过滤条件参数顺序，用户页面点击筛选项时候的顺序。
	 * 记录这个顺序用于后面facet tag作用
	 */
	private String	filterParamOrder;
	
	/**
	 * 后端查询参数（solr.fq）,从这个值里面体现filterquery之间的关系
	 */
	private List<FacetParameter>	facetParameters;
	/**
	 * 排序
	 */
	private String sortStr;
	/**
	 * 页大小
	 */
	private Integer pageSize = 10;
	/**
	 * 第几页 zero based
	 */
	private Integer pageNumber;
	
	public String getFilterParamOrder() {
		return filterParamOrder;
	}
	public void setFilterParamOrder(String filterParamOrder) {
		this.filterParamOrder = filterParamOrder;
	}
	
	public String getSearchWord() {
		return searchWord;
	}
	public String getFilterConditionStr() {
		return filterConditionStr;
	}
	public String getSortStr() {
		return sortStr;
	}
	public Integer getPageSize() {
		return pageSize;
	}
	public Integer getPageNumber() {
		return pageNumber;
	}
	public void setSearchWord(String searchWord) {
		this.searchWord = searchWord;
	}
	public void setFilterConditionStr(String filterConditionStr) {
		this.filterConditionStr = filterConditionStr;
	}

	
	public List<FacetParameter> getFacetParameters() {
		return facetParameters;
	}
	public void setFacetParameters(List<FacetParameter> facetParameters) {
		this.facetParameters = facetParameters;
	}
	public void setSortStr(String sortStr) {
		this.sortStr = sortStr;
	}
	public void setPageSize(Integer pageSize) {
		this.pageSize = pageSize;
	}
	public void setPageNumber(Integer pageNumber) {
		this.pageNumber = pageNumber;
	}
	
	
}
