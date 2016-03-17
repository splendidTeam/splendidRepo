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

import com.baozun.nebula.command.ItemCommand;

/**
 * 和group by 相关的参数,注释掉的部分 表示暂没有封装.
 * 
 * @author <a href="mailto:venusdrogon@163.com">金鑫</a>
 * @version 1.0 Nov 29, 2012 4:05:35 PM
 * @param <T>
 * @see {@link FacetParams}
 */
public class SolrGroup<T> implements Serializable {

	/** The Constant serialVersionUID. */
	private static final long serialVersionUID = 288232184048495608L;

	/**
	 * 这个聚合后的 总数
	 */
	private Long numFound;

	/** 聚合后的总数. */
	private String ngroups;

	/** filedName 按照什么聚合. */
	private String name;

	/**
	 * 将聚合的 每个 document 转成 bean
	 */
	private List<T> beans;

	/**
	 * @return the numFound
	 */
	public Long getNumFound() {
		return numFound;
	}

	/**
	 * @param numFound
	 *            the numFound to set
	 */
	public void setNumFound(Long numFound) {
		this.numFound = numFound;
	}

	/**
	 * @return the beans
	 */
	public List<T> getBeans() {
		return beans;
	}

	/**
	 * @param beans
	 *            the beans to set
	 */
	public void setBeans(List<T> beans) {
		this.beans = beans;
	}

	public String getNgroups() {
		return ngroups;
	}

	public void setNgroups(String ngroups) {
		this.ngroups = ngroups;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

}
