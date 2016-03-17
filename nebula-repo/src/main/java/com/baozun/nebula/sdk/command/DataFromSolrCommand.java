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
import java.util.Map;

import com.baozun.nebula.command.Command;
import com.baozun.nebula.command.ItemListResultCommand;
import com.baozun.nebula.command.promotion.PromotionCommand;
import com.baozun.nebula.model.BaseModel;
import com.baozun.nebula.solr.utils.PaginationForSolr;



/**
 * @author Tianlong.Zhang
 *
 */
public class DataFromSolrCommand  extends BaseModel implements Command{

	/**
	 * 
	 */
	private static final long	serialVersionUID	= 6929123214613332539L;

	/**
	 * 聚合后总数
	 */
	private Long number;
	
	/**
	 * 商品信息
	 */
	private PaginationForSolr<ItemListResultCommand> items;
	
	/**
	 * 商品fenlei信息
	 */
	private Map<String,String> categoryMap;
	
	
	/**
	 * Facet聚合结果信息
	 */
	private List<SearchConditionResultCommand> facetResultList;
	
	private List<SearchConditionResultCommand> salePriceResultList;
	
	private Map<String, Integer> facetQueryMap;
	
	/**
	 * 商品参加的活动
	 */
	private Map<Long,List<PromotionCommand>> itemPromotionMap;
	
	/**
	 * 活动角标
	 */
	private Map<String,String> promotionLogoMarkMap;


	/**
	 * @return the number
	 */
	public Long getNumber() {
		return number;
	}


	/**
	 * @param number the number to set
	 */
	public void setNumber(Long number) {
		this.number = number;
	}


	/**
	 * @return the items
	 */
	public PaginationForSolr<ItemListResultCommand> getItems() {
		return items;
	}


	/**
	 * @param items the items to set
	 */
	public void setItems(PaginationForSolr<ItemListResultCommand> items) {
		this.items = items;
	}


	/**
	 * @return the categoryMap
	 */
	public Map<String, String> getCategoryMap() {
		return categoryMap;
	}


	/**
	 * @param categoryMap the categoryMap to set
	 */
	public void setCategoryMap(Map<String, String> categoryMap) {
		this.categoryMap = categoryMap;
	}

	public void setFacetMap(List<SearchConditionResultCommand> facetMap) {
		this.facetResultList = facetMap;
	}

	public List<SearchConditionResultCommand> getFacetMap() {
		return facetResultList;
	}

	public void setSalePriceMap(List<SearchConditionResultCommand> salePriceMap) {
		this.salePriceResultList = salePriceMap;
	}

	public List<SearchConditionResultCommand> getSalePriceMap() {
		return salePriceResultList;
	}

	public void setFacetQueryMap(Map<String, Integer> facetQueryMap) {
		this.facetQueryMap = facetQueryMap;
	}

	public Map<String, Integer> getFacetQueryMap() {
		return facetQueryMap;
	}

	public Map<Long, List<PromotionCommand>> getItemPromotionMap() {
		return itemPromotionMap;
	}

	public void setItemPromotionMap(Map<Long, List<PromotionCommand>> itemPromotionMap) {
		this.itemPromotionMap = itemPromotionMap;
	}
	public Map<String, String> getPromotionLogoMarkMap() {
		return promotionLogoMarkMap;
	}

	public void setPromotionLogoMarkMap(Map<String, String> promotionLogoMarkMap) {
		this.promotionLogoMarkMap = promotionLogoMarkMap;
	}
}
