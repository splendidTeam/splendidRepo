/**
 * Copyright (c) 2013 Baozun All Rights Reserved.
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
package com.baozun.nebula.sdk.command;

import java.util.List;

import com.baozun.nebula.model.BaseModel;

/**
 * @author Tianlong.Zhang
 *
 */
public class SearchConditionResultCommand  extends BaseModel{

	/**
	 * 
	 */
	private static final long serialVersionUID = -9122418703619580649L;
	
	private String name;
	
	private Long sId;
	
	private Integer sort;
	
	private List<SearchItemResultCommand> items;

	public void setItems(List<SearchItemResultCommand> items) {
		this.items = items;
	}

	public List<SearchItemResultCommand> getItems() {
		return items;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getName() {
		return name;
	}

	public void setsId(Long sId) {
		this.sId = sId;
	}

	public Long getsId() {
		return sId;
	}

	public void setSort(Integer sort) {
		this.sort = sort;
	}

	public Integer getSort() {
		return sort;
	}

}
