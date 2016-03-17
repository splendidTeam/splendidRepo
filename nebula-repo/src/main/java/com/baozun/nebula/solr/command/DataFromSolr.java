package com.baozun.nebula.solr.command;

import java.util.Map;

import loxia.dao.Pagination;

import com.baozun.nebula.command.ItemResultCommand;
import com.baozun.nebula.command.ItemSolrCommand;
import com.baozun.nebula.solr.utils.PaginationForSolr;

public class DataFromSolr {
	
	/**
	 * 聚合后前数
	 */
	private Long number;
	
	/**
	 * 商品信息
	 */
	private PaginationForSolr<ItemResultCommand> items;
	
	/**
	 * 商品fenlei信息
	 */
	private Map<String,String> categoryMap;
	
	
	/**
	 * Facet聚合结果信息
	 * 
	 * id:[valueid1=XXXX,valueid2=XXX],id:[valueid1=XXXX,valueid2=XXX]
	 * 
	 */
	private Map<String, Map<String, Long>> facetMap;
	
	/**
	 * FacetQuery聚合结果信息
	 * 
	 * id1:[ 0 TO 100]=XXX,id2:[ 0 TO 100]=XXX
	 * 
	 */
	private Map<String, Integer> facetQueryMap;

	public Long getNumber() {
		return number;
	}

	public void setNumber(Long number) {
		this.number = number;
	}


	/**
	 * @return the items
	 */
	public PaginationForSolr<ItemResultCommand> getItems() {
		return items;
	}

	/**
	 * @param items the items to set
	 */
	public void setItems(PaginationForSolr<ItemResultCommand> items) {
		this.items = items;
	}

	public Map<String, Map<String, Long>> getFacetMap() {
		return facetMap;
	}

	public void setFacetMap(Map<String, Map<String, Long>> facetMap) {
		this.facetMap = facetMap;
	}

	/**
	 * @return the categoryMap
	 */
	public Map<String, String> getCategoryMap() {
		return categoryMap;
	}

	/**
	 * @param categoryMap the categoryMap to set
	 */
	public void setCategoryMap(Map<String, String> categoryMap) {
		this.categoryMap = categoryMap;
	}

	public Map<String, Integer> getFacetQueryMap() {
		return facetQueryMap;
	}

	public void setFacetQueryMap(Map<String, Integer> facetQueryMap) {
		this.facetQueryMap = facetQueryMap;
	}

}
