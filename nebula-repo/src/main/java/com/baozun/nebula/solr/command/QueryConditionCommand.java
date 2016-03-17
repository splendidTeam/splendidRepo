/**
 * Copyright (c) 2008-2012 FeiLong, Inc. All Rights Reserved.
 * <p>
 * 	This software is the confidential and proprietary information of FeiLong Network Technology, Inc. ("Confidential Information").  <br>
 * 	You shall not disclose such Confidential Information and shall use it 
 *  only in accordance with the terms of the license agreement you entered into with FeiLong.
 * </p>
 * <p>
 * 	FeiLong MAKES NO REPRESENTATIONS OR WARRANTIES ABOUT THE SUITABILITY OF THE SOFTWARE, EITHER EXPRESS OR IMPLIED, 
 * 	INCLUDING BUT NOT LIMITED TO THE IMPLIED WARRANTIES OF MERCHANTABILITY, FITNESS FOR A PARTICULAR
 * 	PURPOSE, OR NON-INFRINGEMENT. <br> 
 * 	FeiLong SHALL NOT BE LIABLE FOR ANY DAMAGES SUFFERED BY LICENSEE AS A RESULT OF USING, MODIFYING OR DISTRIBUTING
 * 	THIS SOFTWARE OR ITS DERIVATIVES.
 * </p>
 */
package com.baozun.nebula.solr.command;

import java.io.Serializable;
import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * 搜索条件.
 * 
 * @author chengchao
 */
public class QueryConditionCommand implements Serializable {

	/** The Constant serialVersionUID. */
	private static final long serialVersionUID = 3047451074419502149L;

	private String code;
	private List<String> codeList;
	private String title;
	private String subTitle;
	private String sketch;
	private String description;
	private String shopName;
	private String industryName;
	private Integer lifecycle;
	private Date createTime;
	
	/**
	 * 判断是否有添加颜色查询条件
	 */
	private Boolean isSpread;
	
	/**
	 * 区间类型的搜索：如价格返回值规范为
	 * list_price:[0 TO 100]
	 * 如果仅传入起始与结束值，则需在set进属性之前通过FilterUtil的paramConverToArea方法进行转换
	 */
	private String list_price;
	private String sale_price;
	
	
	private List<String> ChannelId;
	private String seoKeywords;
	private String seoDescription;
	private String seoTitle;
	private String salesCount;
	private String itemCount;
	private String viewCount;
	private String rankavg;
	private Date activeBeginTime;
	private Date activeEndTime;

	/** 为空Solr不分组，不为空则按该字段对Solr查询的索引分组 **/
	private List<String> groupNames;

	private List<String> facetNames;

	/**  **/
	private List<String> groupSorts;

	/**  **/
	private List<String> facetSorts;
	
	/**  **/
	private String tag;

	/** 搜索字符. */
	private String keyword;
	

	/** isCustomOrder 是否 使用自定义排序 */
	private Boolean isCustomOrder = true;

	/** 是否搜索 贴了 nosearch 标签的商品 ，默认不搜索 */
	private Boolean isSearchNoSearchLabelSku = false;

	/**
	 * 动态条件Map 格式如下 key name value abc key name value abc,def
	 * **/
	private Map<Long, List<Long>> dynamicConditionMap;
	
	private Map<Long, List<String>> dynamicConditionMapForString;
	
	private Map<Long, List<Long>> dynamicConditionWithOutSearchMap;
	
	private Map<Long, List<String>> dynamicConditionWithOutSearchMapForString;
		
	private Map<String, String> category_name;
	
	private Map<String,List<String>> excludeMap;
	
	/**
	 * 是否全场可见
	 */
	private String allDisplay;
	/**
	 * 可见人群
	 */
	private List<String> visiblePersons;

	public QueryConditionCommand() {
	}

	/**
	 * Gets the 搜索字符.
	 * 
	 * @return the keyword
	 */
	public String getKeyword() {
		return keyword;
	}

	/**
	 * Sets the 搜索字符.
	 * 
	 * @param keyword
	 *            the keyword to set
	 */
	public void setKeyword(String keyword) {
		this.keyword = keyword;
	}


	public Boolean getIsCustomOrder() {
		return isCustomOrder;
	}

	public void setIsCustomOrder(Boolean isCustomOrder) {
		this.isCustomOrder = isCustomOrder;
	}


	/**
	 * 是否搜索 贴了 nosearch 标签的商品 ，默认不搜索
	 * 
	 * @return the isSearchNoSearchLabelSku
	 */
	public Boolean getIsSearchNoSearchLabelSku() {
		return isSearchNoSearchLabelSku;
	}

	/**
	 * 是否搜索 贴了 nosearch 标签的商品 ，默认不搜索
	 * 
	 * @param isSearchNoSearchLabelSku
	 *            the isSearchNoSearchLabelSku to set
	 */
	public void setIsSearchNoSearchLabelSku(Boolean isSearchNoSearchLabelSku) {
		this.isSearchNoSearchLabelSku = isSearchNoSearchLabelSku;
	}

	public List<String> getGroupNames() {
		return groupNames;
	}

	public void setGroupNames(List<String> groupNames) {
		this.groupNames = groupNames;
	}

	public List<String> getFacetNames() {
		return facetNames;
	}

	public void setFacetNames(List<String> facetNames) {
		this.facetNames = facetNames;
	}

	public List<String> getGroupSorts() {
		return groupSorts;
	}

	public void setGroupSorts(List<String> groupSorts) {
		this.groupSorts = groupSorts;
	}

