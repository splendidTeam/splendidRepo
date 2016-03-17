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
package com.baozun.nebula.command.product;

import java.util.List;

import loxia.dao.Pagination;

import com.baozun.nebula.command.Command;
import com.baozun.nebula.command.ItemCommand;

/**
 * @author Tianlong.Zhang
 *
 */
public class SearchResultCommand implements Command{

	private static final long serialVersionUID = -6893482546905896950L;

	private Long numFound;
	
	private Pagination<ItemCommand> page;

	/**
	 * @return the numFound
	 */
	public Long getNumFound() {
		return numFound;
	}

	/**
	 * @param numFound the numFound to set
	 */
	public void setNumFound(Long numFound) {
		this.numFound = numFound;
	}

	/**
	 * @return the page
	 */
	public Pagination<ItemCommand> getPage() {
		return page;
	}

	/**
	 * @param page the page to set
	 */
	public void setPage(Pagination<ItemCommand> page) {
		this.page = page;
	}
	
	
}
