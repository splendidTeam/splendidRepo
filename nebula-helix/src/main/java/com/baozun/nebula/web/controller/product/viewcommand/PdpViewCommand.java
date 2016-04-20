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
	private List<BreadcrumbsViewCommand> breadcrumbs;
	
	private ItemBaseInfoViewCommand itemBaseInfo;
	
	private ItemPropertyViewCommand itemProperty;
	
	private List<ItemCategoryViewCommand> categorys;
	
	/** 颜色（或者其他属性，颜色是个统称）切换部分 */
	private List<ItemColorSwatchViewCommand>  itemColorSwatches;

	/** 尺码对照. */
    private String sizeCompareChart;

	public List<BreadcrumbsViewCommand> getBreadcrumbs() {
		return breadcrumbs;
	}

	public void setBreadcrumbs(List<BreadcrumbsViewCommand> breadcrumbs) {
		this.breadcrumbs = breadcrumbs;
	}

	public ItemBaseInfoViewCommand getItemBaseInfo() {
		return itemBaseInfo;
	}

	public void setItemBaseInfo(ItemBaseInfoViewCommand itemBaseInfo) {
		this.itemBaseInfo = itemBaseInfo;
	}

	public ItemPropertyViewCommand getItemProperty() {
		return itemProperty;
	}

	public void setItemProperty(ItemPropertyViewCommand itemProperty) {
		this.itemProperty = itemProperty;
	}

	public List<ItemColorSwatchViewCommand> getItemColorSwitches() {
		return itemColorSwatches;
	}

	public void setItemColorSwitches(
			List<ItemColorSwatchViewCommand> itemColorSwatches) {
		this.itemColorSwatches = itemColorSwatches;
	}

	public String getSizeCompareChart() {
		return sizeCompareChart;
	}

	public void setSizeCompareChart(String sizeCompareChart) {
		this.sizeCompareChart = sizeCompareChart;
	}

	public List<ItemCategoryViewCommand> getCategorys() {
		return categorys;
	}

	public void setCategorys(List<ItemCategoryViewCommand> categorys) {
		this.categorys = categorys;
	}

}
