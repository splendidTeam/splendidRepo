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

/**
 * 商品的分类信息
 * <p>
 * 一个商品会对应多个分类，所以一个商品会有多个分类树。</br>
 * 一个商品至少有一个默认分类，所以一个商品至少有一个默认分类树。</br>
 * 在构造商品的分类树列表时，默认把默认分类树作为列表的第一个元素。
 * </p>
 *
 */
public class ItemCategoryViewCommand extends BaseViewCommand {
	
	private static final long serialVersionUID = -2754059978783410352L;
	
	/** 分类 */
	private Long categoryId;
	
	/** 商品分类编码. */
	private String code;

	/** 分类名称. */
	private String name;
	
	/**
	 * 是否默认分类
	 * pdp获取分类时，默认以isDefault排序
	 * 将默认分类排在第一位，简化获取默认分类时的循环次数
	 * 因为pdp页面对于分类排序不敏感
	 */
	private Boolean isDefault;
	
	/**下属的子分类 .*/
	List<ItemCategoryViewCommand> subItemCategories;

	public Long getCategoryId() {
		return categoryId;
	}

	public void setCategoryId(Long categoryId) {
		this.categoryId = categoryId;
	}

	public String getCode() {
		return code;
	}

	public void setCode(String code) {
		this.code = code;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public Boolean getIsDefault() {
		return isDefault;
	}

	public void setIsDefault(Boolean isDefault) {
		this.isDefault = isDefault;
	}

	public List<ItemCategoryViewCommand> getSubItemCategories() {
		return subItemCategories;
	}

	public void setSubItemCategories(List<ItemCategoryViewCommand> subItemCategories) {
		this.subItemCategories = subItemCategories;
	}
	
}
