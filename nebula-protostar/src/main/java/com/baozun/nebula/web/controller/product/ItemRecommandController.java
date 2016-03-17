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
 */
package com.baozun.nebula.web.controller.product;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import loxia.dao.Pagination;
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

import com.baozun.nebula.command.ItemCommand;
import com.baozun.nebula.command.product.RecommandItemCommand;
import com.baozun.nebula.manager.product.CategoryManager;
import com.baozun.nebula.manager.product.ItemManager;
import com.baozun.nebula.manager.product.RecommandItemManager;
import com.baozun.nebula.manager.system.ChooseOptionManager;
import com.baozun.nebula.model.product.Category;
import com.baozun.nebula.model.product.RecommandItem;
import com.baozun.nebula.model.system.ChooseOption;
import com.baozun.nebula.solr.utils.JsonFormatUtil;
import com.baozun.nebula.utilities.common.Validator;
import com.baozun.nebula.utils.query.bean.QueryBean;
import com.baozun.nebula.web.UserDetails;
import com.baozun.nebula.web.bind.ArrayCommand;
import com.baozun.nebula.web.bind.QueryBeanParam;
import com.baozun.nebula.web.controller.BaseController;

/**
 * 商品推荐Controller
 * 
 * @author chenguang.zhou
 * @date 2014年4月10日 下午2:32:37
 */
@Controller
public class ItemRecommandController extends BaseController {

	@SuppressWarnings("unused")
	private static final Logger		log				= LoggerFactory.getLogger(ItemRecommandController.class);

	@Autowired
	private RecommandItemManager	recommandItemManager;

	@Autowired
	private CategoryManager			categoryManager;

	@Autowired
	private ItemManager				itemManager;

	@Autowired
	private ChooseOptionManager		chooseOptionManager;

	private final static String		RECOMMAND_PARAM	= "RECOMMAND_PARAM";

	/**
	 * 公共推荐商品管理页面
	 * 
	 * @return
	 */
	@RequestMapping("/system/publicRecommandManager.htm")
	public String publicRecommandManagerPage(Model model) {
		// 分类列表
		Sort[] sorts = Sort.parse("parent_id asc,sort_no asc");
		List<Category> categoryList = categoryManager.findEnableCategoryList(sorts);
		model.addAttribute("categoryJson", processZtreeData(categoryList));
		List<ChooseOption> recommandParamList = chooseOptionManager
				.findEffectChooseOptionListByGroupCode(RECOMMAND_PARAM);
		model.addAttribute("recommandParamJson", JsonFormatUtil.format(recommandParamList));

		return "/product/recommand/public-recommand-list";
	}

	/**
	 * 分类推荐商品管理页面
	 * 
	 * @return
	 */
	@RequestMapping("/system/categoryRecommandManager.htm")
	public String categoryRecommandManagerPage(Model model) {
		// 分类列表
		Sort[] sorts = Sort.parse("parent_id asc,sort_no asc");
		List<Category> categoryList = categoryManager.findEnableCategoryList(sorts);
		model.addAttribute("categoryJson", processZtreeData(categoryList));
		return "/product/recommand/category-recommand-list";
	}

	/**
	 * 商品搭配管理页面
	 * 
	 * @return
	 */
	@RequestMapping("/system/itemRecommandManager.htm")
	public String itemRecommandManagerPage(Model model) {
		// 分类列表
		Sort[] sorts = Sort.parse("parent_id asc,sort_no asc");
		List<Category> categoryList = categoryManager.findEnableCategoryList(sorts);
		model.addAttribute("categoryJson", processZtreeData(categoryList));
		return "/product/recommand/item-recommand-list";
	}

	/**
	 * 查询推荐商品
	 * 
	 * @param model
	 * @param queryBean
	 * @return
	 */
	@RequestMapping("/recommand/recommanditemList.json")
	@ResponseBody
	public Object findRecommandItemListJson(@RequestParam("type") Integer type,
			@RequestParam(value = "param", required = false) Long param) {
		Map<String, Object> paramMap = new HashMap<String, Object>();
		if (Validator.isNotNullOrEmpty(type)) {
			paramMap.put("type", type);
		}
		if (Validator.isNotNullOrEmpty(param)) {
			paramMap.put("param", param);
		}
		List<RecommandItemCommand> recommandItemCommandList = recommandItemManager.findRecommandItemListByParaMap(paramMap);
		
		return recommandItemCommandList;
	}