	public List<String> getFacetSorts() {
		return facetSorts;
	}

	public void setFacetSorts(List<String> facetSorts) {
		this.facetSorts = facetSorts;
	}

	public Map<Long, List<Long>> getDynamicConditionMap() {
		return dynamicConditionMap;
	}

	public void setDynamicConditionMap(Map<Long, List<Long>> dynamicConditionMap) {
		this.dynamicConditionMap = dynamicConditionMap;
	}

	public Map<String, String> getCategory_name() {
		return category_name;
	}

	public void setCategory_name(Map<String, String> category_name) {
		this.category_name = category_name;
	}

	public String getCode() {
		return code;
	}

	public void setCode(String code) {
		this.code = code;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public String getSubTitle() {
		return subTitle;
	}

	public void setSubTitle(String subTitle) {
		this.subTitle = subTitle;
	}

	public String getSketch() {
		return sketch;
	}

	public void setSketch(String sketch) {
		this.sketch = sketch;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public String getShopName() {
		return shopName;
	}

	public void setShopName(String shopName) {
		this.shopName = shopName;
	}

	public String getIndustryName() {
		return industryName;
	}

	public void setIndustryName(String industryName) {
		this.industryName = industryName;
	}

	public Integer getLifecycle() {
		return lifecycle;
	}

	public void setLifecycle(Integer lifecycle) {
		this.lifecycle = lifecycle;
	}

	public Date getCreateTime() {
		return createTime;
	}

	public void setCreateTime(Date createTime) {
		this.createTime = createTime;
	}

	public String getList_price() {
		return list_price;
	}

	public void setList_price(String list_price) {
		this.list_price = list_price;
	}

	public String getSale_price() {
		return sale_price;
	}

	public void setSale_price(String sale_price) {
		this.sale_price = sale_price;
	}

	public List<String> getChannelId() {
		return ChannelId;
	}

	public void setChannelId(List<String> channelId) {
		ChannelId = channelId;
	}

	public String getSeoKeywords() {
		return seoKeywords;
	}

	public void setSeoKeywords(String seoKeywords) {
		this.seoKeywords = seoKeywords;
	}

	public String getSeoDescription() {
		return seoDescription;
	}

	public void setSeoDescription(String seoDescription) {
		this.seoDescription = seoDescription;
	}

	public String getSeoTitle() {
		return seoTitle;
	}

	public void setSeoTitle(String seoTitle) {
		this.seoTitle = seoTitle;
	}

	public String getSalesCount() {
		return salesCount;
	}

	public void setSalesCount(String salesCount) {
		this.salesCount = salesCount;
	}

	public String getItemCount() {
		return itemCount;
	}

	public void setItemCount(String itemCount) {
		this.itemCount = itemCount;
	}

	public String getViewCount() {
		return viewCount;
	}

	public void setViewCount(String viewCount) {
		this.viewCount = viewCount;
	}

	public String getRankavg() {
		return rankavg;
	}

	public void setRankavg(String rankavg) {
		this.rankavg = rankavg;
	}

	public Date getActiveBeginTime() {
		return activeBeginTime;
	}

	public void setActiveBeginTime(Date activeBeginTime) {
		this.activeBeginTime = activeBeginTime;
	}

	public Date getActiveEndTime() {
		return activeEndTime;
	}

	public void setActiveEndTime(Date activeEndTime) {
		this.activeEndTime = activeEndTime;
	}

	public String getTag() {
		return tag;
	}

	public void setTag(String tag) {
		this.tag = tag;
	}

	public Boolean getIsSpread() {
		return isSpread;
	}

	public void setIsSpread(Boolean isSpread) {
		this.isSpread = isSpread;
	}

	public List<String> getCodeList() {
		return codeList;
	}

	public void setCodeList(List<String> codeList) {
		this.codeList = codeList;
	}

	public Map<Long, List<String>> getDynamicConditionMapForString() {
		return dynamicConditionMapForString;
	}

	public void setDynamicConditionMapForString(
			Map<Long, List<String>> dynamicConditionMapForString) {
		this.dynamicConditionMapForString = dynamicConditionMapForString;
	}

	public Map<Long, List<Long>> getDynamicConditionWithOutSearchMap() {
		return dynamicConditionWithOutSearchMap;
	}

	public void setDynamicConditionWithOutSearchMap(
			Map<Long, List<Long>> dynamicConditionWithOutSearchMap) {
		this.dynamicConditionWithOutSearchMap = dynamicConditionWithOutSearchMap;
	}

	public Map<Long, List<String>> getDynamicConditionWithOutSearchMapForString() {
		return dynamicConditionWithOutSearchMapForString;
	}

	public void setDynamicConditionWithOutSearchMapForString(
			Map<Long, List<String>> dynamicConditionWithOutSearchMapForString) {
		this.dynamicConditionWithOutSearchMapForString = dynamicConditionWithOutSearchMapForString;
	}

	public Map<String, List<String>> getExcludeMap() {
		return excludeMap;
	}

	public void setExcludeMap(Map<String, List<String>> excludeMap) {
		this.excludeMap = excludeMap;
	}

	public String getAllDisplay() {
		return allDisplay;
	}

	public void setAllDisplay(String allDisplay) {
		this.allDisplay = allDisplay;
	}

	public List<String> getVisiblePersons() {
		return visiblePersons;
	}

	public void setVisiblePersons(List<String> visiblePersons) {
		this.visiblePersons = visiblePersons;
	}
	
}
