package com.baozun.nebula.search.command;

import java.util.List;

import com.baozun.nebula.search.FacetGroup;

import loxia.dao.Pagination;

public class SearchResultPage<T> extends Pagination<T>{

	private static final long	serialVersionUID	= 1L;

	/** Facet Group */
	private List<FacetGroup>	facetGroups;

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

}
