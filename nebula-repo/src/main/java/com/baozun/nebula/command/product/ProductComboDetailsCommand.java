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
package com.baozun.nebula.command.product;

import java.util.ArrayList;
import java.util.List;

import com.baozun.nebula.command.Command;
import com.baozun.nebula.command.ItemCommand;
import com.baozun.nebula.command.rule.MiniItemAtomCommand;
import com.baozun.nebula.model.product.Category;
import com.baozun.nebula.model.rule.CustomizeFilterClass;

/**
 * ProductComboDetailsCommand
 * 
 * 原子商品筛选器所包含的分类和商品详情
 * @author - 项硕
 */
public class ProductComboDetailsCommand implements Command {

	private static final long serialVersionUID = 2319239638127269792L;

	/** 筛选器ID */
	private Long id;
	
	/** 筛选器名称 */
	private String name;
	
	/** 筛选器类型 */
	private Integer type;

	
	private List<Category> categoryList = new ArrayList<Category>();
	private List<Category> excCategoryList = new ArrayList<Category>();
	private List<ItemCommand> itemList = new ArrayList<ItemCommand>();
	private List<CustomizeFilterClass> customizeFilterClassList = new ArrayList<CustomizeFilterClass>();


	
	/** 筛选器所代表的原子（商品和分类）集合  */
	private List<MiniItemAtomCommand> atomList = new ArrayList<MiniItemAtomCommand>();

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public Integer getType() {
		return type;
	}

	public void setType(Integer type) {
		this.type = type;
	}

	public List<MiniItemAtomCommand> getAtomList() {
		return atomList;
	}

	public void setAtomList(List<MiniItemAtomCommand> atomList) {
		this.atomList = atomList;
	}

	public List<ItemCommand> getItemList() {
		return itemList;
	}
	public void setItemList(List<ItemCommand> itemList) {
		this.itemList = itemList;
	}
	public List<Category> getExcCategoryList() {
		return excCategoryList;
	}
	public void setExcCategoryList(List<Category> excCategoryList) {
		this.excCategoryList = excCategoryList;
	}

	public List<Category> getCategoryList() {
		return categoryList;
	}

	public void setCategoryList(List<Category> categoryList) {
		this.categoryList = categoryList;
	}

	public List<CustomizeFilterClass> getCustomizeFilterClassList() {
		return customizeFilterClassList;
	}

	public void setCustomizeFilterClassList(List<CustomizeFilterClass> customizeFilterClassList) {
		this.customizeFilterClassList = customizeFilterClassList;
	}
}
