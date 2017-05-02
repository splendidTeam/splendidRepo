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
package com.baozun.nebula.command.product;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;
import java.util.Map;

import com.baozun.nebula.command.Command;
import com.baozun.nebula.command.i18n.LangProperty;
import com.baozun.nebula.model.product.ItemImage;

/**
 * 扩展商品信息
 * 
 * @author dianchao.song
 */
public class ItemInfoCommand implements Command {

	/**
	 * 
	 */
	private static final long	serialVersionUID	= 2544112989421233359L;

	/** PK. */
	private Long				id;

	private Long				shopId;
	/**
	 * 鍟嗗搧鎻忚堪
	 */
	private LangProperty	   description;

	/**
	 * 商品概述
	 */
	private LangProperty				sketch;

	/**
	 * 琛屼笟Id
	 */
	private String				industryId;

	/**
	 * 鍟嗗搧缂栫爜
	 */
	private String				code;

	/**
	 * 鍟嗗搧鍚嶇О
	 */
	private LangProperty				title;
	
	/**
	 * 商品副标题
	 */
	private LangProperty 				subTitle;

	/** 鎵�睘琛屼笟 */
	private String				industryName;

	/**
	 * 閿�敭浠�
	 */
	private BigDecimal			salePrice;

	/**
	 * 鍚婄墝浠�鍘熷崟浠�
	 */
	private BigDecimal			listPrice;

	/** 鎵�睘鍒嗙被 */
	private List<String>		categoryNames;

	/** 鎵�睘鏍囩 */
	private List<String>		tagNames;

	/** 鍒涘缓鏃堕棿. */
	private Date				createTime;

	/** 淇敼鏃堕棿 */
	private Date				modifyTime;

	/** version. */
	private Date				version;

	/** 闄勫姞灞炴� */
	private Map<String, String>	additionalAttributes;

	/**
	 * 涓婃灦鏃堕棿
	 */
	private Date				listTime;

	/**
	 * 鐢熷懡鍛ㄦ湡
	 */
	private Integer				lifecycle;

	/**
	 * 灞炴�鍊糺son
	 */
	private String				jsonSku;

	/**
	 * 鍟嗗搧寮�娲昏穬鏃堕棿
	 * 
	 * @return
	 */
	private Date				activeBeginTime;

	/**
	 * 鍟嗗搧缁堟娲昏穬鏃堕棿
	 * 
	 * @return
	 */
	private Date				activeEndTime;

	/**
	 * 娓犻亾Id
	 * 
	 * @return channels
	 */
	private List<String>		channels;

	/**
	 * 閿�噺
	 * 
	 * @return
	 */
	private Integer				salesCount;

	/**
	 * 分类是否是商品的默认分类
	 */
	private Boolean				isDefault;

	/**
	 * seo搜索描述
	 */
	private LangProperty				seoDescription;

	/**
	 * seo title
	 */
	private LangProperty				seoTitle;

	private String				style;

	/**
	 * seo搜索关键字
	 */
	private LangProperty				seoKeywords;

	private String				picUrl;

	private String				itemUrl;
	
	private String              suitItem;

	private List<ItemImage>		itemImageList;

	/**
	 * 0非卖品，赠品
	 * 1或者null为，正常商品
	 */
	private Integer				type;
	
	/**
	 * 商品类型
	 * 
	 * @see com.baozun.nebula.model.product.Item#type
	 */
	private Integer				itemType;
	
	/**
	 * 商品图片个数
	 */
	private Integer				imageCount;

	/**
	 * 商品评分
	 */
	private Float				rankavg;

	private Long lastSelectPropertyId;
	private Long lastSelectPropertyValueId;
	/** 1 组合商品的组合extentionCode. 在数据库中以|分割 如[8,4,6]|[1,,4,5] */
	private String groupCode;

	public String getGroupCode() {
		return groupCode;
	}
	public void setGroupCode(String groupCode) {
		this.groupCode = groupCode;
	}
	public BigDecimal getSalePrice() {
		return salePrice;
	}

	public void setSalePrice(BigDecimal salePrice) {
		this.salePrice = salePrice;
	}

	public BigDecimal getListPrice() {
		return listPrice;
	}

	public void setListPrice(BigDecimal listPrice) {
		this.listPrice = listPrice;
	}

	public String getJsonSku() {
		return jsonSku;
	}

	public void setJsonSku(String jsonSku) {
		this.jsonSku = jsonSku;
	}

	public List<String> getCategoryNames() {
		return categoryNames;
	}

	public void setCategoryNames(List<String> categoryNames) {
		this.categoryNames = categoryNames;
	}

	public Integer getLifecycle() {
		return lifecycle;
	}

