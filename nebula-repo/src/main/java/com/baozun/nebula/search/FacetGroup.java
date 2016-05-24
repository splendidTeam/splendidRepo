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
 * Facet Group.
 * Represents a set of facets (e.g. brand, color, size etc).
 *
 * @author D.C
 * @since 2016年4月13日 下午6:51:24
 */
public class FacetGroup {

	private Long 	id;
	
	private String	title;
	
	private String 	type;
	
	/** 是否显示 */
	private Boolean isShow = true;
	
	/**筛选项的值*/
	private List<Facet>	facets = new ArrayList<Facet>();
	
	/**是否是分类*/
	private	Boolean isCategory=false;

	public Long getId() {
		return id;
	}

	public String getTitle() {
		return title;
	}

	public String getType() {
		return type;
	}

	public List<Facet> getFacets() {
		return facets;
	}

	public Boolean getIsCategory() {
		return isCategory;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public void setType(String type) {
		this.type = type;
	}

	public void setFacets(List<Facet> facets) {
		this.facets = facets;
	}

	public void setIsCategory(Boolean isCategory) {
		this.isCategory = isCategory;
	}

	
	/**   
	 * get isShow  
	 * @return isShow  
	 */
	public Boolean getIsShow(){
		return isShow;
	}
	
	/**
	 * set isShow 
	 * @param isShow
	 */
	public void setIsShow(Boolean isShow){
		this.isShow = isShow;
	}
	
	
}