	/**
	 * 新建推荐
	 * 
	 * @param model
	 * @return
	 */
	@RequestMapping("/recommand/createRecommandItem.htm")
	public String createRecommandItem(Model model) {

		// 分类列表
		Sort[] sorts = Sort.parse("parent_id asc,sort_no asc");
		List<Category> categoryList = categoryManager.findEnableCategoryList(sorts);
		model.addAttribute("categoryList", categoryList);
		return "/product/item/recommand-item-add";
	}

	/**
	 * 修改推荐
	 * 
	 * @param model
	 * @return
	 */
	@RequestMapping("/recommand/updateRecommandItem.htm")
	public String updateRecommandItem(Model model, @RequestParam("id") Long id) {
		RecommandItemCommand recommandItemCommand = recommandItemManager.findRecommandItemById(id);
		model.addAttribute("recommandItemCommand", recommandItemCommand);
		// 分类列表
		Sort[] sorts = Sort.parse("parent_id asc,sort_no asc");
		List<Category> categoryList = categoryManager.findEnableCategoryList(sorts);
		model.addAttribute("categoryList", categoryList);
		model.addAttribute("isUpdate", "true");
		return "/product/item/recommand-item-update";
	}

	/**
	 * 保存推荐
	 * 
	 * @param model
	 * @return
	 */
	@RequestMapping(value = "/recommand/saveRecommandItem.json", method = RequestMethod.POST)
	@ResponseBody
	public Object saveRecommandItem(Model model, @ArrayCommand() RecommandItem[] recommandItems,
			@RequestParam("type") Integer type, @RequestParam("param") Long param) {
		UserDetails userDetails = this.getUserDetails();
		recommandItemManager.createOrUpdateRecItem(recommandItems, userDetails.getUserId(), type, param);
		return SUCCESS;
	}

	/**
	 * 启用或禁用
	 * 
	 * @param id
	 * @return
	 */
	@RequestMapping(value = "/recommand/enabledOrDisenabledRecItem.json", method = RequestMethod.GET)
	@ResponseBody
	public Object enabledOrDisenabledRecItem(@RequestParam("id") Long id, @RequestParam("status") Integer status) {
		recommandItemManager.enabledOrDisenabledRecItem(id, status);
		return SUCCESS;
	}

	/**
	 * 删除(单个)
	 * 
	 * @param id
	 * @return
	 */
	@RequestMapping(value = "/recommand/removeRecommandItem.json", method = RequestMethod.GET)
	@ResponseBody
	public Object removeRecommandItem(@RequestParam("id") Long id) {
		recommandItemManager.removeRecommandItemById(id);
		return SUCCESS;
	}

	/**
	 * 批量删除
	 * 
	 * @param id
	 * @return
	 */
	@RequestMapping(value = "/recommand/batchRemoveRecItem.json", method = RequestMethod.GET)
	@ResponseBody
	public Object batchRemoveRecItem(@ArrayCommand(dataBind = true) Long[] ids) {
		recommandItemManager.removeRecommandItemByIds(ids);
		return SUCCESS;
	}

	/**
	 * 动态获取商品列表
	 * 
	 * @param QueryBean
	 * @param Model
	 * @return
	 */
	@RequestMapping("/recommand/findItemInfoList.json")
	@ResponseBody
	public Pagination<ItemCommand> findItemListJson(Model model, @QueryBeanParam QueryBean queryBean) {

		Sort[] sorts = queryBean.getSorts();

		if (sorts == null || sorts.length == 0) {
			sorts = Sort.parse("tpi.create_time desc");
		}

		Pagination<ItemCommand> page = itemManager.findEffectItemInfoListByQueryMap(queryBean.getPage(), sorts,
				queryBean.getParaMap());
		return page;
	}

	private String processZtreeData(List<Category> categoryList) {
		StringBuffer sb = new StringBuffer("[");
		sb.append("{ id:0,pId:null, name:\"ROOT\",code:\"ROOT\",lifecycle:\"1\", open:true,sortNo:0,drag:true},");
		for (Category category : categoryList) {
			sb.append("{id:");
			sb.append(category.getId());
			sb.append(", pId:");
			sb.append(category.getParentId());
			sb.append(", name:\"");
			sb.append(category.getName());
			sb.append("\", code:\"");
			sb.append(category.getCode());
			sb.append("\", sortNo:");
			sb.append(category.getSortNo());
			sb.append(", open:false, lifecycle:");
			sb.append(category.getLifecycle());
			sb.append("},");
		}
		String result = sb.toString().substring(0, sb.toString().length() - 1);
		result += "]";
		return result;
	}

}
