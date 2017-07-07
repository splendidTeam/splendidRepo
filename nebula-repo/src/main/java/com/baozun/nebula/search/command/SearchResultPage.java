package com.baozun.nebula.search.command;

import java.io.Serializable;
import java.util.List;
import java.util.Map;

import org.apache.solr.client.solrj.response.FieldStatsInfo;

import com.baozun.nebula.search.FacetGroup;

import loxia.dao.Pagination;

public class SearchResultPage<T> implements Serializable{

	private static final long	serialVersionUID	= 1L;

	/**
	 * 商品信息无分组（提供商品所有信息）
	 */
	private Pagination<T>		itemsListWithOutGroup;

	/**
	 * 商品信息无分组（提供商品所有信息）
	 */
	private Pagination<List<T>>	itemsListWithGroup;

	/** Facet Group */
	private List<FacetGroup>	facetGroups;

	 /**
     * solr stats统计问题
     */
    private Map<String, FieldStatsInfo>    fieldStatsInfo;
    
	/**
	 * get facetGroups
	 * 
	 * @return facetGroups
	 */
	public List<FacetGroup> getFacetGroups(){
		return facetGroups;
	}

	/**
	 * set facetGroups
	 * 
	 * @param facetGroups
	 */
	public void setFacetGroups(List<FacetGroup> facetGroups){
		this.facetGroups = facetGroups;
	}

	/**
	 * get itemsListWithOutGroup
	 * 
	 * @return itemsListWithOutGroup
	 */
	public Pagination<T> getItemsListWithOutGroup(){
		return itemsListWithOutGroup;
	}

	/**
	 * set itemsListWithOutGroup
	 * 
	 * @param itemsListWithOutGroup
	 */
	public void setItemsListWithOutGroup(Pagination<T> itemsListWithOutGroup){
		this.itemsListWithOutGroup = itemsListWithOutGroup;
	}

	/**
	 * get itemsListWithGroup
	 * 
	 * @return itemsListWithGroup
	 */
	public Pagination<List<T>> getItemsListWithGroup(){
		return itemsListWithGroup;
	}

	/**
	 * set itemsListWithGroup
	 * 
	 * @param itemsListWithGroup
	 */
	public void setItemsListWithGroup(Pagination<List<T>> itemsListWithGroup){
		this.itemsListWithGroup = itemsListWithGroup;
	}

    
    /**
     * @return the fieldStatsInfo
     */
    public Map<String, FieldStatsInfo> getFieldStatsInfo(){
        return fieldStatsInfo;
    }

    
    /**
     * @param fieldStatsInfo the fieldStatsInfo to set
     */
    public void setFieldStatsInfo(Map<String, FieldStatsInfo> fieldStatsInfo){
        this.fieldStatsInfo = fieldStatsInfo;
    }

}
