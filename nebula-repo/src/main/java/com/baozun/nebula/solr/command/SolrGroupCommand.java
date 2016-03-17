/**
 * Copyright (c) 2012 Baozun All Rights Reserved.
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

package com.baozun.nebula.solr.command;

import java.io.Serializable;
import java.util.List;

import loxia.dao.Pagination;

import org.apache.solr.common.params.FacetParams;

/**
 * 和group by 相关的参数,注释掉的部分 表示暂没有封装.
 * 
 * @param <T>
 * @version 1.0 Nov 29, 2012 4:05:35 PM
 * @param <T>
 * @see {@link FacetParams}
 */
public class SolrGroupCommand<T> implements Serializable {

	/** The Constant serialVersionUID. */
	private static final long serialVersionUID = 288232184048495608L;

	/** filedName 按照什么聚合. */
	private String name;

	/** 聚合的结果 */
	private List<SolrGroup<T>> itemForSolrCommandList;

	/** 聚合前的总数. */
	private int matches;

	/** 聚合后的总数. */
	private Integer ngroups;

	/**
	 * Gets the filedName 按照什么聚合.
	 * 
	 * @return the name
	 */
	public String getName() {
		return name;
	}

	/**
	 * Sets the filedName 按照什么聚合.
	 * 
	 * @param name
	 *            the name to set
	 */
	public void setName(String name) {
		this.name = name;
	}

	/**
	 * Gets the 聚合前的总数.
	 * 
	 * @return the matches
	 */
	public int getMatches() {
		return matches;
	}

	/**
	 * Sets the 聚合前的总数.
	 * 
	 * @param matches
	 *            the matches to set
	 */
	public void setMatches(int matches) {
		this.matches = matches;
	}

	/**
	 * Gets the 聚合后的总数.
	 * 
	 * @return the ngroups
	 */
	public Integer getNgroups() {
		return ngroups;
	}

	/**
	 * Sets the 聚合后的总数.
	 * 
	 * @param ngroups
	 *            the ngroups to set
	 */
	public void setNgroups(Integer ngroups) {
		this.ngroups = ngroups;
	}

	public List<SolrGroup<T>> getItemForSolrCommandList() {
		return itemForSolrCommandList;
	}

	public void setItemForSolrCommandList(List<SolrGroup<T>> itemForSolrCommandList) {
		this.itemForSolrCommandList = itemForSolrCommandList;
	}


}
