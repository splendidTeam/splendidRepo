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
package com.baozun.nebula.command;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;

/**
 * @author lujun
 */
public class ItemSolrI18nCommand extends BaseItemForSolrCommand {

	/**
	 * 
	 */
	private static final long serialVersionUID = 2544112989421233359L;

	/**
	 * 用于列表页显示图片的Url. 并非solr中返回的
	 */
	private String imgUrl;

	/**
	 * 用于列表页显示 到商品PDP 的链接 ，非solr 返回
	 */
	private String itemUrl;

	/**
	 * 图片/颜色
	 */
	private List<ItemImageCommand> imageList;

	/**
	 * 分类
	 */
	private Map<Long, ItemForCategoryCommand> categoryMap;

	/**
	 * 动态属性(可关键字查询)
	 */
	private List<ItemPropertiesCommand> dynamicListForSearch;

	/**
	 * 动态属性(不可关键字查询)
	 */
	private List<ItemPropertiesCommand> dynamicListWithOutSearch;

	/**
	 * 用户自定义属性
	 */
	private List<ItemPropertiesCommand> dynamicListForCustomer;

	/**
	 * 标签
	 */
	private List<ItemTagCommand> tagList;

	/**
	 * 动态属性关联
	 * 
	 * @return
	 */
	private List<String> refInfo;

	private Float rankavg;

	private Integer salesCount;

	private Integer favoredCount;
	
	/** 销售额 */
	private Double salesVolume;

	/**
	 * 分组标记
	 */
	private String style;

	/**
	 * 自定义分组标记
	 */
	private String custStyle;

	/**
	 * 默认排序
	 */
	private Integer defaultSort;

	private Map<String, String> dynamicTitle;

	private Map<String, String> dynamicSubTitle;

	private Map<String, String> dynamicSketch;

	private Map<String, String> dynamicSeoTitle;

	private Map<String, String> dynamicSeoKeywords;

	private Map<String, String> dynamicSeoDescription;

	private Map<String, String> dynamicDescription;
	private Map<String, String> dynamicDescriptionForSearch;

	public Map<Long, ItemForCategoryCommand> getCategoryMap() {
		return categoryMap;
	}

	public void setCategoryMap(Map<Long, ItemForCategoryCommand> categoryMap) {
		this.categoryMap = categoryMap;
	}

	public List<ItemPropertiesCommand> getDynamicListForSearch() {
		return dynamicListForSearch;
	}

	public void setDynamicListForSearch(
			List<ItemPropertiesCommand> dynamicListForSearch) {
		this.dynamicListForSearch = dynamicListForSearch;
	}

	public List<ItemImageCommand> getImageList() {
		return imageList;
	}

	public void setImageList(List<ItemImageCommand> imageList) {
		this.imageList = imageList;
	}

	public List<ItemTagCommand> getTagList() {
		return tagList;
	}

	public void setTagList(List<ItemTagCommand> tagList) {
		this.tagList = tagList;
	}

	public List<ItemPropertiesCommand> getDynamicListForCustomer() {
		return dynamicListForCustomer;
	}

	public void setDynamicListForCustomer(
			List<ItemPropertiesCommand> dynamicListForCustomer) {
		this.dynamicListForCustomer = dynamicListForCustomer;
	}

	public List<String> getRefInfo() {
		return refInfo;
	}

	public void setRefInfo(List<String> refInfo) {
		this.refInfo = refInfo;
	}

	public void setImgUrl(String imgUrl) {
		this.imgUrl = imgUrl;
	}

	public String getImgUrl() {
		return imgUrl;
	}

	public void setItemUrl(String itemUrl) {
		this.itemUrl = itemUrl;
	}

	public String getItemUrl() {
		return itemUrl;
	}

	public Float getRankavg() {
		return rankavg;
	}

	public void setRankavg(Float rankavg) {
		this.rankavg = rankavg;
	}

	public Integer getSalesCount() {
		return salesCount;
	}

	public void setSalesCount(Integer salesCount) {
		this.salesCount = salesCount;
	}

	public String getStyle() {
		return style;
	}

	public void setStyle(String style) {
		this.style = style;
	}

	public String getCustStyle() {
		return custStyle;
	}

	public void setCustStyle(String custStyle) {
		this.custStyle = custStyle;
	}

	public void setFavoredCount(Integer favoredCount) {
		this.favoredCount = favoredCount;
	}

	public Integer getFavoredCount() {
		return favoredCount;
	}

	public Integer getDefaultSort() {
		return defaultSort;
	}

	public void setDefaultSort(Integer defaultSort) {
		this.defaultSort = defaultSort;
	}

	public List<ItemPropertiesCommand> getDynamicListWithOutSearch() {
		return dynamicListWithOutSearch;
	}

	public void setDynamicListWithOutSearch(
			List<ItemPropertiesCommand> dynamicListWithOutSearch) {
		this.dynamicListWithOutSearch = dynamicListWithOutSearch;
	}

	public Map<String, String> getDynamicTitle() {
		return dynamicTitle;
	}

	public void setDynamicTitle(Map<String, String> dynamicTitle) {
		this.dynamicTitle = dynamicTitle;
	}

	public Map<String, String> getDynamicSubTitle() {
		return dynamicSubTitle;
	}

	public void setDynamicSubTitle(Map<String, String> dynamicSubTitle) {
		this.dynamicSubTitle = dynamicSubTitle;
	}

	public Map<String, String> getDynamicSketch() {
		return dynamicSketch;
	}

	public void setDynamicSketch(Map<String, String> dynamicSketch) {
		this.dynamicSketch = dynamicSketch;
	}

	public Map<String, String> getDynamicSeoTitle() {
		return dynamicSeoTitle;
	}

	public void setDynamicSeoTitle(Map<String, String> dynamicSeoTitle) {
		this.dynamicSeoTitle = dynamicSeoTitle;
	}

	public Map<String, String> getDynamicDescriptionForSearch() {
		return dynamicDescriptionForSearch;
	}

	public void setDynamicDescriptionForSearch(
			Map<String, String> dynamicDescriptionForSearch) {
		this.dynamicDescriptionForSearch = dynamicDescriptionForSearch;
	}

	public Map<String, String> getDynamicDescription() {
		return dynamicDescription;
	}

	public void setDynamicDescription(
			Map<String, String> dynamicDescription) {
		this.dynamicDescription = dynamicDescription;
	}

	public Map<String, String> getDynamicSeoKeywords() {
		return dynamicSeoKeywords;
	}

	public void setDynamicSeoKeywords(Map<String, String> dynamicSeoKeywords) {
		this.dynamicSeoKeywords = dynamicSeoKeywords;
	}

	public Map<String, String> getDynamicSeoDescription() {
		return dynamicSeoDescription;
	}

	public void setDynamicSeoDescription(
			Map<String, String> dynamicSeoDescription) {
		this.dynamicSeoDescription = dynamicSeoDescription;
	}

	public Double getSalesVolume(){
		return salesVolume;
	}

	public void setSalesVolume(Double salesVolume){
		this.salesVolume = salesVolume;
	}
	
}
