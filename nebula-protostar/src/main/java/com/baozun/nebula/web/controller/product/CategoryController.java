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
 */
package com.baozun.nebula.web.controller.product;

import java.util.List;

import loxia.dao.Sort;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.baozun.nebula.command.product.CategoryCommand;
import com.baozun.nebula.enumeration.TreeMoveType;
import com.baozun.nebula.manager.product.CategoryManager;
import com.baozun.nebula.manager.product.ItemCategoryManager;
import com.baozun.nebula.model.product.Category;
import com.baozun.nebula.model.product.ItemCategory;
import com.baozun.nebula.web.bind.I18nCommand;
import com.baozun.nebula.web.command.BackWarnEntity;
import com.baozun.nebula.web.controller.BaseController;

/**
 * 商品分类管理controller
 * 
 * @author yi.huang
 * @author <a href="mailto:venusdrogon@163.com">金鑫</a>
 * @date 2013-6-20 上午11:09:00
 */
@Controller
public class CategoryController extends BaseController {

	@SuppressWarnings("unused")
	private static final Logger	log	= LoggerFactory.getLogger(CategoryController.class);

	@Autowired
	private CategoryManager		categoryManager;

	@Autowired
	private ItemCategoryManager	itemCategoryManager;

	/**
	 * 商品分类管理页面
	 * 
	 * @param model
	 * @param request
	 * @param response
	 * @return
	 */
	@RequestMapping("/product/category/manager.htm")
	public String categoryManager(Model model) {

		// 顺序 ,一般先有父 再有 子
		Sort[] sorts = Sort.parse("PARENT_ID asc,sort_no asc");
		List<Category> categoryList = categoryManager.findEnableCategoryList(sorts);
		model.addAttribute("categoryList", categoryList);

		return "product/category/category-manager";
	}

	/**
	 * 根据商品分类id在逻辑上删除它
	 * 
	 * @param id
	 *            要删除的商品分类的id
	 */
	@ResponseBody
	@RequestMapping(value = { "/product/category/removeCategory.json" }, method = RequestMethod.POST, headers = HEADER_WITH_AJAX_SPRINGMVC)
	public void removeCategoryById(@RequestParam(value = "id", required = true) Long id) {
		categoryManager.removeCategoryById(id);
	}

	/**
	 * 修改某个商品分类的信息
	 * 
	 * @param id
	 *            商品分类id
	 * @param newCode
	 *            商品分类编码
	 * @param name
	 *            商品分类名称
	 */
	@ResponseBody
	@RequestMapping(value = {"/i18n/product/category/updateCategory.json" }, method = RequestMethod.POST, headers = HEADER_WITH_AJAX_SPRINGMVC)
	public void updateCategoryI18n(@I18nCommand CategoryCommand categoryCommand) {
		
		categoryManager.updateCategory(categoryCommand);
	}
	
	@ResponseBody
	@RequestMapping(value = { "/product/category/updateCategory.json" }, method = RequestMethod.POST, headers = HEADER_WITH_AJAX_SPRINGMVC)
	public void updateCategory(@RequestParam(value = "id", required = true) Long id,
			@RequestParam(value = "newCode", required = true) String newCode,
			@RequestParam(value = "name", required = true) String name) {

		categoryManager.updateCategory(id, newCode, name);
	}