	public void setLifecycle(Integer lifecycle) {
		this.lifecycle = lifecycle;
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getCode() {
		return code;
	}

	public LangProperty getDescription() {
		return description;
	}

	public void setDescription(LangProperty description) {
		this.description = description;
	}

	public String getIndustryId() {
		return industryId;
	}

	public void setIndustryId(String industryId) {
		this.industryId = industryId;
	}

	public void setCode(String code) {
		this.code = code;
	}

	public LangProperty getTitle() {
		return title;
	}

	public void setTitle(LangProperty title) {
		this.title = title;
	}

	public String getIndustryName() {
		return industryName;
	}

	public void setIndustryName(String industryName) {
		this.industryName = industryName;
	}

	public Date getCreateTime() {
		return createTime;
	}

	public void setCreateTime(Date createTime) {
		this.createTime = createTime;
	}

	public Date getModifyTime() {
		return modifyTime;
	}

	public void setModifyTime(Date modifyTime) {
		this.modifyTime = modifyTime;
	}

	public Date getVersion() {
		return version;
	}

	public void setVersion(Date version) {
		this.version = version;
	}

	public Date getListTime() {
		return listTime;
	}

	public void setListTime(Date listTime) {
		this.listTime = listTime;
	}

	public List<String> getTagNames() {
		return tagNames;
	}

	public void setTagNames(List<String> tagNames) {
		this.tagNames = tagNames;
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

	public List<String> getChannels() {
		return channels;
	}

	public void setChannels(List<String> channels) {
		this.channels = channels;
	}

	public Map<String, String> getAdditionalAttributes() {
		return additionalAttributes;
	}

	public void setAdditionalAttributes(Map<String, String> additionalAttributes) {
		this.additionalAttributes = additionalAttributes;
	}

	public LangProperty getSketch() {
		return sketch;
	}

	public void setSketch(LangProperty sketch) {
		this.sketch = sketch;
	}

	public Boolean getIsDefault() {
		return isDefault;
	}

	public void setIsDefault(Boolean isDefault) {
		this.isDefault = isDefault;
	}

	public LangProperty getSeoDescription() {
		return seoDescription;
	}

	public void setSeoDescription(LangProperty seoDescription) {
		this.seoDescription = seoDescription;
	}

	public LangProperty getSeoTitle() {
		return seoTitle;
	}

	public void setSeoTitle(LangProperty seoTitle) {
		this.seoTitle = seoTitle;
	}

	public LangProperty getSeoKeywords() {
		return seoKeywords;
	}

	public void setSeoKeywords(LangProperty seoKeywords) {
		this.seoKeywords = seoKeywords;
	}

	public void setShopId(Long shopId) {
		this.shopId = shopId;
	}

	public Long getShopId() {
		return shopId;
	}

	public String getPicUrl() {
		return picUrl;
	}

	public void setPicUrl(String picUrl) {
		this.picUrl = picUrl;
	}

	public void setItemUrl(String itemUrl) {
		this.itemUrl = itemUrl;
	}

	public String getItemUrl() {
		return itemUrl;
	}

	public void setRankavg(Float rankavg) {
		this.rankavg = rankavg;
	}

	public Float getRankavg() {
		return rankavg;
	}

	public Integer getSalesCount() {
		return salesCount;
	}

	public void setSalesCount(Integer salesCount) {
		this.salesCount = salesCount;
	}

	public List<ItemImage> getItemImageList() {
		return itemImageList;
	}

	public void setItemImageList(List<ItemImage> itemImageList) {
		this.itemImageList = itemImageList;
	}

	public void setStyle(String style) {
		this.style = style;
	}

	public String getStyle() {
		return style;
	}

	public Integer getImageCount() {
		return imageCount;
	}

	public void setImageCount(Integer imageCount) {
		this.imageCount = imageCount;
	}

	public String getSuitItem() {
		return suitItem;
	}

	public void setSuitItem(String suitItem) {
		this.suitItem = suitItem;
	}

	public LangProperty getSubTitle() {
		return subTitle;
	}

	public void setSubTitle(LangProperty subTitle) {
		this.subTitle = subTitle;
	}

	public Integer getType() {
		return type;
	}

	public void setType(Integer type) {
		this.type = type;
	}

	public Long getLastSelectPropertyId() {
		return lastSelectPropertyId;
	}

	public void setLastSelectPropertyId(Long lastSelectPropertyId) {
		this.lastSelectPropertyId = lastSelectPropertyId;
	}

	public Long getLastSelectPropertyValueId() {
		return lastSelectPropertyValueId;
	}

	public void setLastSelectPropertyValueId(Long lastSelectPropertyValueId) {
		this.lastSelectPropertyValueId = lastSelectPropertyValueId;
	}

	public Integer getItemType() {
		return itemType;
	}

	public void setItemType(Integer itemType) {
		this.itemType = itemType;
	}
	
	

}
