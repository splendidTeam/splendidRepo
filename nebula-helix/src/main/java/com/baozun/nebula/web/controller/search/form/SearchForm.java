package com.baozun.nebula.web.controller.search.form;

import com.baozun.nebula.web.controller.BaseForm;

public class SearchForm extends BaseForm{

	private static final long	serialVersionUID	= 1L;

	/**
	 * 搜索关键字
	 */
	private String				searchWord;

	/**
	 * 过滤条件(属性)<br/>
	 * 参数格式：属性Id-属性值Id,属性Id-属性值Id....
	 */
	private String				filterConditionStr;
	
	/**
	 * 过滤条件(分类)<br/>
	 * 参数格式：父级分类id-父级分类id-分类id,父级分类id-父级分类id-分类id......
	 */
	private String				categoryConditionStr;
	
	/**
	 * 前端导航的过滤条件
	 */
	private String				navigationConditionStr;
	
	/**
	 * 过滤条件参数顺序，用户页面点击筛选项时候的顺序。 记录这个顺序用于后面facet tag作用
	 */
	private String				filterParamOrder;
	
	/**
	 * 导航Id
	 */
	private Long				navigationId;

	/**
	 * 排序
	 */
	private String				sortStr;

	/**
	 * 页大小
	 */
	private Integer				pageSize			= 20;

	/**
	 * 第几页 zero based
	 */
	private Integer				pageNumber;

	public String getSearchWord(){
		return searchWord;
	}

	public String getFilterConditionStr(){
		return filterConditionStr;
	}

	public String getFilterParamOrder(){
		return filterParamOrder;
	}

	public String getSortStr(){
		return sortStr;
	}

	public Integer getPageSize(){
		return pageSize;
	}

	public Integer getPageNumber(){
		return pageNumber;
	}

	public void setSearchWord(String searchWord){
		this.searchWord = searchWord;
	}

	public void setFilterConditionStr(String filterConditionStr){
		this.filterConditionStr = filterConditionStr;
	}

	public void setFilterParamOrder(String filterParamOrder){
		this.filterParamOrder = filterParamOrder;
	}

	public void setSortStr(String sortStr){
		this.sortStr = sortStr;
	}

	public void setPageSize(Integer pageSize){
		this.pageSize = pageSize;
	}

	public void setPageNumber(Integer pageNumber){
		this.pageNumber = pageNumber;
	}

	public String getCategoryConditionStr() {
		return categoryConditionStr;
	}

	public String getNavigationConditionStr() {
		return navigationConditionStr;
	}

	public void setCategoryConditionStr(String categoryConditionStr) {
		this.categoryConditionStr = categoryConditionStr;
	}

	public void setNavigationConditionStr(String navigationConditionStr) {
		this.navigationConditionStr = navigationConditionStr;
	}

	
	/**   
	 * get navigationId  
	 * @return navigationId  
	 */
	public Long getNavigationId(){
		return navigationId;
	}

	
	/**
	 * set navigationId 
	 * @param navigationId
	 */
	public void setNavigationId(Long navigationId){
		this.navigationId = navigationId;
	}

}
