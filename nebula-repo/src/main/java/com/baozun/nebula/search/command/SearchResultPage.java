package com.baozun.nebula.search.command;

import java.util.List;

import com.baozun.nebula.search.FacetGroup;

import loxia.dao.Pagination;

public class SearchResultPage<T> extends Pagination<T>{

	List<FacetGroup> facets;
	
//	Map<String,List<FacetGroup>> 
	
}
