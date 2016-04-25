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
package com.baozun.nebula.web.controller.product.viewcommand;

import java.util.List;

import com.baozun.nebula.web.controller.BaseViewCommand;

public class PdpViewCommand extends BaseViewCommand {

	private static final long serialVersionUID = -521994888015333539L;

	//TODO 先放在这,后边决定在什么时机获取
	/** 面包屑 */
	private List<BreadcrumbsViewCommand> breadcrumbs;
	
	/** 商品基本信息 */
	private ItemBaseInfoViewCommand baseInfo;
	
	/** 商品属性，包括销售属性和非销售属性 */
	private ItemPropertyViewCommand properties;
	
	/** 颜色（或者其他属性，颜色是个统称）或商品切换部分 */
	private List<ItemColorSwatchViewCommand>  colorSwatches;
	
	/** 商品图片 */
	private List<ItemImageViewCommand> images;
	
	/** sku */
	private List<SkuViewCommand> skus;
	
	/** 价格 */
	private PriceViewCommand price;
	
	/** 商品分类 */
	private List<ItemCategoryViewCommand> categorys;

	/** 尺码对照 */
    private String sizeCompareChart;
    
    /** 商品扩展信息 */
    private ItemExtraViewCommand extra;
    
    /** 商品推荐信息 */
    private List<RelationItemViewCommand> relations;

	public List<BreadcrumbsViewCommand> getBreadcrumbs() {
		return breadcrumbs;
	}

	public void setBreadcrumbs(List<BreadcrumbsViewCommand> breadcrumbs) {
		this.breadcrumbs = breadcrumbs;
	}

	public ItemBaseInfoViewCommand getBaseInfo() {
		return baseInfo;
	}

	public void setBaseInfo(ItemBaseInfoViewCommand baseInfo) {
		this.baseInfo = baseInfo;
	}

	public ItemPropertyViewCommand getProperties() {
		return properties;
	}

	public void setProperties(ItemPropertyViewCommand properties) {
		this.properties = properties;
	}

	public List<ItemColorSwatchViewCommand> getColorSwatches() {
		return colorSwatches;
	}

	public void setColorSwatches(List<ItemColorSwatchViewCommand> colorSwatches) {
		this.colorSwatches = colorSwatches;
	}

	public List<ItemImageViewCommand> getImages() {
		return images;
	}

	public void setImages(List<ItemImageViewCommand> images) {
		this.images = images;
	}

	public List<SkuViewCommand> getSkus() {
		return skus;
	}

	public void setSkus(List<SkuViewCommand> skus) {
		this.skus = skus;
	}

	public PriceViewCommand getPrice() {
		return price;
	}

	public void setPrice(PriceViewCommand price) {
		this.price = price;
	}

	public List<ItemCategoryViewCommand> getCategorys() {
		return categorys;
	}

	public void setCategorys(List<ItemCategoryViewCommand> categorys) {
		this.categorys = categorys;
	}

	public String getSizeCompareChart() {
		return sizeCompareChart;
	}

	public void setSizeCompareChart(String sizeCompareChart) {
		this.sizeCompareChart = sizeCompareChart;
	}

	public ItemExtraViewCommand getExtra() {
		return extra;
	}

	public void setExtra(ItemExtraViewCommand extra) {
		this.extra = extra;
	}

	public List<RelationItemViewCommand> getRelations() {
		return relations;
	}

	public void setRelations(List<RelationItemViewCommand> relations) {
		this.relations = relations;
	}

}
