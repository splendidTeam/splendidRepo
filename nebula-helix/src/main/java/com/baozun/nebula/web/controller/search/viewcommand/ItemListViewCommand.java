/**
 * Copyright (c) 2010 Jumbomart All Rights Reserved.
 *
 * This software is the confidential and proprietary information of Jumbomart.
 * You shall not disclose such Confidential Information and shall use it only in
 * accordance with the terms of the license agreement you entered into
 * with Jumbo.
 *
 * JUMBOMART MAKES NO REPRESENTATIONS OR WARRANTIES ABOUT THE SUITABILITY OF THE
 * SOFTWARE, EITHER EXPRESS OR IMPLIED, INCLUDING BUT NOT LIMITED TO THE
 * IMPLIED WARRANTIES OF MERCHANTABILITY, FITNESS FOR A PARTICULAR
 * PURPOSE, OR NON-INFRINGEMENT. JUMBOMART SHALL NOT BE LIABLE FOR ANY DAMAGES
 * SUFFERED BY LICENSEE AS A RESULT OF USING, MODIFYING OR DISTRIBUTING
 * THIS SOFTWARE OR ITS DERIVATIVES.
 *
 */
package com.baozun.nebula.web.controller.search.viewcommand;

import java.util.List;

import com.baozun.nebula.search.FacetGroup;
import com.baozun.nebula.solr.command.ItemForSolrI18nCommand;
import com.baozun.nebula.web.controller.BaseViewCommand;

/**
 * 商品列表
 * 
 * @author 冯明雷
 * @version 1.0
 * @time 2016年4月28日 下午6:49:29
 */
public class ItemListViewCommand extends BaseViewCommand{

	private static final long				serialVersionUID	= -7224847778759734419L;

	/** 商品列表 */
	private List<ItemForSolrI18nCommand>	itemForSolrI18nCommands;

	/** 数量 */
	private long							count;

	/** 当前页码 */
	private int								currentPage;

	/** 总页数 */
	private int								totalPages;

	/** 从第几条开始 */
	private int								start;

	/** 每页多少条 */
	private int								size;

	/** 筛选 */
	private List<FacetGroup>				facetGroups;

	/**
	 * get itemForSolrI18nCommands
	 * 
	 * @return itemForSolrI18nCommands
	 */
	public List<ItemForSolrI18nCommand> getItemForSolrI18nCommands(){
		return itemForSolrI18nCommands;
	}

	/**
	 * set itemForSolrI18nCommands
	 * 
	 * @param itemForSolrI18nCommands
	 */
	public void setItemForSolrI18nCommands(List<ItemForSolrI18nCommand> itemForSolrI18nCommands){
		this.itemForSolrI18nCommands = itemForSolrI18nCommands;
	}

	/**
	 * get count
	 * 
	 * @return count
	 */
	public long getCount(){
		return count;
	}

	/**
	 * set count
	 * 
	 * @param count
	 */
	public void setCount(long count){
		this.count = count;
	}

	/**
	 * get currentPage
	 * 
	 * @return currentPage
	 */
	public int getCurrentPage(){
		return currentPage;
	}

	/**
	 * set currentPage
	 * 
	 * @param currentPage
	 */
	public void setCurrentPage(int currentPage){
		this.currentPage = currentPage;
	}

	/**
	 * get totalPages
	 * 
	 * @return totalPages
	 */
	public int getTotalPages(){
		return totalPages;
	}

	/**
	 * set totalPages
	 * 
	 * @param totalPages
	 */
	public void setTotalPages(int totalPages){
		this.totalPages = totalPages;
	}

	/**
	 * get start
	 * 
	 * @return start
	 */
	public int getStart(){
		return start;
	}

	/**
	 * set start
	 * 
	 * @param start
	 */
	public void setStart(int start){
		this.start = start;
	}

	/**
	 * get size
	 * 
	 * @return size
	 */
	public int getSize(){
		return size;
	}

	/**
	 * set size
	 * 
	 * @param size
	 */
	public void setSize(int size){
		this.size = size;
	}

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
