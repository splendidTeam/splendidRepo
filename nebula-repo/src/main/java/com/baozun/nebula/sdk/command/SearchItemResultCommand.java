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

import com.baozun.nebula.model.BaseModel;

/**
 * @author Tianlong.Zhang
 *
 */
public class SearchItemResultCommand extends BaseModel{

	private static final long serialVersionUID = 4713265559331832787L;
	
	private String itemName;
	
	private Long numberFound;
	
	private Long itemKey;
	
	private Integer sort;

	/**
	 * @return the itemName
	 */
	public String getItemName() {
		return itemName;
	}

	/**
	 * @param itemName the itemName to set
	 */
	public void setItemName(String itemName) {
		this.itemName = itemName;
	}

	/**
	 * @return the numberFound
	 */
	public Long getNumberFound() {
		return numberFound;
	}

	/**
	 * @param numberFound the numberFound to set
	 */
	public void setNumberFound(Long numberFound) {
		this.numberFound = numberFound;
	}

	/**
	 * @return the itemKey
	 */
	public Long getItemKey() {
		return itemKey;
	}

	/**
	 * @param itemKey the itemKey to set
	 */
	public void setItemKey(Long itemKey) {
		this.itemKey = itemKey;
	}

	public void setSort(Integer sort) {
		this.sort = sort;
	}

	public Integer getSort() {
		return sort;
	}
	
}
