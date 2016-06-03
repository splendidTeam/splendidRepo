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
 * Facet
 *
 * @author long.xia
 */
public class Facet {

	private Long id;
	
	private String title;

	private String url;

	private long count;

	private boolean selected;

	private String value;
	
	private	Long	parentId;
	
	private Integer	sortNo;
	

	public List<Facet> childrens = new ArrayList<Facet>();
	
	public Facet(){}
	
	public Facet(Facet facet){
		this.id = facet.getId();
		this.title = facet.getTitle();
		this.parentId = facet.getParentId();
		this.count = facet.getCount();
		this.childrens = facet.getChildrens();
		this.sortNo=facet.getSortNo();
		this.value = facet.getValue();
	}
	

	public String getTitle() {
		return title;
	}

	public String getUrl() {
		return url;
	}

	public long getCount() {
		return count;
	}

	public boolean getSelected() {
		return selected;
	}

	public String getValue() {
		return value;
	}

	public Long getId() {
		return id;
	}

	public List<Facet> getChildrens() {
		return childrens;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public void setUrl(String url) {
		this.url = url;
	}

	public void setCount(long count) {
		this.count = count;
	}

	public void setSelected(boolean selected) {
		this.selected = selected;
	}

	public void setValue(String value) {
		this.value = value;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public void setChildrens(List<Facet> childrens) {
		this.childrens = childrens;
	}

	public Long getParentId() {
		return parentId;
	}

	public void setParentId(Long parentId) {
		this.parentId = parentId;
	}

	
	/**   
	 * get sortNo  
	 * @return sortNo  
	 */
	public Integer getSortNo(){
		return sortNo;
	}

	
	/**
	 * set sortNo 
	 * @param sortNo
	 */
	public void setSortNo(Integer sortNo){
		this.sortNo = sortNo;
	}

	

}
