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

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

import com.baozun.nebula.command.Command;

/**
 * 商品的基本信息
 * 
 * @author chenguang.zhou
 * @date 2014-3-7 上午09:54:11
 */
public class ItemBaseCommand implements Command {

	/**
	 * 
	 */
	private static final long				serialVersionUID	= -2256511460208627061L;

	/** PK. */
	private Long							id;

	/**
	 * 商品编码
	 */
	private String							code;

	/**
	 * 行业id
	 */
	private Long							industryId;

	/**
	 * 是否增加分类
	 */
	private Boolean							isaddcategory;

	private Boolean							isaddtag;
	/**
	 * 商品
	 */
	private Long							itemId;

	/**
	 * 语言
	 */
	private String							language;

	/**
	 * 商品名称
	 */
	private String							title;

	/**
	 * 副标题
	 */
	private String							subTitle;

	/** 商品概述 . */
	private String							sketch;

	/** 商品详细描述 */
	private String							description;

	/**
	 * 销售价
	 */
	private BigDecimal						salePrice;

	/**
	 * 吊牌价(原单价)
	 */
	private BigDecimal						listPrice;

	/** 创建时间. */
	private Date							createTime;

	/**
	 * 修改时间
	 */
	private Date							modifyTime;

	/**
	 * version
	 */
	private Date							version;

	/**
	 * 0代表赠品 1代表主卖品
	 */
	private Integer							type;

	/**
	 * 生命周期
	 */
	private Integer							lifecycle;

	/**
	 * seo搜索描述
	 */
	private String							seoDescription;

	/**
	 * seo搜索关键字
	 */
	private String							seoKeywords;

	/**
	 * seoTitle
	 */
	private String							seoTitle;

	/** 商品可用量. */
	private Integer							availableQty;

	/** 分组style */
	private String							style;

	/**
	 * 商品激活开始时间(用于定时上架的商品)
	 */
	private Date							activeBeginTime;

	/**
	 * 商品激活结束时间
	 * 
	 * @return
	 */
	private Date							activeEndTime;

	private List<DynamicPropertyCommand>	salePropCommandList;

	private List<SkuCommand>				skuCommandList;

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getCode() {
		return code;
	}

	public void setCode(String code) {
		this.code = code;
	}

	public Long getItemId() {
		return itemId;
	}

	public void setItemId(Long itemId) {
		this.itemId = itemId;
	}

	public String getLanguage() {
		return language;
	}

	public void setLanguage(String language) {
		this.language = language;
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

	public Integer getType() {
		return type;
	}

	public void setType(Integer type) {
		this.type = type;
	}

	public String getSeoDescription() {
		return seoDescription;
	}

	public void setSeoDescription(String seoDescription) {
		this.seoDescription = seoDescription;
	}

	public String getSeoKeywords() {
		return seoKeywords;
	}

	public void setSeoKeywords(String seoKeywords) {
		this.seoKeywords = seoKeywords;
	}

	public String getSeoTitle() {
		return seoTitle;
	}

	public void setSeoTitle(String seoTitle) {
		this.seoTitle = seoTitle;
	}

	public Integer getAvailableQty() {
		return availableQty;
	}

	public void setAvailableQty(Integer availableQty) {
		this.availableQty = availableQty;
	}

	public Long getIndustryId() {
		return industryId;
	}

	public void setIndustryId(Long industryId) {
		this.industryId = industryId;
	}

	public Boolean getIsaddcategory() {
		return isaddcategory;
	}

	public void setIsaddcategory(Boolean isaddcategory) {
		this.isaddcategory = isaddcategory;
	}

	public Boolean getIsaddtag() {
		return isaddtag;
	}

	public void setIsaddtag(Boolean isaddtag) {
		this.isaddtag = isaddtag;
	}

	public Integer getLifecycle() {
		return lifecycle;
	}

	public void setLifecycle(Integer lifecycle) {
		this.lifecycle = lifecycle;
	}

	public void setSalePropCommandList(List<DynamicPropertyCommand> salePropCommandList) {
		this.salePropCommandList = salePropCommandList;
	}

	public List<DynamicPropertyCommand> getSalePropCommandList() {
		return salePropCommandList;
	}

	public void setSkuCommandList(List<SkuCommand> skuCommandList) {
		this.skuCommandList = skuCommandList;
	}

	public List<SkuCommand> getSkuCommandList() {
		return skuCommandList;
	}

	public String getStyle() {
		return style;
	}

	public void setStyle(String style) {
		this.style = style;
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

}