	/**
	 * 添加一个子商品分类
	 * 
	 * @param parentId
	 *            父级分类.父节点Id
	 * @param code
	 *            添加商品分类编码
	 * @param name
	 *            分类名称
	 * @return
	 */
	@ResponseBody
	@RequestMapping(value = { "/product/category/addLeafCategory.json" }, method = RequestMethod.POST, headers = HEADER_WITH_AJAX_SPRINGMVC)
	public Category addLeafCategory(@RequestParam(value = "parentId", required = true) Long parentId,
			@RequestParam(value = "code", required = true) String code,
			@RequestParam(value = "name", required = true) String name) {

		categoryManager.addLeafCategory(parentId, code, name);

		// 插入成功 ,就 将 新插入的 category 返回,以便 web端取到 需要的数据
		Category category = categoryManager.findCategoryByCode(code);
		return category;
	}
	@ResponseBody
	@RequestMapping(value = { "/i18n/product/category/addLeafCategory.json" }, method = RequestMethod.POST, headers = HEADER_WITH_AJAX_SPRINGMVC)
	public Category addLeafCategoryI18n(@I18nCommand CategoryCommand categoryCommand) {

		categoryManager.addLeafCategory(categoryCommand);
		// 插入成功 ,就 将 新插入的 category 返回,以便 web端取到 需要的数据
		Category category = categoryManager.findCategoryByCode(categoryCommand.getCode());
		return category;
	}
	/**
	 * 插入节点
	 * 
	 * @param selectCategoryId
	 *            选中的category id
	 * @param code
	 *            添加商品分类编码
	 * @param name
	 *            分类名称
	 * @return
	 */
	@ResponseBody
	@RequestMapping(value = { "/product/category/insertSiblingCategory.json" }, method = RequestMethod.POST, headers = HEADER_WITH_AJAX_SPRINGMVC)
	public Category insertSiblingCategory(
			@RequestParam(value = "selectCategoryId", required = true) Long selectCategoryId,
			@RequestParam(value = "code", required = true) String code,
			@RequestParam(value = "name", required = true) String name) {

		categoryManager.insertSiblingCategory(selectCategoryId, code, name);

		// 插入成功 ,就 将 新插入的 category 返回,以便 web端取到 需要的数据
		Category category = categoryManager.findCategoryByCode(code);
		return category;
	}
	
	@ResponseBody
	@RequestMapping(value = { "/i18n/product/category/insertSiblingCategory.json" }, method = RequestMethod.POST, headers = HEADER_WITH_AJAX_SPRINGMVC)
	public Category insertSiblingCategoryI18n(
			@RequestParam(value = "selectCategoryId", required = true) Long selectCategoryId,
			@I18nCommand CategoryCommand categoryCommand) {

		categoryManager.insertSiblingCategory(selectCategoryId, categoryCommand);
		// 插入成功 ,就 将 新插入的 category 返回,以便 web端取到 需要的数据
		Category category = categoryManager.findCategoryByCode(categoryCommand.getCode());
		return category;
	}
	

	/**
	 * 拖拽分类
	 * 
	 * @param selectCategoryId
	 *            选择的分类id
	 * @param targetCategoryId
	 *            目标分类id
	 * @param moveType
	 *            指定移动到目标节点的相对位置 "inner"：成为子节点，"prev"：成为同级前一个节点，"next"：成为同级后一个节点
	 * @return
	 */
	@ResponseBody
	@RequestMapping(value = { "/product/category/dropCategory.json" }, method = RequestMethod.POST, headers = HEADER_WITH_AJAX_SPRINGMVC)
	public BackWarnEntity dropCategory(
			@RequestParam(value = "selectCategoryId", required = true) Long selectCategoryId,
			@RequestParam(value = "targetCategoryId", required = true) Long targetCategoryId,
			@RequestParam(value = "moveType", required = true) String moveType) {

		boolean isSuccess = true;
		// Integer errorCode = null;
		// String description = null;

		TreeMoveType treeMoveType = TreeMoveType.valueOf(moveType.toUpperCase());
		categoryManager.dropCategory(selectCategoryId, targetCategoryId, treeMoveType);

		// ******************************************************************************
		BackWarnEntity backWarnEntity = new BackWarnEntity();
		backWarnEntity.setIsSuccess(isSuccess);

		// 插入成功 ,就 将 新插入的 category 返回,以便 web端取到 需要的数据
		Category category = categoryManager.findCategoryById(selectCategoryId);
		backWarnEntity.setDescription(category);
		return backWarnEntity;
	}

	/**
	 * 通过分类Id查询分类与商品的关联关系
	 * 
	 * @param id
	 *		分类id
	 * @return
	 */
	@RequestMapping("/product/category/findItemCategoryListByCatgoryId.json")
	@ResponseBody
	public Object findItemCategoryListByCategoryId(@RequestParam("id") Long categoryId) {
		List<ItemCategory> itemCategoryList = itemCategoryManager.findItemCategoryListByCategoryId(categoryId);
		return  itemCategoryList;
	}
	
	/**
	 * 通过分类Id查询分类与商品的关联关系
	 * 
	 * @param id
	 *		分类id
	 * @return
	 */
	@RequestMapping("/i18n/category/findCategoryLangByCatgoryId.json")
	@ResponseBody
	public CategoryCommand findCategoryLangByCategoryId(Long categoryId) {
		CategoryCommand categoryCommand = categoryManager.findCategoryLangByCategoryId(categoryId);
		return  categoryCommand;
	}
}
