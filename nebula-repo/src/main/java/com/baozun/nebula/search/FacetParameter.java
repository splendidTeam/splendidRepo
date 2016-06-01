/**
 * Copyright (c) 2016 Baozun All Rights Reserved.
 *
 * This software is the confidential and proprietary information of Baozun.
 * You shall not disclose such Confidential Information and shall use it only in
 * accordance with the terms of the license agreement you entered into
 * with Baozun.
 *
 * BAOZUN MAKES NO REPRESENTATIONS OR WARRANTIES ABOUT THE SUITABILITY OF THE
 * SOFTWARE, EITHER EXPRESS OR IMPLIED, INCLUDING BUT NOT LIMITED TO THE
 * IMPLIED WARRANTIES OF MERCHANTABILITY, FITNESS FOR A PARTICULAR
 * PURPOSE, OR NON-INFRINGEMENT. BAOZUN SHALL NOT BE LIABLE FOR ANY DAMAGES
 * SUFFERED BY LICENSEE AS A RESULT OF USING, MODIFYING OR DISTRIBUTING
 * THIS SOFTWARE OR ITS DERIVATIVES.
 *
 */
package com.baozun.nebula.search;

import java.util.ArrayList;
import java.util.List;

/**
 * Facet Parameter. Is used for representing a user facing facet.
 *
 * @author D.C
 */
public class FacetParameter{

	private String			name;

	private List<String>	values					= new ArrayList<String>();

	private FacetType		facetType;

	/**
	 * Create new facet parameter
	 * 
	 * @param name
	 */
	public FacetParameter(String name){
		this.name = name;
	}

	/***
	 * json转换使用
	 */
	public FacetParameter(){

	}

	/**
	 * Name of the facet parameter. Is shown in the facet URL.
	 * 
	 * @return name
	 */
	public String getName(){
		return name;
	}

	/**
	 * Get all facet values.
	 * 
	 * @return facet values
	 */
	public List<String> getValues(){
		return values;
	}

	/**
	 * Check if specific value is included in this facet parameter (when having multi select facets).
	 *
	 * @param facetValue
	 * @return contains value
	 */
	public boolean containsValue(String facetValue){
		if(values==null){
			return false;
		}
		return this.values.contains(facetValue);
		
	}

	public FacetType getFacetType(){
		return facetType;
	}

	public void setFacetType(FacetType facetType){
		this.facetType = facetType;
	}

	public void setName(String name){
		this.name = name;
	}

	public void setValues(List<String> values){
		this.values = values;
	}
}
